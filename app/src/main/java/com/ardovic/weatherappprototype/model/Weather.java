package com.ardovic.weatherappprototype.model;

import android.graphics.Bitmap;

import com.ardovic.weatherappprototype.model.weather.Clouds;
import com.ardovic.weatherappprototype.model.weather.CurrentCondition;
import com.ardovic.weatherappprototype.model.weather.Location;
import com.ardovic.weatherappprototype.model.weather.Rain;
import com.ardovic.weatherappprototype.model.weather.Snow;
import com.ardovic.weatherappprototype.model.weather.Temperature;
import com.ardovic.weatherappprototype.model.weather.Wind;

import lombok.Data;

@Data
public class Weather {

    // From JSON
    private Location location;
    private CurrentCondition currentCondition;
    private Temperature temperature;
    private Wind wind;
    private Rain rain;
    private Snow snow;
    private Clouds clouds;

    // Downloaded separately
    private Bitmap icon;

    public Weather() {
        location = new Location();
        currentCondition = new CurrentCondition();
        temperature = new Temperature();
        wind = new Wind();
        rain = new Rain();
        snow = new Snow();
        clouds = new Clouds();
    }
}
