package com.example.hw07a;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CreateTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        final EditText name = findViewById(R.id.trip_name);
        final ImageView image = findViewById(R.id.trip_photo);
        final EditText lat = findViewById(R.id.lat_id);
        final EditText lon = findViewById(R.id.lon_id);
        final EditText chatName = findViewById(R.id.chat_name);

        Intent intent = getIntent();

        if (intent != null) {

            findViewById(R.id.trip_create_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // store the trip
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final Trip trip = new Trip("default", name.getText().toString(), lat.getText().toString(),
                            lon.getText().toString(), chatName.getText().toString());
                    db.collection("trips").document(user.getUid()).set(trip).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // logic for members
                                HashMap<String, String> map = new HashMap<>();
                                map.put("members", user.getUid());
                                db.collection("chatrooms").document(trip.getChatName()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(CreateTripActivity.this, "Trip was successfully created!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                finish();
                            } else {
                                Toast.makeText(CreateTripActivity.this, "There was a probem while creating the trip!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }
}
