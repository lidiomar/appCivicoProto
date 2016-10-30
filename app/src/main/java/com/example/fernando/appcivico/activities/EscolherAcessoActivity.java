package com.example.fernando.appcivico.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.EscolherAcessoFragment;

/**
 * Created by fernando on 05/10/16.
 */
public class EscolherAcessoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_default);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new EscolherAcessoFragment()).commit();

    }


}
