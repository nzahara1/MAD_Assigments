package com.example.bmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.nio.file.WatchEvent;

/*
Assignment # - 2
 File Name - MainActivity
 Full name - Noor Zahara, Sowmya Sundar
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("BMI Calculator");

        final TextView weight = findViewById(R.id.weight_val);
        final TextView heightFeet = findViewById(R.id.height_ft_val);
        final TextView heightInches = findViewById(R.id.height_inch_val);
        final TextView bmiStr = findViewById(R.id.bmi_val_string);
        final TextView bmiResult = findViewById(R.id.bmi_result);
        bmiStr.setVisibility(View.INVISIBLE);
        bmiResult.setVisibility(View.INVISIBLE);

        findViewById(R.id.cal_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weightStr = weight.getText().toString();
                String heightFeetStr = heightFeet.getText().toString();
                String heightInchStr = heightInches.getText().toString();

                if (validateInputs(weightStr, heightFeetStr, heightInchStr, weight, heightFeet, heightInches))
                    return;
                Double height = Double.parseDouble(heightFeetStr) * 12 + Double.parseDouble(heightInchStr);
                Double bmi = (Double.parseDouble(weightStr) / (height * height)) * 703;
                Toast.makeText(getApplicationContext(), "BMI Calculated", Toast.LENGTH_LONG).show();
                bmiStr.setVisibility(View.VISIBLE);
                bmiStr.setText("Your BMI:" + bmi.floatValue());
                bmiResult.setVisibility(View.VISIBLE);
                String str = "You are ";
                if (bmi < 18.5) {
                    bmiResult.setText(str + "Underweight");
                } else if (bmi >= 18.5 && bmi <= 24.9) {
                    bmiResult.setText(str + "Normal");
                } else if (bmi >= 25 && bmi <= 29.9) {
                    bmiResult.setText(str + "Overweight");
                } else {
                    bmiResult.setText(str + "Obese");
                }

            }
        });
    }

    /**
     * The function validates the weight and height inputs entered by the user.
     *
     * @param weightStr
     * @param heightFeetStr
     * @param heightInchStr
     * @param weight
     * @param heightFeet
     * @param heightInches
     * @return
     */
    private boolean validateInputs(String weightStr, String heightFeetStr, String heightInchStr, TextView weight, TextView heightFeet, TextView heightInches) {
        boolean error_flag = false;
        if (weightStr.equals(null) || weightStr.equals("")) {
            weight.setError("Kindly provide the weight");
            error_flag = true;
        }
        if (heightFeetStr.equals(null) || heightFeetStr.equals("")) {
            heightFeet.setError("Kindly provide the height in feet");
            error_flag = true;
        }
        if (heightInchStr.equals(null) || heightInchStr.equals("")) {
            heightInches.setError("Kindly provide the height in feet");
            error_flag = true;
        }
        if (error_flag){ return true; }
        return false;
    }
}
