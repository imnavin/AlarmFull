package com.example.navin.alarmfull.weathercomp.model;

/**
 * Created by navin on 8/14/2017.
 */

public class Place {

    private float lon;
    private float lat;
    private String country;
    private String city;

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
