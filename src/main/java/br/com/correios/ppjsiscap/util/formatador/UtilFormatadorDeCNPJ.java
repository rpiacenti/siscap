package br.com.correios.ppjsiscap.util.formatador;

/**
 * Responsável por formatar string em CNPJ
 * 
 * @author arivaldojunior
 */
public class UtilFormatadorDeCNPJ extends UtilFormatadorAbstrato{
	

	/**
	 * Formata a string.
	 * 
	 * @param cnpj
	 *            String que será formatada.
	 * @return String formatada.
	 */
	public  String formatar(String cnpj) {
		String res = cnpj;

		if (isValido(cnpj)) {
			res = formatarString(cnpj, UtilPatternsDeFormatacao.CNPJ);
		}
		return res;
	}

	/**
	 * Verifica se a string é um CNPJ válido.
	 * 
	 * @param cnpj
	 *            String que será validada.
	 * @return true se for um cnpj válido.
	 */
	private  boolean isValido(String cnpj) {
		return (cnpj != null && isTamanhoValido(cnpj));
	}

	/**
	 * Verifica se a string tem o tamnho válido.
	 * 
	 * @param cnpj
	 *            String que será verificada.
	 * @return true se o tamanho da string for válido.
	 */
	private  boolean isTamanhoValido(String cnpj) {
		int size = cnpj.length();
		int cnpjSize = UtilPatternsDeFormatacao.QTD_CARACTERES_CNPJ;
		return (size == cnpjSize);
	}

}
