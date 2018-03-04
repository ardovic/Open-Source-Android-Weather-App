package com.ardovic.weatherappprototype.model.weather;

import lombok.Data;

@Data
public class CurrentCondition {

    private int weatherId;
    private String condition;
    private String description;
    private String icon;
    private float pressure;
    private float humidity;

}
