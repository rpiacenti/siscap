
package br.com.correios.ppjsiscap.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import br.com.correios.siscap.constantes.Arquivo;




/**
 * Responsável pela recuperação das mensagens do properties .
 * 
 * @author Arivaldo Junior
 * 
 */
@Named
@ApplicationScoped
public class UtilMensagem  {
	
	
	private UtilArquivo utilArquivo = new UtilArquivo();
	
	

	/**
	 * @see UtilMensagemAbstrato#getProperties()
	 */
	protected ResourceBundle getResourceBundle(Locale locale) {
		String arquivo = Arquivo.CAMINHO_MENSAGENS_EXCECAO;
		return utilArquivo.getResourceBundle(arquivo, locale);
	}

	

	/**
	 * Formata uma mensagem recuperada do arquivo messages.properties.
	 * 
	 * @param key
	 *        chave da mensagem.
	 * @param argumentos
	 *        argumentos da mensagem.
	 * @return mensagem formatada.
	 */
	public String getMensagem(Locale locale, String key, String... argumentos) {
		String resultado = null;
		ResourceBundle resourceBundle = getResourceBundle(locale);
		if (key != null && !"".equals(key)
				&& resourceBundle != null) {
			String message = resourceBundle.getString(key);

			if ("".equals(message)) {
				message = "Mensagem nao mapeada no .properties";
			}		
			
			resultado = MessageFormat.format(message, argumentos);
		}
		return resultado;
	}

	

}
