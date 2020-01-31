/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.sistema;

import br.udesc.modelo.Projeto;
import br.udesc.modelo.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author marcelo
 */
public class ListaConectados {
    
    private static ListaConectados unicaInstancia;
    List<UsuarioProjeto> conectadosControle = new ArrayList<UsuarioProjeto>();
    Date data = new Date();
    
    
    public ArrayList<int[]> retornaCodigos(){
        
        detectaRemoveDesconectados();
        
        ArrayList<int[]> codigos = new ArrayList<int[]>();
        
        for(UsuarioProjeto up : conectadosControle){
            int[] registro = new int[2];
            registro[0] = up.getUsuario().getCodigo();
            registro[1] = up.getProjeto().getCodigo();
            codigos.add(registro);
        }
        
        return codigos;
    }
    
    
    public void detectaRemoveDesconectados(){        
        Date agora = new Date();
        List<UsuarioProjeto> novalista = new ArrayList<UsuarioProjeto>();
        
        for(UsuarioProjeto up : conectadosControle){
            int tempoSemRequest = dateDiffSeconds(up.getMomentoRequest(), agora);
            if(tempoSemRequest < 10){
                novalista.add(up);
            }
            this.conectadosControle = novalista;
        }
        
        /*for(UsuarioProjeto xx : conectadosControle){
            System.out.println(xx.getUsuario().getNome() + " --> "+xx.getProjeto().getNome());
        }*/
    }
    
    public int dateDiffSeconds(Date firstDate, Date lastDate ) {  
         return Math.round( (lastDate.getTime() - firstDate.getTime()) / (float) (1000) );  
    } 
    
    
    public void adicionaLista(Usuario u, Projeto p, Date d){
        UsuarioProjeto up = new UsuarioProjeto(u, p, d);
        List<UsuarioProjeto> novalista = new ArrayList<UsuarioProjeto>();
        for(UsuarioProjeto userproject : conectadosControle){
            if(up.getUsuario().getCodigo() != userproject.getUsuario().getCodigo()){
                novalista.add(userproject);
            }
        }
        
        novalista.add(up);
        conectadosControle = novalista;
        detectaRemoveDesconectados();
    }
    
    
    public static ListaConectados getInstance(){
        if(unicaInstancia == null)
            unicaInstancia = new ListaConectados();
        
        return unicaInstancia;
    }
    
    private ListaConectados(){}
    
    
    
    
    private class UsuarioProjeto {
        private Usuario usuario;
        private Projeto projeto;
        private Date momentoRequest;
        

        public UsuarioProjeto(Usuario u, Projeto p, Date m){
            this.usuario = u;
            this.projeto = p;
            this.momentoRequest = m;
        }
        
        public Projeto getProjeto() {
            return projeto;
        }

        public void setProjeto(Projeto projeto) {
            this.projeto = projeto;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public Date getMomentoRequest() {
            return momentoRequest;
        }

        public void setMomentoRequest(Date momentoRequest) {
            this.momentoRequest = momentoRequest;
        }
    }
    
}
