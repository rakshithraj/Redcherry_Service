package com.app.redcherry.Adapter;

import android.graphics.Color;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.redcherry.Interface.HistoryInterface;
import com.app.redcherry.Interface.PaymentInterface;
import com.app.redcherry.Model.HistoryInfo;
import com.app.redcherry.R;
import com.app.redcherry.Ulility.Constants;

import java.util.ArrayList;

/**
 * Created by rakshith raj on 25-06-2016.
 */
public class HistoryAdaper extends RecyclerView.Adapter<HistoryAdaper.HistoryHolder> {

    private final ArrayList<HistoryInfo> historyList;
    private static final String PETRL = "Petrol";
    private HistoryInterface historyInterface;
    PaymentInterface paymentInterface;

    public HistoryAdaper(ArrayList<HistoryInfo> historyList) {
        this.historyList = historyList;
    }

    public void setOnAddReview(HistoryInterface historyInterface) {

        this.historyInterface = historyInterface;

    }

    public void setPaymnetInterface(PaymentInterface paymentInterface) {

        this.paymentInterface = paymentInterface;
    }

    public static class HistoryHolder extends RecyclerView.ViewHolder {
        final View itemView;
        TextView tvBookingNumber;
        final TextView tvBookingStatus;
        final TextView tvTimming;
        final TextView vechicleName;
        final TextView vechicleNumber, servicecenterName;
        final TextView tvPrice;
        final TextView tvStatus;
        final ImageView vechicleImage;
        final LinearLayout lyAddReview;
        final Button btMakePayment;

        public HistoryHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvBookingNumber = (TextView) itemView.findViewById(R.id.tvBookingNumber);
            tvBookingStatus = (TextView) itemView.findViewById(R.id.tvBookingStatus);
            tvTimming = (TextView) itemView.findViewById(R.id.tvTimming);
            vechicleName = (TextView) itemView.findViewById(R.id.vechicleName);
            vechicleNumber = (TextView) itemView.findViewById(R.id.vechicleNumber);
            servicecenterName = (TextView) itemView.findViewById(R.id.servicecenterName);

            tvBookingNumber = (TextView) itemView.findViewById(R.id.tvBookingNumber);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);

            vechicleImage = (ImageView) itemView.findViewById(R.id.vechicleImage);
            lyAddReview = (LinearLayout) itemView.findViewById(R.id.lyAddReview);
            btMakePayment = (Button) itemView.findViewById(R.id.btMakePayment);

        }
    }


    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new HistoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history, parent, false));

    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {

        HistoryInfo historyInfo = historyList.get(position);
        if (historyInfo != null) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.tvBookingNumber.setText(historyInfo.getBooking_id());
            // holder.vechicleName.setText(historyInfo.ve());
            holder.vechicleNumber.setText(historyInfo.getVehicle_number());


            holder.tvTimming.setText(historyInfo.getBooking_date() + "  " + historyInfo.getPickuptime());

            holder.vechicleName.setText(historyInfo.getSub_brand());
            holder.servicecenterName.setText(historyInfo.getBranchname());

            if (historyInfo.getCancel().equals(Constants.BOOKING_CANCEL)) {
                holder.tvStatus.setText("Cancelled");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#DD1C13"));
                holder.tvBookingStatus.setText("Your order is failed");

            } else {
                holder.tvStatus.setText("Success");
                holder.tvStatus.setBackgroundColor(Color.parseColor("#ff7701"));
                holder.tvBookingStatus.setText("Your order is successfull");


            }

            if (historyInfo.getBook_type().equals(Constants.Service)) {
                //  holder.tvPrice.setText("RS " + historyInfo.getService_charge());
                holder.tvPrice.setText("RS " + historyInfo.getFinal_service_charge());
            } else {
                //  holder.tvPrice.setText("RS " + historyInfo.getScharge());
                holder.tvPrice.setText("RS " + historyInfo.getFinal_service_charge());
                holder.tvBookingStatus.setText("Your order is " + historyInfo.getVstatus());

            }


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


            if (!TextUtils.isEmpty(historyInfo.getPaid())) {
                if (historyInfo.getPaid().equals("no")) {
                    holder.lyAddReview.setVisibility(View.GONE);
                } else {
                    holder.lyAddReview.setVisibility(View.VISIBLE);

                }

                if (historyInfo.getPay_online().equals("no")) {
                    holder.btMakePayment.setVisibility(View.GONE);
                } else {
                    if (historyInfo.getPaid().equals("no"))
                        holder.btMakePayment.setVisibility(View.VISIBLE);
                    else
                        holder.btMakePayment.setVisibility(View.GONE);

                }

            }

            holder.btMakePayment.setOnClickListener(new onClickItem(historyInfo));

            holder.lyAddReview.setOnClickListener(new onClickItem(historyInfo));

        } else
            holder.itemView.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    private class onClickItem implements View.OnClickListener {

        final HistoryInfo historyInfo;

        public onClickItem(HistoryInfo historyInfo) {
            this.historyInfo = historyInfo;
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.lyAddReview:
                    if (historyInterface != null)
                        historyInterface.onAddview(historyInfo);
                    break;
                case R.id.btMakePayment:
                    if (paymentInterface != null)
                        paymentInterface.onMakePayment(historyInfo);
                    break;
            }


        }
    }
}
