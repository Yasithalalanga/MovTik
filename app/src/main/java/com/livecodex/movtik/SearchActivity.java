package com.livecodex.movtik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.livecodex.movtik.services.MovieData;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.livecodex.movtik.services.Constants.MOVIE_ACTORS;
import static com.livecodex.movtik.services.Constants.MOVIE_DIRECTOR;
import static com.livecodex.movtik.services.Constants.MOVIE_FAVOURITES;
import static com.livecodex.movtik.services.Constants.MOVIE_TITLE;
import static com.livecodex.movtik.services.Constants.TABLE_NAME;

public class SearchActivity extends AppCompatActivity {

    /*
    * Search activity is used to search for movies in the database
    * searched under movie name, actors and director
    * results will be shown to the user
    */

    private static final String[] FROM = { _ID, MOVIE_TITLE};
    private static final String ORDER_BY = MOVIE_TITLE + " ASC";

    private MovieData movieData;
    EditText searchInput;
    ListView searchResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.search_tf);
        searchResultList = findViewById(R.id.searchResultList);
        movieData = new MovieData(this);

        if(savedInstanceState != null){
            String searchText = savedInstanceState.getString("searchText");

            if(searchInput != null){
                searchInput.setText(searchText);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("searchText", String.valueOf(searchInput.getText()));

    }

    public void lookupMovie(View view) {

        String searchText = searchInput.getText().toString().toLowerCase().trim();
        Toast.makeText(getApplicationContext(),searchText,Toast.LENGTH_SHORT).show();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchResultList(searchMovies(searchText)));
        searchResultList.setAdapter(adapter);

    }

    private Cursor searchMovies(String searchText) {

        SQLiteDatabase database = movieData.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, FROM,MOVIE_TITLE + "||" + MOVIE_ACTORS+ "||" + MOVIE_DIRECTOR + " LIKE ?",new String[] {"%"+ searchText+ "%" }, null, null, ORDER_BY);
        return cursor;
    }

    private List<String> searchResultList(Cursor cursor){
        List<String> resultMovies = new ArrayList<>();

        if(cursor.moveToFirst()){
            do {
                String movieTitle = cursor.getString(1);
                resultMovies.add(movieTitle);
            }while (cursor.moveToNext());
        }

        cursor.close();

        return resultMovies;

    }
}