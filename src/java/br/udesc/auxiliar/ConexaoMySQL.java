/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.auxiliar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author marcelo
 */

public class ConexaoMySQL {

    public static String status = "Não conectou...";
    
    public ConexaoMySQL() {}

    public static java.sql.Connection getConexaoMySQL() {
        Connection connection = null;
        try {
            String driverName = "com.mysql.jdbc.Driver";                        
            Class.forName(driverName);
            
            String serverName = "localhost:8889";
            String mydatabase = "fuzzy";
            String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
            String username = "<username>";
            String password = "<password>";
            connection = DriverManager.getConnection(url, username, password);

            if (connection != null) {
                status = ("STATUS--->Conectado com sucesso!");
            } else {
                status = ("STATUS--->Não foi possivel realizar conexão");
            }
            return connection;

        } catch (ClassNotFoundException e) {
            System.out.println("O driver expecificado nao foi encontrado.");
            return null;
        } catch (SQLException e) {
            System.out.println("Nao foi possivel conectar ao Banco de Dados.");
            return null;
        }
    }

    public static String statusConection() {
        return status;
    }
    
    public static boolean FecharConexao() {
        try {
            ConexaoMySQL.getConexaoMySQL().close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static java.sql.Connection ReiniciarConexao() {
        FecharConexao();
        return ConexaoMySQL.getConexaoMySQL();
    }
    
}