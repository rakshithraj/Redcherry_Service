package com.app.redcherry.Constants;

final class WsConstant {


    //    public static final String URL = "http://redcherry.cearsinfotech.com/webservices/api.php";
    public static final String URL = "http://webpanel.redcherryservices.com/admin/api/";
    /**
     * ******* WS Parameters Request Types ***********
     */

    public static final String REQ_SCID = "scId";
    public static final String REQ_BRANCENAME = "BranceName";

    public static final String REQ_GETEVENT = "getevent";
    public static final String REQ_CONTACTUS = "contactus";
    public static final String REQ_FORGOTPASSWORD = "forgotpassword";
    public static final String REQ_GETBRAND = "getbrands";
    public static final String REQ_GETFAQ = "getfaq";
    public static final String REQ_SENDASSISTANT = "sendassistant";
    public static final String REQ_COMPANY = "getcompany";
    public static final String REQ_GETDRIVE = "getdrive";


    /**
     * ******* WS Parameters ***********
     */
    public static final String Param_USERID = "userid";
    public static final String Param_WASHTYPE = "washtypes";
    public static final String Param_REASON = "reason";
    //Parameters for Emargency Login;
    public static final String Param_EMERGENCY_LOGIN_NAME = "fname";
    public static final String Param_EMERGENCY_LOGIN_MOBILENO = "mobileno";
    public static final String Param_EMERGENCY_LOGIN_DEVICEID = "deviceid";
    public static final String Param_EMERGENCY_LOGIN_LATITUDE = "lat";
    public static final String Param_EMERGENCY_LOGIN_LONGITUDE = "long";
    public static final String Param_EMERGENCY_LOGIN_MESSAGE = "message";
    public static final String Param_COMPANYID = "companyid";
    public static final String Param_EMERGENCY_LOGIN_LANDMARK = "landmark";
    public static final String Param_LOGIN_EMAIL = "email";
    public static final String Param_LOGIN_PASSWORD = "password";
    public static final String Param_FACEBOOK_EMAIL = "email";
    public static final String Param_FACEBOOK_ADDRESS = "address";
    public static final String Param_GENDER = "gender";
    public static final String Param_EMERGENCY_LOGIN_VEHICLE = "vehicletype";

    // Parameters Device Login
    public static final String Param_FACEBOOK_MOBILENO = "mobileno";
    public static final String Param_FACEBOOK_FISRTNAME = "fname";

    // Parameters Device facebookLogin
    public static final String Param_FACEBOOK_IMAGE = "image";
    public static final String Param_FACEBOOK_FACEBOOKID = "fbid";
    // Parameters Device googleLogin
    public static final String Param_GOOGLE_EMAIL = "email";
    public static final String Param_GOOGLE_ADDRESS = "address";
    public static final String Param_GOOGLE_MOBILENO = "mobileno";
    public static final String Param_GOOGLE_FISRTNAME = "fname";
    public static final String Param_GOOGLE_IMAGE = "image";
    public static final String Param_GOOGLE_GOOGLEID = "gid";
    public static final String Param_GOOGLE_PASSWORD = "password";
    public static final String Param_REGISTRATION_DEVICEID = "deviceid";
    public static final String Param_REGISTRATION_FNAME = "fname";
    public static final String Param_REGISTRATION_MOBILENO = "mobileno";
    public static final String Param_REGISTRATION_EMAIL = "email";


    // Parameters Device Registration
    public static final String Param_REGISTRATION_ADDRESS = "address";
    public static final String Param_REGISTRATION_PASSWORD = "password";
    public static final String Param_REGISTRATION_IMAGE = "image";
    public static final String Param_ADDVEHICLE_VEHICLE_TYPE = "vtype";
    public static final String Param_ADDVEHICLE__VEHICLE_BRAND = "vbrand";
    public static final String Param_ADDVEHICLE__VEHICLE_NUMBER = "vnumber";
    public static final String Param_ADDVEHICLE__LASTENSURED_DATE = "last_insured";
    public static final String Param_ADDVEHICLE_MOBILE_NUMBER = "mobileno";


    // Parameters Device Add Vehicle
    public static final String Param_ADDVEHICLE__LASTEMISSION_DATE = "last_emission";
    public static final String Param_HOME_LATITUDE = "lat";
    public static final String Param_HOME_LONGITUDE = "lng";
    public static final String Param_REVIEW_SID = "sid";
    public static final String Param_REVIEW_USERID = "userid";
    public static final String Param_ADDVEHICLE_FUELTYPE = "fueltype";
    public static final String Param_SBRAND = "sbrand";



    //Parameters Device get ServiceCenterObject
    public static final String Param_REVIEW_RATTING = "rating";
    public static final String Param_REVIEW_COMMENT = "comment";

    //Parameters Device get Reviews
    public static final String Param_BOOKSERVICE_SCID = "scid";
    public static final String Param_BOOKSERVICE_VID = "vid";
    public static final String Param_BOOKSERVICE_VNUMBER = "vnumber";
    public static final String Param_BOOKSERVICE_SID = "sid";

    //WashCar Serivice
    public static final String Param_CARWASH_WID = "wid";

    // Parameters Device Book Service
    public static final String Param_BOOKSERVICE_DATE = "date";
    public static final String Param_BOOKSERVICE_PICKTIME = "pickuptime";
    public static final String Param_PICKUP = "pickup";
    // Parameters Device Confirm  Book Service
    public static final String Param_CONFIRMBOOKSERVICE_FNAME = "fname";
    public static final String Param_CONFIRMBOOKSERVICE_VEHICLENUMBER = "vnumber";
    public static final String Param_CONFIRMBOOKSERVICE_BRANCHNAME = "branchname";
    public static final String Param_CONFIRMBOOKSERVICE_STYPE = "stype";
    public static final String Param_CONFIRMBOOKSERVICE_VTYPE = "vtype";
    public static final String Param_CONFIRMBOOKSERVICE_DISCRIPTION = "descofser";
    public static final String Param_CONFIRMBOOKSERVICE_AVAILABLESLOT = "avslot";
    public static final String Param_CONFIRMBOOKSERVICE_SERVICECHARGE = "scharge";
    public static final String Param_CONFIRMBOOKSERVICE_SERVICEDATE = "sdate";
    public static final String Param_CONFIRMBOOKSERVICE_SCDATE = "scdate";
    public static final String Param_CONFIRMBOOKSERVICE_STID = "stid";
    public static final String Param_CONFIRMBOOKSERVICE_SCID = "scid";
    //Cancel Booking
    public static final String Param_BOOKINGID = "bid";
    //Delete Vehicle
    public static final String Param_VEHICLEID = "vid";
    // Parameters car wash
    public static final String Param_CARWASHID = "woshid";
    public static final String Param_CARWASHCONTACTNO = "contactno";
    public static final String Param_CARWASHNAME = "name";
    public static final String Param_CARWASHEMAIL = "email";

    public static final String Param_scId = "scid";
    public static final String Param_branchNAME = "branchname";
    public static final String Param_CarModel = "carmodel";
    public static final String Param_LANDMARK = "landmark";
    public static final String Param_NAME = "name";

    // Parameters add DrivingSchoolRating
    public static final String Param_DRIVINGSCHOOLID = "dsid";
    public static final String Param_DRIVINGSCHOOL_USERID = "userid";
    public static final String Param_DRIVINGSCHOOL_RATING = "rating";
    public static final String Param_DRIVINGSCHOOL_COMMENT = "comment";
    public static final String Param_UPDATEVEHICLE_VID = "vid";
    public static final String Param_UPDATEVEHICLE_VTYPE = "vtype";
    public static final String Param_UPDATEVEHICLE_VBARND = "vbrand";
    public static final String Param_UPDATEVEHICLE_LAST_ENSURED = "last_insured";


    //Update Add Vehicles
    public static final String Param_UPDATEVEHICLE_LAST_EMISSION = "last_emission";
    public static final String Param_UPDATEVEHICLE_VNUMBER = "vnumber";
    public static final String Param_DRIVINGSCHOOLBOOKING_DID = "did";
    public static final String Param_DRIVINGSCHOOLBOOKING_NAME = "name";
    public static final String Param_DRIVINGSCHOOLBOOKING_CONTACTNO = "contactno";
    public static final String Param_DRIVINGSCHOOLBOOKING_MESSAGE = "message";
    public static final String Param_DESCRIPTION = "description";
    public static final String Param_LEARNID = "learnid";
    public static final String Param_PACKAGEID = "packageid";



    // Driving School Booking
    public static final String Param_DRIVINGSCHOOLBOOKING_DSNAME = "dsname";
    public static final boolean isNavigationDrawer = true;
    public static final boolean isAddNew = false;
    public static final boolean isBookingService = false;
    public static final boolean isFromMyListAdd = false;
    public static final boolean isemergencyLogin = false;
}