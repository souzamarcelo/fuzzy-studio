/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author marcelo
 */

@Entity
@Table(name="variaveisEntrada")
public class VariavelEntrada implements Serializable{
    
    @Id
    @GeneratedValue
    @Column
    private int codigo;
    
    @Column
    private String nome;
    
    @Column
    private Double minimo;
    
    @Column
    private Double maximo;
    
    @Column
    private String unidade;
    
    @ManyToOne
    @JoinColumn(name="id_projeto", nullable=false)
    private Projeto projeto;
    
    @OneToMany(mappedBy="variavelEntrada")
    private List<TermoLinguisticoEntrada> termosLinguisticos = new ArrayList<TermoLinguisticoEntrada>();
    
    @Column
    private int posx;
    
    @Column
    private int posy;
    
    @Transient
    private Double valor;
    
    @Transient
    private String termo;
    
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getMaximo() {
        return maximo;
    }

    public void setMaximo(Double maximo) {
        this.maximo = maximo;
    }

    public Double getMinimo() {
        return minimo;
    }

    public void setMinimo(Double minimo) {
        this.minimo = minimo;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public List<TermoLinguisticoEntrada> getTermosLinguisticos() {
        return termosLinguisticos;
    }

    public void setTermosLinguisticos(List<TermoLinguisticoEntrada> termosLinguisticos) {
        this.termosLinguisticos = termosLinguisticos;
    }

    public int getPosx() {
        return posx;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }

    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }
}
