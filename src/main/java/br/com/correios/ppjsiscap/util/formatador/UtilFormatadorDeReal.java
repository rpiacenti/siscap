package br.com.correios.ppjsiscap.util.formatador;

import java.util.regex.Pattern;

/**
 * Formatador de real.
 * 
 * @author arivaldojunior
 */
public final class UtilFormatadorDeReal extends UtilFormatadorAbstrato {

	

	/**
	 * Formata um real.
	 * 
	 * @param real
	 *            Real que será formatado.
	 * @return String formatada.
	 */
	public  String formatar(Number real) {
		return formatarReal(real);
	}

	/**
	 * Formata um real com as casas decimais definidas.
	 * 
	 * @param real
	 *            Real que será formatado.
	 * @param casasDecimais
	 *            Quantidade de casas decimais.
	 * @return String formatada.
	 */
	public  String formatar(Number real, int casasDecimais) {
		return formatarReal(real, casasDecimais);
	}

	/**
	 * Formata um real.
	 * 
	 * @param real
	 *            Real que será formatado.
	 * @return String formatada.
	 */
	public  String formatar(double real) {
		return formatar(new Double(real));
	}

	/**
	 * Formata um real.
	 * 
	 * @param real
	 *            Real que será formatado.
	 * @param casasDecimais
	 *            Quantidade de casas decimais.
	 * @return String formatada.
	 */
	public  String formatar(double real, int casasDecimais) {
		return formatar(new Double(real), casasDecimais);
	}

	
	/**
	 * Retorna true se a string for um num�rico real v�lido.
	 * 
	 * @param string
	 *        String validada.
	 * @return true se a string for um num�rico real v�lido.
	 */
	public  boolean isReal(String string) {
		boolean result = false;
		String stringAvaliada = string;
		if (!"".equals(string)) {
			stringAvaliada = getUtilString().substituirString(string, "[.]", "");
			result = Pattern.matches(UtilPatternsDeFormatacao.REAL, stringAvaliada);
		}
		return result;
	}

	/**
	 * Formata um real para monetário.
	 * 
	 * @param real
	 *            Real que será formatado.
	 * @return String formatada.
	 */
	public  String formatarParaMonetario(double real) {
		String valor = formatar(real);
		if (!"".equals(valor)) {
			valor = UtilPatternsDeFormatacao.PREFIXO_MONETARIO + " " + valor;
		}
		return valor;
	}

	/**
	 * Formata um real para monetário.
	 * 
	 * @param real
	 *            Real que será formatado.
	 * @return String formatada.
	 */
	public  String formatarParaMonetario(Number real) {
		String valor = formatarReal(real);
		if (!"".equals(valor)) {
			valor = UtilPatternsDeFormatacao.PREFIXO_MONETARIO + " " + valor;
		}
		return valor;
	}

	/**
	 * Formata um real para monetário sem o prefixo da moeda.
	 * 
	 * @param real
	 *            Real que será formatado.
	 * @return String formatada.
	 */
	public  String formatarParaMonetarioSemPrefixo(Number real) {
		return formatarReal(real);
	}

}
