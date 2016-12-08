package com.app.redcherry.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.redcherry.Model.HistoryInfo;
import com.app.redcherry.R;
import com.app.redcherry.Ulility.Constants;

import java.util.ArrayList;

/**
 * Created by rakshith raj on 24-11-2016.
 */

public class TrackBookAdapter extends RecyclerView.Adapter<TrackBookAdapter.HistoryHolder> {

private final ArrayList<HistoryInfo> historyList;
private static final String PETRL = "Petrol";

public TrackBookAdapter(ArrayList<HistoryInfo> historyList) {
        this.historyList = historyList;
        }



public static class HistoryHolder extends RecyclerView.ViewHolder {
    final View itemView;
    TextView tvBookingNumber;
    final TextView tvBookingStatus;
    final TextView tvTimming;
    final TextView vechicleName;
    final TextView vechicleNumber;
    final TextView tvType;
    final ImageView vechicleImage;
    final TextView tvBooked,tvArrived,tvCompleted,tvDelivered;

    public HistoryHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        tvBookingNumber = (TextView) itemView.findViewById(R.id.tvBookingNumber);
        tvBookingStatus = (TextView) itemView.findViewById(R.id.tvBookingStatus);
        tvTimming = (TextView) itemView.findViewById(R.id.tvTimming);
        vechicleName = (TextView) itemView.findViewById(R.id.vechicleName);
        vechicleNumber = (TextView) itemView.findViewById(R.id.vechicleNumber);
        tvBookingNumber = (TextView) itemView.findViewById(R.id.tvBookingNumber);
        tvType = (TextView) itemView.findViewById(R.id.tvType);

        vechicleImage = (ImageView) itemView.findViewById(R.id.vechicleImage);

        tvBooked = (TextView) itemView.findViewById(R.id.tvBooked);
        tvArrived = (TextView) itemView.findViewById(R.id.tvArrived);
        tvCompleted = (TextView) itemView.findViewById(R.id.tvCompleted);
        tvDelivered = (TextView) itemView.findViewById(R.id.tvDelivered);


    }
}


    @Override
    public TrackBookAdapter.HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new TrackBookAdapter.HistoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.track_booing_row, parent, false));

    }

    @Override
    public void onBindViewHolder(TrackBookAdapter.HistoryHolder holder, int position) {

        HistoryInfo historyInfo = historyList.get(position);
        if (historyInfo != null) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.tvBookingNumber.setText(historyInfo.getBooking_id());
            // holder.vechicleName.setText(historyInfo.ve());
            holder.vechicleNumber.setText(historyInfo.getVehicle_number());
            holder.tvTimming.setText(historyInfo.getBooking_date()+"  "+historyInfo.getPickuptime());
            holder.vechicleName.setText(historyInfo.getSub_brand());


            if (historyInfo.getVehicle_type().equals(Constants.Car)) {
                if (historyInfo.getFueltype().equals(PETRL))
                    holder.vechicleImage.setImageResource(R.mipmap.car_dselect_petrol);
                else
                    holder.vechicleImage.setImageResource(R.mipmap.car_dselect_diesel);


            } else {
                if (historyInfo.getFueltype().equals(PETRL))
                    holder.vechicleImage.setImageResource(R.mipmap.bike_dselect_petrol);
                else
                    holder.vechicleImage.setImageResource(R.mipmap.bike_dselect_diesel);


            }

            if (historyInfo.getCancel().equals(Constants.BOOKING_CANCEL)) {

                holder.tvBookingStatus.setText("Your order is failed");

            } else {

                holder.tvBookingStatus.setText("Your order is successfull");


            }
            if (historyInfo.getBook_type().equals(Constants.Service)) {
                //  holder.tvType.setText(Constants.Service);


            } else {
                // holder.tvType.setText(Constants.Car_wash);
                holder.tvBookingStatus.setText("Your order is " + historyInfo.getVstatus());


            }
            holder.tvType.setVisibility(View.GONE);

        } else
            holder.itemView.setVisibility(View.GONE);


        clearStatus(holder);
        if(!TextUtils.isEmpty(historyInfo.getVstatus()))
        setStatus(historyInfo.getVstatus(),holder);
        else
            setStatus(historyInfo.getVehicle_status(),holder);



    }

    private void setStatus(String status,HistoryHolder holder) {

        status=status.toLowerCase();
        switch(status){

            case "pending":
                holder.tvBooked.setBackgroundColor(Color.parseColor("#0cca2f"));

                break;
            case "arrived":
                holder.tvBooked.setBackgroundColor(Color.parseColor("#0cca2f"));
                holder.tvArrived.setBackgroundColor(Color.parseColor("#0cca2f"));

                break;
            case "completed":
                holder.tvBooked.setBackgroundColor(Color.parseColor("#0cca2f"));
                holder.tvArrived.setBackgroundColor(Color.parseColor("#0cca2f"));
                holder.tvCompleted.setBackgroundColor(Color.parseColor("#0cca2f"));

                break;
            case "deliverd":
                holder.tvBooked.setBackgroundColor(Color.parseColor("#0cca2f"));
                holder.tvArrived.setBackgroundColor(Color.parseColor("#0cca2f"));
                holder.tvCompleted.setBackgroundColor(Color.parseColor("#0cca2f"));
                holder.tvDelivered.setBackgroundColor(Color.parseColor("#0cca2f"));

                break;



        }
    }

    private void clearStatus(HistoryHolder holder) {

        holder.tvBooked.setBackgroundColor(Color.parseColor("#92efa3"));
        holder.tvArrived.setBackgroundColor(Color.parseColor("#92efa3"));
        holder.tvCompleted.setBackgroundColor(Color.parseColor("#92efa3"));
        holder.tvDelivered.setBackgroundColor(Color.parseColor("#92efa3"));


    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }



}
