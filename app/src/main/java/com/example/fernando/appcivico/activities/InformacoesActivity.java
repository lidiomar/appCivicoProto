package com.example.fernando.appcivico.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.InformacoesFragment;

/**
 * Created by fernando on 06/10/16.
 */
public class InformacoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_default);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new InformacoesFragment()).commit();

    }
}