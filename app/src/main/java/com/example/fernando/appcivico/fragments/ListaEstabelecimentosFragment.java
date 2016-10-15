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

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.adapters.EstabelecimentoAdapter;
import com.example.fernando.appcivico.estrutura.Estabelecimento;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fernando on 15/10/16.
 */
public class ListaEstabelecimentosFragment extends Fragment {
    private Estabelecimento[] estabelecimentos;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerViewEstabelecimentos;
    private EstabelecimentoAdapter estabelecimentosAdapter;
    private TextView txtEmptyComentarios;
    private ProgressBar progressBar;
    private ArrayList<Estabelecimento> estabelecimentosList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_estabelecimentos, container, false);


        Bundle extras = this.getActivity().getIntent().getExtras();

        Object[] estabelecimentosObj = (Object[]) extras.get("estabelecimentos");

        txtEmptyComentarios = (TextView)view.findViewById(R.id.txt_empty_comentarios);

        if (estabelecimentosObj != null) {
            estabelecimentos = new Estabelecimento[estabelecimentosObj.length];
            System.arraycopy(estabelecimentosObj, 0, estabelecimentos, 0, estabelecimentos.length);
            estabelecimentosList = new ArrayList<>(Arrays.asList(this.estabelecimentos));

        }

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewEstabelecimentos = (RecyclerView)view.findViewById(R.id.estabelecimentos_recyclerview);
        recyclerViewEstabelecimentos.setLayoutManager(linearLayoutManager);
        recyclerViewEstabelecimentos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEstabelecimentos.setHasFixedSize(true);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
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
}
