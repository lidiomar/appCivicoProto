package com.example.fernando.appcivico.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.Autor;
import com.example.fernando.appcivico.estrutura.ConteudoPostagem;
import com.example.fernando.appcivico.estrutura.Estabelecimento;
import com.example.fernando.appcivico.estrutura.JsonComentario;
import com.example.fernando.appcivico.estrutura.Postagem;
import com.example.fernando.appcivico.estrutura.Tipo;
import com.example.fernando.appcivico.servicos.Avaliacao;
import com.example.fernando.appcivico.utils.Constants;
import com.example.fernando.appcivico.utils.MyAlertDialogFragment;
import com.example.fernando.appcivico.utils.StaticFunctions;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by fernando on 16/10/16.
 */
public class DialogAvaliarFragment extends Fragment {

    private Estabelecimento estabelecimento;
    private RatingBar ratingBar;
    private EditText editTextComentario;
    private Button buttonAvaliar;
    private Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_avaliar_dialog, container, false);
       
        Bundle extras = getActivity().getIntent().getExtras();
        buttonAvaliar = (Button)view.findViewById(R.id.button_avaliar);

        ratingBar = (RatingBar)view.findViewById(R.id.rating_avaliacao);
        Drawable progress = ratingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.rgb(11111111,01011010,00000000));


        editTextComentario = (EditText)view.findViewById(R.id.comentario_avaliacao);

        estabelecimento = (Estabelecimento)extras.get("estabelecimento");
        if(estabelecimento != null) {
            buttonAvaliar.setOnClickListener(clickAvaliar());
        }else {
            String codigoPostagem = (String)extras.get("codigoPostagem");
            String codConteudoPost = (String)extras.get("codConteudoPost");
            String nomeFantasiaEstabelecimento = (String)extras.get("nomeFantasiaEstabelecimento");
            String nomeAutorComentario = (String)extras.get("nomeAutorComentario");

            String comentario = (String)extras.get("comentario");
            int valor = (int)extras.get("valor");

            editTextComentario.setText(comentario);
            ratingBar.setRating(valor);

            buttonAvaliar.setOnClickListener(clickAtualizarAvaliacao(codigoPostagem,codConteudoPost, nomeFantasiaEstabelecimento, nomeAutorComentario));
        }



        return view;
    }

    private View.OnClickListener clickAtualizarAvaliacao(final String codigoPostagem, final String  codConteudoPost, final String nomeFantasiaEstabelecimento, final String nomeAutorComentario) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarEnvio(editTextComentario)) {
                    int notaAvaliacao = (int) ratingBar.getRating();
                    String comentario = editTextComentario.getText().toString();

                    ConteudoPostagem conteudoPostagem = new ConteudoPostagem();
                    conteudoPostagem.setValor(notaAvaliacao);
                    conteudoPostagem.setTexto(comentario);

                    JsonComentario jsonComentario = new JsonComentario();
                    jsonComentario.setNomeFantasiaEstabelecimento(nomeFantasiaEstabelecimento);
                    jsonComentario.setNomeAutorComentario(nomeAutorComentario);

                    Calendar c = Calendar.getInstance();
                    Date data = c.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy");
                    String dataFormatada = sdf.format(data);
                    jsonComentario.setDataComentario(dataFormatada);

                    conteudoPostagem.setJSON(gson.toJson(jsonComentario));

                    final Avaliacao avaliacao = new Avaliacao(DialogAvaliarFragment.this.getActivity());

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(DialogAvaliarFragment.this.getActivity(),DialogAvaliarFragment.this.getActivity().getString(R.string.algo_deu_errado),
                                    Toast.LENGTH_SHORT).show();
                        }
                    };


                    StringRequest stringRequest = avaliacao.atualizaComentario(codigoPostagem, codConteudoPost,conteudoPostagem, responseListener, errorListener);
                    RequestQueue requestQueue = avaliacao.getRequestQueue();

                    final MyAlertDialogFragment myAlertDialogFragment = MyAlertDialogFragment.newInstance("", "Enviando avaliação...");
                    myAlertDialogFragment.show(getFragmentManager(), "");

                    requestQueue.add(stringRequest);
                    requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                        @Override
                        public void onRequestFinished(Request<Object> request) {
                            myAlertDialogFragment.dismiss();
                            DialogAvaliarFragment.this.getActivity().setResult(Constants.COMENTARIO_MODIFICADO);
                            StaticFunctions.exibeMensagemEFecha(DialogAvaliarFragment.this.getActivity().getString(R.string.avaliacao_enviada_com_sucesso),DialogAvaliarFragment.this.getActivity());
                        }
                    });

                }else {
                    Toast.makeText(DialogAvaliarFragment.this.getActivity(), DialogAvaliarFragment.this.getActivity().getString(R.string.preencha_os_dados_da_avaliacao), Toast.LENGTH_SHORT).show();
                }
            }
        };
        return onClickListener;
    }

    public Boolean validarEnvio(EditText comentario) {
        if(comentario.getText().toString().isEmpty()) {
            comentario.setError(this.getActivity().getResources().getString(R.string.campo_obrigatorio));
            return false;
        }
        return true;
    }


    public View.OnClickListener clickAvaliar() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int notaAvaliacao = (int) ratingBar.getRating();
                String comentario = editTextComentario.getText().toString();

                if (validarEnvio(editTextComentario)) {
                    final Avaliacao avaliacao = new Avaliacao(DialogAvaliarFragment.this.getActivity());
                    Tipo tipo = new Tipo();
                    tipo.setCodTipoPostagem(Constants.CODE_TIPO_POSTAGEM);
                    Autor autor = new Autor();
                    autor.setCodPessoa(((ApplicationAppCivico) DialogAvaliarFragment.this.getActivity().getApplication()).getUsuarioAutenticado().getCod());

                    Postagem postagem = new Postagem();
                    postagem.setCodTipoObjetoDestino(Constants.CODE_TIPO_OBJETO_DESTINO);
                    postagem.setCodObjetoDestino(estabelecimento.getCodUnidade());
                    postagem.setTipo(tipo);
                    postagem.setAutor(autor);

                    final ConteudoPostagem conteudoPostagem = new ConteudoPostagem();

                    JsonComentario jsonComentario = new JsonComentario();
                    Calendar c = Calendar.getInstance();
                    Date data = c.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyy");
                    String dataFormatada = sdf.format(data);
                    jsonComentario.setDataComentario(dataFormatada);
                    jsonComentario.setNomeAutorComentario(((ApplicationAppCivico) DialogAvaliarFragment.this.getActivity().getApplication()).getUsuarioAutenticado().getNomeUsuario());
                    jsonComentario.setNomeFantasiaEstabelecimento(estabelecimento.getNomeFantasia());

                    conteudoPostagem.setJSON(gson.toJson(jsonComentario));
                    conteudoPostagem.setTexto(comentario);
                    conteudoPostagem.setValor(notaAvaliacao);

                    Response.Listener<String> responseListenerCriarPostagem = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            avaliacao.atribuirConteudoPostagem(conteudoPostagem);
                        }
                    };
                    final MyAlertDialogFragment myAlertDialogFragment = MyAlertDialogFragment.newInstance("", "Enviando avaliação...");
                    myAlertDialogFragment.show(getFragmentManager(), "");
                    avaliacao.criarPostagem(postagem, conteudoPostagem, responseListenerCriarPostagem);

                    avaliacao.getRequestQueue().addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                        @Override
                        public void onRequestFinished(Request<Object> request) {
                            myAlertDialogFragment.dismiss();
                        }
                    });

                } else {
                    Toast.makeText(DialogAvaliarFragment.this.getActivity(), DialogAvaliarFragment.this.getActivity().getString(R.string.preencha_os_dados_da_avaliacao), Toast.LENGTH_SHORT).show();
                }
            }
        };

        return onClickListener;
    }
}
