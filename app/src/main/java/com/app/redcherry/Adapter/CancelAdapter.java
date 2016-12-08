package com.app.redcherry.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.redcherry.Interface.CancelBookingInterface;
import com.app.redcherry.Model.HistoryInfo;
import com.app.redcherry.R;
import com.app.redcherry.Ulility.Constants;

import java.util.ArrayList;

/**
 * Created by rakshith raj on 25-06-2016.
 */
public class CancelAdapter extends RecyclerView.Adapter<CancelAdapter.HistoryHolder> {

    private final ArrayList<HistoryInfo> historyList;
    private static final String PETRL = "Petrol";
    private CancelBookingInterface cancelBookingInterface;

    public CancelAdapter(ArrayList<HistoryInfo> historyList) {
        this.historyList = historyList;
    }

    public void setOnCancelListner(CancelBookingInterface cancelBookingInterface) {
        this.cancelBookingInterface = cancelBookingInterface;
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
        final LinearLayout lyCancelBooking;

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
            lyCancelBooking = (LinearLayout) itemView.findViewById(R.id.lyCancelBooking);

        }
    }


    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new HistoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cancel, parent, false));

    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {

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

        holder.lyCancelBooking.setOnClickListener(new onCancelClicked(historyList.get(position)));

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    private class onCancelClicked implements View.OnClickListener {
        final HistoryInfo historyInfo;

        public onCancelClicked(HistoryInfo historyInfo) {
            this.historyInfo = historyInfo;
        }

        @Override
        public void onClick(View v) {
            if(cancelBookingInterface!=null)
            cancelBookingInterface.onCancelBooking(historyInfo);
        }
    }
}
