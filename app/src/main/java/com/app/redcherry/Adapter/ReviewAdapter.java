package com.app.redcherry.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.redcherry.Model.Review;
import com.app.redcherry.R;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.webservice.AppController;

import java.util.ArrayList;

/**
 * Created by rakshith raj on 12-06-2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<Review> reviewList;
    private ImageLoader imageLoader;

    public ReviewAdapter(ArrayList<Review> reviewList) {
        this.reviewList=reviewList;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        final View v;
        final NetworkImageView imgUser;
        final TextView tvReview;
        final TextView tvDate;
        final TextView tvFame;
        final RatingBar ratingBar;
        public ReviewViewHolder(View v) {
            super(v);
            this.v = v;
            imgUser=(NetworkImageView)v.findViewById(R.id.imgUser);
            tvReview=(TextView)v.findViewById(R.id.tvReview);
            tvDate=(TextView)v.findViewById(R.id.tvDate);
            tvFame=(TextView)v.findViewById(R.id.tvFame);

            ratingBar=(RatingBar)v.findViewById(R.id.ratingBar);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_review_row, parent, false);

        return new ReviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Review review = reviewList.get(position);

        if(review!=null){
            ReviewViewHolder reviewViewHolder = (ReviewViewHolder)holder;
            reviewViewHolder.tvReview.setText(review.getComment());
            reviewViewHolder.tvDate.setText(review.getDatetime());
            reviewViewHolder.tvFame.setText(Utility.capitalize(review.getFname()));
            reviewViewHolder.ratingBar.setRating(Float.parseFloat(review.getRating()));
            if(imageLoader==null)
            imageLoader = AppController.getInstance().getImageLoader();
            reviewViewHolder.imgUser.setDefaultImageResId(R.mipmap.loading);
            reviewViewHolder.imgUser.setErrorImageResId(R.mipmap.no_images);
            reviewViewHolder.imgUser.setImageUrl(review.getImage(), imageLoader);


        }


    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
