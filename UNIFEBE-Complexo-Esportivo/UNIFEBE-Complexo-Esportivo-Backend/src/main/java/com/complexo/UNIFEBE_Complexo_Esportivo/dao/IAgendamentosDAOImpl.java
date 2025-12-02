package com.complexo.UNIFEBE_Complexo_Esportivo.dao;

import java.util.ArrayList;

public interface IAgendamentosDAOImpl {
    // Inserir agendamento
    int inserirAgendamento(Agendamentos agendamento);

    // Cancelar agendamento
    int cancelarAgendamento(Agendamentos agendamento);
    
    // Excluir agendamento permanentemente
    int excluirAgendamento(int idAgendamento);

    // Consultar todos os agendamentos de um usuário
    ArrayList<Agendamentos> consultarAgendamentosUsuario(int id_usuario);

    // Consultar todos os agendamentos de um ambiente
    ArrayList<Agendamentos> consultarAgendamentosAmbiente(int id_ambiente);

    // Consultar todos os agendamentos futuros de um ambiente
    ArrayList<Agendamentos> consultarAgendamentosAmbienteFuturos(int id_ambiente);

    // Consultar todos os agendamentos futuros de um user
    ArrayList<Agendamentos> consultarAgendamentosUsuarioFuturos(int id_usuario);
}
