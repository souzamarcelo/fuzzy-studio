/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.sistema;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author marcelo
 */

 
public class SessionListener implements HttpSessionListener {
    private int sessionCount = 0;
 
    public void sessionCreated(HttpSessionEvent event) {
        synchronized (this) {
            sessionCount++;
        }
 
        System.out.println("Sessão criada: " + event.getSession().getId());
        System.out.println("Sessões totais: " + sessionCount);
    }
 
    public void sessionDestroyed(HttpSessionEvent event) {
        synchronized (this) {
            sessionCount--;
        }
        System.out.println("Sessão destruída: " + event.getSession().getId());
        System.out.println("Total de sessões: " + sessionCount);
    }
}
