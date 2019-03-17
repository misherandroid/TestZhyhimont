package zhyhimont.st.test.com.testzhyhimont;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

//Класс управления БД, т.к. нужен только один экземпляр использую Singleton

@Database(entities = {CurrencyTable.class, CurrencyFirstData.class, CurrencySecondData.class}, version = 1)
public abstract class AppDatabaseHelper extends RoomDatabase {

    private static AppDatabaseHelper INSTANCE;

    public abstract CurrencyTableDAO currencyTableDao();

    public abstract CurencyFirstDataDAO currencyFisrtDataDao();

    public abstract CurrencySecondDataDAO currencySecondDataDao();

    public static AppDatabaseHelper getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabaseHelper.class, "database")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}