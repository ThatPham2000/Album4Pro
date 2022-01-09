package com.example.album4pro.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.example.album4pro.R;
import com.example.album4pro.TrashActivity;
import com.github.chrisbanes.photoview.PhotoView;

public class DetailPhotoDelete extends AppCompatActivity {
    private PhotoView img;
    private ImageButton btnDeleteBack;

    private String pathImage = "";
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Theme Before SetContentView, Default Is Light Theme
        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);

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

        setContentView(R.layout.activity_detail_photo_delete);

        mContext = getApplicationContext();

        img = (PhotoView) findViewById(R.id.img_detail_delete);
        pathImage = getIntent().getStringExtra("path");
        Glide.with(this).load(pathImage).into(img);

        btnDeleteBack = (ImageButton) findViewById(R.id.btn_delete_back);
        btnDeleteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailPhoto.pathDelete = pathImage;
                DetailPhoto.pressDelete = true;
                DetailPhoto.pressinsideDelete = true;
                DetailPhoto.tempcheckToastDelete = true;
//                onBackPressed();
                Intent i = new Intent(DetailPhotoDelete.this, TrashActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}