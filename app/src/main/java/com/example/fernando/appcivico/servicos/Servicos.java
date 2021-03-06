package com.example.fernando.appcivico.servicos;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.Usuario;
import com.example.fernando.appcivico.utils.MyAlertDialogFragment;
import com.example.fernando.appcivico.utils.StaticFunctions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fernando on 06/10/16.
 */
public class Servicos {
    private final Context context;
    private final FragmentActivity fragmentActivity;
    private final RequestQueue requestQueue;

    public Servicos(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        this.context = fragmentActivity;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void autenticarUsuario(final String email, final String senha, Response.Listener<JSONObject> listener, final MyAlertDialogFragment myAlertDialogFragment) {
        String url = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/pessoas/autenticar";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(fragmentActivity,fragmentActivity.getString(R.string.usuario_nao_cadastrado),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",(email));
                params.put("senha",(senha));
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                ((ApplicationAppCivico)fragmentActivity.getApplication()).setApptoken(headers.get("apptoken"));
                Log.i("token",headers.get("apptoken"));
                return super.parseNetworkResponse(response);
            }
        };


        this.requestQueue.add(jsonObjectRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                if(myAlertDialogFragment != null) {
                    myAlertDialogFragment.dismiss();
                }
            }
        });
    }

    public JsonArrayRequest consultaEstabelecimentoLatLong(double latitude, double longitude,
                                                           int raio, final String texto, final String categoria, Response.Listener responseListener, Response.ErrorListener errorListener) {

        String url = "http://mobile-aceite.tcu.gov.br:80/mapa-da-saude/rest/estabelecimentos/latitude/"+latitude+"/longitude/"+longitude+"/raio/"+raio;
        String queryParams = "";

        boolean parametroAdicionado = false;
        if(!texto.isEmpty()) {
            queryParams = "?texto="+texto;
            parametroAdicionado = true;
        }

        if(!categoria.isEmpty()) {
            if(parametroAdicionado) {
                queryParams += "&categoria="+categoria;
            }else {
                queryParams += "?categoria="+categoria;
                parametroAdicionado = true;
            }
        }

        if(parametroAdicionado) {
            queryParams += "&quantidade="+200;
        }else {
            queryParams += "?quantidade="+200;
        }

        url += queryParams;

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, responseListener , errorListener);


        return jsonArrayRequest;

    }

    public JsonArrayRequest consultaEstabelecimentos(String municipio, String uf, String categoria, String especialidade,
                                         int quantidade, int pagina, Response.Listener resListener,
                                         Response.ErrorListener errorListener) {
        String url = "http://mobile-aceite.tcu.gov.br:80/mapa-da-saude/rest/estabelecimentos" +
                "?municipio=" + municipio +
                "&uf=" + uf +
                "&quantidade=" + quantidade +
                "&pagina="+ pagina;

        if(!categoria.isEmpty()) {
            url = url + "&categoria=" + categoria;
        }
        if(!especialidade.isEmpty()) {
            url = url + "&especialidade=" + especialidade;
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url,null,resListener, errorListener);

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return jsonArrayRequest;

    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
