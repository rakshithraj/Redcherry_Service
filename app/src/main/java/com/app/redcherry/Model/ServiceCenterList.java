package com.app.redcherry.Model;

import java.util.ArrayList;

/**
 * Created by rakshith raj on 05-06-2016.
 */
public class ServiceCenterList {

    private ArrayList<ServiceCenterInfo> data = new ArrayList<>();

    public ArrayList<ServiceCenterInfo> getData() {
        return data;
    }

    public void setData(ArrayList<ServiceCenterInfo> data) {
        this.data = data;
    }
}
