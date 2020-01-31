/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.controle;

import br.udesc.auxiliar.ConexaoMySQL;
import br.udesc.facade.RegraFacade;
import br.udesc.facade.MotorInferenciaFacade;
import br.udesc.facade.TermoLinguisticoEntradaFacade;
import br.udesc.facade.VariavelEntradaFacade;
import br.udesc.modelo.Regra;
import br.udesc.modelo.TermoLinguisticoEntrada;
import br.udesc.modelo.VariavelEntrada;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author marcelo
 */

@ManagedBean
@SessionScoped
public class VariavelEntradaBean {
    
    @EJB
    private VariavelEntradaFacade variavelfacade;
    private VariavelEntrada variavel = new VariavelEntrada();
    private VariavelEntrada variavelselecionada;
    private List<VariavelEntrada> ves = new ArrayList<VariavelEntrada>();
    private DataModel<VariavelEntrada> variaveisEntradaModel;
    
    @EJB
    private TermoLinguisticoEntradaFacade termofacade;
    @EJB
    private RegraFacade regrasfacade;
    @EJB
    private MotorInferenciaFacade motorfacade;
    
    public String salvar(){
        int retorno = variavelfacade.salvar(variavel);
        
        if(retorno == 1){
            variavel = new VariavelEntrada();        
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso!", "Usuário");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (retorno == 2){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro na variável", "Este nome já existe");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        //this.ves = atualizaLista();
        //this.variaveisEntradaModel = new ListDataModel<VariavelEntrada>(this.ves);
        return "editaVariavelEntrada.xhtml";

    }
    
    public String editar(){
        this.variavel = this.variaveisEntradaModel.getRowData();
        return "editaVariavelEntrada.xhtml";
    }
    
    public String nova(){
        //this.ves = atualizaLista();
        //this.variaveisEntradaModel = new ListDataModel<VariavelEntrada>(this.ves);
        this.variavel = new VariavelEntrada();
        return "editaVariavelEntrada.xhtml";
    }
    
    public String cancelar(){
        this.variavel = new VariavelEntrada();
        return "projeto.xhtml";
    }
    
    public String excluir(){
        this.variavel = variaveisEntradaModel.getRowData();
        excluirRegrasAssociadas();
        excluirTermosAssociados();
        variavelfacade.excluir(variavel);
        //this.ves = atualizaLista();
        //this.variaveisEntradaModel = new ListDataModel<VariavelEntrada>(this.ves);
        variavel = new VariavelEntrada();
        return "editaVariavelEntrada.xhtml";
    }
    
    public void excluirRegrasAssociadas(){
        List<Regra> regras = regrasfacade.selecionaTudoPelaEntrada(variavel.getCodigo());
        for(Regra r : regras){  
            excluiMotoresAssociados(r);
            regrasfacade.excluir(r);
        }
    }
    
    public void excluiMotoresAssociados(Regra b){
        ConexaoMySQL classeConexao = new ConexaoMySQL();
        Connection conexao = classeConexao.getConexaoMySQL();
        String sql = "delete from motorregra where id_regra = "+b.getCodigo();

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
    
    public void excluirTermosAssociados(){
        List<TermoLinguisticoEntrada> termos = termofacade.selecionaTudoPelaVariavel(variavel.getCodigo());
        for(TermoLinguisticoEntrada t : termos){
            termofacade.excluir(t);
        }
    }
    
    /*public List<VariavelEntrada> atualizaLista(){
        return this.variavelfacade.selecionaTudo(16);
    }*/
    
    public DataModel<VariavelEntrada> retornaVariaveisPeloProjeto(int codprojeto){
        this.ves = this.variavelfacade.selecionaTudo(codprojeto);
        this.variaveisEntradaModel = new ListDataModel<VariavelEntrada>(ves);
        return this.variaveisEntradaModel;
    }
    
    
    public VariavelEntrada getVariavel() {
        return variavel;
    }

    public void setVariavel(VariavelEntrada variavel) {
        this.variavel = variavel;
    }

    public VariavelEntradaFacade getVariavelfacade() {
        return variavelfacade;
    }

    public void setVariavelfacade(VariavelEntradaFacade variavelfacade) {
        this.variavelfacade = variavelfacade;
    }

    public VariavelEntrada getVariavelselecionada() {
        return variavelselecionada;
    }

    public void setVariavelselecionada(VariavelEntrada variavelselecionada) {
        this.variavelselecionada = variavelselecionada;
    }

    public List<VariavelEntrada> getVes() {
        return this.ves;
    }

    public void setVes(List<VariavelEntrada> ves) {
        this.ves = ves;
    }

    public DataModel<VariavelEntrada> getVariaveisEntradaModel() {
        return variaveisEntradaModel;
    }

    public void setVariaveisEntradaModel(DataModel<VariavelEntrada> variaveisEntradaModel) {
        this.variaveisEntradaModel = variaveisEntradaModel;
    }
}
