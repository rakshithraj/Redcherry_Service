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
 * Created by rakshith raj on 03-06-2016.
 */
public class LoginResponse implements Serializable {

    private String success;
    private String message;
    private ArrayList<UserDetails> data = new ArrayList<>();


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<UserDetails> getData() {
        return data;
    }

    public void setData(ArrayList<UserDetails> data) {
        this.data = data;
    }




    public void Serialize(Context context) {
        try {
            ContextWrapper c = new ContextWrapper(context);
            String Path;

            Path = c.getFilesDir().getAbsolutePath() + "/LoginResponse.bin";


            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(new File(Path)));

            oos.writeObject(this);
            oos.flush();
            oos.close();


        } catch (Exception ex) {
            Log.d("tag",ex+"");
        }
    }

    public  LoginResponse DeSerialize(Context context) {
        ContextWrapper c = new ContextWrapper(context);
        String Path;


        Path = c.getFilesDir().getAbsolutePath() + "/LoginResponse.bin";


        File file = new File(Path);
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                    file));
            Object obj = ois.readObject();
            ois.close();

            return (LoginResponse) obj;
        } catch (Exception ex) {
            Log.d("tag",ex+"");

        }
        return null;

    }

}
