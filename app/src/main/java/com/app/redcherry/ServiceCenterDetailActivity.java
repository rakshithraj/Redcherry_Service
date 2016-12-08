package com.app.redcherry;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.redcherry.Fragments.MapFragment;
import com.app.redcherry.Fragments.ReviewFragment;
import com.app.redcherry.Fragments.ServiceDetailFragment;
import com.app.redcherry.Model.ServiceCenterInfo;
import com.app.redcherry.Ulility.TopExceptionHandler;
import com.app.redcherry.webservice.AppController;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

public class ServiceCenterDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ServiceCenterInfo serviceCenterInfo;
    private final int SERVICE_INFO = 0;
    private final int MAP = 1;
    private final int REVIEW = 2;
    private final ArrayList<String> CONTENT = new ArrayList<>();
    private ViewPager pager;
    private Button btDetail;
    private Button btMap;
    private Button btReview;
    private String price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));
        }
        setContentView(R.layout.activity_service_center_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        price=this.getIntent().getStringExtra("price");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        serviceCenterInfo = (ServiceCenterInfo) this.getIntent().getSerializableExtra("serviceCenterInfo");

        intialize();

        RelativeLayout rlAuthorize=(RelativeLayout) this.findViewById(R.id.rlAuthorize);
        if(!serviceCenterInfo.getAuthorised().equals("1"))
        rlAuthorize.setVisibility(View.GONE);

        SupportMapFragment fm = ((SupportMapFragment)  ServiceCenterDetailActivity.this
                .getSupportFragmentManager().findFragmentById(
                        R.id.mapp));


    }

    private void intialize() {
        NetworkImageView serviceCenterImage = (NetworkImageView) this.findViewById(R.id.serviceCenterImage);
        pager = (ViewPager) this.findViewById(R.id.pager);
        btDetail = (Button) this.findViewById(R.id.btDetail);
        btMap = (Button) this.findViewById(R.id.btMap);
        btReview = (Button) this.findViewById(R.id.btReview);
        btDetail.setOnClickListener(this);
        btMap.setOnClickListener(this);
        btReview.setOnClickListener(this);

        if (serviceCenterInfo.getImage() != null) {
            ImageLoader imageLoader;
            imageLoader = AppController.getInstance().getImageLoader();
            serviceCenterImage.setDefaultImageResId(R.mipmap.loading);
            serviceCenterImage.setErrorImageResId(R.mipmap.no_images);
            serviceCenterImage.setImageUrl(serviceCenterInfo.getImage(), imageLoader);
        } else
            serviceCenterImage.setImageResource(R.mipmap.no_images);


        CONTENT.add("DETAILS");
        CONTENT.add("MAP");
        CONTENT.add("REVIEWS");
        CategoryAdapter adapter = new CategoryAdapter(getSupportFragmentManager(), CONTENT);
        pager.setAdapter(adapter);


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case SERVICE_INFO:
                        btDetail.setBackgroundColor(Color.parseColor("#1E1E1E"));
                        btMap.setBackgroundColor(Color.parseColor("#B21E1E1E"));
                        btReview.setBackgroundColor(Color.parseColor("#B21E1E1E"));

                        break;
                    case MAP:
                        btDetail.setBackgroundColor(Color.parseColor("#B21E1E1E"));
                        btMap.setBackgroundColor(Color.parseColor("#1E1E1E"));
                        btReview.setBackgroundColor(Color.parseColor("#B21E1E1E"));


                        break;
                    case REVIEW:
                        btDetail.setBackgroundColor(Color.parseColor("#B21E1E1E"));
                        btMap.setBackgroundColor(Color.parseColor("#B21E1E1E"));
                        btReview.setBackgroundColor(Color.parseColor("#1E1E1E"));


                        break;
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btDetail:
                btDetail.setBackgroundColor(Color.parseColor("#1E1E1E"));
                btMap.setBackgroundColor(Color.parseColor("#B21E1E1E"));
                btReview.setBackgroundColor(Color.parseColor("#B21E1E1E"));
                pager.setCurrentItem(SERVICE_INFO);
                break;
            case R.id.btMap:
                btDetail.setBackgroundColor(Color.parseColor("#B21E1E1E"));
                btMap.setBackgroundColor(Color.parseColor("#1E1E1E"));
                btReview.setBackgroundColor(Color.parseColor("#B21E1E1E"));
                pager.setCurrentItem(MAP);

                break;
            case R.id.btReview:
                btDetail.setBackgroundColor(Color.parseColor("#B21E1E1E"));
                btMap.setBackgroundColor(Color.parseColor("#B21E1E1E"));
                btReview.setBackgroundColor(Color.parseColor("#1E1E1E"));
                pager.setCurrentItem(REVIEW);

                break;
        }
    }


    class CategoryAdapter extends FragmentPagerAdapter {
        final ArrayList<String> CONTENT;

        public CategoryAdapter(FragmentManager fm, ArrayList<String> CONTENT) {
            super(fm);
            this.CONTENT = CONTENT;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == SERVICE_INFO)
                return ServiceDetailFragment.newInstance(serviceCenterInfo,price);
            else if (position == MAP)
                return MapFragment.newInstance(serviceCenterInfo.getLat(),serviceCenterInfo.getLng(),serviceCenterInfo.getLocation());
            else
                return ReviewFragment.newInstance(serviceCenterInfo.getReviews());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT.get(position % CONTENT.size());
        }

        @Override
        public int getCount() {
            return CONTENT.size();
        }
    }


}
