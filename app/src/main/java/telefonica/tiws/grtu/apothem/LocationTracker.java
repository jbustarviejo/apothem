package telefonica.tiws.grtu.apothem;

import java.util.Timer;
import java.util.TimerTask;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

public class LocationTracker {
    Timer timer1;
    LocationManager lm;
    LocationResult locationResult;
    boolean gps_enabled=false;
    boolean network_enabled=false;
    static boolean hasInitTimer=false;

    public boolean getLocation(Context context, LocationResult result)
    {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult=result;
        if(lm==null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        //exceptions will be thrown if provider is not permitted.
        try{gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}

        //don't start listeners if no provider is enabled
        if(!gps_enabled && !network_enabled)
            return false;

        try {
            if (gps_enabled)
                //noinspection MissingPermission
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5*60*1000, 10, locationListenerGps);
            if (network_enabled)
                //noinspection MissingPermission
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5*60*1000, 10, locationListenerNetwork);

            if (!hasInitTimer) {
                hasInitTimer = true;
                timer1 = new Timer();//When scheduled
                timer1.schedule(new GetLastLocation(), 5*60*1000);
            }
            new GetLastLocation(true).run(); //At start!
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            //noinspection MissingPermission
            lm.removeUpdates(this);
            //noinspection MissingPermission
            lm.removeUpdates(locationListenerNetwork);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            //noinspection MissingPermission
            lm.removeUpdates(this);
            //noinspection MissingPermission
            lm.removeUpdates(locationListenerGps);
        }
        public void onProviderDisabled(String provider) {
            lm=null;
        }
        public void onProviderEnabled(String provider) {
            lm=null;
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    class GetLastLocation extends TimerTask {
        boolean dieAfterInit;
        public GetLastLocation(boolean dieAfterInit){
            this.dieAfterInit=dieAfterInit;
        }
        public GetLastLocation(){
            this.dieAfterInit=false;
        }
        @Override
        public void run() {
            Log.d("Timer","GPS Schedulled!");
            //noinspection MissingPermission
            lm.removeUpdates(locationListenerGps);
            //noinspection MissingPermission
            lm.removeUpdates(locationListenerNetwork);

            Location net_loc=null, gps_loc=null;
            if(gps_enabled)
                //noinspection MissingPermission
                gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(network_enabled)
                //noinspection MissingPermission
                net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (gps_enabled)
                //noinspection MissingPermission
                lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListenerGps, Looper.myLooper());
            if (network_enabled)
                //noinspection MissingPermission
                lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListenerNetwork, Looper.myLooper());

            //if there are both values use the latest one
            if(gps_loc!=null && net_loc!=null){
                if(gps_loc.getTime()>net_loc.getTime())
                    locationResult.gotLocation(gps_loc);
                else
                    locationResult.gotLocation(net_loc);
                return;
            }

            if(gps_loc!=null){
                locationResult.gotLocation(gps_loc);
                return;
            }
            if(net_loc!=null){
                locationResult.gotLocation(net_loc);
                return;
            }
            locationResult.gotLocation(null);
            if(dieAfterInit){
                this.cancel();
            }
        }
    }

    public static abstract class LocationResult{
        public abstract void gotLocation(Location location);
    }
}
