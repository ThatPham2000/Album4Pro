package com.example.album4pro.gallery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.album4pro.DeleteDatabase;
import com.example.album4pro.R;
import com.example.album4pro.TrashActivity;

import java.io.File;
import java.util.ArrayList;

public class VideoDetailDeleteActivity extends AppCompatActivity {
    private VideoView videoView;

    private ImageButton btnDeleteBack;

    private String videoPath = "";
    private DeleteDatabase deleteDatabase;
    private ImageButton btnDeleteHard;
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
        setContentView(R.layout.activity_video_detail_delete);

        deleteDatabase = new DeleteDatabase(this, "delete.sqlite", null, 1);

        videoView = (VideoView) findViewById(R.id.video_detail);
        btnDeleteBack = (ImageButton) findViewById(R.id.btn_delete_video_delete);
        btnDeleteHard = (ImageButton)findViewById(R.id.btn_delete_videos_hard);

        videoPath = getIntent().getStringExtra("path");
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(VideoDetailDeleteActivity.this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.start();

        btnDeleteBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertAndRemoveDelete(videoPath);

                Intent i = new Intent(VideoDetailDeleteActivity.this, TrashActivity.class);
                startActivity(i);
                Toast.makeText(VideoDetailDeleteActivity.this, R.string.recovery_image, Toast.LENGTH_SHORT).show();
                VideoDetailDeleteActivity.this.overridePendingTransition(R.anim.default_status, R.anim.slide_out_right);
                finish();
            }
        });

        btnDeleteHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo AlertDialog từ đối tượng Builder. Tham số là context.
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoDetailDeleteActivity.this);

                // Set Message để thiết lập câu thông báo
                builder.setMessage(R.string.delete_question)
                        // positiveButton
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // file uri: /storage/emulated/0/...
                                // content uri: content://media/external/.../111
                                //=>  convert file uri to content uri

                                Intent i = new Intent(VideoDetailDeleteActivity.this, TrashActivity.class);

                                File file = new File(videoPath);
                                if (file.exists()){
                                    file.delete();
                                }

                                removeHard(videoPath);
                                Toast.makeText(VideoDetailDeleteActivity.this, R.string.recovery_image, Toast.LENGTH_SHORT).show();

                                startActivity(i);
                                VideoDetailDeleteActivity.this.overridePendingTransition(R.anim.default_status, R.anim.slide_out_right);
                                finish();
                            }
                        })
                        // NegativeButton
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                // Tạo dialog và hiển thị
                builder.create().show();
            }
        });
    }

    private void removeHard(String pathImage){

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
        if(check){
            deleteDatabase.QueryData("DELETE FROM DeleteData WHERE Path = '"+ pathImage +"'");
        }
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