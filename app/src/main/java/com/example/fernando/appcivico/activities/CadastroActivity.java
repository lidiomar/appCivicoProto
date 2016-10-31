package com.example.fernando.appcivico.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.CadastroFragment;


public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_home_as_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new CadastroFragment()).commit();

    }
}

/*
{
        "autor": {
        "codPessoa": 1116
        },
        "codObjetoDestino": 2211003402126,
        "codTipoObjetoDestino": 100,
        "tipo": {
        "codTipoPostagem": 173
        }
        }*/
