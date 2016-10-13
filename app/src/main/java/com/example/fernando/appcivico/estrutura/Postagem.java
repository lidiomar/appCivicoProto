package com.example.fernando.appcivico.estrutura;

/**
 * Created by fernando on 06/10/16.
 */
public class Postagem {
    private Autor autor;
    private String codGrupoDestino;
    private String codPessoaDestino;
    private String codObjetoDestino;
    private String codTipoObjetoDestino;
    private Integer latitude;
    private Integer longitude;
    private PostagemRelacionada postagemRelacionada;
    private Tipo tipo;

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public PostagemRelacionada getPostagemRelacionada() {
        return postagemRelacionada;
    }

    public void setPostagemRelacionada(PostagemRelacionada postagemRelacionada) {
        this.postagemRelacionada = postagemRelacionada;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getCodTipoObjetoDestino() {
        return codTipoObjetoDestino;
    }

    public void setCodTipoObjetoDestino(String codTipoObjetoDestino) {
        this.codTipoObjetoDestino = codTipoObjetoDestino;
    }

    public String getCodGrupoDestino() {
        return codGrupoDestino;
    }

    public void setCodGrupoDestino(String codGrupoDestino) {
        this.codGrupoDestino = codGrupoDestino;
    }

    public String getCodPessoaDestino() {
        return codPessoaDestino;
    }

    public void setCodPessoaDestino(String codPessoaDestino) {
        this.codPessoaDestino = codPessoaDestino;
    }

    public String getCodObjetoDestino() {
        return codObjetoDestino;
    }

    public void setCodObjetoDestino(String codObjetoDestino) {
        this.codObjetoDestino = codObjetoDestino;
    }
}
