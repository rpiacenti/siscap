package br.com.correios.siscap.excecao;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class SiscapException extends Exception {

	private static final long serialVersionUID = 1371204821566933039L;

	public SiscapException(String mensagem) {
         super(mensagem);
         FacesMessage msgerr = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Informação de Erro", "Erro: " + mensagem);
		FacesContext.getCurrentInstance().addMessage(null, msgerr);
	 }
}
