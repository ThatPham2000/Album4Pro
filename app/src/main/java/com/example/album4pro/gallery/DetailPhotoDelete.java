package com.example.album4pro.gallery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.album4pro.DeleteDatabase;
import com.example.album4pro.R;
import com.example.album4pro.TrashActivity;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DetailPhotoDelete extends AppCompatActivity {
    private PhotoView img;
    private ImageButton btnDeleteBack, btnDeleteHard;

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
                insertAndRemoveDelete(pathImage);

                Intent i = new Intent(DetailPhotoDelete.this, TrashActivity.class);
                startActivity(i);
                Toast.makeText(DetailPhotoDelete.this, R.string.recovery_image, Toast.LENGTH_SHORT).show();
                DetailPhotoDelete.this.overridePendingTransition(R.anim.default_status, R.anim.slide_out_right);
                finish();
            }
        });

        btnDeleteHard = (ImageButton) findViewById(R.id.btn_delete_hard);
        btnDeleteHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo AlertDialog từ đối tượng Builder. Tham số là context.
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailPhotoDelete.this);

                // Set Message để thiết lập câu thông báo
                builder.setMessage(R.string.delete_question)
                        // positiveButton
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                // file uri: /storage/emulated/0/...
                                // content uri: content://media/external/.../111
                                //=>  convert file uri to content uri

                                Intent i = new Intent(DetailPhotoDelete.this, TrashActivity.class);

                                File file = new File(pathImage);
                                if (file.exists()){
                                    file.delete();
                                }

                                removeHard(pathImage);
                                Toast.makeText(DetailPhotoDelete.this, R.string.recovery_image, Toast.LENGTH_SHORT).show();

                                startActivity(i);
                                DetailPhotoDelete.this.overridePendingTransition(R.anim.default_status, R.anim.slide_out_right);
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

    private Uri getContentUriId(Uri imageUri) {
        String[] projections = {MediaStore.MediaColumns._ID};
        Cursor cursor = DetailPhotoDelete.this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projections,
                MediaStore.MediaColumns.DATA + "=?",
                new String[]{imageUri.getPath()}, null);
        long id = 0;
        if (cursor != null){
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                id  = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            }
        }
        cursor.close();
        return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf((int)id));
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

    public int deleteFileFromMediaStore(final ContentResolver contentResolver, final File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        final Uri uri = MediaStore.Files.getContentUri("external");
        final int result = contentResolver.delete(uri,
                MediaStore.Files.FileColumns.DATA + "=?", new String[]{canonicalPath});
        if (result == 0) {
            final String absolutePath = file.getAbsolutePath();
            if (!absolutePath.equals(canonicalPath)) {
                int deletedRow = contentResolver.delete(uri,
                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
                return deletedRow;
            }
        } else return result;
        return result;
    }
}