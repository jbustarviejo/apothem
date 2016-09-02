package telefonica.tiws.grtu.apothem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    private Context context;
    DeviceInfo deviceInfo;
    static LocationTracker myLocation =null;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Alarm","Doing background things...");
        this.context=context;
        deviceInfo=new DeviceInfo(context);
        if(myLocation==null) {
            myLocation = new LocationTracker();
        }
        myLocation.getLocation(context, locationResult);
    }

    public void setAlarm(Context context) {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
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