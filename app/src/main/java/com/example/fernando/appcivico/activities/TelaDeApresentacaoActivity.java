package com.example.fernando.appcivico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.fernando.appcivico.R;

/**
 * Created by fernando on 04/11/16.
 */
public class TelaDeApresentacaoActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_de_apresentacao);

        new Handler().postDelayed(new Runnable() {
            /*
             * Exibindo splash com um timer.
             */
            @Override
            public void run() {
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal
                Intent i = new Intent(TelaDeApresentacaoActivity.this, PesquisaMapaActivity.class);
                startActivity(i);

                // Fecha esta activity
                finish();
            }
        }, 2000);

    }
}
