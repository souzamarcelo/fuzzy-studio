/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.facade;

import br.udesc.modelo.VariavelEntrada;
import br.udesc.modelo.VariavelSaida;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.*;

/**
 *
 * @author marcelo
 */

@Stateless
public class VariavelSaidaFacade {
    @PersistenceContext
    private EntityManager em;
    
    public int salvar(VariavelSaida ve) {       
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
    
    public void excluir(VariavelSaida ve){
        em.remove(em.merge(ve));
    }
    
    public List<VariavelSaida> selecionaTudo(int codProjeto){
        Query sql = em.createQuery("select v from VariavelSaida v join v.projeto p where p.codigo = "+codProjeto);
        List<VariavelSaida> listave = sql.getResultList();
        return listave;
    }
    
    public VariavelSaida selecionaPorCodigo(int codigo){
        VariavelSaida ve = em.find(VariavelSaida.class, codigo);
        return ve;
    }
    
    public VariavelSaida selecionaPorNome(String nome, int codprojeto){
        Query sql = em.createQuery("select v from VariavelSaida as v join v.projeto p where v.nome like '"+nome+"' and p.codigo = "+codprojeto);
        List<VariavelSaida> listave = sql.getResultList();               
        VariavelSaida ve = new VariavelSaida();
        for(VariavelSaida saida : listave){
            ve = saida;
            break;
        }
        return ve;
    }
}
