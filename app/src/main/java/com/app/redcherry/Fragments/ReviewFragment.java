package com.app.redcherry.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.redcherry.Adapter.ReviewAdapter;
import com.app.redcherry.Model.Review;
import com.app.redcherry.R;

import java.util.ArrayList;

/**
 * Created by rakshith raj on 11-06-2016.
 */
public class ReviewFragment  extends Fragment {

    private ArrayList<Review> reviewList;
    private View view;
    private Activity activity;

    public static Fragment newInstance(ArrayList<Review> reviews) {

        ReviewFragment reviewFragment= new ReviewFragment();
        reviewFragment.setReviewList(reviews);
        return reviewFragment ;
    }


    private void setReviewList(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_review, container, false);


        return view;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize();


    }

    private void initialize() {
        RecyclerView recycler_list = (RecyclerView) view.findViewById(R.id.recycler_list);

        ReviewAdapter reviewAdapter = new ReviewAdapter(reviewList);
        recycler_list.setLayoutManager(new LinearLayoutManager(activity));
        recycler_list.setAdapter(reviewAdapter);

    }

}
