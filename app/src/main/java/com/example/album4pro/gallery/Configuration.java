package com.example.album4pro.gallery;

import java.util.ArrayList;

public class Configuration {
    private GalleryAdapter galleryAdapter;
    private ArrayList<String> listPhotoDelete = new ArrayList<>();

    private static Configuration instance;

    private Boolean isVideo = false;

    public static Configuration getInstance(){
        if(instance == null){
            instance = new Configuration();
        }
        return instance;
    }

    private Configuration(){

    }

    public GalleryAdapter getGalleryAdapter() {
        return galleryAdapter;
    }

    public void setGalleryAdapter(GalleryAdapter galleryAdapter) {
        this.galleryAdapter = galleryAdapter;
    }

    public Boolean getVideo() {
        return isVideo;
    }

    public void setVideo(Boolean video) {
        isVideo = video;
    }

    public ArrayList<String> getListPhotoDelete() {
        return listPhotoDelete;
    }

    public void setListPhotoDelete(ArrayList<String> listPhotoDelete) {
        this.listPhotoDelete = listPhotoDelete;
    }
}
