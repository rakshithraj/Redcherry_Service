package com.app.redcherry.Ulility;

import android.app.Activity;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by rakshith raj on 25-09-2016.
 */
public class TopExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Thread.UncaughtExceptionHandler defaultUEH;

   // private Activity app = null;

    public TopExceptionHandler(Activity app) {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
       // this.app = app;
    }

    public void uncaughtException(Thread t, Throwable e)
    {
        StackTraceElement[] arr = e.getStackTrace();
        String report = e.toString()+"\n\n";
        report += "--------- Stack trace ---------\n\n";
        for (int i=0; i<arr.length; i++)
        {
            report += "    "+arr[i].toString()+"\n";
        }
        report += "-------------------------------\n\n";

// If the exception was thrown in a background thread inside
// AsyncTask, then the actual exception can be found with getCause
        report += "--------- Cause ---------\n\n";
        Throwable cause = e.getCause();
        if(cause != null) {
            report += cause.toString() + "\n\n";
            arr = cause.getStackTrace();
            for (int i=0; i<arr.length; i++)
            {
                report += "    "+arr[i].toString()+"\n";
            }
        }
        report += "-------------------------------\n\n";

      /*  try {
            FileOutputStream trace = app.openFileOutput(
                    "stack.trace", Context.MODE_PRIVATE);
            trace.write(report.getBytes());
            trace.close();
            sendReport(app);
            
            
        } catch(IOException ioe) {
// ...
        }*/

        defaultUEH.uncaughtException(t, e);
    }

    private void sendReport(Activity activity) {
        String trace = "";
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(activity
                            .openFileInput("stack.trace")));
            String line;
            while((line = reader.readLine()) != null) {
                trace += line+"\n";
            }
        } catch(FileNotFoundException fnfe) {
// ...
        } catch(IOException ioe) {
// ...
        }

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        String subject = "Error report";
        String body =
                "Mail this to rakshithraj11@gmail.com: "+
                        "\n\n"+
                        trace+
                        "\n\n";

        sendIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[] {"rakshithraj11@gmail.com"});
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendIntent.setType("message/rfc822");

        activity.startActivity(
                Intent.createChooser(sendIntent, "Title:"));

       activity.deleteFile("stack.trace");

    }
}