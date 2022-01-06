package com.example.album4pro.albums;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.album4pro.ImagesGallery;
import com.example.album4pro.R;
import com.example.album4pro.gallery.DetailPhoto;
import com.example.album4pro.gallery.GalleryAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AlbumPage extends AppCompatActivity {
    private static final int SELECT_PICTURES = 10;
    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;
    private NewAlbumAdapter newAlbumAdapter;

    private List<String> listImageOnAlbum;
    private List<String> listFolderName;
    private String dialogAlbumName;

    private List<String> imagePathList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_page);
        recyclerView = findViewById(R.id.rcv_page_album);

        listFolderName = ImagesGallery.listFolderName(this);

        if (getIntent().getExtras() != null){
            AlbumItem album = (AlbumItem) getIntent().getExtras().get("folder name");
            loadImages(album);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_album, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_album:
                // User chose the "Settings" item, show the app settings UI...
                selectImagesFromGallery();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void loadImages(AlbumItem album){
        setTitle(album.getName());

        if (album.getNumber().equals("0")){
            selectImagesFromGallery();

            GridLayoutManager gridLayoutManager = new GridLayoutManager(AlbumPage.this, 4);
            recyclerView.setLayoutManager(gridLayoutManager);

            if (imagePathList != null){

                galleryAdapter = new GalleryAdapter(this, imagePathList, new GalleryAdapter.PhotoListener() {
                    @Override
                    public void onPhotoClick(String path) {
                        onClickGoToDetailPhoto(path);
                    }
                });
                recyclerView.setAdapter(galleryAdapter);
            }
        }
        else{
            listImageOnAlbum = ImagesGallery.listImageOnAlbum(this,album.getName());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
            recyclerView.setLayoutManager(gridLayoutManager);
            galleryAdapter = new GalleryAdapter(this, listImageOnAlbum, new GalleryAdapter.PhotoListener() {
                @Override
                public void onPhotoClick(String path) {
                    onClickGoToDetailPhoto(path);
                }
            });
            recyclerView.setAdapter(galleryAdapter);
        }
    }
    private void onClickGoToDetailPhoto(String path){
        Intent intent = new Intent(this, DetailPhoto.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }
    //---------------------------------------------------------------------------------------------
    private void selectImagesFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURES);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURES && resultCode == RESULT_OK  && data != null) {
            if (data.getClipData() != null) {
                imagePathList = new ArrayList<>();
                int count = data.getClipData().getItemCount();
                for (int i=0; i<count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    getImageFilePath(imageUri);
                }
            }
            else if (data.getData() != null) {
                Uri imgUri = data.getData();
                getImageFilePath(imgUri);
            }
        }
    }
    public void getImageFilePath(Uri uri) {

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media._ID + " = ? ",
                new String[]{image_id},
                null);
        if (cursor!=null) {
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            imagePathList.add(imagePath);
            cursor.close();
        }
    }
    //---------------------------------------------------------------------------------------------
    /*private void sendAlbum(AlbumItem album){
     *//*AlbumsFragment albumsFragment = new AlbumsFragment();*//*

        Intent returnIntent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putSerializable("album folder", album);
        returnIntent.putExtras(bundle);

        returnIntent.putExtra("album folder", album);
        setResult(Activity.RESULT_OK, returnIntent);
    }*/
    /*private void sendNewAlbum(){
        AlbumItem album = new AlbumItem(R.drawable.icon_album,dialogAlbumName,"10");
        *//*AlbumsFragment albumsFragment = new AlbumsFragment();*//*

        Intent returnIntent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putSerializable("new album", album);
        returnIntent.putExtras(bundle);

        returnIntent.putExtra("new album", album);
        setResult(Activity.RESULT_OK, returnIntent);


        *//*albumsFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.rcv_album, albumsFragment);
        fragmentTransaction.commit();*//*
    }*/
}