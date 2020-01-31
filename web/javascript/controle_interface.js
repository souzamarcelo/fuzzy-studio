function matriz_entradas(codigo, nome, minimo, maximo, unidade, posx, posy){ 
    this.codigo = codigo;
    this.nome = nome;
    this.minimo = minimo;
    this.maximo = maximo;
    this.unidade = unidade;
    this.posx = posx;
    this.posy = posy;
}

function matriz_saidas(codigo, nome, minimo, maximo, unidade, posx, posy){ 
    this.codigo = codigo;
    this.nome = nome;
    this.minimo = minimo;
    this.maximo = maximo;
    this.unidade = unidade;
    this.posx = posx;
    this.posy = posy;
}

function matriz_motores(codigo, nome, posx, posy){ 
    this.codigo = codigo;
    this.nome = nome;
    this.posx = posx;
    this.posy = posy;
}

function matriz_linhas(x1, y1, x2, y2){
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
}

function Elemento(x, y, w, h){
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
}

function ElementoX(tipo, codigo, x, y, w, h){
    this.tipo = tipo;
    this.codigo = codigo;
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
}

var array_entradas = new Array();
var array_saidas = new Array();
var array_motores = new Array();
var array_linhas = new Array();
var campo;
var canvas;
var context;
var codProjeto;

var array_elementos = new Array();

var elementoSelecionado = null;
var clicado = false;

var xoriginal = 0;
var yoriginal = 0;
var xdestino = 0;
var ydestino = 0;

//Função que redesenha a tela para capturar alterações de outros usuários
var intervalo = window.setInterval(recuperaDados, 2000);


alert = function(){}


function salvarCanvas(){
    var ctx = document.querySelector('canvas').getContext('2d');
    window.location = ctx.canvas.toDataURL('image/png');
}

function cria_eventos(){
    
    document.onselectstart="return false";
    
    array_elementos = new Array();
    var contador = 0;
    
    for(var i = 0; i < array_entradas.length; i++){
        array_elementos[contador] = new Elemento(array_entradas[i].posx, array_entradas[i].posy, 90, 60);
        contador++;
    }
    
    for(var j = 0; j < array_saidas.length; j++){
        array_elementos[contador] = new Elemento(array_saidas[j].posx, array_saidas[j].posy, 90, 60);
        contador++;
    }
    
    for(var k = 0; k < array_motores.length; k++){
        array_elementos[contador] = new Elemento(array_motores[k].posx, array_motores[k].posy, 90, 60);
        contador++;
    }
    
    canvas.onmousedown = function(evt){
        var rectNav = canvas.getBoundingClientRect();
        var xclicado = evt.clientX - rectNav.left;
        var yclicado = evt.clientY - rectNav.top;
        if(evt.button == 0)
            clicou_canvas(xclicado, yclicado);
        
        clearInterval(intervalo);
    }
    
    canvas.onmouseup = function(evt){
        var rectNav = canvas.getBoundingClientRect();
        var xclicado = evt.clientX - rectNav.left;
        var yclicado = evt.clientY - rectNav.top;
        soltou_canvas(xclicado, yclicado);
        intervalo = window.setInterval(recuperaDados, 2000);
    }
    
    canvas.onmousemove = function(evt){
        var rectNav = canvas.getBoundingClientRect();
        var xatual = evt.clientX - rectNav.left;
        var yatual = evt.clientY - rectNav.top;
        mouse_moving(xatual, yatual);
    }
    
    canvas.onmouseover = function(evt){
        document.body.style.cursor = 'pointer';
    }
    
    canvas.onmouseout = function(evt){
        document.body.style.cursor = 'default';
    }
        
}

function clicou_canvas_direito(xclicado, yclicado){
    /*ClasseInterface.editaElemento(10, function(data){
        redireciona_para_edicao(data);
    });*/
}

function redireciona_para_edicao(data){
    location.href = data;
}

function redesenha(){
    limpaCanvas();
    atualiza_canvas_linhas();
    atualiza_canvas_entradas();
    atualiza_canvas_saidas();
    atualiza_canvas_motores();
    //cria_eventos();
}

function reposiciona_linhas(posxantiga, posyantiga, posxnova, posynova){
    posxantiga = parseInt(posxantiga) + 45;
    posyantiga = parseInt(posyantiga) + 30;
    posxnova = parseInt(posxnova) + 45;
    posynova = parseInt(posynova) + 30;

    for(var i = 0; i < array_linhas.length; i++){
        if(array_linhas[i].x1 == posxantiga && array_linhas[i].y1 == posyantiga){
            array_linhas[i].x1 = posxnova;
            array_linhas[i].y1 = posynova;
        } else {
            if(array_linhas[i].x2 == posxantiga && array_linhas[i].y2 == posyantiga){
                array_linhas[i].x2 = posxnova;
                array_linhas[i].y2 = posynova;
            }
        }
    }
}

function mouse_moving(xatual, yatual){
    if(elementoSelecionado != null){
        xdestino = xatual;
        ydestino = yatual;
        
        var deslocamentox = parseInt(xdestino) - parseInt(xoriginal);
        var deslocamentoy = parseInt(ydestino) - parseInt(yoriginal);
        var codigo = elementoSelecionado.codigo;
        
        if(elementoSelecionado.tipo == 1){
            //busca o elemento na matriz de entradas
            //altera as coordenadas de acordo com o deslocamento
            //redesenha com base nas matrizes, ignorando as strings
            //quando solta o click grava novas coordenadas no banco
            
            for(var i = 0; i < array_entradas.length; i++){  
                
                if(array_entradas[i].codigo == codigo){
                    posxantiga = array_entradas[i].posx;
                    posyantiga = array_entradas[i].posy;
                    array_entradas[i].posx = parseInt(array_entradas[i].posx) + parseInt(deslocamentox);
                    array_entradas[i].posy = parseInt(array_entradas[i].posy) + parseInt(deslocamentoy);
                    reposiciona_linhas(posxantiga, posyantiga, array_entradas[i].posx, array_entradas[i].posy);
                    xoriginal = xatual;
                    yoriginal = yatual;
                    redesenha();
                }
            }
        }
        
        if(elementoSelecionado.tipo == 2){
            for(var j = 0; j < array_saidas.length; j++){
                if(array_saidas[j].codigo == elementoSelecionado.codigo){
                    posxantiga = array_saidas[j].posx;
                    posyantiga = array_saidas[j].posy;
                    array_saidas[j].posx = parseInt(array_saidas[j].posx) + parseInt(deslocamentox);
                    array_saidas[j].posy = parseInt(array_saidas[j].posy) + parseInt(deslocamentoy);
                    reposiciona_linhas(posxantiga, posyantiga, array_saidas[j].posx, array_saidas[j].posy);
                    xoriginal = xatual;
                    yoriginal = yatual;
                    redesenha();
                }
            }
        }
        
        if(elementoSelecionado.tipo == 3){
            for(var k = 0; k < array_motores.length; k++){
                if(array_motores[k].codigo == elementoSelecionado.codigo){
                    posxantiga = array_motores[k].posx;
                    posyantiga = array_motores[k].posy;
                    array_motores[k].posx = parseInt(array_motores[k].posx) + parseInt(deslocamentox);
                    array_motores[k].posy = parseInt(array_motores[k].posy) + parseInt(deslocamentoy);
                    reposiciona_linhas(posxantiga, posyantiga, array_motores[k].posx, array_motores[k].posy);
                    xoriginal = xatual;
                    yoriginal = yatual;
                    redesenha();
                }
            }
        }
        
    }
}

function soltou_canvas(a, b){
    if(a > 690) a = 690;
    if(b > 440) b = 440;
    var xsoltado = a;
    var ysoltado = b;
    xdestino = a;
    ydestino = b;
    clicado = false;
    
    var cod = elementoSelecionado.codigo;
    var tipo = elementoSelecionado.tipo;
    
    var posx = a;
    var posy = b;
    
    atualizaBancoDeDados(cod, tipo, posx, posy);
}

function atualizaBancoDeDados(codigo, tipo, posx, posy){
    
    var xx = parseInt(posx);
    var yy = parseInt(posy);
    
    ClasseInterface.gravaPosicoes(codigo, tipo, xx, yy, function(data){
        retorno_movimento(data);
    });
    
}

function retorno_movimento(data){
    //Aqui faz a função de callback
    recuperaDados();
    elementoSelecionado = null;
}

function clicou_canvas(a, b){
    
    var xclicado = a;
    var yclicado = b;
    elementoSelecionado = null;
    clicado = true;
    var elemento;var w;var xelemento;var yelemento;var xmaxelemento;var ymaxelemento;
    
    for(w = 0; w < array_motores.length; w++){
        elemento = array_motores[w];
        xelemento = elemento.posx;
        yelemento = elemento.posy;
        xmaxelemento = 90 + parseInt(elemento.posx);
        ymaxelemento = 60 + parseInt(elemento.posy);
        
        if(xclicado >= xelemento && xclicado <= xmaxelemento){
            if(yclicado >= yelemento && yclicado <= ymaxelemento){
                elementoSelecionado = new ElementoX(3, array_motores[w].codigo,array_motores[w].posx, array_motores[w].posy, 90, 60);
                xoriginal = array_motores[w].posx;
                yoriginal = array_motores[w].posy;
            }
        }
            
    }
    
    if(elementoSelecionado == null)
    for(w = 0; w < array_saidas.length; w++){
        elemento = array_saidas[w];
        xelemento = elemento.posx;
        yelemento = elemento.posy;
        xmaxelemento = 90 + parseInt(elemento.posx);
        ymaxelemento = 60 + parseInt(elemento.posy);

        if(xclicado >= xelemento && xclicado <= xmaxelemento){
            if(yclicado >= yelemento && yclicado <= ymaxelemento){
                elementoSelecionado = new ElementoX(2, array_saidas[w].codigo, array_saidas[w].posx, array_saidas[w].posy, 90, 60);
                xoriginal = array_saidas[w].posx;
                yoriginal = array_saidas[w].posy;
            }
        }
            
    }
    
    if(elementoSelecionado == null)
    for(w = 0; w < array_entradas.length; w++){
        elemento = array_entradas[w];
        xelemento = elemento.posx;
        yelemento = elemento.posy;
        xmaxelemento = 90 + parseInt(elemento.posx);
        ymaxelemento = 60 + parseInt(elemento.posy);
        
        if(xclicado >= xelemento && xclicado <= xmaxelemento){
            if(yclicado >= yelemento && yclicado <= ymaxelemento){
                elementoSelecionado = new ElementoX(1, array_entradas[w].codigo, array_entradas[w].posx, array_entradas[w].posy, 90, 60);
                xoriginal = array_entradas[w].posx;
                yoriginal = array_entradas[w].posy;
            }
        }
            
    }
    
    if(elementoSelecionado == null){
        xoriginal = 0;
        yoriginal = 0;
        
        verifica_clique_edicao(a, b);
    }

}

function verifica_clique_edicao(a, b){
    var xclicado = a;
    var yclicado = b;
    var elementoParaEdicao = null;
    var elemento;var w;var xelemento;var yelemento;var xmaxelemento;var ymaxelemento;
    
    
    for(w = 0; w < array_motores.length; w++){
        elemento = array_motores[w];
        xelemento = parseInt(elemento.posx) + 98;
        yelemento = parseInt(elemento.posy) + 3;
        xmaxelemento = parseInt(elemento.posx) + 108;
        ymaxelemento = parseInt(elemento.posy) + 13;
        
        if(xclicado >= xelemento && xclicado <= xmaxelemento){
            if(yclicado >= yelemento && yclicado <= ymaxelemento){
                elementoParaEdicao = new ElementoX(3, array_motores[w].codigo,array_motores[w].posx, array_motores[w].posy, 90, 60);
            }
        }
            
    }
    
    if(elementoParaEdicao == null)
    for(w = 0; w < array_saidas.length; w++){
        elemento = array_saidas[w];
        xelemento = parseInt(elemento.posx) + 98;
        yelemento = parseInt(elemento.posy) + 3;
        xmaxelemento = parseInt(elemento.posx) + 108;
        ymaxelemento = parseInt(elemento.posy) + 13;

        if(xclicado >= xelemento && xclicado <= xmaxelemento){
            if(yclicado >= yelemento && yclicado <= ymaxelemento){
                elementoParaEdicao = new ElementoX(2, array_saidas[w].codigo, array_saidas[w].posx, array_saidas[w].posy, 90, 60);
            }
        }
            
    }
    
    if(elementoParaEdicao == null)
    for(w = 0; w < array_entradas.length; w++){
        elemento = array_entradas[w];
        xelemento = parseInt(elemento.posx) + 98;
        yelemento = parseInt(elemento.posy) + 3;
        xmaxelemento = parseInt(elemento.posx) + 108;
        ymaxelemento = parseInt(elemento.posy) + 13;
        
        if(xclicado >= xelemento && xclicado <= xmaxelemento){
            if(yclicado >= yelemento && yclicado <= ymaxelemento){
                elementoParaEdicao = new ElementoX(1, array_entradas[w].codigo, array_entradas[w].posx, array_entradas[w].posy, 90, 60);
            }
        }
            
    }
    
    if(elementoParaEdicao == null){
        xoriginal = 0;
        yoriginal = 0;
        
        verifica_clique_exclusao(a, b);
    } else {
        buscaElementoDoBanco(elementoParaEdicao);
    }
}

function verifica_clique_exclusao(a, b){
    var xclicado = a;
    var yclicado = b;
    var elementoParaExclusao = null;
    var elemento;var w;var xelemento;var yelemento;var xmaxelemento;var ymaxelemento;

    for(w = 0; w < array_motores.length; w++){
        elemento = array_motores[w];
        xelemento = parseInt(elemento.posx) + 98;
        yelemento = parseInt(elemento.posy) + 20;
        xmaxelemento = parseInt(elemento.posx) + 108;
        ymaxelemento = parseInt(elemento.posy) + 30;
        
        if(xclicado >= xelemento && xclicado <= xmaxelemento){
            if(yclicado >= yelemento && yclicado <= ymaxelemento){
                elementoParaExclusao = new ElementoX(3, array_motores[w].codigo,array_motores[w].posx, array_motores[w].posy, 90, 60);
            }
        }
            
    }
    
    if(elementoParaExclusao == null)
    for(w = 0; w < array_saidas.length; w++){
        elemento = array_saidas[w];
        xelemento = parseInt(elemento.posx) + 98;
        yelemento = parseInt(elemento.posy) + 20;
        xmaxelemento = parseInt(elemento.posx) + 108;
        ymaxelemento = parseInt(elemento.posy) + 30;

        if(xclicado >= xelemento && xclicado <= xmaxelemento){
            if(yclicado >= yelemento && yclicado <= ymaxelemento){
                elementoParaExclusao = new ElementoX(2, array_saidas[w].codigo, array_saidas[w].posx, array_saidas[w].posy, 90, 60);
            }
        }
            
    }
    
    if(elementoParaExclusao == null)
    for(w = 0; w < array_entradas.length; w++){
        elemento = array_entradas[w];
        xelemento = parseInt(elemento.posx) + 98;
        yelemento = parseInt(elemento.posy) + 20;
        xmaxelemento = parseInt(elemento.posx) + 108;
        ymaxelemento = parseInt(elemento.posy) + 30;
        
        if(xclicado >= xelemento && xclicado <= xmaxelemento){
            if(yclicado >= yelemento && yclicado <= ymaxelemento){
                elementoParaExclusao = new ElementoX(1, array_entradas[w].codigo, array_entradas[w].posx, array_entradas[w].posy, 90, 60);
            }
        }
            
    }
    
    if(elementoParaExclusao == null){
        xoriginal = 0;
        yoriginal = 0;
    } else {
        excluiElemento(elementoParaExclusao);
    }
}

function excluiElemento(elementoParaExclusao){
    var tipo = elementoParaExclusao.tipo;
    var codigo = elementoParaExclusao.codigo;
    
    ClasseInterface.excluiElement(tipo, codigo, function(data){
        mensagemExclusaoAtualiza(data);
    });
}

function mensagemExclusaoAtualiza(data){
    if(data == true)
        alert('Elemento eliminado!');
    recuperaDados();
}

function buscaElementoDoBanco(elementoParaEdicao){
    ClasseInterface.retornaElemento(elementoParaEdicao.tipo, elementoParaEdicao.codigo, function(data){
        if(elementoParaEdicao.tipo == 3)
            editaMotor(data);
        if(elementoParaEdicao.tipo == 1)
            editaVariavelEntrada(data);
        if(elementoParaEdicao.tipo == 2)
            editaVariavelSaida(data);
    });
}

function alteraEntrada(){
    var vcodigo = document.getElementById('tabView:entradaCodigo').value;
    var vnome = document.getElementById('tabView:entradaNome').value;
    var vminimo = document.getElementById('tabView:entradaMinimo').value;
    var vmaximo = document.getElementById('tabView:entradaMaximo').value;
    var vunidade = document.getElementById('tabView:entradaUnidade').value;
    
    ClasseInterface.alteraEntrada(vcodigo, vnome, vminimo, vmaximo, vunidade, function(data){
        var str_location = document.location.toString();
        var location_separado = str_location.split("/");
        var destino = "";
        for(var i = 0; i < (location_separado.length - 1); i++){
            destino += location_separado[i] + "/";
        }
        destino += "projeto.xhtml";
        document.location = destino;
    });
}

function alteraSaida(){
    var vcodigo = document.getElementById('tabView:saidaCodigo').value;
    var vnome = document.getElementById('tabView:saidaNome').value;
    var vminimo = document.getElementById('tabView:saidaMinimo').value;
    var vmaximo = document.getElementById('tabView:saidaMaximo').value;
    var vunidade = document.getElementById('tabView:saidaUnidade').value;
    
    ClasseInterface.alteraSaida(vcodigo, vnome, vminimo, vmaximo, vunidade, function(data){
        var str_location = document.location.toString();
        var location_separado = str_location.split("/");
        var destino = "";
        for(var i = 0; i < (location_separado.length - 1); i++){
            destino += location_separado[i] + "/";
        }
        destino += "projeto.xhtml";
        document.location = destino;
    });
}

function alteraMotor(){
    var mcodigo = document.getElementById('tabView:motorCodigo').value;
    var mnome = document.getElementById('tabView:motorNome').value;
    var mmetodo_defuz = "";
    var mmetodo_agreg = "";
    var mconexao = "";
    var mmetodo_ativ = "";
    
    
    if(document.getElementById('cog').checked)
        mmetodo_defuz = "COG";
    if(document.getElementById('rmm').checked)
        mmetodo_defuz = "RM";
    if(document.getElementById('coa').checked)
        mmetodo_defuz = "COA";
    if(document.getElementById('lmm').checked)
        mmetodo_defuz = "LM";


    if(document.getElementById('boundedsum').checked)
        mmetodo_agreg = "BSUM";
    if(document.getElementById('max').checked)
        mmetodo_agreg = "MAX";
    if(document.getElementById('sum').checked)
        mmetodo_agreg = "SUM";
    if(document.getElementById('normedsum').checked)
        mmetodo_agreg = "NSUM";


    if(document.getElementById('and_min').checked)
        mconexao = "MIN";
    if(document.getElementById('and_prod').checked)
        mconexao = "PROD";
    if(document.getElementById('and_boundedsum').checked)
        mconexao = "BDIF";


    if(document.getElementById('ativ_min').checked)
        mmetodo_ativ = "MIN";
    if(document.getElementById('ativ_prod').checked)
        mmetodo_ativ = "PROD";
    
    
    ClasseInterface.alteraMotor(mcodigo, mnome, mmetodo_defuz, mmetodo_agreg, mconexao, mmetodo_ativ, function(data){
        var str_location = document.location.toString();
        var location_separado = str_location.split("/");
        var destino = "";
        for(var i = 0; i < (location_separado.length - 1); i++){
            destino += location_separado[i] + "/";
        }
        destino += "projeto.xhtml";
        document.location = destino;
    });
}

function editaVariavelEntrada(array){    
    var vcodigo = array[0];
    var vnome = array[1];
    var vminimo = array[2];
    var vmaximo = array[3];
    var vunidade = array[4];
    
    document.getElementById('canvasmesa').style.display = "none";
    document.getElementById('editaEntrada').style.display = "inline";
    
    document.getElementById('tabView:entradaCodigo').value = vcodigo;
    document.getElementById('tabView:entradaNome').value = vnome;
    document.getElementById('tabView:entradaMinimo').value = vminimo;
    document.getElementById('tabView:entradaMaximo').value = vmaximo;
    document.getElementById('tabView:entradaUnidade').value = vunidade;   
}

function editaVariavelSaida(array){
    var vcodigo = array[0];
    var vnome = array[1];
    var vminimo = array[2];
    var vmaximo = array[3];
    var vunidade = array[4];
    
    document.getElementById('canvasmesa').style.display = "none";
    document.getElementById('editaSaida').style.display = "inline";
    
    document.getElementById('tabView:saidaCodigo').value = vcodigo;
    document.getElementById('tabView:saidaNome').value = vnome;
    document.getElementById('tabView:saidaMinimo').value = vminimo;
    document.getElementById('tabView:saidaMaximo').value = vmaximo;
    document.getElementById('tabView:saidaUnidade').value = vunidade;
}

function editaMotor(array){
    var mcodigo = array[0];
    var mnome = array[1];
    var mmetodo_defuz = array[2];
    var mmetodo_agreg = array[3];
    var mconexao = array[4];
    var mmetodo_ativ = array[5];
    
    document.getElementById('canvasmesa').style.display = "none";
    document.getElementById('editaMotor').style.display = "inline";
    
    document.getElementById('tabView:motorCodigo').value = mcodigo;
    document.getElementById('tabView:motorNome').value = mnome;
    
    if(mmetodo_defuz == 'COG')
        document.getElementById('cog').checked = true;
    if(mmetodo_defuz == 'RM')
        document.getElementById('rmm').checked = true;
    if(mmetodo_defuz == 'COA')
        document.getElementById('coa').checked = true;
    if(mmetodo_defuz == 'LM')
        document.getElementById('lmm').checked = true;
    
    
    if(mmetodo_agreg == 'BSUM')
        document.getElementById('boundedsum').checked = true;
    if(mmetodo_agreg == 'MAX')
        document.getElementById('max').checked = true;
    if(mmetodo_agreg == 'SUM')
        document.getElementById('sum').checked = true;
    if(mmetodo_agreg == 'NSUM')
        document.getElementById('normedsum').checked = true;
    
    
    if(mconexao == 'MIN')
        document.getElementById('and_min').checked = true;
    if(mconexao == 'PROD')
        document.getElementById('and_prod').checked = true;
    if(mconexao == 'BDIF')
        document.getElementById('and_boundedsum').checked = true;
    
    
    if(mmetodo_ativ == 'MIN')
        document.getElementById('ativ_min').checked = true;
    if(mmetodo_ativ == 'PROD')
        document.getElementById('ativ_prod').checked = true;
    
    //alert(mcodigo+"/"+mnome+"/"+mmetodo_defuz+"/"+mmetodo_agreg+"/"+mconexao+"/"+mmetodo_ativ);
}

function atualiza_interface2(data){
    
    array_entradas = new Array();
    array_saidas = new Array();
    array_motores = new Array();
    array_linhas = new Array();
    
    var string_entradas = "";
    var string_saidas = "";
    var string_motores = "";
    var string_linhas = "";
    
    var dataSeparado = data.split("|");
    string_entradas = dataSeparado[0];
    string_saidas = dataSeparado[1];
    string_motores = dataSeparado[2];
    string_linhas = dataSeparado[3];
    
    var entradas;
    entradas = string_entradas.split(";");
    
    if(entradas.length > 0)
        if(entradas[0] != "")
            for(var i = 0; i < entradas.length; i++){

                var entrada = entradas[i].split(",");
                var codigo = entrada[0];
                var nome = entrada[1];
                var minimo = entrada[2];
                var maximo = entrada[3];
                var unidade = entrada[4];
                var posx = entrada[5];
                var posy = entrada[6];

                array_entradas[i] = new matriz_entradas(codigo, nome, minimo, maximo, unidade, posx, posy);
            }
    
    
    var saidas;
    saidas = string_saidas.split(";");
    
    if(saidas.length > 0)
        if(saidas[0] != "")
            for(i = 0; i < saidas.length; i++){

                var saida = saidas[i].split(",");
                codigo = saida[0];
                nome = saida[1];
                minimo = saida[2];
                maximo = saida[3];
                unidade = saida[4];
                posx = saida[5];
                posy = saida[6];

                array_saidas[i] = new matriz_saidas(codigo, nome, minimo, maximo, unidade, posx, posy);
            }
    
    var motores;
    motores = string_motores.split(";");
    
    if(motores.length > 0)
        if(motores[0] != "")
            for(i = 0; i < motores.length; i++){
                var motor = motores[i].split(",");
                codigo = motor[0];
                nome = motor[1];
                posx = motor[2];
                posy = motor[3];

                array_motores[i] = new matriz_motores(codigo, nome, posx, posy);
            }
    
    var linhas;
    //alert(string_linhas);
    linhas = string_linhas.split(";");
    var contador = 0;
    for(i = 0; i < linhas.length; i++){
        var linha = linhas[i].split(",");
        var xmotor = linha[0];
        var ymotor = linha[1];
        
        for(var j = 2; j < 1000; j = j+2){
            if(linha[j] != null){
                var xv = linha[j];
                var yv = linha[j+1];
                array_linhas[contador] = new matriz_linhas(xmotor, ymotor, xv, yv);
                contador++;
                //alert(xmotor+" - "+ymotor+" - "+xv+" - "+yv);
            }
        }
        
    }
    
    campo = document.getElementById('campo_interface');
    canvas = document.getElementById('canvasmesa');
    canvas.width = canvas.width;
    canvas.height = canvas.height;
    context = canvas.getContext("2d");
    
    atualiza_canvas_linhas();
    atualiza_canvas_entradas();
    atualiza_canvas_saidas();
    atualiza_canvas_motores();
    cria_eventos();
    
}

function limpaCanvas(){
    canvas.width = canvas.width;
    canvas.height = canvas.height;
}


function recuperaDados(){
    //Faz uma chamada ao servidor informando que usuário está conectado em projeto X
    var temUsuario = false;
    var temProjeto = false;
    var codUsuario = -1;
    var codProjeto = -1;
    
    var usuarioMesa = document.getElementById('tabView:userLogadoMesa');
    var projetoMesa = document.getElementById('tabView:codigoProjeto');
    
    var usuarioExecucao = document.getElementById('tabView:userLogadoExecucao');
    var projetoExecucao = document.getElementById('tabView:codigoProjetoExecucao');
    
    var usuarioLog = document.getElementById('tabView:userLogadoLog');
    var projetoLog = document.getElementById('tabView:codigoProjetoLog');
    
    var usuarioOpcoes = document.getElementById('tabView:userLogadoOpcoes');
    var projetoOpcoes = document.getElementById('tabView:codigoProjetoOpcoes');
    
    var usuarioEdicao = document.getElementById('form:userLogadoEdicao');
    var projetoEdicao = document.getElementById('form:codigoProjetoEdicao');
    
    
    if(usuarioMesa != null || usuarioExecucao != null || usuarioLog != null || usuarioOpcoes != null || usuarioEdicao != null)
        temUsuario = true;
    if(projetoMesa != null || projetoExecucao != null || projetoLog != null || projetoOpcoes != null || projetoEdicao != null)
        temProjeto = true;
    
    
    
    if(temUsuario){
        if(usuarioMesa != null)
            codUsuario = document.getElementById('tabView:userLogadoMesa').value;
        if(usuarioExecucao != null)
            codUsuario = document.getElementById('tabView:userLogadoExecucao').value;
        if(usuarioLog != null)
            codUsuario = document.getElementById('tabView:userLogadoLog').value;
        if(usuarioOpcoes != null)
            codUsuario = document.getElementById('tabView:userLogadoOpcoes').value;
        if(usuarioEdicao != null)
            codUsuario = document.getElementById('form:userLogadoEdicao').value;
    }
    
    
    if(temProjeto){
        if(projetoMesa != null)
            codProjeto = document.getElementById('tabView:codigoProjeto').value;
        if(projetoExecucao != null)
            codProjeto = document.getElementById('tabView:codigoProjetoExecucao').value;
        if(projetoLog != null)
            codProjeto = document.getElementById('tabView:codigoProjetoLog').value;
        if(projetoOpcoes != null)
            codProjeto = document.getElementById('tabView:codigoProjetoOpcoes').value;
        if(projetoEdicao != null)
            codProjeto = document.getElementById('form:codigoProjetoEdicao').value;
    }
    
    ControlaConectados.request(codUsuario, codProjeto, function(data){
        
    });
    
    
    
    
    var temcanvas = document.getElementById('canvasmesa');
    if(temcanvas != null){
        codProjeto = document.getElementById('tabView:codigoProjeto').value;
        ClasseInterface.retornaComponentes(codProjeto, function(data){
            atualiza_interface2(data);
        });
    }
}



function atualiza_canvas_linhas(){
    for(var i = 0; i < array_linhas.length; i++){
        var x1 = array_linhas[i].x1;
        var y1 = array_linhas[i].y1;
        var x2 = array_linhas[i].x2;
        var y2 = array_linhas[i].y2;
        if(x1 > 690) x1 = 690;
        if(y1 > 440) y1 = 440;
        if(x2 > 690) x2 = 690;
        if(y2 > 440) y2 = 440;
        
        context.beginPath();
        context.moveTo(x1, y1);
        context.lineTo(x2, y2);
        context.lineWidth = 5;
        context.strokeStyle = 'grey';
        context.stroke();
    }
}

function atualiza_canvas_motores(){
    for(var i = 0; i < array_motores.length; i++){
        
        var nome = array_motores[i].nome;
        var posx = array_motores[i].posx;
        var posy = array_motores[i].posy;
        if(posx > 690) posx = 690;
        if(posy > 440) posy = 440;
        
        context.beginPath();
        context.rect(posx, posy, 90, 60);
        context.fillStyle = '#F5F5F5';
        context.fill();
        
        context.lineWidth = 5;
        context.strokeStyle = 'yellow';
        context.stroke();
        
        context.fillStyle = '#0A0A0A';
        
        if(nome.length > 12)
            nome = nome.substring(0, 9) + "...";
        if (nome.length > 9)
            context.font = '14px Calibri';
        else
            context.font = '18px Calibri';
        
        context.fillText(nome, (parseInt(posx) + 10), (parseInt(posy) + 20));
        
        context.beginPath();
        context.moveTo(parseInt(posx)+108, parseInt(posy)+3);
        context.lineTo(parseInt(posx)+101, parseInt(posy)+10);
        context.lineWidth = 3;
        context.strokeStyle = 'grey';
        context.stroke();
        context.beginPath();
        context.moveTo(parseInt(posx)+101, parseInt(posy)+10);
        context.lineTo(parseInt(posx)+98, parseInt(posy)+13);
        context.lineWidth = 3;
        context.strokeStyle = 'black';
        context.stroke();
        
        context.beginPath();
        context.moveTo(parseInt(posx)+98, parseInt(posy)+20);
        context.lineTo(parseInt(posx)+108, parseInt(posy)+30);
        context.moveTo(parseInt(posx)+98, parseInt(posy)+30);
        context.lineTo(parseInt(posx)+108, parseInt(posy)+20);
        context.fill();
        context.lineWidth = 3;
        context.strokeStyle = '#C81414';
        context.stroke();
        
        context.fillStyle = '#A0A000';
        context.font = '24px Comic Sans MS';
        context.fillText("M-->M", (parseInt(posx) + 10), (parseInt(posy) + 50));
    }
}

function atualiza_canvas_entradas(){
    for(var i = 0; i < array_entradas.length; i++){
        
        var posx = array_entradas[i].posx;
        var posy = array_entradas[i].posy;
        var nome = array_entradas[i].nome;
        var minimo = array_entradas[i].minimo;
        var maximo = array_entradas[i].maximo;
        var unidade = array_entradas[i].unidade;
        
        if(posx > 690) posx = 690;
        if(posy > 440) posy = 440;
        
        //DESENHA RETANGULO
        context.beginPath();
        var e = new Elemento(posx, posy, 90, 60);
        context.rect(e.x, e.y, e.w, e.h);
        context.fillStyle = '#F5F5F5';
        context.fill();
        context.lineWidth = 5;
        context.strokeStyle = 'green';
        context.stroke();
        
        //ESCREVE TEXTO
        context.fillStyle = '#0A0A0A';
        if(nome.length > 12)
            nome = nome.substring(0, 9) + "...";
        if (nome.length > 9)
            context.font = '14px Calibri';
        else
            context.font = '18px Calibri';
        context.fillText(nome, (parseInt(posx) + 10), (parseInt(posy) + 20));
        
        //DESENHA EDICAO
        context.beginPath();
        context.moveTo(parseInt(posx)+108, parseInt(posy)+3);
        context.lineTo(parseInt(posx)+101, parseInt(posy)+10);
        context.lineWidth = 3;
        context.strokeStyle = 'grey';
        context.stroke();
        context.beginPath();
        context.moveTo(parseInt(posx)+101, parseInt(posy)+10);
        context.lineTo(parseInt(posx)+98, parseInt(posy)+13);
        context.lineWidth = 3;
        context.strokeStyle = 'black';
        context.stroke();
        
        /*context.beginPath();
        context.rect(parseInt(posx)+98, parseInt(posy)+3, 10, 10);
        context.fillStyle = '#C8C8C8';
        context.fill();
        context.lineWidth = 1;
        context.strokeStyle = 'green';
        context.stroke();*/
        
        //DESENHA EXCLUSAO
        context.beginPath();
        context.moveTo(parseInt(posx)+98, parseInt(posy)+20);
        context.lineTo(parseInt(posx)+108, parseInt(posy)+30);
        context.moveTo(parseInt(posx)+98, parseInt(posy)+30);
        context.lineTo(parseInt(posx)+108, parseInt(posy)+20);
        context.fill();
        context.lineWidth = 3;
        context.strokeStyle = '#C81414';
        context.stroke();
        
        
        //DESENHA EFEITO
        context.beginPath();
        //Triângulo 1
        context.moveTo(parseInt(posx)+10, parseInt(posy)+50);
        context.lineTo(parseInt(posx)+25, parseInt(posy)+25);
        context.lineTo(parseInt(posx)+43, parseInt(posy)+50);
        //Triângulo 2
        context.moveTo(parseInt(posx)+30, parseInt(posy)+50);
        context.lineTo(parseInt(posx)+45, parseInt(posy)+25);
        context.lineTo(parseInt(posx)+60, parseInt(posy)+50);
        //Triângulo 3
        context.moveTo(parseInt(posx)+47, parseInt(posy)+50);
        context.lineTo(parseInt(posx)+65, parseInt(posy)+25);
        context.lineTo(parseInt(posx)+80, parseInt(posy)+50);
        
        context.lineWidth = 2;
        context.strokeStyle = '#003C00';
        context.stroke();
    }
    
}


function atualiza_canvas_saidas(){
    for(var i = 0; i < array_saidas.length; i++){
        var posx = array_saidas[i].posx;
        var posy = array_saidas[i].posy;
        var nome = array_saidas[i].nome;
        var minimo = array_saidas[i].minimo;
        var maximo = array_saidas[i].maximo;
        var unidade = array_saidas[i].unidade;
        if(posx > 690) posx = 690;
        if(posy > 440) posy = 440;
        
        context.beginPath();
        context.rect(posx, posy, 90, 60);
        context.fillStyle = '#F5F5F5';
        context.fill();
        
        context.lineWidth = 5;
        context.strokeStyle = 'red';
        context.stroke();
        
        context.fillStyle = '#0A0A0A';
        if(nome.length > 12)
            nome = nome.substring(0, 9) + "...";
        if (nome.length > 9)
            context.font = '14px Calibri';
        else
            context.font = '18px Calibri';
        context.fillText(nome, (parseInt(posx) + 10), (parseInt(posy) + 20));
        
        context.beginPath();
        context.moveTo(parseInt(posx)+108, parseInt(posy)+3);
        context.lineTo(parseInt(posx)+101, parseInt(posy)+10);
        context.lineWidth = 3;
        context.strokeStyle = 'grey';
        context.stroke();
        context.beginPath();
        context.moveTo(parseInt(posx)+101, parseInt(posy)+10);
        context.lineTo(parseInt(posx)+98, parseInt(posy)+13);
        context.lineWidth = 3;
        context.strokeStyle = 'black';
        context.stroke();
        
        context.beginPath();
        context.moveTo(parseInt(posx)+98, parseInt(posy)+20);
        context.lineTo(parseInt(posx)+108, parseInt(posy)+30);
        context.moveTo(parseInt(posx)+98, parseInt(posy)+30);
        context.lineTo(parseInt(posx)+108, parseInt(posy)+20);
        context.fill();
        context.lineWidth = 3;
        context.strokeStyle = '#C81414';
        context.stroke();
        
        context.beginPath();
        //Triângulo 1
        context.moveTo(parseInt(posx)+10, parseInt(posy)+50);
        context.lineTo(parseInt(posx)+25, parseInt(posy)+25);
        context.lineTo(parseInt(posx)+43, parseInt(posy)+50);
        //Triângulo 2
        context.moveTo(parseInt(posx)+30, parseInt(posy)+50);
        context.lineTo(parseInt(posx)+45, parseInt(posy)+25);
        context.lineTo(parseInt(posx)+60, parseInt(posy)+50);
        //Triângulo 3
        context.moveTo(parseInt(posx)+47, parseInt(posy)+50);
        context.lineTo(parseInt(posx)+65, parseInt(posy)+25);
        context.lineTo(parseInt(posx)+80, parseInt(posy)+50);
        context.lineWidth = 2;
        context.strokeStyle = '#640000';
        context.stroke();
    }
}