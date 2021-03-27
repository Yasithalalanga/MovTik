package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        Intent selectedIntent = getIntent();
        String editMovie = selectedIntent.getStringExtra("SelectedMovie");

        movieData = new MovieData(this);
    }

    private Cursor getMovies(){

        String[] updateArgs = new String[1];
        updateArgs[0] = String.valueOf(1);

        SQLiteDatabase database = movieData.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, FROM,MOVIE_FAVOURITES +" =? ", updateArgs, null, null, ORDER_BY);
        return cursor;
    }

    private List<String> favList(Cursor cursor){
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