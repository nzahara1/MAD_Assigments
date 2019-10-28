package com.example.homework06;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfile extends Fragment {

    EditText firstName;
    EditText lastName;
    EditText studentId;
    String department = "";
    ITask iTask;
    int imageViewId = -1;
    Profile profile = null;

    public MyProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        view.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //load another fragment
                if (firstName.getText().toString().isEmpty()) {
                    Toast.makeText(getView().getContext(), "First Name cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (lastName.getText().toString().isEmpty()) {
                    Toast.makeText(getView().getContext(), "Last Name cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (studentId.getText().toString().isEmpty()) {
                    Toast.makeText(getView().getContext(), "Student id cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if (studentId.getText().toString().length() > 9) {
                    Toast.makeText(getView().getContext(), "Student id cannot be greater than 9 digits", Toast.LENGTH_LONG).show();
                    return;
                }
                if (department.isEmpty()) {
                    Toast.makeText(getView().getContext(), "Description cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                iTask.changeToDisplay(new Profile(firstName.getText().toString(), lastName.getText().toString(), studentId.getText().toString(), department, imageViewId));
            }
        });

        view.findViewById(R.id.profile_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iTask.changeToAvatar();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            iTask = (ITask) activity;
        } catch (ClassCastException e) {
            Log.d("exception", activity.toString() + " activity has not implemented interface");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (profile == null) {
            String profileString = getActivity().getSharedPreferences("prefs", MODE_PRIVATE).getString(MainActivity.PROFILE, null);
            Gson gson = new Gson();
            profile = gson.fromJson(profileString, Profile.class);
        }

        if (profile != null) {
            loadProfile(profile);
        }
        firstName = getView().findViewById(R.id.first_name);
        lastName = getView().findViewById(R.id.last_name);
        studentId = getView().findViewById(R.id.student_id);
        RadioGroup radioGroup = getView().findViewById(R.id.radio_grp);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.cs_btn:
                        department = "CS";
                        break;
                    case R.id.sis_btn:
                        department = "SIS";
                        break;
                    case R.id.bio_btn:
                        department = "BIO";
                        break;
                    case R.id.other_btn:
                        department = "Other";
                        break;
                    default:
                        break;
                }
            }
        });
        if (imageViewId != -1) {
            ImageView imageView = getView().findViewById(R.id.profile_load);
            imageView.setImageDrawable(getResources().getDrawable(imageViewId));
        }


    }

    public void loadProfile(Profile profile) {
        firstName = getView().findViewById(R.id.first_name);
        lastName = getView().findViewById(R.id.last_name);
        studentId = getView().findViewById(R.id.student_id);
        ImageView imageView = getView().findViewById(R.id.profile_load);
        imageView.setImageDrawable(getResources().getDrawable(profile.getImageDrawable()));
        firstName.setText(profile.getFirstName());
        lastName.setText(profile.getLastName());
        studentId.setText(profile.getStudentId());
        imageViewId = profile.getImageDrawable();
        department = profile.getDepartment();
        RadioGroup radioGroup = getView().findViewById(R.id.radio_grp);
        switch (profile.getDepartment()) {
            case "CS":
                radioGroup.check(R.id.cs_btn);
                break;
            case "SIS":
                radioGroup.check(R.id.sis_btn);
                break;
            case "BIO":
                radioGroup.check(R.id.bio_btn);
                break;
            case "Other":
                radioGroup.check(R.id.other_btn);
                break;

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public interface ITask {
        public void changeToDisplay(Profile profile);

        public void changeToAvatar();

    }
}
