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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
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

    private Button buttonDeixeSuaAvaliação;
    private ApplicationAppCivico applicationAppCivico;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informacoes, container,false);

        String s = "{\n" +
                "    \"codCnes\": 5467799,\n" +
                "    \"codUnidade\": \"4209105467799\",\n" +
                "    \"codIbge\": 420910,\n" +
                "    \"cnpj\": \"08902792000100\",\n" +
                "    \"nomeFantasia\": \"IAPSI ISNTITUTO DE AVALIACAO PSICOLOGICA E PSICOTERAPIA\",\n" +
                "    \"natureza\": \"Empresa\",\n" +
                "    \"tipoUnidade\": \"DIAGNOSE E TERAPIA\",\n" +
                "    \"esferaAdministrativa\": \"Privada\",\n" +
                "    \"vinculoSus\": \"Não\",\n" +
                "    \"retencao\": \"Unidade Privada Lucrativa***\",\n" +
                "    \"fluxoClientela\": \"Atendimento de demanda espontânea\",\n" +
                "    \"origemGeografica\": \"CNES_GEO\",\n" +
                "    \"temAtendimentoUrgencia\": \"Não\",\n" +
                "    \"temAtendimentoAmbulatorial\": \"Sim\",\n" +
                "    \"temCentroCirurgico\": \"Não\",\n" +
                "    \"temObstetra\": \"Não\",\n" +
                "    \"temNeoNatal\": \"Não\",\n" +
                "    \"temDialise\": \"Sim\",\n" +
                "    \"descricaoCompleta\": \"IAPSI ISNTITUTO DE AVALIACAO PSICOLOGICA E PSICOTERAPIA  EMPRESA PRIVADA    \",\n" +
                "    \"tipoUnidadeCnes\": \"UNIDADE DE APOIO DIAGNOSE E TERAPIA (SADT ISOLADO)\",\n" +
                "    \"categoriaUnidade\": \"LABORATÓRIO\",\n" +
                "    \"logradouro\": \"RUA OTTOKAR DOERFFEL\",\n" +
                "    \"numero\": \"929\",\n" +
                "    \"bairro\": \"ANITA GARIBALDI\",\n" +
                "    \"cidade\": \"JOINVILLE\",\n" +
                "    \"uf\": \"SC\",\n" +
                "    \"cep\": \"89203307\",\n" +
                "    \"telefone\": \"(47) 38010881\",\n" +
                "    \"turnoAtendimento\": \"Atendimento nos turnos da manhã e à tarde.\",\n" +
                "    \"lat\": -26.31644,\n" +
                "    \"long\": -48.86307\n" +
                "  }";

        String s2 = "{\n" +
                "        \"codCnes\": 2521431,\n" +
                "            \"codUnidade\": \"4209102521431\",\n" +
                "            \"codIbge\": 420910,\n" +
                "            \"cnpj\": \"82602327000360\",\n" +
                "            \"nomeFantasia\": \"CENTRO HOSPITALAR UNIMED\",\n" +
                "            \"natureza\": \"Cooperativa\",\n" +
                "            \"tipoUnidade\": \"HOSPITAL GERAL\",\n" +
                "            \"esferaAdministrativa\": \"Privada\",\n" +
                "            \"vinculoSus\": \"Sim\",\n" +
                "            \"retencao\": \"Unidade Privada Lucrativa***\",\n" +
                "            \"fluxoClientela\": \"Atendimento de demanda espontânea e referenciada\",\n" +
                "            \"origemGeografica\": \"CNES_GEO\",\n" +
                "            \"temAtendimentoUrgencia\": \"Sim\",\n" +
                "            \"temAtendimentoAmbulatorial\": \"Não\",\n" +
                "            \"temCentroCirurgico\": \"Sim\",\n" +
                "            \"temObstetra\": \"Sim\",\n" +
                "            \"temNeoNatal\": \"Sim\",\n" +
                "            \"temDialise\": \"Sim\",\n" +
                "            \"descricaoCompleta\": \"CENTRO HOSPITALAR UNIMED  COOPERATIVA TRANSPLANTES   DIAGNOSTICA EXAMES MICROBIOLOGICOS EXAME  ELETROCARDIOGRAFICO  RETIRADA GLOBO OCULAR HUMANO TRANSPLANTE DOACAO CAPTACAO ORGAOS TECIDOS CARDIOLOGIA INTERVENCIONISTA (HEMODINAMICA) EXAMES SOROLOGICOS IMUNOLOGICOS NEONATAL ENTERAL EXAMES HORMONAIS DO APARELHO RESPIRATORIO EXAME ELETROENCEFALOGRAFICO CIRURGICA EXAMES COPROLOGICOS TOMOGRAFIA COMPUTADORIZADA EXAMES BIOQUIMICOS  UROANALISE ADULTO DO APARELHO DIGESTIVO EXAMES TOXICOLOGICOS MONITORIZACAO TERAPEUTICA RESSONANCIA MAGNETICA ULTRASONOGRAFIA RADIOLOGIA EXAMES IMUNOHEMATOLOGICOS  EXAMES HEMATOLOGICOS HEMOSTASIA   MEDICO CITOPATOLOGISTA  MEDICO GASTROENTEROLOGISTA  MEDICO GERIATRA  FARMACEUTICO  MEDICO PEDIATRA  MEDICO PSIQUIATRA  MEDICO FISIATRA  FISIOTERAPEUTA GERAL  MEDICO CIRURGIAO TORACICO  MEDICO HOMEOPATA  MEDICO GINECOLOGISTA E OBSTETRA  MEDICO ACUPUNTURISTA  MEDICO INFECTOLOGISTA  MEDICO CIRURGIAO GERAL  MEDICO EM MEDICINA NUCLEAR  MEDICO CIRURGIAO PEDIATRICO  MEDICO NEUROLOGISTA  CIRURGIAO DENTISTA - TRAUMATOLOGISTA BUCOMAXILOFACIAL  MEDICO DERMATOLOGISTA  MEDICO CIRURGIAO DE CABECA E PESCOCO  MEDICO NEUROCIRURGIAO  ENFERMEIRO  MEDICO UROLOGISTA  MEDICO OFTALMOLOGISTA  MEDICO CANCEROLOGISTA CIRURGICO  MEDICO PNEUMOLOGISTA  MEDICO NEFROLOGISTA  MEDICO ORTOPEDISTA E TRAUMATOLOGISTA  MEDICO CARDIOLOGISTA  MEDICO ANATOMOPATOLOGISTA  NUTRICIONISTA  MEDICO CIRURGIAO CARDIOVASCULAR  MEDICO ANESTESIOLOGISTA  FARMACEUTICO ANALISTA CLINICO  MEDICO HEMATOLOGISTA  MEDICO HEMOTERAPEUTA  MEDICO ONCOLOGISTA CLINICO  MEDICO RADIOTERAPEUTA  MEDICO COLOPROCTOLOGISTA  MEDICO EM RADIOLOGIA E DIAGNOSTICO POR IMAGEM  MEDICO ENDOCRINOLOGISTA E METABOLOGISTA  MEDICO OTORRINOLARINGOLOGISTA  MEDICO CLINICO  MEDICO CIRURGIAO PLASTICO  MEDICO EM MEDICINA DE TRAFEGO  MEDICO PATOLOGISTA CLINICO / MEDICINA LABORATORIAL  MEDICO EM MEDICINA INTENSIVA  MEDICO REUMATOLOGISTA  MEDICO EM CIRURGIA VASCULAR  \",\n" +
                "            \"tipoUnidadeCnes\": \"HOSPITAL GERAL\",\n" +
                "            \"categoriaUnidade\": \"HOSPITAL\",\n" +
                "            \"logradouro\": \"RUA ORESTES GUIMARAES\",\n" +
                "            \"numero\": \"905\",\n" +
                "            \"bairro\": \"AMERICA\",\n" +
                "            \"cidade\": \"JOINVILLE\",\n" +
                "            \"uf\": \"SC\",\n" +
                "            \"cep\": \"89204060\",\n" +
                "            \"telefone\": \"(47) 34419555\",\n" +
                "            \"turnoAtendimento\": \"Atendimento contínuo de 24 horas/ dia (plantão: inclui sábados, domingos e feriados).\",\n" +
                "            \"lat\": -26.28946,\n" +
                "            \"long\": -48.84534\n" +
                "    }";

        /*Bundle extras = getActivity().getIntent().getExtras();
        estabelecimento = (Estabelecimento)extras.get("estabelecimento");*/

        linearlayoutEnderecoWrap = (LinearLayout)view.findViewById(R.id.linearlayout_endereco_wrap);
        linearlayoutEnderecoContent = (LinearLayout)view.findViewById(R.id.linearlayout_endereco_content);
        linearlayoutEnderecoContentHeight = linearlayoutEnderecoContent.getHeight();


        linearlayoutInformacoesWrap = (LinearLayout)view.findViewById(R.id.linearlayout_informacoes_wrap);
        linearlayoutInformacoesContent = (LinearLayout)view.findViewById(R.id.linearlayout_informacoes_content);
        linearlayoutInformacoesContentHeight = linearlayoutInformacoesContent.getHeight();

        Gson gson = new Gson();
        String stringJson = String.valueOf(s).replace("\"long\":", "\"longitude\":");
        estabelecimento = gson.fromJson(stringJson, Estabelecimento.class);

        txtViewNomeFantasia  = (TextView)view.findViewById(R.id.nome_fantasia);
        txtViewNatureza = (TextView)view.findViewById(R.id.natureza);
        /*txtViewTipoUnidade = (TextView)view.findViewById(R.id.tipo_unidade);*/
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
        /*txtViewCategoriaUnidade = (TextView)view.findViewById(R.id.categoria_unidade);*/
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
        buttonDeixeSuaAvaliação = (Button)view.findViewById(R.id.button_deixe_sua_avaliacao);

        applicationAppCivico = (ApplicationAppCivico)this.getActivity().getApplication();

        buttonDeixeSuaAvaliação.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(applicationAppCivico.usuarioAutenticado()) {
                    Response.Listener respListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int statusCodeResponse = avaliacao.getBuscaPostagemStatusCode();
                            if(statusCodeResponse == 204) {
                                Intent intent = new Intent(InformacoesFragment.this.getActivity(), AvaliarActivity.class);
                                intent.putExtra("estabelecimento",estabelecimento);
                                startActivity(intent);
                            }else if(statusCodeResponse == 200) {
                                Toast.makeText(InformacoesFragment.this.getActivity(),InformacoesFragment.this.getActivity().getString(R.string.ja_existe_avaliacao),Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(InformacoesFragment.this.getActivity(),InformacoesFragment.this.getActivity().getString(R.string.algo_deu_errado),Toast.LENGTH_LONG).show();
                            }
                        }
                    };

                    Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(InformacoesFragment.this.getActivity(),InformacoesFragment.this.getActivity().getString(R.string.algo_deu_errado),Toast.LENGTH_LONG).show();
                        }
                    };
                    ApplicationAppCivico applicationAppCivico = (ApplicationAppCivico)InformacoesFragment.this.getActivity().getApplication();
                    avaliacao.buscaPostagens(0,1,estabelecimento.getCodUnidade(),applicationAppCivico.getUsuarioAutenticado().getCod(), respListener,responseErrorListener);

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

    public void inicializaCampos() {
        txtViewNomeFantasia.setText(estabelecimento.getNomeFantasia());
        txtViewNatureza.setText(estabelecimento.getNatureza());
        /*txtViewTipoUnidade.setText(String.format(this.getActivity().getString(R.string.tipo_unidade),estabelecimento.getTipoUnidade()));*/
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
        /*txtViewCategoriaUnidade.setText(String.format(this.getActivity().getString(R.string.categoria_unidade),estabelecimento.getCategoriaUnidade()));*/
        txtViewLogradouro.setText(String.format(this.getActivity().getString(R.string.logradouro),estabelecimento.getLogradouro()));
        txtViewNumero.setText(String.format(this.getActivity().getString(R.string.numero),estabelecimento.getNumero()));
        txtViewBairro.setText(String.format(this.getActivity().getString(R.string.bairro),estabelecimento.getBairro()));
        txtViewCidade.setText(String.format(this.getActivity().getString(R.string.cidade),estabelecimento.getCidade()));
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
            txtMediaAvaliacoes.setVisibility(View.VISIBLE);
            txtMediaAvaliacoes.setText(String.format(this.getActivity().getString(R.string.media_das_avaliacoes_x),String.valueOf(media)));
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

        avaliacao.buscaMediaAvaliacoes(estabelecimento.getCodUnidade(),respListener, errorListener);
    }

}
