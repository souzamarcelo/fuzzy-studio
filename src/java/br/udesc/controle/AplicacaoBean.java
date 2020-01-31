/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.controle;

import br.udesc.facade.ProjetoFacade;
import br.udesc.facade.UsuarioFacade;
import br.udesc.facade.VariavelEntradaFacade;
import br.udesc.modelo.MotorInferencia;
import br.udesc.modelo.Projeto;
import br.udesc.modelo.Usuario;
import br.udesc.modelo.VariavelEntrada;
import br.udesc.modelo.VariavelSaida;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.annotation.*;

/**
 *
 * @author marcelo
 */

@ManagedBean
@SessionScoped
public class AplicacaoBean {
    
    @EJB
    private UsuarioFacade usuariofacade;
    @EJB
    private ProjetoFacade projetofacade;
    @EJB
    private VariavelEntradaFacade entradafacade;
    
    private Usuario usuario = new Usuario();
    private DataModel<Projeto> projetosModel;
    private Projeto projeto = new Projeto();
    
    private String descricaoEntradasExecucao = "";
    
    
    public String fecharProjeto(ExecucaoBean execucaoBean, ConectadosBean conectadosBean){
        conectadosBean.removeRegistro(usuario, projeto);
        conectadosBean.limpaProjetosPorUsuario(usuario.getCodigo());
        execucaoBean.setRetorno_log("");
        execucaoBean.setTabindex(0);
        execucaoBean.setValoresEntrada(new ArrayList<VariavelEntrada>());
        execucaoBean.setValoresMotores(new ArrayList<MotorInferencia>());
        execucaoBean.setValoresSaida(new ArrayList<VariavelSaida>());
        this.projeto = new Projeto();
        return atualizaProjetos();
    }
    
    public String abrirProjeto(ConectadosBean conectadosBean){
        conectadosBean.limpaProjetosPorUsuario(usuario.getCodigo());
        conectadosBean.adicionaRegistro(usuario, projeto);
        return "projeto.xhtml";
    }
    
    public String login(ConectadosBean conectadosBean){
        List<Usuario> usuariosBanco = usuariofacade.selecionaTudo();
        Usuario usuarioEncontrado = new Usuario();
        
        boolean encontrouUser = false;
        boolean permissao = false;
        for(Usuario u : usuariosBanco){
            if(usuario.getUser().equals(u.getUser())){
                encontrouUser = true;
                usuarioEncontrado = u;
                break;
            }
        }
        
        if(encontrouUser){
            if(usuario.getSenha().equals(usuarioEncontrado.getSenha())){
                permissao = true;
                this.usuario = usuarioEncontrado;
                conectadosBean.limpaProjetosPorUsuario(usuario.getCodigo());
            }
        }
        
        if(permissao){
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Login realizado com sucesso", "Bem vindo "+usuario.getNome());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            atualizaProjetos();
            return "centralUsuario.xhtml";
        }
        else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro ao realizar login", "Username ou senha incorretos");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "login.xhtml";
        }
    }
    
    public String logout(ExecucaoBean execucaoBean, ConectadosBean conectadosBean){
        conectadosBean.removeRegistro(usuario, projeto);
        conectadosBean.limpaProjetosPorUsuario(usuario.getCodigo());
        execucaoBean.setRetorno_log("");
        execucaoBean.setTabindex(0);
        execucaoBean.setValoresEntrada(new ArrayList<VariavelEntrada>());
        execucaoBean.setValoresMotores(new ArrayList<MotorInferencia>());
        execucaoBean.setValoresSaida(new ArrayList<VariavelSaida>());
        this.projeto = new Projeto();
        this.usuario = new Usuario();
        return "../index.xhtml";
    }
    
    public String atualizaProjetos(){
        projetosModel = new ListDataModel<Projeto>(this.usuario.getProjetos());
        return "centralUsuario.xhtml";
    }    
    
    public String login_voltar(){
        this.usuario = new Usuario();
        return "../index.xhtml";
    }
    
    public String novo_login(){
        if(this.usuario.getCodigo() != 0)
            if(this.projeto.getCodigo() != 0)
                return "geral/projeto.xhtml";
            else
                return "geral/centralUsuario.xhtml";
                
        this.usuario = new Usuario();
        return "geral/login.xhtml";
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UsuarioFacade getUsuariofacade() {
        return usuariofacade;
    }

    public void setUsuariofacade(UsuarioFacade usuariofacade) {
        this.usuariofacade = usuariofacade;
    }

    public DataModel<Projeto> getProjetosModel() {
        projetosModel = new ListDataModel<Projeto>(projetofacade.selecionaPorUsuario(this.usuario.getCodigo()));
        return projetosModel;
    }

    public void setProjetosModel(DataModel<Projeto> projetosModel) {
        this.projetosModel = projetosModel;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public ProjetoFacade getProjetofacade() {
        return projetofacade;
    }

    public void setProjetofacade(ProjetoFacade projetofacade) {
        this.projetofacade = projetofacade;
    }

    public VariavelEntradaFacade getEntradafacade() {
        return entradafacade;
    }

    public void setEntradafacade(VariavelEntradaFacade entradafacade) {
        this.entradafacade = entradafacade;
    }

    public String getDescricaoEntradasExecucao() {
        List<VariavelEntrada> entradas = entradafacade.selecionaTudo(this.projeto.getCodigo());
        String result = "";
        int contador = 1;
        for(VariavelEntrada e : entradas){
            if(contador == 1)
                result += e.getNome().toUpperCase();
            else
                result += ";"+e.getNome().toUpperCase();
            
            contador++;
        }
        this.descricaoEntradasExecucao = result;
        return descricaoEntradasExecucao;
    }

    public void setDescricaoEntradasExecucao(String descricaoEntradasExecucao) {
        this.descricaoEntradasExecucao = descricaoEntradasExecucao;
    }
    
}
