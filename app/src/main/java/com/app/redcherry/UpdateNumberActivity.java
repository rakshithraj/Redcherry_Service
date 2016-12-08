package com.app.redcherry;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.app.redcherry.BroadCastReciver.IncomingSms;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Interface.SmsListener;
import com.app.redcherry.Model.LoginResponse;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.Ulility.TopExceptionHandler;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.webservice.ConnectWebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class UpdateNumberActivity extends AppCompatActivity {
    private EditText etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        }
        setContentView(R.layout.activity_update_number);
        etPhoneNumber = (EditText) this.findViewById(R.id.etPhoneNumber);
        Utility.convertIntoUsPhoneNumberFormat(etPhoneNumber);

    }

    public void Submit(View view) {
        String phoneNumber = etPhoneNumber.getText().toString();
        if (phoneNumber.trim().length() != 10) {
            Utility.alertDialog(this, "Enter valid number");
            return;
        }

        if (checkAsSmsReadPermission()) {
            //apiCall(phoneNumber);
            Random rnd = new Random();
            int n = 100000 + rnd.nextInt(900000);
            Utility.storeString(Constants.OTP, n + "", this);
            callSendOtpApi(etPhoneNumber.getText().toString().trim(), n + "");

        }
    }

    private void callSendOtpApi(String mobileNumber, String otp) {


        if (AppGlobal.isNetwork(this.getApplicationContext())) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    String SUCESSS = "1", FAILURE = "0";
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("success");
                        String message = jsonObject.getString("message");
                        if (status.trim().equals(FAILURE)) {
                            Utility.alertDialog(UpdateNumberActivity.this, "Username does not exists");


                        } else {
                            String id = jsonObject.getJSONArray("data").getJSONObject(0).getString("id");

                            showEnterOtpDialog(id);

                        }
                    } catch (Exception e) {

                    }


                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(UpdateNumberActivity.this, UpdateNumberActivity.this.getResources().getString(R.string.please_try));

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
            LoginResponse loginResponse = new LoginResponse();
            loginResponse = loginResponse.DeSerialize(UpdateNumberActivity.this);

            Map<String, String> parameter = new HashMap<String, String>();

            parameter.put("email", loginResponse.getData().get(0).getEmail());
            parameter.put("otp", otp);
            parameter.put("mobileno", mobileNumber);


            connectWebService.stringPostRequest(Config.MOBILE_VERIFY_OTP, UpdateNumberActivity.this, parameter);


        } else {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }

    }


    Dialog otpDialog;

    private void showEnterOtpDialog(final String id) {
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

            IncomingSms.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String message) {

                    //Do whatever you want with the code here
                    String otp = Utility.getString(Constants.OTP, UpdateNumberActivity.this);

                    if (message.trim().equals(otp)) {
                        otpDialog.dismiss();
                        otpDialog = null;
                        apiCall(etPhoneNumber.getText().toString().trim());
                    }
                }
            });

        }


        otpDialog = new Dialog(this);
        //zoomImage_dialog.getWindow().getAttributes().windowAnimations = android.R.style.Theme_Dialog_Translucent;
        otpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpDialog.setContentView(R.layout.otp_dialog);
        otpDialog.setCancelable(false);
        otpDialog.getWindow().setGravity(Gravity.CENTER);
        final Window window = otpDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        otpDialog.show();

        otpDialog.findViewById(R.id.btCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpDialog.dismiss();

            }
        });

        otpDialog.findViewById(R.id.btResendPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpDialog.dismiss();


                Random rnd = new Random();
                int n = 100000 + rnd.nextInt(900000);
                Utility.storeString(Constants.OTP, n + "", UpdateNumberActivity.this);
                callSendOtpApi(etPhoneNumber.getText().toString().trim(), n + "");


            }
        });


        otpDialog.findViewById(R.id.btSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) otpDialog.findViewById(R.id.etOtp);
                if (TextUtils.isEmpty(editText.getText().toString().trim()) || editText.getText().toString().length() < 6) {
                    Utility.alertDialog(UpdateNumberActivity.this, "Enter valid OTP");
                } else {
                    String otp = Utility.getString(Constants.OTP, UpdateNumberActivity.this);
                    if (editText.getText().toString().trim().equals(otp)) {
                        otpDialog.dismiss();
                        otpDialog = null;

                        apiCall(etPhoneNumber.getText().toString().trim());


                    } else {
                        Utility.alertDialog(UpdateNumberActivity.this, "Enter valid OTP");

                    }
                }

            }
        });


    }

    private final String[] PermissionsSms =
            {
                    "android.permission.READ_SMS",

            };
    private final int RequestSmsPermissionId = 1;

    private boolean checkAsSmsReadPermission() {
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_SMS")) {
                RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);


                Snackbar.make(mainLayout, "Read OTP  needd sms permission", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(UpdateNumberActivity.this, PermissionsSms, RequestSmsPermissionId);
                            }
                        }).show();


            } else {
                ActivityCompat.requestPermissions(UpdateNumberActivity.this, PermissionsSms, RequestSmsPermissionId);

            }


            return false;

        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        int i;
        switch (requestCode) {
            case RequestSmsPermissionId:

                for (i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                        break;

                    }
                }
                // if (i == grantResults.length)
                Random rnd = new Random();
                int n = 100000 + rnd.nextInt(900000);
                Utility.storeString(Constants.OTP, n + "", this);
                callSendOtpApi(etPhoneNumber.getText().toString().trim(), n + "");

                //apiCall(etPhoneNumber.getText().toString().trim());

                break;

        }
    }

    private void apiCall(String phoneNumber) {
        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(UpdateNumberActivity.this, UpdateNumberActivity.this.getResources().getString(R.string.please_try));

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
            parameter.put("user_id", Utility.getString(Constants.userId, this));
            parameter.put("mobileno", phoneNumber);


            connectWebService.stringPostRequest(Config.UPDATE_NUMBER, UpdateNumberActivity.this, parameter);


        } else {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }
    }

    private void pharseData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");
            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(this, message);
            } else {


                LoginResponse loginResponse = new LoginResponse().DeSerialize(this);
                loginResponse.getData().get(0).setMobileno(etPhoneNumber.getText().toString());
                Utility.storeBoolean(Constants.UPDATE_NUMBER, true, this);
                Intent i = new Intent(this, OneTapActivity.class);
                startActivity(i);
                finish();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(UpdateNumberActivity.this, UpdateNumberActivity.this.getResources().getString(R.string.please_try));
        }
    }

}
