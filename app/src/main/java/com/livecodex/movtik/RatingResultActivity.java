package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RatingResultActivity extends AppCompatActivity {

    TextView testResult;
    private String movieSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_result);

        testResult = findViewById(R.id.testResult);

        Intent ratingSelectedIntent = getIntent();
        movieSearch = ratingSelectedIntent.getStringExtra("MovieRequested");

        testResult.setText(movieSearch);

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

            }else{
                Toast.makeText(getApplicationContext(), "Movie Name Not Recognised.", Toast.LENGTH_SHORT).show();
                Log.d("OUTPUT STATS", "Badly Formatted URL");
            }

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


