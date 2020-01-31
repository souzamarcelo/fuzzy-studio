/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.controle;

import br.udesc.facade.ProjetoFacade;
import br.udesc.facade.UsuarioFacade;
import br.udesc.modelo.Projeto;
import br.udesc.modelo.Usuario;
import br.udesc.sistema.ListaConectados;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author marcelo
 */

@ManagedBean
@ApplicationScoped
public class ConectadosBean {
    
    private List<UsuarioProjeto> conectados = new ArrayList<UsuarioProjeto>();
    private List<Usuario> usuarios = new ArrayList<Usuario>();
    
    private ListaConectados lista = ListaConectados.getInstance();
    @EJB
    private UsuarioFacade usuariofacade;
    @EJB
    private ProjetoFacade projetofacade;
    
    
    public void atualizaConformeLista(){
        ArrayList<int[]> codigos = lista.retornaCodigos();
        List<UsuarioProjeto> listanova = new ArrayList<UsuarioProjeto>();
        
        //Laço para remover desconectados
        for(UsuarioProjeto up: conectados){
            for(int i = 0; i < codigos.size(); i++){
                int[] registro = codigos.get(i);
                if(registro[0] == up.getUsuario().getCodigo()){
                    if(registro[1] == up.getProjeto().getCodigo()){
                        listanova.add(up);
                    }
                }
            }
        }
        
        for(int i = 0; i < codigos.size(); i++){
            int[] registro = codigos.get(i);
            boolean encontrou = false;
            for(UsuarioProjeto up : conectados){
                if(registro[0] == up.getUsuario().getCodigo()){
                    if(registro[1] == up.getProjeto().getCodigo()){
                        encontrou = true;
                    }
                }
            }
            
            if(!encontrou){
                Usuario u = usuariofacade.selecionaPorCodigo(registro[0]);
                Projeto p = projetofacade.selecionaPorCodigo(registro[1]);
                UsuarioProjeto userproject = new UsuarioProjeto(u, p);
                listanova.add(userproject);
            }
        }
        
        
        conectados = listanova;
        
    }
    
    public void limpaProjetosPorUsuario(int codUsuario){
        List<UsuarioProjeto> remover = new ArrayList<UsuarioProjeto>();
        
        for(UsuarioProjeto up : conectados){
            if(up.getUsuario().getCodigo() == codUsuario){
                remover.add(up);
            }
        }
        
        for(UsuarioProjeto up2 : remover){
            conectados.remove(up2);
        }
        
    }
    
    public List<Usuario> retornaUsuarios(int codPro){
        atualizaConformeLista();
        List<Usuario> users = new ArrayList<Usuario>();
        for(UsuarioProjeto up : conectados){
            if(up.getProjeto().getCodigo() == codPro)
                users.add(up.getUsuario());
        }
        return users;
    }
    
    public List<Usuario> retornaUsuarios(){
        atualizaConformeLista();
        FacesContext context   = FacesContext.getCurrentInstance();  
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();  
        int codProjetoConsulta = Integer.parseInt(req.getParameter("codProjetoConsulta"));
        
        List<Usuario> users = new ArrayList<Usuario>();
        for(UsuarioProjeto up : conectados){
            if(up.getProjeto().getCodigo() == codProjetoConsulta)
                users.add(up.getUsuario());
        }
        return users;
    }
    
    public void removeRegistro(Usuario u, Projeto p){
        for(UsuarioProjeto up : conectados){
            if(up.getUsuario().getCodigo() == u.getCodigo()){
                if(up.getProjeto().getCodigo() == p.getCodigo()){
                    conectados.remove(up);
                    break;
                }
            }
        }
    }
    
    public void adicionaRegistro(Usuario u, Projeto p){
        boolean adiciona = true;
        for(UsuarioProjeto up : conectados){
            if(up.getUsuario().getCodigo() == u.getCodigo()){
                if(up.getProjeto().getCodigo() == p.getCodigo()){
                    adiciona = false;
                }
            }
        }
        if(adiciona)
            conectados.add(new UsuarioProjeto(u, p));
    }
    
    public int getUsuariosDoProjeto(int codProjeto){
        int quantidade = 0;
        for(UsuarioProjeto up : conectados){
            if(up.getProjeto().getCodigo() == codProjeto){
                quantidade++;
            }
        }
        return quantidade;
    }

    public List<UsuarioProjeto> getConectados() {
        return conectados;
    }

    public void setConectados(List<UsuarioProjeto> conectados) {
        this.conectados = conectados;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
    private class UsuarioProjeto {
        private Usuario usuario;
        private Projeto projeto;

        public UsuarioProjeto(Usuario u, Projeto p){
            this.usuario = u;
            this.projeto = p;
        }
        
        public Projeto getProjeto() {
            return projeto;
        }

        public void setProjeto(Projeto projeto) {
            this.projeto = projeto;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }
    }
}
