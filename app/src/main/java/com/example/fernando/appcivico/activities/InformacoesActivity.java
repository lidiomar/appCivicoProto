package com.example.fernando.appcivico.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.InformacoesFragment;
import com.example.fernando.appcivico.fragments.InformacoesFragmentTab;

/**
 * Created by fernando on 06/10/16.
 */
public class InformacoesActivity extends AppCompatActivity {
    private InformacoesFragmentTab informacoesFragmentTab = new InformacoesFragmentTab();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_home_as_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,informacoesFragmentTab).commit();

    }
}