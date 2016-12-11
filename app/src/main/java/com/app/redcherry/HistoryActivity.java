package com.app.redcherry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Fragments.HistoryFragment;
import com.app.redcherry.Interface.PaymentInterface;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Model.HistoryInfo;
import com.app.redcherry.Model.HistoryInfoList;
import com.app.redcherry.Model.LoginResponse;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.webservice.ConnectWebService;
import com.google.gson.Gson;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;
import com.payUMoney.sdk.fragment.SdkLoginFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends BaseActivity implements PaymentInterface {
    private ViewPager pager;
    private TabLayout indicator;
    private final ArrayList<String> CONTENT = new ArrayList<>();
    public static final int SERVICE = 0;
    private static final int CAR_WASH = 1;
    private boolean loading;
    private final ArrayList<HistoryInfo> serviceHistoryList = new ArrayList<>();
    private final ArrayList<HistoryInfo> carWashHistoryList = new ArrayList<>();
    private LinearLayout lyRefersh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lyRefersh = (LinearLayout) this.findViewById(R.id.lyRefersh);
        lyRefersh.setOnClickListener(this);

        callHistoryApi();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.lyRefersh:
                callHistoryApi();
                break;
        }
    }

        private void setTab() {
        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (TabLayout) findViewById(R.id.indicator);

        CONTENT.add(this.getString(R.string.service));
        CONTENT.add(this.getString(R.string.carwash));


        setViewPageAdapter();

    }

    private void callHistoryApi() {
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
                    Utility.alertDialog(HistoryActivity.this, HistoryActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {

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


            parameter.put("userid", Utility.getString(Constants.userId,HistoryActivity.this));

            connectWebService.stringPostRequest(Config.HISTORY, HistoryActivity.this, parameter);


        } else {

                /*HistoryInfoList historyInfoList = HistoryInfoList.DeSerialize(this);
              if(historyInfoList!=null) {
                  historyList=historyInfoList.getData();
                  for(int i=0;i<historyList.size();i++){
                      if(historyList.get(i).getVehicle_type().equals(Constants.Car))
                          serviceHistoryList.add(historyList.get(i));
                      else
                          carWashHistoryList.add(historyList.get(i));

                  }
                  historyList=null;
                  setTab();
              }*/

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
                Utility.alertDialog(HistoryActivity.this, message);
            } else {

                Gson gson = new Gson();
                HistoryInfoList historyInfoList = gson.fromJson(result, HistoryInfoList.class);
                historyInfoList.Serialize(this);
                ArrayList<HistoryInfo> historyList = historyInfoList.getData();
                for(int i = 0; i< historyList.size(); i++){
                    if(historyList.get(i).getBook_type().equals(Constants.Service))
                        serviceHistoryList.add(historyList.get(i));
                    else
                        carWashHistoryList.add(historyList.get(i));

                }
                historyList =null;
                setTab();
                lyRefersh.setVisibility(View.GONE);


            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(HistoryActivity.this, HistoryActivity.this.getResources().getString(R.string.please_try));
        }

    }




    private void setViewPageAdapter() {
        CategoryAdapter adapter = new CategoryAdapter(getSupportFragmentManager(), CONTENT);
        pager.setAdapter(adapter);
        indicator.setupWithViewPager(pager);


        indicator.setTabTextColors(Color.BLACK, Color.parseColor("#DD1C13"));

        indicator.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                pager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


    }

    @Override
    public void onMakePayment(HistoryInfo historyInfo) {

        LoginResponse loginResponse= new LoginResponse();
        loginResponse = loginResponse.DeSerialize(HistoryActivity.this);
        String email=loginResponse.getData().get(0).getEmail();
        SdkLoginFragment.guestLoginEmail=email;

        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();

        builder.setAmount(getAmount(historyInfo.getFinal_service_charge()))
                .setTnxId(getTxnId())
                .setPhone("8882434664")
                .setProductName("product_name")
                .setFirstName("piyush")
                .setEmail("piyush.jain@payu.in")
                .setsUrl("https://www.payumoney.com/mobileapp/payumoney/success.php")
                .setfUrl("https://www.payumoney.com/mobileapp/payumoney/failure.php")
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setIsDebug(true)
                .setKey("dRQuiA")
                .setMerchantId("4928174");// Debug Merchant ID

        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();
        calculateServerSideHashAndInitiatePayment(paymentParam);


    }


    private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam) {

        // Replace your server side hash generator API URL
        String url = "https://test.payumoney.com/payment/op/calculateHashForTest";

        Toast.makeText(this, "Please wait... Generating hash from server ... ", Toast.LENGTH_LONG).show();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has(SdkConstants.STATUS)) {
                        String status = jsonObject.optString(SdkConstants.STATUS);
                        if (status != null || status.equals("1")) {

                            String hash = jsonObject.getString(SdkConstants.RESULT);
                            Log.i("app_activity", "Server calculated Hash :  " + hash);

                            paymentParam.setMerchantHash(hash);
                            //  SdkSession.getInstance(MyActivity.this.getApplicationContext()).setLoginMode("guestLogin");
                            //  SdkSession.getInstance(MyActivity.this.getApplicationContext()).setGuestEmail("rakshithraj11@gmail.com");
                            PayUmoneySdkInitilizer.startPaymentActivityForResult(HistoryActivity.this, paymentParam);
                        } else {
                            Toast.makeText(HistoryActivity.this,
                                    jsonObject.getString(SdkConstants.RESULT),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(HistoryActivity.this,
                            HistoryActivity.this.getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HistoryActivity.this,
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam.getParams();
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
                showDialogMessage("Payment Success Id : " + paymentId);
            } else if (resultCode == RESULT_CANCELED) {
                showDialogMessage("cancelled");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        showDialogMessage("failure");
                    }
                }
                //Write your code if there's no result
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                showDialogMessage("User returned without login");
            }
        }
    }


    private void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private String getTxnId() {
        return ("0nf7" + System.currentTimeMillis());
    }


    private double getAmount(String strAmount) {


        Double amount = 10.0;

        if (isDouble(strAmount)) {
            amount = Double.parseDouble(strAmount);
            return amount;
        } else {
            return amount;
        }
    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    class CategoryAdapter extends FragmentPagerAdapter {
        final ArrayList<String> CONTENT;

        public CategoryAdapter(FragmentManager fm, ArrayList<String> CONTENT) {
            super(fm);
            this.CONTENT = CONTENT;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == SERVICE)
                return HistoryFragment.newInstance(serviceHistoryList,SERVICE,HistoryActivity.this);
            else
                return HistoryFragment.newInstance(carWashHistoryList,CAR_WASH,HistoryActivity.this);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT.get(position % CONTENT.size());
        }

        @Override
        public int getCount() {
            return CONTENT.size();
        }
    }
}
