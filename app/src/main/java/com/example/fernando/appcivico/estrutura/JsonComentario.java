package com.example.fernando.appcivico.estrutura;

/**
 * Created by fernando on 13/10/16.
 */
public class JsonComentario {
    private String dataComentario;
    private String nomeAutorComentario;
    private String nomeFantasiaEstabelecimento;

    public String getDataComentario() {
        return dataComentario;
    }

    public void setDataComentario(String dataComentario) {
        this.dataComentario = dataComentario;
    }

    public String getNomeAutorComentario() {
        return nomeAutorComentario;
    }

    public void setNomeAutorComentario(String nomeAutorComentario) {
        this.nomeAutorComentario = nomeAutorComentario;
    }

    public String getNomeFantasiaEstabelecimento() {
        return nomeFantasiaEstabelecimento;
    }

    public void setNomeFantasiaEstabelecimento(String nomeFantasiaEstabelecimento) {
        this.nomeFantasiaEstabelecimento = nomeFantasiaEstabelecimento;
    }
}
