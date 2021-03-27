package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.livecodex.movtik.services.MovieData;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.livecodex.movtik.services.Constants.MOVIE_FAVOURITES;
import static com.livecodex.movtik.services.Constants.MOVIE_TITLE;
import static com.livecodex.movtik.services.Constants.TABLE_NAME;

public class EditDetailsActivity extends AppCompatActivity {

    private static final String[] FROM = { _ID, MOVIE_TITLE};
    private static final String ORDER_BY = MOVIE_TITLE + " ASC";
    private MovieData movieData;;
    private String editMovie;

    EditText movieTitleInput;
    EditText movieYearInput;
    EditText movieDirectorInput;
    EditText movieActorInput;
    EditText movieRatingInput;
    EditText movieReviewInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        Intent selectedIntent = getIntent();
        editMovie = selectedIntent.getStringExtra("SelectedMovie");

        movieData = new MovieData(this);
        updateFields(getMovieDetails());
    }

    private Cursor getMovieDetails(){

        SQLiteDatabase database = movieData.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, null,MOVIE_TITLE +" =? ", new String[]{editMovie}, null, null, ORDER_BY);
        return cursor;
    }

    private void updateFields(Cursor cursor){
        boolean available = false;

        while (cursor.moveToNext()){
            String movieTitle = cursor.getString(1);

        }

        if (available){
            Toast.makeText(getApplicationContext(),"hello", Toast.LENGTH_SHORT).show();
        }
        cursor.close();

    }

}