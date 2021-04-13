package com.livecodex.movtik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.livecodex.movtik.services.MovieData;

import java.text.DateFormat;
import java.util.Calendar;

import static android.provider.BaseColumns._ID;
import static com.livecodex.movtik.services.Constants.MOVIE_ACTORS;
import static com.livecodex.movtik.services.Constants.MOVIE_DIRECTOR;
import static com.livecodex.movtik.services.Constants.MOVIE_FAVOURITES;
import static com.livecodex.movtik.services.Constants.MOVIE_RATING;
import static com.livecodex.movtik.services.Constants.MOVIE_REVIEW;
import static com.livecodex.movtik.services.Constants.MOVIE_TITLE;
import static com.livecodex.movtik.services.Constants.MOVIE_YEAR;
import static com.livecodex.movtik.services.Constants.TABLE_NAME;

public class RegisterMovieActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    /*
    * user can register a movie using this activity
    * Registered Movie will be added to the database
    */

    private MovieData movieData;

    EditText movieTitleInput;
    EditText movieYearInput;
    EditText movieDirectorInput;
    EditText movieActorInput;
    EditText movieRatingInput;
    EditText movieReviewInput;

    @SuppressLint("ClickableViewAccessibility")
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

        if(savedInstanceState != null){
            String[] inputData = savedInstanceState.getStringArray("movieInputData");

            if(movieTitleInput != null) movieTitleInput.setText(inputData[0]);
            if(movieYearInput  != null) movieYearInput.setText(inputData[1]);
            if(movieDirectorInput != null) movieDirectorInput.setText(inputData[2]);
            if(movieActorInput != null) movieActorInput.setText(inputData[3]);
            if(movieRatingInput!= null) movieRatingInput.setText(inputData[4]);
            if(movieReviewInput!= null) movieReviewInput.setText(inputData[5]);

        }

        movieYearInput.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (movieYearInput.getRight() - movieYearInput.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))  {

                        DialogFragment datePicker = new DatePickerFragment();
                        datePicker.show(getSupportFragmentManager(), "date picker");
                        return true;
                    }
                }
                return false;
            }
        });

    }

    // Save Instance Data
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        String[] inputData = new String[]{
                String.valueOf(movieTitleInput.getText()),
                String.valueOf(movieYearInput.getText()),
                String.valueOf(movieDirectorInput.getText()),
                String.valueOf(movieActorInput.getText()),
                String.valueOf(movieRatingInput.getText()),
                String.valueOf(movieReviewInput.getText())
        };

        outState.putStringArray("movieInputData", inputData);

    }

    // Getting data from tht user and saving it in the sqLite database
    public void saveData(View view) {

        String movieTitle = movieTitleInput.getText().toString().trim();
        String movieYear = movieYearInput.getText().toString().trim();
        String movieDirector = movieDirectorInput.getText().toString().trim();
        String movieActors = movieActorInput.getText().toString().trim();
        String movieRating = movieRatingInput.getText().toString().trim();
        String movieReview = movieReviewInput.getText().toString().trim();

        boolean validated = true;

        if(movieTitle.isEmpty() || movieYear.isEmpty() || movieDirector.isEmpty() || movieActors.isEmpty() || movieRating.isEmpty() || movieReview.isEmpty()){
            validated = false;
            Toast.makeText(getApplicationContext(), "All the Fields are Required", Toast.LENGTH_SHORT).show();
        }else{

            if(Integer.parseInt(movieYear) < 1895){
                validated = false;
                movieYearInput.requestFocus();
                movieYearInput.setError("Year Should be greater than 1895");

            }

            if(Integer.parseInt(movieRating) < 1 || Integer.parseInt(movieRating) > 10 ){
                validated = false;
                movieRatingInput.requestFocus();
                movieRatingInput.setError("Rating Should be in range 1 - 10");
            }
        }

        if(validated) {

            registerMovie(movieTitle, movieYear, movieDirector, movieActors, Integer.parseInt(movieRating), movieReview);
            Toast.makeText(getApplicationContext(), "Movie Registered Successfully !!", Toast.LENGTH_SHORT).show();

            movieYearInput.setText("");
            movieActorInput.setText("");
            movieTitleInput.setText("");
            movieRatingInput.setText("");
            movieReviewInput.setText("");
            movieDirectorInput.setText("");
        }

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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        String Year = String.valueOf(calendar.get(Calendar.YEAR));
        movieYearInput.setText(Year);
    }
}