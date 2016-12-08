package com.app.redcherry;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.TopExceptionHandler;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.webservice.ConnectWebService;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final String[] PermissionsLocation =
            {
                    Manifest.permission.READ_PHONE_STATE,

            };
    //private EditText etUsername;
    private EditText etFirstname,etLastname;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRePassword;
  //  private EditText etPhoneNumber;
    private EditText etAddress;
    private RadioButton chkMale;
    private RadioButton chkFemale;
    private String userName;
    private String firstName,lastName;
    private String password;
    private String retypePassword;
    private String email;
    //private String mobileNo;
    private String deviceId;
    private String gender;
    private String address;
    boolean isMale, isFemale;
    private final int RequestPermissionId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        }
        setContentView(R.layout.activity_sign_up);
        intialize();
    }

    private void intialize() {
        etFirstname = (EditText) this.findViewById(R.id.etFirstname);
        etLastname = (EditText) this.findViewById(R.id.etLastname);

        etEmail = (EditText) this.findViewById(R.id.etEmail);
        etPassword = (EditText) this.findViewById(R.id.etPassword);
        etRePassword = (EditText) this.findViewById(R.id.etRePassword);
       // etPhoneNumber = (EditText) this.findViewById(R.id.etPhoneNumber);
        //Utility.convertIntoUsPhoneNumberFormat(etPhoneNumber);
        etAddress = (EditText) this.findViewById(R.id.etAddress);

        Button sign_up = (Button) this.findViewById(R.id.sign_up);
        sign_up.setOnClickListener(this);

        TextView tvAlreadyReg = (TextView) this.findViewById(R.id.tvAlreadyReg);
        tvAlreadyReg.setOnClickListener(this);

        chkMale = (RadioButton) this.findViewById(R.id.chkMale);
        chkFemale = (RadioButton) this.findViewById(R.id.chkFemale);

        chkMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    chkFemale.setChecked(false);
            }
        });

        chkFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    chkMale.setChecked(false);
            }
        });


       /* etUsername.post(new Runnable() {
            @Override
            public void run() {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up:
                if (Build.VERSION.SDK_INT < 23 || checkPermission()) {
                    callRegister();
                }


                break;
            case R.id.tvAlreadyReg:
                finish();
                break;
        }
    }

    private void callRegister() {

        getData();
        if (validateData()) {

            callRegisterApi();

        }

    }

    private boolean loading;

    private void callRegisterApi() {

        if (AppGlobal.isNetwork(this)) {
            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);
                    if(result==null){
                        Utility.alertDialog(SignUpActivity.this, SignUpActivity.this.getResources().getString(R.string.please_try));
                        return;
                    }
                    pharseData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(SignUpActivity.this, SignUpActivity.this.getResources().getString(R.string.please_try));
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
            parameter.put("fname", userName);
            parameter.put("password", password);
            parameter.put("gender", gender);
            parameter.put("email", email);
            parameter.put("deviceid", deviceId);
           // parameter.put("mobileno", mobileNo);
            parameter.put("mobileno", "");
            parameter.put("address", address);

            connectWebService.stringPostRequest(Config.REGISTER_URL, SignUpActivity.this, parameter);


        } else

        {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }

    }

    private void pharseData(String result) {

        String SUCESSS="1", FAILURE="0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status=jsonObject.getString("success");
            String message=jsonObject.getString("message");
            if(status.trim().equals(FAILURE)){
                Utility.alertDialog(SignUpActivity.this, message);
            }else{
                registerSucessDialog();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(SignUpActivity.this, SignUpActivity.this.getResources().getString(R.string.please_try));
        }


    }

    private void registerSucessDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Registered Sucessfully");

        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private boolean validateData() {

        if (TextUtils.isEmpty(firstName)) {
            etFirstname.setError("Enter First name");
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            etFirstname.setError("Enter Last name");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Enter email");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter Password");
            return false;
        }
        if (TextUtils.isEmpty(retypePassword)) {
            etRePassword.setError("Retype Password");
            return false;
        }
        if (TextUtils.isEmpty(gender)) {
            etRePassword.setError("Select Gender");
            return false;
        }
       /* if (TextUtils.isEmpty(mobileNo)) {
            etPhoneNumber.setError("Enter mobile numner");
            return false;
        }*/

       /* if (TextUtils.isEmpty(address)) {
            etAddress.setError("Enter Address");
            return false;
        }
*/
        if (!Utility.isValidMail(email)) {
            Utility.alertDialog(this, "Enter valid email address");
            return false;
        }

        if (!password.equals(retypePassword)) {
            Utility.alertDialog(this, "Password and retype password does not match");
            return false;
        }

       /* if (mobileNo.trim().length() != 10) {
            Utility.alertDialog(this, "Enter valid mobile number");
            return false;
        }*/

        return true;
    }

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);


                Snackbar.make(mainLayout, "Device Id  is required to register.", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(SignUpActivity.this, PermissionsLocation, RequestPermissionId);
                            }
                        }).show();


            } else {
                ActivityCompat.requestPermissions(SignUpActivity.this, PermissionsLocation, RequestPermissionId);

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

            case RequestPermissionId:

                for (i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                        break;

                    }
                }
                if (i == grantResults.length)
                    callRegister();

                break;

        }
    }

    private void getData() {
        firstName = etFirstname.getText().toString();
        lastName = etLastname.getText().toString();
        userName=firstName+" "+lastName;
        password = etPassword.getText().toString();
        retypePassword = etRePassword.getText().toString();
        email = etEmail.getText().toString();
       // mobileNo = etPhoneNumber.getText().toString();
        address = etAddress.getText().toString();
        if (chkMale.isChecked())
            gender = "male";
        if (chkFemale.isChecked())
            gender = "female";

        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
