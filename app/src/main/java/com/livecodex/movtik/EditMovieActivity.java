package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.livecodex.movtik.services.MovieData;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.livecodex.movtik.services.Constants.MOVIE_FAVOURITES;
import static com.livecodex.movtik.services.Constants.MOVIE_TITLE;
import static com.livecodex.movtik.services.Constants.TABLE_NAME;

public class EditMovieActivity extends AppCompatActivity {

    /*
    * Show all the movies once clicked on will be
    * moved to a new intent to edit the movie Details
    */

    private static final String[] FROM = { _ID, MOVIE_TITLE};
    private static final String ORDER_BY = MOVIE_TITLE + " ASC";

    private MovieData movieData;
    private ListView editMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        movieData = new MovieData(this);
        editMovieList = findViewById(R.id.editMovieList);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, movieList(getMovies()));
        editMovieList.setAdapter(adapter);

        editMovieList.setOnItemClickListener((adapterView, view, i, l) -> {

            String selectedMovie = (String) editMovieList.getItemAtPosition(i);
            Intent editIntent = new Intent(EditMovieActivity.this, EditDetailsActivity.class);
            editIntent.putExtra("SelectedMovie", selectedMovie);
            startActivity(editIntent);

        });
    }

    private Cursor getMovies(){

        SQLiteDatabase database = movieData.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, FROM, null, null, null, null, ORDER_BY);
        return cursor;
    }

    private List<String> movieList(Cursor cursor){
        List<String> movieTitles = new ArrayList<>();

        if(cursor.moveToFirst()){
            do {
                String movieTitle = cursor.getString(1);
                movieTitles.add(movieTitle);
            }while (cursor.moveToNext());
        }

        cursor.close();
        return movieTitles;
    }
}