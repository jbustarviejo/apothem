package telefonica.tiws.grtu.apothem;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class DeviceInfo {

    private final Context context;
    private static TelephonyManager telephonyManager;
    private static WifiManager wifiManager;
    private static ConnectivityManager connectivityManager;
    private static GPSTracker gpsTracker;

    DeviceInfo(Context context){
        this.context = context;
        gpsTracker = new GPSTracker(context);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /*
     * LOCATION METHODS
     */

    public String getLatitudeString(boolean toString){
        return gpsTracker.getLatitudeToString();
    }

    public double getLatitude(){
        return gpsTracker.getLatitude();
    }

    public String getLongitudeString(boolean toString){
        return gpsTracker.getLongitudeToString();
    }

    public double getLongitude(){
        return gpsTracker.getLatitude();
    }

}
