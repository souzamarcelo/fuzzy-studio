/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.sistema;

import br.udesc.auxiliar.ConexaoMySQL;
import br.udesc.modelo.Projeto;
import br.udesc.modelo.Usuario;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author marcelo
 */

public class ControlaConectados {
    
    ListaConectados lista = ListaConectados.getInstance();
    
    
    public void request(int codUsuario, int codProjeto){
        if(codUsuario != -1 && codProjeto != -1){
            Usuario usuario = buscaUsuario(codUsuario);
            Projeto projeto = buscaProjeto(codProjeto);
            Date data = new Date();

            lista.adicionaLista(usuario, projeto, data);
        }
    }
    
    public Projeto buscaProjeto(int codigo){
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        Statement consulta = null;
        ResultSet resultado = null;
        String sql = "select * from fuzzy.projetos p where p.codigo = "+codigo;
        Projeto project = new Projeto();
        try {
            consulta = conexao.createStatement();
            resultado = consulta.executeQuery(sql);

            while (resultado.next()) {
                
                project.setCodigo(new Integer(resultado.getInt("codigo")));
                project.setNome(new String(resultado.getString("nome")));
                break;
                
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
        } finally {
                try {
                    consulta.close();
                    resultado.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
        
        return project;
    }
    
    
    public Usuario buscaUsuario(int codigo){
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        Statement consulta = null;
        ResultSet resultado = null;
        String sql = "select * from fuzzy.usuarios u where u.codigo = "+codigo;
        Usuario user = new Usuario();
        try {
            consulta = conexao.createStatement();
            resultado = consulta.executeQuery(sql);

            while (resultado.next()) {
                
                user.setCodigo(new Integer(resultado.getInt("codigo")));
                user.setNome(new String(resultado.getString("nome")));
                user.setUser(new String(resultado.getString("user")));
                break;
                
            }
        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
        } finally {
                try {
                    consulta.close();
                    resultado.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
        
        return user;
    }
    
    
    
}
