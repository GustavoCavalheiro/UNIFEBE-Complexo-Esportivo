package com.complexo.UNIFEBE_Complexo_Esportivo.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexaoDB {

    private static HikariDataSource dataSource;
    private String connectionUrl;

    public ConexaoDB(String IP, String database, String user, String senha) {
        this.connectionUrl = "jdbc:oracle:thin:@" + IP + ":1521:" + database;
        
        // Inicializa o pool de conexões
        if (dataSource == null) {
            synchronized (ConexaoDB.class) {
                if (dataSource == null) {
                    initializeDataSource(IP, database, user, senha);
                }
            }
        }
    }
    
    private void initializeDataSource(String IP, String database, String user, String senha) {
        try {
            
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:oracle:thin:@" + IP + ":1521:" + database);
            config.setUsername(user);
            config.setPassword(senha);
            config.setDriverClassName("oracle.jdbc.OracleDriver");

            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(300000);
            config.setConnectionTimeout(20000);
            config.setMaxLifetime(1200000);
            config.setLeakDetectionThreshold(60000);
            
            // Configurações específicas para Oracle
            config.addDataSourceProperty("oracle.jdbc.ReadTimeout", "60000");
            config.setAutoCommit(true);

            config.setPoolName("UNIFEBE-Oracle-Pool");
            
            dataSource = new HikariDataSource(config);

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Pool de conexões não inicializado!");
        }
        
        Connection conn = dataSource.getConnection();
        
        // Log para debug
        System.out.println("Conexão obtida - Ativas: " + dataSource.getHikariPoolMXBean().getActiveConnections() +
                          ", Ociosas: " + dataSource.getHikariPoolMXBean().getIdleConnections() +
                          ", Total: " + dataSource.getHikariPoolMXBean().getTotalConnections());
        
        return conn;
    }

    public static String getPoolStats() {
        if (dataSource != null && dataSource.getHikariPoolMXBean() != null) {
            return String.format("Pool Stats - Ativas: %d, Ociosas: %d, Total: %d, Aguardando: %d",
                dataSource.getHikariPoolMXBean().getActiveConnections(),
                dataSource.getHikariPoolMXBean().getIdleConnections(),
                dataSource.getHikariPoolMXBean().getTotalConnections(),
                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
        return "Pool não inicializado";
    }

    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("Pool de conexões fechado.");
        }
    }}