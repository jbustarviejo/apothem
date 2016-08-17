package telefonica.tiws.grtu.apothem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

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

    public StationInfo(){
        type ="---";
        isRegistered = false;
        mnc="---";
        mcc="---";
        id_cell="---";
        area_code="---";
        power="---";
        signal=0;
        enoughInfo=true;
    }

    public StationInfo(String json){
        JSONObject jObject = null;
        try {
            jObject = new JSONObject(json);
            type = jObject.getString("type");
            mnc = jObject.getString("mnc");
            mcc = jObject.getString("mcc");
            id_cell = jObject.getString("id_cell");
            area_code = jObject.getString("area_code");
            power = jObject.getString("power");
            signal = jObject.getInt("signal");

            isRegistered = true;
            enoughInfo=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toJSON() {
     JSONObject jsonObject = new JSONObject();
     try {
         jsonObject.put("type", type);
         jsonObject.put("mnc", mnc);
         jsonObject.put("mcc", mcc);
         jsonObject.put("id_cell", id_cell);
         jsonObject.put("area_code", area_code);
         jsonObject.put("power", power);
         jsonObject.put("signal", signal);
         return jsonObject.toString();
     } catch (JSONException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return "";
     }
    }
}