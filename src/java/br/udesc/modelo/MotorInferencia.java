/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.modelo;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author marcelo
 */

@Entity
@Table(name="motores")
public class MotorInferencia {
    
    
    @Id
    @GeneratedValue
    @Column
    private int codigo;
    
    @Column
    private String nome;
    
    @ManyToOne
    @JoinColumn(name="id_projeto", nullable=false)
    private Projeto projeto;
    
    @ManyToMany
    @JoinTable(name="motorregra",
            joinColumns={@JoinColumn(name="id_motor")},
            inverseJoinColumns={@JoinColumn(name="id_regra")}
        )
    private List<Regra> regras = new ArrayList<Regra>();
    
    @Column
    private int posx;
    
    @Column
    private int posy;
    
    @Column(name="metodo_defuzzificacao")
    private String metodoDefuzzificacao;
    
    @Column(name="metodo_agregacao_regras")
    private String metodoAgregacaoRegras;
    
    @Column(name="conexao_and")
    private String conexaoAnd;
    
    @Column(name="metodo_ativacao_regras")
    private String metodoAtivacaoRegras;
    
    @Transient
    private String resultado;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public List<Regra> getRegras() {
        return regras;
    }

    public void setRegras(List<Regra> regras) {
        this.regras = regras;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public String getConexao_and() {
        return conexaoAnd;
    }

    public void setConexao_and(String conexao_and) {
        this.conexaoAnd = conexao_and;
    }

    public String getMetodo_agregacao_regras() {
        return metodoAgregacaoRegras;
    }

    public void setMetodo_agregacao_regras(String metodo_agregacao_regras) {
        this.metodoAgregacaoRegras = metodo_agregacao_regras;
    }

    public String getMetodo_ativacao_regras() {
        return metodoAtivacaoRegras;
    }

    public void setMetodo_ativacao_regras(String metodo_ativacao_regras) {
        this.metodoAtivacaoRegras = metodo_ativacao_regras;
    }

    public String getMetodo_defuzzificacao() {
        return metodoDefuzzificacao;
    }

    public void setMetodo_defuzzificacao(String metodo_defuzzificacao) {
        this.metodoDefuzzificacao = metodo_defuzzificacao;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
