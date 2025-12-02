package com.complexo.UNIFEBE_Complexo_Esportivo.controller;

public class CredenciaisBanco {

    // Credencias de acesso ao banco de dados
    private String database = "orcl";
    private String IP = "192.168.1.107";
    private String user = "sisagenda";
    private String pwd_banco = "sisagenda";

    public CredenciaisBanco() {
    }

    // Consultar credencias
    public String getDatabase() {
        return database;
    }

    public String getIP() {
        return IP;
    }

    public String getUser() {
        return user;
    }

    public String getPwd_banco() {
        return pwd_banco;
    }
}
