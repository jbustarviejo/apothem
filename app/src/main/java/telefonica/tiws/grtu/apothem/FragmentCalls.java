package telefonica.tiws.grtu.apothem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentCalls extends Fragment {

    public static FragmentCalls newInstance() {
        FragmentCalls fragment = new FragmentCalls();
        return fragment;
    }

    public FragmentCalls() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_calls, container, false);
        return rootView;
    }


}