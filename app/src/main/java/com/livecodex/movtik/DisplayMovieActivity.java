package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.livecodex.movtik.services.MovieData;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.livecodex.movtik.services.Constants.MOVIE_FAVOURITES;
import static com.livecodex.movtik.services.Constants.MOVIE_TITLE;
import static com.livecodex.movtik.services.Constants.TABLE_NAME;

public class DisplayMovieActivity extends AppCompatActivity {

    /*
    * Display all the movies in the database
    * selected Movies can be added to favourites
    */

    private static final String[] FROM = { _ID, MOVIE_TITLE, MOVIE_FAVOURITES};
    private static final String ORDER_BY = MOVIE_TITLE + " ASC";
    private MovieData movieData;

    private ListView movieTitlesList;
    private List<String> favourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie);

        movieData = new MovieData(this);
        movieTitlesList = findViewById(R.id.movieTitlesList);

        favourites = new ArrayList<String>();
        showMovies();
    }

    // Show all the movies and if already added to favourites item will be disabled once clicked

    private void showMovies() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, movieList(getMovies()));
        movieTitlesList.setAdapter(adapter);

        movieTitlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CheckedTextView checkedView= (CheckedTextView) view;
                for(int element = 0; element < favourites.size(); element++) {
                    if (checkedView.getText().equals(favourites.get(element))){
                        checkedView.setEnabled(false);
                        Toast.makeText(getApplicationContext(),"Item is already added to Favorites", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private Cursor getMovies(){

        SQLiteDatabase database = movieData.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, FROM,null, null, null, null, ORDER_BY);
        return cursor;
    }

    // Read the Database gets the data
    private List<String> movieList(Cursor cursor){
        List<String> movieTitles = new ArrayList<>();

        if(cursor.moveToFirst()){
            do {
                String movieTitle = cursor.getString(1);
                String movieFavourite = cursor.getString(2);

                if(movieFavourite.equals("1")){
                    favourites.add(movieTitle);
                }

                movieTitles.add(movieTitle);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return movieTitles;

    }

    // used to update the database once added to favourites
    public void updateTable(String movieName, boolean value){

        try {
            SQLiteDatabase database = movieData.getReadableDatabase();
            ContentValues updatedValues = new ContentValues();
            updatedValues.put(MOVIE_FAVOURITES, value);
            database.updateWithOnConflict(TABLE_NAME,updatedValues,MOVIE_TITLE + " =? ",new String[]{movieName},0);

        }finally {
            movieData.close();
        }
    }

    // Set the status of favorites to true if favoured
    public void addToFavourites(View view) {

        boolean itemsAdded = false;

        for(int item = 0; item < movieTitlesList.getCount(); item++){
            if(movieTitlesList.isItemChecked(item)){
                updateTable((String) movieTitlesList.getItemAtPosition(item),true);
                itemsAdded = true;
            }
        }

        if(itemsAdded) {
            Toast.makeText(getApplicationContext(), "Selected Movies Added To Favourites !!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "No Movies Added To Favorites.", Toast.LENGTH_SHORT).show();
        }
        showMovies();

    }

}