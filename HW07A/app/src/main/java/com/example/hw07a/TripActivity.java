package com.example.hw07a;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class TripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        final Intent intent = getIntent();
        if (intent != null) {
            findViewById(R.id.create_trip_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(TripActivity.this, CreateTripActivity.class);
                    startActivity(intent1);
                    finish();
                }
            });

            findViewById(R.id.signout_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth auth = LoginActivity.mAuth;
                    auth.signOut();
                    finish();
                }
            });
        }
    }
}
