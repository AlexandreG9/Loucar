package aguiheneuf.gsiaudeau.loucar.util;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.List;

import aguiheneuf.gsiaudeau.loucar.R;
import aguiheneuf.gsiaudeau.loucar.model.Location;

/**
 * Created by aguiheneuf2015 on 28/06/2017.
 */

public class ListeLocationAdapter extends ArrayAdapter {

    private int res;
    private List<Location> listeLocation;

    private TextView nomClientLocation;
    private TextView prenomClientLocation;
    private TextView villeClientLocation;
    private TextView emailClientLocation;
    private TextView telephoneClientLocation;
    private TextView dateDebutLocation;
    private TextView dateFinLocation;
    private TextView nomVoitureLocation;
    private ImageView imageMarqueVoitureLocation;


    public ListeLocationAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.res = resource;
        this.listeLocation = objects;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return listeLocation.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view = inflater.inflate(res, parent, false);
        } else {
            view = convertView;
        }

        Location l = listeLocation.get(position);

        nomClientLocation = (TextView) view.findViewById(R.id.nom_client_location);
        prenomClientLocation = (TextView) view.findViewById(R.id.prenom_client_location);
        villeClientLocation = (TextView) view.findViewById(R.id.ville_client_location);
        emailClientLocation = (TextView) view.findViewById(R.id.email_client_location);
        telephoneClientLocation = (TextView) view.findViewById(R.id.telephone_client_location);
        dateDebutLocation = (TextView) view.findViewById(R.id.date_debut_location);
        dateFinLocation = (TextView) view.findViewById(R.id.date_fin_location);
        nomVoitureLocation = (TextView) view.findViewById(R.id.nom_voiture_location);
        imageMarqueVoitureLocation = (ImageView) view.findViewById(R.id.image_marque_voiture_location);

        nomClientLocation.setText(l.client.nom);
        prenomClientLocation.setText(l.client.prenom);
        villeClientLocation.setText(l.client.ville);
        emailClientLocation.setText(l.client.email);
        telephoneClientLocation.setText(l.client.telephone);
        if (l.dateDebut != null)
            dateDebutLocation.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(l.dateDebut));

        if (l.dateFin != null)
            dateFinLocation.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(l.dateFin));

        nomVoitureLocation.setText(l.voiture.nom);
        Picasso.with(getContext()).load(l.voiture.marque.imageLogo).into(imageMarqueVoitureLocation);

        return view;
    }
}
