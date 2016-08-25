package telefonica.tiws.grtu.apothem;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FragmentConnection extends Fragment {

    static boolean initTimer=false;
    static boolean initTimerSpeedTest=false;
    static DeviceInfo deviceInfo;
    static final Handler handler = new Handler();
    static final Handler hTest = new Handler();

    //TextViews
    static TextView textViewNWType;
    static TextView textViewNWStatus;
    static TextView textViewMac;
    static TextView textViewIP;

    static TextView textViewSSID;
    static TextView textViewBSSID;
    static TextView textViewEncryption;
    static TextView textViewRSSI;
    static TextView textViewLinkSpeed;
    static TextView textViewFreq;

    static TextView textViewConnectionSpeed;
    static TextView textViewLatency;

    //Wifi layout
    static RelativeLayout wifiLayout;

    //Speed test values
    static String speedTest;
    static String latencyTest;


    public static FragmentConnection newInstance() {
        FragmentConnection fragment = new FragmentConnection();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_connection, container, false);
        deviceInfo=new DeviceInfo(getActivity());

        fillViewInfo(rootView);

        if(!initTimerSpeedTest) {
            initTimerSpeedTest = true;
            hTest.post(new Runnable() {
                @Override
                public void run() {
                    new Thread(calculateSpeedTest).start();
                    hTest.postDelayed(this, 60000);
                }
            });
        }

        if(!initTimer) {
            initTimer=true;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    refreshTextViews();
                    handler.postDelayed(this, 2000);
                }
            });
        }
        return rootView;
    }

    //Get the TextViews for this view
    public void fillViewInfo(View thisView){

        //Connection
        textViewNWType = (TextView) thisView.findViewById(R.id.connectionText1);
        textViewNWStatus = (TextView) thisView.findViewById(R.id.connectionText2);
        textViewMac= (TextView) thisView.findViewById(R.id.connectionText3);
        textViewIP = (TextView) thisView.findViewById(R.id.connectionText4);

        //Wifi
        textViewSSID = (TextView) thisView.findViewById(R.id.wifiText1);
        textViewEncryption = (TextView) thisView.findViewById(R.id.wifiText2);
        textViewBSSID = (TextView) thisView.findViewById(R.id.wifiText3);
        textViewRSSI = (TextView) thisView.findViewById(R.id.wifiText4);
        textViewLinkSpeed = (TextView) thisView.findViewById(R.id.wifiText5);
        textViewFreq = (TextView) thisView.findViewById(R.id.wifiText6);

        wifiLayout = (RelativeLayout) thisView.findViewById(R.id.relativeLayoutParent2);

        //Speed test values
        if(speedTest==null) {
            speedTest = getActivity().getResources().getString(R.string.not_available_speed);
        }
        if(latencyTest==null) {
            latencyTest = getActivity().getResources().getString(R.string.not_available_latency);
        }

        //Speed test
        textViewConnectionSpeed = (TextView) thisView.findViewById(R.id.speedText1);
        textViewLatency = (TextView) thisView.findViewById(R.id.speedText2);

        //Refresh values
        refreshTextViews();
    }

    //Refresh the textViews
    public void refreshTextViews(){
        textViewNWType.setText(deviceInfo.getNetworkType());
        textViewNWStatus.setText(deviceInfo.getNetworkStatus());
        textViewMac.setText(deviceInfo.getMAC());
        textViewIP.setText(deviceInfo.getIP());

        textViewSSID.setText(deviceInfo.getSSID());
        textViewBSSID.setText(deviceInfo.getBSSID());
        textViewEncryption.setText(deviceInfo.getNetworkEncryption());
        textViewRSSI.setText(deviceInfo.getWifiRssi());
        textViewLinkSpeed.setText(deviceInfo.getWifiLinkSpeed());
        textViewFreq.setText(deviceInfo.getWifiLinkFreq());

        if(!deviceInfo.isWifiConnected()){
            wifiLayout.setAlpha(0.5f);
        }else{
            wifiLayout.setAlpha(1);
        }

        textViewConnectionSpeed.setText(speedTest);
        textViewLatency.setText(latencyTest);
    }

    /**
     * Calculate download speed
     */
    public final Runnable calculateSpeedTest=new Runnable(){

        @Override
        public void run() {
            InputStream stream=null;
            try {
                int bytesIn=0;
                String downloadFileUrl="http://dummyimage.com/512x512/000/fff"; //Example file
                long startCon=System.currentTimeMillis();
                URL url=new URL(downloadFileUrl);
                URLConnection con=url.openConnection();
                con.setUseCaches(false);
                long connectionLatency=System.currentTimeMillis()- startCon;
                stream=con.getInputStream();

                long start=System.currentTimeMillis();
                int currentByte=0;

                while((currentByte=stream.read())!=-1){
                    bytesIn++;
                }

                long downloadTime=(System.currentTimeMillis()-start);
                //Prevent AritchmeticException
                if(downloadTime==0){
                    downloadTime=1;
                }

                long bitsPerSecond   =((bytesIn*8) / downloadTime) * 1000;
                String downloadSpeed = calculateSpeedUnits(bitsPerSecond);

                speedTest = downloadSpeed;
                if(connectionLatency<=0){
                    connectionLatency=1;
                }
                latencyTest = connectionLatency+" ms";

            } catch (Exception e) {
                speedTest = getActivity().getString(R.string.not_available_speed);
                latencyTest = getActivity().getResources().getString(R.string.not_available_latency);
            }finally{
                try {
                    if(stream!=null){
                        stream.close();
                    }
                } catch (IOException e) {
                }
            }

        }
    };

    public String calculateSpeedUnits(long bitsPerSecond){
        double kbs= Math.ceil(bitsPerSecond/1024);
        if(kbs<=1){
            return bitsPerSecond+" bps";
        }
        double mbs= Math.ceil(kbs*10/1024)/10;
        if(mbs<=1){
            return kbs+" Mbps";
        }
        double gbs= Math.ceil(mbs*10/1024)/10;
        if(gbs<=1){
            return mbs+" Mbps";
        }
        return gbs+" Gbps";
    }
}