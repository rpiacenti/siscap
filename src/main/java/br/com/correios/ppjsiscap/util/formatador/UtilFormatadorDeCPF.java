package br.com.correios.ppjsiscap.util.formatador;


/**
 * Formatador de CPF.
 * 
 * @author arivaldojunior
 */
public final class UtilFormatadorDeCPF extends UtilFormatadorAbstrato {

	
	/**
	 * Formata a string.
	 * 
	 * @param cpf
	 *            String que será formatada.
	 * @return String formatada.
	 */
	public  String formatar(String cpf) {
		String res = cpf;

		if (isValido(cpf)) {
			res = formatarString(cpf, UtilPatternsDeFormatacao.CPF);
		}
		return res;
	}

	/**
	 * Verifica se a string é um CPF válido.
	 * 
	 * @param cpf
	 *            String que será validada.
	 * @return true se for um cpf válido.
	 */
	private  boolean isValido(String cpf) {
		return (cpf != null && isTamanhoValido(cpf));
	}

	/**
	 * Verifica se a string tem o tamnho válido.
	 * 
	 * @param cpf
	 *            String que será verificada.
	 * @return true se o tamanho da string for válido.
	 */
	private  boolean isTamanhoValido(String cpf) {
		int size = cpf.length();
		int tamanhoCpf = UtilPatternsDeFormatacao.QTD_CARACTERES_CPF;
		return (size == tamanhoCpf);
	}

}
