package com.example.fernando.appcivico.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.CadastroFragment;


public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_default);

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
