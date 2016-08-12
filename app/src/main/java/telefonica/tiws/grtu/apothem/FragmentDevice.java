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

    static boolean initTimer1=false;
    static DeviceInfo deviceInfo;
    final Handler handler = new Handler();

    //TextViews
    TextView textViewManufacturer;
    TextView textViewBrand;
    TextView textViewModel;
    TextView textViewPhoneType;
    TextView textViewImei;

    TextView textViewOSVersion;
    TextView textViewBattery;

    TextView textViewLatitude;
    TextView textViewLongitude;


    public static FragmentDevice newInstance() {
        FragmentDevice fragment = new FragmentDevice();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_device, container, false);
        deviceInfo=new DeviceInfo(getActivity());

        fillViewInfo(rootView);
        if(!initTimer1) {
            initTimer1=true;
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