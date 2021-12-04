package com.example.album4pro.gallery;

public class Configuration {
    private GalleryAdapter galleryAdapter;

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
}
