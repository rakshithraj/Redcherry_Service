package com.app.redcherry.Ulility;

/**
 * Created by rakshith raj on 19-05-2016.
 */
public final class Config {
    private static final String BASE_URL ="http://redcherryservices.com/webpanel/admin/api/";
    public static final String LOGIN_URL = BASE_URL+"login";
    public static final String REGISTER_URL = BASE_URL+"register";
    public static final String VECHICLE_LIST = BASE_URL+"getmyvehicle";
    public static final String NOTIFICATION_LIST = BASE_URL+"user_notification/";
    public static final String COMPANY_CAR_LIST = BASE_URL+"getbrands/Car/0";
    public static final String COMPANY_BIKE_LIST =BASE_URL+"getbrands/Bike/0";

    public static final String BRAND_CAR_LIST = BASE_URL+"getbrands/Car/";
    public static final String BRAND_BIKE_LIST = BASE_URL+"getbrands/Bike/";
    public static final String ADD_VECHICLE =BASE_URL+"addvehicle";
    public static final String DELETE_VECHICLE =BASE_URL+"deletemyvehicle";

    public static final String SERVICE_CENTER_LIST =BASE_URL+"getservice";
    public static final String SERVICE_TYPE_LIST =BASE_URL+"getservicetype";
    public static final String Confirm_BOOK =BASE_URL+"confirmbook";
    public static final String HISTORY =BASE_URL+"myhistory";
    public static final String CANCEL_BOOKING =BASE_URL+"cancelbooking";

    public static final String MY_UPCOMMING_BOOKED =BASE_URL+"mybookinghistory";

    public static final String DOOR_STEP_LIST =BASE_URL+"getcarwashservicecenter/doorstep";
    public static final String WORK_STATION_STEP_LIST =BASE_URL+"getcarwashservicecenter/wash_station";
    public static final String BOOK_CAR_WASH =BASE_URL+"addcarwash";

    public static final String ADEVERTISE =BASE_URL+"getadvertise";



    public static final String CAR_WASH_TYPE =BASE_URL+"getWoshtype";
    public static final String CAR_WASH_PRICE =BASE_URL+"getcarwashprice";

    public static final String UPDATE_PROFILE =BASE_URL+"updateprofile";

    public static final String ADD_REVIEW =BASE_URL+"addreview";

    public static final String UPDATE_NUMBER = BASE_URL+"updatemobilenumber";
    public static final String UPDATE_VEHILCE = BASE_URL+"updatevehicle";
    public static final String SOCIAL_LOGIN = BASE_URL+"eregister";
    public static final String SEND_OTP = BASE_URL+"getotp";
    public static final String MOBILE_VERIFY_OTP = BASE_URL+"mobileverify";

    public static final String RESET_PASSWORD = BASE_URL+"resetpassword";


}
