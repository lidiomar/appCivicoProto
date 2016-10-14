package com.example.fernando.appcivico.estrutura;

/**
 * Created by fernando on 13/10/16.
 */
public class Comentario {
    private String nomeUsuario;
    private String texto;
    private int valor;
    private String codigoPostagem;

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getCodigoPostagem() {
        return codigoPostagem;
    }

    public void setCodigoPostagem(String codigoPostagem) {
        this.codigoPostagem = codigoPostagem;
    }
}
