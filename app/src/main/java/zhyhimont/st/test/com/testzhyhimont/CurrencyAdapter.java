package zhyhimont.st.test.com.testzhyhimont;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//Адаптер для отображения курсов валют. Передаю данные из двух таблиц и отображаю их

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.MyViewHolder> {

    List<CurrencyFirstData> listCurrencyFirstData;
    List<CurrencySecondData> listCurrencySecondData;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title_char_code_currency;
        TextView title_name_code_currency;
        TextView title_first_rate_currency;
        TextView title_second_rate_currency;

        public MyViewHolder(View itemView) {
            super(itemView);
            title_char_code_currency = itemView.findViewById(R.id.char_code_currency);
            title_name_code_currency = itemView.findViewById(R.id.name_code_currency);
            title_first_rate_currency = itemView.findViewById(R.id.first_rate_curency);
            title_second_rate_currency = itemView.findViewById(R.id.second_rate_currency);
        }
    }

    public CurrencyAdapter (List<CurrencyFirstData> listCurrencyFisrtData, List<CurrencySecondData> listCurrencySecondData) {
        this.listCurrencyFirstData = listCurrencyFisrtData;
        this.listCurrencySecondData = listCurrencySecondData;
    }

    @Override
    public CurrencyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_currency, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.title_char_code_currency.setText(listCurrencyFirstData.get(position).getCharCode());
        holder.title_name_code_currency.setText(listCurrencyFirstData.get(position).getNameCurrency());
        holder.title_first_rate_currency.setText(listCurrencyFirstData.get(position).getRateCurrency());
        holder.title_second_rate_currency.setText(listCurrencySecondData.get(position).getRateCurrency());
    }


    @Override
    public int getItemCount() {
        return listCurrencyFirstData.size();
    }
}
