package com.example.homework04;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    List<Movie> movie = new ArrayList<Movie>();
    static String MOVIE_OBJ = "movie_obj";
    static String MOVIE_LIST = "movie_list";
    boolean isDelete = false;
    String movieSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button deleteBtn = findViewById(R.id.delete_btn);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] movieNames = movie.stream().map((movie) -> movie.getName()).collect(Collectors.toList()).toArray(new String[movie.size()]);
        builder.setTitle("Pick a Movie").setItems(movie.stream().map((movie) -> movie.getName()).collect(Collectors.toList()).toArray(new String[movie.size()]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isDelete) {
                    movie = movie.stream().filter(movie -> movie.getName().equals(movieNames[i])).collect(Collectors.toList());
                }
            }
        }).create();

        findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMovieActivity.class);
                startActivityForResult(intent, 10);
            }
        });

        findViewById(R.id.year_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie.size() <= 0) {
                    Toast.makeText(MainActivity.this, "Movie list is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent("com.example.homework04.ViewYear");
                intent.addCategory("android.intent.category.DEFAULT");
                movie.sort((Movie m1, Movie m2) -> m1.getYear() - m2.getYear());
                intent.putExtra(MOVIE_LIST, (Serializable) movie);
                startActivity(intent);
            }
        });

        findViewById(R.id.rating_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie.size() <= 0) {
                    Toast.makeText(MainActivity.this, "Movie list is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent("com.example.homework04.ViewRating");
                intent.addCategory("android.intent.category.DEFAULT");
                movie.sort((Movie m1, Movie m2) -> m2.getRating() - m1.getRating());
                intent.putExtra(MOVIE_LIST, (Serializable) movie);
                startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie.size() <= 0) {
                    Toast.makeText(MainActivity.this, "Movie list is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                String[] movieNames = movie.stream().map((movie) -> movie.getName()).collect(Collectors.toList()).toArray(new String[movie.size()]);
                builder.setTitle("Pick a Movie").setItems(movie.stream().map((movie) -> movie.getName()).collect(Collectors.toList()).toArray(new String[movie.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        movie.removeIf(movie -> movie.getName().equals(movieNames[i]));
                        Toast.makeText(MainActivity.this, "The movie : " + movieNames[i] + " has been deleted", Toast.LENGTH_LONG).show();
                    }
                }).create().show();
            }
        });

        findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movie.size() <= 0) {
                    Toast.makeText(MainActivity.this, "Movie list is empty", Toast.LENGTH_LONG).show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                String[] movieNames = movie.stream().map((movie) -> movie.getName()).collect(Collectors.toList()).toArray(new String[movie.size()]);
                builder.setTitle("Pick a Movie").setItems(movie.stream().map((movie) -> movie.getName()).collect(Collectors.toList()).toArray(new String[movie.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Movie movieObj = movie.stream().
                                filter(p -> p.getName().equals(movieNames[i])).
                                findAny().orElse(null);
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        intent.putExtra(MOVIE_OBJ, movieObj);
                        movieSelected = movieNames[i];
                        startActivityForResult(intent, 10);
                    }
                }).create().show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == 20 && data != null) {
                movie.add(((Movie) data.getSerializableExtra(MOVIE_OBJ)));
            }
            if (resultCode == 25 && data != null) {
                movie.removeIf(movie -> movie.getName().equals(movieSelected));
                movie.add((Movie) data.getExtras().get(MOVIE_OBJ));
            }
        }
    }
}
