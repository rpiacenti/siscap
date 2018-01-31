package br.com.correios.ppjsiscap.util.validadores;

import java.io.Serializable;
import java.util.Date;


/**
 * Validador de data iguais.
 * 
 * @author arivaldojunior
 */
@SuppressWarnings("serial")
public final class UtilValidadorDeDatasIguais  implements Serializable{

	
	
	/**
	 * Retorna true se as duas datas forem iguais. As datas são obrigatórias.
	 * 
	 * @param data0 Data.
	 * @param data1 Data.
	 * @return true se as duas datas forem iguais.
	 */
	public  boolean validar(Date data0, Date data1) {
		boolean result = false;
		
		if (data0 != null &&  data1 != null) {
			result = data0.getTime() == data1.getTime();
		}
		
		return result;
	}
	
	/**
	 * Retorna true se as datas forem iguais. As datas são obrigatórias.
	 * 
	 * @param data0 Data.
	 * @param data1 Data.
	 * @param data2 Data.
	 * @return true se as duas datas forem iguais.
	 */
	public  boolean validar(Date data0, Date data1, Date data2) {
		boolean res = false;
		
		if (data0 != null && data1 != null && data2 != null) {
			long time0 = data0.getTime();
			long time1 = data1.getTime();
			long time2 = data2.getTime();
			res = time0 == time1 && time0 == time2;
		}
		
		return res;
	}
}
