package com.app.redcherry.Ulility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.redcherry.BaseActivity;
import com.app.redcherry.Interface.ConfirmInterface;
import com.app.redcherry.LoginActivity;
import com.app.redcherry.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.plus.Plus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rakshith raj on 22-05-2016.
 */
public final class Utility {
    public static void convertIntoUsPhoneNumberFormat(EditText mEtPhone) {

        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // TODO Auto-generated method stub
                if (source.length() > 0) {

                    if (!Character.isDigit(source.charAt(0)))
                        return "";
                /*	else {
						if (dstart == 3) {
							return source + ") ";
						} else if (dstart == 0) {
							return "(" + source;
						} else if ((dstart == 5) || (dstart == 9))
							return "-" + source;*/
                    else if (dstart >= 10)
                        return "";



						/*
						 * if (dstart == 3) { return source + ""; } else if
						 * (dstart == 0) { return "" + source; } else if
						 * ((dstart == 5) || (dstart == 9)) return "" + source;
						 * else if (dstart >= 10) return "";
						 */
                    //}

                } else {

                }

                return null;
            }

        };
        mEtPhone.setFilters(new InputFilter[]{filter});

    }

    public static void hideKeyboard(View view) {
//        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        //  imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        imm.toggleSoftInput(0, 0);
    }

    public static void alertDialog(Context context, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static boolean isValidMail(String email2) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email2);
        check = m.matches();
        return check;
    }


    public static void storeBoolean(String key, Boolean value, Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getResources().getString(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static Boolean getBoolean(String key, Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getResources().getString(R.string.preferences), Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(key, false);
    }


    public static void storeString(String key, String value, Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getResources().getString(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public static String getString(String key, Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getResources().getString(R.string.preferences), Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, null);
    }


    public static void storeInt(String key, int value, Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getResources().getString(R.string.preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public static int getInt(String key, Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getResources().getString(R.string.preferences), Context.MODE_PRIVATE);
        return sharedpreferences.getInt(key, 0);
    }

    public static LatLng getLocationFromAddress(Context context, String strAddress) {


        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private static final HashMap<String, String> locHaspMap = new HashMap<>();

    public static String getAddress(double latitude, double longitude, Activity activity) {
        // TODO Auto-generated method stub
        String locAddresses;

        if (locHaspMap.containsKey(latitude + "" + longitude)) {
            locAddresses = locHaspMap.get(latitude + "" + longitude);
            return locAddresses;
        }

        try {
            Geocoder geocoder;
            List<Address> addresses;

            geocoder = new Geocoder(activity, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            locAddresses = addresses.get(0).getAddressLine(0) + ","
                    + addresses.get(0).getAddressLine(1) + ",\n"
                    + addresses.get(0).getAddressLine(2);

            if (locAddresses != null)
                locHaspMap.put(latitude + "" + longitude, locAddresses);
            return locAddresses;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }


    public static boolean checkGpsEnabled(Activity activity) {
        // TODO Auto-generated method stub
        LocationManager locationManager = null;
        boolean gps_enabled = false;

        if (locationManager == null)
            locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        return gps_enabled;
    }


    public static void showConfirmDialog(final ConfirmInterface confirmInterface, String message, Activity activity) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                confirmInterface.onConfirm();

            }
        });

        alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public static void showSoftKeyboard(View view, Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public static String capitalize(String str) {
        int delimLen = -1;
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        int strLen = str.length();
        StringBuilder buffer = new StringBuilder(strLen);
        boolean capitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt(i);
            if (capitalizeNext) {
                buffer.append(Character.toTitleCase(ch));
                capitalizeNext = false;
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    public static void logoutFacebook() {
        try {
            if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
                //Logged in so show the login button
                LoginManager.getInstance().logOut();


            }
        } catch (Exception e) {

        }
    }


    public static void processGoogleSignOut(final Activity activiy) {
        try {
            GoogleApiClient mGoogleApiClient = null;
            final GoogleApiClient finalMGoogleApiClient = mGoogleApiClient;
            mGoogleApiClient = new GoogleApiClient.Builder(activiy).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(@Nullable Bundle bundle) {
                    Plus.AccountApi.clearDefaultAccount(finalMGoogleApiClient);
                    finalMGoogleApiClient.clearDefaultAccountAndReconnect();
                    finalMGoogleApiClient.disconnect();
                    finalMGoogleApiClient.connect();
//                    Intent intent = new Intent(activiy, LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
//                            Intent.FLAG_ACTIVITY_NEW_TASK);
//                    activiy.startActivity(intent);
//                    activiy.finish();
                }

                @Override
                public void onConnectionSuspended(int i) {

                }
            }).build();


        }catch (Exception e){

        }

    }

    public static  void recyleImageView(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
        }
    }
}
