package com.livecodex.movtik.RatingAdapter;

import android.graphics.Bitmap;

public class MovieRating {

    private String movieName;
    private String movieRating;
    private Bitmap movieImage;

    public MovieRating(String movieName, String movieRating) {
        this.movieName = movieName;
        this.movieRating = movieRating;
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

    @Override
    public String toString() {
        return "MovieRating{" +
                "movieName='" + movieName + '\'' +
                ", movieRating='" + movieRating + '\'' +
                ", movieImage=" + movieImage +
                '}';
    }
}
