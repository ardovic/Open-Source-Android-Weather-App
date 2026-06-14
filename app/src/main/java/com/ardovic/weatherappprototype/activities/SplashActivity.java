package com.ardovic.weatherappprototype.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.ardovic.weatherappprototype.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences pref = getSharedPreferences("first_launch", MODE_PRIVATE);

        boolean first = pref.getBoolean("first", true);

        if (first) {

            new Handler().postDelayed(() -> {

                pref.edit().putBoolean("first", false).apply();

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();

            }, 3000);

        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }
}
