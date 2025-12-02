package com.complexo.UNIFEBE_Complexo_Esportivo.service;

import com.complexo.UNIFEBE_Complexo_Esportivo.dao.Ambientes;
import com.complexo.UNIFEBE_Complexo_Esportivo.dao.AmbientesDAOImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AmbientesService {

    private final AmbientesDAOImpl dao = new AmbientesDAOImpl();

    public int inserir(Ambientes a) {
        return dao.inserirAmbiente(a);
    }

    public int remover(int id) {
        System.out.println("=== SERVICE REMOVER AMBIENTE ===");
        System.out.println("ID recebido no Service: " + id);
        Ambientes a = new Ambientes();
        a.setId_AMBIENTES(id);
        System.out.println("Chamando DAO.removerAmbiente...");
        int resultado = dao.removerAmbiente(a);
        System.out.println("Resultado do DAO: " + resultado);
        return resultado;
    }

    public int atualizar(Ambientes a) {
        return dao.atualizarAmbiente(a);
    }

    public ArrayList<Ambientes> listar() {
        return dao.consultarAmbientes();
    }

    public ArrayList<Ambientes> buscarPorNome(String nome) {
        return dao.buscarAmbientePorNome(nome);
    }
}