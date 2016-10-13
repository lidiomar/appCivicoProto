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
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.Autor;
import com.example.fernando.appcivico.estrutura.ConteudoPostagem;
import com.example.fernando.appcivico.estrutura.ConteudoPostagemRetorno;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.example.fernando.appcivico.estrutura.Postagem;
import com.example.fernando.appcivico.estrutura.PostagemRetorno;
import com.example.fernando.appcivico.estrutura.Tipo;
import com.example.fernando.appcivico.servicos.Avaliacao;
import com.example.fernando.appcivico.servicos.Servicos;
import com.example.fernando.appcivico.utils.Constants;
import com.google.gson.Gson;

/**
 * Created by fernando on 06/10/16.
 */
public class AvaliarFragment extends Fragment {
    private Button buttonAvaliar;
    private RatingBar ratingBar;
    private EditText editTextComentario;
    private Estabelecimento estabelecimento;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_avaliar, container, false);

        Bundle extras = getActivity().getIntent().getExtras();
        estabelecimento = (Estabelecimento)extras.get("estabelecimento");
        
        ratingBar = (RatingBar)view.findViewById(R.id.rating_avaliacao);
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.rgb(11111111,01011010,00000000));

        editTextComentario = (EditText)view.findViewById(R.id.comentario_avaliacao);

        buttonAvaliar = (Button)view.findViewById(R.id.button_avaliar);
        buttonAvaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int notaAvaliacao = (int)ratingBar.getRating();
                String comentario = editTextComentario.getText().toString();

                if(validarEnvio(comentario, notaAvaliacao)) {
                    final Avaliacao avaliacao = new Avaliacao(AvaliarFragment.this.getActivity());
                    Tipo tipo = new Tipo();
                    tipo.setCodTipoPostagem(Constants.CODE_TIPO_POSTAGEM);
                    Autor autor = new Autor();
                    autor.setCodPessoa(((ApplicationAppCivico) AvaliarFragment.this.getActivity().getApplication()).getUsuarioAutenticado().getCod());

                    Postagem postagem = new Postagem();
                    postagem.setCodTipoObjetoDestino(Constants.CODE_TIPO_OBJETO_DESTINO);
                    postagem.setCodObjetoDestino(estabelecimento.getCodUnidade());
                    postagem.setTipo(tipo);
                    postagem.setAutor(autor);

                    final ConteudoPostagem conteudoPostagem = new ConteudoPostagem();
                    conteudoPostagem.setJSON("");
                    conteudoPostagem.setTexto(comentario);
                    conteudoPostagem.setValor(notaAvaliacao);

                    Response.Listener<String> responseListenerCriarPostagem = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            avaliacao.atribuirConteudoPostagem(conteudoPostagem);
                        }
                    };

                    avaliacao.criarPostagem(postagem, conteudoPostagem, responseListenerCriarPostagem);

                } else {
                    Toast.makeText(AvaliarFragment.this.getActivity(),AvaliarFragment.this.getActivity().getString(R.string
                    .preencha_os_dados_da_avaliacao) ,Toast.LENGTH_SHORT).show();
                }
            }
        });
        buscarComentarios();
        return view;
    }

    public Boolean validarEnvio(String comentario, int rating) {
        if(comentario.isEmpty() && rating <= 0) {
            return false;
        }
        return true;
    }

    public void buscarComentarios() {
        Avaliacao avaliacao = new Avaliacao(AvaliarFragment.this.getActivity());

        Response.Listener responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                PostagemRetorno[] postagemRetornos = gson.fromJson(response, PostagemRetorno[].class);
                if(postagemRetornos != null) {
                    for(PostagemRetorno postagemRetorno : postagemRetornos) {
                        String codAutor = postagemRetorno.getCodAutor();

                        ConteudoPostagemRetorno[] conteudos = postagemRetorno.getConteudos();
                        String codConteudoPostagem = conteudos[0].getCodConteudoPostagem();

                        String codPostagem = postagemRetorno.getCodPostagem();



                    }
                }
            }
        };

        Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AvaliarFragment.this.getActivity(),AvaliarFragment.this.getActivity().getString(R.string.algo_deu_errado),Toast.LENGTH_SHORT).show();
            }
        };
        avaliacao.buscaPostagens(0,5,estabelecimento.getCodUnidade(),responseListener,responseErrorListener);
    }
}