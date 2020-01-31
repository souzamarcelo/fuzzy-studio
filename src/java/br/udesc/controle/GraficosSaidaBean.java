/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.controle;

import br.udesc.facade.TermoLinguisticoSaidaFacade;
import br.udesc.facade.VariavelSaidaFacade;
import br.udesc.modelo.TermoLinguisticoSaida;
import br.udesc.modelo.VariavelSaida;
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
public class GraficosSaidaBean {
    private VariavelSaida variavelsaida = new VariavelSaida();
    private List<TermoLinguisticoSaida> termosdavariavel = new ArrayList<TermoLinguisticoSaida>();
    @EJB
    private VariavelSaidaFacade variavelfacade;
    @EJB
    private TermoLinguisticoSaidaFacade termofacade;
    
    HtmlInputText inputcodvar = new HtmlInputText();
    
    //Gráfico
    private CartesianChartModel linearModel;

    public GraficosSaidaBean() {
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

    public CartesianChartModel getLinearModel() {
        Integer num = (Integer)inputcodvar.getValue();
        variavelsaida = variavelfacade.selecionaPorCodigo(num);
        termosdavariavel = termofacade.selecionaTudoPelaVariavel(num);
        
        linearModel = new CartesianChartModel();
        for(TermoLinguisticoSaida termo: termosdavariavel){
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
                serie.set(termo.getVariavelSaida().getMinimo(), 1);
                serie.set(termo.getValora(), 1);
                serie.set(termo.getValorb(), 0);
            }
            if(termo.getFuncao().equalsIgnoreCase("rampadireita")){
                //serie.set(termo.getVariavelSaida().getMaximo(), 0);
                serie.set(termo.getVariavelSaida().getMaximo(), 1);
                serie.set(termo.getValora(), 1);
                serie.set(termo.getValorb(), 0);
            }
            if(termo.getFuncao().equalsIgnoreCase("inclinacaoesquerda")){
                serie.set(termo.getVariavelSaida().getMinimo(), 1);
                serie.set(termo.getValora(), 0);
            }
            if(termo.getFuncao().equalsIgnoreCase("inclinacaodireita")){
                serie.set(termo.getVariavelSaida().getMaximo(), 1);
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

    public HtmlInputText getInputcodvar() {
        return inputcodvar;
    }

    public void setInputcodvar(HtmlInputText inputcodvar) {
        this.inputcodvar = inputcodvar;
    }

    public TermoLinguisticoSaidaFacade getTermofacade() {
        return termofacade;
    }

    public void setTermofacade(TermoLinguisticoSaidaFacade termofacade) {
        this.termofacade = termofacade;
    }

    public VariavelSaidaFacade getVariavelfacade() {
        return variavelfacade;
    }

    public void setVariavelfacade(VariavelSaidaFacade variavelfacade) {
        this.variavelfacade = variavelfacade;
    }

    public List<TermoLinguisticoSaida> getTermosdavariavel() {
        return termosdavariavel;
    }

    public void setTermosdavariavel(List<TermoLinguisticoSaida> termosdavariavel) {
        this.termosdavariavel = termosdavariavel;
    }

    public VariavelSaida getVariavelsaida() {
        return variavelsaida;
    }

    public void setVariavelsaida(VariavelSaida variavelsaida) {
        this.variavelsaida = variavelsaida;
    }
}
