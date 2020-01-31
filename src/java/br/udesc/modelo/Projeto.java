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
@Table(name="projetos")
public class Projeto implements Serializable{
    
    @Id
    @GeneratedValue
    @Column
    private int codigo;
    
    @Column
    private String nome;
    
    @ManyToMany(cascade= CascadeType.REFRESH) 
    @JoinTable(name = "usuarioprojeto",
                joinColumns = {@JoinColumn(name="id_projeto")},
                inverseJoinColumns = {@JoinColumn(name="id_usuario")})
    private List<Usuario> usuarios;
    
    
    @OneToMany(mappedBy="projeto")
    private List<VariavelEntrada> variaveisEntrada = new ArrayList<VariavelEntrada>();
    
    @OneToMany(mappedBy="projeto")
    private List<VariavelSaida> variaveisSaida = new ArrayList<VariavelSaida>();
    
    @OneToMany(mappedBy="projeto")
    private List<Regra> regras = new ArrayList<Regra>();
    
    @OneToMany(mappedBy="projeto")
    private List<MotorInferencia> motores = new ArrayList<MotorInferencia>();

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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<VariavelEntrada> getVariaveisEntrada() {
        return variaveisEntrada;
    }

    public void setVariaveisEntrada(List<VariavelEntrada> variaveisEntrada) {
        this.variaveisEntrada = variaveisEntrada;
    }

    public List<VariavelSaida> getVariaveisSaida() {
        return variaveisSaida;
    }

    public void setVariaveisSaida(List<VariavelSaida> variaveisSaida) {
        this.variaveisSaida = variaveisSaida;
    }

    public List<Regra> getRegras() {
        return regras;
    }

    public void setRegras(List<Regra> regras) {
        this.regras = regras;
    }

    public List<MotorInferencia> getMotores() {
        return motores;
    }

    public void setMotores(List<MotorInferencia> motores) {
        this.motores = motores;
    }
}
