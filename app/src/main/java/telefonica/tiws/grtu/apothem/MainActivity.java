package telefonica.tiws.grtu.apothem;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static Activity thisActivity;
    private static DataBase dataBase;
    public final static int FINISH_LANDING = 1;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        thisActivity=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBase = new DataBase();

        DataBase.SettingsRecord settingsRecord = dataBase.getSettings(thisActivity);
        //settingsRecord.hasInitApp=false; //DELETE THIS<=============

        //Start storage of data in background
        startBackgroundService();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawFragment(R.id.nav_email);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(!settingsRecord.hasInitApp){
            settingsRecord.hasInitApp=true;
            settingsRecord.save(thisActivity);
            Intent intent = new Intent(thisActivity, LandingActivity.class);
            startActivityForResult(intent, FINISH_LANDING);
        }else {
           checkPermissions();
        }

        drawFragment(R.id.nav_network);
        //drawFragment(R.id.nav_connection);
    }

    private void startBackgroundService(){
        //Do this every minute
        Intent alarm = new Intent(thisActivity, AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(thisActivity, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(thisActivity, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        drawFragment(id);
        return true;
    }


    public void drawFragment(int id){
        Fragment fragment;

        if (id == R.id.nav_device) {
            navigationView.getMenu().getItem(1).setChecked(true);
            fragment = new FragmentDevice();
        } else if (id == R.id.nav_sim) {
            navigationView.getMenu().getItem(2).setChecked(true);
            fragment = new FragmentSim();
        } else if (id == R.id.nav_network) {
            navigationView.getMenu().getItem(3).setChecked(true);
            fragment = new FragmentNetwork();
        } else if (id == R.id.nav_calls) {
            navigationView.getMenu().getItem(4).setChecked(true);
            fragment = new FragmentCalls();
        } else if (id == R.id.nav_location) {
            navigationView.getMenu().getItem(5).setChecked(true);
            fragment = new FragmentLocation();
        } else if (id == R.id.nav_settings) {
            navigationView.getMenu().getItem(6).setChecked(true);
            fragment = new FragmentSettings();
        } else if (id == R.id.nav_email) {
            navigationView.getMenu().getItem(7).setChecked(true);
            fragment = new FragmentEmail();
        } else{
            navigationView.getMenu().getItem(0).setChecked(true);
            fragment = new FragmentConnection();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    /**
     * PERMISSIONS
     */
    private final int MY_PERMISSIONS_READ_PHONE_STATE = 1;
    private final int MY_PERMISSIONS_READ_SMS = 3;
    private final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 4;

    private static boolean hasRequestReadPhoneState=false;
    private static boolean hasRequestReadSms=false;
    private static boolean hasRequestAccessFineLocation=false;

    //Check if all permissions are enabled
    public void checkPermissions(){
        if (!hasRequestReadPhoneState && ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(thisActivity, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_READ_PHONE_STATE);
        }else {
            if (!hasRequestReadSms && ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(thisActivity, new String[]{Manifest.permission.READ_SMS}, MY_PERMISSIONS_READ_SMS);
            } else {
                if (!hasRequestAccessFineLocation && ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(thisActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_READ_PHONE_STATE: {
                hasRequestReadPhoneState=true;
                checkPermissions();
                break;
            }
            case MY_PERMISSIONS_READ_SMS: {
                hasRequestReadSms=true;
                checkPermissions();
                break;
            }
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                hasRequestAccessFineLocation=true;
                checkPermissions();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case FINISH_LANDING:
                //When landing has finished, request permissions
                checkPermissions();
                break;
        }
    }
}
