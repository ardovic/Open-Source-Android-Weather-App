package com.ardovic.weatherappprototype;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import com.google.gson.stream.JsonWriter;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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

    public final static String CITY_ID = "city_id";
    public final static String CITY_COUNTRY_NAME = "city_country_name";
    public final static String TABLE = "my_table";
    public final static String ID = "_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});


        //VERY LONG OPERATION

        //makeNewShortJSON();
        readStream();

        long count = DatabaseUtils.queryNumEntries(database, TABLE);
        System.out.println(count);


        // Create a SimpleCursorAdapter for the State Name field.
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this,
                        R.layout.dropdown_text,
                        null,
                        new String[]{CITY_COUNTRY_NAME},
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
                String cityCountryName = cursor.getString(cursor.getColumnIndexOrThrow(CITY_COUNTRY_NAME));

                // Update the parent class's TextView
                actvCityCountryName.setText(cityCountryName);
            }
        });

        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                // Get the label for this row out of the "state" column


                final int columnIndex = cursor.getColumnIndexOrThrow(CITY_COUNTRY_NAME);
                final String cityCountryName = cursor.getString(columnIndex);

                return (cityCountryName);

            }
        });


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


        //readFromDatabase();


    }


    @Override
    protected void onStop() {
        super.onStop();
        databaseHelper.close();
    }


    public void readFromDatabase() {

        String rawQuery = "SELECT " + ID + ", " + CITY_ID + ", " + CITY_COUNTRY_NAME + " FROM " + TABLE + " ORDER BY " + CITY_COUNTRY_NAME + " ASC";

        Cursor c = database.rawQuery(rawQuery, null);

        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex(ID);
            int cityIdColIndex = c.getColumnIndex(CITY_ID);
            int cityCountryNameColIndex = c.getColumnIndex(CITY_COUNTRY_NAME);

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("HEX",
                        "ID = " + c.getInt(idColIndex) +
                                ", city ID = " + c.getInt(cityIdColIndex) +
                                ", city and country name = " + c.getString(cityCountryNameColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла


            } while (c.moveToNext());
        } else
            Log.d("HEX", "0 rows");
        c.close();

    }

    public void readStream() {

        int i = 0;

        ArrayList<ContentValues> cvList = new ArrayList<>();
        ContentValues cv ;
        Gson gson = new GsonBuilder().create();
        City city;

        try (JsonReader reader = new JsonReader(new InputStreamReader(getAssets().open("shortCityList.json")))) {

            // Read file in stream mode
            reader.beginArray();

            while (reader.hasNext()) {
                // Read data into object model
                city = gson.fromJson(reader, City.class);

                cv = new ContentValues();
                i++;
                cv.put(CITY_ID, city.getCityId());
                cv.put(CITY_COUNTRY_NAME, city.getCityCountryName());
                cvList.add(cv);

                if (cvList.size() % 10000 == 0) {
                    System.out.println("Adding 10K to db, current item: " + i);
                    database.beginTransaction();
                    for (ContentValues value : cvList) {
                        database.insert(TABLE, null, value);
                    }
                    database.setTransactionSuccessful();
                    database.endTransaction();
                }

            }

            System.out.println("Adding last part to db, current item: " + i);
            database.beginTransaction();
            for (ContentValues value : cvList) {
                database.insert(TABLE, null, value);
            }
            database.setTransactionSuccessful();
            database.endTransaction();

        } catch (UnsupportedEncodingException ex) {

        } catch (IOException ex) {

        }
    }

    public void makeNewShortJSON() {
        int i = 0;

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/new_json");
        myDir.mkdirs();
        String fname = "newJson.json";
        File file = new File(myDir, fname);
        Gson gson = new GsonBuilder().create();
        OldCity city;

        try (JsonReader reader = new JsonReader(new InputStreamReader(getAssets().open("cityList.json")));
             JsonWriter writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            //JsonReader reader = new JsonReader(new InputStreamReader(getAssets().open("cityList.json")));




            //JsonWriter writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.beginArray();

            // Read file in stream mode
            reader.beginArray();


            while (reader.hasNext()) {
                // Read data into object model
                city = gson.fromJson(reader, OldCity.class);

                writer.beginObject(); // {
                writer.name("city_id").value(city.getCityId());
                writer.name("city_country_name").value(city.getCityName() + ", " + city.getCountryName());
                writer.endObject(); // }

                System.out.println(i++);
                //addToDatabase(city);


            }
            writer.endArray();
            //writer.close();

            //reader.close();
        } catch (UnsupportedEncodingException ex) {

        } catch (IOException ex) {

        }
    }


    public Cursor getMatchingStates(String constraint) throws SQLException {

        String queryString = "SELECT " + ID + ", " + CITY_ID + ", " + CITY_COUNTRY_NAME + " FROM " + TABLE;

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



