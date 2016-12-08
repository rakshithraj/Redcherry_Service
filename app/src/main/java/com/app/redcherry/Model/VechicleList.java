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
 * Created by rakshith raj on 04-06-2016.
 */
public class VechicleList implements Serializable {


    private ArrayList<VechicleDetails> Bike = new ArrayList<>();
    private ArrayList<VechicleDetails> Car = new ArrayList<>();

    public void Serialize(Context context) {
        try {
            ContextWrapper c = new ContextWrapper(context);
            String Path;

            Path = c.getFilesDir().getAbsolutePath() + "/VechicleList.bin";


            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(new File(Path)));

            oos.writeObject(this);
            oos.flush();
            oos.close();


        } catch (Exception ex) {
            Log.d("tag",ex+"");
        }
    }


    public static VechicleList DeSerialize(Context context) {
        ContextWrapper c = new ContextWrapper(context);
        String Path;


        Path = c.getFilesDir().getAbsolutePath() + "/VechicleList.bin";


        File file = new File(Path);
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                    file));
            Object obj = ois.readObject();
            ois.close();

            return (VechicleList) obj;
        } catch (Exception ex) {
            Log.d("tag",ex+"");

        }
        return null;

    }

    public ArrayList<VechicleDetails> getBike() {
        return Bike;
    }

    public void setBike(ArrayList<VechicleDetails> bike) {
        Bike = bike;
    }

    public ArrayList<VechicleDetails> getCar() {
        return Car;
    }

    public void setCar(ArrayList<VechicleDetails> car) {
        Car = car;
    }
}
