package br.com.correios.ppjsiscap.util.formatador;

/**
 * Patterns usados nas formatações.
 * 
 * @author arivaldojunior
 */
public interface UtilPatternsDeFormatacao {

	
	/**
	 *  Pattern de CEP.
	 */
	String CEP = "##.###-###";

	/**
	 *  Pattern de CNPJ.
	 */
	String CNPJ = "##.###.###/####-##";

	/**
	 *  Pattern de CPF.
	 */
	String CPF ="###.###.###-##";

	/**
	 *  Pattern de e-mail
	 */
	String EMAIL = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*"
				+ "[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
	
	
	/**
	 *  Pattern de telefone com 8 números
	 */
	String TELEFONEDEOITO = "####-####";	

	/**
	 *  Pattern de telefone com 10 números
	 */
	String TELEFONEDEDEZ = "## ####-####";

	/**
	 *  Pattern de telefone com 12 números
	 */
	String TELEFONEDEDOZE = "#### ####-####";

	/**
	 *  Pattern de cartão
	 */
	String CARTAO = "####.####.####.####";
	
	/**
	 * Quantidade de casas decimais
	 */
	int QTD_MIN_CASAS_DECIMAIS = 2;
	
	/**
	 * Quantidade de caracteres do cnpj
	 */
	int QTD_CARACTERES_CNPJ = 14;
	
	/**
	 * Prefixo indicador de moeda corrente atual
	 */
	String PREFIXO_MONETARIO = "R$";
	
	/**
	 * pattern regex para real. 
	 */
	String REAL= "[-+]?[0-9]*[\\,]?[0-9]*";

	int QTD_CARACTERES_CPF = 11;
	
	
}
