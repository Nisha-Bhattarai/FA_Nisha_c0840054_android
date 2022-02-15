package com.example.fa_nisha_c0840054_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fa_nisha_c0840054_android.Adapter.FavPlaceAdapter;
import com.example.fa_nisha_c0840054_android.Room.FavPlacesDB;
import com.example.fa_nisha_c0840054_android.Room.FavPlacesEntity;
import com.example.fa_nisha_c0840054_android.Model.*;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FavPlaceActivity extends AppCompatActivity implements View.OnClickListener, FavPlaceAdapter.Listener {
    private RecyclerView rvFavPlaces;
    private FavPlaceAdapter adapter;
    private FavPlacesDB favPlacesDB;
    List<FavPlacesModel> favPlaces = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_place_list);
        getSupportActionBar().hide();
        rvFavPlaces = findViewById(R.id.favPlacesRecyclerView);
        adapter = new FavPlaceAdapter(this, favPlaces, this);
        rvFavPlaces.setAdapter(adapter);
        favPlacesDB = FavPlacesDB.getInstance(this);
        loadData();

        findViewById(R.id.addButton).setOnClickListener(this);
        ItemTouchHelper.SimpleCallback ithFav = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int index = viewHolder.getAdapterPosition();
                FavPlacesModel fav = favPlaces.get(index);
                favPlacesDB.favPlacesDao().deleteById(fav.getId());
                favPlaces.remove(fav);
                adapter.notifyItemRemoved(index);
                Toast.makeText(getApplicationContext(), fav.getAddress() + " was deleted from your fav places list!",
                        Toast.LENGTH_LONG).show();
            }
        };
        ItemTouchHelper itHelper = new ItemTouchHelper(ithFav);
        itHelper.attachToRecyclerView(rvFavPlaces);
    }

    private void loadData() {
        favPlaces.clear();
        List<FavPlacesEntity> entity = favPlacesDB.favPlacesDao().getAllFavPlaces();
        for (int i = 0; i < entity.size(); i++) {
            FavPlacesEntity en = entity.get(i);
            favPlaces.add(new FavPlacesModel(en.getId(), en.getAddress(), en.getDate(), en.getLatitude(), en.getLongitude()));
        }
        if (adapter != null && favPlaces.size() > 0) {
            adapter.setFavPlaces(favPlaces);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addButton:
                startActivity(MapsActivity.getNewIntent(this, 0.0, 0.0, "", "", -1));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }


    @Override
    public void onItemClicked(FavPlacesModel favPlacesModel) {
        startActivity(MapsActivity.getNewIntent(this, favPlacesModel.getLatitude(), favPlacesModel.getLongitude(), favPlacesModel.getAddress(), favPlacesModel
                .getDate(), favPlacesModel.getId()));
    }
}