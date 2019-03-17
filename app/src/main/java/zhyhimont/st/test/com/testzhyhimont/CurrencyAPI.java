package zhyhimont.st.test.com.testzhyhimont;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//Retrofit GET запрос за курсами на сайт nbrb

public interface CurrencyAPI {

    @GET("XmlExRates")
    Call<Responce> getCurrencys(@Query("ondate") String date);

}
