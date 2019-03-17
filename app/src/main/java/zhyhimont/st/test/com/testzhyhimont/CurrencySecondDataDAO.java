package zhyhimont.st.test.com.testzhyhimont;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

//Методы для управления таблицей CurrencySecondData

@Dao
public interface CurrencySecondDataDAO {

    @Query("SELECT * FROM currencyseconddata")
    List<CurrencySecondData> getAll();

    @Query("SELECT * FROM currencyseconddata WHERE charCode = :charCode")
    CurrencySecondData getSpecial(String charCode);

    @Query("DELETE FROM currencyseconddata")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM currencyseconddata")
    int getCount();

    @Insert
    void insert(CurrencySecondData currencySecondData);
}