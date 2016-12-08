package com.app.redcherry.Model;

/**
 * Created by rakshith raj on 17-07-2016.
 */
public class CarWashType  {
    private String id;
    private String washtype;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String price="120";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWashtype() {
        return washtype;
    }

    public void setWashtype(String washtype) {
        this.washtype = washtype;
    }
}
