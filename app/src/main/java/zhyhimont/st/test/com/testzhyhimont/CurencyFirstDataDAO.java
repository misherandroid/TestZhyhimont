package zhyhimont.st.test.com.testzhyhimont;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

//Методы для управления таблицей CurrencyFirstData
@Dao
public interface CurencyFirstDataDAO {

    @Query("SELECT * FROM currencyfirstdata")
    List<CurrencyFirstData> getAll();

    @Query("SELECT * FROM currencyfirstdata WHERE charCode = :charCode")
    CurrencyFirstData getSpecial(String charCode);

    @Query("DELETE FROM currencyfirstdata")
    void deleteAll();

    @Query("SELECT COUNT(*) FROM currencyfirstdata")
    int getCount();

    @Insert
    void insert(CurrencyFirstData currencyFirstData);
}
