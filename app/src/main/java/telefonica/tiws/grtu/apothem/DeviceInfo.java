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

    /* SIM */
    public String getSimState() {
        int simState = telephonyManager.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                return "No hay SIM";
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                return "Red bloqueada";
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                return "Se requiere el PIN";
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                return "Se requiere el PUK";
            case TelephonyManager.SIM_STATE_READY:
                return "Preparada";
            case TelephonyManager.SIM_STATE_UNKNOWN:
                return "Desconocido";
            case 6:
                return "SIM no preparada";
            case 7:
                return "Sin permisos de acceso";
            case 8:
                return "Error de lectura de SIM";
            default:
                return "Desconocido";
        }
    }

    public String getLineNumber() {
        try {
            String simNumber = telephonyManager.getLine1Number();
            if (simNumber.isEmpty()) {
                simNumber = context.getResources().getString(R.string.not_available);
            }
            return simNumber;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getSimSerial() {
        try {
            String SimSerialNumber = telephonyManager.getSimSerialNumber();
            if (SimSerialNumber.isEmpty()) {
                SimSerialNumber = context.getResources().getString(R.string.empty);
            }
            return SimSerialNumber;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getImsi() {
        try {
            String imsi = telephonyManager.getSubscriberId();
            if (imsi.isEmpty()) {
                imsi = context.getResources().getString(R.string.empty);
            }
            return imsi;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getSimCountry() {
        try {
            String countryISO = telephonyManager.getSimCountryIso().toUpperCase();
            if (countryISO.isEmpty()) {
                countryISO = context.getResources().getString(R.string.empty);
            }
            return countryISO;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    /*public String getSIMSWVersion() {
        try {
            String simSWVersion = telephonyManager.getDeviceSoftwareVersion();
            if (simSWVersion.isEmpty()) {
                simSWVersion = context.getResources().getString(R.string.empty);
            }
            return simSWVersion;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }*/

    /*public String getDataRoamingEnabled() {
        try {
            if(Settings.Global.DATA_ROAMING == "1"){
                return context.getResources().getString(R.string.yes);
            }
            return context.getResources().getString(R.string.no);
        }catch (Exception e){
            return context.getResources().getString(R.string.not_allowed);
        }
    }*/

    public String getCallsRoamingEnabled() {
        try {
            if (telephonyManager.isNetworkRoaming()) {
                return context.getResources().getString(R.string.yes);
            }
            return context.getResources().getString(R.string.no);
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    /*OPERATOR INFO*/

    public String getSimOperatorName() {
        try {
            String operatorName = telephonyManager.getSimOperatorName();
            if (operatorName.isEmpty()) {
                operatorName = context.getResources().getString(R.string.empty);
            }
            return operatorName;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getMCC() {
        try {
            String networkOperator = telephonyManager.getNetworkOperator();
            if (networkOperator.isEmpty()) {
                return context.getResources().getString(R.string.empty);
            }
            String mcc = networkOperator.substring(0, 3);
            return mcc;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getMNC() {
        try {
            String networkOperator = telephonyManager.getNetworkOperator();
            if (networkOperator.isEmpty()) {
                return context.getResources().getString(R.string.empty);
            }
            String mnc = "-";
            mnc = networkOperator.substring(3);
            return mnc;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    /*VOICE MAIL INFO*/

    public String getVoiceMailName() {
        try {
            String simVoiceName = telephonyManager.getVoiceMailAlphaTag();
            if (simVoiceName.isEmpty()) {
                simVoiceName = context.getResources().getString(R.string.empty);
            }
            return simVoiceName;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getVoiceMailNumber() {
        try {
            String simVoiceNumber = telephonyManager.getVoiceMailNumber();
            if (simVoiceNumber.isEmpty()) {
                simVoiceNumber = context.getResources().getString(R.string.empty);
            }
            return simVoiceNumber;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
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
