package com.example.album4pro;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        SharedPreferences sharedPreferences = context.getSharedPreferences("save", Context.MODE_PRIVATE);
        String viewAs = sharedPreferences.getInt("view", 0) == 0 ? " DESC" : " ASC";
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + viewAs);

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

        SharedPreferences sharedPreferences = context.getSharedPreferences("save", Context.MODE_PRIVATE);
        String viewAs = sharedPreferences.getInt("view", 0) == 0 ? " DESC" : " ASC";
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + viewAs);

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
        SharedPreferences sharedPreferences = context.getSharedPreferences("save", Context.MODE_PRIVATE);
        String viewAs = sharedPreferences.getInt("view", 0) == 0 ? " DESC" : " ASC";
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + viewAs);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        // get folder name
        //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()){
            pathOfVideo = cursor.getString(column_index_data);
            listOfAllVideo.add(pathOfVideo);
        }

        return listOfAllVideo;
    };

    public static ArrayList<String> listPhotoFilter(Context context, String searchDate){
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
        SharedPreferences sharedPreferences = context.getSharedPreferences("save", Context.MODE_PRIVATE);
        String viewAs = sharedPreferences.getInt("view", 0) == 0 ? " DESC" : " ASC";
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + viewAs);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        // get folder name
        //column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()){
            pathOfImage = cursor.getString(column_index_data);

            //Filter
            String createdDate = "";

            //Get date y/m/d
            File file = new File(pathOfImage);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd");
            if(file.exists()) //Extra check, Just to validate the given path
            {
                ExifInterface intf = null;
                try {
                    intf = new ExifInterface(pathOfImage);
                    if(intf != null)
                    {
                        createdDate = intf.getAttribute(ExifInterface.TAG_DATETIME);
                        //Date temp =
                        if (createdDate != null) {
                            createdDate = simpleDateFormat.format(stringToDate(createdDate));
                        } else {
                            Date lastModDate = new Date(file.lastModified());
                            createdDate = simpleDateFormat.format(lastModDate);
                        }
                    }
                } catch (IOException e) {

                }
                if(intf == null) {
                    Date lastModDate = new Date(file.lastModified());
                    createdDate = "" + simpleDateFormat.format(lastModDate);
                }
            }

            //filter by comparing original day and search day
            if (searchDate.equals(createdDate)) {
                listOfAllImage.add(pathOfImage);
            }
        }

        return listOfAllImage;
    };

    private static Date stringToDate(String aDate) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }
    //=======================================================================================
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
        SharedPreferences sharedPreferences = context.getSharedPreferences("save", Context.MODE_PRIVATE);
        String viewAs = sharedPreferences.getInt("view", 0) == 0 ? " DESC" : " ASC";
        cursor = context.getContentResolver().query(uri, projection, null, null, orderBy + viewAs);
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

    public static String firstImageOnAlbum(List<String> listImageOnALbum){

        return listImageOnALbum.get(0);
    }

    public static String numberImageOnAlbum(List<String> listImageOnAlbum){
        int number = 0;
        for (String imgOnALbum : listImageOnAlbum){
            number++;
        }
        return Integer.toString(number);
    }
    public static ArrayList<String> insertImageOnAlbum(Context context, List<String> pickedImage, String newFolderName){
        Uri uri;
        Cursor cursor;
        ArrayList<String> insertedList = new ArrayList<String>();

        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };
        String orderBy = MediaStore.Images.Media.DATE_TAKEN;


        return insertedList;
    }
}
