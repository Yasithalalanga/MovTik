package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void registerMovieActivity(View view) {
        Intent registerIntent = new Intent(MainActivity.this, RegisterMovieActivity.class);
        startActivity(registerIntent);
    }

    public void displayMoviesActivity(View view) {
        Intent displayIntent = new Intent(MainActivity.this, DisplayMovieActivity.class);
        startActivity(displayIntent);
    }

    public void favouritesActivity(View view) {
        Intent favouritesIntent = new Intent(MainActivity.this, FavouritesActivity.class);
        startActivity(favouritesIntent);
    }

    public void editMoviesActivity(View view) {
        Intent editMoviesIntent = new Intent(MainActivity.this, EditMovieActivity.class);
        startActivity(editMoviesIntent);
    }

    public void searchActivity(View view) {
        Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(searchIntent);
    }

    public void ratingsActivity(View view) {
        Intent ratingsIntent = new Intent(MainActivity.this, RatingActivity.class);
        startActivity(ratingsIntent);
    }
}