package com.example.fernando.appcivico.estrutura;

/**
 * Created by fernando on 06/10/16.
 */
public class Postagem {
    private Autor autor;
    private Integer codGrupoDestino;
    private Integer codPessoaDestino;
    private Integer codObjetoDestino;
    private Integer codTipoObjetoDestino;
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

    public Integer getCodGrupoDestino() {
        return codGrupoDestino;
    }

    public void setCodGrupoDestino(Integer codGrupoDestino) {
        this.codGrupoDestino = codGrupoDestino;
    }

    public Integer getCodPessoaDestino() {
        return codPessoaDestino;
    }

    public void setCodPessoaDestino(Integer codPessoaDestino) {
        this.codPessoaDestino = codPessoaDestino;
    }

    public Integer getCodObjetoDestino() {
        return codObjetoDestino;
    }

    public void setCodObjetoDestino(Integer codObjetoDestino) {
        this.codObjetoDestino = codObjetoDestino;
    }

    public Integer getCodTipoObjetoDestino() {
        return codTipoObjetoDestino;
    }

    public void setCodTipoObjetoDestino(Integer codTipoObjetoDestino) {
        this.codTipoObjetoDestino = codTipoObjetoDestino;
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
}
