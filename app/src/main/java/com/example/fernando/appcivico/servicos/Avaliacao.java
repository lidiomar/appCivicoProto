package com.example.fernando.appcivico.servicos;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.ConteudoPostagem;
import com.example.fernando.appcivico.estrutura.Postagem;
import com.example.fernando.appcivico.utils.Constants;
import com.example.fernando.appcivico.utils.StaticFunctions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fernando on 06/10/16.
 */
public class Avaliacao {
    private final Context context;
    private final FragmentActivity fragmentActivity;
    private final RequestQueue requestQueue;
    private String urlResponse;
    private int buscaPostagemStatusCode;

    public Avaliacao(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        this.context = fragmentActivity;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void criarPostagem(Postagem postagem, final ConteudoPostagem conteudoPostagem, Response.Listener<String> responseListener) {

        String url = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/postagens";
        Gson gson = new Gson();
        final String mRequestBody = gson.toJson(postagem);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url , responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(fragmentActivity,fragmentActivity.getString(R.string.algo_deu_errado),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("appIdentifier", Constants.CODE_APP);
                params.put("appToken",((ApplicationAppCivico)fragmentActivity.getApplication()).getApptoken());
                return params;
            }

            @Override
            public byte[] getBody() {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                final String location = response.headers.get("location");
                Avaliacao.this.setUrlResponse(location);
                return super.parseNetworkResponse(response);
            }
        };


        this.requestQueue.add(stringRequest);
    }

    public void atribuirConteudoPostagem(ConteudoPostagem conteudoPostagem) {

        String url = getUrlResponse()+"/conteudos";
        Gson gson = new Gson();
        final String mRequestBody = gson.toJson(conteudoPostagem);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url ,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                StaticFunctions.exibeMensagemEFecha(fragmentActivity.getString(R.string.avaliacao_enviada_com_sucesso), fragmentActivity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                removePostagem();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("appToken",((ApplicationAppCivico)fragmentActivity.getApplication()).getApptoken());
                return params;
            }

            @Override
            public byte[] getBody() {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };


        this.requestQueue.add(stringRequest);
    }

    public void removePostagem() {
        String url = getUrlResponse();
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(fragmentActivity,fragmentActivity.getString(R.string.algo_deu_errado),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(fragmentActivity,fragmentActivity.getString(R.string.algo_deu_errado),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("appToken",((ApplicationAppCivico)fragmentActivity.getApplication()).getApptoken());
                return params;
            }

        };


        this.requestQueue.add(stringRequest);
    }

    public void buscaPostagem(int pagina, int quantidadeItens, String codObjetoDestino, int codAutor, Response.Listener<String> responseListener, Response.ErrorListener responseErrorListener) {

        String url = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/postagens" +
                "?codAplicativo="+Constants.CODE_APP+"" +
                "&codTiposPostagem="+Constants.CODE_TIPO_POSTAGEM+"" +
                "&codTipoObjetoDestino="+Constants.CODE_TIPO_OBJETO_DESTINO+"" +
                "&quantidadeDeItens="+quantidadeItens+
                "&pagina="+pagina+
                "&codAutor"+codAutor+
                "&codObjetoDestino="+codObjetoDestino;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener , responseErrorListener){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("appToken",((ApplicationAppCivico)fragmentActivity.getApplication()).getApptoken());
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int statusCode = response.statusCode;
                Avaliacao.this.setBuscaPostagemStatusCode(statusCode);
                return super.parseNetworkResponse(response);
            }
        };

        this.requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {

            }
        });
    }

    public void buscaPostagens(int pagina, int quantidadeItens, String codObjetoDestino, Response.Listener<String> responseListener, Response.ErrorListener responseErrorListener) {

        String url = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/postagens" +
                "?codAplicativo="+Constants.CODE_APP+"" +
                "&codTiposPostagem="+Constants.CODE_TIPO_POSTAGEM+"" +
                "&codTipoObjetoDestino="+Constants.CODE_TIPO_OBJETO_DESTINO+"" +
                "&quantidadeDeItens="+quantidadeItens+
                "&pagina="+pagina+
                "&codObjetoDestino="+codObjetoDestino;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener , responseErrorListener){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("appToken",((ApplicationAppCivico)fragmentActivity.getApplication()).getApptoken());
                return params;
            }
        };

        this.requestQueue.add(stringRequest);
    }

    public void buscaMediaAvaliacoes(String codObjetoDestino, Response.Listener responseListener, Response.ErrorListener responseErrorListener) {

        String url = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/postagens" +
                "/tipopostagem/"+Constants.CODE_TIPO_POSTAGEM+
                "/tipoobjeto/"+Constants.CODE_TIPO_OBJETO_DESTINO+
                "/objeto/"+codObjetoDestino;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, responseListener ,responseErrorListener);

        this.requestQueue.add(jsonObjectRequest);

    }

    public JsonObjectRequest buscaConteudoPostagem(final String codPostagem, String codConteudo, Response.Listener respListener, Response.ErrorListener errorListener){
        String url = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/postagens/"+codPostagem+"/conteudos/"+codConteudo;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,respListener , errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("appToken",((ApplicationAppCivico)fragmentActivity.getApplication()).getApptoken());
                return params;
            }
        };
        return jsonObjectRequest;
    }

    public String getUrlResponse() {
        return urlResponse;
    }

    public JsonObjectRequest getRequisicaoPessoaPorCodigo(String codPessoa, Response.Listener responseListener, Response.ErrorListener errorListener) {
        String url = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/pessoas/"+codPessoa;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,responseListener, errorListener);
        return jsonObjectRequest;
    }

    public void setUrlResponse(String urlResponse) {
        this.urlResponse = urlResponse;
    }

    public int getBuscaPostagemStatusCode() {
        return buscaPostagemStatusCode;
    }

    public void setBuscaPostagemStatusCode(int buscaPostagemStatusCode) {
        this.buscaPostagemStatusCode = buscaPostagemStatusCode;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
