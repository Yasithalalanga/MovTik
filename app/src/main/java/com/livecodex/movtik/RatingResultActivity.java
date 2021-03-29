package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RatingResultActivity extends AppCompatActivity {

    TextView testResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_result);

        testResult = findViewById(R.id.testResult);

        Intent ratingSelectedIntent = getIntent();
        String movieSearch = ratingSelectedIntent.getStringExtra("MovieRequested");

        testResult.setText(movieSearch);

    }



}