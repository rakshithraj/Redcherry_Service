package com.app.redcherry;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.app.redcherry.Model.NotificatioInfo;
import com.app.redcherry.Ulility.TopExceptionHandler;
import com.app.redcherry.webservice.AppController;

public class NotificationDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        }
        setContentView(R.layout.activity_notification_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NetworkImageView image = (NetworkImageView) this.findViewById(R.id.image);
        TextView title = (TextView) this.findViewById(R.id.title);
        TextView message = (TextView) this.findViewById(R.id.message);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NotificatioInfo notificatioInfo = (NotificatioInfo) this.getIntent().getSerializableExtra("notificatioInfo");
        if(notificatioInfo!=null) {
            title.setText(notificatioInfo.getTitle());
            message.setText(notificatioInfo.getMessage());
            image.setDefaultImageResId(R.mipmap.loading);
            image.setImageUrl(notificatioInfo.getImage(), AppController.getInstance().getImageLoader());
        }
        CollapsingToolbarLayout toolbar_layout= (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout)  ;
        toolbar_layout.setExpandedTitleColor(Color.TRANSPARENT);
    }
}
