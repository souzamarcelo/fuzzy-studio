/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.controle;

import br.udesc.facade.TermoLinguisticoEntradaFacade;
import br.udesc.facade.VariavelEntradaFacade;
import br.udesc.modelo.TermoLinguisticoEntrada;
import br.udesc.modelo.VariavelEntrada;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author marcelo
 */

@ManagedBean
@SessionScoped
public class GraficosEntradaBean {
    private VariavelEntrada variavelentrada = new VariavelEntrada();
    private List<TermoLinguisticoEntrada> termosdavariavel = new ArrayList<TermoLinguisticoEntrada>();
    @EJB
    private VariavelEntradaFacade variavelfacade;
    @EJB
    private TermoLinguisticoEntradaFacade termofacade;
    
    HtmlInputText inputcodvar = new HtmlInputText();
    
    //Gráfico
    private CartesianChartModel linearModel;

    public GraficosEntradaBean() {
        createLinearModel();
    }
    
    private void createLinearModel() {
        linearModel = new CartesianChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Exemplo1");

        series1.set(1, 0);
        series1.set(2, 0);
        series1.set(3, 0);
        series1.set(4, 0);
        series1.set(5, 0);

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Exemplo2");

        series2.set(1, 1);
        series2.set(2, 1);
        series2.set(3, 1);
        series2.set(4, 1);
        series2.set(5, 1);

        linearModel.addSeries(series1);
        linearModel.addSeries(series2);
    }
    
    public List<TermoLinguisticoEntrada> getTermosdavariavel() {
        return termosdavariavel;
    }

    public void setTermosdavariavel(List<TermoLinguisticoEntrada> termosdavariavel) {
        this.termosdavariavel = termosdavariavel;
    }

    public VariavelEntrada getVariavelentrada() {
        return variavelentrada;
    }

    public void setVariavelentrada(VariavelEntrada variavelentrada) {
        this.variavelentrada = variavelentrada;
    }

    public CartesianChartModel getLinearModel() {
        
        //linearModel = new CartesianChartModel();
        Integer num = (Integer)inputcodvar.getValue();
        variavelentrada = variavelfacade.selecionaPorCodigo(num);
        termosdavariavel = termofacade.selecionaTudoPelaVariavel(num);
        
        linearModel = new CartesianChartModel();
        for(TermoLinguisticoEntrada termo: termosdavariavel){
            LineChartSeries serie = new LineChartSeries();
            serie.setLabel(termo.getNome());
            
            if(termo.getFuncao().equalsIgnoreCase("triangular")){
                serie.set(termo.getValora(), 0);
                serie.set(termo.getValorb(), 1);
                serie.set(termo.getValorc(), 0);
            }
            if(termo.getFuncao().equalsIgnoreCase("trapezoidal")){
                if(termo.getValora() != 0){
                    serie.set(termo.getValora(), 0);
                    serie.set(termo.getValorb(), 1);
                    serie.set(termo.getValorc(), 1);
                    serie.set(termo.getValord(), 0);
                } else {
                    serie.set(termo.getValora(), 0);
                    serie.set(termo.getValorb(), 1);
                    serie.set(termo.getValorc(), 1);
                    serie.set(termo.getValord(), 0);
                }       
            }
            if(termo.getFuncao().equalsIgnoreCase("rampaesquerda")){
                serie.set(termo.getVariavelEntrada().getMinimo(), 1);
                serie.set(termo.getValora(), 1);
                serie.set(termo.getValorb(), 0);
            }
            if(termo.getFuncao().equalsIgnoreCase("rampadireita")){
                //serie.set(termo.getVariavelEntrada().getMaximo(), 0);
                serie.set(termo.getVariavelEntrada().getMaximo(), 1);
                serie.set(termo.getValora(), 1);
                serie.set(termo.getValorb(), 0);
            }
            if(termo.getFuncao().equalsIgnoreCase("inclinacaoesquerda")){
                serie.set(termo.getVariavelEntrada().getMinimo(), 1);
                serie.set(termo.getValora(), 0);
            }
            if(termo.getFuncao().equalsIgnoreCase("inclinacaodireita")){
                serie.set(termo.getVariavelEntrada().getMaximo(), 1);
                serie.set(termo.getValora(), 0);
            }
            if(termo.getFuncao().equalsIgnoreCase("discreto")){
                serie.set(termo.getValora()-0.00000000000001, 0);
                serie.set(termo.getValora(), 1);
            }
            linearModel.addSeries(serie);
            
        }
        return linearModel;
    }

    public void setLinearModel(CartesianChartModel linearModel) {
        this.linearModel = linearModel;
    }

    public TermoLinguisticoEntradaFacade getTermofacade() {
        return termofacade;
    }

    public void setTermofacade(TermoLinguisticoEntradaFacade termofacade) {
        this.termofacade = termofacade;
    }

    public VariavelEntradaFacade getVariavelfacade() {
        return variavelfacade;
    }

    public void setVariavelfacade(VariavelEntradaFacade variavelfacade) {
        this.variavelfacade = variavelfacade;
    }

    public HtmlInputText getInputcodvar() {
        return inputcodvar;
    }

    public void setInputcodvar(HtmlInputText inputcodvar) {
        this.inputcodvar = inputcodvar;
    }
}
