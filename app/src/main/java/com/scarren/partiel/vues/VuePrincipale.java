package com.scarren.partiel.vues;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.scarren.partiel.R;

public class VuePrincipale extends AppCompatActivity
    implements FragmentGoogleMap.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vue_principale);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.conteneur_principale, new FragmentGoogleMap())
                .commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
