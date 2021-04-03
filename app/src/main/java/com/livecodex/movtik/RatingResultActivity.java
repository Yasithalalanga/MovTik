package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.livecodex.movtik.RatingAdapter.MovieRating;
import com.livecodex.movtik.RatingAdapter.MovieRatingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class RatingResultActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private String movieSearch;

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

        DataSyncTask dataSyncTask = new DataSyncTask();
        dataSyncTask.execute();

    }

    public class DataSyncTask extends AsyncTask<String, Void, String>{

        private static final String API_KEY = "k_iklh8ner";

        @Override
        protected String doInBackground(String... strings) {

            Log.d("OUTPUT STATS", movieSearch);
            String baseURL = "https://imdb-api.com/en/API/SearchTitle/" + API_KEY + "/" + movieSearch;
            Log.d("OUTPUT STATS", baseURL);
            Uri builtURI = Uri.parse(baseURL);

            try {
                URL requestURL = new URL(builtURI.toString());
                HttpsURLConnection conn = (HttpsURLConnection) requestURL.openConnection();

                conn.connect();

                int response = conn.getResponseCode();
                InputStream inputStream = conn.getInputStream();
                String contentAsString = convertIsToString(inputStream);

                if(response == HttpsURLConnection.HTTP_OK)
                    return contentAsString;
                else
                    return null;

            } catch (MalformedURLException e) {
                System.out.println("URL Is Not Found (Malformed URL)" + e.getMessage());
            } catch (IOException e) {
                System.out.println("Error getting response " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if( s != null){

                Log.d("OUTPUT STATS", s);

                try {
                    JSONObject jsonObjectReturned = new JSONObject(s);
                    JSONArray resultData = jsonObjectReturned.getJSONArray("results");

                    for(int object = 0; object < resultData.length(); object++){

                        JSONObject movieObject = resultData.getJSONObject(object);
                        String movieTitle = movieObject.getString("title");
                        String movieImageURL = movieObject.getString("image");

                        Bitmap bitmap = getImageBitmap(movieImageURL);

                        MovieRating movie = new MovieRating(movieTitle, "10.0",bitmap );
                        movieRatings.add(movie);
                    }

                    MovieRatingAdapter movieRatingAdapter = new MovieRatingAdapter(movieRatings, RatingResultActivity.this);
                    recyclerView.setAdapter(movieRatingAdapter);


                } catch (JSONException e) {
                    Log.d("ERROR","Error in converting to json object " + e.getMessage());
                }

            }else{
                Toast.makeText(getApplicationContext(), "Movie Name Not Recognised.", Toast.LENGTH_SHORT).show();
                Log.d("OUTPUT STATS", "Badly Formatted URL");
            }

        }

        private Bitmap getImageBitmap(String movieImageURL) {

            final Bitmap[] bitmap = {null};

            Thread imageThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream imageInputStream = new URL(movieImageURL).openStream();
                        bitmap[0] = BitmapFactory.decodeStream(imageInputStream);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            imageThread.start();

            return bitmap[0];
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



}


