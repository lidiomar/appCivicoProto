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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.activities.ListaEstabelecimentosActivity;
import com.example.fernando.appcivico.adapters.ComentarioAdapter;
import com.example.fernando.appcivico.adapters.EstabelecimentoAdapter;
import com.example.fernando.appcivico.estrutura.Categoria;
import com.example.fernando.appcivico.estrutura.Comentario;
import com.example.fernando.appcivico.estrutura.Especialidade;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.example.fernando.appcivico.servicos.Servicos;
import com.example.fernando.appcivico.utils.StaticFunctions;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fernando on 15/10/16.
 */
public class ListaEstabelecimentosFragment extends Fragment {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerViewEstabelecimentos;
    private EstabelecimentoAdapter estabelecimentosAdapter;
    private TextView txtEmptyComentarios;
    private ProgressBar progressBar;
    private ArrayList<Estabelecimento> estabelecimentosList;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private Boolean carregando = false;
    private int countOffset = 0;
    private String uf;
    private String cidade;
    private String categoria;
    private String especialidade;
    private Servicos servicos;
    private ArrayList<Estabelecimento> estabelecimentosListLoadMoreReturn;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_estabelecimentos, container, false);


        Bundle extras = this.getActivity().getIntent().getExtras();

        Object[] estabelecimentosObj = (Object[]) extras.get("estabelecimentos");
        uf = (String)extras.get("uf");
        cidade = (String)extras.get("cidade");
        categoria = (String)extras.get("categoria");
        especialidade = (String)extras.get("especialidade");
        servicos = new Servicos(this.getActivity());
        txtEmptyComentarios = (TextView)view.findViewById(R.id.txt_empty_comentarios);

        if (estabelecimentosObj != null) {
            Estabelecimento[] estabelecimentos = new Estabelecimento[estabelecimentosObj.length];
            System.arraycopy(estabelecimentosObj, 0, estabelecimentos, 0, estabelecimentos.length);
            estabelecimentosList = new ArrayList<>(Arrays.asList(estabelecimentos));

        }

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewEstabelecimentos = (RecyclerView)view.findViewById(R.id.estabelecimentos_recyclerview);
        recyclerViewEstabelecimentos.setLayoutManager(linearLayoutManager);
        recyclerViewEstabelecimentos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEstabelecimentos.setHasFixedSize(true);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        recyclerViewEstabelecimentos.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) { //check for scroll down
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if (((visibleItemCount + pastVisiblesItems) >= totalItemCount) && !carregando) {
                        carregando = true;
                        carregaEstabelecimentos();
                    }
                }
            }

        });

        inicializaRecyclerView();

        return view;
        
    }

    public void inicializaRecyclerView() {
        showProgressBar();
        if(this.estabelecimentosList.size() > 0) {
            estabelecimentosAdapter = new EstabelecimentoAdapter(this.getActivity(), this.estabelecimentosList);
            recyclerViewEstabelecimentos.setAdapter(estabelecimentosAdapter);
            recyclerViewEstabelecimentos.setVisibility(View.VISIBLE);
            txtEmptyComentarios.setVisibility(View.GONE);
        }else {
            txtEmptyComentarios.setVisibility(View.VISIBLE);
        }
        hideProgressBar();
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

    public void carregaEstabelecimentos() {

        if (estabelecimentosAdapter != null && (estabelecimentosAdapter.getItemCount()% 20 == 0 )) {
            countOffset++;
            showProgressBar();
            buscarEstabelecimentos();
        }
    }

    private void buscarEstabelecimentos() {
        Response.Listener respListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();
                Estabelecimento[] estabelecimentos = gson.fromJson(String.valueOf(response), Estabelecimento[].class);
                estabelecimentosListLoadMoreReturn = new ArrayList<>(Arrays.asList(estabelecimentos));
                loadMoreRecyclerView();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListaEstabelecimentosFragment.this.getActivity(),ListaEstabelecimentosFragment.this.getActivity().getString(R.string.algo_deu_errado),Toast.LENGTH_SHORT).show();
            }
        };

        try {
            cidade = URLEncoder.encode(cidade, "UTF-8");
            uf = URLEncoder.encode(uf, "UTF-8");
            categoria = URLEncoder.encode(categoria, "UTF-8");
            especialidade = URLEncoder.encode(especialidade, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        servicos.consultaEstabelecimentos(cidade,uf,categoria,especialidade,20,countOffset,respListener,errorListener);
    }


    public void loadMoreRecyclerView() {
        ArrayList<Estabelecimento> estabelecimentosListLoadMoreReturn = getEstabelecimentosListLoadMoreReturn();
        this.estabelecimentosList.addAll(estabelecimentosListLoadMoreReturn);
        ReceiverThread receiverThread = new ReceiverThread();
        receiverThread.run();

    }

    public ArrayList<Estabelecimento> getEstabelecimentosListLoadMoreReturn() {
        return estabelecimentosListLoadMoreReturn;
    }

    private class ReceiverThread extends Thread {
        @Override
        public void run() {
            ListaEstabelecimentosFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    estabelecimentosAdapter.notifyDataSetChanged();
                    carregando = false;
                    hideProgressBar();
                }
            });
        }
    }
}
