package com.example.fernando.appcivico.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.example.fernando.appcivico.adapters.ComentarioAdapter;
import com.example.fernando.appcivico.adapters.MinhasAvaliacoesAdapter;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.Comentario;
import com.example.fernando.appcivico.estrutura.ConteudoPostagem;
import com.example.fernando.appcivico.estrutura.ConteudoPostagemRetorno;
import com.example.fernando.appcivico.estrutura.JsonComentario;
import com.example.fernando.appcivico.estrutura.PostagemRetorno;
import com.example.fernando.appcivico.servicos.Avaliacao;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by fernando on 20/10/16.
 */
public class MinhasAvaliacoesFragment extends Fragment {
    private Avaliacao avaliacao;
    private ApplicationAppCivico applicationAppCivico;
    private Gson gson = new Gson();
    private HashMap<String,ConteudoPostagem> conteudoPostageHash = new HashMap<>();
    private int requestCount;
    private final String TAG = "TAG_MINHAS_AVALIACOES_CONTEUDO";
    private ArrayList<Comentario> comentariosList = new ArrayList<>();
    private Boolean inicializar = true;
    private MinhasAvaliacoesAdapter comentarioAdapter;
    private RecyclerView recyclerViewComentarios;
    private Boolean carregando = false;
    private ProgressBar progressBar;
    private ProgressBar progressBarComentariosContainer;
    private LinearLayoutManager linearLayoutManager;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int countOffset = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_minhas_avaliacoes,container,false);
        avaliacao = new Avaliacao(this.getActivity());
        applicationAppCivico = (ApplicationAppCivico) this.getActivity().getApplication();
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBarComentariosContainer = (ProgressBar) view.findViewById(R.id.progressBar_comentarios_container);

        applicationAppCivico = (ApplicationAppCivico)this.getActivity().getApplication();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewComentarios = (RecyclerView)view.findViewById(R.id.comentarios_recyclerview);
        recyclerViewComentarios.setLayoutManager(linearLayoutManager);
        recyclerViewComentarios.setItemAnimator(new DefaultItemAnimator());
        recyclerViewComentarios.setHasFixedSize(true);


        recyclerViewComentarios.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) { //check for scroll down
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if (((visibleItemCount + pastVisiblesItems) >= totalItemCount) && !carregando) {
                        carregando = true;
                        loadMoreRecyclerView();
                    }
                }
            }

        });
        progressBarComentariosContainer.setVisibility(View.VISIBLE);
        buscaPostagens();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadMoreRecyclerView() {
        if (comentarioAdapter != null && (comentarioAdapter.getItemCount()% 5 == 0 )) {
            inicializar = false;
            countOffset++;
            showProgressBar();
            buscaPostagens();
        }
    }

    public void buscaPostagens() {
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
                if(countOffset > 0) {
                    countOffset --;
                }

                if(inicializar) {
                    progressBarComentariosContainer.setVisibility(View.GONE);
                }

                Toast.makeText(MinhasAvaliacoesFragment.this.getActivity(),
                        MinhasAvaliacoesFragment.this.getActivity().getResources().getString(R.string.algo_deu_errado),
                        Toast.LENGTH_SHORT).show();
            }
        };

        StringRequest requisicaoPessoaPorCodigo = avaliacao.buscaPostagensPorAutor(countOffset,5,listener,codAutor,errorListener);
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
                    atribuiValoresRecyclerView();
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

                    if(countOffset > 0) {
                        countOffset --;
                    }

                    if(inicializar) {
                        progressBarComentariosContainer.setVisibility(View.GONE);
                    }

                    Toast.makeText(MinhasAvaliacoesFragment.this.getActivity(),
                            MinhasAvaliacoesFragment.this.getActivity().getResources().getString(R.string.algo_deu_errado),
                            Toast.LENGTH_SHORT).show();
                }
            };

            JsonObjectRequest jsonObjectRequest = avaliacao.buscaConteudoPostagem(codPostagem, codConteudoPostagem, listener, errorListener);
            requestQueue.add(jsonObjectRequest);
        }
    }

    public void atribuiValoresRecyclerView() {
        ArrayList<Comentario> comentarios = montaListaComentarios();
        this.comentariosList.addAll(comentarios);

        if(inicializar) {
            progressBarComentariosContainer.setVisibility(View.GONE);
            comentarioAdapter = new MinhasAvaliacoesAdapter(MinhasAvaliacoesFragment.this.getActivity(), this.comentariosList);
            recyclerViewComentarios.setAdapter(comentarioAdapter);
        } else {
            ReceiverThread receiverThread = new ReceiverThread();
            receiverThread.run();
        }
    }

    public ArrayList<Comentario> montaListaComentarios() {

        HashMap<String, ConteudoPostagem> conteudoPostagemHash = getConteudoPostagemHash();
        ArrayList<Comentario> comentariosList = new ArrayList<>();

        Set<String> chaves = conteudoPostagemHash.keySet();

        for(String codigoPostagem : chaves) {
            ConteudoPostagem conteudo = conteudoPostagemHash.get(codigoPostagem);
            String json = conteudo.getJSON();
            JsonComentario jsonComentario = gson.fromJson(json, JsonComentario.class);

            String nomeUsuario = jsonComentario.getNomeAutorComentario();
            String nomeFantasia = jsonComentario.getNomeFantasiaEstabelecimento();
            String texto = conteudo.getTexto();
            int valor = conteudo.getValor();
            String dataComentario = jsonComentario.getDataComentario();


            Comentario comentario = new Comentario();
            comentario.setCodigoPostagem(codigoPostagem);
            comentario.setNomeFantasiaEstabelecimento(nomeFantasia);
            comentario.setValor(valor);
            comentario.setNomeUsuario(nomeUsuario);
            comentario.setTexto(texto);
            comentario.setDataComentario(dataComentario);

            comentariosList.add(comentario);
        }

        return comentariosList;
    }

    public HashMap<String, ConteudoPostagem> getConteudoPostagemHash() {
        return conteudoPostageHash;
    }

    protected void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    protected void showProgressBar() {

        try {
            if (!isVisibleProgressBar()) {
                progressBar.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected boolean isVisibleProgressBar() {

        if (this.progressBar.getVisibility() == View.GONE || progressBar.getVisibility() == View.INVISIBLE) {
            return false;
        }

        return true;
    }

    private class ReceiverThread extends Thread {
        @Override
        public void run() {
            MinhasAvaliacoesFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    comentarioAdapter.notifyDataSetChanged();
                    carregando =false;
                    hideProgressBar();
                }
            });
        }
    }

}
