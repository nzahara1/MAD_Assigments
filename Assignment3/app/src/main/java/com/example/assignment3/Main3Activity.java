package com.example.assignment3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        setTitle("My Profile");

        Bundle intentBundle = getIntent().getExtras();
        final EditText lName = findViewById(R.id.e_last_name);
        final EditText fName = findViewById(R.id.edit_f_name);
        final ImageView imageView = findViewById(R.id.e_image);
        RadioButton mRadio = findViewById(R.id.e_m_radio);
        RadioButton fRadio = findViewById(R.id.e_fradio);
        final RadioGroup radioGroup = findViewById(R.id.e_radio_grp);
        final TextView selectGender = findViewById(R.id.e_select_gender);
        if (intentBundle != null) {
            String[] name = intentBundle.get(MainActivity.NAME).toString().split(" ");
            lName.setText(name[1]);
            fName.setText(name[0]);

            if (intentBundle.get(MainActivity.GENDER).equals("Male")) {
                imageView.setImageDrawable(getDrawable(R.drawable.male));
                mRadio.setChecked(true);
            }

            if (intentBundle.get(MainActivity.GENDER).equals("Female")) {
                imageView.setImageDrawable(getDrawable(R.drawable.female));
                fRadio.setChecked(true);
            }

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    selectGender.setError(null);
                    selectGender.setText("Select Gender");
                    selectGender.setTextColor(Color.GRAY);
                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.e_fradio:
                            imageView.setImageDrawable(getDrawable(R.drawable.female));
                            break;
                        case R.id.e_m_radio:
                            imageView.setImageDrawable(getDrawable(R.drawable.male));
                            break;
                        default:
                            break;
                    }
                }
            });


            findViewById(R.id.e_save_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int checkedIndex = radioGroup.getCheckedRadioButtonId();
                    boolean flag = false;
                    if (fName.getText().toString().isEmpty() || fName.toString() == null) {
                        fName.setError("Kindly fill in the first name");
                        flag = true;
                    }
                    if (lName.getText().toString().isEmpty() || lName.toString() == null) {
                        lName.setError("Kindly fill in the last name");
                        flag = true;
                    }
                    if (!flag) {
                        Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
                        intent.putExtra(MainActivity.NAME, fName.getText().toString() + " " + lName.getText().toString());
                        RadioButton rBtn = findViewById(checkedIndex);
                        intent.putExtra(MainActivity.GENDER, rBtn.getText().toString());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            });
        }
    }

}
