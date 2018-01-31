package br.com.correios.ppjsiscap.util.validadores;

import java.util.regex.Pattern;

import br.com.correios.ppjsiscap.util.UtilString;



/**
 * Classe Responsável por verificar se um CPF é válido
 * 
 * @author arivaldojunior
 * 
 */
public class UtilValidadorDeCPF {
	
	private UtilString utilString = new UtilString();
	
	/**
	 * @param numeroCPF
	 * @return o número do CPF sem formatação.
	 * Se o número for null,vazio ou sem formatação, será retornado o mesmo.
	 */
	protected  String retirarFormatacaoDoCPF(String numeroCPF) {
		String retorno = numeroCPF;
		if (numeroCPF != null) {
			retorno = retorno.trim();
			retorno = utilString.substituirString(retorno,"\\.", ""); 
			retorno = utilString.substituirString(retorno,"-", "");
		}
		return retorno;
	}

	/**
	 * Retorna true se a string for um CPF válido.
	 * 
	 * @param cpf CPF
	 * @return true se a string for um CPF válido.
	 */
	public  boolean validar(String cpf) {
		boolean resultado = false;
		
		if ((cpf == null) || ("".equals(cpf))){
			return resultado;
		}
		
		int posicaoTemp;		
		String cpfSemFormatacao = retirarFormatacaoDoCPF(cpf);
		
		if (!isNumeroDeCPFPadrao(cpfSemFormatacao) && cpfSemFormatacao.length() == 11) {			
			posicaoTemp = 0;
			for (int i = 0; i < 9; i++) {
				posicaoTemp += (10 - i) * (cpfSemFormatacao.charAt(i) - 48);
			}
			posicaoTemp = 11 - posicaoTemp % 11;
			if (posicaoTemp > 9) {
				posicaoTemp = 0;
			}
			if (posicaoTemp == cpfSemFormatacao.charAt(9) - 48) {
				posicaoTemp = 0;
				for (int i = 0; i < 10; i++) {
					posicaoTemp += (11 - i) * (cpfSemFormatacao.charAt(i) - 48);
				}
				posicaoTemp = 11 - posicaoTemp % 11;
				if (posicaoTemp > 9) {
					posicaoTemp = 0;
				}
				if (posicaoTemp == cpfSemFormatacao.charAt(10) - 48) {
					resultado = true;
				}
			}
		}
		return resultado;
	}

	/**
	 * @param numeroDoCPF
	 * @return verdadeiro se o número do CPF for um padrão. Ex: 00000000000, 11111111111, etc.
	 */
	private boolean isNumeroDeCPFPadrao(String numeroDoCPF) {
		String regex = "(?=(\\d)\\1{10})\\d{11}";
		Pattern pat = Pattern.compile(regex);
		return pat.matcher(numeroDoCPF).matches();
	}
}
