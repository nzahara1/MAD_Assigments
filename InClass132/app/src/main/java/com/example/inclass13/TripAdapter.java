package com.example.inclass13;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder> {

    List<Trip> trips = new ArrayList<Trip>();
    Context context;


    public TripAdapter(Context context, List<Trip> trips) {
        this.context = context;
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trip_row_layout, parent, false);
        return new TripAdapter.TripHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripHolder holder, int position) {
        Trip trip = trips.get(position);
        holder.tripName.setText(trip.getTripName());
        holder.tripCity.setText(trip.getTripCity());
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    class TripHolder extends RecyclerView.ViewHolder {
        public TextView tripName;
        public TextView tripCity;

        public TripHolder(@NonNull View itemView) {
            super(itemView);
            tripName = itemView.findViewById(R.id.trip_name);
            tripCity = itemView.findViewById(R.id.trip_city);
        }
    }


}