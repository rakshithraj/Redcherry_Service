package com.app.redcherry;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.NetworkResponse;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Fragments.CancelFragment;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Model.HistoryInfo;
import com.app.redcherry.Model.HistoryInfoList;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.webservice.ConnectWebService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CancelBookingActivity extends BaseActivity  {
    private final ArrayList<HistoryInfo> serviceHistoryList = new ArrayList<>();
    private final ArrayList<HistoryInfo> carWashHistoryList = new ArrayList<>();
    // CancelAdapter adpter;
    private ViewPager pager;
    private TabLayout indicator;
    private final ArrayList<String> CONTENT = new ArrayList<>();
    private static final int SERVICE = 0;
    public static final int CAR_WASH = 1;
    private boolean loading;
    private LinearLayout lyRefersh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adpter = new CancelAdapter(historyList);
        adpter.setOnCancelListner(this);
        recyclerView.setAdapter(adpter);
    */
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
           /* if(CONTENT.size()>0){
                adapter.notifyDataSetChanged();
                return;
            }*/


        pager = (ViewPager) findViewById(R.id.pager);
        indicator = (TabLayout) findViewById(R.id.indicator);
            CONTENT.clear();
        CONTENT.add(this.getString(R.string.service));
        CONTENT.add(this.getString(R.string.carwash));


        setViewPageAdapter();

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


    public void callHistoryApi() {
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
                    Utility.alertDialog(CancelBookingActivity.this, CancelBookingActivity.this.getResources().getString(R.string.please_try));

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


            parameter.put("userid", Utility.getString(Constants.userId, CancelBookingActivity.this));

            connectWebService.stringPostRequest(Config.MY_UPCOMMING_BOOKED, CancelBookingActivity.this, parameter);


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
                Utility.alertDialog(CancelBookingActivity.this, message);
            } else {
                Gson gson = new Gson();
                HistoryInfoList historyInfoList = gson.fromJson(result, HistoryInfoList.class);
                historyInfoList.Serialize(this);
                ArrayList<HistoryInfo> historyList = historyInfoList.getData();
                serviceHistoryList.clear();
                carWashHistoryList.clear();
                for(int i = 0; i< historyList.size(); i++){
                    if(historyList.get(i).getBook_type().equals(Constants.Service))
                        serviceHistoryList.add(historyList.get(i));
                    else
                        carWashHistoryList.add(historyList.get(i));

                }
                historyList =null;
                setTab();
                //adpter.notifyDataSetChanged();

                lyRefersh.setVisibility(View.GONE);


            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(CancelBookingActivity.this, CancelBookingActivity.this.getResources().getString(R.string.please_try));
        }

    }




    private Dialog cancelBooking_dialog;
    private Button btContinue;
    private Button btBack;
    private EditText etReason;

    private void displayReasonDialog(final HistoryInfo historyInfo) {
        // TODO Auto-generated method stub
        if (cancelBooking_dialog != null) {
            if (cancelBooking_dialog.isShowing())
                cancelBooking_dialog.cancel();
        }
        cancelBooking_dialog = new Dialog(this);
        cancelBooking_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cancelBooking_dialog.setContentView(R.layout.enter_reason_dialog);
        intialize(cancelBooking_dialog);
        cancelBooking_dialog.setCancelable(true);
        cancelBooking_dialog.getWindow().setGravity(Gravity.CENTER);
        final Window window = cancelBooking_dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking_dialog.cancel();
            }
        });

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking_dialog.cancel();
                callCancelBookingpi(etReason.getText().toString(), historyInfo);
            }
        });

        cancelBooking_dialog.show();
    }

    private void intialize(Dialog addVechicle_dialog) {
        btContinue = (Button) cancelBooking_dialog.findViewById(R.id.btContinue);
       // etReason = (EditText) cancelBooking_dialog.findViewById(R.id.etReason);
        btBack = (Button) cancelBooking_dialog.findViewById(R.id.btBack);

    }

    private void callCancelBookingpi(String reason, HistoryInfo historyInfo) {
        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseCancelData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(CancelBookingActivity.this, CancelBookingActivity.this.getResources().getString(R.string.please_try));

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


            parameter.put("userid", Utility.getString(Constants.userId, CancelBookingActivity.this));
            parameter.put("book_id", historyInfo.getBooking_id());
            parameter.put("reason", reason);

            connectWebService.stringPostRequest(Config.CANCEL_BOOKING, CancelBookingActivity.this, parameter);


        } else {


            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }

    }

    private void pharseCancelData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(CancelBookingActivity.this, message);
            } else {

                Utility.alertDialog(CancelBookingActivity.this, message);
                callHistoryApi();

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(CancelBookingActivity.this, CancelBookingActivity.this.getResources().getString(R.string.please_try));
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
                return CancelFragment.newInstance(serviceHistoryList,Constants.Service);
            else
                return CancelFragment.newInstance(carWashHistoryList,Constants.Car_wash);

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
