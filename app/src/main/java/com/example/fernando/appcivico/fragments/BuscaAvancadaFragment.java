package com.example.fernando.appcivico.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.activities.ListaEstabelecimentosActivity;
import com.example.fernando.appcivico.estrutura.Categoria;
import com.example.fernando.appcivico.estrutura.Especialidade;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.example.fernando.appcivico.estrutura.Estado;
import com.example.fernando.appcivico.servicos.Servicos;
import com.example.fernando.appcivico.utils.Constants;
import com.example.fernando.appcivico.utils.StaticFunctions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fernando on 14/10/16.
 */
public class BuscaAvancadaFragment extends Fragment  {

    private HashMap<String,String[]> hashMapEstadoCidade = new HashMap<>();
    private ArrayList<String> arrayListEstados = new ArrayList<>();
    private ArrayList<String> arrayListCidades = new ArrayList<>();
    private Spinner spinnerBuscaUf;
    private Spinner spinnerBuscaCidade;
    private Spinner spinnerBuscaCategoria;
    private Spinner spinnerBuscaEspecialidades;
    private ArrayAdapter arrayAdapterCidades;
    private Button buttonPesquisar;
    private Servicos servicos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busca_avancada, container, false);
        spinnerBuscaUf = (Spinner)view.findViewById(R.id.spinner_busca_uf);
        spinnerBuscaCidade = (Spinner)view.findViewById(R.id.spinner_busca_cidade);
        spinnerBuscaCategoria = (Spinner)view.findViewById(R.id.spinner_busca_categoria);
        spinnerBuscaEspecialidades = (Spinner)view.findViewById(R.id.spinner_busca_especialiadade);
        buttonPesquisar = (Button)view.findViewById(R.id.button_busca_enviar);
        servicos = new Servicos(this.getActivity());
        String json = carregaEstadosCidades();
        Gson gson = new Gson();
        Estado[] estados = gson.fromJson(json, Estado[].class);

        for(Estado estado : estados) {
            hashMapEstadoCidade.put(estado.getSigla(),estado.getCidades());
            arrayListEstados.add(estado.getSigla());
        }

        ArrayAdapter arrayAdapterUf = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item, arrayListEstados);
        spinnerBuscaUf.setAdapter(arrayAdapterUf);

        arrayAdapterCidades = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item, arrayListCidades);
        spinnerBuscaCidade.setAdapter(arrayAdapterCidades);

        ArrayAdapter arrayAdapterCategoria = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item, Arrays.asList(Constants.CATEGORIAS));
        spinnerBuscaCategoria.setAdapter(arrayAdapterCategoria);

        ArrayAdapter arrayAdapterEspecialidade = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item, Arrays.asList(Constants.ESPECIALIDADES));
        spinnerBuscaEspecialidades.setAdapter(arrayAdapterEspecialidade);

        spinnerBuscaUf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String uf = (String)spinnerBuscaUf.getSelectedItem();
                String[] cidades = hashMapEstadoCidade.get(uf);
                List<String> strings = Arrays.asList(cidades);
                arrayListCidades.clear();
                arrayListCidades.addAll(strings);
                arrayAdapterCidades.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonPesquisar.setOnClickListener(new View.OnClickListener() {
            String uf;
            String cidade;
            String categoria;
            String especialidade;

            @Override
            public void onClick(View view) {
                Response.Listener respListener = new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Gson gson = new Gson();
                        Estabelecimento[] estabelecimentos = gson.fromJson(String.valueOf(response), Estabelecimento[].class);
                        Intent intent = new Intent(BuscaAvancadaFragment.this.getActivity(), ListaEstabelecimentosActivity.class);
                        intent.putExtra("estabelecimentos",estabelecimentos);
                        intent.putExtra("uf",uf);
                        intent.putExtra("cidade",cidade);
                        intent.putExtra("categoria",categoria);
                        intent.putExtra("especialidade",especialidade);
                        startActivity(intent);

                    }
                };

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BuscaAvancadaFragment.this.getActivity(),BuscaAvancadaFragment.this.getActivity().getString(R.string.algo_deu_errado),Toast.LENGTH_SHORT).show();
                    }
                };
                uf = (String) spinnerBuscaUf.getSelectedItem();
                cidade = (String)spinnerBuscaCidade.getSelectedItem();

                categoria = ((Categoria)spinnerBuscaCategoria.getSelectedItem()).getId();
                especialidade = ((Especialidade)spinnerBuscaEspecialidades.getSelectedItem()).getId();

                servicos.consultaEstabelecimentos(StaticFunctions.convertISOtoUTF8(cidade),uf,categoria,especialidade,20,0,respListener,errorListener);
            }
        });

        return view;
    }

    public String carregaEstadosCidades() {
        String json = null;
        try {

            InputStream is = this.getActivity().getAssets().open("estadoCidades.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
