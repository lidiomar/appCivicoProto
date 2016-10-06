package com.example.fernando.appcivico.estrutura;

import java.util.ArrayList;

/**
 * Created by fernando on 04/10/16.
 */
public class Sexo {
    private String id;
    private String descricao;

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

    public ArrayList<Sexo> getArraylistSexo() {
        ArrayList<Sexo> arraylistSexo = new ArrayList<>();
        Sexo sexoDefault = new Sexo();

        sexoDefault.setDescricao("Informe o sexo");
        sexoDefault.setId("");

        Sexo sexoMasculino = new Sexo();

        sexoMasculino.setId("M");
        sexoMasculino.setDescricao("Masculino");

        Sexo sexoFeminino = new Sexo();
        sexoFeminino.setId("F");
        sexoFeminino.setDescricao("Feminino");

        arraylistSexo.add(sexoDefault);
        arraylistSexo.add(sexoMasculino);
        arraylistSexo.add(sexoFeminino);
        return arraylistSexo;
    }
}
