package br.com.correios.siscap.excecao;

import javax.enterprise.context.ApplicationScoped;

import br.com.correios.ppjsiscap.excecao.CorreiosException;
import br.com.correios.ppjsiscap.excecao.UtilCorreiosExceptionAbstrato;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.constantes.MensagemID;

/**
 * Tratamento das exceções de Sistema. As mensagens de erro são mapeadas no
 * arquivo mensagens-excecao.properties.
 * @author ArivaldoJunior
 *
 */
@SuppressWarnings("serial")
@ApplicationScoped
public class UtilCorreiosException extends UtilCorreiosExceptionAbstrato{
	
	
	/**
	 * Exceção lançada para mensagens personalizadas.
	 * Passar no parâmetro do método a chave referente a mensagem no arquivo mensagens-excecao.properties.
	 * @param key - chave do properties
	 * @param parametos  - argumentos da mensagem - texto que substituirá o {numero} no properties
	 * @return CorreiosException
	 * 
	 */
	public  CorreiosException exibirMensagem(String key, String ... parametros) {		
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
	public  CorreiosException exibirMensagem(Exception e,String key, String ... parametros) {		
		return converter(novaExcecao(e,key, parametros));
	}
	
	/**
	 * Converte para CorreiosException.
	 * 
	 * @param e
	 *            RuntimeExceptionAbstrato
	 * @return InfraRuntimeException
	 */
	private  CorreiosException converter(Throwable e) {
		Throwable excecao = null;
		if(!e.getClass().isAssignableFrom(CorreiosException.class)){
			 excecao = new CorreiosException(e);			
		}else{
			excecao = e;
		}
		UtilLog.logger.warn("Ocorreu um erro: "+((CorreiosException) excecao).getKey()+" "+((CorreiosException) excecao).getArgs(), excecao);
		return (CorreiosException) excecao;
	}

	/**
	 * 
	 * @param e
	 * @return CorreiosException
	 */
	public  CorreiosException erro(Throwable e) {
		String chave = MensagemID.ERRO;
		String mensagem = getMensagem(e);
		return converter(novaExcecao(e, chave, mensagem));
	}
	
	
	
}
