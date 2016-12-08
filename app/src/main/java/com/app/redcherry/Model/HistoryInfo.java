package com.app.redcherry.Model;

import android.text.TextUtils;

import com.app.redcherry.Ulility.Constants;

import java.io.Serializable;

/**
 * Created by rakshith raj on 25-06-2016.
 */
public class HistoryInfo implements Serializable {

    private String id;



    private String woshid;
    private String userid;
    private String fname;
    private String vehicle_id;
    private String vehicle_type;
    private String vehicle_number;
    private String service_center_id;
    private String branchname;
    private String service_type_id;
    private String service_type;
    private String service_type_desc;
    private String avslot;
    private String service_charge;
    private String booking_date;
    private String pickuptime;
    private String booking_completed_date;
    private String booking_id;
    private String cancel;
    private String pickup;
    private String reason;
    private String vstatus;
    private String dtype;
    private String createdate;

    public String getScid() {
        return scid;
    }

    public void setScid(String scid) {
        this.scid = scid;
    }

    private String scid;
    public String getVehicle_status() {
        return vehicle_status;
    }

    public String getWoshid() {
        return woshid;
    }

    public void setWoshid(String woshid) {
        this.woshid = woshid;
    }
    public void setVehicle_status(String vehicle_status) {
        this.vehicle_status = vehicle_status;
    }

    public String getService_type_desc() {
        return service_type_desc;
    }

    public void setService_type_desc(String service_type_desc) {
        this.service_type_desc = service_type_desc;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSub_brand() {
        return sub_brand;
    }

    public void setSub_brand(String sub_brand) {
        this.sub_brand = sub_brand;
    }

    public String getBook_type() {
        return book_type;
    }

    public void setBook_type(String book_type) {
        this.book_type = book_type;
    }

    private String vehicle_status;
    private String       booking_status;
    private String       brand;
    private String sub_brand;
    private String       book_type;


    public String getScharge() {
        return scharge;
    }

    public void setScharge(String scharge) {
        this.scharge = scharge;
    }

    private String scharge;
    public String getFueltype() {
        return fueltype;
    }

    public void setFueltype(String fueltype) {
        this.fueltype = fueltype;
    }

    private String fueltype="Petrol";
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_type() {
        if(TextUtils.isEmpty(vehicle_type))
            return Constants.Car;
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getService_center_id() {
        return service_center_id;
    }

    public void setService_center_id(String service_center_id) {
        this.service_center_id = service_center_id;
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getService_type_id() {
        return service_type_id;
    }

    public void setService_type_id(String service_type_id) {
        this.service_type_id = service_type_id;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getAvslot() {
        return avslot;
    }

    public void setAvslot(String avslot) {
        this.avslot = avslot;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getPickuptime() {
        return pickuptime;
    }

    public void setPickuptime(String pickuptime) {
        this.pickuptime = pickuptime;
    }

    public String getBooking_completed_date() {
        return booking_completed_date;
    }

    public void setBooking_completed_date(String booking_completed_date) {
        this.booking_completed_date = booking_completed_date;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getVstatus() {
        return vstatus;
    }

    public void setVstatus(String vstatus) {
        this.vstatus = vstatus;
    }

    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
