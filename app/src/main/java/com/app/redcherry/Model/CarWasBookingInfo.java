package com.app.redcherry.Model;

import java.io.Serializable;

/**
 * Created by rakshith raj on 28-08-2016.
 */
public class CarWasBookingInfo implements Serializable {

    private String fname;

    private String scharge;

    private String booking_id;

    private String branchname;

    private String description;

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    private  String sdate;

    public String getWashType() {
        return washType;
    }

    public void setWashType(String washType) {
        this.washType = washType;
    }

    private String washType;

    public String getBookingCharge() {
        return bookingCharge;
    }

    public void setBookingCharge(String bookingCharge) {
        this.bookingCharge = bookingCharge;
    }

    private String bookingCharge;


    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getScharge() {
        return scharge;
    }

    public void setScharge(String scharge) {
        this.scharge = scharge;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
