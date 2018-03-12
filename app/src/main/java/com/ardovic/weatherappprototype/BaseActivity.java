package com.ardovic.weatherappprototype;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ardovic.weatherappprototype.database.DatabaseHelper;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    public SharedPreferences sharedPreferences;
    @Inject
    public Context context;
    @Inject
    public Resources resources;
    @Inject
    public DatabaseHelper databaseHelper;
    @Inject
    public SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);

    }

}
