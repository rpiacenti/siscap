package br.com.correios.ppjsiscap.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class NotaInvalida extends Exception {
	 public NotaInvalida(String mensagem) {
         super(mensagem);
         FacesMessage msgerr = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro no envio de Email", "Erro de Email: " + mensagem);
		FacesContext.getCurrentInstance().addMessage(null, msgerr);
	 }
}
