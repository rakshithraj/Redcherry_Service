package com.app.redcherry.Interface;

import com.android.volley.NetworkResponse;

/**
 * Created by Rakshith on 10/13/2015.
 */
public interface ServerResponse {

    void onServerResponse(String result);
    void onServerError();
    void setLoading(boolean status);
    boolean getLoading();


    void parseNetworkResponse(NetworkResponse response);
}
