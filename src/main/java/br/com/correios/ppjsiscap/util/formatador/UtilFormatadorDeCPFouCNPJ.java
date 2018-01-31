package br.com.correios.ppjsiscap.util.formatador;

import br.com.correios.ppjsiscap.util.validadores.UtilValidadorDeCNPJ;
import br.com.correios.ppjsiscap.util.validadores.UtilValidadorDeCPF;


/**
 * Formatador de CPF ou CNPJ.
 * 
 * @author arivaldojunior
 */
public final class UtilFormatadorDeCPFouCNPJ extends UtilFormatadorAbstrato {

	private UtilFormatadorDeCPF utilFormatadorDeCPF = new UtilFormatadorDeCPF();
	
	private UtilFormatadorDeCNPJ utilFormatadorDeCNPJ = new UtilFormatadorDeCNPJ();
	
	private UtilValidadorDeCPF utilValidadorDeCPF = new UtilValidadorDeCPF();
	
	private UtilValidadorDeCNPJ utilValidadorDeCNPJ = new UtilValidadorDeCNPJ();
	
	/**
	 * Formata a string.
	 * 
	 * @param cpfCnpj
	 *            String que será formatada.
	 * @return String formatada.
	 */
	public  String formatar(String cpfCnpj) {
		String res = cpfCnpj;

		res = removerCaracteresEspeciais(res);
		res = formatarCPF(res);
		res = formatarCNPJ(res);

		return res;
	}

	/**
	 * Formata cpf.
	 * 
	 * @param cpf
	 * @return String formatada.
	 */
	private  String formatarCPF(String cpf) {
		String retorno = cpf;
		if (utilValidadorDeCPF.validar(cpf)) {
			retorno = utilFormatadorDeCPF.formatar(cpf);
		}
		return retorno;
	}

	/**
	 * Formata cnpj.
	 * 
	 * @param cnpj
	 * @return String formatada.
	 */
	private  String formatarCNPJ(String cnpj) {
		String retorno = cnpj;
		if (utilValidadorDeCNPJ.validar(cnpj)) {
			retorno = utilFormatadorDeCNPJ.formatar(cnpj);
		}
		return retorno;
	}
	
	/**
	 * Revove os caracteres especiais.
	 * @param string String
	 * @return string sem os caracteres especiais.
	 */
	private  String removerCaracteresEspeciais(String string) {
		String retorno = string;
		retorno = getUtilString().substituirString(retorno, "[\n]", " ");
		retorno = getUtilString().substituirString(retorno, "[\r]", " ");
		retorno = getUtilString().substituirString(retorno, "[\t]", " ");
		return retorno;
	}
}
