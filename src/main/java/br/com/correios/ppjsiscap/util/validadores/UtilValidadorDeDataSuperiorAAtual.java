package br.com.correios.ppjsiscap.util.validadores;

import java.util.Date;

/**
 * Validador de data superior à data atual.
 * 
 * @author arivaldojunior
 */
public final class UtilValidadorDeDataSuperiorAAtual  {

	

	/**
	 * Retorna true se a data informada for maior que a data atual.
	 * 
	 * @param data
	 *            Data validada
	 * @return true se a data informada for maior que a data atual.
	 */
	public  boolean validar(Date data) {
		boolean result = false;

		if (data != null) {
			Date dataAtual = new Date();
			int comparacaoInicial = data.compareTo(dataAtual);
			result = (comparacaoInicial > 0);
		}

		return result;
	}
}
