package com.ardovic.weatherappprototype.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ardovic.weatherappprototype.App;
import com.ardovic.weatherappprototype.database.DatabaseHelper;
import com.ardovic.weatherappprototype.network.WeatherApi;

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
    @Inject
    public WeatherApi weatherApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);

    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
