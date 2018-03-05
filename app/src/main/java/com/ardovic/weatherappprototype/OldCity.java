package com.ardovic.weatherappprototype;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class OldCity {

    @SerializedName("id")
    private int cityId;

    @SerializedName("name")
    private String cityName;

    @SerializedName("country")
    private String countryName;

}
