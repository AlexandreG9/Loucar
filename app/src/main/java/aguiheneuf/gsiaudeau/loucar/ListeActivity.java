package aguiheneuf.gsiaudeau.loucar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import aguiheneuf.gsiaudeau.loucar.model.Voiture;
import aguiheneuf.gsiaudeau.loucar.util.Constant;
import aguiheneuf.gsiaudeau.loucar.util.ListeVoitureAdapter;
import aguiheneuf.gsiaudeau.loucar.util.Network;

public class ListeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listViewVoiture;
    private FloatingActionButton fab;
    private List<Voiture> listeVoiture;
    private ListeVoitureAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListeActivity.this, AjoutActivity.class);
                startActivity(intent);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listViewVoiture = (ListView) findViewById(R.id.list_voiture);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshList task = new RefreshList();
                task.execute();
            }

        });

        listeVoiture = new ArrayList<>();
        this.adapter = new ListeVoitureAdapter(ListeActivity.this, R.layout.ligne_voiture, listeVoiture);
        listViewVoiture.setAdapter(adapter);

        listViewVoiture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListeActivity.this, VoitureDetailActivity.class);
                startActivityForResult(intent, i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        RefreshList task = new RefreshList();
        task.execute();
    }


    private class RefreshList extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            if (Network.isNetworkAvailable(ListeActivity.this)) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(ListeActivity.this);
                String url = Constant.URL_ALL_VOITURE;
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
                        Toast.makeText(ListeActivity.this, R.string.erreur_recuperation_liste, Toast.LENGTH_SHORT);
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            } else {
                Toast.makeText(ListeActivity.this, R.string.erreur_internet, Toast.LENGTH_LONG).show();
            }
            return true;
        }
    }
}
