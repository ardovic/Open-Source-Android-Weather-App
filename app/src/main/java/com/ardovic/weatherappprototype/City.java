package com.ardovic.weatherappprototype;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class City {

    @SerializedName("id")
    private int cityId;

    @SerializedName("name")
    private String cityName;

    @SerializedName("country")
    private String countryName;

}
