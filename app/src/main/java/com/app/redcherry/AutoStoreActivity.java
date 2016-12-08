package com.app.redcherry;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class AutoStoreActivity extends BaseActivity
        {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


}
