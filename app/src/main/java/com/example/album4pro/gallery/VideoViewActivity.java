package com.example.album4pro.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.album4pro.BuildConfig;
import com.example.album4pro.DeleteDatabase;
import com.example.album4pro.R;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;

public class VideoViewActivity extends AppCompatActivity {
    private VideoView videoView;

    private ImageButton btnShare;
    private ImageButton btnHide;
    private ImageButton btnDelete;

    private String videoPath = "";
    private DeleteDatabase deleteDatabase;

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
        setContentView(R.layout.activity_video_view);

        deleteDatabase = new DeleteDatabase(this, "delete.sqlite", null, 1);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        videoView = (VideoView) findViewById(R.id.video_detail);
        btnShare = (ImageButton) findViewById(R.id.btn_share_video);
        btnHide = (ImageButton) findViewById(R.id.btn_hide_video);
        btnDelete = (ImageButton) findViewById(R.id.btn_delete_video);

        videoPath = getIntent().getStringExtra("path");
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(VideoViewActivity.this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.start();


        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailPhoto.pathPrivate = videoPath;
                DetailPhoto.pressPrivate = true;
                DetailPhoto.pressinsidePrivate = true;
                DetailPhoto.tempcheckToast = true;
                onBackPressed();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("video/*");

                share.putExtra(Intent.EXTRA_SUBJECT, "abc");
                share.putExtra(Intent.EXTRA_TITLE, "abcd");

                File videoFile = new File(videoPath);

                Uri uri = FileProvider.getUriForFile(VideoViewActivity.this, BuildConfig.APPLICATION_ID + ".provider",videoFile);

                share.putExtra(Intent.EXTRA_STREAM, uri);

                startActivity(Intent.createChooser(share, "Share"));
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertAndRemoveDelete(videoPath);
                onBackPressed();
                Toast.makeText(VideoViewActivity.this, R.string.delete_image, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<String> listPhotoDelete(){

        ArrayList<String> arrListDelete = new ArrayList<>();
        // select data
        Cursor dataCursor = deleteDatabase.GetData("SELECT * FROM DeleteData");
        while (dataCursor.moveToNext()){
            String path_p = dataCursor.getString(1); // i là cột
            //int id = dataCursor.getInt(0);

            arrListDelete.add(path_p);
        }
        return arrListDelete;
    }

    private void insertAndRemoveDelete(String pathImage){

        Boolean check = false;
        Cursor checkCursor = deleteDatabase.GetData("SELECT * FROM DeleteData");
        while (checkCursor.moveToNext()){
            String path_p = checkCursor.getString(1); // i là cột
            if(pathImage.equals(path_p)){
                check = true;
                break;
            }
        }
        // Chưa tồn tại trong delete
        if(check == false && !pathImage.equals("")){
            // Thêm vào Private
            deleteDatabase.QueryData("INSERT INTO DeleteData VALUES(null, '"+pathImage+"')");

        } else {
            // Đã tồn tại trong private --> đưa ra ngoài Library
            deleteDatabase.QueryData("DELETE FROM DeleteData WHERE Path = '"+ pathImage +"'");
        }
    }
}