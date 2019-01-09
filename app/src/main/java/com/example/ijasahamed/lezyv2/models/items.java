package com.example.ijasahamed.lezyv2.models;

public class items {

    private int item_id;
    private String item_name;
    private String item_img;
    private float item_price;
    private int item_availability;
    private int item_type;
    private String item_category;
    private int shop_id;
    private String shop_name;
    private String item_descriptions;
    private float Avg_rating;


    public items() {
    }

    public items(int item_id, String item_name, String item_img, float item_price, int item_availability, int item_type, String item_category, int shop_id,String shop_name, String item_descriptions, float avg_rating) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_img = item_img;
        this.item_price = item_price;
        this.item_availability = item_availability;
        this.item_type = item_type;
        this.item_category = item_category;
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.item_descriptions = item_descriptions;
        Avg_rating = avg_rating;

    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_img() {
        return item_img;
    }

    public float getItem_price() {
        return item_price;
    }

    public int getItem_availability() {
        return item_availability;
    }

    public int getItem_type() {
        return item_type;
    }

    public String getItem_category() {
        return item_category;
    }

    public String getShop_name() {
        return shop_name;
    }

    public String getItem_descriptions() {
        return item_descriptions;
    }

    public float getAvg_rating() {
        return Avg_rating;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }

    public void setItem_price(float item_price) {
        this.item_price = item_price;
    }

    public void setItem_availability(int item_availability) {
        this.item_availability = item_availability;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public void setItem_category(String item_category) {
        this.item_category = item_category;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setItem_descriptions(String item_descriptions) {
        this.item_descriptions = item_descriptions;
    }

    public void setAvg_rating(float avg_rating) {
        Avg_rating = avg_rating;
    }
}
