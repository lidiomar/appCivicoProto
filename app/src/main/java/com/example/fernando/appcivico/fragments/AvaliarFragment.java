package com.example.fernando.appcivico.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.Autor;
import com.example.fernando.appcivico.estrutura.ConteudoPostagem;
import com.example.fernando.appcivico.estrutura.Postagem;
import com.example.fernando.appcivico.estrutura.Tipo;
import com.example.fernando.appcivico.servicos.Avaliar;

/**
 * Created by fernando on 06/10/16.
 */
public class AvaliarFragment extends Fragment {
    private Spinner spinnerEstabelecimentos;
    private Button buttonAvaliar;
    private RatingBar ratingBar;
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_avaliar, container, false);

        ratingBar = (RatingBar)view.findViewById(R.id.rating_avaliacao);
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.rgb(11111111,01011010,00000000));

        buttonAvaliar = (Button)view.findViewById(R.id.button_avaliar);
        buttonAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Avaliar avaliar = new Avaliar(AvaliarFragment.this.getActivity());

                Tipo tipo = new Tipo();
                tipo.setCodTipoPostagem(173);
                Autor autor = new Autor();
                autor.setCodPessoa(((ApplicationAppCivico)AvaliarFragment.this.getActivity().getApplication()).getUsuarioAutenticado().getCod());
                Postagem postagem = new Postagem();
                postagem.setCodTipoObjetoDestino(100);
                postagem.setCodObjetoDestino(3402126);
                postagem.setTipo(tipo);
                postagem.setAutor(autor);

                ConteudoPostagem conteudoPostagem = new ConteudoPostagem();
                conteudoPostagem.setJSON("");
                conteudoPostagem.setTexto("Avaliação");
                int notaAvaliacao = (int)ratingBar.getRating();
                conteudoPostagem.setValor(notaAvaliacao);

                avaliar.criarPostagem(postagem, conteudoPostagem);

            }
        });
        return view;
    }
}