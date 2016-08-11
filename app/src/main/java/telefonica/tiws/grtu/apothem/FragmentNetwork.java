package telefonica.tiws.grtu.apothem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentNetwork extends Fragment {

    public static FragmentNetwork newInstance() {
        FragmentNetwork fragment = new FragmentNetwork();
        return fragment;
    }

    public FragmentNetwork() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_network, container, false);
        return rootView;
    }


}