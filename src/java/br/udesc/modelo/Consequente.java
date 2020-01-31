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
@Table(name="consequentes")
public class Consequente {
    
    @Id
    @GeneratedValue
    @Column
    private int codigo;
    
    @ManyToOne
    @JoinColumn(name="id_variavelSaida", nullable=false)
    private VariavelSaida variavelSaida;
    
    @ManyToOne
    @JoinColumn(name="id_termoSaida", nullable=false)
    private TermoLinguisticoSaida termo;
    
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

    public TermoLinguisticoSaida getTermo() {
        return termo;
    }

    public void setTermo(TermoLinguisticoSaida termo) {
        this.termo = termo;
    }

    public VariavelSaida getVariavelSaida() {
        return variavelSaida;
    }

    public void setVariavelSaida(VariavelSaida variavelSaida) {
        this.variavelSaida = variavelSaida;
    }
}
