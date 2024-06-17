package com.example.rulushop;

import android.net.Uri;

public class Product {
    private String img;
    private String title;
    private String description;
    private float rating;
    private float price;

    public Product() {
    }
    public Product(String img, String title, String description, float rating, float price) {
        this.img = img;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
