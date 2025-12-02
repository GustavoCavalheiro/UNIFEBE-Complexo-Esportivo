package com.complexo.UNIFEBE_Complexo_Esportivo.dao;

import java.util.ArrayList;

public interface IUsuarioDAOImpl {
    // Inserir um novo usuário
    int inserirUsuario(Usuario usuario);

    // Remover um usuário por matrícula
    int removerUsuarioPorMaticula(Usuario usuario);

    // Buscar usuário por matícula
    Usuario buscarUsuarioPorMatricula(int matricula);

    // Consultar usuário por nome
    ArrayList<Usuario> buscarUsuarioPorNome(String nome);

    // Consultar todos os usuários
    ArrayList<Usuario> selectUsuarios();
}
