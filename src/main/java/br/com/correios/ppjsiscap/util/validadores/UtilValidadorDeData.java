package br.com.correios.ppjsiscap.util.validadores;

import br.com.correios.ppjsiscap.util.UtilData;
import br.com.correios.ppjsiscap.util.formatador.UtilPatternsDeData;


/**
 * Validador de data.
 * 
 * @author arivaldojunior
 */
public final class UtilValidadorDeData {

	private UtilData utilData = new UtilData();

	/**
	 * Retorna true se a data for válida.
	 * 
	 * @param data Data no formato dd/MM/yyyy.
	 * @return true se a data for válida.
	 */
	public  boolean validar(String data) {
		return isDataValida(data, UtilPatternsDeData.ddMMyyyy);
	}

	/**
	 * Retorna true se a data for válida.
	 * 
	 * @param data Data no formato definido no pattern.
	 * @param pattern Pattern da data.
	 * @return true se a data for válida.
	 */
	public  boolean validar(String data, String pattern) {
		return isDataValida(data, pattern);
	}

	/**
	 * Retorna true se a data for válida no formato MM/yyyy.
	 * 
	 * @param data Data no formato MM/yyyy.
	 * @return true se a data for válida.
	 */
	// CHECKSTYLE:OFF
	public  boolean validar_MMyyyy(String data) {
		return isDataValida(data, UtilPatternsDeData.MMyyyy);
	}
	// CHECKSTYLE:ON

	/**
	 * Retorna true se a data for válida.
	 * 
	 * @param data Data no formato definido no pattern.
	 * @param pattern Pattern da data.
	 * @return true se a data for válida.
	 */
	private  boolean isDataValida(String data, String pattern) {
		boolean res = false;
		if (!"".equals(data) && !"".equals(pattern)) {
			res = (utilData.parse(data, pattern) != null);
		}
		return res;
	}
}
