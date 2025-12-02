package com.complexo.UNIFEBE_Complexo_Esportivo.dao;

public class Usuario {
    private String nome;
    private int matricula;
    private int id_USUARIO;
    private String senha;
    private char tipo; // 'A' para Admin, 'C' para Comum

    public Usuario() {
    }

    public int getId_USUARIO() {
        return id_USUARIO;
    }

    public void setId_USUARIO(int id_USUARIO) {
        this.id_USUARIO = id_USUARIO;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }
}