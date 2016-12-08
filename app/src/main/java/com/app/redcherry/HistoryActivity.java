package com.app.redcherry;

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

import com.android.volley.NetworkResponse;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Fragments.HistoryFragment;
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

public class HistoryActivity extends BaseActivity {
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


    class CategoryAdapter extends FragmentPagerAdapter {
        final ArrayList<String> CONTENT;

        public CategoryAdapter(FragmentManager fm, ArrayList<String> CONTENT) {
            super(fm);
            this.CONTENT = CONTENT;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == SERVICE)
                return HistoryFragment.newInstance(serviceHistoryList,SERVICE);
            else
                return HistoryFragment.newInstance(carWashHistoryList,CAR_WASH);

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
