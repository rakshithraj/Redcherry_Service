package com.app.redcherry.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.app.redcherry.Adapter.CancelAdapter;
import com.app.redcherry.CancelBookingActivity;
import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Interface.CancelBookingInterface;
import com.app.redcherry.Interface.ConfirmInterface;
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
 * Created by rakshith raj on 17-07-2016.
 */
public class CancelFragment extends Fragment implements CancelBookingInterface {

    private RecyclerView recyclerView;
    private Context context;
    private ArrayList<HistoryInfo> historyList = new ArrayList<>();
    private String TYPE;
    public static Fragment newInstance(ArrayList<HistoryInfo> historyList, String TYPE) {
        CancelFragment cancelFragment = new CancelFragment();
        cancelFragment.setArguments(historyList,TYPE);
        return cancelFragment;
    }

    private void setArguments(ArrayList<HistoryInfo> historyList, String TYPE) {
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
        CancelAdapter adpter = new CancelAdapter(historyList);
        adpter.setOnCancelListner(this);
        recyclerView.setAdapter(adpter);
    }

    @Override
    public void onCancelBooking(final HistoryInfo historyInfo) {

        Utility.showConfirmDialog(new ConfirmInterface(){

            @Override
            public void onConfirm() {
                displayReasonDialog(historyInfo);

            }
        },"Do you want to cancel your Booking?",getActivity());


    }


    private Dialog cancelBooking_dialog;
    private Button btContinue;
    private Button btBack;
  //  private EditText etReason;
    private RelativeLayout rlReason;
  private TextView tvReason;
    private void displayReasonDialog(final HistoryInfo historyInfo) {
        // TODO Auto-generated method stub
        if (cancelBooking_dialog != null) {
            if (cancelBooking_dialog.isShowing())
                cancelBooking_dialog.cancel();
        }
        cancelBooking_dialog = new Dialog(CancelFragment.this.getActivity());
        cancelBooking_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cancelBooking_dialog.setContentView(R.layout.enter_reason_dialog);
        intialize(cancelBooking_dialog);
        cancelBooking_dialog.setCancelable(true);
        cancelBooking_dialog.getWindow().setGravity(Gravity.CENTER);
        final Window window = cancelBooking_dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking_dialog.cancel();
            }
        });
        rlReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReasonList();
            }
        });

        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking_dialog.cancel();
                if(TextUtils.isEmpty(tvReason.getText().toString())){
                    Utility.alertDialog(CancelFragment.this.getActivity(),"Select vaild reason");
                    return;
                }
                EditText editText=(EditText)cancelBooking_dialog.findViewById(R.id.etReason);
                callCancelBookingpi(tvReason.getText().toString()+" : "+editText.getText().toString(), historyInfo);
            }
        });

        cancelBooking_dialog.show();
    }

    private void intialize(Dialog addVechicle_dialog) {
        btContinue = (Button) cancelBooking_dialog.findViewById(R.id.btContinue);
        tvReason = (TextView) cancelBooking_dialog.findViewById(R.id.tvReason);
        btBack = (Button) cancelBooking_dialog.findViewById(R.id.btBack);
        rlReason= (RelativeLayout) cancelBooking_dialog.findViewById(R.id.rlReason);
    }

    private int selectedReason = -1;
    private final String[] reasonArray = new String[]{"I changed my mind", "Booking date is wrong","Service center issues"
    ,"Other issues"};

    private void showReasonList() {

        final AlertDialog.Builder ad = new AlertDialog.Builder(this.getActivity());


        ad.setSingleChoiceItems(reasonArray, selectedReason, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                selectedReason = arg1;
                tvReason.setText(reasonArray[selectedReason]);
                arg0.dismiss();

            }
        });
        ad.show();
    }

    private void callCancelBookingpi(String reason, HistoryInfo historyInfo) {
        if (AppGlobal.isNetwork(CancelFragment.this.getActivity())) {

            ConnectWebService connectWebService = new ConnectWebService();
            connectWebService.setOnServerResponse(new ServerResponse() {
                @Override
                public void onServerResponse(String result) {
                    Log.d("tag", "result=" + result);

                    pharseCancelData(result);

                }

                @Override
                public void onServerError() {
                    Utility.alertDialog(CancelFragment.this.getActivity(), CancelFragment.this.getActivity().getResources().getString(R.string.please_try));

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


            parameter.put("userid", Utility.getString(Constants.userId, CancelFragment.this.getActivity()));
            parameter.put("book_id", historyInfo.getId());
            parameter.put("reason", reason);

            if(TYPE.equals(Constants.Service))
                parameter.put("book_type", "service");

            else
                parameter.put("book_type", "wash");

            connectWebService.stringPostRequest(Config.CANCEL_BOOKING, CancelFragment.this.getActivity(), parameter);


        } else {


            AppGlobal.showToast(CancelFragment.this.getActivity(), getResources().getString(R.string
                    .network_not_available), 2);
        }

    }

    private void pharseCancelData(String result) {

        String SUCESSS = "1", FAILURE = "0";
        try {
            JSONObject jsonObject = new JSONObject(result);
            String status = jsonObject.getString("success");
            String message = jsonObject.getString("message");

            if (status.trim().equals(FAILURE)) {
                Utility.alertDialog(CancelFragment.this.getActivity(), message);
            } else {

                Utility.alertDialog(CancelFragment.this.getActivity(), "Your booking has been cancelled,we will confirm you shortly, Thank you.");
                ((CancelBookingActivity)CancelFragment.this.getActivity()).callHistoryApi();

            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utility.alertDialog(CancelFragment.this.getActivity(), CancelFragment.this.getActivity().getResources().getString(R.string.please_try));
        }
    }
}

