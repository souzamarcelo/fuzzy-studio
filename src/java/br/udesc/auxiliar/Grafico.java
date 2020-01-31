/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.auxiliar;

import br.udesc.modelo.MotorInferencia;
import java.awt.image.BufferedImage;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author marcelo
 */
public class Grafico {
    
    JFreeChart grafico;
    MotorInferencia motor;
    
    public BufferedImage getImagem(){
        return grafico.createBufferedImage(100, 100);
    }

    public JFreeChart getGrafico() {
        return grafico;
    }

    public void setGrafico(JFreeChart grafico) {
        this.grafico = grafico;
    }

    public MotorInferencia getMotor() {
        return motor;
    }

    public void setMotor(MotorInferencia motor) {
        this.motor = motor;
    }
    
}
