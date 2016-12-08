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
import com.app.redcherry.Model.CarWasBookingInfo;
import com.app.redcherry.Ulility.TopExceptionHandler;

public class CarWashInvoiceActivty extends AppCompatActivity implements View.OnClickListener{
    private TextView tvPrice,tvBookedAt;
    private TextView tvServiceCenterName;
    private TextView tvCustomerName;
    private TextView tvBookingId;
    private TextView tvDescription,tvBookingPrice;
    private CarWasBookingInfo carWasBookingInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        }
        setContentView(R.layout.activity_car_wash_invoice_activty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        carWasBookingInfo=(CarWasBookingInfo)this.getIntent().getSerializableExtra("carWasBookingInfo");
        intialize();
        setData();
        ((TextView)findViewById(R.id.tvTypeOfService)).setText(carWasBookingInfo.getWashType());

    }



    private void setData() {

        tvPrice.setText(this.getIntent().getStringExtra("price"));
        tvCustomerName.setText(carWasBookingInfo.getFname());
        tvBookingId.setText(carWasBookingInfo.getBooking_id());
        tvDescription.setText(carWasBookingInfo.getDescription());
        tvServiceCenterName.setText(carWasBookingInfo.getBranchname());
        tvBookingPrice.setText(carWasBookingInfo.getBookingCharge());
        String dateArray[]=carWasBookingInfo.getSdate().split("-");
        String date=dateArray[2]+dateArray[1]+dateArray[0];


        tvBookedAt.setText(date);
    }

    private void intialize() {
        tvBookingPrice=(TextView)this.findViewById(R.id.tvBookingPrice);
        tvBookedAt=(TextView)this.findViewById(R.id.tvBookedAt);
        tvServiceCenterName=(TextView)this.findViewById(R.id.tvServiceCenterName);
        tvPrice=(TextView)this.findViewById(R.id.tvPrice);
        tvCustomerName=(TextView)this.findViewById(R.id.tvCustomerName);
        tvBookingId=(TextView)this.findViewById(R.id.tvBookingId);
        tvDescription=(TextView)this.findViewById(R.id.tvDescription);
        TextView tvCarModel = (TextView) this.findViewById(R.id.tvCarModel);

        Button btOk = (Button) this.findViewById(R.id.btOk);
        Button btDownloadPdf = (Button) this.findViewById(R.id.btDownloadPdf);

        btOk.setOnClickListener(this);
        btDownloadPdf.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btOk:

                Intent intent = new Intent(CarWashInvoiceActivty.this, ThankYouActivity.class);
                startActivity(intent);


              /*  showConfirmDialog(new ConfirmInterface() {
                    @Override
                    public void onConfirm() {
                        Intent intent = new Intent(CarWashInvoiceActivty.this, HomeActivity.class);
                        startActivity(intent);
                    }
                },"THANK YOU",this);
*/
                break;
            case R.id.btDownloadPdf:

                break;

        }

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, CarWashHomeActivity.class);
        startActivity(intent);

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

//        alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override

//            public void onClick(DialogInterface dialog, int arg1) {
//                dialog.dismiss();
//            }
//        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

}
