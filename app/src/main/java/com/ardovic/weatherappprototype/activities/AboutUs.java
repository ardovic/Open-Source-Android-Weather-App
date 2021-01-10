package com.ardovic.weatherappprototype.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ardovic.weatherappprototype.InternetConnectivityHelper;
import com.ardovic.weatherappprototype.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AboutUs extends AppCompatActivity {
private ListView listView_Contributors;
public static final String TAG="AboutUs";

private InternetConnectivityHelper internetConnectivityHelper;

    ArrayList<String> arrayList_username;
    TextView checkInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        listView_Contributors=findViewById(R.id.contributor_list);
        checkInternet = findViewById(R.id.check_internet);
        internetConnectivityHelper = new InternetConnectivityHelper(this);

        if (internetConnectivityHelper.checkDeviceConnectedToInternet()) {
            checkInternet.setVisibility(View.GONE);
            new FetchContributors().execute();
        }
        else {
            checkInternet.setVisibility(View.VISIBLE);
            Toast.makeText(this, getResources().getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }



    public class FetchContributors extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL("https://api.github.com/repos/ardovic/Open-Source-Android-Weather-App/contributors?anon=1");
                HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream=urlConnection.getInputStream();
                StringBuffer buffer=new StringBuffer();
                BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
                String Line;
                while ((Line=reader.readLine())!=null){
                    buffer.append(Line+"\n");
                }
                String Jsonstr=buffer.toString();
//                Log.d(TAG,Jsonstr);
                arrayList_username=new ArrayList<>();
                JSONArray jsonArray=new JSONArray(Jsonstr);
                Log.d(TAG,""+jsonArray.length());
                String username;
                for (int i = 0; i <jsonArray.length() ; i++) {

                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    try{
                       username=jsonObject.getString("login");
                       arrayList_username.add(username);
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList_username);
            listView_Contributors.setAdapter(arrayAdapter);

        }
    }

}
