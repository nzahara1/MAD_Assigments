package com.example.homework06;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements MyProfile.ITask, SelectAvatar.Image, DisplayMyProfile.DisplayProfile {

    public static String PROFILE = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getSharedPreferences("prefs", MODE_PRIVATE).edit().remove(PROFILE).commit();
        String profileString = getSharedPreferences("prefs", MODE_PRIVATE).getString(PROFILE, null);
        if (profileString != null) {
            setTitle("Display My Profile");
            getFragmentManager().beginTransaction().add(R.id.linear_layout, new DisplayMyProfile(), "display0").commit();
        } else {
            setTitle("My Profile");
            getFragmentManager().beginTransaction().add(R.id.linear_layout, new MyProfile(), "profile0").commit();
        }
    }

    @Override
    public void changeToDisplay(Profile profile) {
        Gson gson = new Gson();
        Log.d("profile", profile.toString());
        getSharedPreferences("prefs", MODE_PRIVATE).edit().putString(PROFILE, gson.toJson(profile)).commit();
        setTitle("Display My Profile");
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.linear_layout, new DisplayMyProfile(), "display0").commit();
    }

    @Override
    public void getImage(int imageView) {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            setTitle("My Profile");
            getFragmentManager().popBackStack();
            MyProfile fragment = (MyProfile) getFragmentManager().findFragmentByTag("profile0");
            fragment.imageViewId = imageView;
            if (fragment.profile != null) {
                fragment.profile.setImageDrawable(imageView);
            }
        }
    }

    @Override
    public void changeToAvatar() {
        setTitle("Select Avatar");
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.linear_layout, new SelectAvatar(), "avatar0").commit();
    }

    @Override
    public void changeDisplay() {
        setTitle("My Profile");
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            getFragmentManager().beginTransaction().replace(R.id.linear_layout, new MyProfile(), "profile0").commit();
        }
    }
}
