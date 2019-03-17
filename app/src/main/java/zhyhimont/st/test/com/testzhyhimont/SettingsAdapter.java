package zhyhimont.st.test.com.testzhyhimont;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.MyViewHolder> {

    List<CurrencyTable> currencyList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title_char_code;
        TextView title_name_code;
        Switch visible_name_currency;

        public MyViewHolder(View itemView) {
            super(itemView);
            title_char_code = itemView.findViewById(R.id.char_code_setting);
            title_name_code = itemView.findViewById(R.id.name_code_setting);
            visible_name_currency = itemView.findViewById(R.id.visible_curency);
        }
    }

    public SettingsAdapter (List<CurrencyTable> currencyList) {
        this.currencyList = currencyList;
    }

    @Override
    public SettingsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_setting, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.title_char_code.setText(currencyList.get(position).getChar_name_table());
        holder.title_name_code.setText(currencyList.get(position).getName_currency_table());

        if (currencyList.get(position).getVisible_currency_table() == 1){
            holder.visible_name_currency.setChecked(true);
        }
        holder.visible_name_currency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true)
                    currencyList.get(getItemViewType(position)).setVisible_currency_table(1);
                else
                    currencyList.get(position).setVisible_currency_table(0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
