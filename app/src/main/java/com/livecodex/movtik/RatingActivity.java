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
import android.widget.Toast;

import com.livecodex.movtik.services.MovieData;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.livecodex.movtik.services.Constants.MOVIE_TITLE;
import static com.livecodex.movtik.services.Constants.TABLE_NAME;

public class RatingActivity extends AppCompatActivity {

    private static final String[] FROM = { _ID, MOVIE_TITLE};
    private static final String ORDER_BY = MOVIE_TITLE + " ASC";

    private MovieData movieData;
    ListView ratingMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ratingMovieList = findViewById(R.id.ratingMovieList);
        movieData = new MovieData(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, movieList(getMovies()));
        ratingMovieList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ratingMovieList.setAdapter(adapter);

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

    public void findInIMDB(View view) {
        String selectedMovie = null;

        for(int item = 0; item < ratingMovieList.getCount(); item++){
            if(ratingMovieList.isItemChecked(item)){
                selectedMovie = (String) ratingMovieList.getItemAtPosition(item);
            }
        }

        Toast.makeText(getApplicationContext(),selectedMovie,Toast.LENGTH_SHORT).show();

        Intent movieInfoIntent = new Intent(RatingActivity.this, RatingResultActivity.class);
        movieInfoIntent.putExtra("MovieRequested", selectedMovie);
        startActivity(movieInfoIntent);

    }
}