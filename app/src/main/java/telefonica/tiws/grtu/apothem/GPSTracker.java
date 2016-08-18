package telefonica.tiws.grtu.apothem;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class GPSTracker extends Service implements LocationListener {

    private final Context context;

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    static Location location; // Location
    static double latitude; // Latitude
    static double longitude; // Longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.context = context;
        location = getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            // Getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    try {
                        //noinspection MissingPermission
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    }catch (RuntimeException rte){
                        //Ignore
                    }
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        try {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }catch (SecurityException se){
                            se.printStackTrace();
                            return location;
                        }
                    }
                }
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        try {
                            //noinspection MissingPermission
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        }catch (RuntimeException rte){
                            //Ignore
                        }
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            try {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }catch (SecurityException se){
                                se.printStackTrace();
                                return location;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public boolean isGPSNetworkEnabled(){
        if (!isGPSEnabled && !isNetworkEnabled) {
            return false;
        }
        return true;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app.
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(GPSTracker.this);
        }
    }


    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Function to get latitude as String
     * */
    public String getLatitudeToString(){
        double latitude=this.getLatitude();
        if(latitude==0){
            return context.getResources().getString(R.string.empty);
        }
        return latitude+"";
    }


    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        // return longitude
        return longitude;
    }

    /**
     * Function to get longitude as String
     * */
    public String getLongitudeToString(){
        double longitude=this.getLongitude();
        if(longitude==0){
            return context.getResources().getString(R.string.empty);
        }
        return longitude+"";
    }

    /**
     * Function to get accuracy
     * */
    public String getAccuracy(){
        if(location != null){
            double accuracy=location.getAccuracy();
            if(accuracy!=0.0){
                return Math.round(accuracy)+" m";
            }
        }
        return context.getResources().getString(R.string.empty);
    }

    /**
     * Function to check GPS/Wi-Fi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     * */
    public void showSettingsAlert(){
       /* if(dimissGpsAlert || showedGpsAlert){
            return;
        }
        showedGpsAlert=true;*/
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Se necesita GPS");

        // Setting Dialog Message
        alertDialog.setMessage("El GPS no está habilitado, puedes configurarlo en ajustes");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Ir a ajustes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
               // showedGpsAlert=false;
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            //    dimissGpsAlert=true;
            //    showedGpsAlert=false;
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /*
        static long ONE_MINUTE_IN_MILLIS=60000;//millisecs

        public boolean isLocationUpdated(){
        Calendar date = Calendar.getInstance();
        long t= date.getTimeInMillis();
        Date afterSubsMins=new Date(t - (7 * ONE_MINUTE_IN_MILLIS));
        if(location.getTime() > afterSubsMins.getTime()) {
            return true;
        }
        location = getLocation();
        return false;
    }*/

    @Override
    public void onLocationChanged(Location location) {
        Log.d("GPSTrackerInfo","Updated Location");
        try {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }catch (SecurityException se){
        }
    }


    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("GPSTrackerInfo","Status changed...");
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}