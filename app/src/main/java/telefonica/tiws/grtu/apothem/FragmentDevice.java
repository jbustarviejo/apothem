package telefonica.tiws.grtu.apothem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentDevice extends Fragment {

    public static FragmentDevice newInstance() {
        FragmentDevice fragment = new FragmentDevice();
        return fragment;
    }

    public FragmentDevice() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_device, container, false);
        return rootView;
    }


}