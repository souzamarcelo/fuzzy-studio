/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.controle;

import br.udesc.facade.UsuarioFacade;
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
public class UsuarioBean {
    
    @EJB
    private UsuarioFacade usuariofacade;
    private Usuario usuario = new Usuario();
    private Usuario usuarioselecionado;
    private List<Usuario> us = new ArrayList<Usuario>();
    private DataModel<Usuario> usuariosModel;
    
    
    public String salvar(){
        int retorno = usuariofacade.salvar(usuario);
        
        if(retorno == 1){
            usuario = new Usuario();        
            FacesMessage msg = null;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro salvo com sucesso!", "Usuário");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "login.xhtml";

        } else if (retorno == 2){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro no usuário", "Este username já existe");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "editaUsuario.xhtml";
        }
        this.us = atualizaLista();
        this.usuariosModel = new ListDataModel<Usuario>(this.us);
        return null;
            

    }
    
    public String alteraDados(){
        return "editaUsuario.xhtml";
    }
    
    public String cadastrar(){
        this.usuario = new Usuario();
        return "editaUsuario.xhtml";
    }
    
    public String editar(){
        this.usuario = this.usuariosModel.getRowData();
        return "editaUsuario.xhtml";
    }
    
    public String nova(){
        this.us = atualizaLista();
        this.usuariosModel = new ListDataModel<Usuario>(this.us);
        this.usuario = new Usuario();
        return "editaUsuario.xhtml";
    }
    
    public String cancelar(){
        this.usuario = new Usuario();
        return "../index.xhtml";
    }
    
    public String excluir(){
        this.usuario = usuariosModel.getRowData();
        usuariofacade.excluir(usuario);
        this.us = atualizaLista();
        this.usuariosModel = new ListDataModel<Usuario>(this.us);
        usuario = new Usuario();
        return "editaUsuario.xhtml";
    }
    
    public List<Usuario> atualizaLista(){
        return this.usuariofacade.selecionaTudo();
    }

    public List<Usuario> getUs() {
        return us;
    }

    public void setUs(List<Usuario> us) {
        this.us = us;
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

    public DataModel<Usuario> getUsuariosModel() {
        return usuariosModel;
    }

    public void setUsuariosModel(DataModel<Usuario> usuariosModel) {
        this.usuariosModel = usuariosModel;
    }

    public Usuario getUsuarioselecionado() {
        return usuarioselecionado;
    }

    public void setUsuarioselecionado(Usuario usuarioselecionado) {
        this.usuarioselecionado = usuarioselecionado;
    }
}
