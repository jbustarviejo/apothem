package telefonica.tiws.grtu.apothem;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {

    private boolean isRunning;
    private Context context;
    Thread backgroundThread;
    DeviceInfo deviceInfo;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
        deviceInfo=new DeviceInfo(context);
    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        locationClick();
        return START_STICKY;
    }

    static LocationTracker myLocation = new LocationTracker();

    private Runnable myTask = new Runnable() {
        public void run() {
        // Do something here
        Log.d("Alarm scheduled", "Doing things...");
        stopSelf();
        }



    };
    private void locationClick() {
        myLocation.getLocation(context, locationResult);
    }

    public LocationTracker.LocationResult locationResult = new LocationTracker.LocationResult() {

        @Override
        public void gotLocation(final Location location) {
            //Got the location!
            DataBase dataBase = new DataBase();
            dataBase.storeLocationData(context, deviceInfo, location);
        }
    };
}