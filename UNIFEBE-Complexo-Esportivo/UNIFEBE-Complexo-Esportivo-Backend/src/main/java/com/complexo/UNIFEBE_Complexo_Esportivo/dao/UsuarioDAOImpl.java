package com.complexo.UNIFEBE_Complexo_Esportivo.dao;

import com.complexo.UNIFEBE_Complexo_Esportivo.controller.CredenciaisBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UsuarioDAOImpl implements IUsuarioDAOImpl {

    private ConexaoDB db;

    public UsuarioDAOImpl() {
        CredenciaisBanco credencial = new CredenciaisBanco();
        this.db = new ConexaoDB(
                credencial.getIP(),
                credencial.getDatabase(),
                credencial.getUser(),
                credencial.getPwd_banco()
        );
    }

    // Inserir um novo usuário
    @Override
    public int inserirUsuario(Usuario usuario) {
        // Primeiro verificar se a matrícula já existe
        String checkSQL = "SELECT COUNT(*) FROM sisagenda.usuario WHERE Matricula = ?";
        String insertSQL = "INSERT INTO sisagenda.usuario (id_USUARIO, Nome, Matricula, Senha, Tipo) " +
                "VALUES (sisagenda.increment_usuario.nextval, ?, ?, ?, ?)";
        
        try (Connection conn = db.getConnection()) {
            
            // Verificar duplicidade
            try (PreparedStatement pstmtCheck = conn.prepareStatement(checkSQL)) {
                pstmtCheck.setInt(1, usuario.getMatricula());
                try (ResultSet rs = pstmtCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println(" Matrícula " + usuario.getMatricula() + " já existe!");
                        return -1; // Matrícula duplicada
                    }
                }
            }
            
            // Inserir usuário
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, usuario.getNome());
                pstmt.setInt(2, usuario.getMatricula());
                pstmt.setString(3, usuario.getSenha());
                pstmt.setString(4, String.valueOf(usuario.getTipo()));
                int linhasAfetadas = pstmt.executeUpdate();
                System.out.println("Usuário inserido: " + linhasAfetadas + " linha(s)");
                return linhasAfetadas;
            }
            
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            System.out.println("Matrícula duplicada: " + e.getMessage());
            return -1;
        } catch (Exception e) {
            System.out.println("Erro ao inserir usuário: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Remover um usuário por matrícula
    @Override
    public int removerUsuarioPorMaticula(Usuario usuario) {
        System.out.println("=== REMOVER USUARIO ===");
        System.out.println("Matrícula: " + usuario.getMatricula());
        
        String sqlBuscar = "SELECT id_USUARIO FROM sisagenda.usuario WHERE Matricula = ?";
        String sqlAgendamentos = "DELETE FROM sisagenda.agendamentos WHERE USUARIO_id_USUARIO = ?";
        String sqlUsuario = "DELETE FROM sisagenda.usuario WHERE id_USUARIO = ?";
        
        try (Connection conn = db.getConnection()) {
            System.out.println("Conexão obtida, AutoCommit: " + conn.getAutoCommit());
            
            // Buscar ID do usuário
            int idUsuario;
            try (PreparedStatement pstmtBuscar = conn.prepareStatement(sqlBuscar)) {
                pstmtBuscar.setInt(1, usuario.getMatricula());
                try (ResultSet rset = pstmtBuscar.executeQuery()) {
                    if (!rset.next()) {
                        System.out.println("Usuário não encontrado: " + usuario.getMatricula());
                        return 0;
                    }
                    idUsuario = rset.getInt("id_USUARIO");
                    System.out.println("ID do usuário encontrado: " + idUsuario);
                }
            }
            
            // Excluir agendamentos do usuário
            try (PreparedStatement pstmtAgendamentos = conn.prepareStatement(sqlAgendamentos)) {
                pstmtAgendamentos.setInt(1, idUsuario);
                int agendamentosExcluidos = pstmtAgendamentos.executeUpdate();
                System.out.println("Agendamentos excluídos: " + agendamentosExcluidos);
            }

            
            // Excluir usuário
            try (PreparedStatement pstmtUsuario = conn.prepareStatement(sqlUsuario)) {
                pstmtUsuario.setInt(1, idUsuario);
                int linhasAfetadas = pstmtUsuario.executeUpdate();
                System.out.println("Usuário removido: " + linhasAfetadas + " linha(s)");
                return linhasAfetadas;
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao remover usuário: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Buscar usuário por matrícula
    @Override
    public Usuario buscarUsuarioPorMatricula(int matricula) {
        String SQL = "SELECT * FROM sisagenda.usuario WHERE Matricula = ?";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, matricula);
            
            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId_USUARIO(rset.getInt("Id_USUARIO"));
                    usuario.setNome(rset.getString("Nome"));
                    usuario.setSenha(rset.getString("Senha"));
                    usuario.setMatricula(rset.getInt("Matricula"));
                    usuario.setTipo(rset.getString("Tipo").charAt(0));
                    return usuario;

                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Consultar usuário por nome
    @Override
    public ArrayList<Usuario> buscarUsuarioPorNome(String nome) {
        ArrayList<Usuario> lista = new ArrayList<>();
        String SQL = "SELECT * FROM sisagenda.usuario WHERE Nome LIKE ?";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId_USUARIO(rset.getInt("Id_USUARIO"));
                    usuario.setNome(rset.getString("Nome"));
                    usuario.setSenha(rset.getString("Senha"));
                    usuario.setMatricula(rset.getInt("Matricula"));
                    usuario.setTipo(rset.getString("Tipo").charAt(0));
                    lista.add(usuario);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar usuário: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Consultar todos os usuários
    @Override
    public ArrayList<Usuario> selectUsuarios() {
        ArrayList<Usuario> lista = new ArrayList<>();
        String SQL = "SELECT * FROM sisagenda.usuario";
        
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rset = stmt.executeQuery(SQL)) {
            
            while (rset.next()) {
                Usuario usuario = new Usuario();
                usuario.setId_USUARIO(rset.getInt("Id_USUARIO"));
                usuario.setNome(rset.getString("Nome"));
                usuario.setSenha(rset.getString("Senha"));
                usuario.setMatricula(rset.getInt("Matricula"));
                usuario.setTipo(rset.getString("Tipo").charAt(0));
                lista.add(usuario);
            }
            System.out.println("Usuários consultados: " + lista.size());
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar usuários: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}
