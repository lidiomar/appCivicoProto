package com.example.fernando.appcivico.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.BuscaAvancadaFragment;
import com.example.fernando.appcivico.fragments.MinhasAvaliacoesFragment;

/**
 * Created by fernando on 20/10/16.
 */
public class MinhasAvaliacoesActivity extends ParentMenuActivity {
    MinhasAvaliacoesFragment minhasAvaliacoesFragment = new MinhasAvaliacoesFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,minhasAvaliacoesFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (applicationAppCivico.usuarioAutenticado()) {
            navigationView.getMenu().getItem(2).setChecked(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent refresh = new Intent(this, MinhasAvaliacoesActivity.class);
        startActivity(refresh);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }
}