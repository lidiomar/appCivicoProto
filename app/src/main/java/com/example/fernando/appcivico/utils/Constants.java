package com.example.fernando.appcivico.utils;

import com.example.fernando.appcivico.estrutura.Categoria;
import com.example.fernando.appcivico.estrutura.Especialidade;

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
    public static final int LOGIN_AUTENTICADO = 11;
    public static final int CADASTRO_EFETUADO = 12;
    public static final int COMENTARIO_MODIFICADO = 13;

    public static final int TURN_LOCATION_ON = 42;

    public static final Categoria[] CATEGORIAS = new Categoria[]{
            /*new Categoria("", "Buscar em todas"),*/
            new Categoria("HOSPITAL", "Hospital"),
            new Categoria("POSTO DE SAÚDE", "Posto de Saúde"),
            new Categoria("URGÊNCIA", "Urgência"),
            new Categoria("SAMU", "Samu"),
            new Categoria("FARMÁCIA", "Fármacia"),
            new Categoria("CLÍNICA", "Clínica"),
            new Categoria("CONSULTÓRIO", "Consultório"),
            new Categoria("LABORATÓRIO", "Laboratório"),
            new Categoria("APOIO À SAÚDE", "Apoio à saúde"),
            new Categoria("ATENÇÃO ESPECÍFICA", "Atenção específica"),
            new Categoria("UNIDADE ADMINISTRATIVA", "Unidade administrativa"),
            new Categoria("ATENDIMENTO DOMICILIAR", "Atendimento domiciliar")
    };
    public static final Especialidade[] ESPECIALIDADES = new Especialidade[]{
            /*new Especialidade("", "Buscar em todas"),*/
            new Especialidade("DST/AIDS", "DST/AIDS"),
            new Especialidade("NEFROLOGIA", "Nefrologia"),
            new Especialidade("OSTEOGENESIS IMPERFECTA", "Osteogenesis imperfecta"),
            new Especialidade("TRANSEXUALIZADOR", "Transexualizador"),
            new Especialidade("NEUROLOGIA/NEUROCIRURGIA", "Neurologia/neurocirurgia"),
            new Especialidade("PNEUMOLOGIA", "Pneumologia"),
            new Especialidade("TRANSPLANTES", "Transplantes"),
            new Especialidade("ATENCAO A SAUDE AUDITIVA", "Atenção a saúde auditiva"),
            new Especialidade("UNIDADE TERAPIA INTENSIVA", "Unidade terapia intensiva"),
            new Especialidade("TRAUMATO-ORTOPEDIA", "Traumato-ortopedia"),
            new Especialidade("QUEIMADOS", "Queimados"),
            new Especialidade("ATENCAO A OBESIDADE GRAVE", "Atenção a obesidade grave"),
            new Especialidade("ONCOLOGIA", "Oncologia"),
            new Especialidade("VIDEOCIRURGIAS", "Videocirurgias"),
            new Especialidade("ATENCAO A SAUDE OCULAR", "Atenção a saúde ocular"),
            new Especialidade("ATENCAO A SAUDE MENTAL", "Atenção a saúde mental"),
            new Especialidade("HOSPITAL DIA", "Hospital dia"),
            new Especialidade("DOR CRONICA", "Dor crônica"),
            new Especialidade("PLANEJAMENTO FAMILIAR/ESTERELIZACAO", "Planejamento familiar/esterelização"),
            new Especialidade("CUIDADOS PROLONGADOS", "Cuidados prolongados"),
            new Especialidade("TERAPIA NUTRICIONAL", "Terapia nutricional"),
            new Especialidade("REABILITACAO", "Reabilitação"),
            new Especialidade("UNIDADE DE CUIDADOS INTERMEDIARIOS NEONATAL", "Unidade de cuidados intermediários neonatal"),
            new Especialidade("ATENCAO A SAUDE BUCAL", "Atenção a saúde bucal"),
            new Especialidade("INTERNACAO DOMICILIAR", "Internação domiciliar"),
            new Especialidade("URGENCIA", "Urgência"),
            new Especialidade("ATENCAO AO IDOSO", "Atenção ao idoso"),
            new Especialidade("CARDIOVASCULAR", "Cardiovascular"),
            new Especialidade("MATERNO INFANTIL", "Materno infantil"),
            new Especialidade("PROGRAMA DA MAMOGRAFIA MÓVEL", "Programa da mamografia móvel")
    };
}