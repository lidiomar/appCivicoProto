package com.example.fernando.appcivico.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.PesquisaMapaFragment;
import com.example.fernando.appcivico.utils.Constants;

public class PesquisaMapaActivity extends ParentMenuActivity {
    PesquisaMapaFragment pesquisaMapaFragment = new PesquisaMapaFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,pesquisaMapaFragment).commit();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.TURN_LOCATION_ON && resultCode == Activity.RESULT_OK) {
            /*Intent i = new Intent(this,PesquisaMapaActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);*/
            pesquisaMapaFragment.onActivityResult(requestCode,resultCode,data);
        }
    }
}
