package telefonica.tiws.grtu.apothem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;

public class OnPhoneCallEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //Detects calls
    private static class PhoneStateChangeListener extends PhoneStateListener {

        static final int IDLE = 0;
        static final int OFFHOOK = 1;
        static final int RINGING = 2;
        int lastState = IDLE;
        Date start;

        Context context;
        public PhoneStateChangeListener(Context context) {
            this.context = context;
            start = new Date();
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    lastState = RINGING;
                    //Sonando...
                    start = new Date();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    lastState = OFFHOOK;
                    //Descolgado...
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    //Ocioso
                    if (lastState == RINGING) {
                    }else if(lastState == OFFHOOK){
                        //Finalizada...
                        Intent intent = new Intent(context, RateCallPopUpActivity.class);
                        intent.putExtra("number",incomingNumber);
                        intent.putExtra("startAt",start.toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else{
                        //Log.d("LLAMADA", "No producida");
                    }
                    lastState = IDLE;
                    break;
            }
        }
    }

    public static class CallListenerServiceReceiver extends BroadcastReceiver {
        TelephonyManager telephony;
        PhoneStateChangeListener phoneListener;

        public void onReceive(Context context, Intent intent) {
            if(phoneListener==null) {
                phoneListener = new PhoneStateChangeListener(context);
            }
            telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        public void onDestroy() {
            telephony.listen(null, PhoneStateListener.LISTEN_NONE);
        }

    }
}
