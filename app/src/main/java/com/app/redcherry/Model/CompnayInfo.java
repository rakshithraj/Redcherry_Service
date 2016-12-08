package com.app.redcherry.Model;

/**
 * Created by rakshith raj on 04-06-2016.
 */
public class CompnayInfo{
    private String id;
    private String brand;
    private String parent_brand_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getParent_brand_id() {
        return parent_brand_id;
    }
    public void setParent_brand_id(String parent_brand_id) {
        this.parent_brand_id = parent_brand_id;
    }
}
