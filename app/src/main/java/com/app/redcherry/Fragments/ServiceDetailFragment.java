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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.redcherry.BookingServiceActivity;
import com.app.redcherry.Model.ServiceCenterInfo;
import com.app.redcherry.R;
import com.app.redcherry.Ulility.StaticVariables;
import com.app.redcherry.Ulility.TimeStamp;
import com.app.redcherry.Ulility.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by rakshith raj on 11-06-2016.
 */
public class ServiceDetailFragment extends Fragment implements View.OnClickListener {
    private ServiceCenterInfo serviceCenterInfo;
    private Activity activity;
    private View view;
    private String bookDate;
    private ArrayList<String> timeSlotList = new ArrayList<>();
    private String timeSlot;

    public static Fragment newInstance(ServiceCenterInfo serviceCenterInfo, String price) {

        ServiceDetailFragment fragment = new ServiceDetailFragment();
        fragment.setServiceCenterInfo(serviceCenterInfo,price);
        return fragment;
    }

    private void setServiceCenterInfo(ServiceCenterInfo serviceCenterInfo,String price) {
        this.serviceCenterInfo = serviceCenterInfo;
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
       // timeSlotList= TimeStamp.getTimeSolts(serviceCenterInfo.getService_time());

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
        tvSlot.setText(serviceCenterInfo.getSlot());
        tvTimming.setText(serviceCenterInfo.getService_time());


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btBook:
                bookDate="";
                timeSlot="";
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

                if(TextUtils.isEmpty(bookDate)){
                    Utility.alertDialog(activity,"Please select the Date");
                    return;
                }
                if(TextUtils.isEmpty(timeSlot)){
                    Utility.alertDialog(activity,"Please select the Time");
                    return;
                }
                Intent intent = new Intent(activity,BookingServiceActivity.class);
                intent.putExtra("price",serviceCenterInfo.getPrice());
                activity.startActivity(intent);
                book_dialog.cancel();
                break;

        }
    }



    private int selectedTimeList = -1;
    private void showTimeSlotDialog() {
        if(TextUtils.isEmpty(bookDate)){
            Utility.alertDialog(activity,"Please select the Date");
            return;
        }
        Calendar cal = Calendar.getInstance();
        String strDate = cal.get(Calendar.YEAR)+"-"+
                (cal.get(Calendar.MONTH)+1) + "-" +cal.get(Calendar.DAY_OF_MONTH)
                ;

        if(bookDate.equals(strDate))
            timeSlotList= TimeStamp.getTimeSolts(serviceCenterInfo.getService_time(),true);
        else
            timeSlotList= TimeStamp.getTimeSolts(serviceCenterInfo.getService_time(),false);





        final AlertDialog.Builder ad = new AlertDialog.Builder(activity);

        final String[] list = new String[timeSlotList.size()];
        for(int i=0;i<timeSlotList.size();i++){
            list[i]=timeSlotList.get(i);
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

    private void displayAddDialog() {
        // TODO Auto-generated method stub
        if (book_dialog != null) {
            if (book_dialog.isShowing())
                book_dialog.cancel();
        }
        book_dialog = new Dialog(activity);
        //zoomImage_dialog.getWindow().getAttributes().windowAnimations = android.R.style.Theme_Dialog_Translucent;
        book_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        book_dialog.setContentView(R.layout.dialog_book_service);
        intialize(book_dialog);
        book_dialog.setCancelable(false);
        book_dialog.getWindow().setGravity(Gravity.CENTER);
        final Window window = book_dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        book_dialog.show();
    }

    private void intialize(final Dialog book_dialog) {
        LinearLayout lyCleander = (LinearLayout) book_dialog.findViewById(R.id.lyCleander);
        LinearLayout lyTime = (LinearLayout) book_dialog.findViewById(R.id.lyTime);
        tvCleander = (TextView) book_dialog.findViewById(R.id.tvCleander);
        tvTime = (TextView) book_dialog.findViewById(R.id.tvTime);
        Button btBookDialog = (Button) book_dialog.findViewById(R.id.btBookDialog);
        Button btCancelDialog = (Button) book_dialog.findViewById(R.id.btCancelDialog);


        lyCleander.setOnClickListener(ServiceDetailFragment.this);
        lyTime.setOnClickListener(ServiceDetailFragment.this);
        btBookDialog.setOnClickListener(ServiceDetailFragment.this);
        btCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookDate="";
                timeSlot="";
                book_dialog.cancel();
            }
        });

    }


    private void showCalenderDialog() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {

                if(checkValidDate(selectedYear,selectedMonth,selectedDay)) {
                    String year1 = String.valueOf(selectedYear);
                    String month1 = String.valueOf(selectedMonth + 1);
                    String day1 = String.valueOf(selectedDay);
                    bookDate =  year1+ "/" + month1 + "/" +day1 ;

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
        datePicker.getDatePicker().setCalendarViewShown(false);

        datePicker.getDatePicker().setMinDate(System.currentTimeMillis()-(1 * 60 * 60 * 1000));
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis()+ (5 * 24 * 60 * 60 * 1000));


        datePicker.show();
    }

    private boolean checkValidDate(int selectedYear, int selectedMonth, int selectedDay) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date
        long currentDateTime=cal.getTimeInMillis();

        cal.set(selectedYear,selectedMonth,selectedDay);
        if(Calendar.SUNDAY == cal.get(Calendar.DAY_OF_WEEK) ) {
            Utility.alertDialog(activity,"Sorry, the service  center is closed on sunday");
            return false;
        }


        long selectedDateTime=cal.getTimeInMillis();

        if(selectedDateTime<currentDateTime) {
            Utility.alertDialog(activity,"Select Valid Date");
            return false;
        }

        long allowedDateTime=currentDateTime + (5*24*60 * 60 * 1000);

        if(selectedDateTime>allowedDateTime) {
            Utility.alertDialog(activity,"Boooking is  allowed only upcomming 5 days");
            return false;
        }


         return true;
    }

}
