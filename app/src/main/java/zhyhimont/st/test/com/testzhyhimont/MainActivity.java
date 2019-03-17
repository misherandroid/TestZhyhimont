package zhyhimont.st.test.com.testzhyhimont;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    //Базловая ссылка для GET запроса
    static final String BASE_URL = "http://www.nbrb.by/Services/";
    //List после разбора Get запроса
    private List<Currency> listCurrency;
    //List для получения видимых элементов и их позиции для последующего отображения полученных данных
    private List<CurrencyTable> listSettings;

    //Lists для текущих и будущих-предыдущих курсов валют
    private List<CurrencyFirstData> listCurrencyFirstData = new ArrayList<>();
    private List<CurrencySecondData> listCurrencySecondData = new ArrayList<>();

    //Даты для запроса на сервер
    private String prevDate;
    private String curDate;
    private String nextDate;

    //Даты для отображения на экране
    private String prevDateText;
    private String curDateText;
    private String nextDateText;

    //Класс управления БД
    private AppDatabaseHelper db;

    //Интерфейсы управления таблицами
    private CurrencyTableDAO currencyDAO;
    private CurencyFirstDataDAO currencyFirstDAO;
    private CurrencySecondDataDAO currencySecondDAO;

    //Сам RecyclerView его LayoutManager и Adapter
    private RecyclerView recyclerViewCurrency;
    private LinearLayoutManager layoutManager;
    private CurrencyAdapter adapter;

    //Заглушка для загрузки
    private ProgressBar progressLoad;

    //Отображение сообщения об ошибке загрузки, первой и второй дат отображения курсов валют
    private TextView textError;
    private TextView firstDate;
    private TextView secondDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //получаю кастом ActionBar, чтобы заголовок был в центре
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = findViewById(R.id.action_bar_title);
        title.setText("Курсы валют");

        //Получаю даты для запроса на сервер и отображения на экране в нужных форматах
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MM.dd.yyyy");
        SimpleDateFormat mdformatText = new SimpleDateFormat("dd.MM.yy");

        curDate = mdformat.format(calendar.getTime());
        curDateText = mdformatText.format(calendar.getTime());

        calendar.add(Calendar.DATE, -1);
        prevDate = mdformat.format(calendar.getTime());
        prevDateText = mdformatText.format(calendar.getTime());

        calendar.add(Calendar.DATE, 2);
        nextDate = mdformat.format(calendar.getTime());
        nextDateText = mdformatText.format(calendar.getTime());

        //Инициализирую БД и получаю интерфейсы для управления таблицами
        db = AppDatabaseHelper.getAppDatabase(getApplicationContext());
        currencyDAO = db.currencyTableDao();
        currencyFirstDAO = db.currencyFisrtDataDao();
        currencySecondDAO = db.currencySecondDataDao();

        //Получаю текущие курсы валют и если все удачно, то иду за будущими\предыдущими
        getCurrencys();

        //Настройка RecyclerView
        recyclerViewCurrency = findViewById(R.id.recycler_view_currency);
        recyclerViewCurrency.setHasFixedSize(true);
        recyclerViewCurrency.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        layoutManager = new LinearLayoutManager(this);
        recyclerViewCurrency.setLayoutManager(layoutManager);
        adapter = new CurrencyAdapter(listCurrencyFirstData, listCurrencySecondData);
        recyclerViewCurrency.setAdapter(adapter);

        //Инициализия компонентов
        progressLoad = findViewById(R.id.progress_load);
        textError = findViewById(R.id.error_text);

        firstDate = findViewById(R.id.first_date);
        secondDate = findViewById(R.id.second_date);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_currency, menu);
        MenuItem settings = menu.findItem(R.id.action_settings);
        //Пока не получены курсы, меню настроек скрыто
        if (listCurrencyFirstData.size() <= 0 && listCurrencySecondData.size() <= 0)
            settings.setVisible(false);
        return true;
    }

    //Обработка нажатий кнопок меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    //Получение ткущих курсов
    private void getCurrencys() {
        //Инициализация запроса на сервер
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        CurrencyAPI api = retrofit.create(CurrencyAPI.class);

        Call<Responce> call = api.getCurrencys(curDate);

        call.enqueue(new Callback<Responce>() {

            @Override
            public void onResponse(Call<Responce> call, retrofit2.Response<Responce> response) {
                // В случае успешного выполнения запроса разбираю данных и отправляю их в таблицы
                listCurrency = response.body().currencys;

                currencyFirstDAO.deleteAll();
                for (int i = 0; i < listCurrency.size(); i++) {
                    CurrencyFirstData currData = new CurrencyFirstData(listCurrency.get(i).getCharcode(), listCurrency.get(i).getScale() + " " + listCurrency.get(i).getName(), listCurrency.get(i).getRate());
                    currencyFirstDAO.insert(currData);
                }

                listSettings = currencyDAO.getVisible();
                for (int i = 0; i < listSettings.size(); i++) {
                    listCurrencyFirstData.add(currencyFirstDAO.getSpecial(listSettings.get(i).getChar_name_table()));
                }

                //Если в таблице настроек пусто, то отправляю туда данные.
                if (currencyDAO.getCount() <= 0) {
                    for (int i = 0; i < listCurrency.size(); i++) {
                        CurrencyTable currencyTable = new CurrencyTable();
                        currencyTable.char_name_table = listCurrency.get(i).getCharcode();
                        currencyTable.name_currency_table = listCurrency.get(i).getScale() + " " + listCurrency.get(i).getName();
                        currencyTable.position_currency_table = i;
                        currencyTable.visible_currency_table = 1;

                        currencyDAO.insert(currencyTable);
                    }
                }

                //Переходж к получению будущих курсов
                getNextCurrencys();
            }

            @Override
            public void onFailure(Call<Responce> call, Throwable t) {
                //Если ошибка при получении текущих курсов, то отображаю соответствующее сообщение
                progressLoad.setVisibility(View.GONE);
                textError.setVisibility(View.VISIBLE);
            }
        });
    }

    //Получение курсов на следующий день
    private void getNextCurrencys() {
    //Аналогично функции getCurrency
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        CurrencyAPI api = retrofit.create(CurrencyAPI.class);

        Call<Responce> call = api.getCurrencys(nextDate);

        call.enqueue(new Callback<Responce>() {

            @Override
            public void onResponse(Call<Responce> call, Response<Responce> response) {
                listCurrency.clear();
                listCurrency = response.body().currencys;
                currencySecondDAO.deleteAll();
                for (int i = 0; i < listCurrency.size(); i++) {
                    CurrencySecondData currSecData = new CurrencySecondData(listCurrency.get(i).getCharcode(), listCurrency.get(i).getScale() + " " + listCurrency.get(i).getName(), listCurrency.get(i).getRate());
                    currencySecondDAO.insert(currSecData);
                }

                listSettings = currencyDAO.getVisible();
                for (int i = 0; i < listSettings.size(); i++) {
                    listCurrencySecondData.add(currencySecondDAO.getSpecial(listSettings.get(i).getChar_name_table()));
                }

                adapter.notifyDataSetChanged();

                //Если все получено, то скрываю заглушку загрузки и отображаю данные на экране
                //Также дергаю метод перерисовки меню ActionBar, т.к. данные уже получены
                if (listCurrencyFirstData.size() > 0 && listCurrencySecondData.size() > 0) {
                    progressLoad.setVisibility(View.GONE);
                    recyclerViewCurrency.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    firstDate.setText(curDateText);
                    secondDate.setText(nextDateText);
                }

            }

            @Override
            public void onFailure(Call<Responce> call, Throwable t) {
                //Данные на следующий день не получены - берем данные для предыдущего дня
                getPrevCurrencys();
            }
        });

    }

    //Получение курсов на предыдущий день
    private void getPrevCurrencys() {
        //Аналогично метода getCurrencys()
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        CurrencyAPI api = retrofit.create(CurrencyAPI.class);

        Call<Responce> call = api.getCurrencys(prevDate);

        call.enqueue(new Callback<Responce>() {

            @Override
            public void onResponse(Call<Responce> call, Response<Responce> response) {
                listCurrency.clear();
                listCurrency = response.body().currencys;
                currencySecondDAO.deleteAll();
                for (int i = 0; i < listCurrency.size(); i++) {
                    CurrencySecondData currSecData = new CurrencySecondData(listCurrency.get(i).getCharcode(), listCurrency.get(i).getScale() + " " + listCurrency.get(i).getName(), listCurrency.get(i).getRate());
                    currencySecondDAO.insert(currSecData);
                }

                listSettings = currencyDAO.getVisible();
                for (int i = 0; i < listSettings.size(); i++) {
                    listCurrencySecondData.add(currencySecondDAO.getSpecial(listSettings.get(i).getChar_name_table()));
                }

                adapter.notifyDataSetChanged();

                if (listCurrencyFirstData.size() > 0 && listCurrencySecondData.size() > 0) {
                    progressLoad.setVisibility(View.GONE);
                    recyclerViewCurrency.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    firstDate.setText(prevDateText);
                    secondDate.setText(curDateText);
                }

            }

            @Override
            public void onFailure(Call<Responce> call, Throwable t) {
                //Если данные не получены, выводим сообщение об ошибке
                progressLoad.setVisibility(View.GONE);
                textError.setVisibility(View.VISIBLE);
            }
        });

    }
}
