package com.example.fernando.appcivico.utils;

import com.example.fernando.appcivico.estrutura.Categoria;

/**
 * Created by fernando on 01/10/16.
 */
public class Constants {
    public static final String CODE_PERFIL = "115";
    public static final String CODE_APP = "282";
    /*public static final String CODE_TIPO_POSTAGEM = "173";*/
    public static final String CODE_TIPO_POSTAGEM = "205";
    public static final String CODE_TIPO_OBJETO_DESTINO = "100";
    public static final String FILE_USUARIO_AUTENTICADO = "file_usuario_autenticado";
    public static final String FILE_APP_TOKEN = "file_app_token";
    public static final Categoria[] CATEGORIAS = new Categoria[]{
        new Categoria("","Selecione uma categoria:"),
        new Categoria("HOSPITAL","Hospital"),
        new Categoria("POSTO DE SAÚDE","Posto de Saúde"),
        new Categoria("URGÊNCIA","Urgência"),
        new Categoria("SAMU","Samu"),
        new Categoria("FARMÁCIA","Fármacia"),
        new Categoria("CLÍNICA","Clínica"),
        new Categoria("CONSULTÓRIO","Consultório"),
        new Categoria("LABORATÓRIO","Laboratório"),
        new Categoria("APOIO À SAÚDE","Apoio à saúde"),
        new Categoria("ATENÇÃO ESPECÍFICA","Atenção específica"),
        new Categoria("UNIDADE ADMINISTRATIVA","Unidade administrativa"),
        new Categoria("ATENDIMENTO DOMICILIAR","Atendimento domiciliar")
    };
}
