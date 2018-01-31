package br.com.correios.ppjsiscap.excecao;

import java.util.Arrays;

import javax.ejb.ApplicationException;

/**
 * Classe que representa uma exceção lançada pela infra estrutura.
 * 
 * @author Arivaldo Junior
 */
@SuppressWarnings("serial")
@ApplicationException(rollback = true)
public class CorreiosException extends Exception {

	private String key;

	private String[] args;

	/**
	 * Construtor.
	 */
	public CorreiosException() {
	}

	/**
	 * Construtor.
	 * 
	 * @param excecao
	 *            exceção
	 */
	public CorreiosException(Throwable excecao) {
		super(excecao.getMessage());
		super.initCause(excecao.getCause());
		setKey("erro");
		setArgs(new String[] { excecao.getMessage() });
	}

	/**
	 * Construtor.
	 * 
	 * @param key
	 *            chave
	 * @param arg0
	 *            argumento 0
	 */
	public CorreiosException(String key, String... args) {
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
