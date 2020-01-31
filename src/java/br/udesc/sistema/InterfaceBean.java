/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.sistema;

import br.udesc.auxiliar.ConexaoMySQL;
import br.udesc.facade.VariavelEntradaFacade;
import br.udesc.facade.VariavelSaidaFacade;
import br.udesc.modelo.Regra;
import br.udesc.modelo.MotorInferencia;
import br.udesc.modelo.VariavelEntrada;
import br.udesc.modelo.VariavelSaida;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;


/**
 *
 * @author marcelo
 */
@ManagedBean
@ApplicationScoped
public class InterfaceBean {
    
    private String variaveisentrada;
    private String variaveissaida;
    private String motores;
    private String linhas;
    
    private List<VariavelEntrada> variaveisentradalist = new ArrayList<VariavelEntrada>();
    private List<VariavelSaida> variaveissaidalist = new ArrayList<VariavelSaida>();
    private List<MotorInferencia> motoreslist = new ArrayList<MotorInferencia>();
    
    private String alteracaoentrada;
    private String alteracaosaida;
    
    private int[] estruturaRegra = new int[16];
    private List<int[]> regras = new ArrayList<int[]>();
    
    private int codProjeto;
    
    @EJB
    private VariavelEntradaFacade variavelentradafacade;
    @EJB
    private VariavelSaidaFacade variavelsaidafacade;
    
    public String retornaComponentes(int c){
        this.codProjeto = c;
        this.montaStringEntradas();
        this.montaStringSaidas();
        this.montaStringMotores();
        this.resolveLinhas();
        //System.out.println(this.linhas);
        if(this.variaveisentrada == null) this.variaveisentrada = "";
        if(this.variaveissaida == null) this.variaveissaida = "";
        if(this.motores == null) this.motores = "";
        if(this.linhas == null) this.linhas = "";
        return this.variaveisentrada+"|"+this.variaveissaida+"|"+this.motores+"|"+this.linhas;
    }
    
    public boolean alteraEntrada(String codigo, String nome, int minimo, int maximo, String unidade){
        
        if(codigo.contains("null")){
            codigo = codigo.substring(4);
        }
        
        String tabela = "variaveisEntrada";
        
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "update "+tabela+" set nome = '"+nome+"', maximo = "+maximo+", minimo = "+minimo+", unidade = '"+unidade+"'  where codigo = "+codigo;
        
        PreparedStatement statement = null;
        
        try {
            statement = conexao.prepareStatement(sql);
            statement.executeUpdate();
            

        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
        } finally {
                try {
                    statement.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
        
        
        return true;
    }
    
    public boolean alteraSaida(String codigo, String nome, int minimo, int maximo, String unidade){
        
        if(codigo.contains("null")){
            codigo = codigo.substring(4);
        }
        
        String tabela = "variaveisSaida";
        
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "update "+tabela+" set nome = '"+nome+"', maximo = "+maximo+", minimo = "+minimo+", unidade = '"+unidade+"'  where codigo = "+codigo;
        
        PreparedStatement statement = null;
        
        try {
            statement = conexao.prepareStatement(sql);
            statement.executeUpdate();
            

        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
        } finally {
                try {
                    statement.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
        
        return true;
    }
    
    public boolean alteraMotor(String codigo, String nome, String metodo_defuz, String metodo_agreg, String mconexao, String metodo_ativ){
        
        if(codigo.contains("null")){
            codigo = codigo.substring(4);
        }
        
        String tabela = "motores";
        
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "update "+tabela+" set nome = '"+nome+"', metodo_defuzzificacao = '"+metodo_defuz+"', metodo_agregacao_regras = '"+metodo_agreg+"', conexao_and = '"+mconexao+"', metodo_ativacao_regras = '"+metodo_ativ+"'  where codigo = "+codigo;
        
        PreparedStatement statement = null;
        
        try {
            statement = conexao.prepareStatement(sql);
            statement.executeUpdate();
            

        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
        } finally {
                try {
                    statement.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
        
        return true;
    }
    
    public boolean excluiElement(int tipo, String codigo){
        
        if(codigo.contains("null")){
            codigo = codigo.substring(4);
        }
        
        if(tipo == 1){
            int[] codsr = codigosRegrasComVariavelEntrada(codigo);
            excluiAntecedentes(codigo);
            excluiTermoEntrada(codigo);
            excluiMotorRegra(codsr);
            excluiRegrasComVariavelEntrada(codigo, codsr);
            
        }
        if(tipo == 2){
            int[] codsr = codigosRegrasComVariavelSaida(codigo);
            excluiConsequentes(codigo);
            excluiTermoSaida(codigo);
            excluiMotorRegra(codsr);
            excluiRegrasComVariavelSaida(codigo, codsr);
            
        }
        if(tipo == 3)
            excluiMotorRegra(codigo);
        
        String tabela = "";
        if(tipo == 1) tabela = "variaveisEntrada";
        if(tipo == 2) tabela = "variaveisSaida";
        if(tipo == 3) tabela = "motores";
        
        
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "delete from "+tabela+" where codigo = "+codigo;
        
        PreparedStatement statement = null;
        
        try {
            statement = conexao.prepareStatement(sql);
            statement.executeUpdate();
            
            if(tipo == 3)
                return excluiMotorRegra(tipo, codigo);
            else
                return true;

        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
            return false;
        } finally {
                try {
                    statement.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
    }
    
    public void excluiMotorRegra(int[] codigos){
        
        for(int i = 0; i < codigos.length; i++){
        
            ConexaoMySQL classeConexao = new ConexaoMySQL();
            Connection conexao = classeConexao.getConexaoMySQL();
            String sql = "delete from motorregra where id_regra = "+codigos[i];

            PreparedStatement statement = null;

            try {
                statement = conexao.prepareStatement(sql);
                statement.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Erro ao executar SQL!" + e.getMessage());
            } finally {
                    try {
                        statement.close();
                        conexao.close();
                    } catch (Throwable e) {
                        System.out.println("Erro ao fechar consulta");
                    }
            }
        }
    }
    
    public int[] codigosRegrasComVariavelSaida(String codigo){
        
        
        String sql = "select b.codigo as code from regras b join consequentes a on b.codigo = a.id_baseRegras where a.id_variavelSaida = "+codigo;
            
            ConexaoMySQL classeConexao = new ConexaoMySQL();
            Connection conexao = classeConexao.getConexaoMySQL();
            Statement consulta = null;
            ResultSet resultado = null;
            
            int[] codigos = new int[200];
            int contador = 0;
            
            try {
                consulta = conexao.createStatement();
                resultado = consulta.executeQuery(sql);
                while (resultado.next()) {
                    
                    codigos[contador] = resultado.getInt("code");
                    contador++;
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
        
        return codigos;
    }
    
    public int[] codigosRegrasComVariavelEntrada(String codigo){
        
        
        String sql = "select b.codigo as code from regras b join antecedentes a on b.codigo = a.id_baseRegras where a.id_variavelEntrada = "+codigo;
            
            ConexaoMySQL classeConexao = new ConexaoMySQL();
            Connection conexao = classeConexao.getConexaoMySQL();
            Statement consulta = null;
            ResultSet resultado = null;
            
            int[] codigos = new int[200];
            int contador = 0;
            
            try {
                consulta = conexao.createStatement();
                resultado = consulta.executeQuery(sql);
                while (resultado.next()) {
                    
                    codigos[contador] = resultado.getInt("code");
                    contador++;
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
        
        return codigos;
    }
    
    public void excluiRegrasComVariavelEntrada(String codigoVariavel, int[] codigos){
        
        for(int i = 0; i < codigos.length; i++){
        
            ConexaoMySQL classeConexao = new ConexaoMySQL();
            Connection conexao = classeConexao.getConexaoMySQL();
            String sql = "delete from regras where codigo = "+codigos[i];

            PreparedStatement statement = null;

            try {
                statement = conexao.prepareStatement(sql);
                statement.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Erro ao executar SQL!" + e.getMessage());
            } finally {
                    try {
                        statement.close();
                        conexao.close();
                    } catch (Throwable e) {
                        System.out.println("Erro ao fechar consulta");
                    }
            }
        }
    }
    
    public void excluiRegrasComVariavelSaida(String codigoVariavel, int[] codigos){
        for(int i = 0; i < codigos.length; i++){
        
            ConexaoMySQL classeConexao = new ConexaoMySQL();
            Connection conexao = classeConexao.getConexaoMySQL();
            String sql = "delete from regras where codigo = "+codigos[i];

            PreparedStatement statement = null;

            try {
                statement = conexao.prepareStatement(sql);
                statement.executeUpdate();

            } catch (SQLException e) {
                System.out.println("Erro ao executar SQL!" + e.getMessage());
            } finally {
                    try {
                        statement.close();
                        conexao.close();
                    } catch (Throwable e) {
                        System.out.println("Erro ao fechar consulta");
                    }
            }
        }
    }
    
    public void excluiAntecedentes(String codigoVariavel){
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "delete from antecedentes where id_variavelEntrada = "+codigoVariavel;
        
        PreparedStatement statement = null;
        
        try {
            statement = conexao.prepareStatement(sql);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
        } finally {
                try {
                    statement.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
    }
    
    public void excluiConsequentes(String codigoVariavel){
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "delete from consequentes where id_variavelSaida = "+codigoVariavel;
        
        PreparedStatement statement = null;
        
        try {
            statement = conexao.prepareStatement(sql);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
        } finally {
                try {
                    statement.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
    }
    
    public void excluiMotorRegra(String codigo){
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "delete from motorregra where id_motor = "+codigo;
        
        PreparedStatement statement = null;
        
        try {
            statement = conexao.prepareStatement(sql);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
        } finally {
                try {
                    statement.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
    }
    
    public void excluiTermoSaida(String codigo){
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "delete from termosLinguisticosSaida where idVariavelSaida = "+codigo;
        
        PreparedStatement statement = null;
        
        try {
            statement = conexao.prepareStatement(sql);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
        } finally {
                try {
                    statement.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
    }
    
    public void excluiTermoEntrada(String codigo){
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "delete from termosLinguisticosEntrada where idVariavelEntrada = "+codigo;
        
        PreparedStatement statement = null;
        
        try {
            statement = conexao.prepareStatement(sql);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
        } finally {
                try {
                    statement.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
    }
    
    public boolean excluiRegrasComVariavelEntrada(int tipo, String codigo){
        if(codigo.contains("null")){
            codigo = codigo.substring(4);
        }
        
        String tabela = "";
        if(tipo == 1) tabela = "variaveisEntrada";
        if(tipo == 2) tabela = "variaveisSaida";
        if(tipo == 3) tabela = "motores";
        
        //System.out.println(codigo +" - "+tipo +" - "+posx +" - "+posy);
        
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "delete from "+tabela+" where codigo = "+codigo;
        
        PreparedStatement statement = null;
        
        try {
            statement = conexao.prepareStatement(sql);
            statement.executeUpdate();
            
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
            return false;
        } finally {
                try {
                    statement.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
    }
    
    public boolean excluiMotorRegra(int tipo, String codigo){
        if(codigo.contains("null")){
            codigo = codigo.substring(4);
        }
        
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "delete from motorregra where id_motor = "+codigo;
        
        PreparedStatement statement = null;
        
        try {
            statement = conexao.prepareStatement(sql);
            statement.executeUpdate();
            
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
            return false;
        } finally {
                try {
                    statement.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
    }
    
    
    public String[] retornaElemento(int tipo, String codigo){
        if(codigo.contains("null")){
            codigo = codigo.substring(4);
        }
        
        String tabela = "";
        if(tipo == 1) tabela += "variaveisEntrada";
        if(tipo == 2) tabela += "variaveisSaida";
        if(tipo == 3) tabela += "motores";
        
        
        String sql = "select * from "+tabela+" t"+
                        " where t.codigo = "+codigo;
            
            ConexaoMySQL classeConexao = new ConexaoMySQL();
            Connection conexao = classeConexao.getConexaoMySQL();
            Statement consulta = null;
            ResultSet resultado = null;
            
            try {
                consulta = conexao.createStatement();
                resultado = consulta.executeQuery(sql);
                while (resultado.next()) {

                    String[] array = new String[6];
                    
                    if(tipo == 1 || tipo == 2){
                        array[0] = String.valueOf(resultado.getInt("codigo"));
                        array[1] = resultado.getString("nome");
                        array[2] = String.valueOf(resultado.getInt("minimo"));
                        array[3] = String.valueOf(resultado.getInt("maximo"));
                        array[4] = resultado.getString("unidade");
                        array[5] = "-1";
                    }
                    
                    if(tipo == 3){
                        array[0] = String.valueOf(resultado.getInt("codigo"));
                        array[1] = resultado.getString("nome");
                        array[2] = resultado.getString("metodo_defuzzificacao");
                        array[3] = resultado.getString("metodo_agregacao_regras");
                        array[4] = resultado.getString("conexao_and");
                        array[5] = resultado.getString("metodo_ativacao_regras");
                    }
                    
                    return array;
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
        
        return null;
    }
    
    
    public boolean gravaPosicoes(String codigo, int tipo, int posx, int posy){
        
        System.out.println(codigo);
        System.out.println("ENTROOOOOOOOOOOOOU");
        
        if(codigo.contains("null")){
            codigo = codigo.substring(4);
        }
        
        String tabela = "";
        if(tipo == 1) tabela = "variaveisEntrada";
        if(tipo == 2) tabela = "variaveisSaida";
        if(tipo == 3) tabela = "motores";
        
        //System.out.println(codigo +" - "+tipo +" - "+posx +" - "+posy);
        
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "update "+tabela+" set posx = "+posx+", posy = "+posy+"  where codigo = "+codigo;
        
        PreparedStatement statement = null;
        
        try {
            statement = conexao.prepareStatement(sql);
            //statement.setInt(1, posx);
            //statement.setInt(2, posy);
            statement.executeUpdate();
            

        } catch (SQLException e) {
            System.out.println("Erro ao executar SQL!" + e.getMessage());
        } finally {
                try {
                    statement.close();
                    conexao.close();
                } catch (Throwable e) {
                    System.out.println("Erro ao fechar consulta");
                }
        }
        
        
        return true;
    } 
    
    
    public void resolveLinhas(){
        this.linhas = "";
        carregaLinhas();
    }
    
    public void carregaLinhas(){
        
        String sql = "select m.posx as xmotor, m.posy as ymotor, ve.posx as xentrada, ve.posy as yentrada, vs.posx as xsaida, vs.posy as ysaida from motores m"+ 
               " join motorregra mr on mr.id_motor = m.codigo" +
               " join regras b on mr.id_regra = b.codigo"+
               " join antecedentes a on a.id_baseRegras = b.codigo"+
               " join consequentes c on c.id_baseRegras = b.codigo"+
               " join variaveisEntrada ve on a.id_variavelEntrada = ve.codigo"+
               " join variaveisSaida vs on c.id_variavelSaida = vs.codigo"+
                       " where m.id_projeto = "+codProjeto;
        
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        Statement consulta = null;
        ResultSet resultado = null;

        try {
            consulta = conexao.createStatement();
            resultado = consulta.executeQuery(sql);
            while (resultado.next()) {
                int xmotor = new Integer(resultado.getInt("xmotor"));
                int ymotor = new Integer(resultado.getInt("ymotor"));
                int xentrada = new Integer(resultado.getInt("xentrada"));
                int yentrada = new Integer(resultado.getInt("yentrada"));
                int xsaida = new Integer(resultado.getInt("xsaida"));
                int ysaida = new Integer(resultado.getInt("ysaida"));
                xmotor += 45; xentrada += 45; xsaida += 45;
                ymotor += 30; yentrada += 30; ysaida += 30;
                
                if(this.linhas.equalsIgnoreCase(""))
                    this.linhas += xmotor+","+ymotor+","+xentrada+","+yentrada+","+xsaida+","+ysaida;
                else
                    this.linhas += ";"+xmotor+","+ymotor+","+xentrada+","+yentrada+","+xsaida+","+ysaida;
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
    }
    
    public int[] getCoordenadaVariavel(int codvar, int es){
        String sql = "";
        int[] mvar = new int[2];
        
        if(es == 1)
            sql = "select v.posx as xvar, v.posy as yvar from variaveisEntrada v where codigo = "+codvar;
        
        if(es == 2)
            sql = "select v.posx as xvar, v.posy as yvar from variaveisSaida v where codigo = "+codvar;
        
        
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        Statement consulta = null;
        ResultSet resultado = null;

        try {
            consulta = conexao.createStatement();
            resultado = consulta.executeQuery(sql);
            while (resultado.next()) {

                mvar[0] = new Integer(resultado.getInt("xvar"));
                mvar[1] = new Integer(resultado.getInt("yvar"));
                //System.out.println("Codigo: " + ve.getCodigo() + "\n" + "Nome: " + ve.getNome());               
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
        
        return mvar;
    }
    
    
    public void montaStringMotores(){
        this.motores = "";
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        Statement consulta = null;
        ResultSet resultado = null;
        String sql = "select * from fuzzy.motores m where m.id_projeto = "+codProjeto;
        try {
            consulta = conexao.createStatement();
            resultado = consulta.executeQuery(sql);

            while (resultado.next()) {
                
                MotorInferencia mi = new MotorInferencia();
                mi.setCodigo(new Integer(resultado.getInt("codigo")));
                mi.setNome(new String(resultado.getString("nome")));
                mi.setPosx(new Integer(resultado.getInt("posx")));
                mi.setPosy(new Integer(resultado.getInt("posy")));

                this.motoreslist.add(mi);
                //System.out.println("Codigo: " + ve.getCodigo() + "\n" + "Nome: " + ve.getNome());               
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
        
        for(int i = 0; i < motoreslist.size(); i++){
            MotorInferencia m = motoreslist.get(i);
            
            if(i > 0){
                this.motores += ";"+m.getCodigo()+
                    ","+m.getNome()+","+m.getPosx()+","+m.getPosy();
            } else {
                this.motores += m.getCodigo()+
                    ","+m.getNome()+","+m.getPosx()+","+m.getPosy();
            }
        }
    }
    
    public void montaStringSaidas(){
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        Statement consulta = null;
        ResultSet resultado = null;
        String sql = "select * from fuzzy.variaveisSaida vs where vs.id_projeto = "+codProjeto;
        try {
            consulta = conexao.createStatement();
            resultado = consulta.executeQuery(sql);

            while (resultado.next()) {
                
                VariavelSaida vs = new VariavelSaida();
                vs.setCodigo(new Integer(resultado.getInt("codigo")));
                vs.setNome(new String(resultado.getString("nome")));
                vs.setMinimo(new Double(resultado.getDouble("minimo")));
                vs.setMaximo(new Double(resultado.getDouble("maximo")));
                vs.setUnidade(new String(resultado.getString("unidade")));
                vs.setPosx(new Integer(resultado.getInt("posx")));
                vs.setPosy(new Integer(resultado.getInt("posy")));

                this.variaveissaidalist.add(vs);
                //System.out.println("Codigo: " + ve.getCodigo() + "\n" + "Nome: " + ve.getNome());               
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
        
        for(int i = 0; i < variaveissaidalist.size(); i++){
            VariavelSaida v = variaveissaidalist.get(i);
            
            if(i > 0){
                this.variaveissaida += ";"+v.getCodigo()+
                    ","+v.getNome()+","+v.getMinimo()+
                    ","+v.getMaximo()+","+v.getUnidade()+
                    ","+v.getPosx()+","+v.getPosy();
            } else {
                this.variaveissaida += v.getCodigo()+
                    ","+v.getNome()+","+v.getMinimo()+
                    ","+v.getMaximo()+","+v.getUnidade()+
                    ","+v.getPosx()+","+v.getPosy();
            }
        }
    }
    
    
    public void montaStringEntradas(){
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        Statement consulta = null;
        ResultSet resultado = null;
        String sql = "select * from fuzzy.variaveisEntrada ve where ve.id_projeto = "+codProjeto;
        try {
            consulta = conexao.createStatement();
            resultado = consulta.executeQuery(sql);

            while (resultado.next()) {
                
                VariavelEntrada ve = new VariavelEntrada();
                ve.setCodigo(new Integer(resultado.getInt("codigo")));
                ve.setNome(new String(resultado.getString("nome")));
                ve.setMinimo(new Double(resultado.getDouble("minimo")));
                ve.setMaximo(new Double(resultado.getDouble("maximo")));
                ve.setUnidade(new String(resultado.getString("unidade")));
                ve.setPosx(new Integer(resultado.getInt("posx")));
                ve.setPosy(new Integer(resultado.getInt("posy")));

                this.variaveisentradalist.add(ve);
                //System.out.println("Codigo: " + ve.getCodigo() + "\n" + "Nome: " + ve.getNome());               
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
        
        for(int i = 0; i < variaveisentradalist.size(); i++){
            VariavelEntrada v = variaveisentradalist.get(i);
            
            if(i > 0){
                this.variaveisentrada += ";"+v.getCodigo()+
                    ","+v.getNome()+","+v.getMinimo()+
                    ","+v.getMaximo()+","+v.getUnidade()+
                    ","+v.getPosx()+","+v.getPosy();
            } else {
                this.variaveisentrada += v.getCodigo()+
                    ","+v.getNome()+","+v.getMinimo()+
                    ","+v.getMaximo()+","+v.getUnidade()+
                    ","+v.getPosx()+","+v.getPosy();
            }
        }
    }
    
    public void alteraregistro(AjaxBehaviorEvent evt){
        System.out.println(alteracaoentrada);
        System.out.println(alteracaosaida);
    }
    
    public void carregaDoBanco(){
        this.variaveisentradalist = variavelentradafacade.selecionaTudo(16);
        this.variaveissaidalist = variavelsaidafacade.selecionaTudo(16);
        this.variaveisentrada = "";
        this.variaveissaida = "";
        
        for(int i = 0; i < variaveisentradalist.size(); i++){
            VariavelEntrada v = variaveisentradalist.get(i);
            
            if(i > 0){
                this.variaveisentrada += ";"+v.getCodigo()+
                    ","+v.getNome()+","+v.getMinimo()+
                    ","+v.getMaximo()+","+v.getUnidade()+
                    ","+v.getPosx()+","+v.getPosy();
            } else {
                this.variaveisentrada += v.getCodigo()+
                    ","+v.getNome()+","+v.getMinimo()+
                    ","+v.getMaximo()+","+v.getUnidade()+
                    ","+v.getPosx()+","+v.getPosy();
            }
        }
        
        for(int i = 0; i < variaveissaidalist.size(); i++){
            VariavelSaida v = variaveissaidalist.get(i);
            
            if(i > 0){
                this.variaveissaida += ";"+v.getCodigo()+
                    ","+v.getNome()+","+v.getMinimo()+
                    ","+v.getMaximo()+","+v.getUnidade()+
                    ","+v.getPosx()+","+v.getPosy();
            } else {
                this.variaveissaida += v.getCodigo()+
                    ","+v.getNome()+","+v.getMinimo()+
                    ","+v.getMaximo()+","+v.getUnidade()+
                    ","+v.getPosx()+","+v.getPosy();
            }
        }
    }

    public String getVariaveisentrada() {
        this.carregaDoBanco();
        return variaveisentrada;
    }

    public void setVariaveisentrada(String variaveisentrada) {
        this.variaveisentrada = variaveisentrada;
    }

    public String getVariaveissaida() {
        return variaveissaida;
    }

    public void setVariaveissaida(String variaveissaida) {
        this.variaveissaida = variaveissaida;
    }

    public VariavelEntradaFacade getVariavelentradafacade() {
        return variavelentradafacade;
    }

    public void setVariavelentradafacade(VariavelEntradaFacade variavelentradafacade) {
        this.variavelentradafacade = variavelentradafacade;
    }

    public VariavelSaidaFacade getVariavelsaidafacade() {
        return variavelsaidafacade;
    }

    public void setVariavelsaidafacade(VariavelSaidaFacade variavelsaidafacade) {
        this.variavelsaidafacade = variavelsaidafacade;
    }

    public List<VariavelEntrada> getVariaveisentradalist() {
        return variaveisentradalist;
    }

    public void setVariaveisentradalist(List<VariavelEntrada> variaveisentradalist) {
        this.variaveisentradalist = variaveisentradalist;
    }

    public List<VariavelSaida> getVariaveissaidalist() {
        return variaveissaidalist;
    }

    public void setVariaveissaidalist(List<VariavelSaida> variaveissaidalist) {
        this.variaveissaidalist = variaveissaidalist;
    }

    public String getAlteracaoentrada() {
        return alteracaoentrada;
    }

    public void setAlteracaoentrada(String alteracaoentrada) {
        this.alteracaoentrada = alteracaoentrada;
    }

    public String getAlteracaosaida() {
        return alteracaosaida;
    }

    public void setAlteracaosaida(String alteracaosaida) {
        this.alteracaosaida = alteracaosaida;
    }

    public String getMotores() {
        return motores;
    }

    public void setMotores(String motores) {
        this.motores = motores;
    }

    public List<MotorInferencia> getMotoreslist() {
        return motoreslist;
    }

    public void setMotoreslist(List<MotorInferencia> motoreslist) {
        this.motoreslist = motoreslist;
    }
    
    public List<VariavelEntrada> getVes(){
        return null;
    }
    
    public List<VariavelSaida> getVss(){
        return null;
    }
    
    public List<Regra> getRgs(){
        return null;
    }
    
    public List<MotorInferencia> getMts(){
        return null;
    }

    public int[] getEstruturaRegra() {
        return estruturaRegra;
    }

    public void setEstruturaRegra(int[] estruturaRegra) {
        this.estruturaRegra = estruturaRegra;
    }

    public String getLinhas() {
        return linhas;
    }

    public void setLinhas(String linhas) {
        this.linhas = linhas;
    }

    public List<int[]> getRegras() {
        return regras;
    }

    public void setRegras(List<int[]> regras) {
        this.regras = regras;
    }
}
