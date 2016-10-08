package com.example.fernando.appcivico.utils;

import android.content.Context;

import com.example.fernando.appcivico.estrutura.Usuario;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by fernando on 08/10/16.
 */
public class CacheDadosDeUsuario {

    private final Context context;

    public CacheDadosDeUsuario(Context context) {
        this.context = context;
    }

    public void salvaEmCache(String conteudo, String fileName) throws IOException {
        FileOutputStream is = context.openFileOutput(fileName, context.MODE_PRIVATE);
        is.write(conteudo.getBytes());
        is.close();
    }


    public Usuario getUsuarioAutenticadoCache() throws IOException {
        String file = Constants.FILE_USUARIO_AUTENTICADO;
        String temp = "";
        Usuario usuario = null;
        Gson gson = new Gson();
        if(context.getFileStreamPath(file).exists()) {
            FileInputStream fin = context.openFileInput(file);
            int c;

            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            fin.close();

            usuario = gson.fromJson(temp, Usuario.class);
        }

        return usuario;
    }

    public String getAppTokenCache() throws IOException {
        String file = Constants.FILE_APP_TOKEN;
        String temp = "";
        if(context.getFileStreamPath(file).exists()) {
            FileInputStream fin = context.openFileInput(file);
            int c;

            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            fin.close();
        }
        return temp;
    }

    public Boolean deleteFiles() {
        Boolean fileAppTokenDeleted = context.deleteFile(Constants.FILE_APP_TOKEN);
        Boolean fileUsuarioAutenticadoDeleted = context.deleteFile(Constants.FILE_USUARIO_AUTENTICADO);

        return (fileAppTokenDeleted && fileUsuarioAutenticadoDeleted);
    }


}
