package com.example.fernando.appcivico.servicos;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fernando.appcivico.R;
import com.example.fernando.appcivico.application.ApplicationAppCivico;
import com.example.fernando.appcivico.estrutura.Usuario;
import com.example.fernando.appcivico.utils.Constants;
import com.example.fernando.appcivico.utils.StaticFunctions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**

 * Created by fernando on 01/10/16.
 */
public class ServicosCadastro {
    private final Context context;
    private final FragmentActivity fragmentActivity;
    private final RequestQueue requestQueue;
    private int responseStatusCode;
    private Usuario usuario;

    public ServicosCadastro(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        this.context = fragmentActivity;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void cadastraPessoa(Usuario usuario) {
        try {
            setUsuario(usuario);
            String URL = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/pessoas";

            Gson gson = new GsonBuilder().create();
            final String mRequestBody = gson.toJson(usuario);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    int statusCode = Integer.parseInt(response);
                    if (statusCode == 201) {
                        autenticarUsuario();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String errorMessage = new String(error.networkResponse.data, "UTF-8");
                        Toast.makeText(fragmentActivity, errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(fragmentActivity, fragmentActivity.getString(R.string.algo_deu_errado), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cadastrarPerfil() {

        try {
            int codUsuario = ((ApplicationAppCivico) fragmentActivity.getApplication()).getUsuarioAutenticado().getCod();
            String url = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/pessoas/" + codUsuario + "/perfil";

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("camposAdicionais", "");

            JSONObject jsonPerfil = new JSONObject();
            jsonBody.put("tipoPerfil", jsonPerfil.put("codTipoPerfil", Constants.CODE_PERFIL));
            final String mRequestBody = jsonBody.toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    StaticFunctions.exibeMensagemEFecha(fragmentActivity.getString(R.string.cadastro_concluido_com_sucesso), fragmentActivity);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(fragmentActivity, fragmentActivity.getString(R.string.algo_deu_errado), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    return super.parseNetworkResponse(response);
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("appToken", ((ApplicationAppCivico) fragmentActivity.getApplication()).getApptoken());

                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };

            this.requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPerfil() {
        int codUsuario = ((ApplicationAppCivico) fragmentActivity.getApplication()).getUsuarioAutenticado().getCod();
        String url = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/pessoas/" + codUsuario + "/perfil";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {/*Provavelmente nunca entrará aqui*/}
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode == 404) {
                    cadastrarPerfil();
                } else {
                    Toast.makeText(fragmentActivity, fragmentActivity.getString(R.string.algo_deu_errado), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("appIdentifier", Constants.CODE_APP);
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                return super.parseNetworkResponse(response);
            }
        };


        this.requestQueue.add(jsonObjectRequest);
    }

    public void autenticarUsuario() {
        String url = "http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/pessoas/autenticar";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                ((ApplicationAppCivico) fragmentActivity.getApplication()).setUsuarioAutenticado(response.toString());
                getPerfil();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(fragmentActivity, fragmentActivity.getString(R.string.algo_deu_errado), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", (getUsuario().getEmail()));
                params.put("senha", (getUsuario().getSenha()));
                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                setResponseStatusCode(response.statusCode);
                Map<String, String> headers = response.headers;

                ((ApplicationAppCivico) fragmentActivity.getApplication()).setApptoken(headers.get("apptoken"));
                return super.parseNetworkResponse(response);
            }
        };


        this.requestQueue.add(jsonObjectRequest);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getResponseStatusCode() {
        return responseStatusCode;
    }

    public void setResponseStatusCode(int responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }
}