package com.example.fernando.appcivico.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.EscolherAcessoFragment;

/**
 * Created by fernando on 05/10/16.
 */
public class EscolherAcessoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        setContentView(R.layout.layout_default);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new EscolherAcessoFragment()).commit();

    }
}
