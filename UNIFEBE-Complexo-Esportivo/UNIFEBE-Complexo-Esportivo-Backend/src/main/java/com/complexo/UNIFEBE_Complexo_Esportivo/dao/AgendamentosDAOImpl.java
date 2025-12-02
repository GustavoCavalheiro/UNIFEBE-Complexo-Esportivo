package com.complexo.UNIFEBE_Complexo_Esportivo.dao;

import com.complexo.UNIFEBE_Complexo_Esportivo.controller.CredenciaisBanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AgendamentosDAOImpl implements IAgendamentosDAOImpl {

    private ConexaoDB db;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public AgendamentosDAOImpl() {
        CredenciaisBanco credencial = new CredenciaisBanco();
        this.db = new ConexaoDB(
                credencial.getIP(),
                credencial.getDatabase(),
                credencial.getUser(),
                credencial.getPwd_banco()
        );
    }

    // Inserir agendamento
    @Override
    public int inserirAgendamento(Agendamentos agendamento) {
        String sqlConflito = "SELECT COUNT(*) FROM sisagenda.agendamentos " +
                "WHERE AMBIENTE_id_AMBIENTES = ? " +
                "AND Status_agendamento = 'A' " +
                "AND TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') < Data_Hora_Fim " +
                "AND TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') > Data_Hora_Inicio";
        
        String sqlInsert = "INSERT INTO sisagenda.agendamentos (id_AGENDAMENTOS, " +
                "AMBIENTE_id_AMBIENTES, USUARIO_id_USUARIO, Data_Hora_Inicio, " +
                "Data_Hora_Fim, Data_Hora_Agendamento, Status_agendamento) " +
                "VALUES (sisagenda.increment_agendamentos.nextval, ?, ?, " +
                "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), " +
                "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), " +
                "TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS'), ?)";
        
        try (Connection conn = db.getConnection()) {
            
            // Verificar conflito
            try (PreparedStatement pstmtConflito = conn.prepareStatement(sqlConflito)) {
                pstmtConflito.setInt(1, agendamento.getAMBIENTE_ID_AMBIENTES());
                pstmtConflito.setString(2, agendamento.getData_Hora_Inicio());
                pstmtConflito.setString(3, agendamento.getData_Hora_Fim());
                
                try (ResultSet rset = pstmtConflito.executeQuery()) {
                    if (rset.next() && rset.getInt(1) > 0) {
                        System.out.println("❌ Conflito de agendamento!");
                        return -1;
                    }
                }
            }
            
            // Inserir agendamento
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                pstmt.setInt(1, agendamento.getAMBIENTE_ID_AMBIENTES());
                pstmt.setInt(2, agendamento.getUSUARIO_ID_USUARIO());
                pstmt.setString(3, agendamento.getData_Hora_Inicio());
                pstmt.setString(4, agendamento.getData_Hora_Fim());
                pstmt.setString(5, agendamento.getData_Hora_Agendamento());
                pstmt.setString(6, String.valueOf(agendamento.getStatus_agendamento()));
                
                int linhasAfetadas = pstmt.executeUpdate();
                System.out.println("Agendamento inserido: " + linhasAfetadas + " linha(s)");
                return linhasAfetadas;
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao agendar: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Cancelar agendamento
    @Override
    public int cancelarAgendamento(Agendamentos agendamento) {
        System.out.println("=== CANCELAR AGENDAMENTO ===");
        System.out.println("ID: " + agendamento.getID_AGENDAMENTOS());
        
        String SQL = "UPDATE sisagenda.agendamentos SET Status_agendamento = 'C' WHERE id_AGENDAMENTOS = ?";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            System.out.println("Conexão obtida, AutoCommit: " + conn.getAutoCommit());
            
            pstmt.setInt(1, agendamento.getID_AGENDAMENTOS());
            int linhasAfetadas = pstmt.executeUpdate();
            System.out.println("Agendamento cancelado: " + linhasAfetadas + " linha(s)");
            return linhasAfetadas;
            
        } catch (Exception e) {
            System.out.println("Erro ao cancelar: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Excluir agendamento permanentemente
    @Override
    public int excluirAgendamento(int idAgendamento) {
        System.out.println("=== EXCLUIR AGENDAMENTO ===");
        System.out.println("ID: " + idAgendamento);
        
        String SQL = "DELETE FROM sisagenda.agendamentos WHERE id_AGENDAMENTOS = ?";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, idAgendamento);
            int linhasAfetadas = pstmt.executeUpdate();
            System.out.println("Agendamento excluído: " + linhasAfetadas + " linha(s)");
            return linhasAfetadas;
            
        } catch (Exception e) {
            System.out.println("Erro ao excluir: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Consultar todos os agendamentos de um user
    @Override
    public ArrayList<Agendamentos> consultarAgendamentosUsuario(int id_usuario) {
        ArrayList<Agendamentos> lista = new ArrayList<>();
        String SQL = "SELECT * FROM sisagenda.agendamentos WHERE USUARIO_id_USUARIO = ? AND Status_agendamento = 'A'";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, id_usuario);
            
            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    lista.add(mapResultSetToAgendamento(rset));
                }
            }

            
        } catch (Exception e) {
            System.out.println("Erro ao consultar agendamentos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Consultar todos os agendamentos de um ambiente
    @Override
    public ArrayList<Agendamentos> consultarAgendamentosAmbiente(int id_ambiente) {
        ArrayList<Agendamentos> lista = new ArrayList<>();
        String SQL = "SELECT * FROM sisagenda.agendamentos WHERE AMBIENTE_id_AMBIENTES = ?";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, id_ambiente);
            
            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    lista.add(mapResultSetToAgendamento(rset));
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao consultar agendamentos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Consultar todos os agendamentos futuros de um ambiente
    @Override
    public ArrayList<Agendamentos> consultarAgendamentosAmbienteFuturos(int id_ambiente) {
        ArrayList<Agendamentos> lista = new ArrayList<>();
        LocalDateTime agora = LocalDateTime.now();
        String aux = agora.format(formatter);
        
        String SQL = "SELECT * FROM sisagenda.agendamentos " +
                "WHERE AMBIENTE_id_AMBIENTES = ? AND " +
                "Data_Hora_Inicio >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') " +
                "ORDER BY Data_Hora_Inicio";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, id_ambiente);
            pstmt.setString(2, aux);
            
            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    lista.add(mapResultSetToAgendamento(rset));
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao consultar agendamentos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // Consultar todos os agendamentos futuros de um usuário
    @Override
    public ArrayList<Agendamentos> consultarAgendamentosUsuarioFuturos(int id_usuario) {
        ArrayList<Agendamentos> lista = new ArrayList<>();
        LocalDateTime agora = LocalDateTime.now();
        String aux = agora.format(formatter);
        
        String SQL = "SELECT * FROM sisagenda.agendamentos " +
                "WHERE USUARIO_id_USUARIO = ? AND " +
                "Status_agendamento = 'A' AND " +
                "Data_Hora_Inicio >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') " +
                "ORDER BY Data_Hora_Inicio";
        
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setInt(1, id_usuario);
            pstmt.setString(2, aux);
            
            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    lista.add(mapResultSetToAgendamento(rset));
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao consultar agendamentos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
    
    // Método auxiliar para mapear ResultSet para Agendamentos
    private Agendamentos mapResultSetToAgendamento(ResultSet rset) throws Exception {
        Agendamentos agendamento = new Agendamentos();
        agendamento.setID_AGENDAMENTOS(rset.getInt("id_AGENDAMENTOS"));
        agendamento.setAMBIENTE_ID_AMBIENTES(rset.getInt("AMBIENTE_id_AMBIENTES"));
        agendamento.setUSUARIO_ID_USUARIO(rset.getInt("USUARIO_id_USUARIO"));
        agendamento.setData_Hora_Inicio(sdf.format(rset.getTimestamp("Data_Hora_Inicio")));
        agendamento.setData_Hora_Fim(sdf.format(rset.getTimestamp("Data_Hora_Fim")));
        agendamento.setData_Hora_Agendamento(sdf.format(rset.getTimestamp("Data_Hora_Agendamento")));
        agendamento.setStatus_agendamento(rset.getString("Status_agendamento").charAt(0));
        return agendamento;
    }
}
