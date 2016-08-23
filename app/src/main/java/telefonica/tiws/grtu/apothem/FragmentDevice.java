package telefonica.tiws.grtu.apothem;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
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
    static final int TIME_TO_RELOAD=60*1000;

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
    static TextView textViewAccuracy;


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
                    handler.postDelayed(this, TIME_TO_RELOAD);
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
        textViewAccuracy = (TextView) thisView.findViewById(R.id.locationText3);

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

        textViewLatitude.setText(latitudeS);
        textViewLongitude.setText(longitudeS);
        textViewAccuracy.setText(accuracyS);
        locationClick();
    }

    static double latitude=0;
    static String latitudeS;
    static double longitude=0;
    static String longitudeS;
    static double accuracy=0;
    static String accuracyS;
    static LocationTracker myLocation = new LocationTracker();

    private void locationClick() {
        if(!myLocation.getLocation(getActivity(), locationResult)){
            longitudeS=getActivity().getResources().getString(R.string.empty);
            latitudeS=getActivity().getResources().getString(R.string.empty);
            accuracyS="0 m";
        }
    }

    public LocationTracker.LocationResult locationResult = new LocationTracker.LocationResult() {

        @Override
        public void gotLocation(final Location location) {
            //Got the location!
            try {
                longitude = location.getLongitude();
                if (longitude == 0) {
                    longitudeS = "---";
                } else {
                    longitudeS = longitude + "";
                }
                latitude = location.getLatitude();
                if (latitude == 0) {
                    latitudeS = "---";
                } else {
                    latitudeS = latitude + "";
                }
                accuracy = location.getAccuracy();
                accuracyS = Math.round(accuracy) + " m";
            }catch (Exception e){
                longitudeS="---";
                latitudeS="---";
                accuracyS="0 m";
            }
        }
    };
}