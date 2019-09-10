package com.example.assignment3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    public static int REQ_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("My Profile");

        Bundle intentBundle = getIntent().getExtras();

        if (intentBundle != null) {
            final ImageView userImage = findViewById(R.id.user_image);
            final TextView userName = findViewById(R.id.name_val);
            final TextView userGender = findViewById(R.id.user_gender);
            final String name;
            final String gender;
            name = intentBundle.get(MainActivity.NAME).toString();
            gender = intentBundle.get(MainActivity.GENDER).toString();
            userName.setText(name);
            userGender.setText(gender);
            if (gender.equals("Male")) {
                userImage.setImageDrawable(getDrawable(R.drawable.male));
            } else if (gender.equals("Female")) {
                userImage.setImageDrawable(getDrawable(R.drawable.female));
            }

            editListener(name, gender);
        }
    }

    private void editListener(final String name, final String gender) {
        findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
                intent.putExtra(MainActivity.NAME, name);
                intent.putExtra(MainActivity.GENDER, gender);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Main2Activity.REQ_CODE && resultCode == RESULT_OK) {
            final ImageView userImage = findViewById(R.id.user_image);
            final TextView userName = findViewById(R.id.name_val);
            final TextView userGender = findViewById(R.id.user_gender);
            final String name;
            final String gender;
            Bundle intentBundle = data.getExtras();
            if (intentBundle != null) {
                name = intentBundle.get(MainActivity.NAME).toString();
                gender = intentBundle.get(MainActivity.GENDER).toString();
                userName.setText(name);
                userGender.setText(gender);
                if (gender.equals("Male")) {
                    userImage.setImageDrawable(getDrawable(R.drawable.male));
                } else if (gender.equals("Female")) {
                    userImage.setImageDrawable(getDrawable(R.drawable.female));
                }
                editListener(name, gender);
            }
        }
    }
}
