/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.facade;

import br.udesc.modelo.VariavelEntrada;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.*;

/**
 *
 * @author marcelo
 */

@Stateless
public class VariavelEntradaFacade {
    @PersistenceContext
    private EntityManager em;
    
    public int salvar(VariavelEntrada ve) {       
        try{
            em.merge(ve);
            
            return 1;
        } catch (Exception e){
            String msgclass = e.getClass().toString();
            if(msgclass.contains("EntityExistsException")){
                return 2;
            }
        }
        return 3;
    }
    
    public void excluir(VariavelEntrada ve){
        em.remove(em.merge(ve));
    }
    
    public List<VariavelEntrada> selecionaTudo(int codProjeto){
        Query sql = em.createQuery("select v from VariavelEntrada v join v.projeto p where p.codigo = "+codProjeto);
        List<VariavelEntrada> listave = sql.getResultList();
        return listave;
    }
    
    public VariavelEntrada selecionaPorCodigo(int codigo){
        VariavelEntrada ve = em.find(VariavelEntrada.class, codigo);
        return ve;
    }
    
    public VariavelEntrada selecionaPorNome(String nome, int codprojeto){
        Query sql = em.createQuery("select v from VariavelEntrada as v join v.projeto p where v.nome like '"+nome+"' and p.codigo = "+codprojeto);
        List<VariavelEntrada> listave = sql.getResultList();               
        VariavelEntrada ve = new VariavelEntrada();
        for(VariavelEntrada entrada : listave){
            ve = entrada;
            break;
        }
        return ve;
    }
}
