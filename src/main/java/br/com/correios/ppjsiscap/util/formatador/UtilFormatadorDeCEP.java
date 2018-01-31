package br.com.correios.ppjsiscap.util.formatador;

/**
 * Formatador de Cep.
 * 
 * @author arivaldojunior
 */
public final class UtilFormatadorDeCEP extends UtilFormatadorAbstrato {

	

	private static final int OITO = 8;

	/**
	 * Formata a string.
	 * 
	 * @param cep
	 *            String que ser� formatada.
	 * @return String formatada.
	 */
	public  String formatar(String cep) {
		String result = cep;

		if (isValido(cep)) {
			result = formatarString(cep, UtilPatternsDeFormatacao.CEP);
		}

		return result;
	}

	/**
	 * Verifica se a string é um CEP válido.
	 * 
	 * @param cep
	 *            String que será validada.
	 * @return true se for um CEP válido.
	 */
	private  boolean isValido(String cep) {
		return (cep != null && cep.length() == OITO);
	}
}
