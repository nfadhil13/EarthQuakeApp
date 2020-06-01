package com.fdev.earthquakeapp.service.model;
import com.google.android.gms.maps.model.LatLng;


public class EarthQuake {

    private String place;


    private double magnitude;

    private long time;


    private String detailLink;


    private String type;


    private LatLng latLng;

    public EarthQuake() {
    }

    public EarthQuake(String place, double magnitude, long time, String detailLink, String type, LatLng latLng) {
        this.place = place;
        this.magnitude = magnitude;
        this.time = time;
        this.detailLink = detailLink;
        this.type = type;
        this.latLng = latLng;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
