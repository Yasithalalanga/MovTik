package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.livecodex.movtik.services.MovieData;

import static android.provider.BaseColumns._ID;
import static com.livecodex.movtik.services.Constants.MOVIE_ACTORS;
import static com.livecodex.movtik.services.Constants.MOVIE_DIRECTOR;
import static com.livecodex.movtik.services.Constants.MOVIE_FAVOURITES;
import static com.livecodex.movtik.services.Constants.MOVIE_RATING;
import static com.livecodex.movtik.services.Constants.MOVIE_REVIEW;
import static com.livecodex.movtik.services.Constants.MOVIE_TITLE;
import static com.livecodex.movtik.services.Constants.MOVIE_YEAR;
import static com.livecodex.movtik.services.Constants.TABLE_NAME;

public class RegisterMovieActivity extends AppCompatActivity {

    private MovieData movieData;

    EditText movieTitleInput;
    EditText movieYearInput;
    EditText movieDirectorInput;
    EditText movieActorInput;
    EditText movieRatingInput;
    EditText movieReviewInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);

        movieTitleInput = findViewById(R.id.movieTitle_tf);
        movieYearInput = findViewById(R.id.movieYear_tf);
        movieDirectorInput = findViewById(R.id.movieDirector_tf);
        movieActorInput = findViewById(R.id.movieActors_tf);
        movieRatingInput = findViewById(R.id.movieRating_tf);
        movieReviewInput = findViewById(R.id.movieReview_tf);

        movieData = new MovieData(this);

    }

    // Getting data from tht user and saving it in the sqLite database
    public void saveData(View view) {

        String movieTitle = movieTitleInput.getText().toString();
        String movieYear = movieYearInput.getText().toString();
        String movieDirector = movieDirectorInput.getText().toString();
        String movieActors = movieActorInput.getText().toString();
        int movieRating = Integer.parseInt(movieRatingInput.getText().toString());
        String movieReview = movieReviewInput.getText().toString();

        registerMovie(movieTitle, movieYear, movieDirector, movieActors, movieRating, movieReview);
        Toast.makeText(getApplicationContext(), "Movie Registered Successfully !!", Toast.LENGTH_SHORT).show();

        movieYearInput.setText("");
        movieActorInput.setText("");
        movieTitleInput.setText("");
        movieRatingInput.setText("");
        movieReviewInput.setText("");
        movieDirectorInput.setText("");

    }

    private void registerMovie(String movieTitle, String movieYear, String movieDirector, String movieActors, int movieRating, String movieReview) {

        try {
            SQLiteDatabase database = movieData.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MOVIE_TITLE, movieTitle);
            values.put(MOVIE_YEAR, movieYear);
            values.put(MOVIE_DIRECTOR, movieDirector);
            values.put(MOVIE_ACTORS, movieActors);
            values.put(MOVIE_RATING,movieRating);
            values.put(MOVIE_REVIEW,movieReview);
            values.put(MOVIE_FAVOURITES, false);
            database.insertOrThrow(TABLE_NAME, null, values);
        }finally {
            movieData.close();
        }

    }
}