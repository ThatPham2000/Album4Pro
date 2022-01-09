package com.example.album4pro.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.example.album4pro.DeleteDatabase;
import com.example.album4pro.R;
import com.example.album4pro.TrashActivity;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class DetailPhotoDelete extends AppCompatActivity {
    private PhotoView img;
    private ImageButton btnDeleteBack;

    private String pathImage = "";
    private Context mContext;
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

        setContentView(R.layout.activity_detail_photo_delete);

        mContext = getApplicationContext();

        deleteDatabase = new DeleteDatabase(this, "delete.sqlite", null, 1);

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
                insertAndRemoveDelete();
                DetailPhoto.pathDelete = "";

                Configuration.getInstance().setListPhotoDelete(listPhotoDelete());
                Intent i = new Intent(DetailPhotoDelete.this, TrashActivity.class);
                startActivity(i);
                finish();
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

    private void insertAndRemoveDelete(){
        String pathImage = DetailPhoto.pathDelete;

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