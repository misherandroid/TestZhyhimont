package zhyhimont.st.test.com.testzhyhimont;

import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    //Отобрадение пунктов натсроек
    private RecyclerView recyclerViewSettings;
    private SettingsAdapter adapter;
    private LinearLayoutManager layoutManager;

    //List настроек
    private List<CurrencyTable> currencyList;

    //Инициализация БД и управление таблицей настроек
    private AppDatabaseHelper db;
    private CurrencyTableDAO currencyDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //получаю кастом ActionBar
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = findViewById(R.id.action_bar_title);
        title.setText("Настройка валют");

        recyclerViewSettings = findViewById(R.id.recycler_view_settings);

        //Инициализация БД
        db = AppDatabaseHelper.getAppDatabase(getApplicationContext());
        currencyDAO = db.currencyTableDao();

        //получение настроек
        currencyList = currencyDAO.getAll();

        //Настройка списка
        recyclerViewSettings.setHasFixedSize(true);
        recyclerViewSettings.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        layoutManager = new LinearLayoutManager(this);
        recyclerViewSettings.setLayoutManager(layoutManager);
        adapter = new SettingsAdapter(currencyList);
        recyclerViewSettings.setAdapter(adapter);

        //Метод вертикального перемещения пунков
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder dragged, RecyclerView.ViewHolder target) {

                int dragged_position = dragged.getAdapterPosition();
                int target_position = target.getAdapterPosition();

                Collections.swap(currencyList, dragged_position, target_position);

                adapter.notifyItemMoved(dragged_position, target_position);

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });
        helper.attachToRecyclerView(recyclerViewSettings);

    }

    //Отображения кнопки сохранения настроек
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_view_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }
        //В случае сохранения настроек чищу предыдущие записи и записываю новые
        if (id == R.id.action_save_settings) {
            //Проверяю, чтобы хотя бы один пункт был видимым ,иначе пользователь больше не сможет
            //зайти в меню настроек и видить он ничего не будет, пока не очистит данные приложения
            boolean unvisibleAll = true;
            for (int i = 0; i < currencyList.size(); i++) {
                if (currencyList.get(i).getVisible_currency_table() == 1) {
                    unvisibleAll = false;
                    break;
                }
            }

            if (unvisibleAll == true)
                Toast.makeText(getApplicationContext(), "Настройки не были сохранены. Оставьте хотя бы один пункт видимым для отображения", Toast.LENGTH_LONG).show();
            else {
                currencyDAO.deleteAll();

                for (int i = 0; i < currencyList.size(); i++) {
                    currencyList.get(i).setPosition_currency_table(i);
                    CurrencyTable currencyTable = currencyList.get(i);
                    currencyDAO.insert(currencyTable);
                }
                Toast.makeText(getApplicationContext(), "Настройки сохранены и вступят в силу после перезапуска приложения", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
