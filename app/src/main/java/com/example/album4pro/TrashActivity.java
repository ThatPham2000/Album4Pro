package com.example.album4pro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.album4pro.gallery.Configuration;
import com.example.album4pro.gallery.DetailPhoto;
import com.example.album4pro.gallery.DetailPhotoDelete;
import com.example.album4pro.gallery.GalleryAdapter;
import com.example.album4pro.gallery.VideoDetailDeleteActivity;
import com.example.album4pro.gallery.VideoViewActivity;

import java.util.ArrayList;
import java.util.List;

public class TrashActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;
    private List<String> listPhoto;

    private DeleteDatabase deleteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        deleteDatabase = new DeleteDatabase(this, "delete.sqlite", null, 1);

        listPhoto = listPhotoDelete();
//        listPhoto = Configuration.getInstance().getListPhotoDelete();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_trash);

        loadImage();
    }

    private void loadImage() {
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(TrashActivity.this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
//        listPhoto = listPhotoPrivate(context);

        galleryAdapter = new GalleryAdapter(TrashActivity.this, listPhoto, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                // TODO ST
                String[] imageExtensions = {"jpg", "png", "gif", "jpeg", "tiff", "webp"};
                boolean isImage = false;
                String extension = path.substring(path.lastIndexOf(".") + 1);

                for (int i = 0; i < imageExtensions.length; i++){
                    if(extension.equalsIgnoreCase(imageExtensions[i])){
                        isImage = true;
                        break;
                    }
                }

                if (!isImage){
                    Intent intent = new Intent(TrashActivity.this, VideoDetailDeleteActivity.class);
                    intent.putExtra("path", path);
                    TrashActivity.this.startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(TrashActivity.this, DetailPhotoDelete.class);
                    intent.putExtra("path", path);
                    TrashActivity.this.startActivity(intent);
                    finish();
                }
            }
        });

        recyclerView.setAdapter(galleryAdapter);
        Configuration.getInstance().setGalleryAdapter(this.galleryAdapter);
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