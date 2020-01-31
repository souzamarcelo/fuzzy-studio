/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.controle;

import br.udesc.auxiliar.ConexaoMySQL;
import br.udesc.facade.RegraFacade;
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
import javax.faces.component.UISelectOne;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author marcelo
 */

@ManagedBean
@SessionScoped
public class TermoLinguisticoEntradaBean {
    
    private TermoLinguisticoEntrada termo = new TermoLinguisticoEntrada();
    
    @EJB
    private TermoLinguisticoEntradaFacade termofacade;
    @EJB
    private VariavelEntradaFacade variavelfacade;
    
    private int codigoVariavel;
    private VariavelEntrada variavelEntrada = new VariavelEntrada();  
    private HtmlInputText inputVariavel;
    private List<TermoLinguisticoEntrada> termosDaVariavel = new ArrayList<TermoLinguisticoEntrada>();
    private int codigoTermoSelecionado;
    //Valores para renderizacao dos componentes de valores
    private boolean rvalora = false;
    private boolean rvalorb = false;
    private boolean rvalorc = false;
    private boolean rvalord = false;
    
    private UISelectOne inputFuncao;
    
    @EJB
    private RegraFacade regrasfacade;
    
    public String geraGrafico(){
        return "index.xhtml";
    }
    
    public void salvar(int codprojeto){
        
        this.setVariavelEntrada(variavelfacade.selecionaPorNome((String)inputVariavel.getValue(), codprojeto));
        termo.setVariavelEntrada(variavelEntrada);
        int retorno = termofacade.salvar(termo);
        
        if(retorno == 1){
            termo = new TermoLinguisticoEntrada();        

            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso!", "Usuário");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (retorno == 2){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro no termo", "Entrada duplicada");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
        this.atualizaTermos(variavelEntrada.getCodigo());
    }
    
    public void excluir(){
        int aux = this.variavelEntrada.getCodigo();
        excluirRegrasAssociadas();
        termofacade.excluir(termo);
        termo = new TermoLinguisticoEntrada();
        this.atualizaTermos(aux);
    }
    
    public void excluirRegrasAssociadas(){
        List<Regra> regras = regrasfacade.selecionaTudoPelaEntrada(termo.getVariavelEntrada().getCodigo());
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
    
    public void atualizaCamposForm(AjaxBehaviorEvent evt){
        this.atualizaCamposForm2();
    }
    
    public void atualizaCamposForm2(){
        if(inputFuncao.getValue() != null)
        {
        if(((String)inputFuncao.getValue()).equalsIgnoreCase("triangular")){
            rvalora = true;
            rvalorb = true;
            rvalorc = true;
            rvalord = false;
        }
        
        if(((String)inputFuncao.getValue()).equalsIgnoreCase("trapezoidal")){
            rvalora = true;
            rvalorb = true;
            rvalorc = true;
            rvalord = true;
        }
        
        if(((String)inputFuncao.getValue()).equalsIgnoreCase("rampaesquerda")){
            rvalora = true;
            rvalorb = true;
            rvalorc = false;
            rvalord = false;
        }
        
        if(((String)inputFuncao.getValue()).equalsIgnoreCase("rampadireita")){
            rvalora = true;
            rvalorb = true;
            rvalorc = false;
            rvalord = false;
        }
        
        if(((String)inputFuncao.getValue()).equalsIgnoreCase("inclinacaoesquerda")){
            rvalora = true;
            rvalorb = false;
            rvalorc = false;
            rvalord = false;
        }
        
        if(((String)inputFuncao.getValue()).equalsIgnoreCase("inclinacaodireita")){
            rvalora = true;
            rvalorb = false;
            rvalorc = false;
            rvalord = false;
        }
        
        if(((String)inputFuncao.getValue()).equalsIgnoreCase("discreto")){
            rvalora = true;
            rvalorb = false;
            rvalorc = false;
            rvalord = false;
        }
        }
    }
    
    public String carregaTermo(int codvar){
        this.atualizaTermos(codvar);
        this.setCodigoVariavel(codvar);
        this.atualizaCamposForm2();
        return "editaTermoEntrada.xhtml";
    }
    
    public String atualizaTermos(int codvar){
        this.variavelEntrada = variavelfacade.selecionaPorCodigo(codvar);
        
        termosDaVariavel = termofacade.selecionaTudoPelaVariavel(codvar);
        return "editaTermoEntrada.xhtml";
    }
    
    public String cancelar(){
        this.termo = new TermoLinguisticoEntrada();
        this.termosDaVariavel = new ArrayList<TermoLinguisticoEntrada>();
        return "editaVariavelEntrada.xhtml";
    }
    
    public String cancelarTermoCorrente(){
        this.termo = new TermoLinguisticoEntrada();
        this.rvalora = false;
        this.rvalorb = false;
        this.rvalorc = false;
        this.rvalord = false;
        this.inputFuncao = null;
        return "editaTermoEntrada.xhtml";
    }

    public TermoLinguisticoEntrada getTermo() {
        return termo;
    }

    public void setTermo(TermoLinguisticoEntrada termo) {
        this.termo = termo;
    }

    public TermoLinguisticoEntradaFacade getTermofacade() {
        return termofacade;
    }

    public void setTermofacade(TermoLinguisticoEntradaFacade termofacade) {
        this.termofacade = termofacade;
    }

    public VariavelEntrada getVariavelEntrada() {
        return variavelEntrada;
    }

    public void setVariavelEntrada(VariavelEntrada variavelEntrada) {
        this.variavelEntrada = variavelEntrada;
    }

    public VariavelEntradaFacade getVariavelfacade() {
        return variavelfacade;
    }

    public void setVariavelfacade(VariavelEntradaFacade variavelfacade) {
        this.variavelfacade = variavelfacade;
    }

    public HtmlInputText getInputVariavel() {
        return inputVariavel;
    }

    public void setInputVariavel(HtmlInputText inputVariavel) {
        this.inputVariavel = inputVariavel;
    }

    public List<TermoLinguisticoEntrada> getTermosDaVariavel() {
        return termosDaVariavel;
    }

    public void setTermosDaVariavel(List<TermoLinguisticoEntrada> termosDaVariavel) {
        this.termosDaVariavel = termosDaVariavel;
    }

    public int getCodigoTermoSelecionado() {
        return codigoTermoSelecionado;
    }

    public void setCodigoTermoSelecionado(int codigoTermoSelecionado) {
        this.codigoTermoSelecionado = codigoTermoSelecionado;
    }

    public boolean isRvalora() {
        return rvalora;
    }

    public void setRvalora(boolean rvalora) {
        this.rvalora = rvalora;
    }

    public boolean isRvalorb() {
        return rvalorb;
    }

    public void setRvalorb(boolean rvalorb) {
        this.rvalorb = rvalorb;
    }

    public boolean isRvalorc() {
        return rvalorc;
    }

    public void setRvalorc(boolean rvalorc) {
        this.rvalorc = rvalorc;
    }

    public boolean isRvalord() {
        return rvalord;
    }

    public void setRvalord(boolean rvalord) {
        this.rvalord = rvalord;
    }

    public UISelectOne getInputFuncao() {
        return inputFuncao;
    }

    public void setInputFuncao(UISelectOne inputFuncao) {
        this.inputFuncao = inputFuncao;
    }

    public int getCodigoVariavel() {
        return codigoVariavel;
    }

    public void setCodigoVariavel(int codigoVariavel) {
        this.codigoVariavel = codigoVariavel;
    }
}
