package com.example.homework04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class EditActivity extends AppCompatActivity {

    EditText nameText;
    EditText description;
    Spinner spinner;
    EditText year;
    EditText imdb;
    SeekBar seekBar;
    TextView seekVal;
    Movie movieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();

        Button addBtn = findViewById(R.id.add_mov_btn);
        nameText = findViewById(R.id.name_id);
        description = findViewById(R.id.desc_val);
        spinner = findViewById(R.id.spinner_id);
        year = findViewById(R.id.year_val);
        imdb = findViewById(R.id.imdb_val);
        seekBar = findViewById(R.id.seek_bar);
        seekVal = findViewById(R.id.seek_val);
        seekBar.setMax(5);
        seekBar.setProgress(0);

        List<String> list = new ArrayList<String>();
        list.add("Select");
        list.add("Action");
        list.add("Animation");
        list.add("Comedy");
        list.add("Documentary");
        list.add("Family");
        list.add("Horror");
        list.add("Crime");
        list.add("Others");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(arrayAdapter);

        if (intent != null) {
            movieData = (Movie) intent.getSerializableExtra(MainActivity.MOVIE_OBJ);
            setMovieContent(movieData);

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
        }
        findViewById(R.id.save_movie).setOnClickListener(new View.OnClickListener() {
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
                movieData.setName(nameText.getText().toString());
                movieData.setDescription(descriptionStr);
                movieData.setGenre(spinner.getSelectedItem().toString());
                movieData.setRating(seekBar.getProgress());
                movieData.setImdb(imdbVal);
                movieData.setYear(yearVal);
                Intent intent1 = new Intent();
                intent1.putExtra(MainActivity.MOVIE_OBJ, movieData);
                setResult(25, intent1);
                finish();
            }
        });
    }

    private boolean validateInputs(String name, String descriptionStr, int yearVal, String imdbVal, Spinner spinner) {
        if (name == null || name.isEmpty()) {
            Toast.makeText(EditActivity.this, "Movie name cannot be empty", Toast.LENGTH_LONG).show();
            return true;
        }
        if (name.length() > 50) {
            Toast.makeText(EditActivity.this, "Movie name cannot be greater than 50 characters", Toast.LENGTH_LONG).show();
            return true;
        }
        if (descriptionStr == null || descriptionStr.isEmpty()) {
            Toast.makeText(EditActivity.this, "Description cannot be empty", Toast.LENGTH_LONG).show();
            return true;
        }
        if (descriptionStr.length() > 1000) {
            Toast.makeText(EditActivity.this, "Description cannot be greater than 1000 characters", Toast.LENGTH_LONG).show();
            return true;
        }
        if (spinner.getSelectedItem() == "Select") {
            Toast.makeText(EditActivity.this, "Genre cannot be empty", Toast.LENGTH_LONG).show();
            return true;
        }
        if (yearVal <= 0) {
            Toast.makeText(EditActivity.this, "Year cannot be empty", Toast.LENGTH_LONG).show();
            return true;
        }
        if (imdbVal == null || imdbVal.isEmpty()) {
            Toast.makeText(EditActivity.this, "IMDB Url cannot be empty", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private void setMovieContent(Movie movie) {
        nameText.setText(movie.getName());
        description.setText(movie.getDescription());
        seekBar.setProgress(movie.getRating());
        seekVal.setText(String.valueOf(movie.getRating()));
        year.setText(String.valueOf(movie.getYear()));
        imdb.setText(movie.getImdb());
        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter();
        int spinnerPosition = myAdap.getPosition(movie.getGenre());
        spinner.setSelection(spinnerPosition);
    }
}
