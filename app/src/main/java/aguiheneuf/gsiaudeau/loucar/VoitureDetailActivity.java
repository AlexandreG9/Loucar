package aguiheneuf.gsiaudeau.loucar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import aguiheneuf.gsiaudeau.loucar.model.Voiture;
import aguiheneuf.gsiaudeau.loucar.util.Constant;
import aguiheneuf.gsiaudeau.loucar.util.Network;

public class VoitureDetailActivity extends AppCompatActivity {
    private Voiture voitureActuelle;

    private TextView marque;
    private TextView modele;
    private ImageView imageVoiture;
    private TextView prixJour;
    private TextView etat;
    private TextView immatriculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voiture_detail);

        // Récupération de l'id
        long id = getIntent().getLongExtra("id", -1l);

        // Récupération de la voiture
        GetVoiture task = new GetVoiture();
        task.execute(id);

    }


    private Voiture GetVoitureDetail(int id){
        Voiture actual = new Voiture();

        return actual;
    }

    private class GetVoiture extends AsyncTask<Long, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Long... params) {
            if (Network.isNetworkAvailable(VoitureDetailActivity.this)) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(VoitureDetailActivity.this);
                String url = String.format(Constant.URL_ID_VOITURE, params[0]);
                // on affiche un loader pour faire patienter l'utilisateur

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String json) {
                                Gson gson = new Gson();
                                Voiture response = gson.fromJson(json, Voiture.class);
                                if (response != null) {
                                    voitureActuelle = response;

                                    // TODO : Appeler methode pour rafraichissement de l'ihm
                                    setIHM(voitureActuelle);

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = new String(error.networkResponse.data);
                        Toast.makeText(VoitureDetailActivity.this, R.string.erreur_recuperation_liste, Toast.LENGTH_SHORT).show();
                    }
                });
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            } else {
                Toast.makeText(VoitureDetailActivity.this, R.string.erreur_internet, Toast.LENGTH_LONG).show();
            }
            return true;
        }
    }

    private void setIHM(Voiture voit){
        marque = (TextView) findViewById(R.id.marque);
        modele = (TextView) findViewById(R.id.modele);
        imageVoiture = (ImageView) findViewById(R.id.imageVoiture);
        prixJour = (TextView) findViewById(R.id.prix_jour);
        etat = (TextView) findViewById(R.id.etat);
        immatriculation = (TextView) findViewById(R.id.immatriculation);

        modele.setText(voit.nom.toString());
        marque.setText(voit.marque.toString() + " : ");
        immatriculation.setText(voit.immatriculation.toString());
        Picasso.with(VoitureDetailActivity.this).load(voit.marque.imageLogo.toString()).into(imageVoiture);
        prixJour.setText(String.valueOf(voit.prix));
        etat.setText(voit.etat.toString());


    }

}
