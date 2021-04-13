package com.livecodex.movtik;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.livecodex.movtik.services.MovieData;

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

public class EditDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    /*
     * Receives movie title from the EditMovie Activity
     * Search and get the details of the movie
     * User can change the details and update the movie
     */

    private static final String ORDER_BY = MOVIE_TITLE + " ASC";
    private MovieData movieData;
    private String editMovie;
    private String currentMovieId;

    EditText movieTitleInput;
    EditText movieYearInput;
    EditText movieDirectorInput;
    EditText movieActorInput;
    EditText movieReviewInput;
    RatingBar movieRatingBar;
    Spinner movieFavouriteSpinner;

    String[] favorites_array = {"Not Favourite","Favourite"};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        Intent selectedIntent = getIntent();
        editMovie = selectedIntent.getStringExtra("SelectedMovie");

        movieTitleInput = findViewById(R.id.editMovieTitle_tf);
        movieYearInput = findViewById(R.id.editMovieYear_tf);
        movieDirectorInput = findViewById(R.id.editMovieDirector_tf);
        movieActorInput = findViewById(R.id.editMovieActors_tf);
        movieReviewInput = findViewById(R.id.editMovieReview_tf);
        movieRatingBar = findViewById(R.id.ratingBar);
        movieFavouriteSpinner = findViewById(R.id.favouriteCheck);

        if(savedInstanceState != null){
            String[] inputData = savedInstanceState.getStringArray("movieEditDetails");
            int rating = savedInstanceState.getInt("rating");
            int selected = savedInstanceState.getInt("selection");

            if(movieTitleInput != null) movieTitleInput.setText(inputData[0]);
            if(movieYearInput  != null) movieYearInput.setText(inputData[1]);
            if(movieDirectorInput != null) movieDirectorInput.setText(inputData[2]);
            if(movieActorInput != null) movieActorInput.setText(inputData[3]);
            if(movieReviewInput!= null) movieReviewInput.setText(inputData[4]);

            if(movieRatingBar != null){
                movieRatingBar.setRating(rating);
            }

            if(movieFavouriteSpinner != null){
                movieFavouriteSpinner.setSelection(selected);
            }

        }

        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, favorites_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if(movieFavouriteSpinner != null){
            movieFavouriteSpinner.setAdapter(adapter);
        }

        movieData = new MovieData(this);
        updateFields(getMovieDetails());

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
                String.valueOf(movieReviewInput.getText())
        };

        outState.putStringArray("movieEditDetails", inputData);
        outState.putInt("rating", (int) movieRatingBar.getRating());
        outState.putInt("selection", movieFavouriteSpinner.getSelectedItemPosition());

    }

    private Cursor getMovieDetails(){

        SQLiteDatabase database = movieData.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null,MOVIE_TITLE +" =? ", new String[]{editMovie}, null, null, ORDER_BY);
        return cursor;
    }

    private void updateFields(Cursor cursor){

        while (cursor.moveToNext()){
            currentMovieId = String.valueOf(cursor.getLong(0));
            String movieTitle = cursor.getString(1);
            String movieYear = cursor.getString(2);
            String movieDirector = cursor.getString(3);
            String movieActors = cursor.getString(4);
            String movieRating = cursor.getString(5);
            String movieReview = cursor.getString(6);
            int movieFavourite = cursor.getInt(7);

            movieTitleInput.setText(movieTitle);
            movieYearInput.setText(movieYear);
            movieDirectorInput.setText(movieDirector);
            movieActorInput.setText(movieActors);
            movieReviewInput.setText(movieReview);
            movieRatingBar.setRating(Integer.parseInt(movieRating));
            movieFavouriteSpinner.setSelection(movieFavourite);

        }

        cursor.close();

    }

    public void updateMovieDetails(View view) {

        String movieTitle = movieTitleInput.getText().toString();
        String movieYear = movieYearInput.getText().toString();
        String movieDirector = movieDirectorInput.getText().toString();
        String movieActors = movieActorInput.getText().toString();
        String movieReview = movieReviewInput.getText().toString();

        int movieRating = (int) movieRatingBar.getRating();
        int movieFavourite = movieFavouriteSpinner.getSelectedItemPosition();

        boolean validated = true;

        if(movieTitle.isEmpty() || movieYear.isEmpty() || movieDirector.isEmpty() || movieActors.isEmpty() || movieRating == 0 || movieReview.isEmpty()){
            validated = false;
            Toast.makeText(getApplicationContext(), "All the Fields are Required", Toast.LENGTH_SHORT).show();
        }else{

            if(Integer.parseInt(movieYear) < 1895){
                validated = false;
                movieYearInput.requestFocus();
                movieYearInput.setError("Year Should be greater than 1895");

            }

            if(movieRating < 1 || movieRating > 10 ){
                validated = false;
                movieRatingBar.requestFocus();
                Toast.makeText(getApplicationContext(), "Rating Should be in range 1 - 10",Toast.LENGTH_SHORT).show();
            }
        }

        boolean selectedValue = false;
        if(movieFavourite == 1) selectedValue = true;

        if(validated) {
            updateMovie(movieTitle, movieYear, movieDirector, movieActors, movieRating, movieReview, selectedValue);
            Toast.makeText(getApplicationContext(), movieTitle + " Movie Updated Successfully !!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void updateMovie(String movieTitle, String movieYear, String movieDirector, String movieActors, int movieRating, String movieReview, boolean movieFavourite) {

        try {
            SQLiteDatabase database = movieData.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MOVIE_TITLE, movieTitle);
            values.put(MOVIE_YEAR, movieYear);
            values.put(MOVIE_DIRECTOR, movieDirector);
            values.put(MOVIE_ACTORS, movieActors);
            values.put(MOVIE_RATING,movieRating);
            values.put(MOVIE_REVIEW,movieReview);
            values.put(MOVIE_FAVOURITES,movieFavourite);
            database.updateWithOnConflict(TABLE_NAME,values,_ID + " =? ",new String[]{currentMovieId},0);

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