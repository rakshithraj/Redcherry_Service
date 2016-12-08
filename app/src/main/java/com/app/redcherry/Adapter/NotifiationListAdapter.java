package com.app.redcherry.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.redcherry.Interface.NotificationClickListner;
import com.app.redcherry.Model.NotificatioInfo;
import com.app.redcherry.R;
import com.app.redcherry.webservice.AppController;

import java.util.ArrayList;

/**
 * Created by rakshith raj on 28-08-2016.
 */
public class NotifiationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<NotificatioInfo> data;
    private NotificationClickListner notificationClickListner;
    private ImageLoader imageLoader;
    public NotifiationListAdapter(ArrayList<NotificatioInfo> data) {
        this.data = data;
    }


    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        final View v;
        final NetworkImageView notificationImage;
        final TextView title;

        public NotificationViewHolder(View v){
            super(v);
            this.v=v;
            notificationImage =(NetworkImageView) v.findViewById(R.id.notificationImage);
            title =(TextView) v.findViewById(R.id.title);

        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_row,parent,false);

        return new NotificationViewHolder(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(imageLoader==null)
            imageLoader= AppController.getInstance().getImageLoader();
        final NotificatioInfo notificatioInfo= data.get(position);
        if(notificatioInfo!=null){

            NotificationViewHolder notificationViewHolder=(NotificationViewHolder)holder;
            notificationViewHolder.title.setText(notificatioInfo.getTitle());
            notificationViewHolder.notificationImage.setDefaultImageResId(R.mipmap.loading);
            notificationViewHolder.notificationImage.setImageUrl(notificatioInfo.getImage(),imageLoader);
            notificationViewHolder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notificationClickListner!=null)
                        notificationClickListner.onNotificationClicked(notificatioInfo);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnNotificationClickListner(NotificationClickListner notificationClickListner) {
        this.notificationClickListner = notificationClickListner;
    }
}
