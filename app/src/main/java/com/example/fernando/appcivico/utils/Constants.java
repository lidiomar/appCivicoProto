package com.example.fernando.appcivico.utils;

import com.example.fernando.appcivico.estrutura.Categorias;

/**
 * Created by fernando on 01/10/16.
 */
public class Constants {
    public static final String CODE_PERFIL = "115";
    public static final String CODE_APP = "282";
    public static final String FILE_USUARIO_AUTENTICADO = "file_usuario_autenticado";
    public static final String FILE_APP_TOKEN = "file_app_token";
    public static final Categorias[] CATEGORIAS = new Categorias[]{
        new Categorias("HOSPITAL","Hospital"),
        new Categorias("POSTO DE SAÚDE","Posto de Saúde"),
        new Categorias("URGÊNCIA","Urgência"),
        new Categorias("SAMU","Samu"),
        new Categorias("FARMÁCIA","Fármacia"),
        new Categorias("CLÍNICA","Clínica"),
        new Categorias("CONSULTÓRIO","Consultório"),
        new Categorias("LABORATÓRIO","Laboratório"),
        new Categorias("APOIO À SAÚDE","Apoio à saúde"),
        new Categorias("ATENÇÃO ESPECÍFICA","Atenção específica"),
        new Categorias("UNIDADE ADMINISTRATIVA","Unidade administrativa"),
        new Categorias("ATENDIMENTO DOMICILIAR","Atendimento domiciliar")
    };
}
