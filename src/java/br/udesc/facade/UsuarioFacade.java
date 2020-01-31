/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.facade;

import br.udesc.modelo.Usuario;
import br.udesc.modelo.VariavelEntrada;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.*;

/**
 *
 * @author marcelo
 */

@Stateless
public class UsuarioFacade {
    
    @PersistenceContext
    private EntityManager em;
    
    public int salvar(Usuario u) {       
        try{
            em.merge(u);
            return 1;
        } catch (Exception e){
            String msgclass = e.getClass().toString();
            if(msgclass.contains("EntityExistsException")){
                return 2;
            }
        }
        return 3;
    }
    
    public void excluir(Usuario u){
        em.remove(em.merge(u));
    }
    
    public List<Usuario> selecionaTudo(){
        Query sql = em.createQuery("select u from Usuario u");
        List<Usuario> listau = sql.getResultList();
        return listau;
    }
    
    public List<Usuario> selecionaTudoPorProjeto(int codprojeto){
        Query sql = em.createQuery("select u from Usuario u join u.projetos p where p = "+codprojeto);
        List<Usuario> listau = sql.getResultList();
        return listau;
    }
    
    public Usuario selecionaPorCodigo(int codigo){
        Usuario u = em.find(Usuario.class, codigo);
        return u;
    }
    
    public Usuario selecionaPorNome(String nome){
        Query sql = em.createQuery("select u from Usuario as u where u.nome like '"+nome+"'");
        List<Usuario> listau = sql.getResultList();               
        Usuario u = new Usuario();
        for(Usuario usuario : listau){
            u = usuario;
            break;
        }
        return u;
    }
}
