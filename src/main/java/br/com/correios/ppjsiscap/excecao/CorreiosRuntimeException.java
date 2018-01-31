package br.com.correios.ppjsiscap.excecao;

import java.util.Arrays;

/**
 * 
 * @author 80131085
 * 
 */
@SuppressWarnings("serial")
public class CorreiosRuntimeException extends RuntimeException {

	private String key;

	private String[] args;

	/**
	 * Construtor.
	 */
	public CorreiosRuntimeException() {
	}

	/**
	 * Construtor.
	 * 
	 * @param key
	 *            chave
	 */
	public CorreiosRuntimeException(String key) {
		super(key);
	}

	/**
	 * Construtor.
	 * 
	 * @param excecao
	 *            exceção
	 */
	public CorreiosRuntimeException(Throwable excecao) {
		super(excecao.getMessage());
		super.initCause(excecao.getCause());
	}

	/**
	 * Construtor.
	 * 
	 * @param key
	 *            chave
	 * @param arg0
	 *            argumento 0
	 */
	public CorreiosRuntimeException(String key, String... args) {
		super(key);
		setKey(key);
		setArgs(args);
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the args
	 */
	public String[] getArgs() {
		return args;
	}

	/**
	 * @param stringArgs
	 *            the args to set
	 */
	public void setArgs(String[] stringArgs) {
		if (stringArgs == null) {
			this.args = new String[0];
		} else {
			this.args = Arrays.copyOf(stringArgs, stringArgs.length);
		}
	}
}
