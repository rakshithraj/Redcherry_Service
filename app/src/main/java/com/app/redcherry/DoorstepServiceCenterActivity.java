package com.app.redcherry;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.app.redcherry.Adapter.CarWashServiceCenterAdapter;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Interface.LocationInterface;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Interface.ServiceCenterInterface;
import com.app.redcherry.Model.BookingDetails;
import com.app.redcherry.Model.CarWashServiceCenterInfo;
import com.app.redcherry.Model.CarWashServiceCenterList;
import com.app.redcherry.Model.LoginResponse;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.Ulility.DeviceLocation;
import com.app.redcherry.Ulility.PlacesAutoCompleteAdapter;
import com.app.redcherry.Ulility.StaticVariables;
import com.app.redcherry.Ulility.TopExceptionHandler;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.webservice.ConnectWebService;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DoorstepServiceCenterActivity extends AppCompatActivity implements View.OnClickListener, ServiceCenterInterface {

    private ImageView imgClear;
    private ImageView imglocation;
    private AutoCompleteTextView etLocation;
    private final ArrayList<CarWashServiceCenterInfo> serviceCenterList = new ArrayList<>();
    private CarWashServiceCenterAdapter serviceCenterAdapter;
    private String lat;
    private String longt;
    String locationList[] = {"Mangaluru"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        }
        setContentView(R.layout.activity_doorstep_service_center);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intialize();

        imgClear.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utility.hideKeyboard(imgClear);
            }
        },300);
        //gpsBasedSearch();
        allServiceCenterApi();
    }


    private void intialize() {

        imgClear = (ImageView) findViewById(R.id.imgClear);
        imgClear.setOnClickListener(this);
        imglocation = (ImageView) findViewById(R.id.imglocation);
        imglocation.setOnClickListener(this);
        RecyclerView serviceCenter = (RecyclerView) this.findViewById(R.id.serviceCenterList);

        serviceCenterAdapter = new CarWashServiceCenterAdapter(serviceCenterList);
        serviceCenterAdapter.setOnVechicleClickListner(this);
        serviceCenter.setLayoutManager(new LinearLayoutManager(this));
        serviceCenter.setAdapter(serviceCenterAdapter);


        etLocation = (AutoCompleteTextView) findViewById(R.id.etLocation);


        etLocation.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));

        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imgSerach.setVisibility(View.GONE);
            }
        });

        etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                imgClear.setVisibility(View.VISIBLE);

            }
        });

        etLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search(v);
                return true;
            }
        });

        findViewById(R.id.idLySelectLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLocationList();
            }
        });

    }

    int mSelectedLocationIndex=0;
    private void showLocationList() {



        final AlertDialog.Builder ad = new AlertDialog.Builder(this);


        ad.setSingleChoiceItems(locationList, mSelectedLocationIndex, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();

                ((TextView)findViewById(R.id.tvSelectedLocation)).setText(locationList[arg1]);
                //if(mSelectedLocationIndex==arg1)
                //    return;

                mSelectedLocationIndex = arg1;

                allServiceCenterApi();

            }
        });
        ad.show();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imglocation:
                gpsBasedSearch();


                break;
            case R.id.imgClear:
                etLocation.setText("");
                break;
        }
    }

    private void gpsBasedSearch() {
        if (!AppGlobal.isNetwork(this)) {
            Utility.alertDialog(this, "Check Internet Connection");
            return;
        }

        if (Build.VERSION.SDK_INT < 23)
            getMyLocation();
        else
            chechForPermissions();

    }

    private DeviceLocation deviceLocation;

    private static final int REQUEST_GPS = 10;

    private boolean locationCallback;

    private void getMyLocation() {

        locationCallback = false;
        lat = null;
        longt = null;
        if (deviceLocation == null)
            deviceLocation = new DeviceLocation(this);

        if (!deviceLocation.isRunning())
            deviceLocation.start();

        if (Utility.checkGpsEnabled(this)) {


            final ProgressDialog dialog = new ProgressDialog(DoorstepServiceCenterActivity.this);
            dialog.setMessage(
                    (Html.fromHtml("<font color='red'>Getting Current location..." +
                            "</font>")));
            dialog.setCancelable(false);
            dialog.show();

            deviceLocation.setOnLocationFoundListner(new LocationInterface() {


                @Override
                public void onLocationFound(final Location location) {

                    DoorstepServiceCenterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            deviceLocation.setOnLocationFoundListner(null);
                            locationCallback = true;
                            lat = location.getLatitude() + "";
                            longt = location.getLongitude() + "";

                            dialog.dismiss();
                            allServiceCenterApi();

                        }
                    });

                }
            });


            imglocation.postDelayed(new Runnable() {
                @Override
                public void run() {
                    deviceLocation.setOnLocationFoundListner(null);
                    if (locationCallback) {
                        return;
                    }
                    Location latLag = null;
                    int count = 5;

                    if (TextUtils.isEmpty(lat))
                        while (latLag == null && count != 0) {
                            latLag = deviceLocation.showCurrentLocation();
                            count--;
                        }
                    deviceLocation.stop();


                    dialog.dismiss();

                    if (latLag != null) {
                        lat = latLag.getLatitude() + "";
                        longt = latLag.getLongitude() + "";
                        allServiceCenterApi();
                    } else
                        Utility.alertDialog(DoorstepServiceCenterActivity.this, "Location not found");

                }
            }, 15000);

        } else {
            deviceLocation.stop();
            final AlertDialog.Builder ad = new AlertDialog.Builder(this);
            //ad.setTitle("Set Location");
            ad.setMessage("Enable gps to get location");
            ad.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();

                }

            });
            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_GPS);

                }


            });
            ad.show();
        }
    }


    private static final int PERMISSIONS_REQUEST_CODE = 20;
    private final ArrayList<String> reuestList = new ArrayList<>();

    private void chechForPermissions() {

        reuestList.clear();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            reuestList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            reuestList.add(Manifest.permission.ACCESS_FINE_LOCATION);


            ActivityCompat.requestPermissions(this, reuestList.toArray(new String[reuestList.size()]),
                    PERMISSIONS_REQUEST_CODE);
        } else {
            getMyLocation();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {


        if (requestCode == PERMISSIONS_REQUEST_CODE
                ) {
            int i;
            for (i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    break;

                }
            }
            if (i == grantResults.length)
                getMyLocation();
        }


    }


    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        //  if(resultCode == RESULT_OK) {
        switch (requestCode) {

            case REQUEST_GPS:
                if (Utility.checkGpsEnabled(this))
                    getMyLocation();


                break;

        }
        // }
    }

    public void search(View v) {

        if (!AppGlobal.isNetwork(this))
            return;


        String query = etLocation.getText().toString();
        if (TextUtils.isEmpty(query))
            return;
        String searchQuery = "";
        if (!TextUtils.isEmpty(query) && !query.equals(searchQuery)) {

            new SearchLocationAsyncTask(query).execute();
        }
    }

    @Override
    public void onServiceCenterClicked(Object object) {
        CarWashServiceCenterInfo serviceCenterInfo =(CarWashServiceCenterInfo)object;
        setBookingDeatils(serviceCenterInfo);

        Intent intent = new Intent(this, CarWashServiceCenterDetailActivity.class);
        intent.putExtra("serviceCenterInfo", serviceCenterInfo);
        intent.putExtra("price", serviceCenterInfo.getPrice());
        this.startActivity(intent);

    }

    private void setBookingDeatils(CarWashServiceCenterInfo serviceCenterInfo) {
        StaticVariables.bookingDetails = new BookingDetails();
         StaticVariables.bookingDetails.setService_charge(serviceCenterInfo.getPrice());
        StaticVariables.bookingDetails.setService_center_id(serviceCenterInfo.getId());
        StaticVariables.bookingDetails.setBranchname(serviceCenterInfo.getBranchname());
        StaticVariables.bookingDetails.setUserid(Utility.getString(Constants.userId, this));
        StaticVariables.bookingDetails.setSlot(serviceCenterInfo.getSlots());
        LoginResponse loginResponse = new LoginResponse();
        loginResponse = loginResponse.DeSerialize(this);
        StaticVariables.bookingDetails.setFname(loginResponse.getData().get(0).getFname());
    }


    public class SearchLocationAsyncTask extends AsyncTask<Void, Void, LatLng> {
        String query;

        SearchLocationAsyncTask(String query) {
            this.query = query;
        }

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = new ProgressDialog(DoorstepServiceCenterActivity.this);
            dialog.setMessage(
                    (Html.fromHtml("<font color='red'>Getting search location..." +
                            "</font>")));
            dialog.setCancelable(false);
            dialog.show();
            query = etLocation.getText().toString();
        }


        @Override

        protected LatLng doInBackground(Void... params) {
            return Utility.getLocationFromAddress(DoorstepServiceCenterActivity.this, query);
        }

        @Override
        protected void onPostExecute(LatLng result) {

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            // getSearchLocation(query);

            if (result == null) {
                Utility.alertDialog(DoorstepServiceCenterActivity.this, "Please check search location and try  again");
                return;
            }
            lat = result.latitude + "";
            longt = result.longitude + "";
            allServiceCenterApi();

        }
    }

    private boolean loading = false;

    private void allServiceCenterApi() {


        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseServiceCenterListData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(DoorstepServiceCenterActivity.this, DoorstepServiceCenterActivity.this.getResources().getString(R.string.please_try));

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

            //parameter.put("latitude", lat);
           // parameter.put("longitude", longt);



           //parameter.put("latitude", "13.3865015");
           // parameter.put("longitude", "74.9052706");
            parameter.put("location", locationList[mSelectedLocationIndex].toLowerCase());


            connectWebService.stringPostRequest(Config.DOOR_STEP_LIST, DoorstepServiceCenterActivity.this, parameter);


        } else {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }

    }

    private void pharseServiceCenterListData(String result) {


        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(DoorstepServiceCenterActivity.this, message);
            } else {

                Gson gson = new Gson();
                CarWashServiceCenterList serviceCenterList = gson.fromJson(result, CarWashServiceCenterList.class);
                this.serviceCenterList.clear();
                this.serviceCenterList.addAll(serviceCenterList.getData());
                serviceCenterAdapter.notifyDataSetChanged();


            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(DoorstepServiceCenterActivity.this, DoorstepServiceCenterActivity.this.getResources().getString(R.string.please_try));
        }
    }


}
