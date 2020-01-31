/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.udesc.controle;

import br.udesc.modelo.VariavelSaida;
import br.udesc.modelo.VariavelEntrada;
import br.udesc.modelo.Antecedente;
import br.udesc.modelo.Regra;
import br.udesc.modelo.MotorInferencia;
import br.udesc.modelo.TermoLinguisticoEntrada;
import br.udesc.modelo.TermoLinguisticoSaida;
import br.udesc.modelo.Consequente;
import br.udesc.auxiliar.Grafico;
import br.udesc.facade.TermoLinguisticoSaidaFacade;
import br.udesc.facade.TermoLinguisticoEntradaFacade;
import br.udesc.facade.VariavelSaidaFacade;
import br.udesc.facade.MotorInferenciaFacade;
import br.udesc.facade.VariavelEntradaFacade;
import br.udesc.facade.RegraFacade;
import br.udesc.adaptadas.FunctionBlockAdaptada;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import org.antlr.runtime.RecognitionException;
import javax.faces.context.FacesContext;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author marcelo
 */

@ManagedBean
@SessionScoped
public class ExecucaoBean {
    
    private String retorno_log = "";
    private int tabindex = 0;
    
    private int cod_projeto;
    private List<VariavelEntrada> variaveisEntrada = new ArrayList<VariavelEntrada>();
    private List<VariavelSaida> variaveisSaida = new ArrayList<VariavelSaida>();
    private List<MotorInferencia> motores = new ArrayList<MotorInferencia>();
    @EJB
    private VariavelEntradaFacade entradafacade;
    @EJB
    private VariavelSaidaFacade saidafacade;
    @EJB
    private MotorInferenciaFacade motorfacade;
    @EJB
    private RegraFacade regrafacade;
    @EJB
    private TermoLinguisticoEntradaFacade termoentradafacade;
    @EJB
    private TermoLinguisticoSaidaFacade termosaidafacade;
    
    private List<VariavelEntrada> valoresEntrada = new ArrayList<VariavelEntrada>();
    private List<VariavelSaida> valoresSaida = new ArrayList<VariavelSaida>();
    private List<MotorInferencia> valoresMotores = new ArrayList<MotorInferencia>();
    
    String fcl = "";
    
    private List<Grafico> graficosEntrada = new ArrayList<Grafico>();
    private List<Grafico> graficosSaida = new ArrayList<Grafico>();
    private List<StreamedContent> chartsEntrada = new ArrayList<StreamedContent>();
    private List<StreamedContent> chartsSaida = new ArrayList<StreamedContent>();
    
    private String showFCL = "";
    private String classeFCL = "";
    
    
    public String atualizaTabelaConectados(){
        return "projeto.xhtml";
    }
    
    public String chamaFCL(MotorInferencia m){
        this.showFCL = "";
        this.classeFCL = "";
        this.showFCL = montaFCL(m);
        this.classeFCL = montaClasseFCL(fclToString(showFCL), m);
        return "fcl.xhtml";
    }
    
    public String chamaJava(MotorInferencia m){
        this.showFCL = montaFCL(m);
        this.classeFCL = montaClasseFCL(fclToString(showFCL), m);
        return "classejava.xhtml";
    }
    
    public String montaClasseFCL(String fcl, MotorInferencia m){
        
        List<Regra> rules = regrafacade.selecionaPeloMotor(m.getCodigo());
        List<VariavelEntrada> inputs = new ArrayList<VariavelEntrada>();
        List<VariavelSaida> outputs = new ArrayList<VariavelSaida>();
        
        
        for(Regra br : rules){
            List<Antecedente> ants = regrafacade.selecionaAntecedentes(br.getCodigo());
            List<Consequente> cons = regrafacade.selecionaConsequentes(br.getCodigo());
            
            for(Antecedente ant : ants){
                VariavelEntrada ve = ant.getVariavelEntrada();
                inputs.add(ve);
            }
            
            for(Consequente con : cons){
                VariavelSaida vs = con.getVariavelSaida();
                outputs.add(vs);
            }
        }
        
        List<VariavelEntrada> inputsDistinct = new ArrayList<VariavelEntrada>();
        for(VariavelEntrada e : inputs){
            boolean adiciona = true;
            for(VariavelEntrada ed : inputsDistinct){
                if(ed.getCodigo() == e.getCodigo()){
                    adiciona = false;
                }
            }
            
            if(adiciona)
                inputsDistinct.add(e);
        }
        
        List<VariavelSaida> outputsDistinct = new ArrayList<VariavelSaida>();
        for(VariavelSaida s : outputs){
            boolean adiciona = true;
            for(VariavelSaida sd : outputsDistinct){
                if(sd.getCodigo() == s.getCodigo()){
                    adiciona = false;
                }
            }
            
            if(adiciona)
                outputsDistinct.add(s);
        }
        
        
        
        
        StringBuffer strClass = new StringBuffer();
        strClass.append("import net.sourceforge.jFuzzyLogic.FIS;");
        strClass.append("\n");
        strClass.append("import net.sourceforge.jFuzzyLogic.FunctionBlock;");
        strClass.append("\n");
        strClass.append("import org.antlr.runtime.RecognitionException;");
        strClass.append("\n\n");
        
        strClass.append("public class FuzzyStudioFCL {");
        strClass.append("\n\n");
        strClass.append("    private static String fcl;");
        strClass.append("\n\n");
                
        strClass.append("    public static void main(String[] args) throws RecognitionException {");
        strClass.append("\n\n");
        strClass.append("        fcl = ");
        strClass.append(fcl);
        strClass.append("\n");
        strClass.append("\n");
        strClass.append("\n");
        strClass.append("        try{");
        strClass.append("\n");
        strClass.append("            FIS fis = FIS.createFromString(fcl, true);");
        strClass.append("\n");
        strClass.append("            FunctionBlock functionBlock = fis.getFunctionBlock(\""+m.getNome()+"\");");
        strClass.append("\n");
        
        for(VariavelEntrada ve : inputsDistinct){
            strClass.append("            functionBlock.setVariable(\""+ve.getNome()+"\", Double.parseDouble(javax.swing.JOptionPane.showInputDialog(\"Informe valor para "+ve.getNome()+"\")));");
            strClass.append("\n");
        }
        strClass.append("\n");
        
        strClass.append("            functionBlock.evaluate();");
        strClass.append("\n");
        strClass.append("\n");
        
        for(VariavelEntrada ve2 : inputsDistinct){
            strClass.append("            functionBlock.getVariable(\""+ve2.getNome()+"\").chart(true);");
            strClass.append("\n");
        }
        strClass.append("\n");
        
        for(VariavelSaida vs : outputsDistinct){
            strClass.append("            functionBlock.getVariable(\""+vs.getNome()+"\").chart(true);");
            strClass.append("\n");
            strClass.append("            functionBlock.getVariable(\""+vs.getNome()+"\").chartDefuzzifier(true);");
            strClass.append("\n");
        }
        strClass.append("\n");
        
        strClass.append("        } catch(Exception e){");
        strClass.append("\n");
        strClass.append("        }");
        strClass.append("\n");
        strClass.append("\n");
        strClass.append("    }");
        strClass.append("\n");
        strClass.append("\n");
        
        
        strClass.append("    public String getFcl(){");
        strClass.append("\n");
        strClass.append("        return this.fcl;");
        strClass.append("\n");
        strClass.append("    }");
        strClass.append("\n");
        strClass.append("}");
        
        return strClass.toString();
    }
    
    public String fclToString(String fcl){
        
        String result = "";
        String[] array = fcl.split("\n");
        
        for(int i = 0; i < array.length; i++){
            
            if(i == 0)
                result += "\""+array[i]+"\\n\""+"+"+"\n";
            else if (i == (array.length-1))
                result += "                    \""+array[i]+"\\n\""+";"+"\n";
            else
                result += "                    \""+array[i]+"\\n\""+"+"+"\n";
            
        }
        //System.out.println(result);
        return result;
    }
    
    public String chamaGraficos(MotorInferencia m) throws IOException{
        this.chartsEntrada = new ArrayList<StreamedContent>();
        this.chartsSaida = new ArrayList<StreamedContent>();
        
        int contador = 1;
        for(Grafico graf : graficosEntrada){
            if(graf.getMotor().getCodigo() == m.getCodigo()){
                File chartFile = new File("grafico"+contador);
                ChartUtilities.saveChartAsPNG(chartFile, graf.getGrafico(), 375, 300);  
                StreamedContent chart = new DefaultStreamedContent(new FileInputStream(chartFile), "image/png");
                chartsEntrada.add(chart);
                contador++;
            }
        }
        
        for(Grafico graf : graficosSaida){
            if(graf.getMotor().getCodigo() == m.getCodigo()){
                File chartFile = new File("grafico"+contador);
                ChartUtilities.saveChartAsPNG(chartFile, graf.getGrafico(), 375, 300);  
                StreamedContent chart = new DefaultStreamedContent(new FileInputStream(chartFile), "image/png");
                chartsSaida.add(chart);
                contador++;
            }
        }
        
        return "graficos.xhtml";
    }
    
    public void recuperaEntradas(int codProjeto){
        List<VariavelEntrada> variaveisDoBanco = entradafacade.selecionaTudo(codProjeto);
        
        for(VariavelEntrada v : variaveisDoBanco){
            for(VariavelEntrada v2 : valoresEntrada){
                if(v.getCodigo() == v2.getCodigo())
                    v.setValor(v2.getValor());
            }
        }
        this.variaveisEntrada = variaveisDoBanco;
    }
    
    public void recuperaSaidas(int codProjeto){
        //NAO UTILIZADO
        List<VariavelSaida> variaveisDoBanco = saidafacade.selecionaTudo(codProjeto);
        
        for(VariavelSaida v : variaveisDoBanco){
            for(VariavelSaida v2 : valoresSaida){
                if(v.getCodigo() == v2.getCodigo())
                    v.setValor(v2.getValor());
            }
        }
        this.variaveisSaida = variaveisDoBanco;
    }
    
    public void recuperaMotores(int codProjeto){
        List<MotorInferencia> motoresDoBanco = motorfacade.selecionaTudo(codProjeto);
        
        for(MotorInferencia m : motoresDoBanco){
            String resultadoDoMotor = "";
            List<VariavelSaida> consideradas = new ArrayList<VariavelSaida>();
            
            for(Regra b : regrafacade.selecionaPeloMotor(m.getCodigo())){
                for(VariavelSaida v : retornaSaidas(codProjeto)){
                    
                    for(Consequente con : regrafacade.selecionaConsequentes(b.getCodigo())){
                        if(con.getVariavelSaida().getCodigo() == v.getCodigo())
                            
                            if(v.getValor() != null){
                                boolean achou = false;
                                for(VariavelSaida v2 : consideradas){
                                    if(v2.getCodigo() == v.getCodigo())
                                        achou = true;
                                }
                                
                                if(!achou){
                                    if(v.getValor().get(0) == m.getCodigo()){
                                        resultadoDoMotor += v.getNome()+" ---> "+v.getValor().get(1)+"\n";
                                        consideradas.add(v);
                                    }
                                }
                            }
                    }
                }
            }
            
            m.setResultado(resultadoDoMotor);
        }
        this.motores = motoresDoBanco;
    }
    
    public String executaPorMotor(int codMotor) throws RecognitionException{
        //NAO UTILIZADO
        MotorInferencia m = motorfacade.selecionaPorCodigo(codMotor);
        
        montaFCL(m);
        executaFCL(m);
        this.tabindex = 1;
        return "projeto.xhtml";
    }
    
    public String executaTudo() throws RecognitionException{
        this.valoresEntrada = new ArrayList<VariavelEntrada>();
        valoresEntrada = this.variaveisEntrada;
        
        this.valoresSaida = new ArrayList<VariavelSaida>();
        this.graficosEntrada = new ArrayList<Grafico>();
        this.graficosSaida = new ArrayList<Grafico>();
        this.retorno_log = "";
        for(MotorInferencia m : motores){
            montaFCL(m);
            executaFCL(m);
        }
        this.tabindex = 1;
        return "projeto.xhtml";
    }
    
    public String montaFCL(MotorInferencia motor){
        
        String result = "";
        
        result += "FUNCTION_BLOCK "+motor.getNome()+" \n";
        result += "\n";
        
        //============================ VARIAVEL DE ENTRADA
        result += "VAR_INPUT \n";
        for(VariavelEntrada ve : retornaEntradasDoMotor(motor.getCodigo())){
            result += "   "+ve.getNome()+" : REAL;\n";
        }
        result += "END_VAR\n";
        result += "\n";
        //================================================
        
        
        //============================ VARIAVEL DE SAIDA
        result += "VAR_OUTPUT \n";
        for(VariavelSaida vs : retornaSaidasDoMotor(motor.getCodigo())){
            result += "   "+vs.getNome()+" : REAL;\n";
        }
        result += "END_VAR\n";
        //================================================
        
        //============================ FUZZIFICACAO
        for(VariavelEntrada ve2 : retornaEntradasDoMotor(motor.getCodigo())){
            List<TermoLinguisticoEntrada> termosEntrada = termoentradafacade.selecionaTudoPelaVariavel(ve2.getCodigo());
            if(termosEntrada.size() > 0)
                result += "FUZZIFY "+ve2.getNome()+" \n";
            
            for(TermoLinguisticoEntrada te : termosEntrada){

                if(te.getFuncao().equalsIgnoreCase("triangular"))
                    result += "   TERM "+te.getNome()+" := ("+te.getValora()+", 0) ("+te.getValorb()+", 1) ("+te.getValorc()+", 0); \n";

                if(te.getFuncao().equalsIgnoreCase("trapezoidal"))
                    result += "   TERM "+te.getNome()+" := ("+te.getValora()+", 0) ("+te.getValorb()+", 1) ("+te.getValorc()+", 1) ("+te.getValord()+", 0); \n";

                if(te.getFuncao().equalsIgnoreCase("rampadireita"))
                    result += "   TERM "+te.getNome()+" := ("+te.getValorb()+", 0) ("+te.getValora()+", 1) ("+ve2.getMaximo() +", 1); \n";

                if(te.getFuncao().equalsIgnoreCase("rampaesquerda"))
                    result += "   TERM "+te.getNome()+" := ("+ve2.getMinimo()+", 1) ("+te.getValora()+", 1) ("+te.getValorb()+", 0); \n";

                if(te.getFuncao().equalsIgnoreCase("inclinacaodireita"))
                    result += "   TERM "+te.getNome()+" := ("+te.getValora()+", 0) ("+ve2.getMaximo()+", 1); \n";

                if(te.getFuncao().equalsIgnoreCase("inclinacaoesquerda"))
                    result += "   TERM "+te.getNome()+" := ("+ve2.getMinimo()+", 1) ("+te.getValora()+", 0); \n";
            }
            result += "END_FUZZIFY\n";
        }
        
        result += "\n";
        //================================================
        
        //============================ DEFUZZIFICACAO
        for(VariavelSaida ve3 : retornaSaidasDoMotor(motor.getCodigo())){
            List<TermoLinguisticoSaida> termosSaida = termosaidafacade.selecionaTudoPelaVariavel(ve3.getCodigo());
            if(termosSaida.size() > 0)
                result += "DEFUZZIFY "+ve3.getNome()+" \n";
            for(TermoLinguisticoSaida te : termosSaida){

                if(te.getFuncao().equalsIgnoreCase("triangular"))
                    result += "   TERM "+te.getNome()+" := ("+te.getValora()+", 0) ("+te.getValorb()+", 1) ("+te.getValorc()+", 0); \n";

                if(te.getFuncao().equalsIgnoreCase("trapezoidal"))
                    result += "   TERM "+te.getNome()+" := ("+te.getValora()+", 0) ("+te.getValorb()+", 1) ("+te.getValorc()+", 1) ("+te.getValord()+", 0); \n";

                if(te.getFuncao().equalsIgnoreCase("rampadireita"))
                    result += "   TERM "+te.getNome()+" := ("+te.getValorb()+", 0) ("+te.getValora()+", 1) ("+ve3.getMaximo() +", 1); \n";

                if(te.getFuncao().equalsIgnoreCase("rampaesquerda"))
                    result += "   TERM "+te.getNome()+" := ("+ve3.getMinimo()+", 1) ("+te.getValora()+", 1) ("+te.getValorb()+", 0); \n";

                if(te.getFuncao().equalsIgnoreCase("inclinacaodireita"))
                    result += "   TERM "+te.getNome()+" := ("+te.getValora()+", 0) ("+ve3.getMaximo()+", 1); \n";

                if(te.getFuncao().equalsIgnoreCase("inclinacaoesquerda"))
                    result += "   TERM "+te.getNome()+" := ("+ve3.getMinimo()+", 1) ("+te.getValora()+", 0); \n";
            }
            result += "   METHOD : "+motor.getMetodo_defuzzificacao()+"; \n";
            result += "   DEFAULT := 0; \n";
            result += "END_DEFUZZIFY\n";
        }
        result += "\n";
        //================================================
        
        
        //============================ BLOCO DE REGRAS
        result += "RULEBLOCK blocoderegra\n";
        result += "\n";
        
        result += "   ACCU : "+motor.getMetodo_agregacao_regras()+"; \n";
        result += "   AND : "+motor.getConexao_and()+"; \n";
        result += "   ACT : "+motor.getMetodo_ativacao_regras()+"; \n";
            
        int counter = 1;
        //regrafacade.selecionaPeloMotor(motor.getCodigo())
        List<Regra> brs = regrafacade.selecionaPeloMotor(motor.getCodigo());
        for(Regra br : brs){
            result += "   RULE "+counter+" : "+montaRegra(br)+";\n";
            counter++;
        }
        
        result += "END_RULEBLOCK\n";
        result += "\n";
        //================================================
        
        
        result += "END_FUNCTION_BLOCK\n";
        
        System.out.println(result);
        
        fcl = result;
        return fcl;
    }
    
    public String executaFCL(MotorInferencia motor) throws RecognitionException{
        
        System.out.println("INÍCIO DO SISTEMA FUZZY");
        
        try{
            FIS fis = FIS.createFromString(fcl, true);
            FunctionBlock fb = fis.getFunctionBlock(motor.getNome());
            FunctionBlockAdaptada functionBlock = new FunctionBlockAdaptada(fis);
            functionBlock.setName(fb.getName());
            functionBlock.setRuleBlocks(fb.getRuleBlocks());
            functionBlock.setVariables(fb.getVariables());
            
            List<Regra> basesDoMotor = regrafacade.selecionaPeloMotor(motor.getCodigo());
            
            for(Regra b : basesDoMotor){
                for(Antecedente ant : regrafacade.selecionaAntecedentes(b.getCodigo())){
                    for(VariavelEntrada entrada : valoresEntrada){
                        if(entrada.getCodigo() == ant.getVariavelEntrada().getCodigo())
                            functionBlock.setVariable(ant.getVariavelEntrada().getNome(), entrada.getValor());
                    }
                }
            }
            
            functionBlock.evaluate();
            
            for(Regra b : basesDoMotor){
                for(Consequente con : regrafacade.selecionaConsequentes(b.getCodigo())){
                    System.out.println("");
                    for(int i = 0; i < variaveisSaida.size(); i++)
                        if(variaveisSaida.get(i).getCodigo() == con.getVariavelSaida().getCodigo()){
                            
                            List<Double> value = new ArrayList<Double>();
                            value.add(Double.parseDouble(String.valueOf(motor.getCodigo())));
                            value.add(functionBlock.getVariable(variaveisSaida.get(i).getNome()).getValue());
                            variaveisSaida.get(i).setValor(value);
                            VariavelSaida vsaida = new VariavelSaida();
                            vsaida = (VariavelSaida)variaveisSaida.get(i).clone();
                            this.valoresSaida.add(vsaida);
                        }
                }
            }
            
            //============================================================================== Montagem GRÁFICOS
            
            
            Collection<Variable> var = functionBlock.variables();
            Iterator<Variable> iterador = var.iterator();
            
            while(iterador.hasNext()){
                
                Variable v = iterador.next();
                
                JFreeChart chart = v.chart(false);
                Grafico grafico = new Grafico();
                grafico.setGrafico(chart);
                grafico.setMotor(motor);
                
                if(v.isOutputVarable())
                    this.graficosSaida.add(grafico);
                else
                    this.graficosEntrada.add(grafico);
                
                /*if(v.isOutputVarable()){
                    JFreeChart chartDef = v.chartDefuzzifier(false);
                    Grafico graficod = new Grafico();
                    graficod.setGrafico(chart);
                    graficod.setMotor(motor);
                    this.graficos.add(graficod);
                }*/
            }
            
            
            //============================================================================== FIM Montagem GRÁFICOS
            
            
            System.out.println(functionBlock);
            retorno_log += "\n==============================================================================\n" + functionBlock.toString();
            

        } catch(Exception e){
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro na Execução", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

        System.out.println("FIM DO SISTEMA FUZZY");
        return "";
    }
    
    public String montaRegra(Regra base){
        String antecedente = "";
        String consequente = "";
        
        for(Antecedente ant : regrafacade.selecionaAntecedentes(base.getCodigo())){
            
            if(antecedente.equalsIgnoreCase("")){
                antecedente += ant.getVariavelEntrada().getNome();
                antecedente += " IS ";
                antecedente += ant.getTermo().getNome();
            } else {
                antecedente += " " + base.getConector().toUpperCase() + " ";
                antecedente += ant.getVariavelEntrada().getNome();
                antecedente += " IS ";
                antecedente += ant.getTermo().getNome();
            }
        }
        
        for(Consequente con : regrafacade.selecionaConsequentes(base.getCodigo())){
            
            if(consequente.equalsIgnoreCase("")){
                consequente += con.getVariavelSaida().getNome();
                consequente += " IS ";
                consequente += con.getTermo().getNome();
            } else {
                consequente += " AND ";
                consequente += con.getVariavelSaida().getNome();
                consequente += " IS ";
                consequente += con.getTermo().getNome();
            }
        }
        
        
        base.setDescricaoAntecedente(antecedente);
        base.setDescricaoConsequente(consequente);
        String regra = "IF " + antecedente + " THEN " + consequente;
        return regra;
    }
    
    public List<VariavelSaida> retornaSaidasDoMotor (int codMotor){
        MotorInferencia motor = motorfacade.selecionaPorCodigo(codMotor);
        List<VariavelSaida> saidasDoMotor = new ArrayList<VariavelSaida>();
        
        for(Regra r : regrafacade.selecionaPeloMotor(codMotor)){
            for(Consequente con : regrafacade.selecionaConsequentes(r.getCodigo())){
                VariavelSaida v = con.getVariavelSaida();
                
                boolean achou = false;
                for(VariavelSaida sai : saidasDoMotor){
                    if(sai.getCodigo() == v.getCodigo())
                        achou = true;
                }
                if(!achou)
                    saidasDoMotor.add(v);
            }
        }
        
        return saidasDoMotor;
    }
    
    public List<VariavelEntrada> retornaEntradasDoMotor (int codMotor){
        MotorInferencia motor = motorfacade.selecionaPorCodigo(codMotor);
        List<VariavelEntrada> entradasDoMotor = new ArrayList<VariavelEntrada>();
        
        for(Regra r : regrafacade.selecionaPeloMotor(codMotor)){
            for(Antecedente ant : regrafacade.selecionaAntecedentes(r.getCodigo())){
                VariavelEntrada v = ant.getVariavelEntrada();
                
                boolean achou = false;
                for(VariavelEntrada ent : entradasDoMotor){
                    if(ent.getCodigo() == v.getCodigo())
                        achou = true;
                }
                if(!achou)
                    entradasDoMotor.add(v);
            }
        }
        
        return entradasDoMotor;
    }
    
    public List<MotorInferencia> retornaMotores(int codProjeto){
        this.recuperaMotores(codProjeto);
        return this.motores;
    }
    
    public List<VariavelEntrada> retornaEntradas(int codProjeto){
        this.recuperaEntradas(codProjeto);
        return this.variaveisEntrada;
    }
    
    public List<VariavelSaida> retornaSaidas(int codProjeto){
        this.recuperaSaidas(codProjeto);
        return this.valoresSaida;
    }
    
    public String chamaExecucao(){
        this.tabindex = 1;
        return "projeto.xhtml";
    }

    public String getRetorno_log() {
        return retorno_log;
    }

    public void setRetorno_log(String retorno_log) {
        this.retorno_log = retorno_log;
    }

    public int getTabindex() {
        return tabindex;
    }

    public void setTabindex(int tabindex) {
        this.tabindex = tabindex;
    }

    public VariavelEntradaFacade getEntradafacade() {
        return entradafacade;
    }

    public void setEntradafacade(VariavelEntradaFacade entradafacade) {
        this.entradafacade = entradafacade;
    }

    public VariavelSaidaFacade getSaidafacade() {
        return saidafacade;
    }

    public void setSaidafacade(VariavelSaidaFacade saidafacade) {
        this.saidafacade = saidafacade;
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

    public String getFcl() {
        return fcl;
    }

    public void setFcl(String fcl) {
        this.fcl = fcl;
    }

    public int getCod_projeto() {
        return cod_projeto;
    }

    public void setCod_projeto(int cod_projeto) {
        this.cod_projeto = cod_projeto;
    }

    public List<MotorInferencia> getMotores() {
        return motores;
    }

    public void setMotores(List<MotorInferencia> motores) {
        this.motores = motores;
    }

    public MotorInferenciaFacade getMotorfacade() {
        return motorfacade;
    }

    public void setMotorfacade(MotorInferenciaFacade motorfacade) {
        this.motorfacade = motorfacade;
    }

    public RegraFacade getRegrafacade() {
        return regrafacade;
    }

    public void setRegrafacade(RegraFacade regrafacade) {
        this.regrafacade = regrafacade;
    }

    public TermoLinguisticoEntradaFacade getTermoentradafacade() {
        return termoentradafacade;
    }

    public void setTermoentradafacade(TermoLinguisticoEntradaFacade termoentradafacade) {
        this.termoentradafacade = termoentradafacade;
    }

    public TermoLinguisticoSaidaFacade getTermosaidafacade() {
        return termosaidafacade;
    }

    public void setTermosaidafacade(TermoLinguisticoSaidaFacade termosaidafacade) {
        this.termosaidafacade = termosaidafacade;
    }

    public List<VariavelEntrada> getValoresEntrada() {
        return valoresEntrada;
    }

    public void setValoresEntrada(List<VariavelEntrada> valoresEntrada) {
        this.valoresEntrada = valoresEntrada;
    }

    public List<MotorInferencia> getValoresMotores() {
        return valoresMotores;
    }

    public void setValoresMotores(List<MotorInferencia> valoresMotores) {
        this.valoresMotores = valoresMotores;
    }

    public List<VariavelSaida> getValoresSaida() {
        return valoresSaida;
    }

    public void setValoresSaida(List<VariavelSaida> valoresSaida) {
        this.valoresSaida = valoresSaida;
    }

    public List<StreamedContent> getChartsEntrada() {
        return chartsEntrada;
    }

    public void setChartsEntrada(List<StreamedContent> chartsEntrada) {
        this.chartsEntrada = chartsEntrada;
    }

    public List<StreamedContent> getChartsSaida() {
        return chartsSaida;
    }

    public void setChartsSaida(List<StreamedContent> chartsSaida) {
        this.chartsSaida = chartsSaida;
    }

    public List<Grafico> getGraficosEntrada() {
        return graficosEntrada;
    }

    public void setGraficosEntrada(List<Grafico> graficosEntrada) {
        this.graficosEntrada = graficosEntrada;
    }

    public List<Grafico> getGraficosSaida() {
        return graficosSaida;
    }

    public void setGraficosSaida(List<Grafico> graficosSaida) {
        this.graficosSaida = graficosSaida;
    }

    public String getShowFCL() {
        return showFCL;
    }

    public void setShowFCL(String showFCL) {
        this.showFCL = showFCL;
    }

    public String getClasseFCL() {
        return classeFCL;
    }

    public void setClasseFCL(String classeFCL) {
        this.classeFCL = classeFCL;
    }
}
