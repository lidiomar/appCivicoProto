package com.example.fernando.appcivico.estrutura;

/**
 * Created by fernando on 11/10/16.
 */
public class Categoria {
    String id;
    String descricao;

    public Categoria(String id, String descricao) {
        this.descricao = descricao;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
