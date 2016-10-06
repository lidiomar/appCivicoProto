package com.example.fernando.appcivico.servicos;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.example.fernando.appcivico.utils.StaticFunctions;
import com.google.gson.Gson;

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

    public void autenticarUsuario(final String email, final String senha) {
        String url = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/pessoas/autenticar";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                Usuario usuarioAutenticado = gson.fromJson(response.toString(), Usuario.class);
                ((ApplicationAppCivico)fragmentActivity.getApplication()).setUsuarioAutenticado(usuarioAutenticado);

                StaticFunctions.exibeMensagemEFecha(fragmentActivity.getString(R.string.usuario_autenticado_com_sucesso),fragmentActivity);
            }
        }, new Response.ErrorListener() {
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
                return super.parseNetworkResponse(response);
            }
        };


        this.requestQueue.add(jsonObjectRequest);
    }

    public void consultaEstabelecimento(Response.Listener r) {
        String url = "http://mobile-aceite.tcu.gov.br:80/mapa-da-saude/rest/estabelecimentos?quantidade=30";
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, r , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,fragmentActivity.getString(R.string.algo_deu_errado),Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonArrayRequest);

    }
}
