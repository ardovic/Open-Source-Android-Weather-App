package com.ardovic.weatherappprototype.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import com.ardovic.weatherappprototype.App;
import com.ardovic.weatherappprototype.database.DatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final static String PREFS = "PREFS";
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context providesAppContext(){
        return app;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Resources providesResources(Context context) {
        return context.getResources();
    }

    @Provides
    @Singleton
    DatabaseHelper providesDatabaseHelper(Context context) {
        return new DatabaseHelper(context);
    }

    @Provides
    @Singleton
    SQLiteDatabase providesDatabase(DatabaseHelper databaseHelper) {
        return databaseHelper.getWritableDatabase();
    }

}
