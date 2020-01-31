/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.facade;

import br.udesc.modelo.Antecedente;
import br.udesc.modelo.Regra;
import br.udesc.modelo.Consequente;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

/**
 *
 * @author marcelo
 */

@Stateless
public class RegraFacade {
    
    @PersistenceContext(type= PersistenceContextType.TRANSACTION)
    private EntityManager em;
    
    public int salvar(Regra br) {       
        try{
            em.flush();
            em.merge(br);
            return 1;
        } catch (Exception e){
            String msgclass = e.getClass().toString();
            if(msgclass.contains("EntityExistsException")){
                return 2;
            }
            System.out.println(e.getMessage());
        }
        return 3;
    }
    
    public void excluir(Regra br){
        em.remove(em.find(Regra.class, br.getCodigo()));
    }
    
    public List<Regra> selecionaTudo(int codProjeto){
        Query sql = em.createQuery("select b from Regra b join b.projeto p where p.codigo = "+codProjeto);
        List<Regra> listabr = sql.getResultList();
        return listabr;
    }
    
    public List<Regra> selecionaPeloMotor(int codMotor){
        Query sql = em.createQuery("select b from MotorInferencia m join m.regras b where m.codigo = "+codMotor);
        List<Regra> listabr = sql.getResultList();
        return listabr;
    }
    
    public Regra selecionaPeloCodigo(int codbase){
        Regra base = em.find(Regra.class, codbase);
        return base;
    }
    
    public List<Antecedente> selecionaAntecedentes(int codRegra){
        Query sql = em.createQuery("select a from Antecedente a join a.baseRegras b where b.codigo = "+codRegra);
        List<Antecedente> listaa = sql.getResultList();
        return listaa;
    }
    
    public List<Consequente> selecionaConsequentes(int codRegra){
        Query sql = em.createQuery("select c from Consequente c join c.baseRegras b where b.codigo = "+codRegra);
        List<Consequente> listac = sql.getResultList();
        return listac;
    }
    
    public List<Regra> selecionaTudoPelaEntrada(int codEntrada){
        Query sql = em.createQuery("select b from Regra b join b.antecedentes a join a.variavelEntrada v where v.codigo = "+codEntrada);
        List<Regra> listabr = sql.getResultList();
        return listabr;
    }
    
    public List<Regra> selecionaTudoPelaSaida(int codEntrada){
        Query sql = em.createQuery("select b from Regra b join b.consequentes a join a.variavelSaida v where v.codigo = "+codEntrada);
        List<Regra> listabr = sql.getResultList();
        return listabr;
    }
}
