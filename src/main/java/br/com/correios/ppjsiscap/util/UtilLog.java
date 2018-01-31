package br.com.correios.ppjsiscap.util;



import org.apache.log4j.Logger;


/**
 * Classe utilitária responsável pelo log do sistema.
 * 
 * @author Arivaldo Junior
 */
public class UtilLog  {

	
	
	public static Logger logger;

	
	static{
		logger =  Logger.getLogger("SISCAP");
		logger.info("Configurando logger do Sistema ...");		
	}
	

}
