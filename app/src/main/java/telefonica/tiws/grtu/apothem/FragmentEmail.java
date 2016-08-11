package telefonica.tiws.grtu.apothem;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FragmentEmail extends Fragment {

    public static FragmentEmail newInstance() {
        FragmentEmail fragment = new FragmentEmail();
        return fragment;
    }

    public FragmentEmail() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_email, container, false);
        return rootView;
    }


}