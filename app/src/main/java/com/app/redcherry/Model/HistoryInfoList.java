package com.app.redcherry.Model;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rakshith raj on 25-06-2016.
 */
public class HistoryInfoList implements Serializable{
    private ArrayList<HistoryInfo> data = new ArrayList<>();

    public   ArrayList<HistoryInfo> getData() {
        return data;
    }

    public void setData(  ArrayList<HistoryInfo> data) {
        this.data = data;
    }

    public void Serialize(Context context) {
        try {
            ContextWrapper c = new ContextWrapper(context);
            String Path;

            Path = c.getFilesDir().getAbsolutePath() + "/HistoryInfoList.bin";


            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(new File(Path)));

            oos.writeObject(this);
            oos.flush();
            oos.close();


        } catch (Exception ex) {
            Log.d("tag",ex+"");
        }
    }


    public static HistoryInfoList DeSerialize(Context context) {
        ContextWrapper c = new ContextWrapper(context);
        String Path;


        Path = c.getFilesDir().getAbsolutePath() + "/HistoryInfoList.bin";


        File file = new File(Path);
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                    file));
            Object obj = ois.readObject();
            ois.close();

            return (HistoryInfoList) obj;
        } catch (Exception ex) {
            Log.d("tag",ex+"");

        }
        return null;

    }

}
