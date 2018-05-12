package com.ardovic.weatherappprototype.model.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * @author Polurival on 12.05.2018.
 */
public class Sys {

    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("message")
    @Expose
    private double message;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sunrise")
    @Expose
    private int sunrise;
    @SerializedName("sunset")
    @Expose
    private int sunset;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getSunrise() {
        return sunrise;
    }

    public void setSunrise(int sunrise) {
        this.sunrise = sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public void setSunset(int sunset) {
        this.sunset = sunset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sys sys = (Sys) o;
        return Objects.equals(type, sys.type) &&
                Objects.equals(id, sys.id) &&
                Objects.equals(message, sys.message) &&
                Objects.equals(country, sys.country) &&
                Objects.equals(sunrise, sys.sunrise) &&
                Objects.equals(sunset, sys.sunset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, message, country, sunrise, sunset);
    }

    @Override
    public String toString() {
        return "Sys{" +
                "type=" + type +
                ", id=" + id +
                ", message=" + message +
                ", country='" + country + '\'' +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }
}
