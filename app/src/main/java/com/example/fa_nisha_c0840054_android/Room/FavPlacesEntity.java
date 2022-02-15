package com.example.fa_nisha_c0840054_android.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity(tableName = "fav_places")
public class FavPlacesEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "address")
    private String address;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;


    public FavPlacesEntity(@NonNull String address, @NonNull String date, double latitude, double longitude) {
        this.address = address;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
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

