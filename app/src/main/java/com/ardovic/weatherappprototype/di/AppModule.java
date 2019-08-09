package com.ardovic.weatherappprototype.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import com.ardovic.weatherappprototype.App;
import com.ardovic.weatherappprototype.database.DatabaseHelper;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public abstract class AppModule { //Module must be made abstract since it has a abstract method now

    private final static String PREFS = "PREFS";

    //Removed the AppModule constructor since no need to initialize Context now, Dagger will provide it

    @Binds //Binds the App instance to Context (superType)
    @Singleton
    abstract Context providesAppContext(App app);//this method should be abstract, Dagger can provide Context


    //Since module cannot have abstract and non-static method together, turn all other method static

    @Provides
    @Singleton
    static SharedPreferences providesSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    static Resources providesResources(Context context) {
        return context.getResources();
    }


    @Provides
    @Singleton
    static DatabaseHelper providesDatabaseHelper(Context context) {
        return new DatabaseHelper(context);
    }

    @Provides
    @Singleton
    static SQLiteDatabase providesDatabase(DatabaseHelper databaseHelper) {
        return databaseHelper.getReadableDatabase();
    }

}
