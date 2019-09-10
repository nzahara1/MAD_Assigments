package com.example.assignment3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * Assignment # 3
 * Noor Zahara
 * Sowmya Sundar
 */

public class MainActivity extends AppCompatActivity {

    public static String NAME = "Name";
    public static String GENDER = "Gender";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("My Profile");

        final EditText fName = findViewById(R.id.first_name);
        final EditText lName = findViewById(R.id.last_name);
        final RadioButton fRadio = findViewById(R.id.female_radio);
        final RadioButton mRadio = findViewById(R.id.male_radio);
        final ImageView imageView = findViewById(R.id.gender_image);
        final TextView selectGender = findViewById(R.id.select_gender);

        final RadioGroup radioGroup = findViewById(R.id.radio_grp);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectGender.setError(null);
                selectGender.setText("Select Gender");
                selectGender.setTextColor(Color.GRAY);
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.female_radio:
                        imageView.setImageDrawable(getDrawable(R.drawable.female));
                        break;
                    case R.id.male_radio:
                        imageView.setImageDrawable(getDrawable(R.drawable.male));
                        break;
                    default:
                        break;
                }
            }
        });


        findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = fName.getText().toString();
                String lastName = lName.getText().toString();
                int checkedIndex = radioGroup.getCheckedRadioButtonId();
                boolean errorFlag = false;
                if (firstName.isEmpty() || firstName == null) {
                    fName.setError("Kindly fill in the first name");
                    errorFlag = true;
                }
                if (lastName.isEmpty() || lastName == null) {
                    lName.setError("Kindly fill in the last name");
                    errorFlag = true;
                }
                if (checkedIndex < 0) {
                    selectGender.setText("Kindly choose a gender");
                    selectGender.setTextColor(Color.RED);
                    errorFlag = true;
                }
                if (errorFlag) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra(NAME, firstName + " " + lastName);
                RadioButton rBtn = findViewById(checkedIndex);
                intent.putExtra(GENDER, rBtn.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }
}