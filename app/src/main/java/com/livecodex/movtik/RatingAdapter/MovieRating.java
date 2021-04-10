package com.livecodex.movtik.RatingAdapter;

import android.graphics.Bitmap;

public class MovieRating {

    private String movieId;
    private String movieName;
    private Bitmap movieImage;
    private String movieRating;
    private String movieImageUrl;

    public MovieRating(String movieId, String movieName, String movieImageUrl, String movieRating, Bitmap movieImage) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieImageUrl = movieImageUrl;
        this.movieRating = movieRating;
        this.movieImage = movieImage;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Bitmap getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(Bitmap movieImage) {
        this.movieImage = movieImage;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getMovieImageUrl() {
        return movieImageUrl;
    }

    public void setMovieImageUrl(String movieImageUrl) {
        this.movieImageUrl = movieImageUrl;
    }

    @Override
    public String toString() {
        return "MovieRating{" +
                "movieId='" + movieId + '\'' +
                ", movieName='" + movieName + '\'' +
                ", movieImage=" + movieImage +
                ", movieRating='" + movieRating + '\'' +
                ", movieImageUrl='" + movieImageUrl + '\'' +
                '}';
    }
}
