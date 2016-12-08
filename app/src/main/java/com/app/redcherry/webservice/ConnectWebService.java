package com.app.redcherry.webservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.R;
import com.app.redcherry.Ulility.Config;


import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rakshith on 9/25/2015.
 */
public class ConnectWebService {

    private ServerResponse serverResponse;


    public void setOnServerResponse(ServerResponse serverResponse){
        this.serverResponse=serverResponse;
    }
    public void jsonObjectGetRequest(String url, final Activity activity) {

        String tag_json_obj = "json_obj_req";
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        Toast.makeText(activity, "response=" + response, Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(activity, "error=" + error, Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    public void jsonArrayGetRequest(String url, final Activity activity) {


        String tag_json_arry = "json_array_req";


        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);

    }


    public void stringGetRequest(String url, final Activity activity, final boolean dialog) {
        String tag_string_req = "string_req";
        final ProgressDialog  pDialog;
        if(activity!=null) {
            pDialog = new ProgressDialog(activity, R.style.MyTheme);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
       else
            pDialog=null;

        if(dialog && activity!=null) {

            pDialog.setMessage("Loading...");
            pDialog.show();
        }



        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {
                if(dialog && activity!=null)
                   pDialog.dismiss();

                serverResponse.setLoading(false);
               // Toast.makeText(activity, "response=" + response, Toast.LENGTH_LONG).show();
                if(serverResponse!=null)
                   serverResponse.onServerResponse(response);



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(dialog && activity!=null)
                    pDialog.dismiss();
                serverResponse.setLoading(false);
                if(serverResponse!=null)
                   // Toast.makeText(activity, "response=" + error, Toast.LENGTH_LONG).show();
                    serverResponse.onServerError();

            }

        }




        ){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if(serverResponse!=null)
                   serverResponse.parseNetworkResponse(response);


                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-Mashape-Key", "9UB3lHnZLAmsh1f0BCneGMjKaLCVp1h8yAcjsnWghOYBzkf3fm");
                headers.put("Accept", "application/json");
                return headers;
            }




        };



       /* strReq.setRetryPolicy(new DefaultRetryPolicy(
                1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    public void jsonArrayPostRequest(String url, final Activity activity, final Map<String, String> params) {

        String tag_json_obj = "json_obj_req";
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();

        CustomJsonArrayRequest jsonObjReq = new CustomJsonArrayRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        pDialog.dismiss();
                        Toast.makeText(activity, "response=" + response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                Toast.makeText(activity, "response=" + error, Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    public void jsonObjectPostRequest(String url, final Activity activity, final Map<String, String> params) {

        String tag_json_obj = "json_obj_req";
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();

        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        pDialog.dismiss();
                        Toast.makeText(activity, "response=" + response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismiss();
                Toast.makeText(activity, "response=" + error, Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    public void stringPostRequest(String url, final Activity activity, final Map<String, String> params) {

        if(url.equals(Config.BOOK_CAR_WASH))
           Log.d("tag","BOOK_CAR_WASH");
        String tag_json_obj = "json_obj_req";
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage(Html.fromHtml("<font color='red'>Please wait..." +
                "</font>"));
        pDialog.setCancelable(false);
        pDialog.show();
        CustomStringRequest jsonObjReq = new CustomStringRequest(Request.Method.POST,
                url, null,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(pDialog!=null)
                        pDialog.dismiss();
                        if(serverResponse!=null)
                            serverResponse.onServerResponse(response);
                        //Toast.makeText(activity, "response=" + response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if(pDialog!=null)
                  pDialog.dismiss();
                serverResponse.onServerError();
               // Toast.makeText(activity, "response=" + error, Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return new HashMap<>();
            }

        };

// Adding request to request queue
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    public void postRequest_File(String url, final Activity activity, File file) {

        String tag_json_obj = "json_obj_req";
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();

        //RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        MultipartRequestTest jsonObjReq = new MultipartRequestTest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Toast.makeText(activity, "response=" + response, Toast.LENGTH_LONG).show();


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(activity, "response=" + error, Toast.LENGTH_LONG).show();

            }
        }, file);

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


       // queue.add(jsonObjReq);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }




    public void postRequest_Entity(String url, final Activity activity, MultipartEntity entity) {

        String tag_json_obj = "json_obj_req";
        final ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();

        //RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        MultipartRequestTest jsonObjReq = new MultipartRequestTest(Request.Method.POST,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        Toast.makeText(activity, "response=" + response, Toast.LENGTH_LONG).show();
                        Log.d("tag", response);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(activity, "response=" + error, Toast.LENGTH_LONG).show();

            }
        }, entity);

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        // queue.add(jsonObjReq);
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void volleyNetworkInageLoader(NetworkImageView networkImageView, String url) {
        if(networkImageView.getDrawable()==null)
            networkImageView.setImageResource(R.mipmap.loading);

        ImageLoader  imageLoader=AppController.getInstance().getImageLoader();
        networkImageView.setImageUrl(url, imageLoader);
    }
}