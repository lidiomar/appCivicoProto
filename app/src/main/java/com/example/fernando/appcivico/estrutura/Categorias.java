package com.example.fernando.appcivico.estrutura;

/**
 * Created by fernando on 11/10/16.
 */
public class Categorias {
    String id;
    String descricao;

    public Categorias(String id, String descricao) {
        this.descricao = descricao;
        this.id = id;
    }
    @Override
    public String toString() {
        return descricao;
    }
}
