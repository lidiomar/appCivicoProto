package com.example.fernando.appcivico.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.activities.CadastroActivity;
import com.example.fernando.appcivico.activities.LoginActivity;

/**
 * Created by fernando on 05/10/16.
 */
public class EscolherAcessoFragment extends Fragment {
    private Button buttonPossuoUmaConta;
    private Button primeiroAcesso;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_escolher_acesso,container,false);
        buttonPossuoUmaConta = (Button)view.findViewById(R.id.button_possuo_uma_conta);

        final Context context = getActivity();

        buttonPossuoUmaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
            }
        });
        primeiroAcesso = (Button)view.findViewById(R.id.button_primeiro_acesso);
        primeiroAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CadastroActivity.class);
                startActivity(i);
            }
        });

        return view;
    }
}
