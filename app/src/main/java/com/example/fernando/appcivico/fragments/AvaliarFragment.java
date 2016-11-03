package com.example.fernando.appcivico.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.example.fernando.appcivico.activities.DialogAvaliarActivity;
import com.example.fernando.appcivico.activities.EscolherAcessoActivity;
import com.example.fernando.appcivico.activities.InformacoesActivity;
import com.example.fernando.appcivico.adapters.ComentarioAdapter;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.Comentario;
import com.example.fernando.appcivico.estrutura.ConteudoPostagem;
import com.example.fernando.appcivico.estrutura.ConteudoPostagemRetorno;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.example.fernando.appcivico.estrutura.JsonComentario;
import com.example.fernando.appcivico.estrutura.PostagemMedia;
import com.example.fernando.appcivico.estrutura.PostagemRetorno;
import com.example.fernando.appcivico.servicos.Avaliacao;
import com.example.fernando.appcivico.utils.Constants;
import com.example.fernando.appcivico.utils.MyAlertDialogFragment;
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
    private Boolean carregando = false;
    private Button buttonAvaliarDialog;
    private TextView textoInformativoComentarios;
    private ApplicationAppCivico applicationAppCivico;
    private Boolean buscarDoServidor = true;
    private Avaliacao avaliacao;
    private PostagemMedia postagemMedia;
    private RatingBar ratingBarReadonly;
    private TextView txtNumeroAvaliacoes;
    private TextView txtMediaAvaliacoes;
    private MyAlertDialogFragment myAlertDialogFragment;
    private LinearLayout linearLayoutMediaContainer;
    private RequestQueue requestQueue;
    private PostagemRetorno[] postagemRetornos = null;
    private final String TAG = "TAG_AVALIAR";
    private View view = null;
    private int requestCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle extras = getActivity().getIntent().getExtras();
        estabelecimento = (Estabelecimento)extras.get("estabelecimento");
        avaliacao =  new Avaliacao(AvaliarFragment.this.getActivity());
        requestQueue = avaliacao.getRequestQueue();
        applicationAppCivico = (ApplicationAppCivico)this.getActivity().getApplication();

        if(applicationAppCivico.usuarioAutenticado()) {
            view = inflater.inflate(R.layout.fragment_avaliar, container, false);
            inicializaCampos();
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

            buscarComentarios();
        }else {
            view = inflater.inflate(R.layout.fragment_avaliar_disconnected, container, false);
            inicializaCampos();
            buscaMedia();
        }

        myAlertDialogFragment = MyAlertDialogFragment.newInstance("","");
        myAlertDialogFragment.show(this.getFragmentManager(),"");



        return view;
    }

    public void inicializaCampos() {
        buttonAvaliarDialog = (Button)view.findViewById(R.id.button_avaliar_dialog);

        LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        l.setMargins(5,5,5,5);
        buttonAvaliarDialog.setLayoutParams(l);

        buttonAvaliarDialog.setBackgroundResource(R.color.colorPrimary);
        buttonAvaliarDialog.setTextColor(ContextCompat.getColor(this.getActivity(),android.R.color.white));

        linearLayoutMediaContainer = (LinearLayout)view.findViewById(R.id.media_container);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        ratingBarReadonly = (RatingBar)view.findViewById(R.id.rating_avaliacao_readonly);
        Drawable progress = ratingBarReadonly.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.rgb(11111111,01011010,00000000));
        txtNumeroAvaliacoes = (TextView) view.findViewById(R.id.text_numero_avaliacoes);
        txtMediaAvaliacoes = (TextView) view.findViewById(R.id.text_media_avaliacoes);


        textoInformativoComentarios = (TextView)view.findViewById(R.id.texto_informativo_comentarios);

        buttonAvaliarDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(applicationAppCivico.usuarioAutenticado()) {
                    loadDialogAvaliar();
                }else {
                    loadEscolherAcesso();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        Intent refresh = new Intent(this.getActivity(), InformacoesActivity.class);
        refresh.putExtra("estabelecimento",estabelecimento);
        startActivity(refresh);
        this.getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        this.getActivity().finish();

    }



    public void buscarComentarios() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
        Response.Listener responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                postagemRetornos = gson.fromJson(response, PostagemRetorno[].class);
                AvaliarFragment.this.getConteudoPostagemHash().clear();
                setPostagemRetornos(postagemRetornos);
            }
        };

        Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("erro: ", AvaliarFragment.this.getString(R.string.nao_foi_possivel_carregar_os_comentarios));
            }
        };

        StringRequest stringRequest = avaliacao.buscaPostagens(countOffset, 5, estabelecimento.getCodUnidade(), responseListener, responseErrorListener);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                buscaConteudoPostagens();
            }
        });
    }

    private void buscaConteudoPostagens() {
        requestCount = 0;

        if(postagemRetornos != null) {

            final RequestQueue requestQueuePostagens = Volley.newRequestQueue(this.getActivity());
            requestQueuePostagens.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                @Override
                public void onRequestFinished(Request<Object> request) {
                    requestCount = requestCount + 1;
                    textoInformativoComentarios.setVisibility(View.VISIBLE);

                    if (requestCount == postagemRetornos.length) {
                        atribuiValoresRecyclerView();
                    }
                }
            });

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

                Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestQueuePostagens.cancelAll(TAG);
                        atribuiValoresSemRecyclerView();
                        Toast.makeText(AvaliarFragment.this.getActivity(),AvaliarFragment.this.getString(R.string.nao_foi_possivel_carregar_os_comentarios),Toast.LENGTH_SHORT).show();
                    }
                };

                JsonObjectRequest jsonObjectRequest = avaliacao.buscaConteudoPostagem(codPostagem, codConteudoPostagem, listener, errorListener);
                jsonObjectRequest.setTag(TAG);
                requestQueuePostagens.add(jsonObjectRequest);
            }

        }else {
            atribuiValoresSemRecyclerView();
            buscarDoServidor = false;
            hideProgressBar();
        }
    }

    public void atribuiValoresSemRecyclerView() {
        if(inicializar) {
            CardView linearLayoutComentarios = (CardView) view.findViewById(R.id.linearlayout_comentarios);
            linearLayoutComentarios.setVisibility(View.GONE);
            LinearLayout linearLayoutMedia = (LinearLayout) view.findViewById(R.id.linearlayout_media);
            linearLayoutMedia.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
            LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
            l.setMargins(5,5,5,5);
            buttonAvaliarDialog.setLayoutParams(l);
            buttonAvaliarDialog.setBackgroundResource(R.color.colorPrimary);
            buttonAvaliarDialog.setTextColor(ContextCompat.getColor(this.getActivity(),android.R.color.white));
            buscaMedia();
        }
    }

    public void atribuiValoresRecyclerView() {
        ArrayList<Comentario> comentarios = montaListaComentarios();
        this.comentariosList.addAll(comentarios);

        if(inicializar) {
            comentarioAdapter = new ComentarioAdapter(AvaliarFragment.this.getActivity(), this.comentariosList);
            recyclerViewComentarios.setAdapter(comentarioAdapter);
            textoInformativoComentarios.setText(this.getActivity().getString(R.string.comentarios_de_outros_usuarios));
            buscaMedia();
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

    public void inicializaAvaliacoesMedia() {
        Float media = 0f;
        int contagem = 0;

        if(postagemMedia != null) {
            media = postagemMedia.getMedia();
            contagem = postagemMedia.getContagem();
        }

        ratingBarReadonly.setRating(media);
        if(contagem > 0) {
            linearLayoutMediaContainer.setVisibility(View.VISIBLE);
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
                /*Log.i("logError",new String(error.networkResponse.data));*/
            }
        };

        avaliacao.buscaMediaAvaliacoes(estabelecimento.getCodUnidade(),respListener, errorListener);
        RequestQueue requestQueue = avaliacao.getRequestQueue();
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                myAlertDialogFragment.dismiss();
            }
        });

    }


    public void setPostagemRetornos(PostagemRetorno[] postagemRetornos) {
        this.postagemRetornos = postagemRetornos;
    }

    public void loadDialogAvaliar() {
        Intent intent;
        intent = new Intent(AvaliarFragment.this.getActivity(), DialogAvaliarActivity.class);
        intent.putExtra("estabelecimento", estabelecimento);
        startActivityForResult(intent, 8);
        AvaliarFragment.this.getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    public void loadEscolherAcesso() {
        Intent intent;
        intent = new Intent(AvaliarFragment.this.getActivity(), EscolherAcessoActivity.class);
        startActivityForResult(intent, 9);
        AvaliarFragment.this.getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }
}

/*
v1_95E033A415E2CAF387122C7D231FA2031F9C05EC30E070F4E42A41A7E300970D47B6E43FC415C1EA0AA4AE8142443B304425A524781A497C9EC8F78045730441CD22D54B0F428B3ACE53259B4AEA482EF12F8C36941C4841D966E9C543E9696028932BDA002E419ACE42BE40A6C2CC81415691877D63C5870F6E25D84A349925190FA5333803FB8B943980053AED86222921D3FFA5538E01E27DA6314216AB0AD6F998220F8043776437162F3417B28D37692552BC1FAE073DB3BF4D96431FDBA50D14E6E0A783801FE87E3B71709776FE2BEE26A5ABAF016C7BE64374485C26C24131D2CA0EF0A04428ABE647473ADAA361FD6FDB5004E7A53BE310B497E80F218E2BB3B39EA7BB0996781247E243B4*/
