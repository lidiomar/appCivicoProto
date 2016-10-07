package com.example.fernando.appcivico.estrutura;

/**
 * Created by fernando on 03/10/16.
 */
public class Usuario {

    private String CEP;
    private String biografia;
    private int cod;
    private String dataNascimento;
    private String email;
    private String emailVerificado;
    private Double latitude;
    private Links[] links;
    private Double longitude;
    private String nomeCompleto;

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    private String nomeUsuario;
    private String senha;
    private String sexo;
    private String tokenFacebook;
    private String tokenGoogle;
    private String tokenInstagram;
    private String tokenTwitter;


    public String getCep() {
        return CEP;
    }

    public void setCep(String cep) {
        this.CEP = cep;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTokenFacebook() {
        return tokenFacebook;
    }

    public void setTokenFacebook(String tokenFacebook) {
        this.tokenFacebook = tokenFacebook;
    }

    public String getTokenGoogle() {
        return tokenGoogle;
    }

    public void setTokenGoogle(String tokenGoogle) {
        this.tokenGoogle = tokenGoogle;
    }

    public String getTokenInstagram() {
        return tokenInstagram;
    }

    public void setTokenInstagram(String tokenInstagram) {
        this.tokenInstagram = tokenInstagram;
    }

    public String getTokenTwitter() {
        return tokenTwitter;
    }

    public void setTokenTwitter(String tokenTwitter) {
        this.tokenTwitter = tokenTwitter;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }


    public String getEmailVerificado() {
        return emailVerificado;
    }

    public void setEmailVerificado(String emailVerificado) {
        this.emailVerificado = emailVerificado;
    }

    public Links[] getLinks() {
        return links;
    }

    public void setLinks(Links[] links) {
        this.links = links;
    }
}
