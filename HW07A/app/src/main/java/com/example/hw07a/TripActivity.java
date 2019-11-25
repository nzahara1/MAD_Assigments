package com.example.hw07a;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TripActivity extends AppCompatActivity {

    User user;
    static final int PROFILE_UPDATE = 200;
    ImageView imageView;
    TextView userName;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        final Intent intent = getIntent();
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        imageView = findViewById(R.id.profile_id);
        if (intent != null) {
            userName = findViewById(R.id.user_id);
            FirebaseAuth auth = LoginActivity.mAuth;
            FirebaseUser currentUser = auth.getCurrentUser();
            userName.setText("Welcome " + currentUser.getEmail() + "!");
            //upload user pic
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(LoginActivity.mAuth.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user = documentSnapshot.toObject(User.class);
                    if (!user.getAvatar_url().isEmpty()) {
                        Picasso.get().load(user.getAvatar_url()).into(imageView);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TripActivity.this, "Unable to fetch user details", Toast.LENGTH_LONG).show();
                    return;
                }
            });
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

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(TripActivity.this, SignUpActivity.class);
                    intent1.putExtra("users", user);
                    startActivityForResult(intent1, PROFILE_UPDATE);
                }
            });
            final ViewPageradapter viewPageradapter = new ViewPageradapter(getSupportFragmentManager());
            viewPageradapter.addFragment(new ChatsFragment(), "Chats");
            viewPageradapter.addFragment(new TripFragment(), "Trips");

            viewPager.setAdapter(viewPageradapter);
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    switch (position){
                        case 0 : ((ChatsFragment)viewPageradapter.getItem(position)).refresh();
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_UPDATE) {
            userName = findViewById(R.id.user_id);
            FirebaseAuth auth = LoginActivity.mAuth;
            FirebaseUser currentUser = auth.getCurrentUser();
            userName.setText("Welcome " + currentUser.getEmail() + "!");
            //upload user pic
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(LoginActivity.mAuth.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    user = documentSnapshot.toObject(User.class);
                    if (!user.getAvatar_url().isEmpty()) {
                        imageView = findViewById(R.id.profile_id);
                        Picasso.get().load(user.avatar_url).into(imageView);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(TripActivity.this, "Unable to fetch user details", Toast.LENGTH_LONG).show();
                    return;
                }
            });

        }
    }

    class ViewPageradapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;

        private ArrayList<String> titles;

        ViewPageradapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        private void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
