/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.controle;

import br.udesc.facade.ProjetoFacade;
import br.udesc.facade.UsuarioFacade;
import br.udesc.modelo.Projeto;
import br.udesc.modelo.Usuario;
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
public class ProjetoBean {
  
    @EJB
    private ProjetoFacade projetofacade;
    @EJB
    private UsuarioFacade usuariofacade;
            
    private Projeto projeto = new Projeto();
    private Usuario criador = new Usuario();
    
    private List<Usuario> usuariosDoBanco = new ArrayList<Usuario>();
    private List<String> usuariosSelecionados = new ArrayList<String>();
    
    public String salvaCompartilhamento(Projeto projetoAberto){
        List<Usuario> usuariosDoProjeto = new ArrayList<Usuario>();
        for(String str_cod : usuariosSelecionados){
            Usuario u = usuariofacade.selecionaPorCodigo(Integer.parseInt(str_cod));
            usuariosDoProjeto.add(u);
        }
        usuariosDoProjeto.add(this.criador);
        this.projeto.setUsuarios(usuariosDoProjeto);
        
        int retorno = projetofacade.salvar(projeto);
        
        if(retorno == 1){
            projeto = new Projeto();
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso!", "Projeto");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (retorno == 2){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro no projeto", "Este username já existe");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
        if(retorno == 1){
            if(projetoAberto.getCodigo() == 0)
                return "centralUsuario.xhtml";
            else
                return "projeto.xhtml";
        }
        else
            return "compartilhaProjeto.xhtml";
        
    }
    
    public String chamaCompartilhamento(){
        return "compartilhaProjeto.xhtml";
    }
    
    public String cancelarCompartilhamento(Projeto projetoAberto){
        if(projetoAberto.getCodigo() == 0)
            return "centralUsuario.xhtml";
        else
            return "projeto.xhtml";
    }
    
    public String excluir(int codusuario){
        List<Usuario> users = usuariofacade.selecionaTudoPorProjeto(projeto.getCodigo());
        projetofacade.removeUsuario(projeto, codusuario, users);
        projeto = new Projeto();
        return "centralUsuario.xhtml";
    }
    
    public String salvar(){
        
        System.out.println("CHAMADO SALVAR PROJETO");
        List<Usuario> users = new ArrayList<Usuario>();
        users.add(criador);
        this.projeto.setUsuarios(users);
        
        int retorno = projetofacade.salvar(projeto);
        
        if(retorno == 1){
            projeto = new Projeto();
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso!", "Projeto");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } else if (retorno == 2){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro no projeto", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
        if(retorno == 1){
            
            return "centralUsuario.xhtml";
        }
        else
            return "editaProjeto.xhtml";
    }
    
    public String cancelar(){
        return "centralUsuario.xhtml";
    }
    
    public String novo(){
        return "editaProjeto.xhtml";
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

    public Usuario getCriador() {
        return criador;
    }

    public void setCriador(Usuario criador) {
        this.criador = criador;
    }

    public UsuarioFacade getUsuariofacade() {
        return usuariofacade;
    }

    public void setUsuariofacade(UsuarioFacade usuariofacade) {
        this.usuariofacade = usuariofacade;
    }

    // Retorna os usuários do banco, exceto o usuário da sessão
    public List<Usuario> getUsuariosDoBanco() {
        this.usuariosDoBanco = new ArrayList<Usuario>();
        List<Usuario> usersBanco = usuariofacade.selecionaTudo();
        for(Usuario u : usersBanco){
            if(u.getCodigo() != this.criador.getCodigo()){
                usuariosDoBanco.add(u);
            }
        }
        return usuariosDoBanco;
    }

    public void setUsuariosDoBanco(List<Usuario> usuariosDoBanco) {
        this.usuariosDoBanco = usuariosDoBanco;
    }

    public List<String> getUsuariosSelecionados() {
        return usuariosSelecionados;
    }

    public void setUsuariosSelecionados(List<String> usuariosSelecionados) {
        this.usuariosSelecionados = usuariosSelecionados;
    }
}
