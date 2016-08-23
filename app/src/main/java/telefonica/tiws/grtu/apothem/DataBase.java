package telefonica.tiws.grtu.apothem;

import android.content.Context;
import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBase {

    private static final String POSITION_HISTORY_TABLE_NAME = "position_history";
    private static final String SETTINGS_TABLE_NAME = "settings";
    private static final String CALLS_TABLE_NAME = "calls";
    private static final int MAX_POSITION_HISTORY_ENTRIES=12*24;

    public class LocationRecord{
        Double latitude;
        Double longitude;
        Date date;

        StationInfo stationInfo;

        LocationRecord(DeviceInfo deviceInfo, Location location){
            if(location==null){
                latitude=0d;
                longitude=0d;
            }else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            date = new Date();
            stationInfo = deviceInfo.getRegisteredCellInfo();
        }

        LocationRecord(String json){
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(json);
                latitude = Double.parseDouble(jObject.getString("latitude"));
                longitude = Double.parseDouble(jObject.getString("longitude"));
                date = new Date(jObject.getString("date"));
                stationInfo = new StationInfo(jObject.getString("stationInfo"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getDate(){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
            return dateFormat.format(date);
        }

        public String toJSON(){
            JSONObject jsonObject= new JSONObject();
            try {
                jsonObject.put("latitude", latitude);
                jsonObject.put("longitude", longitude);
                jsonObject.put("date", date);
                jsonObject.put("stationInfo", stationInfo.toJSON());
                return jsonObject.toString();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "";
            }
        }

        public void save(Context context){

            List<LocationRecord> locationRecordList = getPositionsHistory(context, false);
            locationRecordList.add(this);

            int listLength=locationRecordList.size();

            if(listLength>MAX_POSITION_HISTORY_ENTRIES){
                locationRecordList=locationRecordList.subList(listLength-MAX_POSITION_HISTORY_ENTRIES,listLength);
            }

            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(POSITION_HISTORY_TABLE_NAME, Context.MODE_PRIVATE);
                for(int i=0;i<locationRecordList.size();i++) {
                    LocationRecord locationRecord = locationRecordList.get(i);
                    String jsonRecord=locationRecord.toJSON()+"\n";
                    fos.write(jsonRecord.getBytes());
                }
                fos.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public boolean emptyLatLong() {
            if((latitude==null || latitude==0.0) && (longitude==null || longitude==0.0)) {
                return true;
            }
            return false;
        }
    }

    public class SettingsRecord{
        boolean hasInitApp=false;
        boolean rateCalls=true;

        SettingsRecord(boolean hasInitApp,boolean rateCalls){
            this.hasInitApp=hasInitApp;
            this.rateCalls=rateCalls;
        }

        SettingsRecord(String json){
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(json);
                hasInitApp = Boolean.parseBoolean(jObject.getString("hasInitApp"));
                rateCalls = Boolean.parseBoolean(jObject.getString("rateCalls"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String toJSON(){
            JSONObject jsonObject= new JSONObject();
            try {
                jsonObject.put("hasInitApp", hasInitApp);
                jsonObject.put("rateCalls", rateCalls);
                return jsonObject.toString();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "";
            }
        }

        public void save(Context context){

            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(SETTINGS_TABLE_NAME, Context.MODE_PRIVATE);
                String jsonRecord=this.toJSON()+"\n";
                fos.write(jsonRecord.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public SettingsRecord getSettings(Context context){
        try {
            FileInputStream fis = context.openFileInput(SETTINGS_TABLE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line =bufferedReader.readLine();
            SettingsRecord settingsRecord = new SettingsRecord(line);
            fis.close();
            return settingsRecord;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SettingsRecord(false,true);
    }

    public class CallsRateRecord{
        String number;
        float rate;
        Date startAt;

        CallsRateRecord(String number, Date startAt,float rate){
            this.number=number;
            this.rate=rate;
            this.startAt=startAt;
        }

        CallsRateRecord(String json){
            JSONObject jObject = null;
            try {
                jObject = new JSONObject(json);
                number = jObject.getString("number");
                rate = Float.parseFloat(jObject.getString("rate"));
                startAt = new Date(jObject.getString("startAt"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String toJSON(){
            JSONObject jsonObject= new JSONObject();
            try {
                jsonObject.put("number", number);
                jsonObject.put("rate", rate);
                jsonObject.put("startAt", startAt);
                return jsonObject.toString();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "";
            }
        }

        public void save(Context context){

            List<CallsRateRecord> callsRateRecordList = getCallsRateRecords(context);
            callsRateRecordList.add(this);

            int listLength=callsRateRecordList.size();

            if(listLength>MAX_POSITION_HISTORY_ENTRIES){
                callsRateRecordList=callsRateRecordList.subList(listLength-MAX_POSITION_HISTORY_ENTRIES,listLength);
            }

            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(CALLS_TABLE_NAME, Context.MODE_PRIVATE);
                for(int i=0;i<callsRateRecordList.size();i++) {
                    CallsRateRecord callsRateRecord = callsRateRecordList.get(i);
                    String jsonRecord=callsRateRecord.toJSON()+"\n";
                    fos.write(jsonRecord.getBytes());
                }
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public boolean isEmptyRateOrDate(){
            if(rate==0 && startAt==null){
             return true;
            }
            return false;
        }

    }

    public List<LocationRecord> getPositionsHistory(Context context, boolean ignoreEmptyLatLong){
        List<LocationRecord> locationRecordsList = new ArrayList<LocationRecord>();
        try {
            FileInputStream fis = context.openFileInput(POSITION_HISTORY_TABLE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line=null;
            while((line=bufferedReader.readLine())!=null){
                LocationRecord locationRecord = new LocationRecord(line);
                if(!ignoreEmptyLatLong){
                    locationRecordsList.add(locationRecord);
                }else if(!locationRecord.emptyLatLong()){
                    locationRecordsList.add(locationRecord);
                }
            }
            fis.close();
            return locationRecordsList;
        } catch (FileNotFoundException fnfe) {
            //Nothing to do... File just have benn not created yet
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return locationRecordsList;
    }

    public List<CallsRateRecord> getCallsRateRecords(Context context){
        List<CallsRateRecord> callsRateRecords = new ArrayList<CallsRateRecord>();
        try {
            FileInputStream fis = context.openFileInput(CALLS_TABLE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line=null;
            while((line=bufferedReader.readLine())!=null){
                CallsRateRecord callsRateRecord = new CallsRateRecord(line);
                if(!callsRateRecord.isEmptyRateOrDate()){
                    callsRateRecords.add(callsRateRecord);
                }
            }
            fis.close();
            return callsRateRecords;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return callsRateRecords;
    }

    public void deleteAllTables(Context context){
        File file;
        file = new File(context.getFilesDir(), CALLS_TABLE_NAME);
        file.delete();
        file = new File(context.getFilesDir(), POSITION_HISTORY_TABLE_NAME);
        file.delete();
        file = new File(context.getFilesDir(), SETTINGS_TABLE_NAME);
        file.delete();
    }

    public void storeLocationData(Context context, DeviceInfo deviceInfo, Location location){
        try {
            LocationRecord locationRecord = new LocationRecord(deviceInfo, location);
            locationRecord.save(context);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void storeCallRate(Context context, String number, Date startAt, float rate){
        try {
            CallsRateRecord callsRateRecord = new CallsRateRecord(number, startAt, rate);
            callsRateRecord.save(context);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}