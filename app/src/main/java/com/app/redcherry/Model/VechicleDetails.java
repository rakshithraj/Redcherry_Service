package com.app.redcherry.Model;

import java.io.Serializable;

/**
 * Created by rakshith raj on 03-06-2016.
 */
public class VechicleDetails implements Serializable {

    private String id;
    private String userid;
    private String vtype;
    private String vbrand;
    private String vnumber;
    private String subbrand;
    private String fueltype;
    private String last_insured;
    private String last_emission;
    private String mobileno;
    private String createdate;
    private String brandname;

    public String getSubbrand_name() {
        return subrand_name;
    }

    public void setSubbrand_name(String subbrand_name) {
        this.subrand_name = subbrand_name;
    }

    private String subrand_name;

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

    public String getVtype() {
        return vtype;
    }

    public void setVtype(String vtype) {
        this.vtype = vtype;
    }

    public String getVbrand() {
        return vbrand;
    }

    public void setVbrand(String vbrand) {
        this.vbrand = vbrand;
    }

    public String getVnumber() {
        return vnumber;
    }

    public void setVnumber(String vnumber) {
        this.vnumber = vnumber;
    }

    public String getLast_insured() {
        return last_insured;
    }

    public void setLast_insured(String last_insured) {
        this.last_insured = last_insured;
    }

    public String getLast_emission() {
        return last_emission;
    }

    public void setLast_emission(String last_emission) {
        this.last_emission = last_emission;
    }

    public String getSubbrand() {
        return subbrand;
    }

    public void setSubbrand(String subbrand) {
        this.subbrand = subbrand;
    }

    public String getFueltype() {
        return fueltype;
    }

    public void setFueltype(String fueltype) {
        this.fueltype = fueltype;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }
}
