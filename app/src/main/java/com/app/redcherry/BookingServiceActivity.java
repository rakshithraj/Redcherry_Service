package com.app.redcherry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Interface.ConfirmInterface;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Interface.ServiceTypeInterface;
import com.app.redcherry.Model.BookingconfirmInfo;
import com.app.redcherry.Model.ServiceType;
import com.app.redcherry.Model.ServiceTypeInfo;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.StaticVariables;
import com.app.redcherry.Ulility.TopExceptionHandler;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.webservice.ConnectWebService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingServiceActivity extends AppCompatActivity implements View.OnClickListener, ServiceTypeInterface {


    private TextView tvBranchName;
    private TextView tvVechicleNumber;
    private TextView tvPrice;
    private TextView tvServiceCenterName;
    private TextView tvDateTime;
    private TextView tvSelectedService;
    private TextView tvSlotNumber;
    private TextView tvServiceCharge;
    private EditText etDescription;
    private Switch switchPickup;
    // btCancelServicelist;

    //RecyclerView servicetype_list;
    //ServiceTypeAdapter serviceTypeAdapter;
    //LinearLayout lyBookDetails;
    /// RelativeLayout lyServiceList;
    private String price;
    private final ArrayList<ServiceTypeInfo> serviceTypeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        }        setContentView(R.layout.activity_booking_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        price = this.getIntent().getStringExtra("price");
        intialize();
        setData();


    }

    private void setData() {

        tvBranchName.setText(StaticVariables.bookingDetails.getVechicleName());
        tvVechicleNumber.setText(StaticVariables.bookingDetails.getVechicle_number());
        tvPrice.setText(price);
        tvServiceCenterName.setText(StaticVariables.bookingDetails.getBranchname());
        tvDateTime.setText(StaticVariables.bookingDetails.getBooking_date() + " | " + StaticVariables.bookingDetails.getBooking_time());
        tvSlotNumber.setText(StaticVariables.bookingDetails.getSlot());
        tvServiceCharge.setText("+ Booking charge " + StaticVariables.bookingDetails.getService_charge());
        if(StaticVariables.bookingDetails.getPickup().equals("false")){
            findViewById(R.id.lyPickup).setVisibility(View.INVISIBLE);
        }
    }

    private void intialize() {
        tvBranchName = (TextView) this.findViewById(R.id.tvBranchName);
        tvVechicleNumber = (TextView) this.findViewById(R.id.tvVechicleNumber);
        tvPrice = (TextView) this.findViewById(R.id.tvPrice);
        tvServiceCenterName = (TextView) this.findViewById(R.id.tvServiceCenterName);
        tvDateTime = (TextView) this.findViewById(R.id.tvDateTime);
        tvSelectedService = (TextView) this.findViewById(R.id.tvSelectedService);
        tvSlotNumber = (TextView) this.findViewById(R.id.tvSlotNumber);
        tvServiceCharge = (TextView) this.findViewById(R.id.tvServiceCharge);

        etDescription = (EditText) this.findViewById(R.id.etDescription);

        Button btBookConfirm = (Button) this.findViewById(R.id.btBookConfirm);
        Button btCancelDialog = (Button) this.findViewById(R.id.btCancelDialog);
        // btCancelServicelist = (Button) this.findViewById(R.id.btCancelServicelist);

        RelativeLayout lySelectService = (RelativeLayout) this.findViewById(R.id.lySelectService);
        //lyServiceList= (RelativeLayout) this.findViewById(R.id.lyServiceList);
        //lyBookDetails= (LinearLayout) this.findViewById(R.id.lyBookDetails);
        switchPickup = (Switch) this.findViewById(R.id.switchPickup);


        btBookConfirm.setOnClickListener(this);
        btCancelDialog.setOnClickListener(this);
        lySelectService.setOnClickListener(this);
        //btCancelServicelist.setOnClickListener(this);

        //servicetype_list = (RecyclerView) this.findViewById(R.id.servicetype_list);
        //serviceTypeAdapter = new ServiceTypeAdapter(serviceTypeList);
        //serviceTypeAdapter.setOnServiceTypeListener(this);
        //servicetype_list.setLayoutManager(new LinearLayoutManager(this));
        //servicetype_list.setAdapter(serviceTypeAdapter);


        switchPickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BookingServiceActivity.this);
                    alertDialogBuilder.setMessage("Vehicle Pickup may involve Additional charges. We" +
                            " will intimate the cost if any, immediately after confirming your " +
                            "Booking");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();


                        }
                    });

                    alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.dismiss();
                            switchPickup.setChecked(false);

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();



                }
            }
        });
    }

    /* @Override
     public void onBackPressed() {

         if (lyServiceList.getVisibility() == View.VISIBLE) {
             lyServiceList.setVisibility(View.INVISIBLE);
             lyBookDetails.setVisibility(View.VISIBLE);
         } else
             finish();
     }
 */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lySelectService:
                if (serviceTypeList.size() == 0)
                    getServiceType();
                else {
                    //lyServiceList.setVisibility(View.VISIBLE);
                    //lyBookDetails.setVisibility(View.INVISIBLE);
                    showServiceList();

                }
                break;
            case R.id.btCancelServicelist:
                //lyServiceList.setVisibility(View.INVISIBLE);
                // lyBookDetails.setVisibility(View.VISIBLE);

                break;
            case R.id.btCancelDialog:
                finish();
                break;
            case R.id.btBookConfirm:
                if (TextUtils.isEmpty(StaticVariables.bookingDetails.getService_type())) {
                    Utility.alertDialog(this, "Please select the Service type");
                    return;
                }
                if (!TextUtils.isEmpty(etDescription.getText().toString())) {
                    StaticVariables.bookingDetails.setService_type_desc(etDescription.getText().toString());
                } else
                    StaticVariables.bookingDetails.setService_type_desc("");


                if (switchPickup.isChecked())
                    StaticVariables.bookingDetails.setPickup("Yes");
                else
                    StaticVariables.bookingDetails.setPickup("No");


                Utility.showConfirmDialog(new ConfirmInterface() {
                    @Override
                    public void onConfirm() {
                        confirmBooking();
                    }
                },"Do you really want to confirm your booking?",this);

                break;

        }

    }

    private void confirmBooking() {
        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseServiceConfirmData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(BookingServiceActivity.this, BookingServiceActivity.this.getResources().getString(R.string.please_try));

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
            parameter.put("userid", StaticVariables.bookingDetails.getUserid());
            parameter.put("fname", StaticVariables.bookingDetails.getFname());
            parameter.put("vehicle_type", StaticVariables.bookingDetails.getVechicle_type());
            parameter.put("vehicle_number", StaticVariables.bookingDetails.getVechicle_number());
            parameter.put("vechicle_type", StaticVariables.bookingDetails.getVechicle_type());
            parameter.put("service_center_id", StaticVariables.bookingDetails.getService_center_id());
            parameter.put("branchname", StaticVariables.bookingDetails.getBranchname());
            parameter.put("service_type_id", StaticVariables.bookingDetails.getService_type_id());
            parameter.put("service_center_id", StaticVariables.bookingDetails.getService_center_id());
            parameter.put("service_type", StaticVariables.bookingDetails.getService_type());
            parameter.put("service_type_desc", StaticVariables.bookingDetails.getService_type_desc());
            Float tempPrice =Float.parseFloat(StaticVariables.bookingDetails.getService_charge());
            tempPrice=tempPrice+Float.parseFloat(price);
            parameter.put("service_charge", tempPrice+"");
            parameter.put("booking_date", StaticVariables.bookingDetails.getBooking_date());
            parameter.put("booking_time", StaticVariables.bookingDetails.getBooking_time());
            parameter.put("pickup", StaticVariables.bookingDetails.getPickup());
            parameter.put("vehicle_id", StaticVariables.bookingDetails.getVechicle_id());
            parameter.put("booking_completed_date", StaticVariables.bookingDetails.getBooking_date());


            connectWebService.stringPostRequest(Config.Confirm_BOOK, BookingServiceActivity.this, parameter);


        } else {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }
    }


    private boolean loading = false;

    private void getServiceType() {

        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseServiceTypeListData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(BookingServiceActivity.this, BookingServiceActivity.this.getResources().getString(R.string.please_try));

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


            connectWebService.stringPostRequest(Config.SERVICE_TYPE_LIST, BookingServiceActivity.this, parameter);


        } else {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }


    }

    private void pharseServiceTypeListData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(BookingServiceActivity.this, message);
            } else {

                Gson gson = new Gson();
                ServiceType serviceType = gson.fromJson(result, ServiceType.class);
                serviceTypeList.addAll(serviceType.getData());


                showServiceList();


                //serviceTypeAdapter.notifyDataSetChanged();
                //lyServiceList.setVisibility(View.VISIBLE);
                //lyBookDetails.setVisibility(View.INVISIBLE);


            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(BookingServiceActivity.this, BookingServiceActivity.this.getResources().getString(R.string.please_try));
        }

    }

    private int selectedService = -1;

    private void showServiceList() {


        final android.support.v7.app.AlertDialog.Builder ad = new android.support.v7.app.AlertDialog.Builder(this);

        final String[] list = new String[serviceTypeList.size()];
        for (int i = 0; i < serviceTypeList.size(); i++) {
            list[i] = serviceTypeList.get(i).getItemname();
        }
        ad.setSingleChoiceItems(list, selectedService, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                selectedService = arg1;


                arg0.dismiss();
                setOnServiceTypeSelected(serviceTypeList.get(selectedService));

            }
        });
        ad.show();
    }


    private void pharseServiceConfirmData(String result) {
        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(BookingServiceActivity.this, message);
            } else {
               /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BookingServiceActivity.this);
                alertDialogBuilder.setMessage(message);

                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                        Intent intent = new Intent(BookingServiceActivity.this, VechicleServiceActivity.class);
                        BookingServiceActivity.this.startActivity(intent);
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();*/



                jsonObject = jsonObject.getJSONObject("booking_data");
                jsonObject = jsonObject.getJSONObject("booking_data");
                Gson gson = new Gson();
                BookingconfirmInfo bookingconfirmInfo=gson.fromJson(jsonObject.toString(), BookingconfirmInfo.class);
                bookingconfirmInfo.setVechicle_name(StaticVariables.bookingDetails.getVechicleName());
                bookingconfirmInfo.setBranchname(StaticVariables.bookingDetails.getBranchname());

                bookingconfirmInfo.setServicecenterChargee(price);
                bookingconfirmInfo.setBookingCharge(StaticVariables.bookingDetails.getService_charge());
                Intent intent = new Intent(this,BookingServiceInvoiceActivity.class);
                intent.putExtra("bookingconfirmInfo",bookingconfirmInfo);
                startActivity(intent);









            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(BookingServiceActivity.this, BookingServiceActivity.this.getResources().getString(R.string.please_try));
        }

    }

    @Override
    public void setOnServiceTypeSelected(ServiceTypeInfo serviceTypeInfo) {
        tvSelectedService.setText(serviceTypeInfo.getItemname());
        StaticVariables.bookingDetails.setService_type(serviceTypeInfo.getItemname());
        StaticVariables.bookingDetails.setService_type_id(serviceTypeInfo.getId());
        //price=serviceTypeInfo.getPrice();
        // tvPrice.setText(price);
        //lyServiceList.setVisibility(View.INVISIBLE);
        //lyBookDetails.setVisibility(View.VISIBLE);

    }
}
