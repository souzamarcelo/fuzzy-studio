/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.modelo;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author marcelo
 */

@Entity
@Table(name="termosLinguisticosSaida")
public class TermoLinguisticoSaida implements Serializable {
    
    @Id
    @GeneratedValue
    @Column
    private int codigo;
    
    @Column
    private String nome;
    
    @Column
    private String funcao;
    
    @Column
    private Double valora;
    
    @Column
    private Double valorb;
    
    @Column
    private Double valorc;
    
    @Column
    private Double valord;
    
    @ManyToOne
    @JoinColumn(name="idVariavelSaida", nullable=true)
    private VariavelSaida variavelSaida;
    
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValora() {
        return valora;
    }

    public void setValora(Double valora) {
        this.valora = valora;
    }

    public Double getValorb() {
        return valorb;
    }

    public void setValorb(Double valorb) {
        this.valorb = valorb;
    }

    public Double getValorc() {
        return valorc;
    }

    public void setValorc(Double valorc) {
        this.valorc = valorc;
    }

    public Double getValord() {
        return valord;
    }

    public void setValord(Double valord) {
        this.valord = valord;
    }

    public VariavelSaida getVariavelSaida() {
        return variavelSaida;
    }

    public void setVariavelSaida(VariavelSaida variavelSaida) {
        this.variavelSaida = variavelSaida;
    }
}
