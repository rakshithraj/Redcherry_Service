package com.app.redcherry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.app.redcherry.Model.AddvertiseInfo;
import com.app.redcherry.Ulility.StaticVariables;
import com.app.redcherry.webservice.AppController;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intialize();
        setImageAdapter();
        findViewById(R.id.toolbar_icon).setVisibility(View.VISIBLE);
        findViewById(R.id.tvTitle).setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        StaticVariables.bookingDetails = null;
    }


    private void setImageAdapter() {

        ViewPager intro_images = (ViewPager) findViewById(R.id.pager_introduction);

        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);


        ArrayList<AddvertiseInfo.Addvertise> imageResource = new ArrayList<AddvertiseInfo.Addvertise>();
        AddvertiseInfo addvertiseInfo = new AddvertiseInfo();
        addvertiseInfo = addvertiseInfo.DeSerialize(this);
        if (addvertiseInfo != null) {
            imageResource.addAll(addvertiseInfo.getData());
        }
        addvertiseInfo = null;
        mAdapter = new ViewPagerAdapter(this, imageResource);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();

    }

    private void setUiPageViewController() {

        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }
        if (dots.length > 0)
            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));


    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void intialize() {
        LinearLayout lyVechicleService = (LinearLayout) this.findViewById(R.id.lyVechicleService);
        lyVechicleService.setOnClickListener(this);
        LinearLayout lyCarWash = (LinearLayout) this.findViewById(R.id.lyCarWash);
        lyCarWash.setOnClickListener(this);
        LinearLayout lyAutoStore = (LinearLayout) this.findViewById(R.id.lyAutoStore);
        lyAutoStore.setOnClickListener(this);
        LinearLayout lyRoadsideAssistance = (LinearLayout) this.findViewById(R.id.lyRoadsideAssistance);
        lyRoadsideAssistance.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        previous_sub_page = current_sub_menu;
        switch (v.getId()) {
            case R.id.lyVechicleService:
                BaseActivity.current_page = PAGE.HOME;
                BaseActivity.current_sub_menu = HOME_MENU.VECHICELE_SERVICE;
                intent = new Intent(this, VechicleServiceActivity.class);
                this.startActivity(intent);
                //finish();
                break;
            case R.id.lyCarWash:
                BaseActivity.current_page = PAGE.HOME;
                BaseActivity.current_sub_menu = HOME_MENU.CAR_WASH;
                intent = new Intent(this, CarWashHomeActivity.class);
                this.startActivity(intent);
                //finish();
                break;
            case R.id.lyAutoStore:
                BaseActivity.current_page = PAGE.HOME;
                BaseActivity.current_sub_menu = HOME_MENU.AUTO_STORE;
                intent = new Intent(this, AutoStoreActivity.class);
                this.startActivity(intent);
                // finish();
                break;
            case R.id.lyRoadsideAssistance:
                BaseActivity.current_page = PAGE.HOME;
                BaseActivity.current_sub_menu = HOME_MENU.ROADSIDE_ASSISTANCE;
                intent = new Intent(this, RoadsideAssistanceActivity.class);
                this.startActivity(intent);
                //finish();
                break;
        }
    }


    private static final int TIME_INTERVAL = 200;
    private long mBackPressed = 0;

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        if (mBackPressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else {
            Toast.makeText(this, "Double tap Back button to exit", Toast.LENGTH_SHORT).show();

            mBackPressed = System.currentTimeMillis();
        }


    }

    class ViewPagerAdapter extends PagerAdapter {

        private final Context mContext;
        private final ArrayList<AddvertiseInfo.Addvertise> mResources;

        public ViewPagerAdapter(Context mContext, ArrayList<AddvertiseInfo.Addvertise> mResources) {
            this.mContext = mContext;
            this.mResources = mResources;
        }

        @Override
        public int getCount() {
            return mResources.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item, container, false);

            NetworkImageView imageView = (NetworkImageView) itemView.findViewById(R.id.img_pager_item);
            imageView.setDefaultImageResId(R.mipmap.loading);
            imageView.setImageUrl(mResources.get(position).getImage(), AppController.getInstance().getImageLoader());
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
