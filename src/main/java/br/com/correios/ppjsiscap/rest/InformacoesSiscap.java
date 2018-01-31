package br.com.correios.ppjsiscap.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import br.com.correios.ppjsiscap.beans.Informacao;


@Path("/informacoes")
public class InformacoesSiscap {
	@GET
	@Produces("application/json")
	public Response getSobre(){
		Informacao info = new Informacao();
		info.setCabecalho("Sistema de Captação de Pedidos - SISCAP");
		info.setParagrafos(
				new String [] {"O Sistema corporativo desenvolvido com o objetivo de aperfeiçoar o processo de suprimento, possibilitando o cadastramento e o acompanhamento dos pedidos de materiais de consumo e produtos por todas as unidades próprias e terceirizadas dos Correios. Em caso de dúvidas que não sejam esclarecidas pelo manual do usuário ou problemas  com seu acesso ou utilização, cadastre uma interação no Help Desk dos Correios."});
		info.setLista(new String [] {""});
		return Response.ok(info).build();
	}

}
