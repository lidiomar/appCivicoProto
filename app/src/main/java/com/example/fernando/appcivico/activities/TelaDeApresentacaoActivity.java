package com.example.fernando.appcivico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.fragments.LoginFragment;
import com.example.fernando.appcivico.fragments.TelaDeApresentacaoFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by fernando on 04/11/16.
 */
public class TelaDeApresentacaoActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_de_apresentacao);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new TelaDeApresentacaoFragment()).commit();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(TelaDeApresentacaoActivity.this, PesquisaMapaActivity.class);
                startActivity(i);


                finish();
            }
        }, 3000);

    }
}
