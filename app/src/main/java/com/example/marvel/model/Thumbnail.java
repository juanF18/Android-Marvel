package com.example.marvel.model;

import java.io.Serializable;

public class Thumbnail implements Serializable {
    public String path;
    public String extension;

    public String getExtension() {
        return extension;
    }

    public String getPath() {
        return path;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
