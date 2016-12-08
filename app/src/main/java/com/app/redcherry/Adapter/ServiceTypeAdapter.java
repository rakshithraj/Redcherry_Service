package com.app.redcherry.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.redcherry.Interface.ServiceTypeInterface;
import com.app.redcherry.Model.ServiceTypeInfo;
import com.app.redcherry.R;

import java.util.ArrayList;

/**
 * Created by rakshith raj on 12-06-2016.
 */
class ServiceTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private final ArrayList<ServiceTypeInfo> serviceTypeList;
    private ServiceTypeInterface serviceTypeInterface;
    public ServiceTypeAdapter(ArrayList<ServiceTypeInfo> serviceTypeList) {
        this.serviceTypeList=serviceTypeList;
    }

    public void setOnServiceTypeListener(ServiceTypeInterface serviceTypeInterface) {
        this.serviceTypeInterface=serviceTypeInterface;
    }

    public static class ServiceTypeHolder extends RecyclerView.ViewHolder {
        final View v;
        final TextView tvTypeName;
        final TextView tvDescription;
        final TextView tvPrice;
        final TextView tvDays;
          public ServiceTypeHolder(View v) {
            super(v);
            this.v = v;
              tvTypeName=(TextView)v.findViewById(R.id.tvTypeName) ;
              tvDescription=(TextView)v.findViewById(R.id.tvDescription) ;
              tvPrice=(TextView)v.findViewById(R.id.tvPrice) ;
              tvDays=(TextView)v.findViewById(R.id.tvDays) ;

        }

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_servicetype_row, parent, false);

        return new ServiceTypeHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ServiceTypeInfo serviceTypeInfo = serviceTypeList.get(position);
        if(serviceTypeInfo!=null) {
            ServiceTypeHolder serviceTypeHolder = (ServiceTypeHolder) holder;
            serviceTypeHolder.tvTypeName.setText(serviceTypeInfo.getItemname());
            serviceTypeHolder.tvDescription.setText(serviceTypeInfo.getDescription());
            serviceTypeHolder.tvPrice.setText(serviceTypeInfo.getPrice());
            serviceTypeHolder.tvDays.setText(serviceTypeInfo.getDay());
            serviceTypeHolder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(serviceTypeInterface!=null)
                    serviceTypeInterface.setOnServiceTypeSelected(serviceTypeInfo);
                }
            });

        }






    }

    @Override
    public int getItemCount() {
        return serviceTypeList.size();
    }
}
