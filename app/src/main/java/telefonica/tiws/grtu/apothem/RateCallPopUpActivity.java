package telefonica.tiws.grtu.apothem;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RateCallPopUpActivity extends Activity {

    RatingBar ratingBar;
    Activity thisActivity;
    Dialog dialog;
    TextView textViewNumber;
    String number=null;
    Date startAt=null;
    DataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            thisActivity = this;
            super.onCreate(savedInstanceState);

            dataBase = new DataBase();
            if(!dataBase.getSettings(thisActivity).rateCalls){
                return;
            }

            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.activity_rate_call_pop_up);

            Button button = (Button) dialog.findViewById(R.id.rateCallButton);
            ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
            textViewNumber = (TextView) dialog.findViewById(R.id.textViewNumber);

            try {
                number=getIntent().getStringExtra("number");
                String startAtS=getIntent().getStringExtra("startAt");
                startAt=new Date(startAtS);
                textViewNumber.setText("De las "+ new SimpleDateFormat("HH:mm:ss").format(startAt)+ " ("+new SimpleDateFormat("dd-MM-yyyy").format(startAt)+")");
            }catch (Exception e){
                textViewNumber.setText("De la última llamada");
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    float rate=ratingBar.getRating();
                    Log.d("Valorada la llamada", rate + " estrellas");
                    dataBase.storeCallRate(thisActivity, number, startAt, rate);

                    CheckBox checkBox=(CheckBox) dialog.findViewById(R.id.checkBoxDontShowAgain);
                    if(checkBox.isChecked()){
                        DataBase.SettingsRecord settingsRecord = dataBase.getSettings(thisActivity);
                        settingsRecord.rateCalls=false;
                        settingsRecord.save(thisActivity);
                    }
                    dialog.dismiss();
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                public void onDismiss(final DialogInterface dialog) {
                    thisActivity.finish();
                }
            });
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
            Log.d("ERROR","En rate call");
        }
    }
}
