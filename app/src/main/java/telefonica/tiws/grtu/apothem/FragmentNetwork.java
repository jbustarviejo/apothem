package telefonica.tiws.grtu.apothem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class FragmentNetwork extends Fragment {

    boolean initTimer=false;
    DeviceInfo deviceInfo;
    final Handler handler = new Handler();
    Activity thisActivity;

    //Layout
    LinearLayout stationsContainerLayout;


    public static FragmentNetwork newInstance() {
        FragmentNetwork fragment = new FragmentNetwork();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_network, container, false);
        deviceInfo=new DeviceInfo(getActivity());

        thisActivity = getActivity();
        fillViewInfo(rootView);

        if(!initTimer) {
            initTimer=true;
            thisActivity = getActivity();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    updateStationNetwork();
                    handler.postDelayed(this, 3000);
                }
            });
        }
        return rootView;
    }

    //Get the TextViews for this view
    public void fillViewInfo(View thisView){
        //Stations layout
        stationsContainerLayout = (LinearLayout) thisView.findViewById(R.id.layoutStations);

        //Refresh values
        updateStationNetwork();
    }

    @SuppressLint("NewApi")
    public void updateStationNetwork(){
        stationsContainerLayout.removeAllViews();

        List<DeviceInfo.StationInfo> stationInfoList = deviceInfo.getAllCellsInfo();
        int neighbour=1;

        if(stationInfoList==null || stationInfoList.size()==0){
            TextView textViewTitle=new TextView(thisActivity);
            TextView textViewSubtitle=new TextView(thisActivity);
            textViewTitle.setText("No se han detectado estaciones cercanas");
            textViewTitle.setTypeface(null, Typeface.BOLD);
            textViewSubtitle.setText("Esta pantalla se actualizará si aparecen nuevas estaciones");

            stationsContainerLayout.addView(textViewTitle);
            stationsContainerLayout.addView(textViewSubtitle);
        }else{
            for(DeviceInfo.StationInfo stationInfo : stationInfoList) {
                LayoutInflater li = thisActivity.getLayoutInflater();
                View netWorkInfoBox= li.inflate(R.layout.fragment_stations, null);

                TextView cellType=(TextView) netWorkInfoBox.findViewById(R.id.cellType);
                TextView cellMcc=(TextView) netWorkInfoBox.findViewById(R.id.cellMcc);
                TextView cellMnc=(TextView) netWorkInfoBox.findViewById(R.id.cellMnc);
                TextView cellId=(TextView) netWorkInfoBox.findViewById(R.id.cellId);
                TextView cellAreaCode=(TextView) netWorkInfoBox.findViewById(R.id.cellAreaCode);
                TextView cellPower=(TextView) netWorkInfoBox.findViewById(R.id.cellPower);

                ImageView iconSignal=(ImageView) netWorkInfoBox.findViewById(R.id.iconSignal);


                if(stationInfo.isRegistered){
                    cellType.setText((neighbour++)+". "+stationInfo.type+" -Registrado-");
                }else{
                    cellType.setText((neighbour++)+". "+stationInfo.type);
                }
                cellMcc.setText(stationInfo.mcc);
                cellMnc.setText(stationInfo.mnc);
                cellId.setText(stationInfo.id_cell);
                cellAreaCode.setText(stationInfo.area_code);
                cellPower.setText(stationInfo.power);

                switch (stationInfo.signal){
                    case 1:
                        iconSignal.setColorFilter(thisActivity.getColor(R.color.signal1));
                        break;
                    case 2:
                        iconSignal.setColorFilter(thisActivity.getColor(R.color.signal2));
                        break;
                    case 3:
                        iconSignal.setColorFilter(thisActivity.getColor(R.color.signal3));
                        break;
                    case 4:
                        iconSignal.setColorFilter(thisActivity.getColor(R.color.signal4));
                        break;
                    default:
                        iconSignal.setColorFilter(thisActivity.getColor(R.color.signal0));
                        iconSignal.setImageResource(R.drawable.ic_signal_empty);
                        break;
                }

                if(!stationInfo.enoughInfo){
                    netWorkInfoBox.setAlpha(0.6f);
                }

                stationsContainerLayout.addView(netWorkInfoBox);
            }
        }
    }
}