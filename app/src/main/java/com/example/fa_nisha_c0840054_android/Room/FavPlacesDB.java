package com.example.fa_nisha_c0840054_android.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavPlacesEntity.class}, version = 1, exportSchema = false)
public abstract class FavPlacesDB extends RoomDatabase{

    private static final String DB_NAME = "fav_DB";

    public abstract FavPlacesDao favPlacesDao();

    private static volatile FavPlacesDB INSTANCE;

    public static FavPlacesDB getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), FavPlacesDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        return INSTANCE;
        }
    }


