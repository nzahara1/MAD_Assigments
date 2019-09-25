package com.example.homework04;

import java.io.Serializable;

public class Movie implements Serializable {

    private String name;

    private String description;

    private String genre;

    private int year;

    private String imdb;

    private int rating;

    public Movie(String name, String description, String genre, int year, String imdb, int rating) {
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.year = year;
        this.imdb = imdb;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public String getImdb() {
        return imdb;
    }

    public int getRating() {
        return rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setImdb(String imdb) {
        this.imdb = imdb;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", year=" + year +
                ", imdb='" + imdb + '\'' +
                ", rating=" + rating +
                '}';
    }
}
