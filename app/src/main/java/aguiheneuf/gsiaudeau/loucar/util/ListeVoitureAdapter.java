package aguiheneuf.gsiaudeau.loucar.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import aguiheneuf.gsiaudeau.loucar.R;
import aguiheneuf.gsiaudeau.loucar.model.Voiture;

/**
 * Created by aguiheneuf2015 on 26/06/2017.
 */

public class ListeVoitureAdapter extends ArrayAdapter {

    private int res;
    private List<Voiture> listeVoiture;

    public ListeVoitureAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.res = resource;
        this.listeVoiture = objects;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return listeVoiture.get(position);
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

        Voiture v = listeVoiture.get(position);

        TextView nomVoitureListe = (TextView) view.findViewById(R.id.nom_voiture_liste);
        TextView marqueVoitureListe = (TextView) view.findViewById(R.id.marque_voiture_liste);
        TextView immatriculationVoitureListe = (TextView) view.findViewById(R.id.immatriculation_voiture_liste);
        TextView etatVoitureListe = (TextView) view.findViewById(R.id.etat_voiture_liste);

        return view;
    }
}
