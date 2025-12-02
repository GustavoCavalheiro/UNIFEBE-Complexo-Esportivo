package com.complexo.UNIFEBE_Complexo_Esportivo.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Agendamentos {

    private int ID_AGENDAMENTOS;

    @JsonProperty("AMBIENTE_ID_AMBIENTES")
    private int AMBIENTE_ID_AMBIENTES;

    @JsonProperty("USUARIO_ID_USUARIO")
    private int USUARIO_ID_USUARIO;

    @JsonProperty("Data_Hora_Inicio")
    private String Data_Hora_Inicio;

    @JsonProperty("Data_Hora_Fim")
    private String Data_Hora_Fim;

    @JsonProperty("Data_Hora_Agendamento")
    private String Data_Hora_Agendamento;

    @JsonProperty("Status_agendamento")
    private char Status_agendamento;


    // Construtor
    public Agendamentos() {
    }

    // Get and set
    public int getID_AGENDAMENTOS() {
        return ID_AGENDAMENTOS;
    }

    public void setID_AGENDAMENTOS(int ID_AGENDAMENTOS) {
        this.ID_AGENDAMENTOS = ID_AGENDAMENTOS;
    }

    public int getAMBIENTE_ID_AMBIENTES() {
        return AMBIENTE_ID_AMBIENTES;
    }

    public void setAMBIENTE_ID_AMBIENTES(int AMBIENTE_ID_AMBIENTES) {
        this.AMBIENTE_ID_AMBIENTES = AMBIENTE_ID_AMBIENTES;
    }

    public int getUSUARIO_ID_USUARIO() {
        return USUARIO_ID_USUARIO;
    }

    public void setUSUARIO_ID_USUARIO(int USUARIO_ID_USUARIO) {
        this.USUARIO_ID_USUARIO = USUARIO_ID_USUARIO;
    }

    public String getData_Hora_Inicio() {
        return Data_Hora_Inicio;
    }

    public void setData_Hora_Inicio(String data_Hora_Inicio) {
        Data_Hora_Inicio = data_Hora_Inicio;
    }

    public String getData_Hora_Fim() {
        return Data_Hora_Fim;
    }

    public void setData_Hora_Fim(String data_Hora_Fim) {
        Data_Hora_Fim = data_Hora_Fim;
    }

    public String getData_Hora_Agendamento() {
        return Data_Hora_Agendamento;
    }

    public void setData_Hora_Agendamento(String data_Hora_Agendamento) {
        Data_Hora_Agendamento = data_Hora_Agendamento;
    }

    public char getStatus_agendamento() {
        return Status_agendamento;
    }

    public void setStatus_agendamento(char status) {
        Status_agendamento = status;
    }

}