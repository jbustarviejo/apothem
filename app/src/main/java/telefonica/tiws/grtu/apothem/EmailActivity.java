package telefonica.tiws.grtu.apothem;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailActivity extends AppCompatActivity {

    private static Activity thisActivity;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private boolean isOpenFileContentLocations=false;
    private boolean isOpenFileContentCalls=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        thisActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEmail);
        setSupportActionBar(toolbar);

        ScrollView contentFile = (ScrollView) findViewById(R.id.contentFile);
        contentFile.setAlpha(0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button sendButton = (Button) findViewById(R.id.sendEmailButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText userEmailBox = (EditText) findViewById(R.id.userEmailText);
                EditText descriptionBox = (EditText) findViewById(R.id.supportDescriptionText);

                String userEmail = userEmailBox.getText().toString();

                //Validate email
                if (userEmail.length() == 0) {
                    Snackbar.make(view, "Introduce un email de contacto", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    userEmailBox.requestFocus();
                    return;
                }

                String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
                Pattern p = Pattern.compile(ePattern);
                Matcher m = p.matcher(userEmail);
                if (!m.matches()) {
                    Snackbar.make(view, "El email parece incorrecto, revísalo", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    userEmailBox.requestFocus();
                    return;
                }

                //Check description
                String description = descriptionBox.getText().toString();

                if (description.length() == 0) {
                    Snackbar.make(view, "Introduce una descripción", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    descriptionBox.requestFocus();
                    return;
                }

                Toast.makeText(thisActivity, "Email enviado correctamente, ¡gracias!", Toast.LENGTH_LONG).show();
                thisActivity.finish();
            }
        });

        TextView positionRecords = (TextView) findViewById(R.id.positionRecords);
        positionRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView contentFile = (ScrollView) findViewById(R.id.contentFile);
                if(isOpenFileContentLocations){
                    isOpenFileContentLocations=false;
                    contentFile.setAlpha(0);
                    return;
                }
                isOpenFileContentLocations=true;
                contentFile.setAlpha(1);

                TextView fileContent = (TextView) findViewById(R.id.fileContent);

                DataBase dataBase = new DataBase();
                List<DataBase.LocationRecord> locationRecordList = dataBase.getPositionsHistory(thisActivity,false);
                String file="";
                for(int i=0;i<locationRecordList.size();i++){
                    file+=locationRecordList.get(i).toJSON();
                }

                fileContent.setText(file);
            }
        });
        TextView callsRatingRecords = (TextView) findViewById(R.id.CallsRatingRecords);
        assert callsRatingRecords != null;
        callsRatingRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView contentFile = (ScrollView) findViewById(R.id.contentFile);
                if(isOpenFileContentCalls){
                    isOpenFileContentCalls=false;
                    assert contentFile != null;
                    contentFile.setAlpha(0);
                    return;
                }
                isOpenFileContentCalls=true;
                assert contentFile != null;
                contentFile.setAlpha(1);

                TextView fileContent = (TextView) findViewById(R.id.fileContent);

                DataBase dataBase = new DataBase();
                List<DataBase.CallsRateRecord> callsRateRecordList = dataBase.getCallsRateRecords(thisActivity);
                String file="";
                for(int i=0;i<callsRateRecordList.size();i++){
                    file+=callsRateRecordList.get(i).toJSON();
                }

                assert fileContent != null;
                fileContent.setText(file);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Email Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://telefonica.tiws.grtu.apothem/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Email Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://telefonica.tiws.grtu.apothem/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
