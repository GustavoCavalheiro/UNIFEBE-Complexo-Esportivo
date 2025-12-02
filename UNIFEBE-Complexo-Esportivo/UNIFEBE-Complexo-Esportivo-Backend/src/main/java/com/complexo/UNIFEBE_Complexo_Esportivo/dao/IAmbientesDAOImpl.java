package com.complexo.UNIFEBE_Complexo_Esportivo.dao;

import java.util.ArrayList;

public interface IAmbientesDAOImpl {
    // Inserir um registro de ambiente
    int inserirAmbiente(Ambientes ambiente);

    // Remover um registro de ambiente
    int removerAmbiente(Ambientes ambiente);

    // Atualizar um registro de ambiente
    int atualizarAmbiente(Ambientes ambiente);

    // Consultar ambientes registrados
    ArrayList<Ambientes> consultarAmbientes();

    // Consultar ambiente por nome
    ArrayList<Ambientes> buscarAmbientePorNome(String nome);
}
