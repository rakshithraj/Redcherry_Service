package com.app.redcherry.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rakshith raj on 09-07-2016.
 */
public class CarWashServiceCenterInfo implements Serializable {

private String id;
    private String branchname;
    private String location;
    private String city;
    private String authorised;
    private String lat;
    private String lng;
    private String service_time;
    private String wash_center_type;
    private String slots;
    private String price;
    private String service_center_code;
    private String status;
    private String imid;
    private String image;
    private ArrayList<Review> reviews = new ArrayList<>();

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    private String average="0";
    private DistanceInfo distance;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAuthorised() {
        return authorised;
    }

    public void setAuthorised(String authorised) {
        this.authorised = authorised;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getService_time() {
        return service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time;
    }

    public String getWash_center_type() {
        return wash_center_type;
    }

    public void setWash_center_type(String wash_center_type) {
        this.wash_center_type = wash_center_type;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getService_center_code() {
        return service_center_code;
    }

    public void setService_center_code(String service_center_code) {
        this.service_center_code = service_center_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImid() {
        return imid;
    }

    public void setImid(String imid) {
        this.imid = imid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public DistanceInfo getDistance() {
        return distance;
    }

    public void setDistance(DistanceInfo distance) {
        this.distance = distance;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
