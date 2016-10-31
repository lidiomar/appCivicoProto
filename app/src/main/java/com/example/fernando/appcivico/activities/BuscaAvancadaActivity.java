package com.example.fernando.appcivico.activities;

import android.os.Bundle;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.PesquisaCidadeFragment;

/**
 * Created by fernando on 06/10/16.
 */
public class BuscaAvancadaActivity extends ParentMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new PesquisaCidadeFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (applicationAppCivico.usuarioAutenticado()) {
            navigationView.getMenu().getItem(1).setChecked(true);
        }else {
            navigationView.getMenu().getItem(2).setChecked(true);
        }

    }
}