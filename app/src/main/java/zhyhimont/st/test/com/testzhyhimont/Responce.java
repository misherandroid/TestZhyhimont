package zhyhimont.st.test.com.testzhyhimont;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

//Разбираю ответ GET запроса с помощью библиотеки Simple XML

@Root(name="DailyExRates", strict = false)
public class Responce {
    @ElementList(inline = true)
    public List<Currency> currencys;

}

@Root(name = "Currency", strict = false)
@Entity
class Currency {

    @Element (name = "NumCode")
    private String numcode;

    @Element (name = "CharCode")
    @NonNull
    @PrimaryKey
    private String charcode;

    @Element (name = "Scale")
    private String scale;

    @Element (name = "Name")
    private String name;

    @Element (name = "Rate")
    private String rate;


    public String getNumcode() {
        return numcode;
    }

    public void setNumcode(String numcode) {
        this.numcode = numcode;
    }

    public String getCharcode() {
        return charcode;
    }

    public void setCharcode(String charcode) {
        this.charcode = charcode;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}

