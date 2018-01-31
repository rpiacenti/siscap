package br.com.correios.ppjsiscap.util.validadores;

import br.com.correios.ppjsiscap.util.UtilString;



/**
 * Classe Responsável por verificar se um cnpj é válido
 * @author arivaldojunior
 *
 */
public class UtilValidadorDeCNPJ {

	private UtilString utilString = new UtilString();
	
	/**
	 * Retira caracteres não numéricos do cnpj
	 * @param numeroCNPJ
	 * @return
	 */
	protected  String retiraFormatacao(String numeroCNPJ) {
		String resposta = numeroCNPJ;		
		if (numeroCNPJ != null) {
			resposta = resposta.trim();
			resposta = utilString.substituirString(resposta, "\\.", "");
			resposta = utilString.substituirString(resposta,"/", "");
			resposta = utilString.substituirString(resposta,"-", "");
		}
		return resposta;
	}

	/**
	 * Retorna true se o número form um cnpj válido.
	 * 
	 * @param cnpj
	 *            CNPJ
	 * @return true se o número form um cnpj válido.
	 */
	public  boolean validar(Number cnpj) {
		boolean res = false;
		if (cnpj != null) {
			res = validar(cnpj.toString());
		}

		return res;
	}

	/**
	 * Retorna true se o número form um cnpj válido.
	 * 
	 * @param cnpj
	 *            CNPJ
	 * @return true se o número form um cnpj válido.
	 */
	public  boolean validar(long cnpj) {
		return validar(new Long(cnpj));
	}

	/**
	 * Retorna true se a string for um CNPJ válido.
	 * 
	 * @param stringCNPJAvaliada
	 *            CNPJ
	 * @return true se a string for um cnpj válido.
	 */
	public  boolean validar(String cnpj) {
		boolean result;
		int position;
		String stringCNPJAvaliada = cnpj; 
		if (stringCNPJAvaliada == null
				|| !(stringCNPJAvaliada.length() >= getQuantidadeDeCaracteresDoCNPJ())) {
			return false;
		}
		result = false;
		stringCNPJAvaliada = stringCNPJAvaliada.trim();
		int tamanho = stringCNPJAvaliada.length();
		if (tamanho > getQuantidadeDeCaracteresDoCNPJ() && tamanho < 19) {
			stringCNPJAvaliada = retiraFormatacao(stringCNPJAvaliada);
		}
		if (stringCNPJAvaliada.length() > getQuantidadeDeCaracteresDoCNPJ()) {
			return false;
		}
		position = 0;
		int i = 0;
		int j = 5;
		for (; i < 12; i++) {
			position += j-- * (stringCNPJAvaliada.charAt(i) - 48);
			if (j < 2) {
				j = 9;
			}
		}
		position = 11 - position % 11;
		if (position > 9) {
			position = 0;
		}
		if (position == stringCNPJAvaliada.charAt(12) - 48) {
			position = 0;
			i = 0;
			j = 6;
			for (; i < 13; i++) {
				position += j-- * (stringCNPJAvaliada.charAt(i) - 48);
				if (j < 2) {
					j = 9;
				}
			}
			position = 11 - position % 11;
			if (position > 9) {
				position = 0;
			}
			if (position == stringCNPJAvaliada.charAt(13) - 48) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * @return quantidade de caracteres do cnpj.
	 * @see Constante#getQuantidadeDeCaracteresDoCNPJ()
	 */
	private  int getQuantidadeDeCaracteresDoCNPJ() {
		return 14;		
	}
}
