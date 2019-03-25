package com.moahammedomer.networkingliberaries;

public class Product {

    private String name, description, category, country;
    private double price;

    Product(int id, String name, double price, String description, String category, String country){
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.country = country;
    }

    public String getImageUrl(){
        String server_images_url = MainActivity.SERVER_URL + "imageUpload/";
        return server_images_url + getId() + ".jpg";
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

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    private int id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getCountry() {
        return country;
    }

    public double getPrice() {
        return price;
    }

}
