package com.example.cookshook.Model;

import java.io.Serializable;

public class RecipeModel implements Serializable {
    String id ,cuisine , recipename , recipe , imgurl , duration , postdate , posttime;

    public RecipeModel() {
    }

    public RecipeModel(String id, String cuisine, String recipename, String recipe, String imgurl, String duration, String postdate, String posttime) {
        this.id = id;
        this.cuisine = cuisine;
        this.recipename = recipename;
        this.recipe = recipe;
        this.imgurl = imgurl;
        this.duration = duration;
        this.postdate = postdate;
        this.posttime = posttime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getRecipename() {
        return recipename;
    }

    public void setRecipename(String recipename) {
        this.recipename = recipename;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
