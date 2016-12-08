package com.app.redcherry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.Ulility.StaticVariables;

public class CarWashHomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intialize();
    }

    private void intialize() {
        RelativeLayout rlDoorStep = (RelativeLayout) this.findViewById(R.id.rlDoorStep);
        RelativeLayout rlWorkStation = (RelativeLayout) this.findViewById(R.id.rlWorkStation);
        rlDoorStep.setOnClickListener(this);
        rlWorkStation.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()){
            case R.id.rlDoorStep:
                StaticVariables.CAR_WASH_TYPE= Constants.CAR_WASH.DOOR_STEP;
                intent = new Intent(this,DoorstepServiceCenterActivity.class);
                this.startActivity(intent);

                break;
            case R.id.rlWorkStation:
                StaticVariables.CAR_WASH_TYPE= Constants.CAR_WASH.WORK_STATION;
                intent = new Intent(this,WorkStationServiceCenterActivity.class);
                this.startActivity(intent);
                break;
        }


    }
}
