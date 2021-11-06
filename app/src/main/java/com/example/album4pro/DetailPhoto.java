package com.example.album4pro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;

import java.io.File;

public class DetailPhoto extends AppCompatActivity {

    private ImageView img;
    private ImageButton btnShare;
    private ImageButton btnEdit;
    private ImageButton btnDelete;
    private ImageButton btnMore;

    private String pathImage = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_photo);

        img = findViewById(R.id.img_detail);
        pathImage = getIntent().getStringExtra("path");
        Glide.with(this).load(pathImage).into(img);

        btnShare = findViewById(R.id.btn_share);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);
        btnMore = findViewById(R.id.btn_more);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri filePath = Uri.fromFile(new File(pathImage));
                Log.e("URI nha", filePath.toString());
                Intent dsPhotoEditorIntent = new Intent(DetailPhoto.this, DsPhotoEditorActivity.class);
                dsPhotoEditorIntent.setData(filePath);

                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "PhotoEditorNha");

                int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};

                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

                startActivityForResult(dsPhotoEditorIntent, 200);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}