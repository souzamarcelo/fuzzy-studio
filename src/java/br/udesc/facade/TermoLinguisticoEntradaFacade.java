/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.facade;

import br.udesc.modelo.TermoLinguisticoEntrada;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.*;

/**
 *
 * @author marcelo
 */

@Stateless
public class TermoLinguisticoEntradaFacade {
    
    @PersistenceContext
    private EntityManager em;
    
    public int salvar(TermoLinguisticoEntrada tle) {       
        try{
            em.merge(tle);
            return 1;
        } catch (Exception e){
            String msgclass = e.getClass().toString();
            if(msgclass.contains("EntityExistsException")){
                return 2;
            }
        }
        return 3;
    }
    
    public void excluir(TermoLinguisticoEntrada tle){
        em.remove(em.find(TermoLinguisticoEntrada.class, tle.getCodigo()));
    }
    
    public List<TermoLinguisticoEntrada> selecionaTudo(){
        Query sql = em.createQuery("select v from TermoLinguisticoEntrada v");
        List<TermoLinguisticoEntrada> listatle = sql.getResultList();
        return listatle;
    }
    
    public List<TermoLinguisticoEntrada> selecionaTudoPelaVariavel(int codvar){
        Query sql = em.createQuery("select t from TermoLinguisticoEntrada t where t.variavelEntrada = "+codvar);
        List<TermoLinguisticoEntrada> listatle = sql.getResultList();
        return listatle;
    }
    
    public TermoLinguisticoEntrada selecionaPeloCodigo(int codtermo){
        TermoLinguisticoEntrada termo = em.find(TermoLinguisticoEntrada.class, codtermo);
        return termo;
    }
    
    public TermoLinguisticoEntrada selecionaPeloNome(String nometermo){
        Query sql = em.createQuery("select t from TermoLinguisticoEntrada t where t.nome like '"+nometermo+"'");
        List<TermoLinguisticoEntrada> listatle = sql.getResultList();
        TermoLinguisticoEntrada termo = listatle.get(0);
        return termo;
    }
}
