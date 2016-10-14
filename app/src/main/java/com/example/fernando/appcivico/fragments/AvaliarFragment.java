package com.example.fernando.appcivico.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.Autor;
import com.example.fernando.appcivico.estrutura.Comentario;
import com.example.fernando.appcivico.estrutura.ConteudoPostagem;
import com.example.fernando.appcivico.estrutura.ConteudoPostagemRetorno;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.example.fernando.appcivico.estrutura.Postagem;
import com.example.fernando.appcivico.estrutura.PostagemRetorno;
import com.example.fernando.appcivico.estrutura.Tipo;
import com.example.fernando.appcivico.estrutura.Usuario;
import com.example.fernando.appcivico.servicos.Avaliacao;
import com.example.fernando.appcivico.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by fernando on 06/10/16.
 */
public class AvaliarFragment extends Fragment {
    private Button buttonAvaliar;
    private RatingBar ratingBar;
    private EditText editTextComentario;
    private Estabelecimento estabelecimento;
    private int requestCount = 0;
    private HashMap<String,ConteudoPostagem> conteudoPostagemHash = new HashMap<>();
    private HashMap<String,String> autoresPostagemHash = new HashMap<>();
    private HashMap<String,Usuario> autoresHash = new HashMap<>();

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
        final Avaliacao avaliacao = new Avaliacao(AvaliarFragment.this.getActivity());
        final RequestQueue requestQueue = Volley.newRequestQueue(AvaliarFragment.this.getActivity());
        requestCount = 0;
        Response.Listener responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                final PostagemRetorno[] postagemRetornos = gson.fromJson(response, PostagemRetorno[].class);
                if(postagemRetornos != null) {
                    for(PostagemRetorno postagemRetorno : postagemRetornos) {

                        ConteudoPostagemRetorno[] conteudos = postagemRetorno.getConteudos();
                        String codConteudoPostagem = conteudos[0].getCodConteudoPostagem();
                        final String codPostagem = postagemRetorno.getCodPostagem();
                        autoresPostagemHash.put(codPostagem,postagemRetorno.getCodAutor());

                        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Gson gson = new Gson();
                                ConteudoPostagem conteudoPostagem = gson.fromJson(String.valueOf(response), ConteudoPostagem.class);
                                AvaliarFragment.this.getConteudoPostagemHash().put(codPostagem, conteudoPostagem);
                            }
                        };


                        JsonObjectRequest jsonObjectRequest = avaliacao.buscaConteudoPostagem(codPostagem, codConteudoPostagem, listener, null);
                        requestQueue.add(jsonObjectRequest);
                    }
                    requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                        @Override
                        public void onRequestFinished(Request<Object> request) {
                            requestCount = requestCount + 1;
                            if(requestCount == postagemRetornos.length) {
                                buscarAutores();
                            }
                        }
                    });
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

    public void buscarAutores() {
        final Avaliacao avaliacao = new Avaliacao(AvaliarFragment.this.getActivity());
        final RequestQueue requestQueue = Volley.newRequestQueue(AvaliarFragment.this.getActivity());
        requestCount = 0;

        Set<String> chaves = autoresPostagemHash.keySet();

        for(final String codPostagem : chaves) {
            Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    Usuario usuario = gson.fromJson(String.valueOf(response), Usuario.class);
                    AvaliarFragment.this.getAutoresHash().put(codPostagem,usuario);
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AvaliarFragment.this.getActivity(), AvaliarFragment.this.getActivity().getString(R.string.algo_deu_errado), Toast.LENGTH_SHORT).show();
                }
            };

            JsonObjectRequest requisicaoPessoaPorCodigo = avaliacao.getRequisicaoPessoaPorCodigo(autoresPostagemHash.get(codPostagem), responseListener, errorListener);
            requestQueue.add(requisicaoPessoaPorCodigo);
        }

        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestCount = requestCount + 1;
                if(requestCount == autoresPostagemHash.size()) {

                }
            }
        });
    }

    public ArrayList<Comentario> montaListaComentarios() {
        HashMap<String, Usuario> autoresHash = getAutoresHash();
        HashMap<String, ConteudoPostagem> conteudoPostagemHash = getConteudoPostagemHash();
        ArrayList<Comentario> comentariosList = new ArrayList<>();

        Set<String> chaves = conteudoPostagemHash.keySet();

        if(autoresHash.size() < conteudoPostagemHash.size()) {
            chaves = autoresHash.keySet();
        }

        for(String codigoPostagem : chaves) {
            Usuario u = autoresHash.get(codigoPostagem);
            ConteudoPostagem conteudo = conteudoPostagemHash.get(codigoPostagem);

            String nomeUsuario = u.getNomeUsuario();
            String texto = conteudo.getTexto();
            int valor = conteudo.getValor();

            Comentario comentario = new Comentario();
            comentario.setCodigoPostagem(codigoPostagem);
            comentario.setValor(valor);
            comentario.setNomeUsuario(nomeUsuario);
            comentario.setTexto(texto);

            comentariosList.add(comentario);
        }

        return comentariosList;
    }

    public HashMap<String, ConteudoPostagem> getConteudoPostagemHash() {
        return conteudoPostagemHash;
    }

    public HashMap<String, Usuario> getAutoresHash() {
        return autoresHash;
    }
}