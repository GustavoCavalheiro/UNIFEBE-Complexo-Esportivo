package com.complexo.UNIFEBE_Complexo_Esportivo.dao;

import com.complexo.UNIFEBE_Complexo_Esportivo.controller.CredenciaisBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {

    private ConexaoDB db;

    public Login() {
        CredenciaisBanco credencial = new CredenciaisBanco();
        this.db = new ConexaoDB(
                credencial.getIP(),
                credencial.getDatabase(),
                credencial.getUser(),
                credencial.getPwd_banco()
        );
    }

    // Validar a entrada do usuário
    public boolean validarLogin(int matricula, String senha) {
        System.out.println("=== VALIDAR LOGIN ===");
        System.out.println("Matrícula: " + matricula);
        System.out.println("Senha: " + senha);
        
        String SQL = "SELECT * FROM sisagenda.usuario WHERE Matricula = ? AND Senha = ?";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            System.out.println("Conexão obtida com sucesso");
            
            pstmt.setInt(1, matricula);
            pstmt.setString(2, senha);
            
            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    String nome = rset.getString("Nome");
                    System.out.println("Usuário encontrado: " + nome);
                    return true;
                } else {
                    System.out.println("Usuário não encontrado com essas credenciais");

                    verificarUsuarioExiste(matricula);
                    return false;
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao validar login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Método de debug para verificar se o usuário existe
    private void verificarUsuarioExiste(int matricula) {
        String SQL = "SELECT Nome, Senha FROM sisagenda.usuario WHERE Matricula = ?";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, matricula);
            
            try (ResultSet rset = pstmt.executeQuery()) {
                if (rset.next()) {
                    System.out.println("[Debug] Usuário existe! Nome: " + rset.getString("Nome"));
                    System.out.println("[Debug] Senha no banco: '" + rset.getString("Senha") + "'");
                } else {
                    System.out.println("[Debug] Matrícula " + matricula + " não existe no banco");
                }
            }
            
        } catch (Exception e) {
            System.out.println("[Debug] Erro: " + e.getMessage());
        }
    }
}
