package telefonica.tiws.grtu.apothem;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

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

    /*CONNECTION INFO*/

    public String getNetworkType() {
        try {
            int type = connectivityManager.getActiveNetworkInfo().getType();
            String type_s = context.getResources().getString(R.string.empty);
            switch (type) {
                case 0:
                    type_s = "Red Móvil de datos";
                    break;
                case 1:
                    type_s = "WiFi";
                    break;
                case 2:
                    type_s = "Red Móvil MMS";
                    break;
                case 3:
                    type_s = "Red Móvil SUPL";
                    break;
                case 4:
                    type_s = "Red Móvil DUN";
                    break;
                case 5:
                    type_s = "Red Móvil HiPri";
                    break;
                case 6:
                    type_s = "WiMAX";
                    break;
                case 9:
                    type_s = "Ethernet";
                    break;
                case 17:
                    type_s = "VPN";
                    break;
            }

            String subtype = connectivityManager.getActiveNetworkInfo().getSubtypeName();
            if (subtype.isEmpty() || subtype == "UNKNOWN") {
                return type_s;
            }
            return type_s + " (" + subtype + ")";
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    /*public String getNetworkExtra() {
        try {
            String extra = connectivityManager.getActiveNetworkInfo().getExtraInfo();
            if (extra.isEmpty()) {
                extra = context.getResources().getString(R.string.empty);
            }
            return extra;
        }catch (Exception e){
            return context.getResources().getString(R.string.not_allowed);
        }
    }*/

    public String getNetworkStatus() {
        try {
            String status = connectivityManager.getActiveNetworkInfo().getDetailedState().toString();
            if (status.isEmpty()) {
                status = context.getResources().getString(R.string.empty);
            }
            String status_str = status;
            switch (status) {
                case "CONNECTED":
                    status_str = "Conectado";
                    break;
                case "DISONNECTED":
                    status_str = "Desconectado";
                    break;
                case "AUTHENTICATING":
                    status_str = "Autenticando...";
                    break;
                case "CONNECTING":
                    status_str = "Conectando...";
                    break;
                case "DISCONNECTING":
                    status_str = "Desconectando...";
                    break;
                case "IDLE":
                    status_str = "Activa";
                    break;
                case "CAPTIVE_PORTAL_CHECK":
                    status_str = "Portal cautivo detectado";
                    break;
                case "BLOCKED":
                    status_str = "Bloqueado";
                    break;
                case "OBTAINING_IPADDR":
                    status_str = "Obteniendo IP...";
                    break;
                case "SCANNING":
                    status_str = "Escaneando redes...";
                    break;
                case "VERIFYING":
                    status_str = "Vericando...";
                    break;
                case "FAILED":
                    status_str = "Fallo de conexión";
                    break;
                case "SUSPENDED":
                    status_str = "Suspendida";
                    break;
            }
            return status_str;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }


    public String getMAC() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            return context.getResources().getString(R.string.not_allowed);
        }
        return context.getResources().getString(R.string.empty);
    }

    public boolean isWifiConnected() {
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;
        }
        return false;
    }

    public String getIP() {
        try {
            NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected()) {
                return getWifiIP();
            }else {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                            return ip;
                        }
                    }
                }
                return context.getResources().getString(R.string.empty);
            }
        } catch (SocketException ex) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getWifiIP() {
        try {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddressOriginal = wifiInfo.getIpAddress();
            if (ipAddressOriginal == 0) {
                return context.getResources().getString(R.string.empty);
            }
            byte[] ipAddress = BigInteger.valueOf(ipAddressOriginal).toByteArray();
            InetAddress myaddr = InetAddress.getByAddress(ipAddress);
            String hostaddr = myaddr.getHostAddress(); // numeric representation (such as "127.0.0.1")
            if (hostaddr.isEmpty()) {
                hostaddr = context.getResources().getString(R.string.empty);
            }
            return hostaddr;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    /*WIFI INFO*/

    public String getSSID() {
        try {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            if (ssid.isEmpty()) {
                ssid = context.getResources().getString(R.string.empty);
            }
            if (ssid == "0x" || ssid == "<unknown ssid>") {
                return context.getResources().getString(R.string.empty);
            }
            return ssid;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getWifiRssi() {
        try {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int rssi = wifiInfo.getRssi();
            if (rssi == -127) {
                return context.getResources().getString(R.string.empty);
            }
            return rssi + " dbm";
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getWifiLinkSpeed() {
        try {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int speed = wifiInfo.getLinkSpeed();
            if (speed == -1) {
                return context.getResources().getString(R.string.empty);
            }
            return speed + " " + wifiInfo.LINK_SPEED_UNITS;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

    public String getWifiLinkFreq() {
        try {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int freq = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                freq = wifiInfo.getFrequency();
            }else{
                return context.getResources().getString(R.string.empty);
            }
            if (freq == -1) {
                return context.getResources().getString(R.string.empty);
            }
            return freq + " " + wifiInfo.FREQUENCY_UNITS;
        } catch (Exception e) {
            return context.getResources().getString(R.string.not_allowed);
        }
    }

     /*STATIONS NETWORK*/

    public List<CellInfo> getAllCellsInfoListFromSystem() {
        try {
            List<CellInfo> cellList = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cellList = telephonyManager.getAllCellInfo();
            }else{
                return null;
            }
            return cellList;
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressLint("NewApi")
    public List<StationInfo> getAllCellsInfo() {
        try {
            List<CellInfo> cellList = this.getAllCellsInfoListFromSystem();
            if (cellList==null) {
                return null;
            }
            List<StationInfo> cellInfoContainerArrayList= new ArrayList<StationInfo>();

            for(CellInfo cellInfo : cellList) {

                StationInfo stationInfo=new StationInfo();

                try { //LTE
                    CellInfoLte cellInfoClass = (CellInfoLte) cellInfo;
                    CellIdentityLte cellIdentity = cellInfoClass.getCellIdentity();
                    CellSignalStrengthLte cellSignalStrength = cellInfoClass.getCellSignalStrength();

                    stationInfo.type="LTE";
                    int unknownParamCount=0;

                    if(cellInfoClass.isRegistered()){
                        stationInfo.isRegistered=true;
                    }

                    //MNC
                    int mnc=cellIdentity.getMnc();
                    if(mnc==Integer.MAX_VALUE||mnc==0){
                        stationInfo.mnc=context.getResources().getString(R.string.empty);
                        unknownParamCount++;
                    }else{
                        stationInfo.mnc=mnc+"";
                    }

                    //MCC
                    int mcc=cellIdentity.getMcc();
                    if(mcc==Integer.MAX_VALUE||mcc==0){
                        stationInfo.mnc=context.getResources().getString(R.string.empty);
                        unknownParamCount++;
                    }else{
                        stationInfo.mcc=mcc+"";
                    }

                    //Cell id
                    int mCid=cellIdentity.getCi();
                    if(mCid==Integer.MAX_VALUE||mCid==0){
                        stationInfo.id_cell=context.getResources().getString(R.string.empty);
                        unknownParamCount++;
                    }else{
                        stationInfo.id_cell=mCid+"";
                    }

                    //LAC - TAC
                    int lac=cellIdentity.getTac();
                    if(lac==Integer.MAX_VALUE||lac==0){
                        stationInfo.area_code=context.getResources().getString(R.string.empty);
                        unknownParamCount++;
                    }else{
                        stationInfo.area_code=lac+"";
                    }

                    //Power
                    int power=cellSignalStrength.getDbm();
                    if(power==Integer.MAX_VALUE||power==0){
                        stationInfo.power=context.getResources().getString(R.string.empty);
                        unknownParamCount++;
                    }else{
                        stationInfo.power=power+" dBm";
                    }

                    //If we don't have enough info...
                    if(unknownParamCount>2){
                        stationInfo.enoughInfo=false;
                        continue;
                    }

                    //Coverage level
                    int level=cellSignalStrength.getLevel();
                    stationInfo.signal=level;

                }catch(ClassCastException elte) {
                    //No LTE, GSM?
                    try {
                        CellInfoGsm cellInfoClass = (CellInfoGsm) cellInfo;
                        CellIdentityGsm cellIdentity = cellInfoClass.getCellIdentity();
                        CellSignalStrengthGsm cellSignalStrength = cellInfoClass.getCellSignalStrength();

                        stationInfo.type="GSM";
                        int unknownParamCount=0;

                        if(cellInfoClass.isRegistered()){
                            stationInfo.isRegistered=true;
                        }

                        //MNC
                        int mnc=cellIdentity.getMnc();
                        if(mnc==Integer.MAX_VALUE||mnc==0){
                            stationInfo.mnc=context.getResources().getString(R.string.empty);
                            unknownParamCount++;
                        }else{
                            stationInfo.mnc=mnc+"";
                        }

                        //MCC
                        int mcc=cellIdentity.getMcc();
                        if(mcc==Integer.MAX_VALUE||mcc==0){
                            stationInfo.mnc=context.getResources().getString(R.string.empty);
                            unknownParamCount++;
                        }else{
                            stationInfo.mcc=mcc+"";
                        }

                        //Cell id
                        int mCid=cellIdentity.getCid();
                        if(mCid==Integer.MAX_VALUE||mCid==0){
                            stationInfo.id_cell=context.getResources().getString(R.string.empty);
                            unknownParamCount++;
                        }else{
                            stationInfo.id_cell=mCid+"";
                        }

                        //LAC - TAC
                        int lac=cellIdentity.getLac();
                        if(lac==Integer.MAX_VALUE||lac==0){
                            stationInfo.area_code=context.getResources().getString(R.string.empty);
                            unknownParamCount++;
                        }else{
                            stationInfo.area_code=lac+"";
                        }

                        //Power
                        int power=cellSignalStrength.getDbm();
                        if(power==Integer.MAX_VALUE||power==0){
                            stationInfo.power=context.getResources().getString(R.string.empty);
                            unknownParamCount++;
                        }else{
                            stationInfo.power=power+" dBm";
                        }

                        //If we don't have enough info...
                        if(unknownParamCount>2){
                            stationInfo.enoughInfo=false;
                            continue;
                        }

                        //Coverage level
                        int level=cellSignalStrength.getLevel();
                        stationInfo.signal=level;

                    } catch (ClassCastException egsm) {
                        //No GSM, WCDMA?
                        try {
                            CellInfoWcdma cellInfoClass = (CellInfoWcdma) cellInfo;
                            CellIdentityWcdma cellIdentity = cellInfoClass.getCellIdentity();
                            CellSignalStrengthWcdma cellSignalStrength = cellInfoClass.getCellSignalStrength();

                            stationInfo.type="WCDMA";
                            int unknownParamCount=0;

                            if(cellInfoClass.isRegistered()){
                                stationInfo.isRegistered=true;
                            }

                            //MNC
                            int mnc=cellIdentity.getMnc();
                            if(mnc==Integer.MAX_VALUE||mnc==0){
                                stationInfo.mnc=context.getResources().getString(R.string.empty);
                                unknownParamCount++;
                            }else{
                                stationInfo.mnc=mnc+"";
                            }

                            //MCC
                            int mcc=cellIdentity.getMcc();
                            if(mcc==Integer.MAX_VALUE||mcc==0){
                                stationInfo.mnc=context.getResources().getString(R.string.empty);
                                unknownParamCount++;
                            }else{
                                stationInfo.mcc=mcc+"";
                            }

                            //Cell id
                            int mCid=cellIdentity.getCid();
                            if(mCid==Integer.MAX_VALUE||mCid==0){
                                stationInfo.id_cell=context.getResources().getString(R.string.empty);
                                unknownParamCount++;
                            }else{
                                stationInfo.id_cell=mCid+"";
                            }

                            //LAC - TAC
                            int lac=cellIdentity.getLac();
                            if(lac==Integer.MAX_VALUE||lac==0){
                                stationInfo.area_code=context.getResources().getString(R.string.empty);
                                unknownParamCount++;
                            }else{
                                stationInfo.area_code=lac+"";
                            }

                            //Power
                            int power=cellSignalStrength.getDbm();
                            if(power==Integer.MAX_VALUE||power==0){
                                stationInfo.power=context.getResources().getString(R.string.empty);
                                unknownParamCount++;
                            }else{
                                stationInfo.power=power+" dBm";
                            }

                            //If we don't have enough info...
                            if(unknownParamCount>2){
                                stationInfo.enoughInfo=false;
                                continue;
                            }

                            //Coverage level
                            int level=cellSignalStrength.getLevel();
                            stationInfo.signal=level;

                        } catch (ClassCastException ewcdma) {
                            //No WCDM, CDMA?
                            try {
                                CellInfoCdma cellInfoClass = (CellInfoCdma) cellInfo;
                                CellIdentityCdma cellIdentity = cellInfoClass.getCellIdentity();
                                CellSignalStrengthCdma cellSignalStrength = cellInfoClass.getCellSignalStrength();

                                stationInfo.type="CDMA";
                                int unknownParamCount=0;

                                if(cellInfoClass.isRegistered()){
                                    stationInfo.isRegistered=true;
                                }

                                //MNC
                                stationInfo.mnc=context.getResources().getString(R.string.empty);

                                //MCC
                                stationInfo.mcc=context.getResources().getString(R.string.empty);

                                //Cell id
                                int mCid=cellIdentity.getBasestationId();
                                if(mCid==Integer.MAX_VALUE||mCid==0){
                                    stationInfo.id_cell=context.getResources().getString(R.string.empty);
                                    unknownParamCount++;
                                }else{
                                    stationInfo.id_cell=mCid+"";
                                }

                                //LAC - TAC
                                stationInfo.area_code=cellIdentity.getLatitude()+" "+cellIdentity.getLongitude();

                                //Power
                                int power=cellSignalStrength.getDbm();
                                if(power==Integer.MAX_VALUE||power==0){
                                    stationInfo.power=context.getResources().getString(R.string.empty);
                                    unknownParamCount++;
                                }else{
                                    stationInfo.power=power+" dBm";
                                }

                                //If we don't have enough info...
                                if(unknownParamCount>1){
                                    stationInfo.enoughInfo=false;
                                    continue;
                                }

                                //Coverage level
                                int level=cellSignalStrength.getLevel();
                                stationInfo.signal=level;

                            } catch (ClassCastException ecdma) {
                                //No CDMA
                                stationInfo.type="¡No reconocido!";

                                try {
                                    throw new Exception("CAST EXCEPTION");
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                    }
                }
                cellInfoContainerArrayList.add(stationInfo);
            }

            return cellInfoContainerArrayList;
        } catch (Exception e) {
            return null;
        }
    }

    public class StationInfo{
        String type ="---";
        boolean isRegistered = false;
        String mnc="---";
        String mcc="---";
        String id_cell="---";
        String area_code="---";
        String power="---";
        int signal=0;
        boolean enoughInfo=true;
    }

}
