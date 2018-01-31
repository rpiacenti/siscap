package br.com.correios.ppjsiscap.components;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;


@FacesComponent("br.com.correios.ppjsistema.components.Mensagens")
public class MensagensComponent extends UINamingContainer {
	
	private List<FacesMessage> mensagensGlobais;
	
	public MensagensComponent(){}
	


	/**
	 * @param mensagensGlobais the mensagensGlobais to set
	 */
	public void setMensagensGlobais(List<FacesMessage> mensagensGlobais) {
		this.mensagensGlobais = mensagensGlobais;
	}


	/**
	 * @return the mensagensGlobais
	 */
	public List<FacesMessage> getMensagensGlobais() {
		mensagensGlobais = FacesContext.getCurrentInstance().getMessageList(null);
		return mensagensGlobais;
	}
	
	
	
	
}
