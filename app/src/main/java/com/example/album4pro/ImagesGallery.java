package com.example.album4pro;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ImagesGallery {
    public static ArrayList<String> listPhotoAndVideo(Context context){
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllPhotoAndVideo = new ArrayList<>();
        String pathOfPhotoAndVideo;

        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };
        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        // get folder name
        //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()){
            pathOfPhotoAndVideo = cursor.getString(column_index_data);
            listOfAllPhotoAndVideo.add(pathOfPhotoAndVideo);
        }

        return listOfAllPhotoAndVideo;
    };

    public static ArrayList<String> listPhoto(Context context){
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImage = new ArrayList<>();
        String pathOfImage;

        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };
        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        // get folder name
        //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()){
            pathOfImage = cursor.getString(column_index_data);
            listOfAllImage.add(pathOfImage);
        }

        return listOfAllImage;
    };

    public static ArrayList<String> listVideo(Context context){
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllVideo = new ArrayList<>();
        String pathOfVideo;

        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME
        };
        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        // get folder name
        //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()){
            pathOfVideo = cursor.getString(column_index_data);
            listOfAllVideo.add(pathOfVideo);
        }

        return listOfAllVideo;
    };

    public static ArrayList<String> listFolderName(Context context){
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfFolderImageName = new ArrayList<>();
        String pathOfImageName;

        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };
        String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
       /* column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);*/

        // get folder name
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()){
            pathOfImageName = cursor.getString(column_index_folder_name);
            listOfFolderImageName.add(pathOfImageName);
        }

        //filter name folder
        ArrayList<String> tempName = new ArrayList<>();
        for (int index = 0; index < listOfFolderImageName.size(); index++){
            if (!tempName.contains(listOfFolderImageName.get(index))){
                tempName.add(listOfFolderImageName.get(index));
            }
        }
        listOfFolderImageName.clear();
        listOfFolderImageName.addAll(tempName);

        return listOfFolderImageName;
    }
    public static ArrayList<String> listImageOnAlbum(Context context, String folderName){
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listImageOnAlbum = new ArrayList<>();
        String pathOfImage, pathOfName;

        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };
        String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()){
            pathOfImage = cursor.getString(column_index_data);
            pathOfName = cursor.getString(column_index_folder_name);
            if (folderName.equals(pathOfName)){
                listImageOnAlbum.add(pathOfImage);
            }
        }
        return listImageOnAlbum;
    }
    public static int firstImageOnAlbum(List<String> listImageOnALbum){
        return Integer.parseInt(listImageOnALbum.get(0));
    }
    public static String numberImageOnAlbum(List<String> listImageOnAlbum){
        int number = 0;
        for (String imgOnALbum : listImageOnAlbum){
            number++;
        }
        return Integer.toString(number);
    }
}
