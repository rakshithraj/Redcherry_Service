package com.app.redcherry;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by rakshith raj on 19-06-2016.
 */
public class VechicleListActivity extends VechicleServiceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btAddVechicle = (Button) this.findViewById(R.id.btAddVechicle);
        btAddVechicle.setVisibility(View.GONE);
        super.onClickList = false;
    }
}
