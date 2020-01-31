/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.modelo;

import javax.persistence.*;

/**
 *
 * @author marcelo
 */
@Entity
@Table(name="antecedentes")
public class Antecedente {
    
    @Id
    @GeneratedValue
    @Column
    private int codigo;
    
    @ManyToOne
    @JoinColumn(name="id_variavelEntrada", nullable=false)
    private VariavelEntrada variavelEntrada;
    
    @ManyToOne
    @JoinColumn(name="id_termoEntrada", nullable=false)
    private TermoLinguisticoEntrada termo;
    
    @ManyToOne (cascade=CascadeType.ALL)
    @JoinColumn(name="id_baseRegras", nullable=false)
    private Regra baseRegras;

    public Regra getBaseRegras() {
        return baseRegras;
    }

    public void setBaseRegras(Regra baseRegras) {
        this.baseRegras = baseRegras;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public TermoLinguisticoEntrada getTermo() {
        return termo;
    }

    public void setTermo(TermoLinguisticoEntrada termo) {
        this.termo = termo;
    }

    public VariavelEntrada getVariavelEntrada() {
        return variavelEntrada;
    }

    public void setVariavelEntrada(VariavelEntrada variavelEntrada) {
        this.variavelEntrada = variavelEntrada;
    }
}
