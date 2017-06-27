package aguiheneuf.gsiaudeau.loucar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import aguiheneuf.gsiaudeau.loucar.model.EtatVoiture;
import aguiheneuf.gsiaudeau.loucar.model.Marque;
import aguiheneuf.gsiaudeau.loucar.model.Voiture;
import aguiheneuf.gsiaudeau.loucar.util.Constant;
import aguiheneuf.gsiaudeau.loucar.util.FastDialog;
import aguiheneuf.gsiaudeau.loucar.util.Network;

public class AjoutActivity extends AppCompatActivity {

    private CoordinatorLayout coordinateLayout;
    private AppBarLayout appBar;
    private CollapsingToolbarLayout toolbarLayout;
    private Toolbar toolbar;
    private EditText nomVoitureAjout;
    private Spinner marqueVoitureAjout;
    private ImageView imageMarqueVoitureAjout;
    private EditText prixVoitureAjout;
    private FloatingActionButton fab;
    private EditText immatriculationVoitureAjout;

    private List<Marque> listeMarque;
    ArrayAdapter<Marque> adapter;

    protected Dialog progressDialog;

    private Voiture voiture;
    private final EtatVoiture etat = EtatVoiture.DISPONIBLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.progressDialog = FastDialog.showProgressDialog(AjoutActivity.this, "Sauvegarde en cours...");

        this.listeMarque = new ArrayList<>();

        coordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinate_layout);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        nomVoitureAjout = (EditText) findViewById(R.id.nom_voiture_ajout);
        marqueVoitureAjout = (Spinner) findViewById(R.id.marque_voiture_ajout);
        imageMarqueVoitureAjout = (ImageView) findViewById(R.id.image_marque_voiture_ajout);
        prixVoitureAjout = (EditText) findViewById(R.id.prix_voiture_ajout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        immatriculationVoitureAjout = (EditText) findViewById(R.id.immatriculation_voiture_ajout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAjout(view);
            }
        });

        // On definit l'adapter pour le spinner
        adapter = new ArrayAdapter<Marque>(AjoutActivity.this, android.R.layout.simple_spinner_item, listeMarque);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marqueVoitureAjout.setAdapter(adapter);

        // On récupère la liste des marques
        RefreshList task = new RefreshList();
        task.execute();

        marqueVoitureAjout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Marque marque = (Marque) marqueVoitureAjout.getSelectedItem();
                if (marque != null && marque.imageLogo != null) {
                    Picasso.with(AjoutActivity.this).load(marque.imageLogo).into(imageMarqueVoitureAjout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Marque marque = (Marque) marqueVoitureAjout.getSelectedItem();
                if (marque != null && marque.imageLogo != null) {
                    Picasso.with(AjoutActivity.this).load(marque.imageLogo).into(imageMarqueVoitureAjout);
                }
            }
        });
    }

    public void onClickAjout(View view) {
        voiture = new Voiture();

        String nom = nomVoitureAjout.getText().toString();
        String immatriculation = immatriculationVoitureAjout.getText().toString();
        String prix = prixVoitureAjout.getText().toString();
        Marque marque = (Marque) marqueVoitureAjout.getSelectedItem();

        if (verifEditText(nom, immatriculation)) {
            voiture.nom = nom;
            voiture.marque = marque;
            try {
                voiture.prix = Double.parseDouble(prix);
            } catch (Exception e){

            }
            voiture.immatriculation = immatriculation;
            voiture.etat = etat;

            appelApi();
        }
    }

    private void appelApi(){
        if (Network.isNetworkAvailable(AjoutActivity.this)) {
            progressDialog.show();

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(AjoutActivity.this);
            String url = Constant.URL_ADD_VOITURE;
            // on affiche un loader pour faire patienter l'utilisateur

            Gson gson = new Gson();
            JSONObject jsonObj = null;
            // Création de l'objet json à partir de la string
            try {
                jsonObj = new JSONObject(gson.toJson(voiture));
            } catch (Exception e) {
                Toast.makeText(AjoutActivity.this, R.string.impossible_convertir_json, Toast.LENGTH_LONG).show();
            }

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            Toast.makeText(AjoutActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(AjoutActivity.this, R.string.erreur_save, Toast.LENGTH_SHORT).show();
                }
            });
            // Add the request to the RequestQueue.
            queue.add(jsonObjReq);
        } else {
            progressDialog.dismiss();
            Toast.makeText(AjoutActivity.this, R.string.erreur_internet, Toast.LENGTH_LONG).show();
        }
    }

    public boolean verifEditText(String nom, String immatriculation) {
        if (nom.isEmpty()) {
            nomVoitureAjout.setError("Le nom n'est pas renseigné");
            return false;
        }

        if (immatriculation.isEmpty()) {
            immatriculationVoitureAjout.setError("L'immatriculation n'est pas renseignée");
            return false;
        }

        return true;
    }

    private class RefreshList extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            if (Network.isNetworkAvailable(AjoutActivity.this)) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(AjoutActivity.this);
                String url = Constant.URL_ALL_MARQUE;
                // on affiche un loader pour faire patienter l'utilisateur

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String json) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<ArrayList<Marque>>() {
                                }.getType();
                                List<Marque> response = gson.fromJson(json, listType);
                                if (response != null && response.size() > 0) {
                                    listeMarque.clear();
                                    listeMarque.addAll(response);
                                    adapter.notifyDataSetChanged();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = new String(error.networkResponse.data);
                        Toast.makeText(AjoutActivity.this, R.string.erreur_recuperation_liste, Toast.LENGTH_SHORT);
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            } else {
                Toast.makeText(AjoutActivity.this, R.string.erreur_internet, Toast.LENGTH_LONG).show();
            }
            return true;
        }
    }
}
