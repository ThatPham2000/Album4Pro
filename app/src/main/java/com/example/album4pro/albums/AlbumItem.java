package com.example.album4pro.albums;

import android.net.Uri;

import java.io.Serializable;

public class AlbumItem implements Serializable {
    private String resourceId;
    private String name;
    private String number;

    public AlbumItem(String resourceId, String name, String number) {
        this.resourceId = resourceId;
        this.name = name;
        this.number = number;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}
