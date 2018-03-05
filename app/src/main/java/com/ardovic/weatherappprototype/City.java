package com.ardovic.weatherappprototype;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class City {

    @SerializedName("city_id")
    private int cityId;

    @SerializedName("city_country_name")
    private String cityCountryName;

}
