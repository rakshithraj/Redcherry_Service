package com.app.redcherry;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import com.android.volley.NetworkResponse;
import com.app.redcherry.Constants.AppConstant;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Model.AddvertiseInfo;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.webservice.ConnectWebService;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SplashScreenActivity extends Activity {
    // GoogleCloudMessaging gcm;
    private String gcmRegId;
    //InstanceID instanceID;
    private static final long SPLASH_TIME_OUT = 3000;
    private InstanceID instanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
           // Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        }
      //  if (Build.VERSION.SDK_INT < 15)
      //      getSupportActionBar().hide();
      //  else
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        callAddApi();


    }

    @Override
    protected void onDestroy() {
        instanceId=null;
        gcmRegId=null;
        super.onDestroy();

    }


    private void callAddApi() {

        if (AppGlobal.isNetwork(this.getApplicationContext())) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseAddData(result);

                }

                @Override
                public void onServerError() {
                    navigateToPage();


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
            Map<String, String> parameter = new HashMap<String, String>();


            connectWebService.stringGetRequest(Config.ADEVERTISE, SplashScreenActivity.this, false);


        } else {
            navigateToPage();
        }
    }

    private void pharseAddData(String result) {

        try {
            JSONObject jsonObject = new JSONObject(result);

            String status = jsonObject.getString("success");

            if (status.trim().equals("1")) {

                Gson gson = new Gson();

                AddvertiseInfo addvertiseInfo=gson
                        .fromJson(result,AddvertiseInfo.class);
                addvertiseInfo.Serialize(SplashScreenActivity.this.getApplicationContext());

            }
      } catch (JSONException e) {
            e.printStackTrace();
        }

        navigateToPage();
    }


    private void navigateToPage() {

        if (!Utility.getBoolean(Constants.isLoggedIn, this.getApplicationContext())) {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    registerwithGcm();

                }
            }, SPLASH_TIME_OUT);


        } else if (!Utility.getBoolean(Constants.UPDATE_NUMBER, this.getApplicationContext())) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreenActivity.this, UpdateNumberActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIME_OUT);

        }

    }


    private void registerwithGcm() {
        // gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
        instanceId = InstanceID.getInstance(getApplicationContext());
        InstanceID instanceID = InstanceID.getInstance(this.getApplicationContext());
        // Read saved registration id from shared preferences.
        gcmRegId = AppGlobal.getStringPreference(this.getApplicationContext(), AppConstant.GCMID);
        Log.d("gcmid is :", gcmRegId);

        if (TextUtils.isEmpty(gcmRegId)) {
            new GCMRegistrationTask().execute();
        } else {
//            Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    private class GCMRegistrationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            /*if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            }*/
            if (instanceId == null) {
                instanceId = InstanceID.getInstance(getApplicationContext());
            }

            /*if (instanceID == null) {
                instanceID = InstanceID.getInstance(getApplicationContext());
            }*/
            try {
                //  gcmRegId = gcm.register(getResources().getString(R.string.sender_id));
                gcmRegId = instanceId.getToken(getResources().getString(R.string.sender_id), "GCM");

/*
                String token = instanceID.getToken(getString(R.string.sender_id),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
*/
                Log.d("Register Successfully", "done");
                Log.d("Device Id is:", "" + gcmRegId);
                AppConstant.gcmid = gcmRegId;
            } catch (IOException e) {
                Log.e("exception", e.getMessage());
            }

            return gcmRegId;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            AppGlobal.setStringPreference(SplashScreenActivity.this.getApplicationContext(), gcmRegId, AppConstant.GCMID);
            AppConstant.gcmid = gcmRegId;

            Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}
