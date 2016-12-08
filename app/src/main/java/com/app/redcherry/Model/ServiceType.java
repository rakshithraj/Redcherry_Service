package com.app.redcherry.Model;

import java.util.ArrayList;

/**
 * Created by rakshith raj on 12-06-2016.
 */
public class ServiceType {
    public ArrayList<ServiceTypeInfo> getData() {
        return data;
    }

    public void setData(ArrayList<ServiceTypeInfo> data) {
        this.data = data;
    }

    private ArrayList<ServiceTypeInfo> data = new ArrayList<>();
}
