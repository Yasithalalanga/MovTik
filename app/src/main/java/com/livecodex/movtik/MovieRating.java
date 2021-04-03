package com.livecodex.movtik;

public class MovieRating {

    private String movieName;
    private String movieRating;
    private int movieImage;

    public MovieRating(String movieName, String movieRating, int movieImage) {
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

    public int getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(int movieImage) {
        this.movieImage = movieImage;
    }
}
