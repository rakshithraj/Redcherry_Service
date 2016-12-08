package com.app.redcherry.Model;

import java.util.ArrayList;

/**
 * Created by rakshith raj on 04-06-2016.
 */
public class CompanyList {
    private ArrayList<CompnayInfo> data = new ArrayList<>();


    public ArrayList<CompnayInfo> getData() {
        return data;
    }

    public void setData(ArrayList<CompnayInfo> data) {
        this.data = data;
    }
}
