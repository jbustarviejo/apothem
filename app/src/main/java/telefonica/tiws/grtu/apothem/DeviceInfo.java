package telefonica.tiws.grtu.apothem;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
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

    /* DEVICE */

    public String getManufacturer() {
        try {
            String manufacturer = Build.MANUFACTURER;
            if (manufacturer.isEmpty()) {
                manufacturer = context.getResources().getString(R.string.empty);
            }
            return manufacturer;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getBrand() {
        try {
            String brand = Build.BRAND;
            if (brand.isEmpty()) {
                brand = context.getResources().getString(R.string.empty);
            }
            return brand;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getDeviceModel() {
        try {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            if (model.isEmpty()) {
                model = context.getResources().getString(R.string.empty);
            }
            if (model.startsWith(manufacturer)) {
                return model.toUpperCase();
            }
            return model;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getPhoneType() {
        try {
            int phoneType = telephonyManager.getPhoneType();
            return getPhoneTypeString(phoneType);
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    private String getPhoneTypeString(int phoneType) {
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_GSM:
                return "GSM";
            case TelephonyManager.PHONE_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.PHONE_TYPE_SIP:
                return "SIP";
            case TelephonyManager.PHONE_TYPE_NONE:
                return "Ninguno";
            default:
                return context.getResources().getString(R.string.empty);
        }
    }

    public String getImei() {
        try {
            String imei = telephonyManager.getDeviceId();
            if (imei.isEmpty()) {
                imei = context.getResources().getString(R.string.empty);
            }
            return imei;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    /* SYSTEM */

    public String getOsVersion() {
        try {
            String osVersion = Build.VERSION.RELEASE;
            if (osVersion.isEmpty()) {
                osVersion = context.getResources().getString(R.string.empty);
            }
            return osVersion;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getBatteryLevel() {
        try {
            Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            // Error checking that probably isn't needed but I added just in case.
            if (level == -1 || scale == -1 || scale == 0) {
                return context.getResources().getString(R.string.empty);
            }

            return Math.round(((float) level / (float) scale) * 100f) + "%";
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    /* LOCATION */

    public String getLatitudeString(){
        return gpsTracker.getLatitudeToString();
    }

    public double getLatitude(){
        return gpsTracker.getLatitude();
    }

    public String getLongitudeString(){
        return gpsTracker.getLongitudeToString();
    }

    public double getLongitude(){
        return gpsTracker.getLatitude();
    }

}
