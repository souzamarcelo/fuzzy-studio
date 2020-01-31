/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.modelo;

import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author marcelo
 */

@Entity
@Table(name="regras")
public class Regra implements Serializable {
    
    @Id
    @GeneratedValue
    @Column
    private int codigo;
    
    @ManyToOne
    @JoinColumn(name="id_projeto", nullable=false)
    private Projeto projeto;
    
    @Column(name="conector")
    private String conector;
    
    @Column(name="descricaoAntecedente")
    private String descricaoAntecedente;
    
    @Column(name="descricaoConsequente")
    private String descricaoConsequente;
    
    @OneToMany(mappedBy="baseRegras", cascade=CascadeType.ALL)
    private List<Antecedente> antecedentes = new ArrayList<Antecedente>();

    @OneToMany(mappedBy="baseRegras", cascade=CascadeType.ALL)
    private List<Consequente> consequentes = new ArrayList<Consequente>();
    
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getConector() {
        return conector;
    }

    public void setConector(String conector) {
        this.conector = conector;
    }

    public String getDescricaoAntecedente() {
        return descricaoAntecedente;
    }

    public void setDescricaoAntecedente(String descricaoAntecedente) {
        this.descricaoAntecedente = descricaoAntecedente;
    }

    public String getDescricaoConsequente() {
        return descricaoConsequente;
    }

    public void setDescricaoConsequente(String descricaoConsequente) {
        this.descricaoConsequente = descricaoConsequente;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public List<Antecedente> getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(List<Antecedente> antecedentes) {
        this.antecedentes = antecedentes;
    }

    public List<Consequente> getConsequentes() {
        return consequentes;
    }

    public void setConsequentes(List<Consequente> consequentes) {
        this.consequentes = consequentes;
    }
}
