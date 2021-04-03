package com.livecodex.movtik.RatingAdapter;

import android.graphics.Bitmap;

public class MovieRating {

    private String movieName;
    private String movieRating;
    private Bitmap movieImage;

    public MovieRating(String movieName, String movieRating, Bitmap movieImage) {
        this.movieName = movieName;
        this.movieRating = movieRating;
        this.movieImage = movieImage;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public Bitmap getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(Bitmap movieImage) {
        this.movieImage = movieImage;
    }
}
