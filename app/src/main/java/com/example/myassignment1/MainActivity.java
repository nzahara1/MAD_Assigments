package com.example.myassignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Area Calculator");


        final TextView tv = findViewById(R.id.select_text);
        final EditText length1 = findViewById(R.id.shape_length_1);
        final EditText length2 = findViewById(R.id.shape_length_2);
        final TextView resultTv = findViewById(R.id.result_length);


        findViewById(R.id.triangle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText(R.string.triangle);
                length2.setVisibility(View.VISIBLE);
                tv.setError(null);
            }
        });

        findViewById(R.id.square).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText(R.string.square);
                length2.setVisibility(View.INVISIBLE);
                tv.setError(null);
            }
        });

        findViewById(R.id.circle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText(R.string.circle);
                findViewById(R.id.shape_length_2).setVisibility(View.INVISIBLE);
                tv.setError(null);
            }
        });

        findViewById(R.id.calculate_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv.getText().equals("Select a shape")) {
                    tv.setError("Kindly select a shape");
                    return;
                }
                double area = 0.0;
                switch (tv.getText().toString()) {
                    case "Triangle":
                        String baseLength = length1.getText().toString();
                        String heigthLength = length2.getText().toString();
                        if (baseLength.equals("") || baseLength.equals(null)){
                            length1.setError("Enter a value");
                            break;
                        }
                        if (heigthLength.equals("") || heigthLength.equals(null)) {
                            length2.setError("Enter a value");
                            break;
                        }
                        area = .5 * Double.parseDouble(baseLength) * Double.parseDouble(heigthLength);
                        resultTv.setText(String.valueOf(area));
                        break;
                    case "Square":
                        String sideLength = length1.getText().toString();
                        if (sideLength.equals("") || sideLength.equals(null)) {
                            length1.setError("Enter a value");
                            return;
                        }
                        area = Double.parseDouble(sideLength) * Double.parseDouble(sideLength);
                        resultTv.setText(String.valueOf(area));
                        break;
                    case "Circle":
                        String radius = length1.getText().toString();
                        if (radius.equals("") || radius.equals(null)) {
                            length1.setError("Enter a value");
                            return;
                        }
                        area = Math.PI * Double.parseDouble(length1.getText().toString()) * Double.parseDouble(length1.getText().toString());
                        resultTv.setText(String.valueOf(area));
                        break;
                    default:
                        break;
                }

            }
        });

        findViewById(R.id.clear_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv.setText("Select a shape");
                length1.setVisibility(View.VISIBLE);
                length2.setVisibility(View.VISIBLE);
                length1.setText("");
                length2.setText("");
                tv.setError(null);
                length2.setError(null);
                length1.setError(null);
                resultTv.setText("");
            }
        });
    }

}
