package br.com.correios.ppjsiscap.excecao;

import java.io.Serializable;





/**
 * Classe utilitária para montagem das exceções.
 * @author ArivaldoJunior
 *
 */
@SuppressWarnings("serial")
public abstract class UtilCorreiosExceptionAbstrato implements Serializable{
	
	private static UtilExcecao utilExcecao = new UtilExcecao();
	

	/**
	 * Cria uma Excecao da aplicacao.
	 * 
	 * @param key chave da mensagem de Excecao.
	 * @param arg0 argumento da mensagem.
	 * @return RuntimeExceptionAbstrato
	 */
	protected static  CorreiosException novaExcecao(String key, String... argumentos) {		
		CorreiosException e = new CorreiosException();
		e.setKey(key);
		e.setArgs(argumentos);		
		return e;
	}

	/**
	 * Retorna ExceptionAbstrato.
	 * 
	 * @param excecao Excecao causadora.
	 * @return ExceptionAbstrato
	 */
	protected static  CorreiosException novaExcecao(Throwable excecao) {
		String mensagem = utilExcecao.getMensagem(excecao);
		CorreiosException e = new CorreiosException(mensagem);
		e.initCause(excecao);
		return e;
	}


	/**
	 * Cria uma Excecao da aplicacao.
	 * 
	 * @param excecao Throwable Excecao causadora.
	 * @param key chave da mensagem de Excecao.
	 * @param arg0 argumento da mensagem.
	 * @return ExceptionAbstrato
	 */
	protected static  CorreiosException novaExcecao(Throwable excecao,
			String key, String... args) {		
		CorreiosException e = novaExcecao(excecao);
		e.setKey(key);
		e.setArgs(args);
		return e;
	}

	
	/**
	 * Recupera a mensagem de uma Excecao.
	 * 
	 * @param e Throwable
	 * @return String da mensagem.
	 */
	protected static String getMensagem(Throwable e) {
		return utilExcecao.getMensagem(e);
	}


}
