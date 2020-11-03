package com.ardovic.weatherappprototype.network;

import com.ardovic.weatherappprototype.model.retrofit.Response;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * @author Polurival on 12.05.2018.
 */
public interface WeatherApi {

    String API_KEY = "1780541fd97c219bcb6b471152ad65c7";

    String BASE_URL = "https://api.openweathermap.org";

    String WEATHER_PATH = "/data/2.5/weather";
    String IMAGE_PATH = BASE_URL + "/img/w/{iconId}.png";

    String PARAM_LOCATION = "q";
    String PARAM_LAT = "lat";
    String PARAM_LON = "lon";
    String PARAM_APP_ID = "APPID";

    @GET(WEATHER_PATH)
    Call<Response> getWeather(@Query(PARAM_LOCATION) String location,
                              @Query(PARAM_APP_ID) String apiKey);

    @GET(WEATHER_PATH)
    Call<Response> getWeatherUsingCoordinates(@Query(PARAM_LAT) String lat,
                                              @Query(PARAM_LON) String lon,
                                              @Query(PARAM_APP_ID) String apiKey);

    @GET(IMAGE_PATH)
    @Streaming
    Call<ResponseBody> getIcon(@Path("iconId") String iconId);
}
