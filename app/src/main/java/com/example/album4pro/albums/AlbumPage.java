package com.example.album4pro.albums;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.album4pro.ImagesGallery;
import com.example.album4pro.R;
import com.example.album4pro.gallery.Configuration;
import com.example.album4pro.gallery.DetailPhoto;
import com.example.album4pro.gallery.GalleryAdapter;

import java.util.ArrayList;
import java.util.List;

public class AlbumPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;
    private List<String> listPhoto;
    private List<String> listImageOnAlbum;
    private List<String> listFolderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_page);
        recyclerView = findViewById(R.id.rcv_page_album);

        listFolderName = ImagesGallery.listFolderName(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        AlbumItem album = (AlbumItem) bundle.get("folder name");
        loadImages(album.getName());
    }
    private void loadImages(String folderName){
/*        recyclerView.setHasFixedSize(true);*/
        listImageOnAlbum = ImagesGallery.listImageOnAlbum(this,folderName);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recyclerView.setLayoutManager(gridLayoutManager);

        galleryAdapter = new GalleryAdapter(this, listImageOnAlbum, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                onClickGoToDetailPhoto(path);
            }
        });


        /*galleryAdapter.setListPhoto(listPhoto);*/
        recyclerView.setAdapter(galleryAdapter);
    }
    private void onClickGoToDetailPhoto(String path){
        Intent intent = new Intent(this, DetailPhoto.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }
}