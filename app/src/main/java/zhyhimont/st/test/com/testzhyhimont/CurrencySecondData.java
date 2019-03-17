package zhyhimont.st.test.com.testzhyhimont;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//Таблица для текущих будущих/предыдущих курсов валют
//Аннотации библиотеки Room

@Entity
public class CurrencySecondData {

    @NonNull
    @PrimaryKey
    private String charCode;

    private String nameCurrency;
    private String rateCurrency;


    CurrencySecondData(String charCode, String nameCurrency, String rateCurrency) {
        this.charCode = charCode;
        this.nameCurrency = nameCurrency;
        this.rateCurrency = rateCurrency;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getNameCurrency() {
        return nameCurrency;
    }

    public void setNameCurrency(String nameCurrency) {
        this.nameCurrency = nameCurrency;
    }

    public String getRateCurrency() {
        return rateCurrency;
    }

    public void setRateCurrency(String rateCurrency) {
        this.rateCurrency = rateCurrency;
    }
}
