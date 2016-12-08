package com.app.redcherry;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.app.redcherry.BroadCastReciver.IncomingSms;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Interface.SmsListener;
import com.app.redcherry.Model.LoginResponse;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.TopExceptionHandler;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.webservice.ConnectWebService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.app.redcherry.Constants.AppConstant;
import com.app.redcherry.Constants.AppGlobal;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("ALL")
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks {
    // The minimum time between updates in milliseconds
    ProgressDialog progress_dialog;
    CallbackManager callbackManager;
    GoogleCloudMessaging gcm;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;

    EditText userName, password;
    Button signIn;

    private SharedPreferences sharedpreferences;
    private String gcmRegId;
    private LocationManager locationManager;
    private int REQUEST_CODE = 101;
    private AlertDialog mAlertDialog;

    double lat = 78, longi = 28;
    boolean loading = false;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private static final int SIGN_IN_REQUEST_CODE = 10;
    private static final int ERROR_DIALOG_REQUEST_CODE = 11;
    private boolean mIntentInProgress;
    private boolean mSignInClicked = false;
    private Context context;

    @Override
    protected void onDestroy() {
        mGoogleApiClient = null;
        mConnectionResult = null;
        locationManager = null;
        mConnectionResult = null;
        mGoogleApiClient = null;
        callbackManager = null;
        userName = null;
        super.onDestroy();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        }


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_signin_new);
        registerwithGcm();

        bindView();


        //Log.e("Hash key is ", getHashKey());
        facebookLogin();
        mGoogleApiClient = buildGoogleAPIClient();
        try {
            if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
                //Logged in so show the login button
                LoginManager.getInstance().logOut();


            }
        } catch (Exception e) {

        }

    }


    protected void onPause() {
        super.onPause();

    }

    protected void onResume() {
        super.onResume();

       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // make sure to initiate connection
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // disconnect api if it is connected
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }


    /**
     * API to return GoogleApiClient Make sure to create new after revoking
     * access or for first time sign in
     *
     * @return
     */
    private GoogleApiClient buildGoogleAPIClient() {
        return new GoogleApiClient.Builder(this.getApplicationContext()).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    private void facebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginButton btnFacebookLogin = (LoginButton) findViewById(R.id.login_button);
        btnFacebookLogin.setReadPermissions("email", "public_profile");
        btnFacebookLogin.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_photos"));


        btnFacebookLogin.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        Log.e("FACEBOOK Object >>  :", object.toString());
                                        String personEmail = object.optString("email");
                                        String personName = object.optString("name");
                                        String personId = object.optString("id");
                                        String gender = object.optString("gender");
                                        Uri personPhotoUrl = Uri.parse("https://graph.facebook.com/" + object.optString("id") + "/picture?type=large");
                                        if (personPhotoUrl == null) {
                                            apiCallForFacebookLogin(gender, personName.trim().toString(), personEmail, personId, "");
                                        } else {
                                            apiCallForFacebookLogin(gender, personName.trim().toString(), personEmail, personId, personPhotoUrl.toString());
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void registerwithGcm() {
        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());

        // Read saved registration id from shared preferences.
        gcmRegId = AppGlobal.getStringPreference(LoginActivity.this.getApplicationContext(), AppConstant.GCMID);
        Log.e("gcmid is :", gcmRegId);

        if (TextUtils.isEmpty(gcmRegId)) {
            new GCMRegistrationTask().execute();
        } else {
//            Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_SHORT).show();
        }
    }

    public String getHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        return null;
    }

    private void bindView() {
        TextView signUp, googlePlusSignIn,
                emergencyLogin, forgotpassword;
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        sharedpreferences = getSharedPreferences(getResources().getString(R.string.preferences), Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        signUp = (TextView) findViewById(R.id.signUp_textview);
        signUp.setOnClickListener(this);
        signIn = (Button) findViewById(R.id.login_textview);
        signIn.setOnClickListener(this);
        userName = (EditText) findViewById(R.id.username_textview);
        password = (EditText) findViewById(R.id.password_textview);
        googlePlusSignIn = (TextView) findViewById(R.id.googleplus_signInTextview);
        googlePlusSignIn.setOnClickListener(this);

        forgotpassword = (TextView) findViewById(R.id.btnForgotPassword);
        forgotpassword.setOnClickListener(this);
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

        userName.post(new Runnable() {
            @Override
            public void run() {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            }
        });
        context = userName.getContext();

        progress_dialog = new ProgressDialog(context);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp_textview:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.login_textview:
                if (!userName.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    if (isValidMail(userName.getText().toString().trim())) {
                        apiCall();
                    } else {
                        Utility.alertDialog(context, "Email is Not Valid");
                    }
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString("username", "abc");
//                    editor.putString("password", "abc");
//                    editor.commit();
                } else {
                    Utility.alertDialog(context, "Please fill all detail above ");
                }
                break;
            case R.id.googleplus_signInTextview:

                if (AppGlobal.isNetwork(this)) {
                    progress_dialog = new ProgressDialog(context);
                    progress_dialog.setMessage("Signing in....");

                    if (Build.VERSION.SDK_INT < 23 || checkPermission())
                        signInWithGplus();

                } else {
                    AppGlobal.showToast(this, getResources().getString(R.string
                            .network_not_available), 2);
                }

                break;

            case R.id.btnForgotPassword:
                if (TextUtils.isEmpty(userName.getText().toString())) {
                    Utility.alertDialog(this, "Enter Email");
                    return;
                }
                if (isValidMail(userName.getText().toString().trim())) {


                    if (checkAsSmsReadPermission()) {
                        Random rnd = new Random();
                        int n = 100000 + rnd.nextInt(900000);
                        Utility.storeString(Constants.OTP, n + "", this);
                        callSendOtpApi(userName.getText().toString().trim(), n + "");
                    }


                } else {
                    Utility.alertDialog(context, "Email is Not Valid");
                }

                break;

        }

    }


    private final String[] PermissionsSms =
            {
                    "android.permission.READ_SMS",

            };
    private final int RequestSmsPermissionId = 1;

    private boolean checkAsSmsReadPermission() {
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_SMS")) {
                RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);


                Snackbar.make(mainLayout, "Read OTP  needd sms permission", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(LoginActivity.this, PermissionsSms, RequestSmsPermissionId);
                            }
                        }).show();


            } else {
                ActivityCompat.requestPermissions(LoginActivity.this, PermissionsSms, RequestSmsPermissionId);

            }


            return false;

        }
        return true;
    }


    private void callSendOtpApi(String email, String otp) {

        if (AppGlobal.isNetwork(this.getApplicationContext())) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    String SUCESSS = "1", FAILURE = "0";
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("success");
                        String message = jsonObject.getString("message");
                        if (status.trim().equals(FAILURE)) {
                            Utility.alertDialog(context, "Username does not exists");


                        } else {
                            String id = jsonObject.getJSONArray("data").getJSONObject(0).getString("id");

                            showEnterOtpDialog(id);

                        }
                    } catch (Exception e) {

                    }


                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(context, LoginActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {
                    loading = status;
                }

                @Override
                public boolean getLoading() {
                    return loading;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<String, String>();

            parameter.put("email", email);
            parameter.put("otp", otp);
            //parameter.put("mobileno", "9035308757");


            connectWebService.stringPostRequest(Config.SEND_OTP, LoginActivity.this, parameter);


        } else {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }

    }


    Dialog otpDialog;
    private void showEnterOtpDialog(final String id) {
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

            IncomingSms.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String message) {

                    //Do whatever you want with the code here
                    String otp = Utility.getString(Constants.OTP, LoginActivity.this);

                    if (message.trim().equals(otp)) {
                        otpDialog.dismiss();
                        otpDialog = null;
                        showResetPasswordDialog(id);
                    }
                }
            });

        }


        otpDialog = new Dialog(this);
        //zoomImage_dialog.getWindow().getAttributes().windowAnimations = android.R.style.Theme_Dialog_Translucent;
        otpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpDialog.setContentView(R.layout.otp_dialog);
        otpDialog.setCancelable(false);
        otpDialog.getWindow().setGravity(Gravity.CENTER);
        final Window window = otpDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        otpDialog.show();

        otpDialog.findViewById(R.id.btCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpDialog.dismiss();

            }
        });

        otpDialog.findViewById(R.id.btResendPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpDialog.dismiss();


                Random rnd = new Random();
                int n = 100000 + rnd.nextInt(900000);
                Utility.storeString(Constants.OTP, n + "", LoginActivity.this);
                callSendOtpApi(userName.getText().toString().trim(), n + "");


            }
        });

        otpDialog.findViewById(R.id.btSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText) otpDialog.findViewById(R.id.etOtp);
                if (TextUtils.isEmpty(editText.getText().toString().trim()) || editText.getText().toString().length() < 6) {
                    Utility.alertDialog(LoginActivity.this, "Enter valid OTP");
                } else {
                    String otp = Utility.getString(Constants.OTP, LoginActivity.this);
                    if (editText.getText().toString().trim().equals(otp)) {
                        otpDialog.dismiss();
                        otpDialog = null;
                        showResetPasswordDialog(id);
                    } else {
                        Utility.alertDialog(LoginActivity.this, "Enter valid OTP");

                    }
                }

            }
        });


    }


    /*private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("otp")) {
                final String message = intent.getStringExtra("message");
                //Do whatever you want with the code here
                String otp = Utility.getString(Constants.OTP, LoginActivity.this);

                if (message.equals(otp)) {
                    otpDialog.dismiss();
                    otpDialog = null;
                    showResetPasswordDialog(userName.getText().toString());
                }
            }
        }
    };

*/
    Dialog resetPasswordDialog;

    private void showResetPasswordDialog(final String id) {
        resetPasswordDialog = new Dialog(this);
        //zoomImage_dialog.getWindow().getAttributes().windowAnimations = android.R.style.Theme_Dialog_Translucent;
        resetPasswordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        resetPasswordDialog.setContentView(R.layout.reset_dialog);
        resetPasswordDialog.setCancelable(false);
        resetPasswordDialog.getWindow().setGravity(Gravity.CENTER);
        final Window window = resetPasswordDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        resetPasswordDialog.show();

        resetPasswordDialog.findViewById(R.id.btCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPasswordDialog.dismiss();
                resetPasswordDialog = null;

            }
        });

        resetPasswordDialog.findViewById(R.id.btSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText password = (EditText) resetPasswordDialog.findViewById(R.id.etNewPassword);
                EditText retypePassword = (EditText) resetPasswordDialog.findViewById(R.id.etRetypePassword);

                if (TextUtils.isEmpty(password.getText().toString().trim())) {
                    Utility.alertDialog(LoginActivity.this, "Enter new password");
                    return;
                }

                if (TextUtils.isEmpty(retypePassword.getText().toString().trim())) {
                    Utility.alertDialog(LoginActivity.this, "Enter retype password");
                    return;
                }
                if (!password.getText().toString().trim().equals(retypePassword.getText().toString().trim())) {
                    Utility.alertDialog(LoginActivity.this, "Retype password not matching password entered");
                    return;
                }

                callResetPassword(id, password.getText().toString().trim());


            }
        });
    }

    private void callResetPassword(String id, String password) {


        if (AppGlobal.isNetwork(this.getApplicationContext())) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    String SUCESSS = "1", FAILURE = "0";
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("success");
                        String message = jsonObject.getString("message");
                        if (status.trim().equals(FAILURE)) {
                            Utility.alertDialog(context, LoginActivity.this.getResources().getString(R.string.please_try));


                        } else {
                            Utility.alertDialog(context, "Reset password success");
                            resetPasswordDialog.dismiss();
                            resetPasswordDialog = null;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(context, LoginActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {
                    loading = status;
                }

                @Override
                public boolean getLoading() {
                    return loading;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<String, String>();

            parameter.put("userid", id);
            parameter.put("password", password);


            connectWebService.stringPostRequest(Config.RESET_PASSWORD, LoginActivity.this, parameter);


        } else {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }


    }

    private final String[] PermissionsLocation =
            {
                    Manifest.permission.GET_ACCOUNTS,

            };
    private final int RequestPermissionId = 0;

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {
                RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);


                Snackbar.make(mainLayout, "Sign requeried get Account Detail", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(LoginActivity.this, PermissionsLocation, RequestPermissionId);
                            }
                        }).show();


            } else {
                ActivityCompat.requestPermissions(LoginActivity.this, PermissionsLocation, RequestPermissionId);

            }


            return false;

        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        int i;
        switch (requestCode) {

            case RequestPermissionId:

                for (i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                        break;

                    }
                }
                if (i == grantResults.length)
                    signInWithGplus();

                break;
            case RequestSmsPermissionId:

                for (i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                        break;

                    }
                }
                // if (i == grantResults.length)
                Random rnd = new Random();
                int n = 100000 + rnd.nextInt(900000);
                Utility.storeString(Constants.OTP, n + "", this);
                callSendOtpApi(userName.getText().toString().trim(), n + "");

                break;

        }
    }

    private void getForgotPassword() {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_forgot_dialog, null, false);
        final EditText edt = (EditText) v.findViewById(R.id.txtEmail);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(v);

        alert.setTitle("Forgot Password");
        alert.setPositiveButton("SEND", null);
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        alert.create();//.show();

        mAlertDialog = alert.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edt.getText().length() == 0) {
                            edt.setError("Enter email");
                            edt.requestFocus();
                        } else if (!AppGlobal.isVaildEmail(edt.getText().toString().trim().replace(" ", "%20"))) {
                            edt.setError("Enter valid email");
                            edt.requestFocus();
                        } else {
                            callForgotPassword(edt.getText().toString().trim().replace(" ", "%20"));
                        }
                    }
                });
            }
        });
        mAlertDialog.show();
    }


    private void callForgotPassword(String s) {
        if (AppGlobal.isNetwork(LoginActivity.this.getApplicationContext())) {


        } else {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }
    }

    private boolean isValidMail(String email2) {
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

    private void signInWithGplus() {
        processSignIn();
    }

     ProgressDialog pDialogGoogle;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;


            }else{
                pDialogGoogle= new ProgressDialog(this);
                pDialogGoogle.setMessage(Html.fromHtml("<font color='red'>Please wait..." +
                        "</font>"));
                pDialogGoogle.setCancelable(false);
                pDialogGoogle.show();
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void apiCallForFacebookLogin(String gender, String username, String emailId, String facebookId, String imageurl) {
        if (AppGlobal.isNetwork(this)) {
            String userName = this.userName.getText().toString();
            String password = this.password.getText().toString();
            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseLoginData(result);

                }

                @Override
                public void onServerError() {
                    try {
                        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
                            //Logged in so show the login button
                            LoginManager.getInstance().logOut();


                        }
                    } catch (Exception e) {

                    }

                    Utility.alertDialog(context, LoginActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {
                    loading = status;
                }

                @Override
                public boolean getLoading() {
                    return loading;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("fbid", facebookId);

            parameter.put("fname", username);
            parameter.put("address", "");
            parameter.put("gender", gender);
            parameter.put("email", emailId);
            parameter.put("mobileno", "");
            parameter.put("image", imageurl);
            parameter.put("gcmid", gcmRegId);
            parameter.put("deviceid", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));


            connectWebService.stringPostRequest(Config.SOCIAL_LOGIN, LoginActivity.this, parameter);


        } else {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }
    }

    private void apiCallForGooglePlusLogin(String gender, String username, String emailId, String googlePlusId, String imageurl) {
        if (AppGlobal.isNetwork(this.getApplicationContext())) {
            String userName = this.userName.getText().toString();
            String password = this.password.getText().toString();
            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseLoginData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(context, LoginActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {
                    loading = status;
                }

                @Override
                public boolean getLoading() {
                    return loading;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("gid", googlePlusId);

            parameter.put("fname", username);
            parameter.put("address", "");
            if (gender.equals("0"))
                gender = "male";
            else
                gender = "female";

            parameter.put("gender", gender);
            parameter.put("email", emailId);
            parameter.put("mobileno", "");
            parameter.put("image", imageurl);
            parameter.put("gcmid", gcmRegId);
            parameter.put("deviceid", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));


            connectWebService.stringPostRequest(Config.SOCIAL_LOGIN, LoginActivity.this, parameter);


        } else {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }
    }

    private void apiCall() {
        if (AppGlobal.isNetwork(this.getApplicationContext())) {
            String userName = this.userName.getText().toString().trim();
            String password = this.password.getText().toString();
            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseLoginData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(context, LoginActivity.this.getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {
                    loading = status;
                }

                @Override
                public boolean getLoading() {
                    return loading;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("email", userName);
            parameter.put("password", password);
            parameter.put("gcmid", gcmRegId);
            parameter.put("deviceid", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));


            connectWebService.stringPostRequest(Config.LOGIN_URL, LoginActivity.this, parameter);


        } else {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }
    }


    /**
     * API to update signed in user information
     */
    private void processUserInfoI() {
        String name = "", gender = "", id = "", image = "";
        Person signedInUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if (signedInUser != null) {

            if (signedInUser.hasDisplayName()) {
                name = signedInUser.getDisplayName();
            }
            if (signedInUser.hasGender()) {
                gender = signedInUser.getGender() + "";
            }
            if (signedInUser.hasId()) {
                id = signedInUser.getId();
            }

            String userEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);


            if (signedInUser.hasImage()) {
                image = signedInUser.getImage().getUrl();
                // default size is 50x50 in pixels.changes it to desired size
                int profilePicRequestSize = 250;

                image = image.substring(0,
                        image.length() - 2) + profilePicRequestSize;

            }
            apiCallForGooglePlusLogin(gender, name, userEmail, id, image);


        }
    }


    private class GCMRegistrationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
            }
            try {
                gcmRegId = gcm.register(getResources().getString(R.string.sender_id));
                Log.e("Register Successfully", "done");
                Log.e("Device Id is:", "" + gcmRegId);
                AppConstant.gcmid = gcmRegId;
            } catch (IOException e) {
                Log.e("exception", e.getMessage());
            }

            return gcmRegId;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            AppGlobal.setStringPreference(LoginActivity.this.getApplicationContext(), gcmRegId, AppConstant.GCMID);
            AppConstant.gcmid = gcmRegId;
        }
    }

    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        LoginActivity.this.startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void pharseLoginData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");
            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(context, message);
                Utility.logoutFacebook();

            } else {

                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(result, LoginResponse.class);
                Utility.storeBoolean(Constants.isLoggedIn, true, this.getApplicationContext());
                Utility.storeString(Constants.userId, loginResponse.getData().get(0).getId(), this.getApplicationContext());
                loginResponse.Serialize(this.getApplicationContext());
                Utility.storeBoolean(Constants.UPDATE_NUMBER, false, this.getApplicationContext());
                if (TextUtils.isEmpty(loginResponse.getData().get(0).getMobileno())) {
                    Intent i = new Intent(this.getApplicationContext(), UpdateNumberActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Utility.storeBoolean(Constants.UPDATE_NUMBER, true, this.getApplicationContext());
                    Intent i = new Intent(this, OneTapActivity.class);
                    startActivity(i);
                    finish();
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(context, LoginActivity.this.getResources().getString(R.string.please_try));
            Utility.logoutFacebook();
        }
    }


    /**
     * API to handler sign in of user If error occurs while connecting process
     * it in processSignInError() api
     */
    private void processSignIn() {

        if (!mGoogleApiClient.isConnecting()) {
            processSignInError();
            mSignInClicked = true;
        }
        if (mGoogleApiClient.isConnected()) {
            processUserInfoI();
        }
    }

    /**
     * API to process sign in error Handle error based on ConnectionResult
     */
    private void processSignInError() {
        Log.d("tag","processSignInError");
        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this,
                        SIGN_IN_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /**
     * Callback for GoogleApiClient connection failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {

        Log.d("tag","onConnectionFailed "+result.getErrorMessage() +" "+result.getErrorCode()
        );

        if( pDialogGoogle!=null && pDialogGoogle.isShowing())
            pDialogGoogle.dismiss();
        pDialogGoogle=null;
        Toast.makeText(this,"Failed to connect",Toast.LENGTH_SHORT);
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    ERROR_DIALOG_REQUEST_CODE).show();
            return;
        }
        if (!mIntentInProgress) {
            mConnectionResult = result;

            if (mSignInClicked) {
                processSignInError();
            }
        }

    }


    /**
     * Callback for GoogleApiClient connection success
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("tag","onConnected ");

        if( pDialogGoogle!=null && pDialogGoogle.isShowing())
            pDialogGoogle.dismiss();
        pDialogGoogle=null;
        /*Toast.makeText(getApplicationContext(), "Signed In Successfully",
                Toast.LENGTH_LONG).show();
*/
        mGoogleApiClient.clearDefaultAccountAndReconnect();


        if (mSignInClicked) {
            processUserInfoI();
        }

        mSignInClicked = false;
    }

    /**
     * Callback for suspension of current connection
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.d("tag","onConnectionSuspended " +cause);

        if( pDialogGoogle!=null && pDialogGoogle.isShowing())
            pDialogGoogle.dismiss();
        pDialogGoogle=null;
        mGoogleApiClient.connect();

    }


}
