/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.facade;

import br.udesc.modelo.Projeto;
import br.udesc.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.*;

/**
 *
 * @author marcelo
 */

@Stateless
public class ProjetoFacade {
    
    @PersistenceContext
    private EntityManager em;
    
    public int salvar(Projeto p) {       
        try{
            em.merge(p);
            return 1;
        } catch (Exception e){
            String msgclass = e.getClass().toString();
            if(msgclass.contains("EntityExistsException")){
                return 2;
            }
        }
        return 3;
    }
    
    public void excluir(Projeto p){
        em.remove(em.merge(p));
    }
    
    public List<Projeto> selecionaTudo(){
        Query sql = em.createQuery("select p from Projeto p");
        List<Projeto> listap = sql.getResultList();
        return listap;
    }
    
    public Projeto selecionaPorCodigo(int codigo){
        Projeto p = em.find(Projeto.class, codigo);
        return p;
    }
    
    public Projeto selecionaPorNome(String nome){
        Query sql = em.createQuery("select p from Projeto as p where p.nome like '"+nome+"'");
        List<Projeto> listap = sql.getResultList();               
        Projeto p = new Projeto();
        for(Projeto projeto : listap){
            p = projeto;
            break;
        }
        return p;
    }
    
    public List<Projeto> selecionaPorUsuario(int coduser){
        
        Query sql = em.createQuery("select p from Projeto p join p.usuarios u where u.codigo = "+coduser);
        List<Projeto> listap = sql.getResultList();
        return listap;
    }
    
    public void removeUsuario(Projeto p, int codusuario, List<Usuario> users){
        List<Usuario> usuarios = new ArrayList<Usuario>();
        for(Usuario u : users){
            if(u.getCodigo() != codusuario)
                usuarios.add(u);
        }
        
        p.setUsuarios(usuarios);
        em.merge(p);
    }
}
