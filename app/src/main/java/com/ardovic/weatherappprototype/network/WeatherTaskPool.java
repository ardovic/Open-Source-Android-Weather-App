package com.ardovic.weatherappprototype.network;

import android.graphics.Bitmap;

import com.ardovic.weatherappprototype.model.Weather;

import org.json.JSONException;

import java.util.concurrent.Callable;

/**
 * Created by user on 01.04.2018.
 *
 * @deprecated - используется retrofit
 */

public class WeatherTaskPool implements Callable<Weather> {

    String location;

    public WeatherTaskPool(String location) {
        this.location = location;
    }

    @Override
    public Weather call() throws Exception {
        Weather weather = null;
        String data = ((new HTTPWeatherClient()).getWeatherData(location));

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
}
