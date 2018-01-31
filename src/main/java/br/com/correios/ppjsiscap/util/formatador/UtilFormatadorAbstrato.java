package br.com.correios.ppjsiscap.util.formatador;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import br.com.correios.ppjsiscap.util.UtilObjeto;
import br.com.correios.ppjsiscap.util.UtilString;

/**
 * Responsável por ser a super classe dos formatadores
 * @author arivaldojunior
 */
public abstract class UtilFormatadorAbstrato {
	
	private UtilString utilString = new UtilString();
	
	private UtilObjeto utilObjeto = new UtilObjeto();
	
	

	/**
	 * Formata uma string de acordo com o pattern passado por parâmetro.
	 * 
	 * @param string
	 *            String que será formatada.
	 * @param pattern
	 *            Pattern usado na formatação.
	 * @return String formatada.
	 */
	protected  String formatarString(String string, String pattern) {
		JFormattedTextField format = novoJFormattedTextField(pattern);
		format.setText(string);
		return format.getText();
	}

	/**
	 * Formata uma data de acordo com o pattern passado por parâmetro.
	 * 
	 * @param data
	 *            Dtaa que será formatada.
	 * @param pattern
	 *            Pattern usado na formatação.
	 * @return String da data formatada.
	 */
	protected  String formatarData(Date data, String pattern) {
		String res = null;

		if (data != null && !"".equals(pattern)) {
			res = novoSimpleDateFormat(pattern).format(data);
		}
		return res;
	}

	/**
	 * Formata um número.
	 * 
	 * @param number
	 *            número que será formatado.
	 * @return String do número formatado
	 */
	protected  String formatarNumero(Number number) {
		String res = null;

		if (number != null) {
			res = novoNumberFormat().format(number);
		}
		return res;
	}

	/**
	 * Formata um número real.
	 * 
	 * @param number
	 *            número que será formatado.
	 * @return String do número formatado
	 */
	protected  String formatarReal(Number number) {
		String result = null;

		if (number != null) {
			result = novoDecimalFormat().format(number);
		}
		return result;
	}

	/**
	 * Formata um número real.
	 * 
	 * @param number
	 *            número que será formatado.
	 * @param casasDecimais
	 *            quantidade de casas decimais.
	 * @return String do n�mero formatado
	 */
	protected  String formatarReal(Number number, int casasDecimais) {
		String result = null;

		if (number != null) {
			result = novoDecimalFormat(casasDecimais).format(number);
		}
		return result;
	}

	/**
	 * Retorna o separador de decimal.
	 * 
	 * @return separador de decimal
	 */
	protected  String getSeparadorDecimal() {
		char separador = novoDecimalFormatSymbols().getDecimalSeparator();
		return String.valueOf(separador);
	}

	
	/**
	 * Retorna novo DecimalFormatSymbols.
	 * 
	 * @return instância do DecimalFormatSymbols
	 */
	private  DecimalFormatSymbols novoDecimalFormatSymbols() {
		return new DecimalFormatSymbols();
	}

	/**
	 * Retorna a inst�ncia SimpleDateFormat.
	 * 
	 * @param pattern
	 *            pattern da formatação.
	 * @return instância de SimpleDateFormat.
	 */
	private  SimpleDateFormat novoSimpleDateFormat(String pattern) {
		return new SimpleDateFormat(pattern);
	}

	/**
	 * Retorna a inst�ncia de JFormattedTextField usada na formata��o de
	 * Strings.
	 * 
	 * @param pattern
	 *            patterna usado na formatação.
	 * @return instância de JFormattedTextField.
	 */
	private  JFormattedTextField novoJFormattedTextField(String pattern) {
		return new JFormattedTextField(novoMaskFormatter(pattern));
	}

	/**
	 * Retorna instância de MaskFormatter do pattern passado por par�metro.
	 * 
	 * @param pattern
	 *            pattern da máscara.
	 * @return instância de MaskFormatter.
	 */
	private  MaskFormatter novoMaskFormatter(String pattern) {
		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter(pattern);
		} catch (ParseException e) {
			// nao faz nada.
		}
		return formatter;
	}

	/**
	 * Retorna novo NumberFormat.
	 * 
	 * @return novo NumberFormat.
	 */
	private  DecimalFormat novoDecimalFormat() {
		NumberFormat numberInstance = DecimalFormat.getNumberInstance(new Locale("pt", "BR"));
		numberInstance.setMinimumFractionDigits(UtilPatternsDeFormatacao.QTD_MIN_CASAS_DECIMAIS);
		return (DecimalFormat) numberInstance;
	}

	/**
	 * Retorna novo NumberFormat.
	 * 
	 * @param casasDecimais
	 *            Quantidade de casas decimais.
	 * @return novo NumberFormat.
	 */
	private  DecimalFormat novoDecimalFormat(int casasDecimais) {
		DecimalFormat decimalFormat = novoDecimalFormat();
		decimalFormat.setMinimumFractionDigits(casasDecimais);
		decimalFormat.setMaximumFractionDigits(casasDecimais);
		return decimalFormat;
	}

	/**
	 * Retorna novo DecimalFormat.
	 * 
	 * @return novo DecimalFormat.
	 */
	private  NumberFormat novoNumberFormat() {
		return NumberFormat.getCurrencyInstance();
	}

	public UtilString getUtilString() {
		return utilString;
	}
	
	public UtilObjeto getUtilObjeto() {
		return utilObjeto;
	}
	
}
