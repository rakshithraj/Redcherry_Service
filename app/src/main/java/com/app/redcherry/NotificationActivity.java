package com.app.redcherry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.NetworkResponse;
import com.app.redcherry.Adapter.NotifiationListAdapter;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Interface.NotificationClickListner;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Model.NotificatioInfo;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.webservice.ConnectWebService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends BaseActivity implements NotificationClickListner {
    private LinearLayout lyRefersh;
    private NotifiationListAdapter notifiationListAdapter;
    private final ArrayList<NotificatioInfo> data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intialize();
        Utility.storeInt(Constants.NOTIFICATION_COUNT,0,this.getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                apiCall();
            }
        },100);
    }

    @Override
    public void onDestroy() {
        lyRefersh=null;
        notifiationListAdapter=null;
        data.clear();
        super.onDestroy();;
    }
    private void intialize() {
        lyRefersh = (LinearLayout) this.findViewById(R.id.lyRefersh);

        RecyclerView notofication_list = (RecyclerView) this.findViewById(R.id.notofication_list);
        notifiationListAdapter = new NotifiationListAdapter(data);
        notifiationListAdapter.setOnNotificationClickListner(this);

        notofication_list.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        notofication_list.setAdapter(notifiationListAdapter);
        lyRefersh.setOnClickListener(this);
        lyRefersh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCall();
            }
        });

        notofication_list=null;

    }


    private void apiCall() {
        if (AppGlobal.isNetwork(this)) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseListData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(NotificationActivity.this, NotificationActivity.this.getResources().getString(R.string.please_try));

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
            parameter.put("userid", Utility.getString(Constants.userId, this));

            //Utility.getString(Constants.userId, this)
            connectWebService.stringPostRequest(Config.NOTIFICATION_LIST+Utility.getString(Constants.userId, this)
                    , NotificationActivity.this, parameter);
            connectWebService=null;


        } else {


            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }
    }

    private void pharseListData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                //Utility.alertDialog(NotificationActivity.this, message);
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                Gson gson = new Gson();

                ArrayList<NotificatioInfo> list = gson.fromJson(jsonArray.toString(),
                        new TypeToken<ArrayList<NotificatioInfo>>(){}.getType()
                       );

                data.addAll(list);
                notifiationListAdapter.notifyDataSetChanged();
                lyRefersh.setVisibility(View.GONE);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(NotificationActivity.this, NotificationActivity.this.getResources().getString(R.string.please_try));
        }

    }

    @Override
    public void onNotificationClicked(NotificatioInfo notificatioInfo) {
        Intent intent = new Intent(this,NotificationDetail.class);
        intent.putExtra("notificatioInfo",notificatioInfo);
        this.startActivity(intent);


    }
}
