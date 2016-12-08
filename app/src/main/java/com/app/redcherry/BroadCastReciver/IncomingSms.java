package com.app.redcherry.BroadCastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.app.redcherry.Interface.SmsListener;

/**
 * Created by rakshith raj on 08-10-2016.
 */
public class IncomingSms extends BroadcastReceiver {
    private static SmsListener mListener;

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

                    if (message.contains("Redcherry")) {
                        message=message.substring(14,21);
                        mListener.messageReceived(message);
                       /* Intent myIntent = new Intent("otp");
                        myIntent.putExtra("message", message);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);*/
                    }
                    // Show Alert

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}