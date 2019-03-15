package com.moahammedomer.networkingliberaries;

import com.android.volley.toolbox.StringRequest;

public class Product {

    private int id;
    private String name, description, image_url;
    private boolean inStock;
    private float price;

    Product(String name, String description, boolean inStock, float price, int id){
        this.description = description;
        this.name = name;
        this.image_url = "" + id + ".jpg";
        this.inStock = inStock;
        this.price = price;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }

    public boolean isInStock() {
        return inStock;
    }

    public float getPrice() {
        return price;
    }
}
