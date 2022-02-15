package com.example.fa_nisha_c0840054_android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fa_nisha_c0840054_android.Model.FavPlacesModel;
import com.example.fa_nisha_c0840054_android.R;

import java.util.List;

public class FavPlaceAdapter extends RecyclerView.Adapter<FavPlaceAdapter.ViewHolder> {

    private List<FavPlacesModel> favPlaces;
    private Context context;
    private Listener listener;

    public FavPlaceAdapter(Context activity, List<FavPlacesModel> favPlaces, Listener listener) {
        this.context = activity;
        this.favPlaces = favPlaces;
        this.listener = listener;
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        FavPlacesModel item = favPlaces.get(position);
        holder.tvAddress.setText("Address: " + item.getAddress());
        holder.date.setText("Date: " + item.getDate());
        holder.LatLng.setText(String.format("Latitude: %.4f, Longitude: %.4f", item.getLatitude(), item.getLongitude()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(item);
            }
        });
    }

    private Boolean toBoolean(int n) {
        return n != 0;
    }

    public void setFavPlaces(List<FavPlacesModel> favPlaces) {
        this.favPlaces = favPlaces;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return favPlaces.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fav_place, parent, false);
        return new ViewHolder(itemView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress, date, LatLng;

        ViewHolder(View view) {
            super(view);
            tvAddress = view.findViewById(R.id.addressTV);
            date = view.findViewById(R.id.dateTV);
            LatLng = view.findViewById(R.id.latLongTV);
        }
    }

    public interface Listener {
        void onItemClicked(FavPlacesModel favPlacesModel);
    }
}

