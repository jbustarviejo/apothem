package telefonica.tiws.grtu.apothem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RunOnStartupReceiver extends  BroadcastReceiver {

    AlarmReceiver alarmReceiver = new AlarmReceiver();

    @Override
    public void onReceive(Context context, Intent intent){
        Log.d("Alarm","Run on startup");
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            alarmReceiver.setAlarm(context);
        }
    }
}


