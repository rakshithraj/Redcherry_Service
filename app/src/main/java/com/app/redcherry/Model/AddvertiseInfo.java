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
 * Created by rakshith raj on 01-10-2016.
 */
public class AddvertiseInfo implements Serializable {
    public ArrayList<Addvertise> getData() {
        return data;
    }

    public void setData(ArrayList<Addvertise> data) {
        this.data = data;
    }

    ArrayList<Addvertise> data = new ArrayList<>();


    public void Serialize(Context context) {
        try {
            ContextWrapper c = new ContextWrapper(context);
            String Path;

            Path = c.getFilesDir().getAbsolutePath() + "/AddvertiseInfo.bin";


            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(new File(Path)));

            oos.writeObject(this);
            oos.flush();
            oos.close();


        } catch (Exception ex) {
            Log.d("tag",ex+"");
        }
    }

    public  AddvertiseInfo DeSerialize(Context context) {
        ContextWrapper c = new ContextWrapper(context);
        String Path;


        Path = c.getFilesDir().getAbsolutePath() + "/AddvertiseInfo.bin";


        File file = new File(Path);
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                    file));
            Object obj = ois.readObject();
            ois.close();

            return (AddvertiseInfo) obj;
        } catch (Exception ex) {
            Log.d("tag",ex+"");

        }
        return null;

    }

    public class Addvertise implements Serializable{
        String image;
        String description;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

}
