package telefonica.tiws.grtu.apothem;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentSim extends Fragment {

    static boolean initTimer=false;
    static DeviceInfo deviceInfo;
    final Handler handler = new Handler();

    //TextViews
    TextView textViewSim;
    TextView textViewTelNum;
    TextView textViewSimSerial;
    TextView textViewSimImsi;
    TextView textViewSimCountry;
    TextView textViewCallsRoaming;

    TextView textViewSimOPName;
    TextView textViewMCC;
    TextView textViewMNC;
    TextView textViewVoiceName;
    TextView textViewVoiceNumber;


    public static FragmentSim newInstance() {
        FragmentSim fragment = new FragmentSim();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_sim, container, false);
        deviceInfo=new DeviceInfo(getActivity());

        fillViewInfo(rootView);
        if(!initTimer) {
            initTimer=true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    refreshTextViews();
                    handler.postDelayed(this, 3000);
                }
            });
        }
        return rootView;
    }

    //Get the TextViews for this view
    public void fillViewInfo(View thisView){
        //SIM
        textViewSim = (TextView) thisView.findViewById(R.id.simText1);
        textViewTelNum = (TextView) thisView.findViewById(R.id.simText2);
        textViewSimSerial = (TextView) thisView.findViewById(R.id.simText3);
        textViewSimImsi = (TextView) thisView.findViewById(R.id.simText4);
        textViewSimCountry = (TextView) thisView.findViewById(R.id.simText5);
        textViewCallsRoaming = (TextView) thisView.findViewById(R.id.simText6);

        //Operator
        textViewSimOPName = (TextView) thisView.findViewById(R.id.operatorText1);
        textViewMCC = (TextView) thisView.findViewById(R.id.operatorText2);
        textViewMNC = (TextView) thisView.findViewById(R.id.operatorText3);
        textViewVoiceName = (TextView) thisView.findViewById(R.id.operatorText4);
        textViewVoiceNumber = (TextView) thisView.findViewById(R.id.operatorText5);

        //Refresh values
        refreshTextViews();
    }

    //Refresh the textViews
    public void refreshTextViews() {
        textViewSim.setText(deviceInfo.getSimState());
        textViewTelNum.setText(deviceInfo.getLineNumber());
        textViewSimSerial.setText(deviceInfo.getSimSerial());
        textViewSimImsi.setText(deviceInfo.getImsi());
        textViewSimCountry.setText(deviceInfo.getSimCountry());
        textViewCallsRoaming.setText(deviceInfo.getCallsRoamingEnabled());

        textViewSimOPName.setText(deviceInfo.getSimOperatorName());
        textViewMCC.setText(deviceInfo.getMCC());
        textViewMNC.setText(deviceInfo.getMNC());
        textViewVoiceName.setText(deviceInfo.getVoiceMailName());
        textViewVoiceNumber.setText(deviceInfo.getVoiceMailNumber());
    }
}