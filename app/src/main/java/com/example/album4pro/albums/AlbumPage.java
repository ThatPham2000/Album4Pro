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
import android.os.Environment;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;


public class AlbumPage extends AppCompatActivity implements View.OnClickListener{
/*    private static final int SELECT_PICTURES = 10;*/
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

    private List<String> imagePathList;
    private String folderName;

    public class RunnableSimple implements Runnable{
        @Override
        public void run() {
            if (Thread.currentThread().getName().equals("Thread-0")){
                selectImagesFromGallery();
            }
        }
    }

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

        btnScrollUp = findViewById(R.id.btnScrollUp2);
        btnScrollDown = findViewById(R.id.btnScrollDown2);
        Handler handler =  new android.os.Handler();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Appear Scroll Up Button If View As Top To Bottom
                if (sharedPreferences.getInt("view", 0) == 0) {
                    if (dy != 0) {
                        btnScrollUp.show();

                        // Hide Button After 2 Seconds
                        Runnable myRunnable = new Runnable() {
                            public void run() {
                                btnScrollUp.hide();
                            }
                        };

                        // Remove All messages and callbacks in handler
                        handler.removeCallbacksAndMessages(null);

                        handler.postDelayed(myRunnable, 2000);


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

                        // Remove All messages and callbacks in handler
                        handler.removeCallbacksAndMessages(null);

                        handler.postDelayed(myRunnable, 2000);
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

                if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
                    return true;
                } else {
                    String folderNameTest =  "Screenshots";
                    listImageOnAlbum = ImagesGallery.listImageOnAlbum(this, folderNameTest);
                    String sourceImage = ImagesGallery.firstImageOnAlbum(listImageOnAlbum);
                    File f = new File(sourceImage);


                    String fileName = "newfile";
                    File myExternalFile = new File(getExternalFilesDir(folderName), fileName);
                    if (!myExternalFile.exists()){
                        try {
                            f.createNewFile();
                            copyFile(f, myExternalFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                /*selectImagesFromGallery();*/
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void loadImages(AlbumItem album){
        folderName = album.getName();
        setTitle(folderName);

        listImageOnAlbum = ImagesGallery.listImageOnAlbum(this,album.getName());

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

        // Scroll To Begin if View As Top To Bottom
        if (sharedPreferences.getInt("view", 0) == 0) {
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
        activityResultLauncher.launch(intent);
    }
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        imagePathList = new ArrayList<>();
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for (int i=0; i<count; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                String selectedImagePath = getImageFilePath(imageUri);
                                imagePathList.add(selectedImagePath);
                                /*getImageFilePath(imageUri);*/
                            }
                        }
                        else if (data.getData() != null) {
                            Uri imgUri = data.getData();
                            String selectedImagePath = getImageFilePath(imgUri);
                            imagePathList.add(selectedImagePath);
                            /*getImageFilePath(imgUri);*/
                        }
                    }
                }
            });
    private String getImageFilePath(Uri uri) {

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];
        String imagePath = null;

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Images.Media._ID + " = ? ",
                new String[]{image_id},
                null);
        if (cursor!=null) {
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

        }
        return imagePath;
    }
    /*
     * Kiểm tra xe bộ nhớ ngoài SDCard có readonly không vì nếu là readonly thì
     * không thể tạo file trên đó được
     */

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    /*
     * Kiểmtra xem device có bộ nhớ ngoài không
     */
    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
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
    public static void MoveFile(String path_source, String path_destination) throws IOException {
        File file_Source = new File(path_source);
        File file_Destination = new File(path_destination);

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(file_Source).getChannel();
            destination = new FileOutputStream(file_Destination).getChannel();

            long count = 0;
            long size = source.size();
            while((count += destination.transferFrom(source, count, size-count))<size);
            file_Source.delete();
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }
    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }
}