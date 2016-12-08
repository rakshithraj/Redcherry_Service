package com.redcherry.app.redcherry.extras;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

final class Utils {
    /**
     * Take screenshot of the activity including the action bar
     *
     * @param activity
     * @return The screenshot of the activity including the action bar
     */
    public static Bitmap takeScreenshot(Activity activity) {
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setDrawingCacheEnabled(true);
        decorChild.buildDrawingCache();
        Bitmap drawingCache = decorChild.getDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(drawingCache);
        decorChild.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * Print hash key
     */
    public static void printHashKey(Context context) {
        try {
            String TAG = "com.sromku.simple.fb.example";
            PackageInfo info = context.getPackageManager().getPackageInfo(TAG, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d(TAG, "keyHash: " + keyHash);
            }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     * Update language
     *
     * @param code The language code. Like: en, cz, iw, ...
     */
    public static void updateLanguage(Context context, String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    /**
     * Build alert dialog with properties and data
     *
     * @param pairs
     * @return {@link AlertDialog}
     */
    public static AlertDialog buildProfileResultDialog(Activity activity, Pair<String, String>... pairs) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Pair<String, String> pair : pairs) {
            stringBuilder.append(String.format("<h3>%s</h3>", pair.first));
            stringBuilder.append(String.valueOf(pair.second));
            stringBuilder.append("<br><br>");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(Html.fromHtml(stringBuilder.toString())).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        return builder.create();
    }

    public static String toHtml(Object object) {
        StringBuilder stringBuilder = new StringBuilder(256);
        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object val = field.get(object);
                stringBuilder.append("<b>");
                stringBuilder.append(field.getName().substring(1, field.getName().length()));
                stringBuilder.append(": ");
                stringBuilder.append("</b>");
                stringBuilder.append(val);
                stringBuilder.append("<br>");
            }
        } catch (Exception e) {
            // Do nothing
        }
        return stringBuilder.toString();
    }

    public static byte[] getSamleVideo(Context context, String assetFile) {
        try {
            InputStream inputStream = context.getAssets().open(assetFile);
            return getBytes(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getBytes(InputStream input) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        return output.toByteArray();
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static boolean exists(String URLName) {

        try {
            //if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.HONEYCOMB){
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                    .penaltyLog().penaltyDeath().build());
            // }else{
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build());

            //}


            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            // HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con = (HttpURLConnection) new URL(
                    URLName.replaceAll(" ", "%20")).openConnection();
            con.setRequestMethod("HEAD");
            // Log.e(TAG,"response code : "+con.getResponseCode());
            return con.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFilename(String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }

    public static String getAlbumname(String url, String filename) {
        Log.e("TAG", "Filename : " + url + " " + filename);
        String[] filepath = url.split(filename);
        Log.e("TAG", "Filepath : " + filepath[0]);
        String[] albumname = filepath[0].split("/");
        Log.e("TAG", "Filepath : " + albumname[0]);


        return albumname[albumname.length - 1];
    }

}
