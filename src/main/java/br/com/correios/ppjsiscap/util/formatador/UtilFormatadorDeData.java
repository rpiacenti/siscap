package br.com.correios.ppjsiscap.util.formatador;

import java.io.Serializable;
import java.util.Date;


/**
 * Formatador de Data.
 * 
 * @author arivaldojunior
 */
@SuppressWarnings("serial")
public final class UtilFormatadorDeData extends UtilFormatadorAbstrato implements Serializable{


	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @param pattern
	 *            Pattern que será usado para a formatação.
	 * @return String formatada.
	 */
	public  String formatar(Date data, String pattern) {
		return formatarData(data, pattern);
	}

	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF
	public  String formatar_ddMMyyyy(Date data) {
		return formatarData(data, UtilPatternsDeData.ddMMyyyy);
	}
	//CHECKSTYLE:ON

	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_ddHifenMMHifenyyyy(Date data) {
		return formatarData(data, UtilPatternsDeData.ddHifenMMHifenyyyy);
	}
	//CHECKSTYLE:ON	
	
	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_ddMMyyyyEspacoHHmm(Date data) {
		return formatarData(data, UtilPatternsDeData.ddMMyyyyEspacoHHmm);
	}
	//CHECKSTYLE:ON	

	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_ddMMyyyyEspacohhmm(Date data) {
		return formatarData(data, UtilPatternsDeData.ddMMyyyyEspacohhmm);
	}
	//CHECKSTYLE:ON
	
	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_ddMMyyyyEspacoHHmmss(Date data) {
		return formatarData(data, UtilPatternsDeData.ddMMyyyyEspacoHHmmss);
	}
	//CHECKSTYLE:ON
	
	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_ddMMyyyyEspacohhmmss(Date data) {
		return formatarData(data, UtilPatternsDeData.ddMMyyyyEspacohhmmss);
	}
	//CHECKSTYLE:ON	

	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_hhmm(Date data) {
		return formatarData(data, UtilPatternsDeData.hhmm);
	}
	//CHECKSTYLE:ON	

	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_hhmmss(Date data) {
		return formatarData(data, UtilPatternsDeData.hhmmss);
	}
	//CHECKSTYLE:ON	

	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_HHmm(Date data) {
		return formatarData(data, UtilPatternsDeData.HHmm);
	}
	//CHECKSTYLE:ON
	
	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_HHmmss(Date data) {
		return formatarData(data, UtilPatternsDeData.HHmmss);
	}
	//CHECKSTYLE:ON	

	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_MMMyyyy(Date data) {
		return formatarData(data, UtilPatternsDeData.MMMyyyy);
	}
	//CHECKSTYLE:ON	
	
	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_MMyyyy(Date data) {
		return formatarData(data, UtilPatternsDeData.MMyyyy);
	}
	//CHECKSTYLE:ON
	
	/**
	 * Formata o date no formato DB2.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_padraoDB2(Date data) {
		return formatarData(data, UtilPatternsDeData.PadraoDB2);
	}
	//CHECKSTYLE:ON
	
	/**
	 * Formata o date no formato DB2.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatar_padraoDB2yyyyMMdd(Date data) {
		String pattern = UtilPatternsDeData.yyyyMMddSeparadoPorTraco;
		return formatarData(data, pattern);
	}
	//CHECKSTYLE:ON	
	
	/**
	 * Formata o date.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatarParaExtenso(Date data) {
		return formatarData(data, UtilPatternsDeData
				.EEEEvirguladddeMMMMdeyyyy);
	}
	//CHECKSTYLE:ON	

	/**
	 * Formata o date para o dia da semana.
	 * 
	 * @param data
	 *            Data que será formatada.
	 * @return String formatada.
	 */
	//CHECKSTYLE:OFF	
	public  String formatarParaDiaDaSemana(Date data) {
		return formatarData(data, UtilPatternsDeData.EEEE);
	}
	//CHECKSTYLE:ON

}