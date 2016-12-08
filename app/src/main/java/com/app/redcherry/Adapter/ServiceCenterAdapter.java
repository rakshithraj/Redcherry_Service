package com.app.redcherry.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.redcherry.Interface.ServiceCenterInterface;
import com.app.redcherry.Model.ServiceCenterInfo;
import com.app.redcherry.R;

import java.util.ArrayList;

/**
 * Created by rakshith raj on 05-06-2016.
 */
public class ServiceCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

      private ServiceCenterInterface serviceCenterInterface;
    private ArrayList<ServiceCenterInfo> serviceCenterList = new ArrayList<>();

    public ServiceCenterAdapter(ArrayList<ServiceCenterInfo> serviceCenterList) {
        this.serviceCenterList =serviceCenterList;
    }

    public void setOnVechicleClickListner( ServiceCenterInterface serviceCenterInterface) {
        this.serviceCenterInterface=serviceCenterInterface;
    }

    public static class ServiceCenterViewHolder extends RecyclerView.ViewHolder{
        final View v;
        final TextView branchname;
        final TextView tvDistance;
        final TextView tvAddress;
        final TextView price;
        final RatingBar ratingBar;
        public final TextView tvServiceCharge;

        public ServiceCenterViewHolder(View v){
            super(v);
            this.v=v;
                 ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
                branchname = (TextView) v.findViewById(R.id.branchname);
                tvDistance = (TextView) v.findViewById(R.id.tvDistance);
                tvAddress = (TextView) v.findViewById(R.id.tvAddress);
            tvServiceCharge = (TextView) v.findViewById(R.id.tvServiceCharge);

            price = (TextView) v.findViewById(R.id.price);

            }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.servicecenter_lis_row,parent,false);

        return new ServiceCenterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ServiceCenterInfo serviceCenterInfo = serviceCenterList.get(position);
        if(serviceCenterInfo !=null){

            ServiceCenterViewHolder serviceCenterViewHolder = (ServiceCenterViewHolder)holder;

            serviceCenterViewHolder.ratingBar.setRating(Float.parseFloat(serviceCenterInfo.getAverage()));
            serviceCenterViewHolder.branchname.setText(serviceCenterInfo.getBranchname());
            serviceCenterViewHolder.price.setText(serviceCenterInfo.getPrice());
            serviceCenterViewHolder.tvAddress.setText(serviceCenterInfo.getLocation());
            serviceCenterViewHolder.tvServiceCharge.setVisibility(View.VISIBLE);
            serviceCenterViewHolder.tvServiceCharge.setText("+ Booking charge " + serviceCenterInfo.getService_charge());

            serviceCenterViewHolder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(serviceCenterInterface!=null)
                        serviceCenterInterface.onServiceCenterClicked(serviceCenterInfo);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return serviceCenterList.size();
    }
}
