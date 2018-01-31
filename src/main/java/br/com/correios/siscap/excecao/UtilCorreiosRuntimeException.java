package br.com.correios.siscap.excecao;

import javax.enterprise.context.ApplicationScoped;

import br.com.correios.ppjsiscap.excecao.CorreiosRuntimeException;
import br.com.correios.ppjsiscap.excecao.UtilCorreiosRuntimeExcecaoAbstrato;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.constantes.MensagemID;

/**
 * Tratamento das exceções de Sistema. As mensagens de erro são mapeadas no
 * arquivo mensagens-excecao.properties.
 * 
 * @author Arivaldo Junior
 */
@ApplicationScoped
public class UtilCorreiosRuntimeException extends UtilCorreiosRuntimeExcecaoAbstrato {
	

	/**
	 * @param e  Excecao
	 * @return CorreiosRuntimeException
	 */
	public  CorreiosRuntimeException erro(Throwable e) {
		String chave = MensagemID.ERRO;
		String mensagem = getMensagem(e);
		return converter(novaExcecao(e, chave, mensagem));
	}


	/**
	 * Exceção lançada para mensagens personalizadas.
	 * Passar no parâmetro do método a chave referente a mensagem no arquivo mensagens-excecao.properties.
	 * @param key - chave do properties
	 * @param parametos  - argumentos da mensagem - texto que substituir� o {numero} no properties
	 * @return CorreiosException
	 * 
	 */
	public  CorreiosRuntimeException exibirMensagem(String key, String ... parametros) {		
		return converter(novaExcecao(key, parametros));
	}
	
	/**
	 * Exceção lançada para mensagens personalizadas.
	 * Passar no parâmetro do método a chave referente a mensagem no arquivo mensagens-excecao.properties.
	 * @param e - Exceção capturada para exibição das causas.
	 * @param key - chave do properties
	 * @param parametos  - argumentos da mensagem - texto que substituirá o {numero} no properties
	 * @return CorreiosException
	 */
	public  CorreiosRuntimeException exibirMensagem(Exception e,String key, String ... parametros) {		
		return converter(novaExcecao(e,key, parametros));
	}

	
		
	/**
	 * Converte para CorreiosRuntimeException.
	 * 
	 * @param e Throwable
	 * @return CorreiosRuntimeException
	 */
	private  CorreiosRuntimeException converter(Throwable e) {
		CorreiosRuntimeException excecao = new CorreiosRuntimeException(e);
		UtilLog.logger.warn("Ocorreu um erro: "+excecao.getKey()+" "+excecao.getArgs(), excecao);
		return excecao;
	}

		

}
