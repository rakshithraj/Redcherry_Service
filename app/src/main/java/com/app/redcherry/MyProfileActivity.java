package com.app.redcherry;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.ImageLoader;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.CustomView.RoundedNetworkImageView;
import com.app.redcherry.Interface.ConfirmInterface;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Model.LoginResponse;
import com.app.redcherry.Ulility.AppPictureUltility;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.Ulility.LDScalingUtilities;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.webservice.AppController;
import com.app.redcherry.webservice.ConnectWebService;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MyProfileActivity extends BaseActivity implements OnClickListener {

    private Bitmap circleBitmap;
    private static final int TAKE_PHOTO = 0;
    private static final int EXISTING_PHOTO = 1;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPhoneNumber;
   // private RoundedNetworkImageView imgNetworkProfile;
    private ImageView imgProfile;
    private TextView tvSelectedDate;
    private TextView tvName;
    private TextView tvEmail;
    private String username = "";
    private String email = "";
    private String birthDate = "";
    private String mobileNumber = "";
    private String uploadImagePath = "";
    private final String addresss = "";
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intialize();
        Utility.convertIntoUsPhoneNumberFormat(etPhoneNumber);

        tvSelectedDate.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utility.hideKeyboard(tvSelectedDate);
            }
        }, 300);
    }

    @Override
    public void onDestroy() {
       // imgNetworkProfile=null;
        super.onDestroy();

    }

    private void intialize() {
        tvSelectedDate = (TextView) this.findViewById(R.id.tvSelectedDate);
        tvName = (TextView) this.findViewById(R.id.tvName);
        tvEmail = (TextView) this.findViewById(R.id.tvEmail);

        etUsername = (EditText) this.findViewById(R.id.etUsername);
        etEmail = (EditText) this.findViewById(R.id.etEmail);
        etPhoneNumber = (EditText) this.findViewById(R.id.etPhoneNumber);
        EditText etAddress = (EditText) this.findViewById(R.id.etAddress);
        LinearLayout lyCalender = (LinearLayout) this.findViewById(R.id.lyCalender);
        lyCalender.setOnClickListener(this);

        Button btSave = (Button) this.findViewById(R.id.btSave);
        Button btCancel = (Button) this.findViewById(R.id.btCancel);

        btSave.setOnClickListener(this);
        btCancel.setOnClickListener(this);


        ImageView imgTakePhoto = (ImageView) this.findViewById(R.id.imgTakePhoto);
        imgTakePhoto.setOnClickListener(this);
        ImageView imgUploadPhoto = (ImageView) this.findViewById(R.id.imgUploadPhoto);
        imgUploadPhoto.setOnClickListener(this);


        imgProfile = (ImageView) this.findViewById(R.id.imgProfile);

        LoginResponse loginResponse= new LoginResponse();
         loginResponse = loginResponse.DeSerialize(MyProfileActivity.this);
        if (loginResponse != null) {
            tvEmail.setText(loginResponse.getData().get(0).getEmail());
            etEmail.setText(loginResponse.getData().get(0).getEmail());

            tvName.setText(loginResponse.getData().get(0).getFname());
            etUsername.setText(loginResponse.getData().get(0).getFname());
            etPhoneNumber.setText(loginResponse.getData().get(0).getMobileno());
            etAddress.setText(loginResponse.getData().get(0).getAddress());

            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            String image=loginResponse.getData().get(0).getImage();

            this.findViewById(R.id.imgNetworkProfile).setVisibility(View.VISIBLE);
            imgProfile.setVisibility(View.GONE);

            if(image!=null)
                ((RoundedNetworkImageView) this.findViewById(R.id.imgNetworkProfile)).setImageUrl(image,imageLoader);



        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.lyCalender:
                showCalenderDialog();
                break;
            case R.id.btSave:
                saveDetails();
                break;
            case R.id.btCancel:
                Intent intent = new Intent(this,HomeActivity.class);
                this.startActivity(intent);
                break;
            case R.id.imgTakePhoto:
                if (Build.VERSION.SDK_INT < 23)
                    takePhoto();
                else
                    chechForPermissions(TAKE_PHOTO);


                break;
            case R.id.imgUploadPhoto:
                if (Build.VERSION.SDK_INT < 23)
                    uploadPhoto();
                else
                    chechForPermissions(EXISTING_PHOTO);

                break;

        }
    }

    private void saveDetails() {


        username = etUsername.getText().toString();
        email = etEmail.getText().toString();
        mobileNumber = etPhoneNumber.getText().toString();

        if(mobileNumber.trim().length()!=10){
            Utility.alertDialog(this,"Enter valid number");
            return;
        }
        if (!Utility.isValidMail(email)) {
            Utility.alertDialog(this, "Enter valid email address");
            return ;
        }

        if(TextUtils.isEmpty(mobileNumber)){
            Utility.alertDialog(this,"Enter mobile Number");
            return;
        }

        if(TextUtils.isEmpty(email)){
            Utility.alertDialog(this,"Enter Email address");
            return;
        }



        File file = new File(uploadImagePath);

        if (file.exists()) {


           // Bitmap ourbitmap = BitmapFactory.decodeFile(uploadImagePath);
          //  Bitmap ourbitmap = decodeFile(file);
            Bitmap ourbitmap = circleBitmap;
           // compressBitmap(ourbitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ourbitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            image = Base64.encodeToString(b, Base64.DEFAULT);


        }

        callSaveProfileApi();
    }

    private void callSaveProfileApi() {

        if (AppGlobal.isNetwork(this)) {
            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);
                    if (result == null) {
                        Utility.alertDialog(MyProfileActivity.this, MyProfileActivity.this.getResources().getString(R.string.please_try));
                    } else {
                        //Utility.alertDialog(MyProfileActivity.this, result);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String image = jsonObject.getJSONArray("data").getJSONObject(0).getString("image");

                            LoginResponse loginResponse = new   LoginResponse();
                             loginResponse = loginResponse.DeSerialize(MyProfileActivity.this);
                            if (loginResponse != null) {
                                loginResponse.getData().get(0).setEmail(email);
                                loginResponse.getData().get(0).setFname(username);
                                loginResponse.getData().get(0).setMobileno(mobileNumber);
                                loginResponse.getData().get(0).setImage(image);
                                loginResponse.getData().get(0).setAddress(addresss);

                                tvEmail.setText(loginResponse.getData().get(0).getEmail());
                                etEmail.setText(loginResponse.getData().get(0).getEmail());

                                tvName.setText(loginResponse.getData().get(0).getFname());
                                etUsername.setText(loginResponse.getData().get(0).getFname());
                                etPhoneNumber.setText(loginResponse.getData().get(0).getMobileno());


                                loginResponse.Serialize(MyProfileActivity.this);
                            }
                            showConfirmDialog(new ConfirmInterface() {
                                @Override
                                public void onConfirm() {
                                    Intent intent = new Intent(MyProfileActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            },"Profile updated successfully",MyProfileActivity.this);
                           // Utility.alertDialog(MyProfileActivity.this, "Profile updated successfully");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utility.alertDialog(MyProfileActivity.this, "Something went wrong in server");
                        }


                    }
                    //pharseData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(MyProfileActivity.this, MyProfileActivity.this.getResources().getString(R.string.please_try));
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
            parameter.put("name", username);
            parameter.put("mobileno", mobileNumber);
            parameter.put("email", email);
            parameter.put("userid", Utility.getString(Constants.userId, this));
            parameter.put("address", addresss);
            if(image!=null)
            parameter.put("image", image);


            connectWebService.stringPostRequest(Config.UPDATE_PROFILE, MyProfileActivity.this, parameter);


        } else

        {

            AppGlobal.showToast(this, getResources().getString(R.string
                    .network_not_available), 2);
        }


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

//        alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override

//            public void onClick(DialogInterface dialog, int arg1) {
//                dialog.dismiss();
//            }
//        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }
    private void showCalenderDialog() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            // when dialog box is closed, below method will be called.
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {

                if (checkValidDate(selectedYear, selectedMonth, selectedDay)) {
                    String year1 = String.valueOf(selectedYear);
                    String month1 = String.valueOf(selectedMonth + 1);
                    String day1 = String.valueOf(selectedDay);
                    birthDate = day1 + "/" + month1 + "/" + year1;

                    tvSelectedDate.setText(birthDate);
                }


            }
        };

        DatePickerDialog datePicker = new DatePickerDialog(this,
                datePickerListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        datePicker.setCancelable(false);
        datePicker.setTitle("Select the date");
        datePicker.show();
    }


    private boolean checkValidDate(int selectedYear, int selectedMonth, int selectedDay) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date
        long currentDateTime = cal.getTimeInMillis();

        cal.set(selectedYear, selectedMonth, selectedDay);


        long selectedDateTime = cal.getTimeInMillis();

        if (selectedDateTime >= currentDateTime) {
            Utility.alertDialog(this, "Select Valid Date");
            return false;
        }


        return true;
    }


    private static final int PERMISSIONS_REQUEST_CODE_TAKE_PHOTO = 10;
    private static final int PERMISSIONS_REQUEST_CODE_EXISTING_PHOTO = 20;
    private final ArrayList<String> reuestList = new ArrayList<>();

    private void chechForPermissions(int upload) {


        reuestList.clear();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            reuestList.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            if (TAKE_PHOTO == upload)
                ActivityCompat.requestPermissions(this, reuestList.toArray(new String[reuestList.size()]),
                        PERMISSIONS_REQUEST_CODE_TAKE_PHOTO);
            else
                ActivityCompat.requestPermissions(this, reuestList.toArray(new String[reuestList.size()]),
                        PERMISSIONS_REQUEST_CODE_EXISTING_PHOTO);


        } else {
            if (TAKE_PHOTO == upload)
                takePhoto();
            else
                uploadPhoto();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {


        if (requestCode == PERMISSIONS_REQUEST_CODE_TAKE_PHOTO
                ) {
            int i;
            for (i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    break;

                }
            }
            if (i == grantResults.length)
                takePhoto();
        }
        if (requestCode == PERMISSIONS_REQUEST_CODE_EXISTING_PHOTO
                ) {
            int i;
            for (i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    break;

                }
            }
            if (i == grantResults.length)
                uploadPhoto();
        }


    }

    private void uploadPhoto() {

        Intent getContentIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getContentIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(
                getContentIntent, "Options"),
                EXISTING_PHOTO);

    }


    private void takePhoto() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PHOTO);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case Crop.REQUEST_CROP:
                uploadImagePath = AppPictureUltility.getPath(this.getApplicationContext(), Crop.getOutput(intent));
                addPhotoToImageView(uploadImagePath);
                break;
            case EXISTING_PHOTO:
                uploadImagePath = null;
                Uri result = intent == null || resultCode != RESULT_OK ? null
                        : intent.getData();



                //uploadImagePath = AppPictureUltility.getPath(this.getApplicationContext(), result);
               // addPhotoToImageView(uploadImagePath);
                beginCrop(result );

                break;

            case TAKE_PHOTO:


                uploadImagePath = null;
                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, MediaStore.Images.Media.DATE_ADDED, null, "date_added ASC");
                if (cursor != null && cursor.moveToFirst()) {
                    Uri selectedImage;
                    do {
                        uploadImagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        selectedImage =Uri.fromFile(new File(uploadImagePath));

                    }
                    while (cursor.moveToNext());
                    cursor.close();

                    // imgProfile.setImageURI(selectedImage);
                    beginCrop(selectedImage );
                  //  addPhotoToImageView(uploadImagePath);

                }
                break;
        }
    }


    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void addPhotoToImageView(String mPhotoImagePath) {
        RoundedNetworkImageView  imgNetworkProfile = (RoundedNetworkImageView) this.findViewById(R.id.imgNetworkProfile);

        File imageFile = new File(mPhotoImagePath);
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile),
                    null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            options.inSampleSize = 4;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(
                        imageFile), null, options);
            } catch (FileNotFoundException f_error) {

            }
        }

        try {
            ExifInterface exif = new ExifInterface(mPhotoImagePath);
            float rotation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            float rotationInDegrees = LDScalingUtilities
                    .exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            matrix.postRotate(rotationInDegrees);

            int targetPhotoWidth = imgNetworkProfile.getWidth() - 8;
            int targetPhotoHeight = imgNetworkProfile.getHeight() - 8;
            bitmap = LDScalingUtilities.getRoundedShape(bitmap,
                    targetPhotoWidth, targetPhotoHeight, matrix);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // imgNetworkProfile.setDefaultImageResId(R.mipmap.servicecenter3);

        imgNetworkProfile.setVisibility(View.GONE);
        imgProfile.setVisibility(View.VISIBLE);

        circleBitmap=bitmap;
        imgProfile.setImageBitmap(bitmap);
    }

}
