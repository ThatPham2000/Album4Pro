package com.example.album4pro.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.VideoView;

import com.example.album4pro.BuildConfig;
import com.example.album4pro.R;

import java.io.File;
import java.io.OutputStream;

public class VideoViewActivity extends AppCompatActivity {
    private VideoView videoView;

    private ImageButton btnShare;
    private ImageButton btnHide;
    private ImageButton btnDelete;

    String videoPath = "";

    public static String pathPrivate = "";
    public static Boolean pressPrivate = false;
    public static Boolean pressinsidePrivate = false;
    public static Boolean tempcheckToast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

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


//        btnHide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pathPrivate = videoPath;
//                pressPrivate = true;
//                pressinsidePrivate = true;
//                tempcheckToast = true;
//                onBackPressed();
//            }
//        });

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

            }
        });
    }
}