package telefonica.tiws.grtu.apothem;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

public class FragmentSettings extends Fragment {

    static DataBase dataBase;
    Switch switchRateCalls;
    RelativeLayout relativeLayoutDeleteAllRecords;

    public static FragmentSettings newInstance() {
        FragmentSettings fragment = new FragmentSettings();
        return fragment;
    }

    public FragmentSettings() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_settings, container, false);

        dataBase = new DataBase();
        DataBase.SettingsRecord settingsRecord = dataBase.getSettings(getActivity());

        switchRateCalls = (Switch) rootView.findViewById(R.id.switchRateCalls);
        if(settingsRecord.rateCalls){
            switchRateCalls.setChecked(true);
        }else {
            switchRateCalls.setChecked(false);
        }

        relativeLayoutDeleteAllRecords = (RelativeLayout) rootView.findViewById(R.id.deleteRecordsLayout);

        switchRateCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CHECKED",switchRateCalls.isChecked()+"?");
                DataBase.SettingsRecord settingsRecord = dataBase.getSettings(getActivity());
                settingsRecord.rateCalls=switchRateCalls.isChecked();
                settingsRecord.save(getActivity());
            }
        });

        relativeLayoutDeleteAllRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CHECKED","Clicked relativelayout");

                new AlertDialog.Builder(getActivity())
                        .setTitle("Borrar todos los registros")
                        .setMessage("¿Estás seguro de querer borrar los registros de la aplicación?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dataBase.deleteAllTables(getActivity());
                            }})
                        .setNegativeButton("No", null).show();
            }
        });

        return rootView;
    }


}