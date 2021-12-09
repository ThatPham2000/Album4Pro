package com.example.album4pro.albums;

import java.io.Serializable;

public class AlbumItem implements Serializable {
    private int resourceId;
    private String name;
    private String number;

    public AlbumItem(int resourceId, String name, String number) {
        this.resourceId = resourceId;
        this.name = name;
        this.number = number;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
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
