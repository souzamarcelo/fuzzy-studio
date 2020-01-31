/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.controle;

import br.udesc.facade.RegraFacade;
import br.udesc.facade.MotorInferenciaFacade;
import br.udesc.modelo.Regra;
import br.udesc.modelo.MotorInferencia;
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
public class MotorInferenciaBean {

    MotorInferencia motor = new MotorInferencia();
    MotorInferencia motorselecionado = new MotorInferencia();
    private List<Regra> bases = new ArrayList<Regra>();
    private List<MotorInferencia> motores = new ArrayList<MotorInferencia>();
    private DataModel<MotorInferencia> motoresDataModel;
    
    private int codProjeto;
    
    @EJB
    MotorInferenciaFacade motorfacade = new MotorInferenciaFacade();
    
    @EJB
    RegraFacade basefacade = new RegraFacade();
    
    List<Integer> codigosregras = new ArrayList<Integer>();
    
    public String salvar(){
        List<Regra> basescapturadas = new ArrayList<Regra>();
        for(int i = 0; i < codigosregras.size(); i++){
            Object ob = codigosregras.get(i);
            String str = String.valueOf(ob);
            int cod = Integer.parseInt(str);
            basescapturadas.add(basefacade.selecionaPeloCodigo(cod));
        }
        this.motor.setRegras(basescapturadas);
        
        int retorno = motorfacade.salvar(motor);
        
        if(retorno == 1){
            motor = new MotorInferencia();        
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso!", "Usuário");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (retorno == 2){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro na variável", "Este nome já existe");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
        //this.motores = this.atualizaMotores();
        //this.motoresDataModel = new ListDataModel<MotorInferencia>(this.getMotores());
        
        return "editaMotor.xhtml";

    }
    
    public String editar(){
        this.motorselecionado = motoresDataModel.getRowData();
        this.setMotor(motorselecionado);
        return "editaMotor.xhtml";
    }
    
    public String nova(int codProjeto){
        this.motor = new MotorInferencia();
        this.motoresDataModel = new ListDataModel<MotorInferencia>(this.getMotores());
        this.codProjeto = codProjeto;
        return "editaMotor.xhtml";
    }
    
    public String cancelar(){
        this.motor = new MotorInferencia();
        return "projeto.xhtml";
    }
    
    public String excluir(){
        motorselecionado = motoresDataModel.getRowData();
        this.setMotor(motorfacade.selecionaPorCodigo(motorselecionado.getCodigo()));
        motorfacade.excluir(motor);
        motor = new MotorInferencia();
        //this.motores = atualizaMotores();
        //this.motoresDataModel = new ListDataModel<MotorInferencia>(this.getMotores());
        return "editaMotor.xhtml";
    }
    
    public DataModel<MotorInferencia> retornaMotoresPeloProjeto(int codprojeto){
        this.motores = motorfacade.selecionaTudo(codprojeto);
        this.motoresDataModel = new ListDataModel<MotorInferencia>(motores);
        return motoresDataModel;
    }
    
    /*public List<MotorInferencia> atualizaMotores(){
        return motorfacade.selecionaTudo();
    }*/

    public MotorInferencia getMotor() {
        return motor;
    }

    public void setMotor(MotorInferencia motor) {
        this.motor = motor;
    }

    public MotorInferenciaFacade getMotorfacade() {
        return motorfacade;
    }

    public void setMotorfacade(MotorInferenciaFacade motorfacade) {
        this.motorfacade = motorfacade;
    }

    public MotorInferencia getMotorselecionado() {
        return motorselecionado;
    }

    public void setMotorselecionado(MotorInferencia motorselecionado) {
        this.motorselecionado = motorselecionado;
    }

    public List<Regra> getBases() {
        //AQUIIIIIIIIIII
        return basefacade.selecionaTudo(codProjeto);
    }

    public void setBases(List<Regra> bases) {
        this.bases = bases;
    }

    public RegraFacade getBasefacade() {
        return basefacade;
    }

    public void setBasefacade(RegraFacade basefacade) {
        this.basefacade = basefacade;
    }

    public List<Integer> getCodigosregras() {
        return codigosregras;
    }

    public void setCodigosregras(List<Integer> codigosregras) {
        this.codigosregras = codigosregras;
    }

    public List<MotorInferencia> getMotores() {
        return this.motores;
    }

    public void setMotores(List<MotorInferencia> motores) {
        this.motores = motores;
    }

    public DataModel<MotorInferencia> getMotoresDataModel() {
        return motoresDataModel;
    }

    public void setMotoresDataModel(DataModel<MotorInferencia> motoresDataModel) {
        this.motoresDataModel = motoresDataModel;
    }
}
