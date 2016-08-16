package telefonica.tiws.grtu.apothem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class FragmentCalls extends Fragment {

    static boolean initTimer=false;
    static DeviceInfo deviceInfo;
    static final Handler handler = new Handler();
    Activity thisActivity;

    //TextViews
    LinearLayout callLayoutContainer;
    LinearLayout smsLayoutContainer;

    public static FragmentCalls newInstance() {
        FragmentCalls fragment = new FragmentCalls();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_calls, container, false);
        deviceInfo=new DeviceInfo(getActivity());
        thisActivity=getActivity();

        fillViewInfo(rootView);

        if(!initTimer) {
            initTimer=true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    refreshView();
                    handler.postDelayed(this, 5000);
                }
            });
        }
        return rootView;
    }

    //Get the TextViews for this view
    public void fillViewInfo(View thisView){
        callLayoutContainer = (LinearLayout) thisView.findViewById(R.id.layoutCalls);
        smsLayoutContainer = (LinearLayout) thisView.findViewById(R.id.layoutSms);

        //Refresh values
        refreshView();
    }

    //Refresh the textViews
    public void refreshView(){
        refreshCallsLog();
        refreshSMSLog();
    }

    //Refresh SMS logs
    public void refreshSMSLog(){

        List<DeviceInfo.SMS> smsList= deviceInfo.getSmsLog();
        smsLayoutContainer.removeAllViews();

        if(smsList==null || smsList.size()==0){
            TextView textViewTitle=new TextView(thisActivity);
            TextView textViewSubtitle=new TextView(thisActivity);
            textViewTitle.setText("No hay SMS que mostrar");
            textViewTitle.setTypeface(null, Typeface.BOLD);
            textViewSubtitle.setText("Esta pantalla se actualizará si aparecen nuevos mensajes");

            smsLayoutContainer.addView(textViewTitle);
            smsLayoutContainer.addView(textViewSubtitle);
        }else{
            for (DeviceInfo.SMS sms : smsList) {

                LayoutInflater li = LayoutInflater.from(thisActivity);
                View smsContent = li.inflate(R.layout.fragment_sms, null);

                TextView smsNumberTV = (TextView) smsContent.findViewById(R.id.smsText);
                TextView smsContentTV = (TextView) smsContent.findViewById(R.id.smsDate);

                smsNumberTV.setText(sms.getNumber());
                smsContentTV.setText(sms.type + " el " + sms.getDateWithFormat()+"\nLongitud: "+sms.numChars+" caracteres");

                smsLayoutContainer.addView(smsContent);
            }
        }
    }

    //Refresh Calls logs
    public void refreshCallsLog(){

        List<DeviceInfo.Call> callList = deviceInfo.getCallLog();
        callLayoutContainer.removeAllViews();

        if(callList==null || callList.size()==0){
            TextView textViewTitle=new TextView(thisActivity);
            TextView textViewSubtitle=new TextView(thisActivity);
            textViewTitle.setText("No hay llamadas que mostrar");
            textViewTitle.setTypeface(null, Typeface.BOLD);
            textViewSubtitle.setText("Esta pantalla se actualizará si aparecen nuevas llamadas");

            callLayoutContainer.addView(textViewTitle);
            callLayoutContainer.addView(textViewSubtitle);
        }else {
            for (DeviceInfo.Call call : callList) {

                LayoutInflater li = LayoutInflater.from(thisActivity);
                View callContent = li.inflate(R.layout.fragment_call, null);

                ImageView callIcon = (ImageView) callContent.findViewById(R.id.iconCall);
                TextView callPhoneNumberTV = (TextView) callContent.findViewById(R.id.callText);
                TextView callDate = (TextView) callContent.findViewById(R.id.callDate);

                int id;
                switch (call.type){
                    case "Recibida":
                        id = R.drawable.ic_call_received;
                        callIcon.setColorFilter(thisActivity.getResources().getColor(R.color.callReceived));
                        break;
                    case "Perdida":
                        id = R.drawable.ic_call_missed;
                        callIcon.setColorFilter(thisActivity.getResources().getColor(R.color.callMissing));
                        break;
                    default:
                        id = R.drawable.ic_call_made;
                        callIcon.setColorFilter(thisActivity.getResources().getColor(R.color.callMade));
                        break;
                }

                callIcon.setImageResource(id);
                callPhoneNumberTV.setText(call.getNumber());
                callDate.setText(call.type + " el " + call.getDateWithFormat() + "\nDuración: " + call.getCallDurationWithFormat());

                callLayoutContainer.addView(callContent);
            }
        }

    }
}