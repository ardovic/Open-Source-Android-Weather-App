package com.ardovic.weatherappprototype.model;

import android.graphics.Bitmap;

import com.ardovic.weatherappprototype.model.weather.Clouds;
import com.ardovic.weatherappprototype.model.weather.CurrentCondition;
import com.ardovic.weatherappprototype.model.weather.Location;
import com.ardovic.weatherappprototype.model.weather.Rain;
import com.ardovic.weatherappprototype.model.weather.Snow;
import com.ardovic.weatherappprototype.model.weather.Temperature;
import com.ardovic.weatherappprototype.model.weather.Wind;

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public CurrentCondition getCurrentCondition() {
        return currentCondition;
    }

    public void setCurrentCondition(CurrentCondition currentCondition) {
        this.currentCondition = currentCondition;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Snow getSnow() {
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
