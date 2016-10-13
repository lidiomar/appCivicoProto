package com.example.fernando.appcivico.estrutura;

/**
 * Created by fernando on 12/10/16.
 */
public class PostagemRetorno {
    private String codAutor;
    private String codObjetoDestino;
    private String codPostagem;
    private String codTipoObjetoDestino;
    private String codTipoPostagem;
    private ConteudoPostagemRetorno[] conteudos;
    private String dataHoraPostagem;
    private String latitude;
    private String longitude;

    public String getCodAutor() {
        return codAutor;
    }

    public void setCodAutor(String codAutor) {
        this.codAutor = codAutor;
    }

    public String getCodObjetoDestino() {
        return codObjetoDestino;
    }

    public void setCodObjetoDestino(String codObjetoDestino) {
        this.codObjetoDestino = codObjetoDestino;
    }

    public String getCodPostagem() {
        return codPostagem;
    }

    public void setCodPostagem(String codPostagem) {
        this.codPostagem = codPostagem;
    }

    public String getCodTipoObjetoDestino() {
        return codTipoObjetoDestino;
    }

    public void setCodTipoObjetoDestino(String codTipoObjetoDestino) {
        this.codTipoObjetoDestino = codTipoObjetoDestino;
    }

    public String getCodTipoPostagem() {
        return codTipoPostagem;
    }

    public void setCodTipoPostagem(String codTipoPostagem) {
        this.codTipoPostagem = codTipoPostagem;
    }

    public ConteudoPostagemRetorno[] getConteudos() {
        return conteudos;
    }

    public void setConteudos(ConteudoPostagemRetorno[] conteudos) {
        this.conteudos = conteudos;
    }

    public String getDataHoraPostagem() {
        return dataHoraPostagem;
    }

    public void setDataHoraPostagem(String dataHoraPostagem) {
        this.dataHoraPostagem = dataHoraPostagem;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
