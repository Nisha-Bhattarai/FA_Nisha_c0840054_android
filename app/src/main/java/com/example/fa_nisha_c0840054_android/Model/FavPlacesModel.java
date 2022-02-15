package com.example.fa_nisha_c0840054_android.Model;

public class FavPlacesModel {
    private int id;
    private String address, date;
    private double latitude, longitude;

    public FavPlacesModel(int id, String address, String date, double latitude, double longitude) {
        this.id = id;
        this.address = address;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
