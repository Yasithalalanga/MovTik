package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.livecodex.movtik.RatingAdapter.MovieRating;
import com.livecodex.movtik.RatingAdapter.MovieRatingAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RatingResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private String movieSearch;
    Bitmap imageSet;
    private static final String API_KEY = "k_iklh8ner";

    ArrayList<MovieRating> movieRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_result);

        // testResult = findViewById(R.id.testResult);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieRatings = new ArrayList<>();

        Intent ratingSelectedIntent = getIntent();
        movieSearch = ratingSelectedIntent.getStringExtra("MovieRequested");

        // testResult.setText(movieSearch);

        searchInIMDB(movieSearch.trim());


    }

    private void searchInIMDB(String movieName) {

        ArrayList<MovieRating> moviesAvailable = new ArrayList<>();

        Thread search = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    URL resultUrl = new URL("https://imdb-api.com/en/API/SearchTitle/" + API_KEY + "/" + movieName);
                    HttpURLConnection conn = (HttpURLConnection) resultUrl.openConnection();

                    InputStream inputStream = conn.getInputStream();
                    String contentAsString = convertIsToString(inputStream);

                    JSONObject jsonReturned = new JSONObject(contentAsString);
                    JSONArray resultData = jsonReturned.getJSONArray("results");

                    Log.d("OUTPUT STATS", String.valueOf(resultData));

                    for (int object = 0; object < resultData.length(); object++) {

                        JSONObject movieObject = resultData.getJSONObject(object);
                        String movieTitle = movieObject.getString("title");
                        String movieUrl = movieObject.getString("image");
                        String movieId = movieObject.getString("id");

                        String movieRating = getMovieRating(movieId);
                        Bitmap movieImage = getImageBitmap(movieUrl);

                        MovieRating movie = new MovieRating(movieId, movieTitle, movieUrl, movieRating,movieImage);
                        moviesAvailable.add(movie);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), moviesAvailable.get(0).toString(),Toast.LENGTH_SHORT).show();
                        MovieRatingAdapter movieRatingAdapter = new MovieRatingAdapter(moviesAvailable, RatingResultActivity.this);
                        recyclerView.setAdapter(movieRatingAdapter);
                    }
                });

            }
        });

        search.start();
    }


    private String getMovieRating(String movieId) {

        String totalRating = "";

            try {

                URL imageUrl = new URL("https://imdb-api.com/en/API/UserRatings/" + API_KEY + "/" + movieId);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();

                InputStream ratingStream = conn.getInputStream();
                String contentAsString = convertIsToString(ratingStream);

                JSONObject jsonValue = new JSONObject(contentAsString);
                totalRating = jsonValue.getString("totalRating");

                Log.d("OUTPUT STATS", String.valueOf(jsonValue));

            }catch (Exception ex){
                ex.printStackTrace();
            }

            if(totalRating.equalsIgnoreCase("null") || totalRating.equals("")){
                totalRating = "Nah";
            }

            return totalRating;
    }


    private Bitmap getImageBitmap(String movieUrl) {

        Bitmap imageBitmap = null;

            try {

                URL imageUrl = new URL(movieUrl);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();

                BufferedInputStream bufferedStream = new BufferedInputStream(conn.getInputStream());
                imageBitmap = BitmapFactory.decodeStream(bufferedStream);


            }catch (Exception ex){
                ex.printStackTrace();
            }

            return imageBitmap;
    }


    public String convertIsToString(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }
        if (builder.length() == 0) {
            return null;
        }
        reader.close();
        return builder.toString();
    }

}


