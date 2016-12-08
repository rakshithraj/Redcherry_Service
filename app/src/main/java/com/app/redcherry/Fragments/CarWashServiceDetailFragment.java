package com.app.redcherry.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.app.redcherry.BookingServiceActivity;
import com.app.redcherry.CarWashInvoiceActivty;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Interface.ConfirmInterface;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Model.CarWasBookingInfo;
import com.app.redcherry.Model.CarWashServiceCenterInfo;
import com.app.redcherry.Model.CarWashType;
import com.app.redcherry.R;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.Ulility.StaticVariables;
import com.app.redcherry.Ulility.TimeStamp;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.webservice.ConnectWebService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by rakshith raj on 09-07-2016.
 */
public class CarWashServiceDetailFragment extends Fragment implements View.OnClickListener {
    private CarWashServiceCenterInfo serviceCenterInfo;
    private Activity activity;
    private View view;
    private String bookDate;
    private String price;
    private ArrayList<String> timeSlotList = new ArrayList<>();
    private String timeSlot;
    private String carType;
    // String washTypeId;

    public static CarWashServiceDetailFragment newInstance(CarWashServiceCenterInfo serviceCenterInfo, String price) {

        CarWashServiceDetailFragment fragment = new CarWashServiceDetailFragment();
        fragment.setServiceCenterInfo(serviceCenterInfo, price);
        return fragment;
    }

    private void setServiceCenterInfo(CarWashServiceCenterInfo serviceCenterInfo, String price) {
        this.serviceCenterInfo = serviceCenterInfo;
        this.price = price;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = this.getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_service_detail, container, false);

        return view;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();
        // timeSlotList = TimeStamp.getTimeSolts(serviceCenterInfo.getService_time(),false);

    }

    private void initialize() {
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        TextView branchname = (TextView) view.findViewById(R.id.branchname);
        TextView tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        TextView tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        TextView tvPrice = (TextView) view.findViewById(R.id.price);
        TextView tvTimming = (TextView) view.findViewById(R.id.tvTimming);
        TextView tvSlot = (TextView) view.findViewById(R.id.tvSlot);
        Button btBook = (Button) view.findViewById(R.id.btBook);
        btBook.setOnClickListener(this);

        ratingBar.setRating(Float.parseFloat(serviceCenterInfo.getAverage()));
        branchname.setText(serviceCenterInfo.getBranchname());
        tvPrice.setText(serviceCenterInfo.getPrice());
        tvAddress.setText(serviceCenterInfo.getLocation());
        tvSlot.setText(serviceCenterInfo.getSlots());
        tvTimming.setText(serviceCenterInfo.getService_time());


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btBook:
                bookDate = "";
                timeSlot = "";
                carType = "";
                washTypeIdList.clear();
                bookDate = "";
                timeSlot = "";
                carType = "";
                displayAddDialog();
                break;
            case R.id.lyCleander:
                showCalenderDialog();
                break;
            case R.id.lyTime:
                try {
                    showTimeSlotDialog();
                } catch (Exception e) {
                    Log.d("", "");
                }
                break;
            case R.id.btBookDialog:

                StaticVariables.bookingDetails.setBooking_time(timeSlot);
                StaticVariables.bookingDetails.setBooking_date(bookDate);


                if (TextUtils.isEmpty(carType)) {
                    Utility.alertDialog(activity, "Please select the Type of yoy car");
                    return;
                }
                if (washTypeIdList.size() == 0) {
                    Utility.alertDialog(activity, "Select WashType");
                    return;
                }
                if (TextUtils.isEmpty(bookDate)) {
                    Utility.alertDialog(activity, "Please select the Date");
                    return;
                }
                if (TextUtils.isEmpty(timeSlot)) {
                    Utility.alertDialog(activity, "Please select the Time");
                    return;
                }
                if (StaticVariables.CAR_WASH_TYPE == Constants.CAR_WASH.DOOR_STEP) {

                    if (TextUtils.isEmpty(tvLandmark.getText())) {
                        Utility.alertDialog(activity, "Please enter your  Landmark");
                        return;
                    }
                }
                if (TextUtils.isEmpty(carType)) {
                    Utility.alertDialog(activity, "Select Car Type");
                    return;
                }

                /*Intent intent = new Intent(activity,BookingServiceActivity.class);
                intent.putExtra("price",serviceCenterInfo.getPrice());
                activity.startActivity(intent);
                book_dialog.cancel();
            */


                Utility.showConfirmDialog(new ConfirmInterface() {
                    @Override
                    public void onConfirm() {
                        callBookApi();
                    }
                },"Do you really want to confirm your booking?",this.getActivity());


                break;
            case R.id.lyCarType:
                //callCarTypeApi();
                tvWashType.setText("Select Wash Type");
                //washTypeId = "";
                washTypeIdList.clear();
                washTypeList.clear();
                showCarType();

                break;
            case R.id.lyWashType:

                if (washTypeList.size() == 0)
                    callWashTypeApi();
                else
                    showWashTypeMultiple();
                break;
        }
    }

    private void callBookApi() {


        if (AppGlobal.isNetwork(this.getActivity())) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseBookingData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(CarWashServiceDetailFragment.this.getActivity(), CarWashServiceDetailFragment.this.getActivity().getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {

                }

                @Override
                public boolean getLoading() {
                    return true;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<>();


            parameter.put("userid", StaticVariables.bookingDetails.getUserid());
            parameter.put("fname", StaticVariables.bookingDetails.getFname());
            // parameter.put("vehicle_id", "4");

            Float tempPrice = Float.parseFloat(price);
            tempPrice = tempPrice + Float.parseFloat(StaticVariables.bookingDetails.getService_charge());
            parameter.put("service_charge", tempPrice + "");
            if (switchPickup.isChecked())
                parameter.put("pickup", "Yes");

            else
                parameter.put("pickup", "No");

            parameter.put("service_center_id", StaticVariables.bookingDetails.getService_center_id());
            parameter.put("branchname", StaticVariables.bookingDetails.getBranchname());
            parameter.put("carmodel", carType);
            parameter.put("wash_type", "1");
            String descrption = tvDescription.getText() + "";
            parameter.put("description", descrption + "");
            if (StaticVariables.CAR_WASH_TYPE == Constants.CAR_WASH.DOOR_STEP)
                parameter.put("car_wash_type", Constants.DOOR_STEP);
            else
                parameter.put("car_wash_type", Constants.WORK_STATION);

            parameter.put("landmark", tvLandmark.getText().toString() + "");

            parameter.put("booking_date", StaticVariables.bookingDetails.getBooking_date());
            parameter.put("booking_time", StaticVariables.bookingDetails.getBooking_time());

            String value = "";
           /* for (Map.Entry<String, String> e : parameter.entrySet()){
                value=value+e.getKey()+":"+e.getValue()+"\n";
            }*/
            connectWebService.stringPostRequest(Config.BOOK_CAR_WASH, this.getActivity(), parameter);


        } else {

            AppGlobal.showToast(this.getActivity(), getResources().getString(R.string
                    .network_not_available), 2);
        }

    }

    private void pharseBookingData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(CarWashServiceDetailFragment.this.getActivity(), message);
            } else {

                result = jsonObject.getJSONObject("data").toString();
                Gson gson = new Gson();
                CarWasBookingInfo carWasBookingInfo = gson.fromJson(result, CarWasBookingInfo.class);
                carWasBookingInfo.setWashType(selectedWashTypeText);
                carWasBookingInfo.setBookingCharge(serviceCenterInfo.getPrice());
                Intent intent = new Intent(getActivity(), CarWashInvoiceActivty.class);
                intent.putExtra("carWasBookingInfo", carWasBookingInfo);
                intent.putExtra("price", price);

                getActivity().startActivity(intent);

                /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CarWashServiceDetailFragment.this.getActivity());
                alertDialogBuilder.setMessage(message);

                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                        Intent intent = new Intent(CarWashServiceDetailFragment.this.getActivity(), CarWashHomeActivity.class);
                        CarWashServiceDetailFragment.this.getActivity().startActivity(intent);
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();*/


            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(CarWashServiceDetailFragment.this.getActivity(), CarWashServiceDetailFragment.this.getActivity().getResources().getString(R.string.please_try));
        }
    }

    //private ArrayList<CarWashType> washTypeList = new ArrayList<>();
    //private int selectedWashType = -1;
    //private ArrayList<String> washTypeId=new ArrayList<String>;

    /*private void showWashType() {

        final AlertDialog.Builder ad = new AlertDialog.Builder(activity);

        final String[] list = new String[washTypeList.size()];
        for (int i = 0; i < washTypeList.size(); i++) {
            list[i] = washTypeList.get(i).getWashtype();
        }

        ad.setSingleChoiceItems(list, selectedWashType, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                selectedWashType = arg1;
                tvWashType.setText(list[selectedWashType]);
                washTypeId = washTypeList.get(selectedWashType).getId();
                arg0.dismiss();
                callPriceApi();

            }
        });
        ad.show();

    }
*/

    boolean selectedwashTypes[] = null;
    private ArrayList<CarWashType> washTypeList = new ArrayList<>();
    private ArrayList<String> washTypeIdList = new ArrayList<String>();
    String selectedWashTypeText = "";

    private void showWashTypeMultiple() {

        final String[] list = new String[washTypeList.size()];
        for (int i = 0; i < washTypeList.size(); i++) {
            list[i] = washTypeList.get(i).getWashtype() + " (" + washTypeList.get(i).getPrice() + " Rs)";
        }
        final AlertDialog.Builder ad = new AlertDialog.Builder(activity);
        //ad.setTitle("Select your wash type");
        ad.setMultiChoiceItems(list, selectedwashTypes, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
                // TODO Auto-generated method stub

                if (arg2) {
                    // If user select a item then add it in selected items
                    washTypeIdList.add(washTypeList.get(arg1).getId());
                } else if (washTypeIdList.contains(washTypeList.get(arg1).getId())) {
                    // if the item is already selected then remove it
                    washTypeIdList.remove(washTypeList.get(arg1).getId());
                }
            }
        });
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();

                selectedWashTypeText = "";

                int tempPrice = 0;

                for (int i = 0; i < selectedwashTypes.length; i++) {
                    if (selectedwashTypes[i]) {

                        if (selectedWashTypeText.equals("")) {
                            selectedWashTypeText = washTypeList.get(0).getWashtype();
                        } else {

                            selectedWashTypeText = selectedWashTypeText + " , " + washTypeList.get(i).getWashtype();
                        }
                        tempPrice = tempPrice + Integer.parseInt(washTypeList.get(i).getPrice());
                    }
                }


                if (selectedWashTypeText.equals(""))
                    tvWashType.setText("Select wash type");
                else
                    tvWashType.setText(selectedWashTypeText);

                price = tempPrice + "";
                tvDialogPrice.setText("Rs " + price);


            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
            }
        });


        ad.show();

    }

    private void callPriceApi() {
        if (AppGlobal.isNetwork(this.getActivity())) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharsePriceeData(result);

                }


                @Override
                public void onServerError() {
                    Utility.alertDialog(CarWashServiceDetailFragment.this.getActivity(), CarWashServiceDetailFragment.this.getActivity().getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {

                }

                @Override
                public boolean getLoading() {
                    return true;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<>();
            parameter.put("car_type", carType);
            String tempWashTypeId = "";
            if (washTypeIdList.size() == 1) {
                tempWashTypeId = washTypeIdList.get(0);
            } else {
                for (int i = 0; i < washTypeIdList.size(); i++) {
                    tempWashTypeId = tempWashTypeId + "," + washTypeIdList.get(i);
                }
            }
            parameter.put("wash_type", tempWashTypeId);
            parameter.put("servicecenter", serviceCenterInfo.getId());
            // parameter.put("servicecenter", "13");

            connectWebService.stringPostRequest(Config.CAR_WASH_PRICE, this.getActivity(), parameter);


        }
    }

    private void pharsePriceeData(String result) {
        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                tvWashType.setText("Select Wash Type");
                // washTypeId = "";
                washTypeIdList.clear();
                Utility.alertDialog(CarWashServiceDetailFragment.this.getActivity(), message);
            } else {
                try {
                    price = jsonObject.getJSONArray("data").getJSONObject(0).getString("price");
                    tvDialogPrice.setText("Rs " + price);

                } catch (Exception e) {
                    tvWashType.setText("Select Wash Type");
                    // washTypeId = "";
                    washTypeIdList.clear();
                    Utility.alertDialog(CarWashServiceDetailFragment.this.getActivity(), CarWashServiceDetailFragment.this.getActivity().getResources().getString(R.string.please_try));

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
            tvWashType.setText("Select Wash Type");
            //washTypeId = "";
            washTypeIdList.clear();
            Utility.alertDialog(CarWashServiceDetailFragment.this.getActivity(), CarWashServiceDetailFragment.this.getActivity().getResources().getString(R.string.please_try));
        }
    }


    private void callWashTypeApi() {


        if (AppGlobal.isNetwork(this.getActivity())) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseWashTypeData(result);

                }


                @Override
                public void onServerError() {
                    Utility.alertDialog(CarWashServiceDetailFragment.this.getActivity(), CarWashServiceDetailFragment.this.getActivity().getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {

                }

                @Override
                public boolean getLoading() {
                    return true;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<>();
            parameter.put("car_type", carType);
            parameter.put("servicecenter", StaticVariables.bookingDetails.getService_center_id());


            connectWebService.stringPostRequest(Config.CAR_WASH_TYPE, this.getActivity(), parameter);


        } else {

            AppGlobal.showToast(this.getActivity(), getResources().getString(R.string
                    .network_not_available), 2);
        }

    }

    private void pharseWashTypeData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(CarWashServiceDetailFragment.this.getActivity(), message);
            } else {
                try {
                    JSONObject jsonOject = new JSONObject(result);
                    result = jsonOject.getJSONArray("data").toString();
                    Gson gson = new Gson();
                    washTypeList = gson.fromJson(result, new TypeToken<ArrayList<CarWashType>>() {
                    }.getType());
                    selectedwashTypes = new boolean[washTypeList.size()];

                    showWashTypeMultiple();
                } catch (Exception e) {
                    Utility.alertDialog(CarWashServiceDetailFragment.this.getActivity(), CarWashServiceDetailFragment.this.getActivity().getResources().getString(R.string.please_try));

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(CarWashServiceDetailFragment.this.getActivity(), CarWashServiceDetailFragment.this.getActivity().getResources().getString(R.string.please_try));
        }
    }


    private final ArrayList<String> carTypeList = new ArrayList<>();
    private int selectedCarType = -1;

    private void showCarType() {
        if (carTypeList.size() == 0) {
            carTypeList.add("crossover");
            carTypeList.add("suv");
            carTypeList.add("hatchback");
            carTypeList.add("sedan");

        }
        final AlertDialog.Builder ad = new AlertDialog.Builder(activity);

        final String[] list = new String[carTypeList.size()];
        for (int i = 0; i < carTypeList.size(); i++) {
            list[i] = carTypeList.get(i);
        }

        ad.setSingleChoiceItems(list, selectedCarType, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                selectedCarType = arg1;
                tvSelectedCarType.setText(list[selectedCarType]);
                carType = carTypeList.get(selectedCarType);
                arg0.dismiss();

            }
        });
        ad.show();

    }

    private int selectedTimeList = -1;

    private void showTimeSlotDialog() {

        if (TextUtils.isEmpty(bookDate)) {
            Utility.alertDialog(activity, "Please select the Date");
            return;
        }

        Calendar cal = Calendar.getInstance();
        String strDate = cal.get(Calendar.YEAR) + "-" +
                (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);


        if (bookDate.equals(strDate))
            timeSlotList = TimeStamp.getTimeSolts(serviceCenterInfo.getService_time(), true);
        else
            timeSlotList = TimeStamp.getTimeSolts(serviceCenterInfo.getService_time(), false);


        final AlertDialog.Builder ad = new AlertDialog.Builder(activity);

        final String[] list = new String[timeSlotList.size()];
        for (int i = 0; i < timeSlotList.size(); i++) {
            list[i] = timeSlotList.get(i);
        }

        ad.setSingleChoiceItems(list, selectedTimeList, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                selectedTimeList = arg1;
                timeSlot = timeSlotList.get(selectedTimeList);
                tvTime.setText(list[selectedTimeList]);
                arg0.dismiss();

            }
        });
        ad.show();
    }


    private Dialog book_dialog;
    private TextView tvCleander;
    private TextView tvTime;
    private TextView tvDialogPrice;
    private TextView tvSelectedCarType;
    private TextView tvWashType;
    private TextView tvLandmark;
    private TextView tvDescription;
    private Switch switchPickup;

    private void displayAddDialog() {
        // TODO Auto-generated method stub
        if (book_dialog != null) {
            if (book_dialog.isShowing())
                book_dialog.cancel();
        }
        book_dialog = new Dialog(activity);
        //zoomImage_dialog.getWindow().getAttributes().windowAnimations = android.R.style.Theme_Dialog_Translucent;
        book_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        book_dialog.setContentView(R.layout.dialog_book_carwash_service);
        intialize(book_dialog);
        book_dialog.setCancelable(false);
        book_dialog.getWindow().setGravity(Gravity.CENTER);
        final Window window = book_dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        book_dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //Window window = book_dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        lp.dimAmount = (float) 0.0;
        window.setAttributes(lp);

       /* LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(layoutParams.leftMargin, android.R.attr.actionBarSize,layoutParams.rightMargin, layoutParams.bottomMargin);
        imageView.setLayoutParams(lp);*/
    }

    private void intialize(final Dialog book_dialog) {
        LinearLayout lyCleander = (LinearLayout) book_dialog.findViewById(R.id.lyCleander);
        LinearLayout lyTime = (LinearLayout) book_dialog.findViewById(R.id.lyTime);
        tvCleander = (TextView) book_dialog.findViewById(R.id.tvCleander);
        tvTime = (TextView) book_dialog.findViewById(R.id.tvTime);
        Button btBookDialog = (Button) book_dialog.findViewById(R.id.btBookDialog);
        Button btCancelDialog = (Button) book_dialog.findViewById(R.id.btCancelDialog);

        lyCleander.setOnClickListener(CarWashServiceDetailFragment.this);
        lyTime.setOnClickListener(CarWashServiceDetailFragment.this);
        btBookDialog.setOnClickListener(CarWashServiceDetailFragment.this);
        btCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookDate = "";
                timeSlot = "";
                book_dialog.cancel();
            }
        });

        RelativeLayout lyCarType = (RelativeLayout) book_dialog.findViewById(R.id.lyCarType);
        RelativeLayout lyWashType = (RelativeLayout) book_dialog.findViewById(R.id.lyWashType);
        tvDialogPrice = (TextView) book_dialog.findViewById(R.id.tvPrice);
        tvSelectedCarType = (TextView) book_dialog.findViewById(R.id.tvSelectedCarType);
        tvWashType = (TextView) book_dialog.findViewById(R.id.tvWashType);

        tvLandmark = (TextView) book_dialog.findViewById(R.id.tvLandmark);
        LinearLayout lyPickUp = (LinearLayout) book_dialog.findViewById(R.id.lyPickUp);
        switchPickup = (Switch) book_dialog.findViewById(R.id.switchPickup);
        tvDescription = (TextView) book_dialog.findViewById(R.id.tvDescription);

        if (StaticVariables.CAR_WASH_TYPE == Constants.CAR_WASH.WORK_STATION)
            tvLandmark.setVisibility(View.GONE);
        else {
            TextView tvAdditionalCharge = (TextView) book_dialog.findViewById(R.id.tvAdditionalCharge);
            tvAdditionalCharge.setVisibility(View.GONE);
            switchPickup.setVisibility(View.GONE);
        }

        lyCarType.setOnClickListener(CarWashServiceDetailFragment.this);
        lyWashType.setOnClickListener(CarWashServiceDetailFragment.this);


        switchPickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CarWashServiceDetailFragment.this.getActivity());
                    alertDialogBuilder.setMessage("Vehicle Pickup may involve Additional charges. We" +
                            " will intimate the cost if any, immediately after confirming your " +
                            "Booking"); alertDialogBuilder.setCancelable(false);
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
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();



                }


            }
        });





}


    private void showCalenderDialog() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {

                if (checkValidDate(selectedYear, selectedMonth, selectedDay)) {
                    String year1 = String.valueOf(selectedYear);
                    String month1 = String.valueOf(selectedMonth + 1);
                    String day1 = String.valueOf(selectedDay);
                    bookDate = year1 + "-" + month1 + "-" + day1;

                    tvCleander.setText(bookDate);
                }


            }
        };

        DatePickerDialog datePicker = new DatePickerDialog(activity,
                datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis() + (5 * 24 * 60 * 60 * 1000));
        datePicker.show();
    }

    private boolean checkValidDate(int selectedYear, int selectedMonth, int selectedDay) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date
        long currentDateTime = cal.getTimeInMillis();

        cal.set(selectedYear, selectedMonth, selectedDay);
        if (Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK)) {
            Utility.alertDialog(activity, "Sunday booking is not allowed");
            return false;
        }


        long selectedDateTime = cal.getTimeInMillis();

        if (selectedDateTime < currentDateTime) {
            Utility.alertDialog(activity, "Select Valid Date");
            return false;
        }

        long allowedDateTime = currentDateTime + (5 * 24 * 60 * 60 * 1000);

        if (selectedDateTime > allowedDateTime) {
            Utility.alertDialog(activity, "Boooking is  allowed only upcomming 5 days");
            return false;
        }


        return true;
    }

}
