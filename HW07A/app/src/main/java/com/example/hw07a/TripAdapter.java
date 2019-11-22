package com.example.hw07a;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private Context context;

    private List<Trip> tripList;
    String members;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Trip trip = tripList.get(position);
        holder.title.setText(trip.getName());
        holder.location.setText(trip.getLat() + "," + trip.getLon());
        if (!trip.getImageUrl().isEmpty()) {
            Picasso.get().load(trip.getImageUrl()).into(holder.imageView);
        }
        //hit db and fetch members
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("chatrooms").document(trip.getChatName());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                members = documentSnapshot.getString("members");
                if (members.contains(trip.getUserId())) {
                    holder.joinBtn.setText("Already Joined");
                    holder.joinBtn.setClickable(false);
                    holder.joinBtn.setTextSize(8);
                } else {
                    docRef.set(members + trip.getUserId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Successfully added to the chatroom", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Unable to add to the chatroom", Toast.LENGTH_LONG).show();
                            return;
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Unable to fetch chat members", Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;
        TextView location;
        Button joinBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.trip_cover);
            title = itemView.findViewById(R.id.title_val);
            location = itemView.findViewById(R.id.location_val);
            joinBtn = itemView.findViewById(R.id.join_btn);
        }
    }

}
