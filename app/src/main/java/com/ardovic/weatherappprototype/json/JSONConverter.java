package com.ardovic.weatherappprototype.json;

import android.content.ContentValues;
import android.content.Context;
import android.os.Environment;

import com.ardovic.weatherappprototype.model.IJ;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

public class JSONConverter {

    private static JSONConverter instance;

    private JSONConverter(){}

    public static JSONConverter getInstance() {
        if(instance == null) {
            instance = new JSONConverter();
        }
        return instance;
    }

    /**
     *
     * @param context
     * @param inputName provide the correct name of the original JSON in assets folder
     * @param outputName system will create a new JSON file with this name
     * @param outputFolder system will create a folder with this name for your file
     */
    public void makeNewShortJSON(Context context, String inputName, String outputName, String outputFolder) {
        int i = 0;

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/" + outputFolder);
        myDir.mkdirs();
        String fname = outputName + ".json";
        File file = new File(myDir, fname);
        Gson gson = new GsonBuilder().create();
        IJ ij;

        ContentValues cv;

        HashSet<String> set = new HashSet<>();

        try (JsonReader reader = new JsonReader(new InputStreamReader(context.getAssets().open(inputName)));
             JsonWriter writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(file)))) {




            writer.beginArray();

            // Read file in stream mode
            reader.beginArray();




            while (reader.hasNext()) {
                // Read data into object model
                //city = gson.fromJson(reader, OldCity.class);

                ij = gson.fromJson(reader, IJ.class);

                String cityCountryName = ij.j;

                if(!set.contains(cityCountryName)) {

                    set.add(cityCountryName);


                    writer.beginObject();
                    writer.name("i").value(ij.i);
                    writer.name("j").value(ij.j);
                    writer.endObject();

                    System.out.println(i++);
                } else {
                    System.out.println("Duplicate detected");
                }


                /*
                if(!hasObject(cityCountryName)) {

                    cv = new ContentValues();
                    cv.put(CITY_COUNTRY_NAME, cityCountryName);
                    database.insert(TABLE_2, null, cv);


                    writer.beginObject();
                    writer.name("city_id").value(city.getCityId());
                    writer.name("city_country_name").value(cityCountryName);
                    writer.endObject();

                    System.out.println(i++);
                } else {
                    System.out.println("Duplicate detected");
                }
                */

            }

            writer.endArray();
            //writer.close();

            //reader.close();
        } catch (UnsupportedEncodingException ex) {

        } catch (IOException ex) {

        }
    }


}
