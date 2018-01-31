package br.com.correios.ppjsiscap.util.validadores;

import java.io.Serializable;
import java.util.Date;

/**
 * Validador que verifica se a data inicial é maior que a data final.
 * 
 * @author arivaldojunior
 */
@SuppressWarnings("serial")
public final class UtilValidadorDeDataFinalMaiorQueDataInicial implements Serializable{

	/**
	 * Construtor.
	 */
	private UtilValidadorDeDataFinalMaiorQueDataInicial() {
		// Construtor.
	}

	/**
	 * Retorna true se a data final for maior que a data inicial. As datas são
	 * obrigatórias.
	 * 
	 * @param dataInicial
	 *            Data inicial.
	 * @param dataFinal
	 *            Data final.
	 * @return true se a data final for maior que a data inicial.
	 */
	public boolean validar(Date dataInicial, Date dataFinal) {
		return dataFinal.getTime() > dataInicial.getTime();
	}
}
