package aguiheneuf.gsiaudeau.loucar.fragment.location;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import aguiheneuf.gsiaudeau.loucar.R;
import aguiheneuf.gsiaudeau.loucar.fragment.liste.FragmentListeVoiture;
import aguiheneuf.gsiaudeau.loucar.model.Location;
import aguiheneuf.gsiaudeau.loucar.model.Voiture;
import aguiheneuf.gsiaudeau.loucar.util.Constant;
import aguiheneuf.gsiaudeau.loucar.util.ListeLocationAdapter;
import aguiheneuf.gsiaudeau.loucar.util.Network;

public class LocationFragment extends Fragment {

    private long idAgence;

    private List<Location> listeLocation;

    private ListView listLocation;

    private ListeLocationAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    public LocationFragment() {

    }

    public static LocationFragment newInstance(long idAgence) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putLong("idAgence", idAgence);
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
        View v = inflater.inflate(R.layout.fragment_location, container, false);
        listLocation = (ListView) v.findViewById(R.id.list_location);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.listeLocation = new ArrayList<>();
        this.adapter = new ListeLocationAdapter(getActivity(), R.layout.ligne_location, listeLocation);
        listLocation.setAdapter(adapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshList task = new RefreshList();
                task.execute();
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Maj de la liste
        RefreshList task = new RefreshList();
        task.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class RefreshList extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Long... params) {
            if (Network.isNetworkAvailable(getActivity())) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = String.format(Constant.URL_ALL_LOCATION_BY_AGENCE, idAgence);
                Log.d("DEBUG", "Création de l'url : " + url);
                // on affiche un loader pour faire patienter l'utilisateur

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String json) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<ArrayList<Location>>() {
                                }.getType();
                                List<Location> response = gson.fromJson(json, listType);
                                if (response != null && response.size() > 0) {
                                    listeLocation.clear();
                                    listeLocation.addAll(response);
                                    adapter.notifyDataSetChanged();
                                }
                                swipeContainer.setRefreshing(false);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = new String(error.networkResponse.data);
                        swipeContainer.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.erreur_recuperation_liste, Toast.LENGTH_SHORT);
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            } else {
                Toast.makeText(getActivity(), R.string.erreur_internet, Toast.LENGTH_LONG).show();
            }
            return true;
        }
    }

}
