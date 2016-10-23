package com.example.fernando.appcivico.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.example.fernando.appcivico.estrutura.PostagemMedia;
import com.example.fernando.appcivico.servicos.Avaliacao;

/**
 * Created by fernando on 11/10/16.
 */
public class InformacoesFragment extends Fragment{
    private Estabelecimento estabelecimento;
    private TextView txtViewNomeFantasia;
    private TextView txtViewNatureza;
    private TextView txtViewTipoUnidade;
    private TextView txtViewEsferaAdministrativa;
    private TextView txtViewVinculoSus;
    private TextView txtViewFluxoClientela;
    private TextView txtViewAtendimentoUrgencia;
    private TextView txtViewAtendimentoAmbulatorial;
    private TextView txtViewCentroCirurgico;
    private TextView txtViewObstetra;
    private TextView txtViewNeonatal;
    private TextView txtViewDialise;
    private TextView txtViewCategoriaUnidade;
    private TextView txtViewLogradouro;
    private TextView txtViewNumero;
    private TextView txtViewBairro;
    private TextView txtViewCidade;
    private TextView txtViewUf;
    private TextView txtViewCep;
    private TextView txtViewTelefone;
    private TextView txtViewTurnoAtendimento;
    private Avaliacao avaliacao;

    private ApplicationAppCivico applicationAppCivico;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informacoes, container,false);
        Bundle extras = getActivity().getIntent().getExtras();
        estabelecimento = (Estabelecimento)extras.get("estabelecimento");


        txtViewNomeFantasia  = (TextView)view.findViewById(R.id.nome_fantasia);
        txtViewNatureza = (TextView)view.findViewById(R.id.natureza);
        txtViewTipoUnidade = (TextView)view.findViewById(R.id.tipo_unidade);
        txtViewEsferaAdministrativa = (TextView)view.findViewById(R.id.esfera_administrativa);
        txtViewVinculoSus = (TextView)view.findViewById(R.id.vinculo_sus);
        txtViewFluxoClientela = (TextView)view.findViewById(R.id.fluxo_clientela);
        txtViewAtendimentoUrgencia = (TextView)view.findViewById(R.id.tem_atendimento_urgencia);
        txtViewAtendimentoAmbulatorial = (TextView)view.findViewById(R.id.tem_atendimento_ambulatorial);
        txtViewCentroCirurgico = (TextView)view.findViewById(R.id.tem_centro_cirurgico);
        txtViewObstetra = (TextView)view.findViewById(R.id.tem_obstetra);
        txtViewNeonatal = (TextView)view.findViewById(R.id.tem_neonatal);
        txtViewDialise = (TextView)view.findViewById(R.id.tem_dialise);
        txtViewCategoriaUnidade = (TextView)view.findViewById(R.id.categoria_unidade);
        txtViewLogradouro = (TextView)view.findViewById(R.id.logradouro);
        txtViewNumero = (TextView)view.findViewById(R.id.numero);
        txtViewBairro = (TextView)view.findViewById(R.id.bairro);
        txtViewCidade = (TextView)view.findViewById(R.id.cidade);
        txtViewUf = (TextView)view.findViewById(R.id.uf);
        txtViewCep  = (TextView)view.findViewById(R.id.cep_estabelecimento);
        txtViewTelefone = (TextView)view.findViewById(R.id.telefone);
        txtViewTurnoAtendimento = (TextView)view.findViewById(R.id.turno_atendimento);

        applicationAppCivico = (ApplicationAppCivico)this.getActivity().getApplication();

        avaliacao = new Avaliacao(this.getActivity());
        this.inicializaCampos();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        inicializaCampos();
    }

    public void inicializaCampos() {
        txtViewNomeFantasia.setText(estabelecimento.getNomeFantasia());
        txtViewNatureza.setText(estabelecimento.getNatureza());
        txtViewTipoUnidade.setText(String.format(this.getString(R.string.tipo_unidade),
                estabelecimento.getTipoUnidade().substring(0,1).toUpperCase()+
                        estabelecimento.getTipoUnidade().substring(1).toLowerCase()
        ));
        txtViewEsferaAdministrativa.setText(String.format(this.getActivity().getString(R.string.esfera_administrativa),estabelecimento.getEsferaAdministrativa()));
        txtViewVinculoSus.setText(String.format(this.getActivity().getString(R.string.vinculo_sus),estabelecimento.getVinculoSus()));
        /*txtViewRetencao.setText(String.format(this.getActivity().getString(R.string.retencao),estabelecimento.getRetencao()));*/
        txtViewFluxoClientela.setText(String.format(this.getActivity().getString(R.string.fluxo_clientela),estabelecimento.getFluxoClientela()));
        txtViewAtendimentoUrgencia.setText(String.format(this.getActivity().getString(R.string.tem_atendimento_urgencia),estabelecimento.getTemAtendimentoUrgencia()));
        txtViewAtendimentoAmbulatorial.setText(String.format(this.getActivity().getString(R.string.tem_atendimento_ambulatorial),estabelecimento.getTemAtendimentoAmbulatorial()));
        txtViewCentroCirurgico.setText(String.format(this.getActivity().getString(R.string.tem_centro_cirurgico),estabelecimento.getTemCentroCirurgico()));
        txtViewObstetra.setText(String.format(this.getActivity().getString(R.string.tem_obstetra),estabelecimento.getTemObstetra()));
        txtViewNeonatal.setText(String.format(this.getActivity().getString(R.string.tem_neonatal),estabelecimento.getTemNeoNatal()));
        txtViewDialise.setText(String.format(this.getActivity().getString(R.string.tem_dialise),estabelecimento.getTemDialise()));
        /*txtViewDescricaoCompleta.setText(String.format(this.getActivity().getString(R.string.descricao_completa),estabelecimento.getDescricaoCompleta()));*/
        txtViewCategoriaUnidade.setText(String.format(this.getString(R.string.categoria_unidade),
                estabelecimento.getCategoriaUnidade().substring(0,1).toUpperCase()+
                estabelecimento.getCategoriaUnidade().substring(1).toLowerCase()
        ));
        txtViewLogradouro.setText(String.format(this.getActivity().getString(R.string.logradouro),
                estabelecimento.getLogradouro().substring(0,1).toUpperCase()+
                estabelecimento.getLogradouro().substring(1).toLowerCase()
        ));

        txtViewNumero.setText(String.format(this.getActivity().getString(R.string.numero),estabelecimento.getNumero()));
        txtViewBairro.setText(String.format(this.getActivity().getString(R.string.bairro),
                estabelecimento.getBairro().substring(0,1).toUpperCase()+
                estabelecimento.getBairro().substring(1).toLowerCase()
        ));
        txtViewCidade.setText(String.format(this.getActivity().getString(R.string.cidade),
                estabelecimento.getCidade().substring(0,1).toUpperCase()+
                estabelecimento.getCidade().substring(1).toLowerCase()
        ));
        txtViewUf.setText(String.format(this.getActivity().getString(R.string.uf),estabelecimento.getUf()));
        txtViewCep.setText(String.format(this.getActivity().getString(R.string.cep_estabelecimento),estabelecimento.getCep()));
        txtViewTelefone.setText(String.format(this.getActivity().getString(R.string.telefone),estabelecimento.getTelefone()));
        txtViewTurnoAtendimento.setText(String.format(this.getActivity().getString(R.string.turno_atendimento),estabelecimento.getTurnoAtendimento()));
    }



}
