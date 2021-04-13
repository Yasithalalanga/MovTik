package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.livecodex.movtik.services.MovieData;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.livecodex.movtik.services.Constants.MOVIE_FAVOURITES;
import static com.livecodex.movtik.services.Constants.MOVIE_TITLE;
import static com.livecodex.movtik.services.Constants.TABLE_NAME;

public class FavouritesActivity extends AppCompatActivity {

    /*
    * Shows all the Favourite Movies
    * Unchecked Movies will be removed from favourites
    */

    private static final String[] FROM = { _ID, MOVIE_TITLE};
    private static final String ORDER_BY = MOVIE_TITLE + " ASC";
    private MovieData movieData;

    private ListView favouriteMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        movieData = new MovieData(this);
        favouriteMoviesList = findViewById(R.id.movieTitlesList);

        showingFavourites();

    }

    private void showingFavourites() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, favList(getMovies()));
        favouriteMoviesList.setAdapter(adapter);

        for(int position = 0; position < favouriteMoviesList.getCount(); position++){
            favouriteMoviesList.setItemChecked(position,true);
        }
    }

    private Cursor getMovies(){

        SQLiteDatabase database = movieData.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, FROM,MOVIE_FAVOURITES +" =? ", new String[]{"1"}, null, null, ORDER_BY);
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

    public void updateTable(String movieName, boolean value){

        try {
            SQLiteDatabase database = movieData.getWritableDatabase();
            ContentValues updatedValues = new ContentValues();
            updatedValues.put(MOVIE_FAVOURITES, value);
            database.updateWithOnConflict(TABLE_NAME,updatedValues,MOVIE_TITLE + " =? ",new String[]{movieName},0);

        }finally {
            movieData.close();
        }
    }


    public void saveFavourites(View view) {

        for(int item = 0; item < favouriteMoviesList.getCount(); item++){
            if(!favouriteMoviesList.isItemChecked(item)){
                updateTable((String) favouriteMoviesList.getItemAtPosition(item),false);
            }
        }

        showingFavourites();
    }
}