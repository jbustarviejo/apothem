package telefonica.tiws.grtu.apothem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FragmentLocation extends Fragment {

    public static FragmentLocation newInstance() {
        FragmentLocation fragment = new FragmentLocation();
        return fragment;
    }

    public FragmentLocation() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_location, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.textViewLocation);
        DataBase dataBase=new DataBase();
        List<DataBase.LocationRecord> positionRecordList = dataBase.getPositionsHistory(getActivity(),false);

        String dataBaseContent="";
        if(positionRecordList!=null){
            for(int i=0; i<positionRecordList.size(); i++){
                DataBase.LocationRecord locationRecord= positionRecordList.get(i);
                dataBaseContent+=locationRecord.toJSON()+"\n";
            }
        }

        textView.setText(dataBaseContent);
        return rootView;
    }


}