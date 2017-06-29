package aguiheneuf.gsiaudeau.loucar.fragment.liste;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import aguiheneuf.gsiaudeau.loucar.AjoutActivity;
import aguiheneuf.gsiaudeau.loucar.ListeActivity;
import aguiheneuf.gsiaudeau.loucar.MainActivity;
import aguiheneuf.gsiaudeau.loucar.R;
import aguiheneuf.gsiaudeau.loucar.VoitureDetailActivity;
import aguiheneuf.gsiaudeau.loucar.model.Voiture;
import aguiheneuf.gsiaudeau.loucar.util.Constant;
import aguiheneuf.gsiaudeau.loucar.util.ListeVoitureAdapter;
import aguiheneuf.gsiaudeau.loucar.util.Network;

public class FragmentListeVoiture extends Fragment {

    private Toolbar toolbar;
    private ListView listViewVoiture;
    private FloatingActionButton fab;
    private List<Voiture> listeVoiture;
    private ListeVoitureAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    protected long idAgence;


    public static FragmentListeVoiture newInstance(long idAgence) {
        FragmentListeVoiture f = new FragmentListeVoiture();
        Bundle args = new Bundle();
        args.putLong("idAgence", idAgence);
        f.setArguments(args);
        return f;
    }

    public FragmentListeVoiture() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            this.idAgence = getArguments().getLong("idAgence");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_liste_voiture, container, false);

        listViewVoiture = (ListView) v.findViewById(R.id.list_voiture);
        fab = (FloatingActionButton) v.findViewById(R.id.fab);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AjoutActivity.class);
                intent.putExtra("idAgence", idAgence);
                startActivity(intent);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshList task = new RefreshList();
                task.execute();
            }

        });

        listeVoiture = new ArrayList<>();
        this.adapter = new ListeVoitureAdapter(getActivity(), R.layout.ligne_voiture, listeVoiture);
        listViewVoiture.setAdapter(adapter);

        listViewVoiture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), VoitureDetailActivity.class);
                intent.putExtra("id", listeVoiture.get(i).id);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        RefreshList task = new RefreshList();
        task.execute(this.idAgence);
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
                String url = String.format(Constant.URL_ALL_VOITURE_BY_AGENCE, idAgence);
                Log.d("DEBUG", "Création de l'url : " + url);
                // on affiche un loader pour faire patienter l'utilisateur

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String json) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<ArrayList<Voiture>>() {
                                }.getType();
                                List<Voiture> response = gson.fromJson(json, listType);
                                if (response != null && response.size() > 0) {
                                    listeVoiture.clear();
                                    listeVoiture.addAll(response);
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
