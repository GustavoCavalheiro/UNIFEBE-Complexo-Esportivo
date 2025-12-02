package com.complexo.UNIFEBE_Complexo_Esportivo.dao;

public class Ambientes {

    // Atributos
    private int id_AMBIENTES;
    private String nome_ambiente;
    private String descricao;


    // Construtor
    public Ambientes() {
    }

    // Get and set
    public int getId_AMBIENTES() {
        return id_AMBIENTES;
    }

    public void setId_AMBIENTES(int id_AMBIENTES) {
        this.id_AMBIENTES = id_AMBIENTES;
    }

    public String getNome_ambiente() {
        return nome_ambiente;
    }

    public void setNome_ambiente(String nome_ambiente) {
        this.nome_ambiente = nome_ambiente;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
