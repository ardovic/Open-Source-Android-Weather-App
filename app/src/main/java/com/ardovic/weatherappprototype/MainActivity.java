package com.ardovic.weatherappprototype;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.LoaderManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.ardovic.weatherappprototype.database.DatabaseHelper;
import com.ardovic.weatherappprototype.json.JSONConverter;
import com.ardovic.weatherappprototype.model.IJ;
import com.ardovic.weatherappprototype.model.Weather;
import com.ardovic.weatherappprototype.network.HTTPWeatherClient;
import com.ardovic.weatherappprototype.network.JSONWeatherParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.actv_city_country_name)
    AutoCompleteTextView actvCityCountryName;
    @BindView(R.id.iv_condition_icon)
    ImageView ivConditionIcon;
    @BindView(R.id.tv_city_country_name)
    TextView tvCityCountryName;
    @BindView(R.id.tv_condition_description)
    TextView tvConditionDescription;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.tv_pressure)
    TextView tvPressure;
    @BindView(R.id.tv_humidity)
    TextView tvHumidity;
    @BindView(R.id.tv_wind_speed_degrees)
    TextView tvWindSpeedDegrees;

    public final static String CITY_ID = "city_id";
    public final static String CITY_COUNTRY_NAME = "city_country_name";
    public final static String TABLE_1 = "my_table";
    public final static String ID = "_id";

    public String cityCountryName;

    public SimpleCursorAdapter mAdapter;

    public DatabaseHelper databaseHelper;
    public SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        /*
        FragmentManager fm = getFragmentManager();
        DialogFragment newFragment = new AddingCityDialogFragment();
        newFragment.show(fm, "abc");
        */

        cityCountryName = sharedPreferences.getString(CITY_COUNTRY_NAME, "");
        actvCityCountryName.setText(cityCountryName);

        if(!cityCountryName.equals("")) {
            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[]{cityCountryName});
        }


        //JSONConverter.getInstance().makeNewShortJSON(this, null, null, null);




        // Create a SimpleCursorAdapter for the State Name field.
        mAdapter = new SimpleCursorAdapter(this,
                        R.layout.dropdown_text,
                        null,
                        new String[]{CITY_COUNTRY_NAME},
                        new int[]{R.id.text},0);

        getLoaderManager().initLoader(0, null, this);




        // Set an OnItemClickListener, to update dependent fields when
        // a choice is made in the AutoCompleteTextView.
        actvCityCountryName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the
                // result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                cityCountryName = cursor.getString(cursor.getColumnIndexOrThrow(CITY_COUNTRY_NAME));

                // Update the parent class's TextView
                actvCityCountryName.setText(cityCountryName);

                JSONWeatherTask task = new JSONWeatherTask();
                task.execute(new String[]{cityCountryName});


            }
        });




        // Set the FilterQueryProvider, to run queries for choices
        // that match the specified input.
        mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                // Search for states whose names begin with the specified letters.
                Cursor cursor = getMatchingStates(
                        (constraint != null ? constraint.toString() : null));

                return cursor;
            }

        });

        actvCityCountryName.setAdapter(mAdapter);

        //readFromDatabase();

    }

    @Override
    protected void onPause() {
        super.onPause();

        database.close();
        databaseHelper = null;

    }

    @Override
    protected void onResume() {
        super.onResume();

        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        if(databaseHelper.isTableExists(database, TABLE_1)) {
            long count = DatabaseUtils.queryNumEntries(database, TABLE_1);
            System.out.println(count);

            if(count != 168820) {
                database.execSQL("DROP TABLE IF EXISTS " + TABLE_1);
                databaseHelper.createTable1(database);
                createLocalCityDB();
            }

        } else {
            databaseHelper.createTable1(database);
            createLocalCityDB();
        }



    }

    @Override
    protected void onStop() {
        super.onStop();


        sharedPreferences.edit().putString(CITY_COUNTRY_NAME, cityCountryName).apply();

    }

    public void readFromDatabase() {

        String rawQuery = "SELECT " + ID + ", " + CITY_ID + ", " + CITY_COUNTRY_NAME + " FROM " + TABLE_1 + " ORDER BY " + CITY_COUNTRY_NAME + " ASC";

        Cursor c = database.rawQuery(rawQuery, null);

        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex(ID);
            int cityIdColIndex = c.getColumnIndex(CITY_ID);
            int cityCountryNameColIndex = c.getColumnIndex(CITY_COUNTRY_NAME);

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("HEX",
                        "City ID = " + c.getInt(cityIdColIndex) +
                                ", city and country name = " + c.getString(cityCountryNameColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла


            } while (c.moveToNext());
        } else
            Log.d("HEX", "0 rows");
        c.close();

    }

    public void createLocalCityDB() {

        int i = 0;

        ArrayList<ContentValues> cvList = new ArrayList<>();
        ContentValues cv ;
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

        } catch (UnsupportedEncodingException ex) {

        } catch (IOException ex) {

        }
    }



    public Cursor getMatchingStates(String constraint) throws SQLException {

        String queryString = "SELECT " + ID + ", " + CITY_ID + ", " + CITY_COUNTRY_NAME + " FROM " + TABLE_1;

        if (constraint != null) {
            // Query for any rows where the state name begins with the
            // string specified in constraint.
            //
            // NOTE:
            // If wildcards are to be used in a rawQuery, they must appear
            // in the query parameters, and not in the query string proper.
            // See http://code.google.com/p/android/issues/detail?id=3153
            constraint = constraint.trim() + "%";
            queryString += " WHERE " + CITY_COUNTRY_NAME + " LIKE ?";
        }
        String params[] = {constraint};

        if (constraint == null) {
            // If no parameters are used in the query,
            // the params arg must be null.
            params = null;
        }
        try {
            Cursor cursor = database.rawQuery(queryString, params);
            if (cursor != null) {
                this.startManagingCursor(cursor);
                cursor.moveToFirst();
                return cursor;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
        //return new CursorLoader(this, CONTENT_URI, PROJECTION, null, null, null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }







    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new HTTPWeatherClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);
                Bitmap icon = HTTPWeatherClient.getResizedBitmap(
                        HTTPWeatherClient.getBitmapFromURL(weather.getCurrentCondition().getIcon()),
                        100,
                        100);
                weather.setIcon(icon);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);


            ivConditionIcon.setImageBitmap(weather.getIcon());

            tvCityCountryName.setText(weather.getLocation().getCity() + ", " + weather.getLocation().getCountry());
            tvConditionDescription.setText(weather.getCurrentCondition().getCondition() + " (" + weather.getCurrentCondition().getDescription() + ")");
            tvTemperature.setText(", " + Math.round((weather.getTemperature().getTemperature() - 273.15)) + (char) 0x00B0 + "C");
            tvHumidity.setText(weather.getCurrentCondition().getHumidity() + "%");
            tvPressure.setText(weather.getCurrentCondition().getPressure() + " hPa");
            tvWindSpeedDegrees.setText(weather.getWind().getSpeed() + " mps, " + weather.getWind().getDegrees() + (char) 0x00B0);

        }
    }
}



