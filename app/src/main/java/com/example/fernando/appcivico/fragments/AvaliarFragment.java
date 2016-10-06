package com.example.fernando.appcivico.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Response;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.example.fernando.appcivico.servicos.Servicos;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fernando on 06/10/16.
 */
public class AvaliarFragment extends Fragment {
    private Spinner spinnerEstabelecimentos;
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_avaliar, container, false);

        spinnerEstabelecimentos = (Spinner) view.findViewById(R.id.spinner_estabelecimentos);

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                ArrayList<Estabelecimento> arrayListEstabelecimento = new ArrayList<>();
                String responseReplaced = response.toString().replace("\"long\":", "\"longitude\":");
                Estabelecimento[] estabelecimentos = gson.fromJson(responseReplaced, Estabelecimento[].class);

                if (estabelecimentos.length > 0) {
                    arrayListEstabelecimento.addAll(Arrays.asList(estabelecimentos));

                    ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arrayListEstabelecimento);
                    spinnerEstabelecimentos.setAdapter(arrayAdapter);
                }
            }

        };

        Servicos servicos = new Servicos(getActivity());
        servicos.consultaEstabelecimento(listener);

        return view;
    }
}