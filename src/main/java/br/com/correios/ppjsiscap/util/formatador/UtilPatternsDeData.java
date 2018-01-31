package br.com.correios.ppjsiscap.util.formatador;

/**
 * Patterns de Data
 * 
 * @author arivaldojunior
 * 
 */
@SuppressWarnings("PMD.SuspiciousConstantFieldName")
public interface UtilPatternsDeData {

	/**
	 * Retorna o pattern dd/MM/yyyy.
	 */
	String ddMMyyyy = "dd/MM/yyyy"; // NOPMD

	/**
	 * Retorna o pattern dd/MM/yyyy hh:mm.
	 */
	String ddMMyyyyEspacohhmm = "dd/MM/yyyy hh:mm";

	/**
	 * Retorna o pattern dd/MM/yyyy hh:mm:ss.
	 */
	String ddMMyyyyEspacohhmmss = "dd/MM/yyyy hh:mm:ss";

	/**
	 * Retorna o pattern dd/MM/yyyy HH:mm.
	 */
	String ddMMyyyyEspacoHHmm = "dd/MM/yyyy HH:mm";

	/**
	 * Retorna o pattern dd/MM/yyyy HH:mm:ss.
	 */
	String ddMMyyyyEspacoHHmmss = "dd/MM/yyyy HH:mm:ss";

	/**
	 * Retorna o pattern dd-MM-yyyy.
	 */
	String ddHifenMMHifenyyyy = "dd-MM-yyyy";

	/**
	 * Retorna o pattern MMM/yyyy.
	 */
	String MMMyyyy = "MMM/yyyy";

	/**
	 * Retorna o pattern MM/yyyy.
	 */
	String MMyyyy = "MM/yyyy";

	/**
	 * Retorna o pattern hh:mm.
	 */
	String hhmm = "hh:mm";

	/**
	 * Retorna o pattern hh:mm:ss.
	 */
	String hhmmss = "hh:mm:ss";

	/**
	 * Retorna o pattern HH:mm.
	 */
	String HHmm = "HH:mm";

	/**
	 * Retorna o pattern HH:mm:ss.
	 */
	String HHmmss = "HH:mm:ss";

	/**
	 * Retorna o pattern de data por extenso.
	 */
	String EEEEvirguladddeMMMMdeyyyy = "EEEE, dd 'de' MMMM 'de' yyyy";

	/**
	 * Retorna o pattern do formato IBM (yyyyMMdd).
	 */
	String PadraoIBM = "yyyyMMdd";

	/**
	 * Retorna o pattern do padrão DB2 (yyyy-MM-dd-HH.mm.ss.SSSSSS).
	 */
	String PadraoDB2 = "yyyy-MM-dd-HH.mm.ss.SSSSSS";

	/**
	 * Retorna o pattern do padrão DB2 (yyyy-MM-dd), somente data.
	 */
	String yyyyMMddSeparadoPorTraco = "yyyy-MM-dd";

	/**
	 * Retorna o pattern do padrão DB2 (dd.MM.yyyy), somente data.
	 */
	String ddMMyyyySeparadoPorPonto = "dd.MM.yyyy";

	/**
	 * Retorna o pattern do dia da semana.
	 */
	String EEEE = "EEEE";

}
