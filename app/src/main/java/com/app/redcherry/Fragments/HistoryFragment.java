package com.app.redcherry.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.app.redcherry.Adapter.HistoryAdaper;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.HistoryActivity;
import com.app.redcherry.Interface.HistoryInterface;
import com.app.redcherry.Interface.PaymentInterface;
import com.app.redcherry.Interface.ServerResponse;
import com.app.redcherry.Model.HistoryInfo;
import com.app.redcherry.R;
import com.app.redcherry.Ulility.Config;
import com.app.redcherry.Ulility.Constants;
import com.app.redcherry.Ulility.Utility;
import com.app.redcherry.webservice.ConnectWebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rakshith raj on 25-06-2016.
 */
public class HistoryFragment extends Fragment implements HistoryInterface {

    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<HistoryInfo> historyList = new ArrayList<>();
    private int TYPE;
    private final String[] rating= new String[]{"Hated it","Disliked it","It’s OK","Liked it","Loved it"};
    PaymentInterface paymentInterface;
    public static Fragment newInstance(ArrayList<HistoryInfo> historyList, int TYPE,PaymentInterface paymentInterface) {
        HistoryFragment historyFragment = new HistoryFragment();
        historyFragment.setArguments(historyList, TYPE);
        historyFragment.setPaymentInterface(paymentInterface);
        return historyFragment;
    }

    private void setPaymentInterface(PaymentInterface paymentInterface) {
        this.paymentInterface=paymentInterface;
    }

    private void setArguments(ArrayList<HistoryInfo> historyList, int TYPE) {
        this.historyList = historyList;
        this.TYPE = TYPE;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        HistoryAdaper adpter = new HistoryAdaper(historyList);
        adpter.setOnAddReview(this);
        adpter.setPaymnetInterface(paymentInterface);
        recyclerView.setAdapter(adpter);
    }

    @Override
    public void onAddview(HistoryInfo historyInfo) {
        displayAddDialog(historyInfo);
    }

    private Dialog addReview_dialog;

    private void displayAddDialog(final HistoryInfo historyInfo) {
        // TODO Auto-generated method stub
        if (addReview_dialog != null) {
            if (addReview_dialog.isShowing())
                addReview_dialog.cancel();
        }
        addReview_dialog = new Dialog(this.getActivity());
        //zoomImage_dialog.getWindow().getAttributes().windowAnimations = android.R.style.Theme_Dialog_Translucent;
        addReview_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addReview_dialog.setContentView(R.layout.add_review_dialog);
        intialize(addReview_dialog);
        addReview_dialog.setCancelable(true);
        addReview_dialog.getWindow().setGravity(Gravity.CENTER);
        final Window window = addReview_dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btCancel = (Button) addReview_dialog.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview_dialog.cancel();
            }
        });

        final RatingBar ratingBar = (RatingBar) addReview_dialog.findViewById(R.id.ratingBar);
        final EditText etComment = (EditText) addReview_dialog.findViewById(R.id.etComment);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ((TextView)addReview_dialog.findViewById(R.id.yvRating)).setText(""+HistoryFragment.this.rating[Math.round(rating)-1]);
            }
        });
        Button btAdd = (Button) addReview_dialog.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview_dialog.cancel();
                callAddReviewApi(etComment.getText().toString(), ratingBar.getRating(), historyInfo
                );
            }
        });

        addReview_dialog.show();
    }

    private void callAddReviewApi(String comment, float rating, HistoryInfo historyInfo) {

        if (AppGlobal.isNetwork(this.getActivity())) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    String SUCESSS = "1", FAILURE = "0";
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("success");
                        String message = jsonObject.getString("message");
                        Utility.alertDialog(HistoryFragment.this.getActivity(), HistoryFragment.this.getActivity().getResources().getString(R.string.review_message));
                        if (status.trim().equals(FAILURE)) {
                            //  Utility.alertDialog(HistoryFragment.this.getActivity(), message);
                        } else {
                            //  Utility.alertDialog(HistoryFragment.this.getActivity(), message);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Utility.alertDialog(HistoryFragment.this.getActivity(), HistoryFragment.this.getActivity().getResources().getString(R.string.please_try));
                    }

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(HistoryFragment.this.getActivity(), HistoryFragment.this.getActivity().getResources().getString(R.string.please_try));

                }

                @Override
                public void setLoading(boolean status) {

                }

                @Override
                public boolean getLoading() {
                    return true;
                }

                @Override
                public void parseNetworkResponse(NetworkResponse response) {

                }
            });
            Map<String, String> parameter = new HashMap<>();
            parameter.put("userid", Utility.getString(Constants.userId, this.getActivity()));

            if(TYPE== HistoryActivity.SERVICE)
            parameter.put("sid", historyInfo.getService_center_id());
             else
                parameter.put("wash_id", historyInfo.getScid());

            parameter.put("rating", rating + "");
            parameter.put("comment", comment);

            connectWebService.stringPostRequest(Config.ADD_REVIEW, this.getActivity(), parameter);


        } else {

           /* VechicleList vechicleList = VechicleList.DeSerialize(this);

            if (vechicleList != null) {
                this.vechicleList.addAll(vechicleList.getBike());
                this.vechicleList.addAll(vechicleList.getCar());
                if(!onClickList)
                    vechicleListAdapter.setVechicleService(false);
                vechicleListAdapter.notifyDataSetChanged();

            } else*/
            AppGlobal.showToast(HistoryFragment.this.getActivity(), getResources().getString(R.string
                    .network_not_available), 2);
        }


    }

    private void intialize(Dialog addVechicle_dialog) {
    }


}
