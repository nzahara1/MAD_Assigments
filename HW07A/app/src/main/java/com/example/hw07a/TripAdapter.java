package com.example.hw07a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private Context context;

    private List<Trip> tripList;

    TripAdapter(Context context, List<Trip> trips) {
        this.context = context;
        this.tripList = trips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trip_row_layout, parent, false);
        return new TripAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.title.setText(trip.getName());
        holder.location.setText(trip.getLat() + "," + trip.getLon());
        if (!trip.getImageUrl().isEmpty()) {
            Picasso.get().load(trip.getImageUrl()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        TextView location;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.trip_cover);
            title = itemView.findViewById(R.id.title_val);
            location = itemView.findViewById(R.id.location_val);
        }
    }

}
