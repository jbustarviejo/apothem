package telefonica.tiws.grtu.apothem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentConnection extends Fragment {

    public static FragmentConnection newInstance() {
        FragmentConnection fragment = new FragmentConnection();
        return fragment;
    }

    public FragmentConnection() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_connection, container, false);
        return rootView;
    }


}