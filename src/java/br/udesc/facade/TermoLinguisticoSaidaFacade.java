/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.facade;

import br.udesc.modelo.TermoLinguisticoSaida;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.*;

/**
 *
 * @author marcelo
 */

@Stateless
public class TermoLinguisticoSaidaFacade {
    
    @PersistenceContext
    private EntityManager em;
    
    public int salvar(TermoLinguisticoSaida tls) {       
        try{
            em.merge(tls);
            return 1;
        } catch (Exception e){
            String msgclass = e.getClass().toString();
            if(msgclass.contains("EntityExistsException")){
                return 2;
            }
        }
        return 3;
    }
    
    public void excluir(TermoLinguisticoSaida tls){
        em.remove(em.find(TermoLinguisticoSaida.class, tls.getCodigo()));
    }
    
    public List<TermoLinguisticoSaida> selecionaTudo(){
        Query sql = em.createQuery("select v from TermoLinguisticoSaida v");
        List<TermoLinguisticoSaida> listatls = sql.getResultList();
        return listatls;
    }
    
    public List<TermoLinguisticoSaida> selecionaTudoPelaVariavel(int codvar){
        Query sql = em.createQuery("select t from TermoLinguisticoSaida t where t.variavelSaida = "+codvar);
        List<TermoLinguisticoSaida> listatls = sql.getResultList();
        return listatls;
    }
    
    public TermoLinguisticoSaida selecionaPeloCodigo(int codtermo){
        TermoLinguisticoSaida termo = em.find(TermoLinguisticoSaida.class, codtermo);
        return termo;
    }
}
