package com.livecodex.movtik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    /*
    * Splash Screen
    * Shows Before the App Starts
    */

    private static final int SPLASH_SCREEN_TIME = 4500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        ImageView logo = findViewById(R.id.logo);
        ImageView mLogo = findViewById(R.id.mLogo);
        TextView slogan = findViewById(R.id.slogan);

        Animation top_animation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        Animation bottom_animation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        mLogo.setAnimation(top_animation);
        logo.setAnimation(bottom_animation);
        slogan.setAnimation(bottom_animation);

        /*
        * Send the User to the Dashboard
        * When Splash Screen Times out
        */

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_SCREEN_TIME);
    }
}