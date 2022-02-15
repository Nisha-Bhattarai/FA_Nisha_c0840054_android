package com.example.fa_nisha_c0840054_android.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.fa_nisha_c0840054_android.Model.FavPlacesModel;

import java.util.List;

@Dao
public interface FavPlacesDao {

    @Insert
    void insert(FavPlacesEntity favPlacesEntity);

    @Delete
    public void deleteAll(FavPlacesEntity favPlacesEntity);

    @Query("DELETE FROM fav_places WHERE id = :id" )
    int deleteById(int id);

    @Query("UPDATE fav_places SET address = :address, date = :date, latitude = :lat, longitude = :lng  WHERE id = :id")
    int update(int id, String address, String date, double lat, double lng);



    @Query("SELECT * FROM fav_places ORDER BY date")
    List<FavPlacesEntity> getAllFavPlaces();
}
