package aguiheneuf.gsiaudeau.loucar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import aguiheneuf.gsiaudeau.loucar.model.Voiture;

public class VoitureDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voiture_detail);

        Voiture actuelle = GetVoitureDetail( 0);




        TextView immatriculation = (TextView) findViewById(R.id.immatriculation);
        TextView prix = (TextView) findViewById(R.id.prix_jour);
        TextView marque = (TextView) findViewById(R.id.marque);
        TextView modele = (TextView) findViewById(R.id.modele);
        TextView etat = (TextView) findViewById(R.id.etat);


    }


    private Voiture GetVoitureDetail(int id){
        Voiture actual = new Voiture();

        return actual;
    }

}
