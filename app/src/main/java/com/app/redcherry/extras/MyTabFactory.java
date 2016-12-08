package com.redcherry.app.redcherry.extras;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

class MyTabFactory implements TabContentFactory {

    private final Context mContext;

    public MyTabFactory(Context context, String tag, int icon) {
        mContext = context;
    }

    public View createTabContent(String tag) {
        View v = new View(mContext);
        v.setMinimumWidth(0);
        v.setMinimumHeight(0);
        return v;
    }

    public View createTabContent(String tag, int icon) {
        View v = new View(mContext);
        v.setMinimumWidth(0);
        v.setMinimumHeight(0);
        return v;
    }
}
