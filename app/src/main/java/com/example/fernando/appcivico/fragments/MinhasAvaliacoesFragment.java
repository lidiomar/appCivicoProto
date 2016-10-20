package com.example.fernando.appcivico.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.ConteudoPostagem;
import com.example.fernando.appcivico.estrutura.ConteudoPostagemRetorno;
import com.example.fernando.appcivico.estrutura.PostagemRetorno;
import com.example.fernando.appcivico.servicos.Avaliacao;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by fernando on 20/10/16.
 */
public class MinhasAvaliacoesFragment extends Fragment {
    private TextView txt;
    private Avaliacao avaliacao;
    private ApplicationAppCivico applicationAppCivico;
    private Gson gson = new Gson();
    private HashMap<String,ConteudoPostagem> conteudoPostageHash = new HashMap<>();
    private int requestCount;
    private final String TAG = "TAG_MINHAS_AVALIACOES_CONTEUDO";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minhas_avaliacoes,container,false);
        txt = (TextView)view.findViewById(R.id.txt);
        avaliacao = new Avaliacao(this.getActivity());
        applicationAppCivico = (ApplicationAppCivico) this.getActivity().getApplication();

        carregaComentarios();

        return view;
    }

    public void carregaComentarios() {
        String codAutor = String.valueOf(applicationAppCivico.getUsuarioAutenticado().getCod());

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final PostagemRetorno[] postagemRetornos = gson.fromJson(response, PostagemRetorno[].class);
                MinhasAvaliacoesFragment.this.getConteudoPostagemHash().clear();
                if(postagemRetornos != null) {
                    buscaConteudoPostagens(postagemRetornos);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MinhasAvaliacoesFragment.this.getActivity(),
                        MinhasAvaliacoesFragment.this.getActivity().getResources().getString(R.string.algo_deu_errado),
                        Toast.LENGTH_SHORT).show();
            }
        };

        StringRequest requisicaoPessoaPorCodigo = avaliacao.buscaPostagensPorAutor(0,20, "",listener,codAutor,errorListener);
        RequestQueue requestQueueAvaliacao = avaliacao.getRequestQueue();
        requestQueueAvaliacao.add(requisicaoPessoaPorCodigo);

    }

    private void buscaConteudoPostagens(final PostagemRetorno[] postagemRetornos) {
        final RequestQueue requestQueue = Volley.newRequestQueue(MinhasAvaliacoesFragment.this.getActivity());
        requestCount = 0;
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestCount = requestCount + 1;
                if(requestCount == postagemRetornos.length) {

                }
            }
        });

        for(PostagemRetorno postagemRetorno : postagemRetornos) {

            ConteudoPostagemRetorno[] conteudos = postagemRetorno.getConteudos();
            String codConteudoPostagem = conteudos[0].getCodConteudoPostagem();
            final String codPostagem = postagemRetorno.getCodPostagem();

            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    ConteudoPostagem conteudoPostagem = gson.fromJson(String.valueOf(response), ConteudoPostagem.class);
                    MinhasAvaliacoesFragment.this.getConteudoPostagemHash().put(codPostagem, conteudoPostagem);
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    requestQueue.cancelAll(TAG);
                    Toast.makeText(MinhasAvaliacoesFragment.this.getActivity(),
                            MinhasAvaliacoesFragment.this.getActivity().getResources().getString(R.string.algo_deu_errado),
                            Toast.LENGTH_SHORT).show();
                }
            };

            JsonObjectRequest jsonObjectRequest = avaliacao.buscaConteudoPostagem(codPostagem, codConteudoPostagem, listener, errorListener);
            requestQueue.add(jsonObjectRequest);
        }
    }

    public HashMap<String, ConteudoPostagem> getConteudoPostagemHash() {
        return conteudoPostageHash;
    }

}
