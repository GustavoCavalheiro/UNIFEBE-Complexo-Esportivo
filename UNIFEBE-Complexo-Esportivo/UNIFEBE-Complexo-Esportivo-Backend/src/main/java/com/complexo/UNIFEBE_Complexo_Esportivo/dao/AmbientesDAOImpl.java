package com.complexo.UNIFEBE_Complexo_Esportivo.dao;

import com.complexo.UNIFEBE_Complexo_Esportivo.controller.CredenciaisBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AmbientesDAOImpl implements IAmbientesDAOImpl {

    private ConexaoDB db;

    public AmbientesDAOImpl() {
        CredenciaisBanco credencial = new CredenciaisBanco();
        this.db = new ConexaoDB(
                credencial.getIP(),
                credencial.getDatabase(),
                credencial.getUser(),
                credencial.getPwd_banco()
        );
    }

    // Inserir um registro de ambiente
    @Override
    public int inserirAmbiente(Ambientes ambiente) {
        String SQL = "INSERT INTO sisagenda.ambientes (id_AMBIENTES, Nome_Ambiente, Descricao) " +
                "VALUES (sisagenda.increment_ambientes.nextval, ?, ?)";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, ambiente.getNome_ambiente());
            pstmt.setString(2, ambiente.getDescricao());
            int linhasAfetadas = pstmt.executeUpdate();
            System.out.println("Ambiente inserido: " + linhasAfetadas + " linha(s)");
            return linhasAfetadas;
            
        } catch (Exception e) {
            System.out.println("Erro ao inserir ambiente: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Remover um registro de ambiente
    @Override
    public int removerAmbiente(Ambientes ambiente) {
        System.out.println("=== REMOVER AMBIENTE (DAO) ===");
        System.out.println("ID do ambiente: " + ambiente.getId_AMBIENTES());
        
        String sqlAgendamentos = "DELETE FROM sisagenda.agendamentos WHERE AMBIENTE_id_AMBIENTES = ?";
        String sqlAmbiente = "DELETE FROM sisagenda.ambientes WHERE id_AMBIENTES = ?";
        
        try (Connection conn = db.getConnection()) {
            System.out.println("Conexão obtida: " + (conn != null));
            System.out.println("AutoCommit: " + conn.getAutoCommit());
            
            // Primeiro, excluir todos os agendamentos deste ambiente
            try (PreparedStatement pstmtAgendamentos = conn.prepareStatement(sqlAgendamentos)) {
                pstmtAgendamentos.setInt(1, ambiente.getId_AMBIENTES());
                int agendamentosExcluidos = pstmtAgendamentos.executeUpdate();
                System.out.println("Agendamentos excluídos: " + agendamentosExcluidos);
            }
            
            // Depois, excluir o ambiente
            try (PreparedStatement pstmtAmbiente = conn.prepareStatement(sqlAmbiente)) {
                pstmtAmbiente.setInt(1, ambiente.getId_AMBIENTES());
                int linhasAfetadas = pstmtAmbiente.executeUpdate();
                System.out.println("Ambiente removido: " + linhasAfetadas + " linha(s)");
                return linhasAfetadas;
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao remover ambiente: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Atualizar um registro de ambiente
    @Override
    public int atualizarAmbiente(Ambientes ambiente) {
        String SQL = "UPDATE sisagenda.ambientes SET Nome_Ambiente = ?, Descricao = ? WHERE id_AMBIENTES = ?";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, ambiente.getNome_ambiente());
            pstmt.setString(2, ambiente.getDescricao());
            pstmt.setInt(3, ambiente.getId_AMBIENTES());

            int linhasAfetadas = pstmt.executeUpdate();
            System.out.println("Ambiente atualizado: " + linhasAfetadas + " linha(s)");
            return linhasAfetadas;
            
        } catch (Exception e) {
            System.out.println("Erro ao atualizar ambiente: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Consultar ambientes registrados
    @Override
    public ArrayList<Ambientes> consultarAmbientes() {
        ArrayList<Ambientes> lista = new ArrayList<>();
        String SQL = "SELECT * FROM sisagenda.ambientes ORDER BY Nome_ambiente";
        
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rset = stmt.executeQuery(SQL)) {
            
            while (rset.next()) {
                Ambientes ambiente = new Ambientes();
                ambiente.setId_AMBIENTES(rset.getInt("id_AMBIENTES"));
                ambiente.setNome_ambiente(rset.getString("Nome_ambiente"));
                ambiente.setDescricao(rset.getString("Descricao"));
                lista.add(ambiente);
            }
            System.out.println("Ambientes consultados: " + lista.size());
            
        } catch (Exception e) {
            System.out.println("Erro ao consultar ambientes: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Consultar ambiente por nome
    @Override
    public ArrayList<Ambientes> buscarAmbientePorNome(String nome) {
        ArrayList<Ambientes> lista = new ArrayList<>();
        String SQL = "SELECT * FROM sisagenda.ambientes WHERE Nome_ambiente LIKE ? ORDER BY Nome_ambiente";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    Ambientes ambiente = new Ambientes();
                    ambiente.setId_AMBIENTES(rset.getInt("id_AMBIENTES"));
                    ambiente.setNome_ambiente(rset.getString("Nome_ambiente"));
                    ambiente.setDescricao(rset.getString("Descricao"));
                    lista.add(ambiente);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao buscar ambiente: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}
