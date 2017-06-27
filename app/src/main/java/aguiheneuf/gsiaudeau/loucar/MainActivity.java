package aguiheneuf.gsiaudeau.loucar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import aguiheneuf.gsiaudeau.loucar.fragment.liste.FragmentListeVoiture;
import aguiheneuf.gsiaudeau.loucar.fragment.location.LocationFragment;
import aguiheneuf.gsiaudeau.loucar.fragment.statistique.StatistiqueFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_liste_voiture:
                    changeNameActionBar(getString(R.string.list_voiture));
                    switchFragment(new FragmentListeVoiture());
                    return true;
                case R.id.navigation_locations:
                    changeNameActionBar(getString(R.string.location_titre));
                    switchFragment(new LocationFragment());
                    return true;
                case R.id.navigation_statistiques:
                    changeNameActionBar(getString(R.string.statistiques));
                    switchFragment(new StatistiqueFragment());
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        changeNameActionBar(getString(R.string.list_voiture));
        switchFragment(new FragmentListeVoiture());
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment, fragment)
                .commit();
    }

    private void changeNameActionBar(String nom){
        setTitle(nom);
    }

}
