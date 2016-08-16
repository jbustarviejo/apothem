package telefonica.tiws.grtu.apothem;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

public class FragmentSettings extends Fragment {

    static DataBase dataBase;
    Switch switchRateCalls;

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

        switchRateCalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CHECKED",switchRateCalls.isChecked()+"?");
                DataBase.SettingsRecord settingsRecord = dataBase.getSettings(getActivity());
                settingsRecord.rateCalls=switchRateCalls.isChecked();
                settingsRecord.save(getActivity());
            }
        });

        return rootView;
    }


}