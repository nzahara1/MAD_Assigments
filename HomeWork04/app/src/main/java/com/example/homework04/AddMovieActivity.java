package com.example.homework04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        Intent intent = getIntent();

        Button addBtn = findViewById(R.id.add_mov_btn);
        final EditText nameText = findViewById(R.id.name_id);
        final EditText description = findViewById(R.id.desc_val);
        final Spinner spinner = findViewById(R.id.spinner_id);
        final EditText year = findViewById(R.id.year_val);
        final EditText imdb = findViewById(R.id.imdb_val);
        final SeekBar seekBar = findViewById(R.id.seek_bar);
        final TextView seekVal = findViewById(R.id.seek_val);
        seekBar.setMax(5);
        seekBar.setProgress(0);

        SpannableStringBuilder selectText = new SpannableStringBuilder("Select");
        selectText.setSpan(
                new ForegroundColorSpan(Color.GRAY),
                0, // start
                selectText.length(), // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );
        List list = new ArrayList<>();
        list.add(selectText);
        list.add("Action");
        list.add("Animation");
        list.add("Comedy");
        list.add("Documentary");
        list.add("Family");
        list.add("Horror");
        list.add("Crime");
        list.add("Others");

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(arrayAdapter);
        if (intent != null) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    seekVal.setText(String.valueOf(i));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = nameText.getText().toString();
                    String descriptionStr = description.getText().toString();
                    int yearVal = (year.getText().toString() == null || year.getText().toString().isEmpty()) ?
                            0 : Integer.parseInt(year.getText().toString());
                    String imdbVal = imdb.getText().toString();
                    if (validateInputs(name, descriptionStr, yearVal, imdbVal, spinner)) {
                        return;
                    }
                    Movie movie = new Movie(name, descriptionStr, spinner.getSelectedItem().toString(), yearVal, imdbVal, seekBar.getProgress());
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(MainActivity.MOVIE_OBJ, movie);
                    setResult(20, returnIntent);
                    finish();
                }
            });

        }
    }

    private boolean validateInputs(String name, String descriptionStr, int yearVal, String imdbVal, Spinner spinner) {
        if (name == null || name.isEmpty()) {
            Toast.makeText(AddMovieActivity.this, "Movie name cannot be empty", Toast.LENGTH_LONG).show();
            return true;
        }
        if (name.length() > 50) {
            Toast.makeText(AddMovieActivity.this, "Movie name cannot be greater than 50 characters", Toast.LENGTH_LONG).show();
            return true;
        }
        if (descriptionStr == null || descriptionStr.isEmpty()) {
            Toast.makeText(AddMovieActivity.this, "Description cannot be empty", Toast.LENGTH_LONG).show();
            return true;
        }
        if (descriptionStr.length() > 1000) {
            Toast.makeText(AddMovieActivity.this, "Description cannot be greater than 1000 characters", Toast.LENGTH_LONG).show();
            return true;
        }
        if (spinner.getSelectedItem().toString().equals("Select")) {
            Toast.makeText(AddMovieActivity.this, "Genre cannot be empty", Toast.LENGTH_LONG).show();
            return true;
        }
        if (yearVal <= 0) {
            Toast.makeText(AddMovieActivity.this, "Year cannot be empty", Toast.LENGTH_LONG).show();
            return true;
        }
        if (imdbVal == null || imdbVal.isEmpty()) {
            Toast.makeText(AddMovieActivity.this, "IMDB Url cannot be empty", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}
