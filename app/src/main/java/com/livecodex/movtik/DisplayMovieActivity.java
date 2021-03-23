package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.livecodex.movtik.services.MovieData;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.livecodex.movtik.services.Constants.MOVIE_TITLE;
import static com.livecodex.movtik.services.Constants.TABLE_NAME;

public class DisplayMovieActivity extends AppCompatActivity {

    private static final String[] FROM = { _ID, MOVIE_TITLE};
    private static final String ORDER_BY = MOVIE_TITLE + " ASC";
    private MovieData movieData;

    private ListView movieTitlesList;
    private TextView movieTitle;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie);

        movieData = new MovieData(this);
        movieTitlesList = findViewById(R.id.movieTitlesList);
        movieTitle = findViewById(R.id.movieTitle);

        movieTitle.setText(movieList(getMovies()).get(0));

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, movieList(getMovies()));
        movieTitlesList.setAdapter(adapter);

    }

    private Cursor getMovies(){
        SQLiteDatabase database = movieData.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, FROM,null, null, null, null, ORDER_BY);
        return cursor;
    }

    private List<String> movieList(Cursor cursor){
        List<String> movieTitles = new ArrayList<>();

        while (cursor.moveToNext()){
            String movieTitle = cursor.getString(1);
            movieTitles.add(movieTitle);
        }
        cursor.close();

        return movieTitles;

    }

    public void addToFavourites(View view) {
    }

//    public void addToFavourites(View view) {
//
//        String itemSelected = "Item : \n";
//
//        for(int item = 0; item < movieTitlesList.getCount(); item++){
//            if(movieTitlesList.isItemChecked(item)){
//                itemSelected += movieTitlesList.getItemAtPosition(item) + "\n";
//            }
//        }
//
//        Toast.makeText(getApplicationContext(), itemSelected, Toast.LENGTH_SHORT).show();
//
//    }
}