/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.controle;

import br.udesc.modelo.VariavelSaida;
import br.udesc.modelo.Projeto;
import br.udesc.modelo.Regra;
import br.udesc.modelo.TermoLinguisticoSaida;
import br.udesc.facade.TermoLinguisticoSaidaFacade;
import br.udesc.facade.VariavelSaidaFacade;
import br.udesc.facade.MotorInferenciaFacade;
import br.udesc.facade.RegraFacade;
import br.udesc.auxiliar.ConexaoMySQL;
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
public class VariavelSaidaBean {
    
    @EJB
    private VariavelSaidaFacade variavelfacade;
    private VariavelSaida variavel = new VariavelSaida();
    private List<VariavelSaida> vss = new ArrayList<VariavelSaida>();
    private VariavelSaida variavelselecionada;
    private DataModel<VariavelSaida> variaveisSaidaModel;
    private Projeto projeto = new Projeto();
    
    @EJB
    private TermoLinguisticoSaidaFacade termofacade;
    @EJB
    private RegraFacade regrasfacade;
    @EJB
    private MotorInferenciaFacade motorfacade;
    
    
    public String salvar(){
        int retorno = variavelfacade.salvar(variavel);
        
        if(retorno == 1){
            variavel = new VariavelSaida();        
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso!", "Usuário");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (retorno == 2){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro na variável", "Este nome já existe");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        //this.vss = atualizaLista();
        //this.variaveisSaidaModel = new ListDataModel<VariavelSaida>(vss);
        return "editaVariavelSaida.xhtml";

    }
    
    public String editar(){
        this.variavel = this.variaveisSaidaModel.getRowData();
        return "editaVariavelSaida.xhtml";
    }
    
    public String nova(){
        //this.vss = atualizaLista();
        //this.variaveisSaidaModel = new ListDataModel<VariavelSaida>(this.vss);
        this.variavel = new VariavelSaida();
        return "editaVariavelSaida.xhtml";
    }
    
    public String cancelar(){
        this.variavel = new VariavelSaida();
        return "projeto.xhtml";
    }
    
    public String excluir(){
        this.variavel = variaveisSaidaModel.getRowData();
        excluirRegrasAssociadas();
        excluirTermosAssociados();
        variavelfacade.excluir(variavel);
        //this.vss = atualizaLista();
        //this.variaveisSaidaModel = new ListDataModel<VariavelSaida>(this.vss);
        variavel = new VariavelSaida();
        return "editaVariavelSaida.xhtml";
    }
    
    public void excluirRegrasAssociadas(){
        List<Regra> regras = regrasfacade.selecionaTudoPelaSaida(variavel.getCodigo());
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
        List<TermoLinguisticoSaida> termos = termofacade.selecionaTudoPelaVariavel(variavel.getCodigo());
        for(TermoLinguisticoSaida t : termos){
            termofacade.excluir(t);
        }
    }
    
    public DataModel<VariavelSaida> retornaVariaveisPeloProjeto(int codprojeto){
        this.vss = this.variavelfacade.selecionaTudo(codprojeto);
        this.variaveisSaidaModel = new ListDataModel<VariavelSaida>(vss);
        return this.variaveisSaidaModel;
    }
    
    /*public List<VariavelSaida> atualizaLista(){
        return this.variavelfacade.selecionaTudo(this.projeto.getCodigo());
    }*/

    public VariavelSaida getVariavel() {
        return variavel;
    }

    public void setVariavel(VariavelSaida variavel) {
        this.variavel = variavel;
    }

    public VariavelSaidaFacade getVariavelfacade() {
        return variavelfacade;
    }

    public void setVariavelfacade(VariavelSaidaFacade variavelfacade) {
        this.variavelfacade = variavelfacade;
    }

    public VariavelSaida getVariavelselecionada() {
        return variavelselecionada;
    }

    public void setVariavelselecionada(VariavelSaida variavelselecionada) {
        this.variavelselecionada = variavelselecionada;
    }

    public List<VariavelSaida> getVss() {
        return this.vss;
    }

    public void setVss(List<VariavelSaida> ves) {
        this.vss = ves;
    }

    public DataModel<VariavelSaida> getVariaveisSaidaModel() {
        return variaveisSaidaModel;
    }

    public void setVariaveisSaidaModel(DataModel<VariavelSaida> variaveisSaidaModel) {
        this.variaveisSaidaModel = variaveisSaidaModel;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }
}
