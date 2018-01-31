package br.com.correios.siscap.managedbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import br.com.correios.ppjsiscap.enums.TipoAcao;
import br.com.correios.ppjsiscap.paginador.Paginador;
import br.com.correios.ppjsiscap.util.UtilMensagem;


/**
 * 
 * @author ArivaldoJunior
 *
 */
@SuppressWarnings("serial")
public abstract class MB implements Serializable{
	
	
	
	@Inject
	protected UtilMensagem utilMensagem;
	
	
    /**
     * Retorna o mapa dos atributos da sessão ativa 
     * @return  Map<String,Object>
     */
	public Map<String,Object> getSessionMap(){
		return getContext().getExternalContext().getSessionMap();
	}

    /**
	* Retorna o mapo de parâmetros do request
	* @return Map<String, String>
	*/

	public Map<String, String> getRequestParameterMap() {
		return getContext().getExternalContext().getRequestParameterMap();
	}
	
	/**
	 * Recupera o contexto ativo.
	 * @return FacesContext
	 */
	protected FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}
	
	/**
	 * Recupera o caminho físico da aplicação.
	 * Pode ser passado um parâmetro para complemento do caminho a partir da raiz.
	 * @param String 
	 * @return String
	 */
	protected String getRealPath(String complementoDoCaminho) {
		ServletContext servletContext = (ServletContext) getContext().getExternalContext().getContext();
		return servletContext.getRealPath("/" + complementoDoCaminho);
	}
	
	
	
	/**
	 * Adiciona uma mensagem para ser exibida no componente message do jsf.
	 * FacesMessage.SEVERITY_WARN
	 * @param idComponent
	 * @param key para o arquivo properties
	 */
	protected void adicionaMensagemAlerta(String idComponent, String key , String ...args){
		String mensagem = utilMensagem.getMensagem(
				FacesContext.getCurrentInstance().getViewRoot().getLocale(),key, args);
		FacesContext.getCurrentInstance().addMessage(
				idComponent, new FacesMessage(FacesMessage.SEVERITY_WARN, mensagem, 
						""));
	}
	
	/**
	 * Adiciona uma mensagem para ser exibida no componente message do jsf.
	 * FacesMessage.SEVERITY_INFO
	 * @param idComponent
	 * @param key para o arquivo properties
	 */
	protected void adicionaMensagemInfo(String idComponent, String key, String ...args ){
		String mensagem = utilMensagem.getMensagem(
				FacesContext.getCurrentInstance().getViewRoot().getLocale(),key, args);
		FacesContext.getCurrentInstance().addMessage(
				idComponent, new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, 
						""));
	}
	
	/**
	 * Adiciona uma mensagem para ser exibida no componente message do jsf. 
	 * Exibe o icone de sucesso
	 * @param idComponent
	 * @param key para o arquivo properties
	 */
	protected void adicionaMensagemSucesso(String idComponent, String key, String ...args ){
		String mensagem = utilMensagem.getMensagem(
				FacesContext.getCurrentInstance().getViewRoot().getLocale(),key, args);
		FacesContext.getCurrentInstance().addMessage(
				idComponent, new FacesMessage(FacesMessage.SEVERITY_FATAL,mensagem, ""));
	}
	
	/**
	 * Adiciona uma mensagem para ser exibida no componente message do jsf.
	 * FacesMessage.SEVERITY_ERROR
	 * @param idComponent
	 * @param key
	 * @param arg parametro para acrescentar na mensagem
	 */
	protected void adicionaMensagemErro(String idComponent, String key, String ... args){
		String mensagem = utilMensagem.getMensagem(
				FacesContext.getCurrentInstance().getViewRoot().getLocale(),key, args);
		FacesContext.getCurrentInstance().addMessage(
				idComponent, new FacesMessage(FacesMessage.SEVERITY_ERROR,mensagem,""));
	}
	
	
	
	/**
	 * Retorna um ValidatorException com a mensagem da chave passada por parametro.
	 * @param msgKey
	 * @param arg - parametro que sera inserido na mensagem mapeada no properties
	 * @return
	 */
	protected ValidatorException novoValidatorException(String msgKey, String ... args){
		String mensagem = utilMensagem.getMensagem(
				FacesContext.getCurrentInstance().getViewRoot().getLocale(),msgKey, args);
		return new ValidatorException(novoFacesMessage(mensagem));
	}
	
	/**
	 * @return the FacesMessage
	 */
	private FacesMessage novoFacesMessage(String msg){
		return new FacesMessage(msg);
	}

	
	
	/**
	 * Seta o Locale do sistema para ingles.
	 * Este metodo pode ser chamado do xhtml, de preferencia do icone que representa o uso da lingua inglesa no site.
	 * @return
	 * @throws IOException 
	 */
	public String setLocaleIngles() throws IOException{
		getContext().getViewRoot().setLocale(Locale.US);
		return null;
	}
	
	/**
	 * Seta o Locale do sistema para portugues.
	 * Este metodo pode ser chamado do xhtml, de preferencia do icone que representa o uso da lingua portuguesa no site.
	 * @return
	 * @throws IOException 
	 */
	public String setLocalePortugues() throws IOException{
		getContext().getViewRoot().setLocale(Locale.getDefault());
		return null;
	}
	
	/**
	 * Seta o Locale do sistema para espanhol.
	 * Este metodo pode ser chamado do xhtml, de preferencia do icone que representa o uso da lingua espanhola no site.
	 * @return
	 * @throws IOException 
	 */
	public String setLocaleEspanhol() throws IOException{
		getContext().getViewRoot().setLocale(Locale.getDefault());
		return null;
	}
	
	/**
	 * Este método serve para atualizar a coleção de registros por página que existe dentro do próprio paginador.
	 * Usado apenas para paginas coleção na memória
	 * @return
	 * @throws  
	 */
	public Paginador atualizaPaginador(int paginaAtual, Paginador p) {
		
		p.getColecaoDeRegistrosPorPagina().clear();
		
		int inicioLoop = (paginaAtual-1) * p.getQuantidadeDeRegistrosPorPagina();
		for (int i=inicioLoop; i<inicioLoop+p.getQuantidadeDeRegistrosPorPagina(); i++){
			if (i >= p.getColecaoDeRegistros().size())
				break;
			p.getColecaoDeRegistrosPorPagina().add(p.getColecaoDeRegistros().get(i));
		}
		p.setPaginaAtual(paginaAtual);
		return p;
	}
	
	/**
	 * Este método serve para movimentar os itens dentro do componente picklist.
	 * @return
	 * @throws  
	 */
	public void movimentarItensPL(TipoAcao acao, List<SelectItem> itensDisponiveis, List<SelectItem> itensAtribuidos, List<SelectItem> itensAuxiliares, 
			List<String> itensSelecionadosOrigem, List<String> itensSelecionadosDestino) {
		
    	boolean gravou;
    	switch(acao) {
		case A: {
			itensAuxiliares.clear();
			for (SelectItem s: itensDisponiveis) {
				gravou = false;
				for (String codigo: itensSelecionadosOrigem) {
					if (s.getValue().equals(codigo)) {
						itensAtribuidos.add(s);
						gravou = true;
					}
				}
				if (!gravou)
					itensAuxiliares.add(s);
			}
			itensDisponiveis.clear();
			for (SelectItem s: itensAuxiliares) {
				itensDisponiveis.add(s);
			}
			itensSelecionadosOrigem.clear();
			break;
		}
		case AT: {
			itensAtribuidos.addAll(itensDisponiveis);
			itensDisponiveis.clear();
			break;
		}
		case R: {
			itensAuxiliares.clear();
			for (SelectItem s: itensAtribuidos) {
				gravou = false;
				for (String codigo: itensSelecionadosDestino) {
					if (s.getValue().equals(codigo)) {
						itensDisponiveis.add(s);
						gravou = true;
					}
				}
				if (!gravou)
					itensAuxiliares.add(s);
			}
			itensAtribuidos.clear();
			for (SelectItem s: itensAuxiliares) {
				itensAtribuidos.add(s);
			}
			itensSelecionadosDestino.clear();
			break;
		}
		case RT: {
			itensDisponiveis.addAll(itensAtribuidos);
			itensAtribuidos.clear();
			break;
		}
		default:
			break;
		}
	}
	
	/**
	 * Este método serve para adicionar os itens dentro da lista de destino do componente picklist.
	 * @return
	 * @throws  
	 */
	public void adicionarDblClickPL(List<SelectItem> itensDisponiveis, List<SelectItem> itensAtribuidos, 
			List<String> itensSelecionadosOrigem) throws AbortProcessingException {
		
    	boolean sair = false;
    	for (SelectItem s: itensDisponiveis) {
			for (String codigo: itensSelecionadosOrigem) {
				if (s.getValue().equals(codigo)) {
					itensDisponiveis.remove(s);
					itensAtribuidos.add(s);
					sair = true;
					break;
				}
			}
			if (sair)
				break;
		}
		itensSelecionadosOrigem.clear();
    }
    
	/**
	 * Este método serve para remover os itens da lista de destino do componente picklist.
	 * @return
	 * @throws  
	 */
    public void removerDblClickPL(List<SelectItem> itensDisponiveis, List<SelectItem> itensAtribuidos, 
			List<String> itensSelecionadosDestino) throws AbortProcessingException {
    	boolean sair = false;
    	for (SelectItem s: itensAtribuidos) {
			for (String codigo: itensSelecionadosDestino) {
				if (s.getValue().equals(codigo)) {
					itensAtribuidos.remove(s);
					itensDisponiveis.add(s);
					sair = true;
					break;
				}
			}
			if (sair)
				break;
		}
		itensSelecionadosDestino.clear();
    }
	

}
