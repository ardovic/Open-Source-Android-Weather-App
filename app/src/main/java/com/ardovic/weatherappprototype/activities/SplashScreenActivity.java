package com.ardovic.weatherappprototype.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ardovic.weatherappprototype.R;
import com.ardovic.weatherappprototype.model.IJ;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SplashScreenActivity extends BaseActivity {
    public static final String PACKAGE_NAME = "com.ardovic.weatherappprototype";
    public final static String CITY_ID = "city_id";
    public final static String CITY_COUNTRY_NAME = "city_country_name";
    public final static String TABLE_1 = "my_table";
    public static boolean IF_TABLE_EXITS = false;
    private static final String TAG = "SplashScreenActivity";
    public static final String TABLE = "table";
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IF_TABLE_EXITS = sharedPreferences.getBoolean(TABLE, false);
        setContentView(R.layout.activity_splash_screen);
        imageView = findViewById(R.id.splash_screen);
        imageView.setVisibility(View.INVISIBLE);


        if (IF_TABLE_EXITS) {
            Log.d(TAG, "onCreate: data base exists");
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            imageView.setVisibility(View.VISIBLE);
            Log.d(TAG, "onCreate: data base doesn't exits");
            createLocalCityDB();
            sharedPreferences.edit().putBoolean(TABLE, true).apply();
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }

    public void createLocalCityDB() {

        int i = 0;

        ArrayList<ContentValues> cvList = new ArrayList<>();
        ContentValues cv;
        Gson gson = new GsonBuilder().create();
        IJ ij;

        try (JsonReader reader = new JsonReader(new InputStreamReader(getAssets().open("ijCityList.json")))) {

            // Read file in stream mode
            reader.beginArray();

            while (reader.hasNext()) {
                // Read data into object model
                ij = gson.fromJson(reader, IJ.class);

                cv = new ContentValues();
                i++;
                cv.put(CITY_ID, ij.i);
                cv.put(CITY_COUNTRY_NAME, ij.j);
                cvList.add(cv);

                if (cvList.size() % 10000 == 0) {
                    System.out.println("Adding 10K to db, current item: " + i);
                    database.beginTransaction();
                    for (ContentValues value : cvList) {
                        database.insert(TABLE_1, null, value);
                    }
                    database.setTransactionSuccessful();
                    database.endTransaction();
                    cvList = new ArrayList<>();
                }

            }

            System.out.println("Adding last part to db, current item: " + i);
            database.beginTransaction();
            for (ContentValues value : cvList) {
                database.insert(TABLE_1, null, value);
            }
            database.setTransactionSuccessful();
            database.endTransaction();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
