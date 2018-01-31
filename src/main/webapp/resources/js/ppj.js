$(document)
//controla o foco nas linhas das tabelas.
.on("mouseenter", "table tr", function(){$(this).addClass("focused");})
.on("mouseleave", "table tr", function(){$(this).removeClass("focused");})
//efeito azul do contorno dos inputs
.on("focus", "input[type='text'], select", function(){$(this).addClass("focused");})
.on("blur", "input[type='text'], select", function() {$(this).removeClass("focused");})
//campo só inteiros
.on("keypress keyup", ".numerico",
	function (e) {
		var re = /[^0-9^\x00-\x1F\x7F]/g;
		$(this).val($(this).val().replace(re,''));
})
//Example of preserving a JavaScript event for inline calls.
.on("click","#click",function(){
	$(this).css({"background-color":"#f00", "color":"#fff", "cursor":"inherit"}).text("Open this window again and this message will still be here.");
	return false;
	})
//aumentando a fonte	
.on("click","#biggerFont",function(){
	var size = $("body").css('font-size');
	
	size = size.replace('px', '');
	size = parseInt(size) + 1.4;
	
	$("body").animate({'font-size' : size + 'px'});
})
//diminuindo a fonte
.on("click","#smallerFont",function(){
	var size = $("body").css('font-size');
	
	size = size.replace('px', '');
	size = parseInt(size) - 1.4;
	
	$("body").animate({'font-size' : size + 'px'});
})
//resetando a fonte
.on("click","#defaultFont",function(){
	$("body").animate({'font-size' : '10px'});
})
//adicionando ou removendo contrast
.on("click","#contrast",function(){
	if ($("body").hasClass('contrast')){
		$("body").removeClass('contrast');
	}else{
		$("body").addClass('contrast');		
	}	
})
//mascaras
.on('focus','.data',function(){$(this).unmask();$(this).mask('99/99/9999');})
.on('focus','.matricula',function(){$(this).unmask();$(this).mask('9.999.999-9');})
.on('focus','.telefone',function(){$(this).unmask();$(this).mask('9999-9999');});

/** Executa javascript na chamada de f:ajax
Argumentos:
    1 - data  : informações da operação ajax.
    2 - status: quando executar o javascript: antes ou depois do processamento ajax. Valores
		    possíveis:
		        "begin"    - invocado antes da requisição ajax ser enviada.
		        "complete" -  invocado logo após a resposta ajax for retornada.
		        "success"  - invocado logo após o processamento com sucesso da resposta ajax e atualização do HTML DOM.                    
			 NOTA:
			    O parâmetro data e os valores utilizados para parâmetro status fazem parte da especificação/implementação do jsf 2.0
			    (http://download.oracle.com/otn-pub/jcp/jsf-2.0-fr-eval-oth-JSpec/jsf-2_0-fr-all-spec.zip)
               
    3 - operacao: operação javascript a ser executada  */
function manipulaAjax(data,status,operacao){
if (data.status===status) eval(operacao);
}

//exemplo de chamada da função: <input ... onkeyup="somente_caracteres(this, '0123456789');" />
function somente_caracteres(campo, caracteres){  
	var campo_temp;
    for (var i=0;i<campo.value.length;i++){  
        campo_temp=campo.value.substring(i,i+1);   
        if (caracteres.indexOf(campo_temp)==-1){  
            campo.value = campo.value.substring(0,i);  
        }  
    }  
}

function posicionaStatus(source, ajusteX, ajusteY){
//coordenadas em relacao ao elemento pai
var coordenadas = $(source).offset();                               
$("#ajaxStatusPanel").css({left:coordenadas.left + $(source).width()-ajusteX,top:coordenadas.top-ajusteY});
};

var exibeStatusProximoDoElemento = function manipula(data){manipulaAjax(data,'begin','posicionaStatus(data.source,281,163);');};

//modal
$(document).ready(function(){
	//Examples of how to assign the ColorBox event to elements
	$(".group1").colorbox({rel:'group1'});
	$(".group2").colorbox({rel:'group2', transition:"fade"});
	$(".group3").colorbox({rel:'group3', transition:"none", width:"75%", height:"75%"});
	$(".group4").colorbox({rel:'group4', slideshow:true});
	$(".ajax").colorbox();
	$(".youtube").colorbox({iframe:true, innerWidth:425, innerHeight:344});
	$(".iframe").colorbox({iframe:true, width:"80%", height:"80%"});
	$(".inline").colorbox({inline:true, width:"742px"});
	$(".inline2").colorbox({inline:true, width:"350px"});
	$(".callbacks").colorbox({
		onOpen:function(){ alert('onOpen: colorbox is about to open'); },
		onLoad:function(){ alert('onLoad: colorbox has started to load the targeted content'); },
		onComplete:function(){ alert('onComplete: colorbox has displayed the loaded content'); },
		onCleanup:function(){ alert('onCleanup: colorbox has begun the close process'); },
		onClosed:function(){ alert('onClosed: colorbox has completely closed'); }
	});	
});