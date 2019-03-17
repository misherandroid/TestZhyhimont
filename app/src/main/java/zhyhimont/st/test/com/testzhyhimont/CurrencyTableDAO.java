package zhyhimont.st.test.com.testzhyhimont;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

//Методы для управления таблицей CurrencyTable

@Dao
public interface CurrencyTableDAO {

    @Query("SELECT * FROM currencytable")
    List<CurrencyTable> getAll();

    @Query("SELECT * FROM currencytable WHERE visible_currency_table = 1")
    List<CurrencyTable> getVisible();

    @Query("DELETE FROM currencytable")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM currencytable")
    int getCount();

    @Insert
    void insert(CurrencyTable currencytable);

}
