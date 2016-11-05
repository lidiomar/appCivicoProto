package com.example.fernando.appcivico.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fernando.appcivico.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by fernando on 04/11/16.
 */
public class TelaDeApresentacaoFragment extends Fragment {

    ImageView imageLogo;
    TextView textDesafio;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tela_de_apresentacao, container, false);
        imageLogo = (ImageView)view.findViewById(R.id.image_logo);
        textDesafio = (TextView) view.findViewById(R.id.texto_desafio);

        Picasso.with(this.getActivity()).load(R.drawable.logo_desafio).memoryPolicy(MemoryPolicy.NO_STORE).fit().into(imageLogo, new Callback() {
            @Override
            public void onSuccess() {
                textDesafio.setText(getResources().getString(R.string.desafio_de_aplicativos));
            }

            @Override
            public void onError() {

            }
        });

        return view;
    }
}
