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

public class FragmentDevice extends Fragment {

    static boolean initTimer=false;
    static DeviceInfo deviceInfo;
    static final Handler handler = new Handler();

    //TextViews
    static TextView textViewManufacturer;
    static TextView textViewBrand;
    static TextView textViewModel;
    static TextView textViewPhoneType;
    static TextView textViewImei;

    static TextView textViewOSVersion;
    static TextView textViewBattery;

    static TextView textViewLatitude;
    static TextView textViewLongitude;


    public static FragmentDevice newInstance() {
        FragmentDevice fragment = new FragmentDevice();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_device, container, false);
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
        //Device
        textViewManufacturer = (TextView) thisView.findViewById(R.id.deviceText1);
        textViewBrand = (TextView) thisView.findViewById(R.id.deviceText2);
        textViewModel = (TextView) thisView.findViewById(R.id.deviceText3);
        textViewPhoneType = (TextView) thisView.findViewById(R.id.deviceText4);
        textViewImei = (TextView) thisView.findViewById(R.id.deviceText5);

        //System
        textViewOSVersion = (TextView) thisView.findViewById(R.id.systemText1);
        textViewBattery = (TextView) thisView.findViewById(R.id.systemText2);

        //Location
        textViewLatitude = (TextView) thisView.findViewById(R.id.locationText1);
        textViewLongitude = (TextView) thisView.findViewById(R.id.locationText2);

        //Refresh values
        refreshTextViews();
    }

    //Refresh the textViews
    public void refreshTextViews(){
        textViewManufacturer.setText(deviceInfo.getManufacturer());
        textViewBrand.setText(deviceInfo.getBrand());
        textViewModel.setText(deviceInfo.getDeviceModel());
        textViewPhoneType.setText(deviceInfo.getPhoneType());
        textViewImei.setText(deviceInfo.getImei());

        textViewOSVersion.setText(deviceInfo.getOsVersion());
        textViewBattery.setText(deviceInfo.getBatteryLevel());

        textViewLatitude.setText(deviceInfo.getLatitudeString());
        textViewLongitude.setText(deviceInfo.getLongitudeString());
    }
}