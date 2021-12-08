package com.example.album4pro.albums;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.renderscript.ScriptGroup;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.album4pro.ImagesGallery;
import com.example.album4pro.MainActivity;
import com.example.album4pro.R;
import com.example.album4pro.fragments.AlbumsFragment;
import com.example.album4pro.gallery.Configuration;
import com.example.album4pro.gallery.DetailPhoto;
import com.example.album4pro.gallery.GalleryAdapter;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class AlbumPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;
    private NewAlbumAdapter newAlbumAdapter;

    private List<String> listImageOnAlbum;
    private List<String> listFolderName;
    private String dialogAlbumName;
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
                Toast.makeText(this, "Show Camera", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void loadImages(String folderName){
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

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(AlbumPage.this, 4);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(newAlbumAdapter);

                    selectImagesFromGallery();
                    sendNewAlbum();
                    dialog.dismiss();
                }
            });
            dialog.show();

        }
        else {
            listImageOnAlbum = ImagesGallery.listImageOnAlbum(this,folderName);

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

    private void selectImagesFromGallery(){
        TedBottomPicker.with(this)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Thêm")
                .setEmptySelectionText("Trống")
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        // here is selected image uri list
                        if (uriList != null && !uriList.isEmpty()){
                            newAlbumAdapter.setNewAlbum(uriList);
                        }
                    }
                });
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
}