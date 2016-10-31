package com.example.fernando.appcivico.activities;

import android.os.Bundle;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.PesquisaMapaFragment;

public class MainActivity extends ParentMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new PesquisaMapaFragment()).commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (applicationAppCivico.usuarioAutenticado()) {
            navigationView.getMenu().getItem(0).setChecked(true);
        }else {
            navigationView.getMenu().getItem(1).setChecked(true);
        }
    }

}
