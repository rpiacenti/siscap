/**
 * Autor Ronald Piacenti Jr.
 */

function validaDia(pObj){
	var pVal = pObj;
	if(pVal.lenght > 2){
		document.getElementById("formCronograma:messages").value = "O dia deve conter até dois digitos.";
    }	
}

function checaPedidoCategoria() {
	var valPed = document.getElementById("formPedido:campoTipoPedido").value;
	var valCat = document.getElementById("formPedido:campoCategoria").value;

	if ((valPed != "") && (valCat != "")) {
		toggleOn("formPedido:Entrada-Item");
	} else {
		toggleOff("formPedido:Entrada-Item");
		document.getElementById("formPedido:messages").value = "Informe O Tipo de Pedido e/ou Categoria.";
		//toggleOn("formPedido:messages");
	}
}

function checaCD() {
	var valCD = document.getElementById("formGrupoAtendimento:campoCD").value;
	if (valCD != "") {
		toggleOn("formGrupoAtendimento:Entrada-Grupo");
		toggleOn("formGrupoAtendimento:Lista-Grupo");
	} else {
		toggleOff("formGrupoAtendimento:Entrada-Grupo");
		toggleOff("formGrupoAtendimento:Lista-Grupo");
		document.getElementById("formGrupoAtendimento:messages").value = "Selecione o Centro de Distribuição.";
		//toggleOn("formPedido:messages");
	}
	
}

function checaAno() {
	var val1 = document.getElementById("formCronograma:campoAno").value;
	var val2 = document.getElementById("formCronograma:campoGRP").value;
	//alert('Valores: '+val1+ ' - ' + val2);
	//alert( val1.selectedindex + ' - '+ val2.length);
	
	if (val1!= "" && val2 != "") {
		toggleOn("formCronograma:Lista-Crono");
	} else {
		toggleOff("formCronograma:Lista-Crono");
		document.getElementById("formCronograma:messages").value = "Selecione o ano do cronograma.";
		//toggleOn("formPedido:messages");
	}
	
}




function toggleOn(id) {
	var element = document.getElementById(id);

	element.style.display = 'block';

}

function toggleOff(id) {
	var element = document.getElementById(id);

	element.style.display = 'none';
}




function validaData(pData) {
	aleret("Passei!");
	var date=pData;
	var ardt=new Array;
	var ExpReg=new RegExp("(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[12][0-9]{3}");
	ardt=date.split("/");
	erro=false;
	if ( date.search(ExpReg)==-1){
		erro = true;
		}
	else if (((ardt[1]==4)||(ardt[1]==6)||(ardt[1]==9)||(ardt[1]==11))&&(ardt[0]>30))
		erro = true;
	else if ( ardt[1]==2) {
		if ((ardt[0]>28)&&((ardt[2]%4)!=0))
			erro = true;
		if ((ardt[0]>29)&&((ardt[2]%4)==0))
			erro = true;
	}
	if (erro) {
		alert(pData + " não é uma data válida!!!");
		campo.value = "";
		return false;
	}
	return true;
}


PrimeFaces.locales['pt_BR'] = {
                closeText: 'Fechar',
                prevText: 'Anterior',
                nextText: 'Próximo',
                currentText: 'Começo',
                monthNames: ['Janeiro','Fevereiro','Março','Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
                monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun', 'Jul','Ago','Set','Out','Nov','Des'],
                dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado'],
                dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb'],
                dayNamesMin: ['D','S','T','Q','Q','S','S'],
                weekHeader: 'Semana',
                firstDay: 1,
                isRTL: false,
                showMonthAfterYear: false,
                yearSuffix: '',
                timeOnlyTitle: 'Só Horas',
                timeText: 'Tempo',
                hourText: 'Hora',
                minuteText: 'Minuto',
                secondText: 'Segundo',
                currentText: 'Data Atual',
                ampm: false,
                month: 'Mês',
                week: 'Semana',
                day: 'Dia',
                allDayText : 'Todo Dia'
            };


