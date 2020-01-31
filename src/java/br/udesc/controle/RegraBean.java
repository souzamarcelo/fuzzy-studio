/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.controle;

import br.udesc.modelo.VariavelSaida;
import br.udesc.modelo.VariavelEntrada;
import br.udesc.modelo.Antecedente;
import br.udesc.modelo.Regra;
import br.udesc.modelo.TermoLinguisticoEntrada;
import br.udesc.modelo.TermoLinguisticoSaida;
import br.udesc.modelo.Consequente;
import br.udesc.facade.ProjetoFacade;
import br.udesc.facade.TermoLinguisticoSaidaFacade;
import br.udesc.facade.TermoLinguisticoEntradaFacade;
import br.udesc.facade.VariavelSaidaFacade;
import br.udesc.facade.VariavelEntradaFacade;
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
public class RegraBean {
    
    private Regra base = new Regra();
    private List<Regra> bases = new ArrayList<Regra>();
    private DataModel<Regra> basesDataModel;
    
    private List<VariavelEntrada> entradasDaRegra = new ArrayList<VariavelEntrada>();
    private List<VariavelSaida> saidasDaRegra = new ArrayList<VariavelSaida>();
    
    @EJB
    private ProjetoFacade projetofacade;
    @EJB
    private RegraFacade baseregrasfacade;
    @EJB
    private VariavelEntradaFacade entradafacade;
    @EJB
    private TermoLinguisticoEntradaFacade termoentradafacade;
    @EJB
    private VariavelSaidaFacade saidafacade;
    @EJB
    private TermoLinguisticoSaidaFacade termosaidafacade;
    
    public List<TermoLinguisticoEntrada> retornaTermosDaEntrada(int codVar){
        List<TermoLinguisticoEntrada> termos = termoentradafacade.selecionaTudoPelaVariavel(codVar);
        TermoLinguisticoEntrada t = new TermoLinguisticoEntrada();
        t.setNome("");
        termos.add(0, t);
        return termos;
    }
    
    public List<VariavelSaida> retornaSaidas(int codProjeto){
        this.saidasDaRegra = saidafacade.selecionaTudo(codProjeto);
        return saidasDaRegra;
    }
    
    public List<TermoLinguisticoSaida> retornaTermosDaSaida(int codVar){
        List<TermoLinguisticoSaida> termos = termosaidafacade.selecionaTudoPelaVariavel(codVar);
        TermoLinguisticoSaida t = new TermoLinguisticoSaida();
        t.setNome("");
        termos.add(0, t);
        return termos;
    }
    
    public List<VariavelEntrada> retornaEntradas(int codProjeto){
        this.entradasDaRegra = entradafacade.selecionaTudo(codProjeto);
        return entradasDaRegra;
    }
    
    public String nova(int codProjeto){
        this.base = new Regra();
        this.base.setProjeto(projetofacade.selecionaPorCodigo(codProjeto));
        return "editaBaseRegras.xhtml";
    }

    public String salvar(){
        String descricao_antecedente = "IF ";
        String descricao_consequente = "THEN ";
        
        List<Antecedente> antecedenteDaRegra = new ArrayList<Antecedente>();
        int contador = 0;
        for(VariavelEntrada v : entradasDaRegra){
            contador++;
            if(v.getTermo() != null)
                if(!v.getTermo().equalsIgnoreCase("")){
                    Antecedente a = new Antecedente();
                    a.setBaseRegras(base);
                    a.setVariavelEntrada(v);
                    
                    for(TermoLinguisticoEntrada t : termoentradafacade.selecionaTudoPelaVariavel(v.getCodigo())){
                        if(t.getNome().equalsIgnoreCase(v.getTermo())){
                            a.setTermo(t);
                        }
                    }
                    
                    antecedenteDaRegra.add(a);
                    
                    if(contador == 1)
                        descricao_antecedente += v.getNome() + " IS " + v.getTermo() + " ";
                    else
                        descricao_antecedente += base.getConector().toUpperCase() + " " + v.getNome() + " IS " + v.getTermo() + " ";
                }
                    
        }
        
        List<Consequente> consequenteDaRegra = new ArrayList<Consequente>();
        for(VariavelSaida v : saidasDaRegra){
            if(v.getTermo() != null)
                if(!v.getTermo().equalsIgnoreCase("")){
                    Consequente c = new Consequente();
                    c.setBaseRegras(base);
                    c.setVariavelSaida(v);
                    
                    for(TermoLinguisticoSaida t : termosaidafacade.selecionaTudoPelaVariavel(v.getCodigo())){
                        if(t.getNome().equalsIgnoreCase(v.getTermo())){
                            c.setTermo(t);
                        }
                    }
                    
                    consequenteDaRegra.add(c);
                    
                    descricao_consequente += v.getNome() + " IS " + v.getTermo() + " ";
                }
                    
        }
        
        base.setDescricaoAntecedente(descricao_antecedente);
        base.setDescricaoConsequente(descricao_consequente);
        base.setConsequentes(consequenteDaRegra);
        base.setAntecedentes(antecedenteDaRegra);
        int retorno = baseregrasfacade.salvar(base);
        
        if(retorno == 1){
            base = new Regra();
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Regra adicionada com sucesso!", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else if (retorno == 2){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao adicionar regra!", "Regra duplicada");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao adicionar regra!", "Erro desconhecido");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return null;
    }
    
    public String excluir(){
        Regra baseselecionada = basesDataModel.getRowData();
        excluiMotoresAssociados(baseselecionada);
        baseregrasfacade.excluir(baseselecionada);
        return "editaBaseRegras.xhtml";
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
    
    public String cancelar(){
        this.base = new Regra();
        return "projeto.xhtml";
    }
    
    public DataModel<Regra> retornaBasesPeloProjeto(int codProjeto){
        this.bases = baseregrasfacade.selecionaTudo(codProjeto);
        this.basesDataModel = new ListDataModel<Regra>(this.bases);
        return this.basesDataModel;
    }
    
    public Regra getBase() {
        return base;
    }

    public void setBase(Regra base) {
        this.base = base;
    }

    public List<Regra> getBases() {
        return bases;
    }

    public void setBases(List<Regra> bases) {
        this.bases = bases;
    }

    public DataModel<Regra> getBasesDataModel() {
        return basesDataModel;
    }

    public void setBasesDataModel(DataModel<Regra> basesDataModel) {
        this.basesDataModel = basesDataModel;
    }

    public RegraFacade getBaseregrasfacade() {
        return baseregrasfacade;
    }

    public void setBaseregrasfacade(RegraFacade baseregrasfacade) {
        this.baseregrasfacade = baseregrasfacade;
    }

    public VariavelEntradaFacade getEntradafacade() {
        return entradafacade;
    }

    public void setEntradafacade(VariavelEntradaFacade entradafacade) {
        this.entradafacade = entradafacade;
    }

    public ProjetoFacade getProjetofacade() {
        return projetofacade;
    }

    public void setProjetofacade(ProjetoFacade projetofacade) {
        this.projetofacade = projetofacade;
    }

    public VariavelSaidaFacade getSaidafacade() {
        return saidafacade;
    }

    public void setSaidafacade(VariavelSaidaFacade saidafacade) {
        this.saidafacade = saidafacade;
    }

    public TermoLinguisticoEntradaFacade getTermoentradafacade() {
        return termoentradafacade;
    }

    public void setTermoentradafacade(TermoLinguisticoEntradaFacade termoentradafacade) {
        this.termoentradafacade = termoentradafacade;
    }

    public TermoLinguisticoSaidaFacade getTermosaidafacade() {
        return termosaidafacade;
    }

    public void setTermosaidafacade(TermoLinguisticoSaidaFacade termosaidafacade) {
        this.termosaidafacade = termosaidafacade;
    }
    
}
