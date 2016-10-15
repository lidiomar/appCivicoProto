package com.example.fernando.appcivico.estrutura;

/**
 * Created by fernando on 14/10/16.
 */
public class Estado {
    private String sigla;
    private String nome;
    private String[] cidades;

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String[] getCidades() {
        return cidades;
    }

    public void setCidades(String[] cidades) {
        this.cidades = cidades;
    }
}
