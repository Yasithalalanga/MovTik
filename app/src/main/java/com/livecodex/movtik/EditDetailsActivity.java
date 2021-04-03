package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.livecodex.movtik.services.MovieData;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.livecodex.movtik.services.Constants.MOVIE_ACTORS;
import static com.livecodex.movtik.services.Constants.MOVIE_DIRECTOR;
import static com.livecodex.movtik.services.Constants.MOVIE_FAVOURITES;
import static com.livecodex.movtik.services.Constants.MOVIE_RATING;
import static com.livecodex.movtik.services.Constants.MOVIE_REVIEW;
import static com.livecodex.movtik.services.Constants.MOVIE_TITLE;
import static com.livecodex.movtik.services.Constants.MOVIE_YEAR;
import static com.livecodex.movtik.services.Constants.TABLE_NAME;

public class EditDetailsActivity extends AppCompatActivity {

    private static final String[] FROM = { _ID, MOVIE_TITLE};
    private static final String ORDER_BY = MOVIE_TITLE + " ASC";
    private MovieData movieData;;
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

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, favorites_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if(movieFavouriteSpinner != null){
            movieFavouriteSpinner.setAdapter(adapter);
        }

        movieData = new MovieData(this);
        updateFields(getMovieDetails());
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

        boolean selectedValue = false;
        if(movieFavourite == 1) selectedValue = true;

        updateMovie(movieTitle, movieYear, movieDirector, movieActors, movieRating, movieReview,selectedValue);
        Toast.makeText(getApplicationContext(), "Movie Updates Successfully !!" , Toast.LENGTH_SHORT).show();

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
}