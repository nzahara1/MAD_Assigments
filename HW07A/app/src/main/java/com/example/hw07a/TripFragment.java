package com.example.hw07a;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TripFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Trip> trips;

    private TripAdapter tripAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trip, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        trips = new ArrayList<>();
        //get trips
        getTrips();
    }

    private void getTrips() {
        FirebaseFirestore reference = FirebaseFirestore.getInstance();
        reference.collection("trips").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    return;
                }
                trips.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Trip trip = new Trip(documentSnapshot.get("imageUrl").toString(), documentSnapshot.get("name").toString(), documentSnapshot.get("lat").toString(), documentSnapshot.get("lon").toString(),
                            documentSnapshot.get("chatName").toString(), documentSnapshot.get("userId").toString());
                    trips.add(trip);
                }
                tripAdapter = new TripAdapter(getContext(), trips);
                recyclerView.setAdapter(tripAdapter);
            }
        });
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                trips.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Trip trip = snapshot.getValue(Trip.class);
//                    trips.add(trip);
//                }
//                tripAdapter = new TripAdapter(getContext(), trips);
//                recyclerView.setAdapter(tripAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
}
