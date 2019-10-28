package com.example.homework06;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayMyProfile extends Fragment {

    Profile profile;
    DisplayProfile displayProfile;

    public DisplayMyProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_my_profile, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        displayProfile = (DisplayProfile) activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Gson gson = new Gson();
        String profileStr = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE).getString(MainActivity.PROFILE, null);
        profile = gson.fromJson(profileStr, Profile.class);
        ImageView imageView = getView().findViewById(R.id.update_image);
        TextView name = getView().findViewById(R.id.name_val);
        TextView studentId = getView().findViewById(R.id.studentid_val);
        final TextView department = getView().findViewById(R.id.department_val);
        name.setText(profile.getFirstName() + " " + profile.getLastName());
        studentId.setText(profile.getStudentId());
        department.setText(profile.getDepartment());
        imageView.setImageDrawable(getResources().getDrawable(profile.getImageDrawable()));
        getView().findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayProfile.changeDisplay();
            }
        });
    }

    public void getProfile(Profile profile) {
        this.profile = profile;
    }

    public interface DisplayProfile {
        public void changeDisplay();
    }
}
