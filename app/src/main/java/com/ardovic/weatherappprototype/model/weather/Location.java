package com.ardovic.weatherappprototype.model.weather;

import lombok.Data;

@Data
public class Location {

    private float longitude;
    private float latitude;
    private String country;
    private String city;

}
