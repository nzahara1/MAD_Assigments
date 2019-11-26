package com.example.inclass13;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


//AIzaSyAdLZSZC6naDeztF-_-lPLTRXGM6O5cWM4
public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TripAdapter tripAdapter;
    List<Trip> trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        trips = new ArrayList<>();
        tripAdapter = new TripAdapter(this, trips);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tripAdapter);
        findViewById(R.id.trip_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 10) {
            if (data != null) {
                Log.d("data", data.getSerializableExtra("trip").toString());
                trips.add((Trip) data.getSerializableExtra("trip"));
                tripAdapter.notifyDataSetChanged();
            }
        }
    }
}


