package com.example.fernando.appcivico.application;

import android.app.Application;

import com.example.fernando.appcivico.estrutura.Usuario;
import com.example.fernando.appcivico.utils.CacheDadosDeUsuario;
import com.example.fernando.appcivico.utils.Constants;
import com.google.gson.Gson;

import java.io.IOException;

/**
 * Created by fernando on 06/10/16.
 */
public class ApplicationAppCivico extends Application {
    private Usuario usuarioAutenticado;
    private String apptoken;
    private CacheDadosDeUsuario cacheDadosDeUsuario = new CacheDadosDeUsuario(this);

    public String getApptoken() {
        if(this.apptoken == null) {
            try {
                this.apptoken = cacheDadosDeUsuario.getAppTokenCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return apptoken;
    }

    public void setApptoken(String apptoken) {
        try {
            cacheDadosDeUsuario.salvaEmCache(apptoken, Constants.FILE_APP_TOKEN);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.apptoken = apptoken;

    }

    public Usuario getUsuarioAutenticado() {
        if(this.usuarioAutenticado == null) {
            try {
                usuarioAutenticado = cacheDadosDeUsuario.getUsuarioAutenticadoCache();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return usuarioAutenticado;
    }

    public void setUsuarioAutenticado(String usuarioAutenticadoJson) {
        Usuario usuarioAutenticado = null;
        Gson gson = new Gson();
        try {
            cacheDadosDeUsuario.salvaEmCache(usuarioAutenticadoJson, Constants.FILE_USUARIO_AUTENTICADO);
            usuarioAutenticado = gson.fromJson(usuarioAutenticadoJson, Usuario.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.usuarioAutenticado = usuarioAutenticado;
    }

    public Boolean usuarioAutenticado() {
        String apptoken = getApptoken();
        return ((apptoken == null || apptoken.isEmpty()) ? false : true);
    }
}
