package com.example.fernando.appcivico.application;

import android.app.Application;

import com.example.fernando.appcivico.estrutura.Usuario;

/**
 * Created by fernando on 06/10/16.
 */
public class ApplicationAppCivico extends Application {
    private Usuario usuarioAutenticado;
    private String apptoken;

    public String getApptoken() {
        return apptoken;
    }

    public void setApptoken(String apptoken) {
        this.apptoken = apptoken;
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public void setUsuarioAutenticado(Usuario usuarioAutenticado) {
        this.usuarioAutenticado = usuarioAutenticado;
    }

    public Boolean isAuthenticated() {
        return (apptoken.isEmpty() ? false : true);
    }
}
