package com.complexo.UNIFEBE_Complexo_Esportivo.service;

import com.complexo.UNIFEBE_Complexo_Esportivo.dao.Usuario;
import com.complexo.UNIFEBE_Complexo_Esportivo.dao.UsuarioDAOImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UsuarioService {

    private final UsuarioDAOImpl dao = new UsuarioDAOImpl();

    public int inserir(Usuario usuario) {
        int resultado = dao.inserirUsuario(usuario);
        // Retorna: 1 = sucesso, -1 = matrícula duplicada, 0 = outro erro
        return resultado;
    }

    public int removerPorMatricula(int matricula) {
        Usuario u = new Usuario();
        u.setMatricula(matricula);
        return dao.removerUsuarioPorMaticula(u);
    }

    public Usuario buscarPorMatricula(int matricula) {
        return dao.buscarUsuarioPorMatricula(matricula);
    }

    public ArrayList<Usuario> buscarPorNome(String nome) {
        return dao.buscarUsuarioPorNome(nome);
    }

    public ArrayList<Usuario> listarTodos() {
        return dao.selectUsuarios();
    }

    public ArrayList<Usuario> listarPorTipo(char tipo) {
        ArrayList<Usuario> todos = dao.selectUsuarios();
        ArrayList<Usuario> out = new ArrayList<>();
        for (Usuario u : todos) if (u.getTipo() == tipo) out.add(u);
        return out;
    }
}
