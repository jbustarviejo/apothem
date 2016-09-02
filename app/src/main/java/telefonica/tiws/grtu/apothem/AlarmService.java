package telefonica.tiws.grtu.apothem;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {

    AlarmReceiver alarmReceiver = new AlarmReceiver();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        alarmReceiver.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        alarmReceiver.setAlarm(this);
    }
}