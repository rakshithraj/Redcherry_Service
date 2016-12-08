package com.app.redcherry.Model;

/**
 * Created by rakshith raj on 12-06-2016.
 */
public class BookingDetails {

    private String userid;
    private String fname;
    private String vechicle_type;
    private String vechicle_number;
    private String service_center_id;
    private String branchname;
    private String service_type_id;
    private String service_type;
    private String service_type_desc;
    private String booking_date;
    private String booking_completed_date;
    private String booking_time;
    private String pickup;
    private String vechicle_id;

    public String getVechicleName() {
        return vechicleName;
    }

    public void setVechicleName(String vechicleName) {
        this.vechicleName = vechicleName;
    }

    private String vechicleName;
    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    private String service_charge;
    public String getService_type_desc() {
        return service_type_desc;
    }

    public void setService_type_desc(String service_type_desc) {
        this.service_type_desc = service_type_desc;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    private String slot;
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

    public String getVechicle_type() {
        return vechicle_type;
    }

    public void setVechicle_type(String vechicle_type) {
        this.vechicle_type = vechicle_type;
    }

    public String getVechicle_number() {
        return vechicle_number;
    }

    public void setVechicle_number(String vechicle_number) {
        this.vechicle_number = vechicle_number;
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


    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_completed_date() {
        return booking_completed_date;
    }

    public void setBooking_completed_date(String booking_completed_date) {
        this.booking_completed_date = booking_completed_date;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getVechicle_id() {
        return vechicle_id;
    }

    public void setVechicle_id(String vechicle_id) {
        this.vechicle_id = vechicle_id;
    }
}
