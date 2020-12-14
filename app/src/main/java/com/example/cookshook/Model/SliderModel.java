package com.example.cookshook.Model;

import android.widget.SearchView;

import java.io.Serializable;

public class SliderModel implements Serializable {
    String cuisine , img , postid;

    public SliderModel() {
    }

    public SliderModel(String cuisine, String img, String postid) {
        this.cuisine = cuisine;
        this.img = img;
        this.postid = postid;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
