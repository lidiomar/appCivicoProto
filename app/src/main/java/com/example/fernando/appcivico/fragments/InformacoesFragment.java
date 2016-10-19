package com.example.fernando.appcivico.fragments;

import android.animation.Animator;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.activities.AvaliarActivity;
import com.example.fernando.appcivico.activities.EscolherAcessoActivity;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.example.fernando.appcivico.estrutura.PostagemMedia;
import com.example.fernando.appcivico.estrutura.PostagemRetorno;
import com.example.fernando.appcivico.servicos.Avaliacao;
import com.google.gson.Gson;

import org.json.JSONObject;

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
    private TextView txtViewRetencao;
    private TextView txtViewFluxoClientela;
    private TextView txtViewAtendimentoUrgencia;
    private TextView txtViewAtendimentoAmbulatorial;
    private TextView txtViewCentroCirurgico;
    private TextView txtViewObstetra;
    private TextView txtViewNeonatal;
    private TextView txtViewDialise;
    private TextView txtViewDescricaoCompleta;
    private TextView txtViewCategoriaUnidade;
    private TextView txtViewLogradouro;
    private TextView txtViewNumero;
    private TextView txtViewBairro;
    private TextView txtViewCidade;
    private TextView txtViewUf;
    private TextView txtViewCep;
    private TextView txtViewTelefone;
    private TextView txtViewTurnoAtendimento;

    private LinearLayout linearlayoutEnderecoWrap;
    private LinearLayout linearlayoutEnderecoContent;
    private int linearlayoutEnderecoContentHeight;

    private LinearLayout linearlayoutInformacoesWrap;
    private LinearLayout linearlayoutInformacoesContent;
    private int linearlayoutInformacoesContentHeight;

    private PostagemRetorno postagemRetorno;
    private PostagemMedia postagemMedia;

    private RatingBar ratingBarReadonly;
    private Avaliacao avaliacao;
    private TextView txtNumeroAvaliacoes;
    private TextView txtMediaAvaliacoes;

    private Button buttonVisualizarAvaliacoes;
    private ApplicationAppCivico applicationAppCivico;
    private ImageView enderecoMore;
    private ImageView enderecoLess;
    private ImageView informacoesMore;
    private ImageView informacoesLess;
    private ProgressBar progressBarAvaliacao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informacoes, container,false);
        Bundle extras = getActivity().getIntent().getExtras();
        estabelecimento = (Estabelecimento)extras.get("estabelecimento");

        linearlayoutEnderecoWrap = (LinearLayout)view.findViewById(R.id.linearlayout_endereco_wrap);
        linearlayoutEnderecoContent = (LinearLayout)view.findViewById(R.id.linearlayout_endereco_content);
        linearlayoutEnderecoContentHeight = linearlayoutEnderecoContent.getHeight();
        progressBarAvaliacao = (ProgressBar)view.findViewById(R.id.progressBar_avaliacoes);

        linearlayoutInformacoesWrap = (LinearLayout)view.findViewById(R.id.linearlayout_informacoes_wrap);
        linearlayoutInformacoesContent = (LinearLayout)view.findViewById(R.id.linearlayout_informacoes_content);
        linearlayoutInformacoesContentHeight = linearlayoutInformacoesContent.getHeight();

        txtViewNomeFantasia  = (TextView)view.findViewById(R.id.nome_fantasia);
        txtViewNatureza = (TextView)view.findViewById(R.id.natureza);
        txtViewTipoUnidade = (TextView)view.findViewById(R.id.tipo_unidade);
        txtViewEsferaAdministrativa = (TextView)view.findViewById(R.id.esfera_administrativa);
        txtViewVinculoSus = (TextView)view.findViewById(R.id.vinculo_sus);
        /*txtViewRetencao = (TextView)view.findViewById(R.id.retencao);*/
        txtViewFluxoClientela = (TextView)view.findViewById(R.id.fluxo_clientela);
        txtViewAtendimentoUrgencia = (TextView)view.findViewById(R.id.tem_atendimento_urgencia);
        txtViewAtendimentoAmbulatorial = (TextView)view.findViewById(R.id.tem_atendimento_ambulatorial);
        txtViewCentroCirurgico = (TextView)view.findViewById(R.id.tem_centro_cirurgico);
        txtViewObstetra = (TextView)view.findViewById(R.id.tem_obstetra);
        txtViewNeonatal = (TextView)view.findViewById(R.id.tem_neonatal);
        txtViewDialise = (TextView)view.findViewById(R.id.tem_dialise);
        /*txtViewDescricaoCompleta = (TextView)view.findViewById(R.id.descricao_completa);*/
        txtViewCategoriaUnidade = (TextView)view.findViewById(R.id.categoria_unidade);
        txtViewLogradouro = (TextView)view.findViewById(R.id.logradouro);
        txtViewNumero = (TextView)view.findViewById(R.id.numero);
        txtViewBairro = (TextView)view.findViewById(R.id.bairro);
        txtViewCidade = (TextView)view.findViewById(R.id.cidade);
        txtViewUf = (TextView)view.findViewById(R.id.uf);
        txtViewCep  = (TextView)view.findViewById(R.id.cep_estabelecimento);
        txtViewTelefone = (TextView)view.findViewById(R.id.telefone);
        txtViewTurnoAtendimento = (TextView)view.findViewById(R.id.turno_atendimento);

        ratingBarReadonly = (RatingBar)view.findViewById(R.id.rating_avaliacao_readonly);
        Drawable progress = ratingBarReadonly.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.rgb(11111111,01011010,00000000));

        txtNumeroAvaliacoes = (TextView)view.findViewById(R.id.text_numero_avaliacoes);
        txtMediaAvaliacoes = (TextView)view.findViewById(R.id.text_media_avaliacoes);
        buttonVisualizarAvaliacoes = (Button)view.findViewById(R.id.button_deixe_sua_avaliacao);

        enderecoMore = (ImageView)view.findViewById(R.id.endereco_more);
        enderecoLess = (ImageView)view.findViewById(R.id.endereco_less);

        informacoesMore = (ImageView)view.findViewById(R.id.informacoes_more);
        informacoesLess = (ImageView)view.findViewById(R.id.informacoes_less);

        applicationAppCivico = (ApplicationAppCivico)this.getActivity().getApplication();

        buttonVisualizarAvaliacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(applicationAppCivico.usuarioAutenticado()) {
                    Response.Listener respListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Intent intent = new Intent(InformacoesFragment.this.getActivity(), AvaliarActivity.class);
                            intent.putExtra("estabelecimento",estabelecimento);
                            startActivity(intent);
                        }
                    };

                    Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(InformacoesFragment.this.getActivity(),InformacoesFragment.this.getActivity().getString(R.string.algo_deu_errado),Toast.LENGTH_LONG).show();
                        }
                    };
                    ApplicationAppCivico applicationAppCivico = (ApplicationAppCivico)InformacoesFragment.this.getActivity().getApplication();
                    avaliacao.buscaPostagem(0,1,estabelecimento.getCodUnidade(),applicationAppCivico.getUsuarioAutenticado().getCod(), respListener,responseErrorListener);
                }else {
                    Intent intent = new Intent(InformacoesFragment.this.getActivity(), EscolherAcessoActivity.class);
                    startActivity(intent);
                }
            }
        });

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

        linearlayoutEnderecoContent.setVisibility(View.GONE);

        linearlayoutEnderecoWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(linearlayoutEnderecoContent.getVisibility() == View.GONE) {

                    linearlayoutEnderecoContent.setVisibility(View.VISIBLE);
                    linearlayoutEnderecoContent.setAlpha(0.0f);
                    linearlayoutEnderecoContent.animate()
                            .setDuration(400)
                            .translationY(linearlayoutEnderecoContentHeight)
                            .alpha(1.0f)
                            .setListener(null);
                    enderecoLess.setVisibility(View.VISIBLE);
                    enderecoMore.setVisibility(View.GONE);
                }else if(linearlayoutEnderecoContent.getVisibility() == View.VISIBLE) {

                    linearlayoutEnderecoContent.animate()
                            .setDuration(400)
                            .alpha(0.0f)
                            .translationY(0)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    linearlayoutEnderecoContent.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                    enderecoMore.setVisibility(View.VISIBLE);
                    enderecoLess.setVisibility(View.GONE);
                }
            }
        });

        linearlayoutInformacoesContent.setVisibility(View.GONE);

        linearlayoutInformacoesWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(linearlayoutInformacoesContent.getVisibility() == View.GONE) {
                    linearlayoutInformacoesContent.setVisibility(View.VISIBLE);
                    linearlayoutInformacoesContent.setAlpha(0.0f);
                    linearlayoutInformacoesContent.animate()
                            .setDuration(400)
                            .translationY(linearlayoutInformacoesContentHeight)
                            .alpha(1.0f)
                            .setListener(null);

                    informacoesLess.setVisibility(View.VISIBLE);
                    informacoesMore.setVisibility(View.GONE);

                }else if(linearlayoutInformacoesContent.getVisibility() == View.VISIBLE) {
                    linearlayoutInformacoesContent.animate()
                            .setDuration(400)
                            .alpha(0.0f)
                            .translationY(0)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    linearlayoutInformacoesContent.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });

                    informacoesMore.setVisibility(View.VISIBLE);
                    informacoesLess.setVisibility(View.GONE);
                }
            }
        });
        this.buscaMedia();
    }

    public void inicializaAvaliacoesMedia() {
        Float media = 0f;
        int contagem = 0;

        if(postagemMedia != null) {
            media = postagemMedia.getMedia();
            contagem = postagemMedia.getContagem();
        }

        ratingBarReadonly.setRating(media);

        if(contagem <= 0) {
            txtNumeroAvaliacoes.setText(this.getActivity().getString(R.string.sem_avaliacoes));
        }else {
            ratingBarReadonly.setVisibility(View.VISIBLE);
            txtMediaAvaliacoes.setVisibility(View.VISIBLE);
            txtMediaAvaliacoes.setText(String.format(this.getActivity().getString(R.string.media_das_avaliacoes_x),String.valueOf(Math.ceil(media))));
            if(contagem > 1) {
                txtNumeroAvaliacoes.setText(String.format(this.getActivity().getString(R.string.x_pessoas_avaliaram), String.valueOf(contagem)));
            }else {
                txtNumeroAvaliacoes.setText(String.format(this.getActivity().getString(R.string.x_pessoa_avaliou), String.valueOf(contagem)));
            }
        }
    }

    public void buscaMedia() {
        Response.Listener respListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                postagemMedia = gson.fromJson(response.toString(), PostagemMedia.class);
                inicializaAvaliacoesMedia();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("logError",new String(error.networkResponse.data));
            }
        };
        progressBarAvaliacao.setVisibility(View.VISIBLE);
        avaliacao.buscaMediaAvaliacoes(estabelecimento.getCodUnidade(),respListener, errorListener);
        avaliacao.getRequestQueue().addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                progressBarAvaliacao.setVisibility(View.GONE);
                buttonVisualizarAvaliacoes.setVisibility(View.VISIBLE);
                txtNumeroAvaliacoes.setVisibility(View.VISIBLE);
            }
        });
    }

}
