package br.com.correios.ppjsiscap.util.formatador;

import java.util.HashMap;
import java.util.Map;


/**
 * Utilitário para formatação de telefone.
 * 
 * @author arivaldojunior
 */
public class UtilFormatadorDeTelefone extends UtilFormatadorAbstrato {
	
	private static final int DOZE = 12;
	private static final int DEZ = 10;
	private static final int OITO = 8;
	private  Map<Integer, String>	patterns;

	

	/**
	 * Formata um telefone.
	 * 
	 * @param telefone
	 *            Real que será formatado.
	 * @return String formatada.
	 */
	public  String formatar(String telefone) {
		String resultado = telefone;
		if (isValido(telefone)) {
			String pattern = getPattern(telefone);
			resultado = formatarString(telefone, pattern);
		}
		return resultado;
	}

	/**
	 * Formata um telefone.
	 * 
	 * @param telefone
	 * @return String formatada.
	 */
	public  String formatar(Number telefone) {
		return formatar(telefone.toString());
	}

	/**
	 * Formata um telefone.
	 * 
	 * @param telefone
	 * @return String formatada.
	 */
	public  String formatar(int telefone) {
		return formatar(String.valueOf(telefone));
	}

	/**
	 * Formata um telefone.
	 * 
	 * @param telefone
	 * @return String formatada.
	 */
	public  String formatar(long telefone) {
		return formatar(String.valueOf(telefone));
	}

	/**
	 * Verifica se a string é um Telefone válido.
	 * 
	 * @param telefone
	 *            String que será validada.
	 * @return true se for um telefone válido.
	 */
	private  boolean isValido(String telefone) {
		int tamanho = telefone.length();
		return (telefone != null && (isTamanho(tamanho, OITO)
				|| isTamanho(tamanho, DEZ) || isTamanho(tamanho, DOZE)));
	}

	/**
	 * Retorna true se o tamanho for o mesmo do desejado.
	 * 
	 * @param tamanho
	 *            Inteiro validado
	 * @param desejado
	 *            Inteiro desejado
	 * @return true se o tamanho for o mesmo do desejado.
	 */
	private  boolean isTamanho(int tamanho, int desejado) {
		return tamanho == desejado;
	}

	

	/**
	 * Retorna o pattern de um telefone.
	 * 
	 * @param telefone
	 *            Telefone
	 * @return pattern
	 */
	private  String getPattern(String telefone) {
		int tamanho = telefone.length();
		return (String) getPatterns().get(tamanho);
	}

	/**
	 * @return patterns de telefone.
	 */
	private  Map<Integer, String> getPatterns() {
		if (patterns == null) {
			patterns = new HashMap<Integer, String>(3);
			patterns.put(OITO, UtilPatternsDeFormatacao.TELEFONEDEOITO);
			patterns.put(DEZ, UtilPatternsDeFormatacao.TELEFONEDEDEZ);
			patterns.put(DOZE, UtilPatternsDeFormatacao.TELEFONEDEDOZE);
		}
		return patterns;
	}
}
