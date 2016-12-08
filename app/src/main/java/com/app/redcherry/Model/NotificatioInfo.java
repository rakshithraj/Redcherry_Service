package com.app.redcherry.Model;

import java.io.Serializable;

/**
 * Created by rakshith raj on 28-08-2016.
 */
public class NotificatioInfo  implements Serializable{

    private String id;
    private String user_id;
    private String title;
    private String message;
    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
