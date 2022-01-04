package com.example.album4pro.albums;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.album4pro.ImagesGallery;
import com.example.album4pro.R;
import com.example.album4pro.gallery.DetailPhoto;
import com.example.album4pro.gallery.GalleryAdapter;
import com.example.album4pro.setting.AboutUsActivity;
import com.example.album4pro.setting.HelpActivity;
import com.example.album4pro.setting.LanguageActivity;
import com.example.album4pro.setting.PolicyActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AlbumPage extends AppCompatActivity implements View.OnClickListener{
    private static final int SELECT_PICTURES = 10;
    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;
    private NewAlbumAdapter newAlbumAdapter;
    private FloatingActionButton btnScrollUp;
    private FloatingActionButton btnScrollDown;
    GridLayoutManager gridLayoutManager;
    SharedPreferences sharedPreferences;

    private List<String> listImageOnAlbum;
    private List<String> listFolderName;
    private String dialogAlbumName;

    private String imagePath;
    private List<String> imagePathList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_page);
        recyclerView = findViewById(R.id.rcv_page_album);

        listFolderName = ImagesGallery.listFolderName(this);


        if (getIntent().getExtras() != null){
            AlbumItem album = (AlbumItem) getIntent().getExtras().get("folder name");
            loadImages(album.getName());
        }

        btnScrollUp = findViewById(R.id.btnScrollUp2);
        btnScrollDown = findViewById(R.id.btnScrollDown2);
        Handler handler =  new android.os.Handler();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Appear Scroll Up Button If View As Top To Bottom
                if (sharedPreferences.getInt("view", 1) == 0) {
                    if (dy != 0) {
                        btnScrollUp.show();

                        // Hide Button After 2 Seconds
                        Runnable myRunnable = new Runnable() {
                            public void run() {
                                btnScrollUp.hide();
                            }
                        };

                        handler.postDelayed(myRunnable, 2000);


                    } else {
                        btnScrollUp.hide();
                    }
                } // Appear Scroll Up Button If View As Bottom To Top
                else {
                    if (dy != 0) {
                        btnScrollDown.show();

                        // Hide Button After 2 Seconds
                        Runnable myRunnable = new Runnable() {
                            public void run() {
                                btnScrollDown.hide();
                            }
                        };

                        handler.postDelayed(myRunnable, 2000);
                    } else {
                        btnScrollDown.hide();
                    }
                }
            }
        });

        btnScrollUp.setOnClickListener(this);
        btnScrollDown.setOnClickListener(this);

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

    private void loadImages(String folderName){
        setTitle(folderName);

        if (folderName.equals("Tạo album mới")){

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_create_new_album);

            Window window = dialog.getWindow();
            if (window == null){
                return;
            }
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCancelable(false);

            EditText edtAlbumName = dialog.findViewById(R.id.edt_album_name);
            Button btnExit = dialog.findViewById(R.id.btn_exit);
            Button btnCreate = dialog.findViewById(R.id.btn_create);

            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*dialog.dismiss();*/
                    finish();
                }
            });
            btnCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialogAlbumName = edtAlbumName.getText().toString().trim();

                    newAlbumAdapter = new NewAlbumAdapter(AlbumPage.this);

                    gridLayoutManager = new GridLayoutManager(AlbumPage.this, 4);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(newAlbumAdapter);

                    selectImagesFromGallery();

                    dialog.dismiss();

                }
            });
            dialog.show();

        }
        else {
            listImageOnAlbum = ImagesGallery.listImageOnAlbum(this,folderName);

            sharedPreferences = getSharedPreferences("save", Context.MODE_PRIVATE);
            gridLayoutManager = new GridLayoutManager(this, sharedPreferences.getInt("column", 3));
            recyclerView.setLayoutManager(gridLayoutManager);

            galleryAdapter = new GalleryAdapter(this, listImageOnAlbum, new GalleryAdapter.PhotoListener() {
                @Override
                public void onPhotoClick(String path) {
                    onClickGoToDetailPhoto(path);
                }
            });
            recyclerView.setAdapter(galleryAdapter);
        }

        // Scroll To Begin if View As Top To Bottom
        if (sharedPreferences.getInt("view", 1) == 0) {
            scrollToItem(0);
        } // Scroll To End if View As Bottom To Top
        else {
            scrollToItem(listImageOnAlbum.size() - 1);
        }
    }
    private void onClickGoToDetailPhoto(String path){
        Intent intent = new Intent(this, DetailPhoto.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }



    private void selectImagesFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURES);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == SELECT_PICTURES){
            if (resultCode == Activity.RESULT_OK){
                if (data.getClipData() != null){
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++){
                        Uri uri = data.getClipData().getItemAt(i).getUri();
                    }
                } else if (data.getData() != null){
                    String imagePath = data.getData().getPath();
                }
            }
        }*/
        if (requestCode == SELECT_PICTURES && resultCode == RESULT_OK  && data != null) {
            imagePathList = new ArrayList<>();
            if (data.getClipData() != null) {
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

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor!=null) {
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            imagePathList.add(imagePath);
            cursor.close();
        }
    }
    /*private void sendAlbum(AlbumItem album){
     *//*AlbumsFragment albumsFragment = new AlbumsFragment();*//*

        Intent returnIntent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putSerializable("album folder", album);
        returnIntent.putExtras(bundle);

        returnIntent.putExtra("album folder", album);
        setResult(Activity.RESULT_OK, returnIntent);
    }*/
    private void sendNewAlbum(){
        AlbumItem album = new AlbumItem(R.drawable.icon_album,dialogAlbumName,"10");
        /*AlbumsFragment albumsFragment = new AlbumsFragment();*/

        Intent returnIntent = new Intent();

        Bundle bundle = new Bundle();
        bundle.putSerializable("new album", album);
        returnIntent.putExtras(bundle);

        returnIntent.putExtra("new album", album);
        setResult(Activity.RESULT_OK, returnIntent);


        /*albumsFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.rcv_album, albumsFragment);
        fragmentTransaction.commit();*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScrollUp2:
                scrollToItem(0);
                break;
            case R.id.btnScrollDown2:
                scrollToItem(listImageOnAlbum.size() - 1);
                break;
        }
    }

    private void scrollToItem(int index) {
        if (gridLayoutManager == null) return;

        gridLayoutManager.scrollToPositionWithOffset(index, 0);
    }
}