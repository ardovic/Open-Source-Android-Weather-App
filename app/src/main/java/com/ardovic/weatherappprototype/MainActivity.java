package com.ardovic.weatherappprototype;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.ardovic.weatherappprototype.model.Weather;
import com.ardovic.weatherappprototype.network.HTTPWeatherClient;
import com.ardovic.weatherappprototype.network.JSONWeatherParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

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


    String city = "Ankara, TR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});


        //VERY LONG OPERATION


        readStream();

        // Create a SimpleCursorAdapter for the State Name field.
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this,
                        R.layout.dropdown_text,
                        null,
                        new String[]{"city_name"},
                        new int[]{R.id.text});
        actvCityCountryName.setAdapter(adapter);

        // Set an OnItemClickListener, to update dependent fields when
        // a choice is made in the AutoCompleteTextView.
        actvCityCountryName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the
                // result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String countryName =
                        cursor.getString(cursor.getColumnIndexOrThrow("country_name"));
                String cityName =
                        cursor.getString(cursor.getColumnIndexOrThrow("city_name"));

                // Update the parent class's TextView
                actvCityCountryName.setText(cityName + ", " + countryName);
            }
        });

        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor c) {
                return c.getString(c.getColumnIndexOrThrow("country_name"));
            }
        });
/*
        // Set the CursorToStringConverter, to provide the labels for the
        // choices to be displayed in the AutoCompleteTextView.
        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                // Get the label for this row out of the "state" column


                final int columnIndex = cursor.getColumnIndexOrThrow("city_name");
                final String cityName = cursor.getString(columnIndex);
                final int columnIndex2 = cursor.getColumnIndex("country_name");
                final String countryName = cursor.getString(columnIndex2);


                return (cityName + ", " + countryName);

            }
        });
        */


        // Set the FilterQueryProvider, to run queries for choices
        // that match the specified input.
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                // Search for states whose names begin with the specified letters.
                Cursor cursor = getMatchingStates(
                        (constraint != null ? constraint.toString() : null));
                return cursor;
            }
        });


        readFromDatabase();


    }


    @Override
    protected void onStop() {
        super.onStop();
        databaseHelper.close();
    }

    public void addToDatabase(City city) {
        ContentValues cv = new ContentValues();

        cv.put("city_id", city.getCityId());
        cv.put("city_name", city.getCityName());
        cv.put("country_name", city.getCountryName());

        long rowID = database.insert("mytable", null, cv);
        Log.d("HEX", "row inserted, ID = " + rowID);

    }

    public void readFromDatabase() {

        Cursor c = database.rawQuery("SELECT _id, city_id, city_name, country_name FROM mytable ORDER BY city_name ASC", null);

        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("_id");
            int cityIdColIndex = c.getColumnIndex("city_id");
            int cityNameColIndex = c.getColumnIndex("city_name");
            int countryNameColIndex = c.getColumnIndex("country_name");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("HEX",
                        "ID = " + c.getInt(idColIndex) +
                                ", city ID = " + c.getInt(cityIdColIndex) +
                                ", city name = " + c.getString(cityNameColIndex) +
                                ", country name = " + c.getString(countryNameColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d("HEX", "0 rows");
        c.close();

    }

    public void readStream() {
        int i = 0;

        try {
            JsonReader reader = new JsonReader(new InputStreamReader(getAssets().open("cityList.json")));
            Gson gson = new GsonBuilder().create();

            // Read file in stream mode
            reader.beginArray();
            while (reader.hasNext()) {
                // Read data into object model
                City city = gson.fromJson(reader, City.class);

                System.out.println(i++);
                addToDatabase(city);

                if (i == 5) {
                    break;
                }

            }
            reader.close();
        } catch (UnsupportedEncodingException ex) {

        } catch (IOException ex) {

        }
    }

    public Cursor getMatchingStates(String constraint) throws SQLException {

        String queryString =
                "SELECT _id, city_id, city_name, country_name FROM mytable";

        if (constraint != null) {
            // Query for any rows where the state name begins with the
            // string specified in constraint.
            //
            // NOTE:
            // If wildcards are to be used in a rawQuery, they must appear
            // in the query parameters, and not in the query string proper.
            // See http://code.google.com/p/android/issues/detail?id=3153
            constraint = constraint.trim() + "%";
            queryString += " WHERE city_name LIKE ?";
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



