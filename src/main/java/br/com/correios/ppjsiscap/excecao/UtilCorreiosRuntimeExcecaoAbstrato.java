package br.com.correios.ppjsiscap.excecao;






/**
 * Classe utilitária para montagem das exceções runtime.
 * 
 * @author Arivaldo Junior
 */
public abstract class UtilCorreiosRuntimeExcecaoAbstrato {
	
	private static UtilExcecao utilExcecao = new UtilExcecao();
	
	/**
	 * Cria uma CorreiosRuntimeException da aplicacao.
	 * 
	 * @param key chave da mensagem de Excecao.
	 * @param arg0 argumento da mensagem.
	 * @return RuntimeExceptionAbstrato
	 */
	protected static  CorreiosRuntimeException novaExcecao(String key, String... argumentos) {		
		CorreiosRuntimeException e = new CorreiosRuntimeException();
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
	protected static  CorreiosRuntimeException novaExcecao(Throwable excecao) {
		String mensagem = utilExcecao.getMensagem(excecao);
		CorreiosRuntimeException e = new CorreiosRuntimeException(mensagem);
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
	protected static  CorreiosRuntimeException novaExcecao(Throwable excecao,
			String key, String... args) {		
		CorreiosRuntimeException e = novaExcecao(excecao);
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
