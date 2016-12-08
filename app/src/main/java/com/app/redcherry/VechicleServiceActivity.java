package com.app.redcherry;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.app.redcherry.Adapter.VechicleListAdapter;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Interface.ConfirmInterface;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Interface.VechicleListInterface;
import com.app.redcherry.Model.CompanyList;
import com.app.redcherry.Model.CompnayInfo;
import com.app.redcherry.Model.VechicleDetails;
import com.app.redcherry.Model.VechicleList;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.webservice.ConnectWebService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class VechicleServiceActivity extends BaseActivity implements VechicleListInterface, View.OnClickListener {

    private RecyclerView vechicle_list;
    private VechicleListAdapter vechicleListAdapter;
    private ArrayList<VechicleDetails> vechicleList = new ArrayList<>();
    private boolean loading = false;
    private Button btAddVechicle;
    private Button btAdd;
    private Button btCancel;
    private LinearLayout lyCar;
    private LinearLayout lyBike;
    private View viewBike;
    private View viewCar;
    private RelativeLayout lyCompany;
    private RelativeLayout lyBrand;
    private RelativeLayout lyFuel;
    private RelativeLayout lyLastInsured;
    private RelativeLayout lyLastEmmissioned;
    private TextView tvCompany;
    private TextView tvBrand;
    private TextView tvFuel;
    private TextView tvLastInsured;
    private TextView tvLastEmmissioned;
    private String type = "Car";
    private String brand;
    private String fuel;
    private String company;
    private String vechicleNumber;
    private String lastInsured;
    private String lastEmmissioned;
    private EditText etVechicelNumber1;
    private EditText etVechicelNumber2;
    private EditText etVechicelNumber3;
    private EditText etVechicelNumber4;
    private ArrayList<CompnayInfo> companyCarList = new ArrayList<>();
    private ArrayList<CompnayInfo> companyBikeList = new ArrayList<>();

    private ArrayList<CompnayInfo> brandList = new ArrayList<>();

    boolean onClickList = true;

    private LinearLayout lyRefersh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vechicle_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intialize();


        apiCall();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        vechicle_list = null;
        vechicleList = null;

        btAddVechicle = btAdd = btCancel = null;
        lyCar = lyBike = null;
        viewBike = viewCar = null;
        lyCompany = lyBrand = lyFuel = lyLastInsured = lyLastEmmissioned;
        tvCompany = tvBrand = tvFuel = tvLastInsured = tvLastEmmissioned = null;
        etVechicelNumber1 = etVechicelNumber2 = etVechicelNumber3 = etVechicelNumber4 = null;
        companyCarList = null;
        companyBikeList = null;

        brandList = null;


        lyRefersh = null;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.lyRefersh:
                apiCall();
                break;
            case R.id.btAddVechicle:
                clearData();
                displayAddDialog();
                break;
            case R.id.lyBike:
                viewCar.setBackgroundColor(Color.parseColor("#F2F2F2"));
                viewBike.setBackgroundColor(Color.parseColor("#DD1C13"));
                type = "Bike";

                selectedCompanyList = -1;
                company = "";
                tvCompany.setText("Select Manufacturer");

                brand = "";
                selectedBrandList = -1;
                tvBrand.setText("Enter Brand");

                break;
            case R.id.lyCar:
                viewBike.setBackgroundColor(Color.parseColor("#F2F2F2"));
                viewCar.setBackgroundColor(Color.parseColor("#DD1C13"));
                type = "Car";

                selectedCompanyList = -1;
                company = "";
                tvCompany.setText("Select Manufacturer");

                brand = "";
                selectedBrandList = -1;
                tvBrand.setText("Enter Brand");

                break;
            case R.id.lyCompany:
                if (type.equals("Car"))
                    getCompanyCarList();
                else
                    getCompanyBikeList();

                brand = "";
                selectedBrandList = -1;
                tvBrand.setText("Enter Brand");
                break;
            case R.id.lyBrand:
                if (TextUtils.isEmpty(company)) {
                    Utility.alertDialog(VechicleServiceActivity.this, "please select manufacturer");
                    return;
                }

                getBrandList();
                break;
            case R.id.lyFuel:
                showFuelList();
                break;
            case R.id.lyLastInsured:
                showLastInsured();
                break;
            case R.id.lyLastEmmissioned:
                showLastEmmissioned();
                break;
            case R.id.btAdd:


                callAddApi();
                break;
        }


    }

    private void clearData() {
        companyCarList.clear();
        companyBikeList.clear();
        brandList.clear();
        type="Car";
        company  =null;
        brand=null;
        fuel=null;
        vechicleNumber=null;
        lastInsured=null;
        lastEmmissioned=null;

    }

    private void callEditApi(String id) {
        if (!isDataValid()) {
            return;
        }


        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    String SUCESSS = "1", FAILURE = "0";
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("success");
                        String message = jsonObject.getString("message");

                        if (status.trim().equals(FAILURE)) {
                            Utility.alertDialog(VechicleServiceActivity.this, message);
                        } else {
                            addVechicle_dialog.cancel();
                            vechicleList.clear();
                            vechicleListAdapter.notifyDataSetChanged();
                            apiCall();
                            Utility.alertDialog(VechicleServiceActivity.this, "Vehicle Updated successfully");


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(VechicleServiceActivity.this, VechicleServiceActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {
                    loading = status;
                }

                @Override
                public boolean getLoading() {
                    return loading;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<>();
            parameter.put("userid", Utility.getString(Constants.userId, this));
            parameter.put("vtype", type);
            parameter.put("vbrand", company);
            parameter.put("subbrand", brand);
            parameter.put("fueltype", fuel);
            parameter.put("vnumber", vechicleNumber);
            parameter.put("last_insured", lastInsured);
            parameter.put("last_emission", lastEmmissioned);
            parameter.put("id", id);

            //parameter.put("vbrand",company);

Log.d("tag","userid ,"+Utility.getString(Constants.userId, this)+"\n"+
        "vtype ,"+type+"\n"+
        "vbrand ,"+company+"\n"+
        "subbrand ,"+brand+"\n"+
        "fueltype ,"+fuel+"\n"+
        "vnumber ,"+vechicleNumber+"\n"+
        "last_insured ,"+lastInsured+"\n"+
        "id ,"+id+"\n");


            connectWebService.stringPostRequest(Config.UPDATE_VEHILCE, VechicleServiceActivity.this, parameter);


        } else {
            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }


    }

    private void callAddApi() {
        if (!isDataValid()) {
            return;
        }


        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    String SUCESSS = "1", FAILURE = "0";
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("success");
                        String message = jsonObject.getString("message");

                        if (status.trim().equals(FAILURE)) {
                            Utility.alertDialog(VechicleServiceActivity.this, message);
                        } else {
                            addVechicle_dialog.cancel();
                            vechicleList.clear();
                            vechicleListAdapter.notifyDataSetChanged();
                            apiCall();
                            Utility.alertDialog(VechicleServiceActivity.this, message);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(VechicleServiceActivity.this, VechicleServiceActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {
                    loading = status;
                }

                @Override
                public boolean getLoading() {
                    return loading;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<>();
            parameter.put("userid", Utility.getString(Constants.userId, this));
            parameter.put("vtype", type);
            parameter.put("vbrand", company);
            parameter.put("subbrand", brand);
            parameter.put("fueltype", fuel);
            parameter.put("vnumber", vechicleNumber);
            parameter.put("last_insured", lastInsured);
            parameter.put("last_emission", lastEmmissioned);


            //parameter.put("vbrand",company);


            connectWebService.stringPostRequest(Config.ADD_VECHICLE, VechicleServiceActivity.this, parameter);


        } else {
            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }


    }

    private boolean isDataValid() {

        if (TextUtils.isEmpty(company)) {
            Utility.alertDialog(this, "Please select manufacturer name");
            return false;
        }

        if (TextUtils.isEmpty(brand)) {
            Utility.alertDialog(this, "Please select Brand name");
            return false;
        }

        if (TextUtils.isEmpty(fuel)) {
            Utility.alertDialog(this, "Please select Fuel type");
            return false;
        }


        if (TextUtils.isEmpty(etVechicelNumber1.getText().toString()) || TextUtils.isEmpty(etVechicelNumber2.getText().toString()) ||
                TextUtils.isEmpty(etVechicelNumber3.getText().toString()) || TextUtils.isEmpty(etVechicelNumber4.getText().toString())) {
            Utility.alertDialog(this, "Please Enter Vechicle number");
            return false;
        }

        vechicleNumber = etVechicelNumber1.getText().toString() + " " + etVechicelNumber2.getText().toString() + " " + etVechicelNumber3.getText().toString() + " " + etVechicelNumber4.getText().toString();

       /* if (!vechicleNumber.matches("^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$")) {
            Utility.alertDialog(this, "Please Enter  valid Vechicle number");
            return false;
        }*/

        if (TextUtils.isEmpty(lastInsured)) {
            // Utility.alertDialog(this,"Please select Last Insured");
            // return false;
            lastInsured = "";
        }

        if (TextUtils.isEmpty(lastEmmissioned)) {
            // Utility.alertDialog(this,"Please select Last Emmissioned");
            // return false;
            lastEmmissioned = "";
        }

        return true;
    }

    private void getCompanyCarList() {

        if (companyCarList.size() > 0) {
            showComplaintList();
            return;
        }
        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseCompanyCarListData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(VechicleServiceActivity.this, VechicleServiceActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {
                    loading = status;
                }

                @Override
                public boolean getLoading() {
                    return loading;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<>();
            parameter.put("userid", Utility.getString(Constants.userId, this));


            connectWebService.stringPostRequest(Config.COMPANY_CAR_LIST, VechicleServiceActivity.this, parameter);


        } else {
            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }

    }


    private void getCompanyBikeList() {

        if (companyBikeList.size() > 0) {
            showComplaintList();
            return;
        }
        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseCompanyBikeListData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(VechicleServiceActivity.this, VechicleServiceActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {
                    loading = status;
                }

                @Override
                public boolean getLoading() {
                    return loading;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<>();
            parameter.put("userid", Utility.getString(Constants.userId, this));


            connectWebService.stringPostRequest(Config.COMPANY_BIKE_LIST, VechicleServiceActivity.this, parameter);


        } else {
            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }

    }


    private void getBrandList() {

        if (brandList.size() > 0 && !TextUtils.isEmpty(brand)) {
            showBrandList();
            return;
        }
        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseBrandListData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(VechicleServiceActivity.this, VechicleServiceActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {
                    loading = status;
                }

                @Override
                public boolean getLoading() {
                    return loading;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<>();
            parameter.put("userid", Utility.getString(Constants.userId, this));


            if (type.equals("Car"))
                connectWebService.stringPostRequest(Config.BRAND_CAR_LIST + company, VechicleServiceActivity.this, parameter);
            else
                connectWebService.stringPostRequest(Config.BRAND_BIKE_LIST + company, VechicleServiceActivity.this, parameter);


        } else {
            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }

    }


    private void pharseCompanyCarListData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(VechicleServiceActivity.this, message);
            } else {

                Gson gson = new Gson();
                CompanyList companyList = gson.fromJson(result, CompanyList.class);
                this.companyCarList = companyList.getData();

                showComplaintList();

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(VechicleServiceActivity.this, VechicleServiceActivity.this.getResources().getString(R.string.please_try));
        }


    }


    private void pharseCompanyBikeListData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(VechicleServiceActivity.this, message);
            } else {

                Gson gson = new Gson();
                CompanyList companyList = gson.fromJson(result, CompanyList.class);
                this.companyBikeList = companyList.getData();

                showComplaintList();

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(VechicleServiceActivity.this, VechicleServiceActivity.this.getResources().getString(R.string.please_try));
        }


    }

    private void pharseBrandListData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(VechicleServiceActivity.this, message);
            } else {

                Gson gson = new Gson();
                CompanyList brandList = gson.fromJson(result, CompanyList.class);
                this.brandList = brandList.getData();

                showBrandList();

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(VechicleServiceActivity.this, VechicleServiceActivity.this.getResources().getString(R.string.please_try));
        }


    }

    private Dialog addVechicle_dialog;

    private void displayAddDialog() {
        // TODO Auto-generated method stub
        if (addVechicle_dialog != null) {
            if (addVechicle_dialog.isShowing())
                addVechicle_dialog.cancel();
        }
        addVechicle_dialog = new Dialog(this);
        //zoomImage_dialog.getWindow().getAttributes().windowAnimations = android.R.style.Theme_Dialog_Translucent;
        addVechicle_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addVechicle_dialog.setContentView(R.layout.vechicle_add_dialog);
        intialize(addVechicle_dialog);
        addVechicle_dialog.setCancelable(true);
        addVechicle_dialog.getWindow().setGravity(Gravity.CENTER);
        final Window window = addVechicle_dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btCancel = (Button) addVechicle_dialog.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVechicle_dialog.cancel();
            }
        });
        if (!this.isFinishing()) {
            addVechicle_dialog.show();
        }
    }


    private void displayEditDialog(final VechicleDetails vechicleDetails) {
        // TODO Auto-generated method stub
        if (addVechicle_dialog != null) {
            if (addVechicle_dialog.isShowing())
                addVechicle_dialog.cancel();
        }
        addVechicle_dialog = new Dialog(this);
        //zoomImage_dialog.getWindow().getAttributes().windowAnimations = android.R.style.Theme_Dialog_Translucent;
        addVechicle_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addVechicle_dialog.setContentView(R.layout.vechicle_add_dialog);
        intialize(addVechicle_dialog);
        addVechicle_dialog.setCancelable(true);
        addVechicle_dialog.getWindow().setGravity(Gravity.CENTER);
        final Window window = addVechicle_dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btCancel = (Button) addVechicle_dialog.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVechicle_dialog.cancel();
            }
        });
        if (!this.isFinishing()) {
            addVechicle_dialog.show();
        }
        Button btSave = (Button) addVechicle_dialog.findViewById(R.id.btSave);
        btSave.setVisibility(View.VISIBLE);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditApi(vechicleDetails.getId());

            }
        });

        addVechicle_dialog.findViewById(R.id.btAdd).setVisibility(View.GONE);
        setVechicelEditValue(vechicleDetails);

    }

    private void setVechicelEditValue(VechicleDetails vechicleDetails) {

        viewCar.setVisibility(View.GONE);
        viewBike.setVisibility(View.GONE);
        tvCompany.setText(vechicleDetails.getBrandname());
        tvBrand.setText(vechicleDetails.getSubbrand_name());
        tvFuel.setText(vechicleDetails.getFueltype());

        etVechicelNumber1.setText(vechicleDetails.getVnumber().split(" ")[0]);
        etVechicelNumber2.setText(vechicleDetails.getVnumber().split(" ")[1]);
        etVechicelNumber3.setText(vechicleDetails.getVnumber().split(" ")[2]);
        if (vechicleDetails.getVnumber().split(" ").length > 3)
            etVechicelNumber4.setText(vechicleDetails.getVnumber().split(" ")[3]);

        if (!TextUtils.isEmpty(vechicleDetails.getLast_insured()) && !vechicleDetails.getLast_insured().equals("0000-00-00")) {
            tvLastInsured.setText(vechicleDetails.getLast_emission());
        }

        if (!TextUtils.isEmpty(vechicleDetails.getLast_emission()) && !vechicleDetails.getLast_emission().equals("0000-00-00")) {
            tvLastEmmissioned.setText(vechicleDetails.getLast_emission());
        }

        type=vechicleDetails.getVtype();
        company=vechicleDetails.getVbrand();
        brand=vechicleDetails.getSubbrand();
        vechicleNumber=vechicleDetails.getVnumber();
        fuel=vechicleDetails.getFueltype();
        lastInsured=vechicleDetails.getLast_insured();
        lastEmmissioned=vechicleDetails.getLast_emission();



    }

    private void intialize(Dialog addVechicle_dialog) {

        viewCar = addVechicle_dialog.findViewById(R.id.viewCar);
        viewBike = addVechicle_dialog.findViewById(R.id.viewBike);


        lyCar = (LinearLayout) addVechicle_dialog.findViewById(R.id.lyCar);
        lyCar.setOnClickListener(this);
        lyBike = (LinearLayout) addVechicle_dialog.findViewById(R.id.lyBike);
        lyBike.setOnClickListener(this);


        lyCompany = (RelativeLayout) addVechicle_dialog.findViewById(R.id.lyCompany);
        lyCompany.setOnClickListener(this);
        tvCompany = (TextView) addVechicle_dialog.findViewById(R.id.tvCompany);

        lyBrand = (RelativeLayout) addVechicle_dialog.findViewById(R.id.lyBrand);
        lyBrand.setOnClickListener(this);
        tvBrand = (TextView) addVechicle_dialog.findViewById(R.id.tvBrand);

        lyFuel = (RelativeLayout) addVechicle_dialog.findViewById(R.id.lyFuel);
        lyFuel.setOnClickListener(this);
        tvFuel = (TextView) addVechicle_dialog.findViewById(R.id.tvFuel);
        etVechicelNumber1 = (EditText) addVechicle_dialog.findViewById(R.id.etVechicelNumber1);
        etVechicelNumber2 = (EditText) addVechicle_dialog.findViewById(R.id.etVechicelNumber2);
        etVechicelNumber3 = (EditText) addVechicle_dialog.findViewById(R.id.etVechicelNumber3);
        etVechicelNumber4 = (EditText) addVechicle_dialog.findViewById(R.id.etVechicelNumber4);

        lyLastInsured = (RelativeLayout) addVechicle_dialog.findViewById(R.id.lyLastInsured);
        lyLastInsured.setOnClickListener(this);
        tvLastInsured = (TextView) addVechicle_dialog.findViewById(R.id.tvLastInsured);
        lyLastEmmissioned = (RelativeLayout) addVechicle_dialog.findViewById(R.id.lyLastEmmissioned);
        lyLastEmmissioned.setOnClickListener(this);
        tvLastEmmissioned = (TextView) addVechicle_dialog.findViewById(R.id.tvLastEmmissioned);
        btAdd = (Button) addVechicle_dialog.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(this);

    }

    private void apiCall() {
        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseVechicleListData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(VechicleServiceActivity.this, VechicleServiceActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {
                    loading = status;
                }

                @Override
                public boolean getLoading() {
                    return loading;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<>();
            parameter.put("userid", Utility.getString(Constants.userId, this));


            connectWebService.stringPostRequest(Config.VECHICLE_LIST, VechicleServiceActivity.this, parameter);


        } else {

           /* VechicleList vechicleList = VechicleList.DeSerialize(this);

            if (vechicleList != null) {
                this.vechicleList.addAll(vechicleList.getBike());
                this.vechicleList.addAll(vechicleList.getCar());
                if(!onClickList)
                    vechicleListAdapter.setVechicleService(false);
                vechicleListAdapter.notifyDataSetChanged();

            } else*/
            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }
    }

    private void pharseVechicleListData(String result) {
        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");
            jsonObject = jsonObject.getJSONObject("data");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(VechicleServiceActivity.this, message);
            } else {

                Gson gson = new Gson();
                VechicleList vechicleList = gson.fromJson(jsonObject.toString(), VechicleList.class);
                this.vechicleList.addAll(vechicleList.getBike());
                this.vechicleList.addAll(vechicleList.getCar());

                Collections.reverse(this.vechicleList);
                vechicleList.Serialize(this);
                if (!onClickList)
                    vechicleListAdapter.setVechicleService(false);
                vechicleListAdapter.notifyDataSetChanged();
                //if (this.vechicleList.size() > 0)
                    lyRefersh.setVisibility(View.GONE);

                if (this.vechicleList.size() == 0)
                  Utility.alertDialog(VechicleServiceActivity.this, VechicleServiceActivity.this.getResources().getString(R.string.add_vech_mess));

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(VechicleServiceActivity.this, VechicleServiceActivity.this.getResources().getString(R.string.please_try));
        }
    }


   /* Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {

            LoginResponse loginResponse = new LoginResponse();
            loginResponse = loginResponse.DeSerialize(VechicleServiceActivity.this);
            if (loginResponse != null && loginResponse.getVehicle()!=null) {
                vechicleList.addAll(loginResponse.getVehicle());

                VechicleServiceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vechicleListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    });*/

    private void intialize() {
        lyRefersh = (LinearLayout) this.findViewById(R.id.lyRefersh);
        btAddVechicle = (Button) this.findViewById(R.id.btAddVechicle);
        btAddVechicle.setOnClickListener(this);
        vechicle_list = (RecyclerView) this.findViewById(R.id.vechicle_list);
        vechicleListAdapter = new VechicleListAdapter(vechicleList);
        vechicleListAdapter.setOnVechicleClickListner(this);

        vechicle_list.setLayoutManager(new LinearLayoutManager(this));
        vechicle_list.setAdapter(vechicleListAdapter);
        lyRefersh.setOnClickListener(this);

    }


    private int selectedCompanyList = -1;

    private void showComplaintList() {

        final ArrayList<CompnayInfo> compnayList;
        if (type.equals("Car"))
            compnayList = companyCarList;
        else
            compnayList = companyBikeList;

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);

        final String[] list = new String[compnayList.size()];
        for (int i = 0; i < compnayList.size(); i++) {
            list[i] = compnayList.get(i).getBrand();
        }

        ad.setSingleChoiceItems(list, selectedCompanyList, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                selectedCompanyList = arg1;
                company = compnayList.get(selectedCompanyList).getId();
                tvCompany.setText(list[selectedCompanyList]);
                arg0.dismiss();

            }
        });
        ad.show();
    }


    private int selectedBrandList = -1;

    private void showBrandList() {

        /*final ArrayList<CompnayInfo> compnayList;
        if (type.equals("Car"))
            brandList = companyCarList;
        else
            brandList = companyBikeList;
*/

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);

        final String[] list = new String[brandList.size()];
        for (int i = 0; i < brandList.size(); i++) {
            list[i] = brandList.get(i).getBrand();
        }

        ad.setSingleChoiceItems(list, selectedBrandList, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                selectedBrandList = arg1;
                brand = brandList.get(selectedBrandList).getId();
                tvBrand.setText(list[selectedBrandList]);
                arg0.dismiss();

            }
        });
        ad.show();
    }


    private int selectedFuel = -1;
    private final String[] fuelArray = new String[]{"Petrol", "Diesel"};

    private void showFuelList() {

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);


        ad.setSingleChoiceItems(fuelArray, selectedFuel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                selectedFuel = arg1;
                fuel = fuelArray[selectedFuel];
                tvFuel.setText(fuel);
                arg0.dismiss();

            }
        });
        ad.show();
    }

    private void showLastInsured() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                String year1 = String.valueOf(selectedYear);
                String month1 = String.valueOf(selectedMonth + 1);
                String day1 = String.valueOf(selectedDay);
                lastInsured = year1 + "-" + month1 + "-" + day1;
                tvLastInsured.setText(lastInsured);

            }
        };

        DatePickerDialog datePicker = new DatePickerDialog(this,
                datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.show();


    }


    private void showLastEmmissioned() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
                String year1 = String.valueOf(selectedYear);
                String month1 = String.valueOf(selectedMonth + 1);
                String day1 = String.valueOf(selectedDay);
                lastEmmissioned = year1 + "-" + month1 + "-" + day1;
                tvLastEmmissioned.setText(lastEmmissioned);

            }
        };

        DatePickerDialog datePicker = new DatePickerDialog(this,
                datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.show();
    }

    @Override
    public void OnVechicleSelected(VechicleDetails vechicleDetails) {

        if (onClickList) {
            Intent intent = new Intent(this, ServiceCenterActivity.class);
            intent.putExtra("vechicle", vechicleDetails);
            this.startActivity(intent);
        }

    }

    @Override
    public void OnVechicleDelete(final VechicleDetails vechicleDetails) {
        Utility.showConfirmDialog(new ConfirmInterface() {
            @Override
            public void onConfirm() {
                callDeleteApi(vechicleDetails);

            }
        }, "Do you want to delete your vehicle?", this);


    }

    @Override
    public void OnVechicleEdit(final VechicleDetails vechicleDetails) {
        Utility.showConfirmDialog(new ConfirmInterface() {
            @Override
            public void onConfirm() {
                clearData();
                displayEditDialog(vechicleDetails);

            }
        }, "Do you want to edit your vehicle?", this);

    }


    private void callDeleteApi(VechicleDetails vechicleDetails) {

        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    String SUCESSS = "1", FAILURE = "0";
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("success");
                        String message = jsonObject.getString("message");

                        if (status.trim().equals(FAILURE)) {
                            Utility.alertDialog(VechicleServiceActivity.this,message );
                        } else {

                            vechicleList.clear();
                            vechicleListAdapter.notifyDataSetChanged();
                            apiCall();
                            Utility.alertDialog(VechicleServiceActivity.this, "Vehicle Deleted successfully");


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(VechicleServiceActivity.this, VechicleServiceActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {
                    loading = status;
                }

                @Override
                public boolean getLoading() {
                    return loading;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<>();
            parameter.put("userid", Utility.getString(Constants.userId, this));
            parameter.put("vehicle_id", vechicleDetails.getId());


            //parameter.put("vbrand",company);


            connectWebService.stringPostRequest(Config.DELETE_VECHICLE, VechicleServiceActivity.this, parameter);


        } else {
            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }


    }
}
