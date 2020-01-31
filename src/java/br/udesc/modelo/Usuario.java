/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author marcelo
 */

@Entity
@Table(name="usuarios")
public class Usuario implements Serializable{
    
    @Id
    @GeneratedValue
    @Column
    private int codigo;
    
    @Column
    private String nome;
    
    @Column
    private String user;
    
    @Column
    private String senha;
    
    @ManyToMany(mappedBy="usuarios", fetch=FetchType.EAGER)
    private List<Projeto> projetos;

    @Column
    private String skype;
    
    @Column
    private String gtalk;
    
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
    }

    public String getGtalk() {
        return gtalk;
    }

    public void setGtalk(String gtalk) {
        this.gtalk = gtalk;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }
}
