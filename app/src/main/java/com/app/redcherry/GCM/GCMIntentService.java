package com.app.redcherry.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.app.redcherry.LoginActivity;
import com.app.redcherry.NotificationActivity;
import com.app.redcherry.R;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.Ulility.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * Created by Elaunch-T-2 on 3/21/2016.
 */
public class GCMIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1000;
    NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;


    public GCMIntentService() {
        super(GCMIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty()) {

            // read extras as sent from server
            String message = extras.getString("message");
            String title = extras.getString("title");
            String image = extras.getString("image");
            // String serverTime = extras.getString("timestamp");
            if (message != null && !message.equals("")) {
                sendNotification(message, title, image);
            }

        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

   /* private void sendNotification(String msg, String title, String image) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.mipmap.cherry_logo_login)
                .setContentTitle("RedCherry")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }*/


       private void sendNotification(final String msg, final String title, final String image) {

        final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final Bitmap bitmap = BitmapFactory.decodeResource(this.getApplicationContext().getResources(), R.mipmap.notification_icon);

        Bitmap remote_picture = null;

        // Create the style object with BigPictureStyle subclass.
        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setBigContentTitle(title);
        notiStyle.setSummaryText(msg);

        if (image != null) {
            try {
                remote_picture = BitmapFactory.decodeStream((InputStream) new URL(image).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            notiStyle.bigPicture(remote_picture);
        }
          /*    Intent resultIntent = new Intent(AllNewsActivity.this, AllNewsActivity.class);
              TaskStackBuilder stackBuilder = TaskStackBuilder.create(AllNewsActivity.this);
              stackBuilder.addParentStack(AllNewsActivity.class);
              stackBuilder.addNextIntent(resultIntent);
              PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);*/
        Intent intent;
        if (Utility.getBoolean(Constants.isLoggedIn, this))
            intent = new Intent(this, NotificationActivity.class);
        else
            intent = new Intent(this, LoginActivity.class);


        intent.putExtra("navigaate", true);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)

                    .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.notification_icon)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setContentTitle("Redchery")
                .setContentText(msg)
                .setStyle(notiStyle)
                .setSound(uri);


        int NOTIFICATION_ID = (msg.getBytes().length * 5) / 2;
       // int NOTIFICATION_ID = 1;
        mNotificationManager.notify(NOTIFICATION_ID, nb.build());

        int count = Utility.getInt(Constants.NOTIFICATION_COUNT, this);
        Utility.storeInt(Constants.NOTIFICATION_COUNT, count + 1, this);

        Intent intnetTrace = new Intent(Constants.ACTION_NOTIFICATION);
        sendBroadcast(intnetTrace);

    }


//    private void sendNotification(String msg) {
//
//        Log.d("TAG", "Preparing to send notification...: " + msg);
//        String title=msg;
//        String eventid="";
//        Intent intent1=null;
//        PendingIntent intent=null;
//        if(msg.contains("$")) {
//            title = msg.split("\\$")[0];
//            eventid=msg.split("\\$")[1];
//            intent1 = new Intent(this, HomeActivity.class);
//
//            AppConstant.eventId=eventid;
//            //intent1.putExtra(AppConstant.EventId, eventid+"");
//            intent1.putExtra(AppConstant.NOTIFICATION_FLAG, "1");
//            //  Log.e("TAG","Notification event id is : "+AppConstant.eventId);
//
//            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            intent = PendingIntent.getActivity(this, 0, intent1, 0);
//        }
//
//        mNotificationManager = (NotificationManager) this
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                this).setSmallIcon(R.drawable.ic_launcher)
//                .setContentTitle("RedCherry")
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
//                .setContentIntent(intent)
//                .setContentText(title);
//
//        mBuilder.setDefaults(1);
//        mBuilder.setDefaults(2);
//
//        mBuilder.setAutoCancel(true);
//
//        // mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
//        // mBuilder.setContentIntent(contentIntent);
//
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//        Log.d("TAG", "Notification sent successfully.");
//    }

}

