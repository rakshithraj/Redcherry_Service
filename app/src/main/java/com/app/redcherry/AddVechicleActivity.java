package com.app.redcherry;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Interface.ConfirmInterface;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Model.CompanyList;
import com.app.redcherry.Model.CompnayInfo;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.webservice.ConnectWebService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class AddVechicleActivity extends BaseActivity implements View.OnClickListener{

    private boolean loading = false;
    private View viewBike;
    private View viewCar;
    private TextView tvCompany;
    private TextView tvBrand;
    private TextView tvFuel;
    private TextView tvLastInsured;
    private TextView tvLastEmmissioned;
    private String type="Car";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vechicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intialize(this);
        viewCar.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utility.hideKeyboard(viewCar);
            }
        },100);
    }


    private void intialize(Activity addVechicle_dialog) {
        viewCar = addVechicle_dialog.findViewById(R.id.viewCar);
        viewBike = addVechicle_dialog.findViewById(R.id.viewBike);


        LinearLayout lyCar = (LinearLayout) addVechicle_dialog.findViewById(R.id.lyCar);
        lyCar.setOnClickListener(this);
        LinearLayout lyBike = (LinearLayout) addVechicle_dialog.findViewById(R.id.lyBike);
        lyBike.setOnClickListener(this);


        RelativeLayout lyCompany = (RelativeLayout) addVechicle_dialog.findViewById(R.id.lyCompany);
        lyCompany.setOnClickListener(this);
        tvCompany = (TextView) addVechicle_dialog.findViewById(R.id.tvCompany);

        RelativeLayout lyBrand = (RelativeLayout) addVechicle_dialog.findViewById(R.id.lyBrand);
        lyBrand.setOnClickListener(this);
        tvBrand = (TextView) addVechicle_dialog.findViewById(R.id.tvBrand);

        RelativeLayout lyFuel = (RelativeLayout) addVechicle_dialog.findViewById(R.id.lyFuel);
        lyFuel.setOnClickListener(this);
        tvFuel = (TextView) addVechicle_dialog.findViewById(R.id.tvFuel);
        etVechicelNumber1 = (EditText) addVechicle_dialog.findViewById(R.id.etVechicelNumber1);
        etVechicelNumber2 = (EditText) addVechicle_dialog.findViewById(R.id.etVechicelNumber2);
        etVechicelNumber3 = (EditText) addVechicle_dialog.findViewById(R.id.etVechicelNumber3);
        etVechicelNumber4 = (EditText) addVechicle_dialog.findViewById(R.id.etVechicelNumber4);

        RelativeLayout lyLastInsured = (RelativeLayout) addVechicle_dialog.findViewById(R.id.lyLastInsured);
        lyLastInsured.setOnClickListener(this);
        tvLastInsured = (TextView) addVechicle_dialog.findViewById(R.id.tvLastInsured);
        RelativeLayout lyLastEmmissioned = (RelativeLayout) addVechicle_dialog.findViewById(R.id.lyLastEmmissioned);
        lyLastEmmissioned.setOnClickListener(this);
        tvLastEmmissioned = (TextView) addVechicle_dialog.findViewById(R.id.tvLastEmmissioned);
        Button btAdd = (Button) addVechicle_dialog.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.lyBike:
                viewCar.setBackgroundColor(Color.parseColor("#F2F2F2"));
                viewBike.setBackgroundColor(Color.parseColor("#DD1C13"));
                type = "Bike";

                selectedCompanyList = -1;
                company = "";
                tvCompany.setText("Select Manufacturer");

                brand="";
                selectedBrandList=-1;
                tvBrand.setText("Enter Brand");

                break;
            case R.id.lyCar:
                viewBike.setBackgroundColor(Color.parseColor("#F2F2F2"));
                viewCar.setBackgroundColor(Color.parseColor("#DD1C13"));
                type = "Car";

                selectedCompanyList = -1;
                company = "";
                tvCompany.setText("Select Manufacturer");

                brand="";
                selectedBrandList=-1;
                tvBrand.setText("Enter Brand");

                break;
            case R.id.lyCompany:
                if(type.equals("Car"))
                    getCompanyCarList();
                else
                    getCompanyBikeList();

                brand="";
                selectedBrandList=-1;
                tvBrand.setText("Enter Brand");
                break;
            case R.id.lyBrand:
                if(TextUtils.isEmpty(company)) {
                    Utility.alertDialog(AddVechicleActivity.this, "please select manufacturer");
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





    private void callAddApi() {
        if(!isDataValid()){
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
                            Utility.alertDialog(AddVechicleActivity.this, message);
                        } else {


                            etVechicelNumber1.setText("");
                            etVechicelNumber2.setText("");
                            etVechicelNumber3.setText("");
                            etVechicelNumber4.setText("");
                            type="";
                            company="";
                            brand="";
                            fuel="";
                            vechicleNumber="";
                            lastInsured="";
                            lastEmmissioned="";


                            showConfirmDialog(new ConfirmInterface() {
                                @Override
                                public void onConfirm() {
                                    current_page = PAGE.ADD_VECHICLE;
                                    if (android.os.Build.VERSION.SDK_INT >= 11)
                                    {
                                        AddVechicleActivity.this.recreate();
                                    }
                                    else
                                    {
                                        AddVechicleActivity.this.startActivity(getIntent());
                                        AddVechicleActivity.this.finish();
                                    }
                                     }
                            },message,AddVechicleActivity.this);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(AddVechicleActivity.this, AddVechicleActivity.this.getResources().getString(R.string.please_try));

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
            parameter.put("vtype",type);
            parameter.put("vbrand",company);
            parameter.put("subbrand",brand);
            parameter.put("fueltype",fuel);
            parameter.put("vnumber",vechicleNumber);
            parameter.put("last_insured",lastInsured);
            parameter.put("last_emission",lastEmmissioned);



            //parameter.put("vbrand",company);


            connectWebService.stringPostRequest(Config.ADD_VECHICLE, AddVechicleActivity.this, parameter);


        } else {
            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }




    }


    public static void showConfirmDialog(final ConfirmInterface confirmInterface, String message, Activity activity) {

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                confirmInterface.onConfirm();

            }
        });

//        alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override

//            public void onClick(DialogInterface dialog, int arg1) {
//                dialog.dismiss();
//            }
//        });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }


    private boolean isDataValid() {

        if(TextUtils.isEmpty(company)){
            Utility.alertDialog(this,"Please select manufacturer name");
            return false;
        }

        if(TextUtils.isEmpty(brand)){
            Utility.alertDialog(this,"Please select Brand name");
            return false;
        }

        if(TextUtils.isEmpty(fuel)){
            Utility.alertDialog(this,"Please select Fuel type");
            return false;
        }


        if(TextUtils.isEmpty(etVechicelNumber1.getText().toString()) || TextUtils.isEmpty(etVechicelNumber2.getText().toString()) || TextUtils.isEmpty(etVechicelNumber3.getText().toString()) || TextUtils.isEmpty(etVechicelNumber4.getText().toString())){
            Utility.alertDialog(this,"Please Enter Vechicle number");
            return false;
        }

        vechicleNumber=etVechicelNumber1.getText().toString()+" "+etVechicelNumber2.getText().toString()+" "+etVechicelNumber3.getText().toString()+" "+etVechicelNumber4.getText().toString();

       /* if(!vechicleNumber.matches("^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$")){
            Utility.alertDialog(this,"Please Enter  valid Vechicle number");
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
        return  true;
    }

    private void getCompanyCarList() {

        if(companyCarList.size()>0) {
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
                    Utility.alertDialog(AddVechicleActivity.this, AddVechicleActivity.this.getResources().getString(R.string.please_try));

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


            connectWebService.stringPostRequest(Config.COMPANY_CAR_LIST, AddVechicleActivity.this, parameter);


        } else {
            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }

    }


    private void getCompanyBikeList() {

        if(companyBikeList.size()>0) {
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
                    Utility.alertDialog(AddVechicleActivity.this, AddVechicleActivity.this.getResources().getString(R.string.please_try));

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


            connectWebService.stringPostRequest(Config.COMPANY_BIKE_LIST, AddVechicleActivity.this, parameter);


        } else {
            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }

    }


    private void getBrandList() {

        if(brandList.size()>0 && !TextUtils.isEmpty(brand)) {
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
                    Utility.alertDialog(AddVechicleActivity.this, AddVechicleActivity.this.getResources().getString(R.string.please_try));

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


            if(type.equals("Car"))
                connectWebService.stringPostRequest(Config.BRAND_CAR_LIST +company, AddVechicleActivity.this, parameter);
            else
                connectWebService.stringPostRequest(Config.BRAND_BIKE_LIST +company, AddVechicleActivity.this, parameter);



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
                Utility.alertDialog(AddVechicleActivity.this, message);
            } else {

                Gson gson = new Gson();
                CompanyList companyList = gson.fromJson(result, CompanyList.class);
                this.companyCarList =companyList.getData();

                showComplaintList();

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(AddVechicleActivity.this, AddVechicleActivity.this.getResources().getString(R.string.please_try));
        }


    }


    private void pharseCompanyBikeListData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(AddVechicleActivity.this, message);
            } else {

                Gson gson = new Gson();
                CompanyList companyList = gson.fromJson(result, CompanyList.class);
                this.companyBikeList =companyList.getData();

                showComplaintList();

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(AddVechicleActivity.this, AddVechicleActivity.this.getResources().getString(R.string.please_try));
        }


    }

    private void pharseBrandListData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(AddVechicleActivity.this, message);
            } else {

                Gson gson = new Gson();
                CompanyList brandList = gson.fromJson(result, CompanyList.class);
                this.brandList=brandList.getData();

                showBrandList();

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(AddVechicleActivity.this, AddVechicleActivity.this.getResources().getString(R.string.please_try));
        }


    }


    private int selectedCompanyList = -1;

    private void showComplaintList() {

        final ArrayList<CompnayInfo> compnayList;
        if(type.equals("Car"))
            compnayList = companyCarList;
        else
            compnayList = companyBikeList;

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);

        final String[] list = new String[compnayList.size()];
        for(int i = 0; i< compnayList.size(); i++){
            list[i]= compnayList.get(i).getBrand();
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
        if(!this.isFinishing())
        {
            ad.show();
        }

    }



    private int selectedBrandList = -1;

    private void showBrandList() {

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);

        final String[] list = new String[brandList.size()];
        for(int i=0;i<brandList.size();i++){
            list[i]=brandList.get(i).getBrand();
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
                lastInsured = day1 + "-" + month1 + "-" + year1;
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
                lastEmmissioned=day1 + "-" + month1 + "-" + year1;
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

}
