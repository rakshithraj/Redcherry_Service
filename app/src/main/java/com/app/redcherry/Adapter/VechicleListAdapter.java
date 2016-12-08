package com.app.redcherry.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.redcherry.Interface.VechicleListInterface;
import com.app.redcherry.Model.VechicleDetails;
import com.app.redcherry.R;

import java.util.ArrayList;

/**
 * Created by rakshith raj on 03-06-2016.
 */
public class VechicleListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String PETRL="Petrol";
    public static final String CAR="Car";
    private VechicleListInterface vechicleListInterface;
    private ArrayList<VechicleDetails> vechicleList = new ArrayList<>();
    private boolean isVechicleService=true;

    public boolean isVechicleService() {
        return isVechicleService;
    }

    public void setVechicleService(boolean vechicleService) {
        isVechicleService = vechicleService;
    }


    public VechicleListAdapter(ArrayList<VechicleDetails> vechicle_list) {
        this.vechicleList=vechicle_list;
    }

    public void setOnVechicleClickListner(VechicleListInterface vechicleListInterface) {
        this.vechicleListInterface=vechicleListInterface;
    }

    public static class VechicleViewHolder extends RecyclerView.ViewHolder{
        final View v;
        final ImageView vechicleImage;
        final TextView vechicleNumber;
        final TextView vechicleName,vechicleSubname;
        final ImageView deleteVechicle,editVechicle;
        final ImageView imgRightArrow;
        public VechicleViewHolder(View v){
            super(v);
            this.v=v;
            vechicleImage =(ImageView) v.findViewById(R.id.vechicleImage);
            imgRightArrow =(ImageView) v.findViewById(R.id.imgRightArrow);
            vechicleNumber =(TextView) v.findViewById(R.id.vechicleNumber);
            vechicleName =(TextView) v.findViewById(R.id.vechicleName);
            vechicleSubname=(TextView) v.findViewById(R.id.vechicleSubname);
            deleteVechicle=(ImageView) v.findViewById(R.id.deleteVechicle);
            editVechicle=(ImageView) v.findViewById(R.id.editVechicle);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vechicle_list_row,parent,false);

        return new VechicleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final VechicleDetails vechicleDetails = vechicleList.get(position);
        if(vechicleDetails !=null){

            VechicleViewHolder  vechicleViewHolder = (VechicleViewHolder)holder;
            vechicleViewHolder.vechicleName.setText(vechicleDetails.getBrandname());
            vechicleViewHolder.vechicleSubname.setText(vechicleDetails.getSubbrand_name());
            vechicleViewHolder.vechicleNumber.setText(vechicleDetails.getVnumber());

            if(vechicleDetails.getVtype().equals(CAR)){
                if(vechicleDetails.getFueltype().equals(PETRL))
                    vechicleViewHolder.vechicleImage.setImageResource(R.mipmap.car_dselect_petrol);
                else
                    vechicleViewHolder.vechicleImage.setImageResource(R.mipmap.car_dselect_diesel);


            }else{
                if(vechicleDetails.getFueltype().equals(PETRL))
                    vechicleViewHolder.vechicleImage.setImageResource(R.mipmap.bike_dselect_petrol);
                else
                    vechicleViewHolder.vechicleImage.setImageResource(R.mipmap.bike_dselect_diesel);

            }

            vechicleViewHolder.deleteVechicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(vechicleListInterface!=null){
                        vechicleListInterface.OnVechicleDelete(vechicleDetails);
                    }
                }
            });

            vechicleViewHolder.editVechicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(vechicleListInterface!=null){
                        vechicleListInterface.OnVechicleEdit(vechicleDetails);
                    }
                }
            });

            vechicleViewHolder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(vechicleListInterface!=null){
                        vechicleListInterface.OnVechicleSelected(vechicleDetails);
                    }
                }
            });


            if(!isVechicleService)
                vechicleViewHolder.imgRightArrow.setVisibility(View.GONE);

        }


    }

    @Override
    public int getItemCount() {
        return vechicleList.size();
    }
}
