package aguiheneuf.gsiaudeau.loucar.fragment.location;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aguiheneuf.gsiaudeau.loucar.R;

public class LocationFragment extends Fragment {

    private long idAgence;

    public LocationFragment() {

    }

    public static LocationFragment newInstance(String idAgence) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString("idAgence", idAgence);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.idAgence = getArguments().getLong("idAgence");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
