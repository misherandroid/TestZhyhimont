package zhyhimont.st.test.com.testzhyhimont;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//Таблица для настроек курсов валют
//Аннотации библиотеки Room

@Entity
public class CurrencyTable {

    @PrimaryKey
    @NonNull
    public String char_name_table;

    public String name_currency_table;

    public int visible_currency_table;

    public int position_currency_table;

    @NonNull
    public String getChar_name_table() {
        return char_name_table;
    }

    public void setChar_name_table(@NonNull String char_name_table) {
        this.char_name_table = char_name_table;
    }

    public String getName_currency_table() {
        return name_currency_table;
    }

    public void setName_currency_table(String name_currency_table) {
        this.name_currency_table = name_currency_table;
    }

    public int getVisible_currency_table() {
        return visible_currency_table;
    }

    public void setVisible_currency_table(int visible_currency_table) {
        this.visible_currency_table = visible_currency_table;
    }

    public int getPosition_currency_table() {
        return position_currency_table;
    }

    public void setPosition_currency_table(int position_currency_table) {
        this.position_currency_table = position_currency_table;
    }
}
