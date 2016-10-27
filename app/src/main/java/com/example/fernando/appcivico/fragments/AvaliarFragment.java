package com.example.fernando.appcivico.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
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
            buscarComentarios();
        }else {
            view = inflater.inflate(R.layout.fragment_avaliar_disconnected, container, false);
            buscaMedia();
        }

        buttonAvaliarDialog = (Button)view.findViewById(R.id.button_avaliar_dialog);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        linearLayoutMediaContainer = (LinearLayout)view.findViewById(R.id.media_container);

        ratingBarReadonly = (RatingBar)view.findViewById(R.id.rating_avaliacao_readonly);
        Drawable progress = ratingBarReadonly.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.rgb(11111111,01011010,00000000));
        txtNumeroAvaliacoes = (TextView) view.findViewById(R.id.text_numero_avaliacoes);
        txtMediaAvaliacoes = (TextView) view.findViewById(R.id.text_media_avaliacoes);

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

        textoInformativoComentarios = (TextView)view.findViewById(R.id.texto_informativo_comentarios);

        buttonAvaliarDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AvaliarFragment.this.getActivity(), DialogAvaliarActivity.class);
                intent.putExtra("estabelecimento", estabelecimento);
                startActivity(intent);
            }
        });

        myAlertDialogFragment = MyAlertDialogFragment.newInstance("","");
        myAlertDialogFragment.show(this.getFragmentManager(),"");



        return view;
    }

    public void buscarComentarios() {
        requestCount = 0;

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
        if(postagemRetornos != null) {

            final RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity());
            requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
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
                        requestQueue.cancelAll(TAG);
                        Log.i("erro: ", AvaliarFragment.this.getString(R.string.nao_foi_possivel_carregar_os_comentarios));
                    }
                };

                JsonObjectRequest jsonObjectRequest = avaliacao.buscaConteudoPostagem(codPostagem, codConteudoPostagem, listener, errorListener);
                jsonObjectRequest.setTag(TAG);
                requestQueue.add(jsonObjectRequest);
            }

        }else {
            Toast.makeText(this.getActivity(),AvaliarFragment.this.getString(R.string.nao_foi_possivel_carregar_os_comentarios), Toast.LENGTH_SHORT).show();
        }
    }

    public void atribuiValoresSemRecyclerView() {
        LinearLayout linearLayoutComentarios = (LinearLayout) view.findViewById(R.id.linearlayout_comentarios);
        linearLayoutComentarios.setVisibility(View.GONE);
        LinearLayout linearLayoutMedia = (LinearLayout) view.findViewById(R.id.linearlayout_media);
        linearLayoutMedia.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0));
        buttonAvaliarDialog.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0));
        buscaMedia();
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
                Log.i("logError",new String(error.networkResponse.data));
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
}
