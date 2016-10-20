package com.example.fernando.appcivico.activities;

import android.os.Bundle;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.BuscaAvancadaFragment;
import com.example.fernando.appcivico.fragments.MinhasAvaliacoesFragment;

/**
 * Created by fernando on 20/10/16.
 */
public class MinhasAvaliacoesActivity extends ParentMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new MinhasAvaliacoesFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (applicationAppCivico.usuarioAutenticado()) {
            navigationView.getMenu().getItem(2).setChecked(true);
        }
    }
}