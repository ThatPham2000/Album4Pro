package com.example.album4pro.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.album4pro.MainActivity;
import com.example.album4pro.R;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    RadioGroup rgLanguage;
    RadioButton rbEnglish, rbVietnamese;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Theme Before SetContentView, Default Is Light Theme
        sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);

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

        setContentView(R.layout.activity_language);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rgLanguage = findViewById(R.id.rgLanguage);
        rbEnglish = findViewById(R.id.rbEnglish);
        rbVietnamese = findViewById(R.id.rbVietnamese);

        // Set Check For Radio Button, Default is English
        if (sharedPreferences.getBoolean("vietnamese", false)) {
            rbVietnamese.setChecked(true);
            setLocale("vi");
        } else {
            rbEnglish.setChecked(true);
            setLocale("en");
        }

        rgLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbEnglish:
                        setLocale("en");
                        break;
                    case R.id.rbVietnamese:
                        setLocale("vi");
                        break;
                }
            }
        });
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
        super.onConfigurationChanged(configuration);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

        // Save Language Mode
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("vietnamese", rbVietnamese.isChecked());

        editor.apply();
    }
}