package com.example.album4pro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

import java.util.Locale;


public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Theme Before SetContentView, Default Is Light Theme
        sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);

        // Set Theme Dark Light Mode
        if (sharedPreferences.getBoolean("darkmode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
            if (sharedPreferences.getBoolean("smoke", false)) setTheme(R.style.SmokeTheme);
            if (sharedPreferences.getBoolean("blue", true)) setTheme(R.style.Theme_Album4Pro);
            if (sharedPreferences.getBoolean("brown", false)) setTheme(R.style.BrownTheme);
            if (sharedPreferences.getBoolean("purple", false)) setTheme(R.style.PurpleTheme);
            if (sharedPreferences.getBoolean("yellow", false)) setTheme(R.style.YellowTheme);
            if (sharedPreferences.getBoolean("green", false)) setTheme(R.style.GreenTheme);
            if (sharedPreferences.getBoolean("orange", false)) setTheme(R.style.OrangeTheme);
            if (sharedPreferences.getBoolean("navy", false)) setTheme(R.style.NavyTheme);
            if (sharedPreferences.getBoolean("pink", false)) setTheme(R.style.PinkTheme);
        }

        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.this.finish();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        //---------------------------------------- SET LANGUAGE ------------------------------------------------
        // Set Language
        if (sharedPreferences.getBoolean("vietnamese", false)) {
            setLocale("vi");
        } else {
            setLocale("en");
        }

    }

    private void setLocale(String language) {
        // Initalize resources
        Resources resources = getResources();
        // Initialize metrics
        DisplayMetrics metrics = resources.getDisplayMetrics();
        // Initialize configuration
        Configuration configuration = resources.getConfiguration();
        // Initialize locale
        configuration.setLocale(new Locale(language));
        // Update configuration
        resources.updateConfiguration(configuration, metrics);
        // Notify configuration
        onConfigurationChanged(configuration);
    }
}