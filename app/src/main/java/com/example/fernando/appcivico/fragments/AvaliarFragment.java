package com.example.fernando.appcivico.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.activities.DialogAvaliarActivity;
import com.example.fernando.appcivico.adapters.ComentarioAdapter;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.Comentario;
import com.example.fernando.appcivico.estrutura.ConteudoPostagem;
import com.example.fernando.appcivico.estrutura.ConteudoPostagemRetorno;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.example.fernando.appcivico.estrutura.JsonComentario;
import com.example.fernando.appcivico.estrutura.PostagemRetorno;
import com.example.fernando.appcivico.servicos.Avaliacao;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by fernando on 06/10/16.
 */
public class AvaliarFragment extends Fragment {
    private Estabelecimento estabelecimento;
    private int requestCount = 0;
    private HashMap<String,ConteudoPostagem> conteudoPostagemHash = new HashMap<>();
    private HashMap<String,String> autoresPostagemHash = new HashMap<>();
    private RecyclerView recyclerViewComentarios;
    private LinearLayoutManager linearLayoutManager;
    private ComentarioAdapter comentarioAdapter;
    private Gson gson = new Gson();
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int countOffset = 0;
    private Boolean inicializar = true;
    private ArrayList<Comentario> comentariosList = new ArrayList<>();
    private ProgressBar progressBar;
    private ProgressBar progressBarComentariosContainer;
    private Boolean carregando = false;
    private Button buttonAvaliarDialog;
    private TextView textoInformativoComentarios;
    private ApplicationAppCivico applicationAppCivico;
    private Boolean buscarDoServidor = true;
    private Avaliacao avaliacao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_avaliar, container, false);
        Bundle extras = getActivity().getIntent().getExtras();
        estabelecimento = (Estabelecimento)extras.get("estabelecimento");
        avaliacao =  new Avaliacao(AvaliarFragment.this.getActivity());
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBarComentariosContainer = (ProgressBar) view.findViewById(R.id.progressBar_comentarios_container);

        applicationAppCivico = (ApplicationAppCivico)this.getActivity().getApplication();
        textoInformativoComentarios = (TextView)view.findViewById(R.id.texto_informativo_comentarios);

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
                    if (((visibleItemCount + pastVisiblesItems) >= totalItemCount) && !carregando  && buscarDoServidor) {
                        carregando = true;
                        carregaComentarios();
                    }
                }
            }

        });

        buttonAvaliarDialog = (Button)view.findViewById(R.id.button_avaliar_dialog);
        buttonAvaliarDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AvaliarFragment.this.getActivity(), DialogAvaliarActivity.class);
                intent.putExtra("estabelecimento", estabelecimento);
                startActivity(intent);
            }
        });
        progressBarComentariosContainer.setVisibility(View.VISIBLE);
        buscarComentarios();

        return view;
    }

    public void buscarComentarios() {
        requestCount = 0;

        Response.Listener responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final PostagemRetorno[] postagemRetornos = gson.fromJson(response, PostagemRetorno[].class);
                AvaliarFragment.this.getConteudoPostagemHash().clear();
                if(postagemRetornos != null) {
                    buscaConteudoPostagens(postagemRetornos);
                }else {
                    buscarDoServidor =false;
                    progressBarComentariosContainer.setVisibility(View.GONE);
                    textoInformativoComentarios.setVisibility(View.VISIBLE);
                    textoInformativoComentarios.setText(AvaliarFragment.this.getActivity().getString(R.string.nao_ha_comentarios));
                }
            }
        };

        Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AvaliarFragment.this.getActivity(),AvaliarFragment.this.getActivity().getString(R.string.algo_deu_errado),Toast.LENGTH_SHORT).show();
            }
        };

        avaliacao.buscaPostagens(countOffset,5,estabelecimento.getCodUnidade(),responseListener,responseErrorListener);
    }

    private void buscaConteudoPostagens(final PostagemRetorno[] postagemRetornos) {
        final RequestQueue requestQueue = Volley.newRequestQueue(AvaliarFragment.this.getActivity());
        for (PostagemRetorno postagemRetorno : postagemRetornos) {

            ConteudoPostagemRetorno[] conteudos = postagemRetorno.getConteudos();
            String codConteudoPostagem = conteudos[0].getCodConteudoPostagem();
            final String codPostagem = postagemRetorno.getCodPostagem();
            autoresPostagemHash.put(codPostagem, postagemRetorno.getCodAutor());

            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
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

                progressBarComentariosContainer.setVisibility(View.GONE);
                textoInformativoComentarios.setVisibility(View.VISIBLE);

                if (requestCount == postagemRetornos.length) {
                    atribuiValoresRecyclerView();
                }
            }
        });
    }

    public void atribuiValoresRecyclerView() {
        ArrayList<Comentario> comentarios = montaListaComentarios();
        this.comentariosList.addAll(comentarios);

        if(inicializar) {
            comentarioAdapter = new ComentarioAdapter(AvaliarFragment.this.getActivity(), this.comentariosList);
            recyclerViewComentarios.setAdapter(comentarioAdapter);
            textoInformativoComentarios.setText(this.getActivity().getString(R.string.comentarios_de_outros_usuarios));
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
            String texto = conteudo.getTexto();
            int valor = conteudo.getValor();
            String dataComentario = jsonComentario.getDataComentario();


            Comentario comentario = new Comentario();
            comentario.setCodigoPostagem(codigoPostagem);
            comentario.setValor(valor);
            comentario.setNomeUsuario(nomeUsuario);
            comentario.setTexto(texto);
            comentario.setDataComentario(dataComentario);

            comentariosList.add(comentario);
        }

        return comentariosList;
    }

    public HashMap<String, ConteudoPostagem> getConteudoPostagemHash() {
        return conteudoPostagemHash;
    }

    public void carregaComentarios() {

        if (comentarioAdapter != null && (comentarioAdapter.getItemCount()% 5 == 0 )) {
            inicializar = false;
            countOffset++;
            showProgressBar();
            buscarComentarios();
        }
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
            AvaliarFragment.this.getActivity().runOnUiThread(new Runnable() {
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
