package com.app.redcherry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.app.redcherry.CustomView.RoundedNetworkImageView;
import com.app.redcherry.Interface.ConfirmInterface;
import com.app.redcherry.Model.LoginResponse;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.Ulility.TopExceptionHandler;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.webservice.AppController;

import static com.app.redcherry.BaseActivity.HOME_MENU.HOME;

/**
 * Created by rakshith raj on 03-06-2016.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tvTitle;

    public enum PAGE {HOME, ADD_VECHICLE, MY_VECHICLE, MY_PROFILE, MY_HISTORY, TRACK_BOOKING, CANCEL_BOOKING, TIPS_VECHICLE, RATE_US, SIGN_OUT}

    public enum HOME_MENU {HOME, VECHICELE_SERVICE, CAR_WASH, AUTO_STORE, ROADSIDE_ASSISTANCE}

    static HOME_MENU current_sub_menu = null;
    static HOME_MENU previous_sub_page = null;


    public static PAGE current_page = null;
    private RoundedNetworkImageView profileImage;

    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private TextView notificationCount;
    private TextView toolbarNotificationCount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        }

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        intialize();
        if (current_page == null)
            current_page = PAGE.HOME;

        if (current_sub_menu == null)
            current_sub_menu = HOME;

        setPageMenuTitle();


    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(changeStatausReceiver);

    }
    @Override
    public void finish() {
       // Runtime.getRuntime().gc();

        super.finish();
        //System.gc();
        //super.finish();

    }


    @Override
    public void onDestroy() {

        tvTitle=null;
        //Utility.recyleImageView(profileImage);
        profileImage=null;

        tvProfileName=null;
        tvProfileEmail=null;
        notificationCount=null;
        toolbarNotificationCount=null;


        //Runtime.getRuntime().gc();
        //System.gc();
        //Runtime.getRuntime().gc();
        super.onDestroy();
    }


    private final BroadcastReceiver changeStatausReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(
                    Constants.ACTION_NOTIFICATION)) {

                notificationCount.setText("" + Utility.getInt(Constants.NOTIFICATION_COUNT, BaseActivity.this));
                toolbarNotificationCount.setText("" + Utility.getInt(Constants.NOTIFICATION_COUNT, BaseActivity.this));

            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse = loginResponse.DeSerialize(this);
        if (loginResponse != null) {
            tvProfileEmail.setText(loginResponse.getData().get(0).getEmail());
            tvProfileName.setText(Utility.capitalize(loginResponse.getData().get(0).getFname()));


            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            String image = loginResponse.getData().get(0).getImage();

            profileImage.setDefaultImageResId(R.mipmap.profile_default);
            profileImage.setErrorImageResId(R.mipmap.profile_default);
            if (image != null)
                profileImage.setImageUrl(image, imageLoader);



        }
        toolbarNotificationCount.setText("" + Utility.getInt(Constants.NOTIFICATION_COUNT, BaseActivity.this));
        notificationCount.setText("" + Utility.getInt(Constants.NOTIFICATION_COUNT, BaseActivity.this));
        this.registerReceiver(changeStatausReceiver, new IntentFilter(
                Constants.ACTION_NOTIFICATION));


        loginResponse=null;
    }


    private void setPageMenuTitle() {


        findViewById(R.id.lyHome).setBackgroundColor(Color.parseColor("#2B2B2B"));
        findViewById(R.id.lyAddVechicle).setBackgroundColor(Color.parseColor("#2B2B2B"));
        findViewById(R.id.lyMyVechicle).setBackgroundColor(Color.parseColor("#2B2B2B"));
        findViewById(R.id.lyMyProfile).setBackgroundColor(Color.parseColor("#2B2B2B"));
        findViewById(R.id.lyMyHistory).setBackgroundColor(Color.parseColor("#2B2B2B"));
        findViewById(R.id.lyTrackBooking).setBackgroundColor(Color.parseColor("#2B2B2B"));
        findViewById(R.id.lyCancelBooking).setBackgroundColor(Color.parseColor("#2B2B2B"));
        findViewById(R.id.lyTipsForVechicle).setBackgroundColor(Color.parseColor("#2B2B2B"));
        findViewById(R.id.lyRateUs).setBackgroundColor(Color.parseColor("#2B2B2B"));
        findViewById(R.id.lySignOut).setBackgroundColor(Color.parseColor("#2B2B2B"));


        switch (current_page) {

            case HOME:
                findViewById(R.id.lyHome).setBackgroundColor(Color.parseColor("#DD1C13"));

                switch (current_sub_menu) {
                    case HOME:
                        tvTitle.setText("Redcherry");
                        break;
                    case VECHICELE_SERVICE:
                        tvTitle.setText("Vehicle Service");
                        break;
                    case CAR_WASH:
                        tvTitle.setText("Car Wash");
                        break;
                    case AUTO_STORE:
                        tvTitle.setText("Auto Store");
                        break;
                    case ROADSIDE_ASSISTANCE:
                        tvTitle.setText("Roadside Assistance");
                        break;
                }

                break;
            case ADD_VECHICLE:

                findViewById(R.id.lyAddVechicle).setBackgroundColor(Color.parseColor("#DD1C13"));
                tvTitle.setText("Add Vehicle");
                break;
            case MY_VECHICLE:
                findViewById(R.id.lyMyVechicle).setBackgroundColor(Color.parseColor("#DD1C13"));
                tvTitle.setText("My Vehicle");
                break;
            case MY_PROFILE:
                findViewById(R.id.lyMyProfile).setBackgroundColor(Color.parseColor("#DD1C13"));
                tvTitle.setText("My Profile");
                break;
            case MY_HISTORY:
                findViewById(R.id.lyMyHistory).setBackgroundColor(Color.parseColor("#DD1C13"));
                tvTitle.setText("My History");
                break;
            case TRACK_BOOKING:
                findViewById(R.id.lyTrackBooking).setBackgroundColor(Color.parseColor("#DD1C13"));
                tvTitle.setText("Track Booking");
                break;
            case CANCEL_BOOKING:
                findViewById(R.id.lyCancelBooking).setBackgroundColor(Color.parseColor("#DD1C13"));
                tvTitle.setText("Cancel Booking");
                break;
            case TIPS_VECHICLE:
                findViewById(R.id.lyTipsForVechicle).setBackgroundColor(Color.parseColor("#DD1C13"));
                tvTitle.setText("Notification");
                break;
            case RATE_US:
                findViewById(R.id.lyRateUs).setBackgroundColor(Color.parseColor("#DD1C13"));
                tvTitle.setText("Rate Us");
                break;
            case SIGN_OUT:

                findViewById(R.id.lySignOut).setBackgroundColor(Color.parseColor("#DD1C13"));
                tvTitle.setText("Sign Out");
                break;
        }
    }

    private void intialize() {

        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        notificationCount = (TextView) this.findViewById(R.id.notificationCount);
        toolbarNotificationCount= (TextView) this.findViewById(R.id.toolbarNotificationCount);

        findViewById(R.id.lyHome).setOnClickListener(this);
        findViewById(R.id.lyAddVechicle).setOnClickListener(this);
        findViewById(R.id.lyMyVechicle).setOnClickListener(this);
        findViewById(R.id.lyMyProfile).setOnClickListener(this);
        findViewById(R.id.lyMyHistory).setOnClickListener(this);
        findViewById(R.id.lyTrackBooking).setOnClickListener(this);
        findViewById(R.id.lyCancelBooking).setOnClickListener(this);
        findViewById(R.id.lyTipsForVechicle).setOnClickListener(this);
        findViewById(R.id.lyRateUs).setOnClickListener(this);
        findViewById(R.id.lySignOut).setOnClickListener(this);

        findViewById(R.id.notificationBell).setOnClickListener(this);

        tvProfileName = (TextView) this.findViewById(R.id.tvProfileName);
        tvProfileEmail = (TextView) this.findViewById(R.id.tvProfileEmail);
        profileImage = (RoundedNetworkImageView) this.findViewById(R.id.profileImage);
        //notificationCount.setText("" + Utility.getInt(Constants.NOTIFICATION_COUNT, this));
       // toolbarNotificationCount.setText("" + Utility.getInt(Constants.NOTIFICATION_COUNT, BaseActivity.this));


    }

    @Override
    public void onClick(View v) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        Intent intent = null;
        previous_sub_page = current_sub_menu;
        switch (v.getId()) {

            case R.id.lyHome:

                current_page = PAGE.HOME;
                current_sub_menu = HOME;
                intent = new Intent(this, HomeActivity.class);
                break;
            case R.id.lyAddVechicle:
                current_page = PAGE.ADD_VECHICLE;
                intent = new Intent(this, AddVechicleActivity.class);
                break;
            case R.id.lyMyVechicle:
                current_page = PAGE.MY_VECHICLE;
                intent = new Intent(this, VechicleListActivity.class);
                break;
            case R.id.lyMyProfile:
                current_page = PAGE.MY_PROFILE;
                intent = new Intent(this, MyProfileActivity.class);
                break;
            case R.id.lyMyHistory:
                current_page = PAGE.MY_HISTORY;
                intent = new Intent(this, HistoryActivity.class);
                break;
            case R.id.lyTrackBooking:
                current_page = PAGE.TRACK_BOOKING;
                intent = new Intent(this, TrackBookActivity.class);
                break;
            case R.id.lyCancelBooking:
                current_page = PAGE.CANCEL_BOOKING;
                intent = new Intent(this, CancelBookingActivity.class);
                break;

            case R.id.lyTipsForVechicle:
                current_page = PAGE.TIPS_VECHICLE;
                intent = new Intent(this, NotificationActivity.class);

                break;
            case R.id.notificationBell:
                current_page = PAGE.TIPS_VECHICLE;
                intent = new Intent(this, NotificationActivity.class);
                break;
            case R.id.lyRateUs:
                //current_page = PAGE.RATE_US;
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                break;
            case R.id.lySignOut:
                Utility.showConfirmDialog(new ConfirmInterface() {

                    @Override
                    public void onConfirm() {
                        current_page = PAGE.HOME;
                        Utility.logoutFacebook();
                        Utility.processGoogleSignOut(BaseActivity.this);
                        Utility.storeBoolean(Constants.isLoggedIn, false, BaseActivity.this);
                        Utility.storeBoolean(Constants.UPDATE_NUMBER, false, BaseActivity.this);

                        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        BaseActivity.this.startActivity(intent);
                        finish();
                    }
                }, "Sign out? Click Ok to confirm.", this);

                return;


        }
        if (intent != null) {
            final Intent finalIntent = intent;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    BaseActivity.this.startActivity(finalIntent);
                    if(!BaseActivity.this.getClass().getSimpleName().equals("HomeActivity"))
                         BaseActivity.this.finish();
                }
            }, 200);
        }


        /*if(previous_page!=PAGE.HOME  && previous_sub_page!=HOME &&previous_page !=null){
            finish();
        }*/


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/


}
