package com.example.homework04;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class DisplayListYearActivity extends AppCompatActivity {

    EditText nameText;
    EditText description;
    TextView genreVal;
    EditText year;
    EditText imdb;
    int selectedIndex = 0;
    TextView ratingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list_year);

        final ImageButton firstMovie = findViewById(R.id.first_movie);
        final ImageButton lastMovie = findViewById(R.id.last_movie);
        final ImageButton prevMovie = findViewById(R.id.prev_movie);
        final ImageButton nextMovie = findViewById(R.id.next_movie);
        nameText = findViewById(R.id.name_id);
        description = findViewById(R.id.desc_val);
        genreVal = findViewById(R.id.genre_val);
        year = findViewById(R.id.year_val);
        imdb = findViewById(R.id.imdb_val);
        ratingView = findViewById(R.id.rating_value);
        Button finishBtn = findViewById(R.id.finish_btn);

        Intent intent = getIntent();


        if (intent != null) {
            List<Movie> movieData = (List<Movie>) intent.getExtras().get(MainActivity.MOVIE_LIST);
            setMovieContent(movieData.get(0));
            firstMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedIndex = 0;
                    setMovieContent(movieData.get(selectedIndex));
                }
            });

            lastMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedIndex = movieData.size() - 1;
                    setMovieContent(movieData.get(selectedIndex));
                }
            });

            nextMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedIndex == movieData.size() - 1) {
                        selectedIndex = 0;
                    } else {
                        selectedIndex += 1;
                    }
                    setMovieContent(movieData.get(selectedIndex));
                }
            });

            prevMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedIndex <= 0) {
                        selectedIndex = movieData.size() - 1;
                    } else {
                        selectedIndex -= 1;
                    }
                    setMovieContent(movieData.get(selectedIndex));
                }
            });
            finishBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

    }

    private void setMovieContent(Movie movie) {
        nameText.setText(movie.getName());
        description.setText(movie.getDescription());
        genreVal.setText(movie.getGenre());
        year.setText(String.valueOf(movie.getYear()));
        imdb.setText(movie.getImdb());
        ratingView.setText(movie.getRating() + "/ 5");
    }
}
