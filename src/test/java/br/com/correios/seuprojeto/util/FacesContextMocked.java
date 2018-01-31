package br.com.correios.seuprojeto.util;

import java.util.Iterator;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;


public class FacesContextMocked extends FacesContext {

	
	private ExternalContext externalContext;

	private String mensagem;
	
	private UIViewRoot uiViewRoot;
	
	@Override
	public void addMessage(String idComponent, FacesMessage facesMessage) {
		this.mensagem = facesMessage.getSummary();
	}

	@Override
	public Application getApplication() {
		return null;
	}

	@Override
	public Iterator<String> getClientIdsWithMessages() {
		return null;
	}

	@Override
	public Severity getMaximumSeverity() {
		return null;
	}

	@Override
	public Iterator<FacesMessage> getMessages() {
		return null;
	}

	@Override
	public Iterator<FacesMessage> getMessages(String arg0) {
		return null;
	}

	@Override
	public RenderKit getRenderKit() {
		return null;
	}

	@Override
	public boolean getRenderResponse() {
		return false;
	}

	@Override
	public boolean getResponseComplete() {
		return false;
	}

	@Override
	public ResponseStream getResponseStream() {
		return null;
	}

	@Override
	public ResponseWriter getResponseWriter() {
		return null;
	}

	
	@Override
	public void release() {
	}

	@Override
	public void renderResponse() {
	}

	@Override
	public void responseComplete() {
	}

	@Override
	public void setResponseStream(ResponseStream arg0) {
	}

	@Override
	public void setResponseWriter(ResponseWriter arg0) {
	}
	

	@Override
	public UIViewRoot getViewRoot() {
		return this.uiViewRoot;
	}

	@Override
	public void setViewRoot(UIViewRoot uiViewRoot) {
		this.uiViewRoot = uiViewRoot;		
	}

	public ExternalContext getExternalContext() {
		return externalContext;
	}

	public void setExternalContext(ExternalContext externalContext) {
		this.externalContext = externalContext;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
		
	
}