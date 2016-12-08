package com.app.redcherry.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.redcherry.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by rakshith raj on 11-06-2016.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private double lat;
    private double lng;
    private GoogleMap mMap;
    private String address;
    public static MapFragment newInstance(String lat, String lng,String address) {
        MapFragment mapFragment = new MapFragment();
        mapFragment.setLocation(lat, lng, address);
        return mapFragment;
    }

    private void setLocation(String lat, String lng,String address) {
        if(lat!=null)
        this.lat = Double.parseDouble(lat);
        if(lng!=null)

            this.lng = Double.parseDouble(lng);
        if(address!=null)

            this.address=address;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = this.getActivity();
    }


    @Override
    public void onDestroy() {
        mMap=null;
        super.onDestroy();
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if(view==null)
            view= inflater.inflate(R.layout.fragment_map, container, false);
        return view;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();
        handlerMap.postDelayed(runnable,500);

    }

    @Override
    public void onResume() {
        super.onResume();
        LatLng point = new LatLng(lat,lng);
        drawMarker(point);
    }

    private void initialize() {
    }




    private final Handler handlerMap = new Handler();

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Fragment supFrg = MapFragment.this.getChildFragmentManager().
                    findFragmentById(
                            R.id.mapp);



            SupportMapFragment fm = ((SupportMapFragment) supFrg);
            if(fm!=null)

            fm.getMapAsync(MapFragment.this);

        }
    };



    @Override
    public void onMapReady(GoogleMap googleMap) {
        Fragment supFrg = MapFragment.this.getChildFragmentManager().
                findFragmentById(
                        R.id.mapp);
        SupportMapFragment fm = ((SupportMapFragment) supFrg);
       fm.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap =googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                LatLng point = new LatLng(lat,lng);
                drawMarker(point);
            }
        });



    }


    private void drawMarker(LatLng point) {
        if(mMap!=null) {
            mMap.clear();

            // Creating an instance of MarkerOptions
            final MarkerOptions markerOptions = new MarkerOptions();

            // Setting latitude and longitude for the marker
            markerOptions.position(point);

            // Setting title for the InfoWindow
            markerOptions.title(address);


            Marker userLocation = mMap.addMarker(markerOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                    lat, lng), 14.0f));
        }

    }

}
