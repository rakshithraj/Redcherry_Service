package com.app.redcherry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.app.redcherry.Ulility.TopExceptionHandler;

public class OneTapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        }
        setContentView(R.layout.activity_one_tap);

        ImageView imgTap = (ImageView) findViewById(R.id.imgTap);
        imgTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OneTapActivity.this, HomeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OneTapActivity.this.finish();
                    }
                },700);

            }
        });

        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OneTapActivity.this, HomeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OneTapActivity.this.finish();
                    }
                },700);

            }
        });


    }

}
