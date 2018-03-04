package com.ardovic.weatherappprototype.network;

import com.ardovic.weatherappprototype.model.Weather;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONWeatherParser {

    public final static String COORDINATES = "coord",
            LATITUDE = "lat",
            LONGITUDE = "lon",
            EXTRAS = "sys",
            WEATHER = "weather",
            WEATHER_TITLE = "main",
            WEATHER_DESCRIPTION = "description",
            EXTRAS_COUNTRY = "country",
            CITY = "name",
            WEATHER_ICON_STRING = "icon",
            CURRENT = "main",
            CURRENT_TEMPERATURE = "temp",
            CURRENT_HUMIDITY = "humidity",
            CURRENT_PRESSURE = "pressure",
            CURRENT_MAX_TEMPERATURE = "temp_max",
            CURRENT_MIN_TEMPERATURE = "temp_min",
            WIND = "wind",
            WIND_SPEED = "speed",
            WIND_DEGREES = "deg",
            CLOUDS = "clouds",
            CLOUDINESS = "all";

    public static Weather getWeather(String data) throws JSONException {

        JSONObject mainObj = new JSONObject(data);
        JSONObject coordinatesObj = getObject(COORDINATES, mainObj);
        JSONObject extrasObj = getObject(EXTRAS, mainObj);
        JSONObject weatherObj = mainObj.getJSONArray(WEATHER).getJSONObject(0);
        JSONObject currentOjb = getObject(CURRENT, mainObj);
        JSONObject cloudsObj = getObject(CLOUDS, mainObj);
        JSONObject windObj = getObject(WIND, mainObj);

        Weather weather = new Weather();

        // Location data
        weather.getLocation().setCity(getString(CITY, mainObj));
        weather.getLocation().setLatitude(getFloat(LATITUDE, coordinatesObj));
        weather.getLocation().setLongitude(getFloat(LONGITUDE, coordinatesObj));
        weather.getLocation().setCountry(getString(EXTRAS_COUNTRY, extrasObj));

        // Current condition
        weather.getCurrentCondition().setDescription(getString(WEATHER_DESCRIPTION, weatherObj));
        weather.getCurrentCondition().setCondition(getString(WEATHER_TITLE, weatherObj));
        weather.getCurrentCondition().setIcon(getString(WEATHER_ICON_STRING, weatherObj));
        weather.getCurrentCondition().setHumidity(getInt(CURRENT_HUMIDITY, currentOjb));
        weather.getCurrentCondition().setPressure(getInt(CURRENT_PRESSURE, currentOjb));

        // Temperature data
        weather.getTemperature().setMaxTemperature(getFloat(CURRENT_MAX_TEMPERATURE, currentOjb));
        weather.getTemperature().setMinTemperature(getFloat(CURRENT_MIN_TEMPERATURE, currentOjb));
        weather.getTemperature().setTemperature(getFloat(CURRENT_TEMPERATURE, currentOjb));

        // Wind data
        weather.getWind().setSpeed(getFloat(WIND_SPEED, windObj));
        weather.getWind().setDegrees(getFloat(WIND_DEGREES, windObj));

        // Clouds data
        weather.getClouds().setPrecipitation(getInt(CLOUDINESS, cloudsObj));

        return weather;
    }


    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        return jObj.getJSONObject(tagName);
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

}
