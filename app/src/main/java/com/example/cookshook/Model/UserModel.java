package com.example.cookshook.Model;

import java.io.Serializable;

public class UserModel implements Serializable {
    String name , password, mobile , id , img_url , email , status;


    public UserModel() {
    }

    public UserModel(String name, String password, String mobile, String id, String img_url, String email, String status, String title, String location, String description) {
        this.name = name;
        this.password = password;
        this.mobile = mobile;
        this.id = id;
        this.img_url = img_url;
        this.email = email;
        this.status = status;

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}

