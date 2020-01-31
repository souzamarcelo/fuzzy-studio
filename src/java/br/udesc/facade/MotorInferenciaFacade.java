/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.facade;

import br.udesc.modelo.MotorInferencia;
import br.udesc.modelo.VariavelEntrada;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.*;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author marcelo
 */

@Stateless
public class MotorInferenciaFacade {
    
    @PersistenceContext
    private EntityManager em;
    
    public int salvar(MotorInferencia m) {       
        try{
            em.merge(m);
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
    
    public void excluir(MotorInferencia m){
        em.remove(em.merge(m));
    }
    
    public List<MotorInferencia> selecionaTudo(int codProjeto){
        Query sql = em.createQuery("select m from MotorInferencia m join m.projeto p where p.codigo = "+codProjeto);
        List<MotorInferencia> listave = sql.getResultList();
        return listave;
    }
    
    public MotorInferencia selecionaPorCodigo(int codigo){
        MotorInferencia m = em.find(MotorInferencia.class, codigo);
        return m;
    }
}
