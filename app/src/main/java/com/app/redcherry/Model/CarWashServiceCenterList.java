package com.app.redcherry.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rakshith raj on 09-07-2016.
 */
public class CarWashServiceCenterList implements Serializable {
    public ArrayList<CarWashServiceCenterInfo> getData() {
        return data;
    }

    public void setData(ArrayList<CarWashServiceCenterInfo> data) {
        this.data = data;
    }

    private ArrayList<CarWashServiceCenterInfo> data = new ArrayList<>();


}
