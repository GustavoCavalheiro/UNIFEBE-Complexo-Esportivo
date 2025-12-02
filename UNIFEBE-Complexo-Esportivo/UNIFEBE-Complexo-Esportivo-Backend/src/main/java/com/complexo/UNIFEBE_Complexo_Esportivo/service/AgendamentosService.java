package com.complexo.UNIFEBE_Complexo_Esportivo.service;

import com.complexo.UNIFEBE_Complexo_Esportivo.dao.Agendamentos;
import com.complexo.UNIFEBE_Complexo_Esportivo.dao.AgendamentosDAOImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AgendamentosService {

    private final AgendamentosDAOImpl dao = new AgendamentosDAOImpl();

    public int inserirAgendamento(Agendamentos a) {
        int res = dao.inserirAgendamento(a);
        // retorna 1 para conflito 2 caso contrário
        return res == -1 ? 2 : res;
    }

    public int cancelarAgendamento(int id) {
        System.out.println("=== SERVICE CANCELAR AGENDAMENTO ===");
        System.out.println("ID recebido: " + id);
        Agendamentos a = new Agendamentos();
        a.setID_AGENDAMENTOS(id);
        int resultado = dao.cancelarAgendamento(a);
        System.out.println("Resultado do DAO: " + resultado);
        return resultado;
    }
    
    public int excluirAgendamento(int id) {
        System.out.println("=== SERVICE EXCLUIR AGENDAMENTO ===");
        System.out.println("ID recebido: " + id);
        int resultado = dao.excluirAgendamento(id);
        System.out.println("Resultado do DAO: " + resultado);
        return resultado;
    }

    public ArrayList<Agendamentos> consultarAgendamentosUsuario(int id) {
        return dao.consultarAgendamentosUsuario(id);
    }

    public ArrayList<Agendamentos> consultarAgendamentosUsuarioFuturos(int id) {
        return dao.consultarAgendamentosUsuarioFuturos(id);
    }

    public ArrayList<Agendamentos> consultarAgendamentosAmbiente(int id) {
        return dao.consultarAgendamentosAmbiente(id);
    }

    public ArrayList<Agendamentos> consultarAgendamentosAmbienteFuturos(int id) {
        return dao.consultarAgendamentosAmbienteFuturos(id);
    }
}
