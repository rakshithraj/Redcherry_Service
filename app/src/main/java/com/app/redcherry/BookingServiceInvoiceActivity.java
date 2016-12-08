package com.app.redcherry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.redcherry.Interface.ConfirmInterface;
import com.app.redcherry.Model.BookingconfirmInfo;
import com.app.redcherry.R;
import com.app.redcherry.Ulility.TopExceptionHandler;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.VechicleServiceActivity;

public class BookingServiceInvoiceActivity extends AppCompatActivity implements View.OnClickListener {

   // private TextView tvPrice;
    private TextView tvServiceCenterName,tvBookingCharge,tvServiceCharge;
    private TextView tvVechicleNumber;
    private TextView tvVechicleName;
    private TextView tvCustomerName;
    private TextView tvBookingId;
    private TextView tvBookedAt;
    private TextView tvTypeOfService;
    private TextView tvDescription;
    private TextView tvVechiclePickup;
    private BookingconfirmInfo bookingconfirmInfo;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, VechicleServiceActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        }
        setContentView(R.layout.activity_booking_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         this.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        bookingconfirmInfo=(BookingconfirmInfo)this.getIntent().getSerializableExtra("bookingconfirmInfo");

        intialize();
        setData();
    }

    private void setData() {
        //tvBookingDtae.setText(bookingconfirmInfo.getBooking_date());
        tvVechicleNumber.setText(bookingconfirmInfo.getVehicle_number()
        );
        //tvPrice.setText(bookingconfirmInfo.getService_charge());
        tvVechicleName.setText(bookingconfirmInfo.getVechicle_name());
        tvCustomerName.setText(bookingconfirmInfo.getFname());
        tvBookingId.setText(bookingconfirmInfo.getBooking_id());
        String dateArray[]=bookingconfirmInfo.getBooking_date().split("-");
        String date=dateArray[2]+dateArray[1]+dateArray[0];
        tvBookedAt.setText(date);
        tvDescription.setText(bookingconfirmInfo.getService_type_desc());
        tvVechiclePickup.setText(bookingconfirmInfo.getPickup());
        tvTypeOfService.setText(bookingconfirmInfo.getService_type());
        tvServiceCenterName.setText(bookingconfirmInfo.getBranchname());
        tvBookingCharge.setText(bookingconfirmInfo.getBookingCharge());;
        tvServiceCharge.setText(bookingconfirmInfo.getServicecenterChargee());;

    }

    private void intialize() {

        tvBookingCharge=(TextView)this.findViewById(R.id.tvBookingCharge);
        tvServiceCharge=(TextView)this.findViewById(R.id.tvServiceCharge);


        //  tvBookingDtae=(TextView)this.findViewById(R.id.tvBookingDtae);
        tvServiceCenterName=(TextView)this.findViewById(R.id.tvServiceCenterName);
       // tvPrice=(TextView)this.findViewById(R.id.tvPrice);
        tvVechicleNumber=(TextView)this.findViewById(R.id.tvVechicleNumber);
        tvVechicleName=(TextView)this.findViewById(R.id.tvVechicleName);
        tvCustomerName=(TextView)this.findViewById(R.id.tvCustomerName);
        tvBookingId=(TextView)this.findViewById(R.id.tvBookingId);
        tvBookedAt=(TextView)this.findViewById(R.id.tvBookedAt);
        tvTypeOfService=(TextView)this.findViewById(R.id.tvTypeOfService);
        tvDescription=(TextView)this.findViewById(R.id.tvDescription);
        tvVechiclePickup=(TextView)this.findViewById(R.id.tvVechiclePickup);

        Button btOk = (Button) this.findViewById(R.id.btOk);
        Button btDownloadPdf = (Button) this.findViewById(R.id.btDownloadPdf);

        btOk.setOnClickListener(this);
        btDownloadPdf.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btOk:
               /* showConfirmDialog(new ConfirmInterface() {
                    @Override
                    public void onConfirm() {
                        Intent intent = new Intent(BookingServiceInvoiceActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                },"THANK YOU",this);
*/
                Intent intent = new Intent(BookingServiceInvoiceActivity.this, ThankYouActivity.class);
                startActivity(intent);

                break;
            case R.id.btDownloadPdf:

                break;

        }

    }

    public static void showConfirmDialog(final ConfirmInterface confirmInterface, String message, Activity activity) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                confirmInterface.onConfirm();

            }
        });

       /* alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });*/
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

}
