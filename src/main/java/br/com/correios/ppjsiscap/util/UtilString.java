
package br.com.correios.ppjsiscap.util;



/**
 * Classe utilitaria responsavel por manipular strings.
 * 
 * @author arivaldojunior
 */
public final class UtilString {

	
	

	/**
	 * Altera a string definida por parametro.
	 * 
	 * @param srcString
	 *            String que sera modificada.
	 * @param localizar
	 *            String que sera pesquisada.
	 * @param novaString
	 *            String substituta.
	 * @return nova string com as devidas alteracoes.
	 */
	public  String substituirString(String srcString, String localizar,
			String novaString) {

		if (!"".equals(srcString) && !"".equals(localizar)) {
			srcString = srcString.replaceAll(localizar, novaString);
		}
		return srcString;
	}
	


	/**
	 * Altera a string com base na condicao passada por parametro.
	 * 
	 * @param condicao
	 *            Condicao
	 * @param srcString
	 *            String que sera modificada.
	 * @param localizar
	 *            String que sera pesquisada.
	 * @param seVerdadeiro
	 *            String substituta caso a condicao seja true.
	 * @param seFalso
	 *            String substituta caso a condicao seja false.
	 * @return nova string com as devidas alteracoes.
	 */
	public  String substituirStringSe(boolean condicao, String srcString,
			String localizar, String seVerdadeiro, String seFalso) {
		if (condicao) {
			srcString = substituirString(srcString, localizar,
					seVerdadeiro);
		} else {
			srcString = substituirString(srcString, localizar,
					seFalso);
		}

		return srcString;
	}

	/**
	 * Remove a string definida por parametro.
	 * 
	 * @param srcString
	 *            String que sera modificada.
	 * @param localizar
	 *            String que sera removida.
	 * @return nova string com as devidas altera��es.
	 */
	public  String remover(String srcString, String localizar) {
		if (!"".equals(srcString) && !"".equals(localizar)) {
			srcString = srcString.replaceAll(localizar, "");
		}
		return srcString;
	}

	

	

	/**
	 * Verifica a exist�ncia de uma string.
	 * 
	 * @param srcString
	 *            String que ser� validada.
	 * @param localizar
	 *            String que ser� pesquisada.
	 * @return true se a string existir.
	 */
	public  boolean isTemString(String srcString, String localizar) {
		boolean result = false;

		if (!"".equals(srcString) && !"".equals(localizar)) {
			result = srcString.indexOf(localizar) != -1;
		}
		return result;
	}

	

	/**
	 * Repete a string a quantidade de vezes definida.
	 * 
	 * @param string
	 *            String que sera repetida.
	 * @param quantidade
	 *            Quantidade de repeticoes.
	 * @return nova string.
	 */
	public  String repetir(String string, int quantidade) {
		StringBuffer result = new StringBuffer();

		if (!"".equals(string)) {
			while (quantidade > 0) {
				result.append(string);
				quantidade--;
			}
		}
		return result.toString();
	}

	/**
	 * Completa a string com a novaString definida a esquerda ate o tamanho
	 * também definido.
	 * 
	 * @param srcString
	 *            String que sera alterada.
	 * @param novaString
	 *            String que sera repetida.
	 * @param tamanho
	 *            Tamanho final da string.
	 * @return string alterada.
	 */
	public  String completarAEsquerda(String srcString,
			String novaString, int tamanho) {
		StringBuffer result = new StringBuffer(srcString);

		if (novaString != null) {
			tamanho = (tamanho - result.length());
			result.insert(0, repetir(novaString, tamanho));
		}
		return result.toString();
	}

	/**
	 * Completa a string com a novaString definida � direita at� o tamanho
	 * tambem definido.
	 * 
	 * @param srcString
	 *            String que sera alterada.
	 * @param novaString
	 *            String que sera repetida.
	 * @param tamanho
	 *            Quantidade de repeticoes.
	 * @return string alterada.
	 */
	public  String completarADireita(String srcString, String novaString,
			int tamanho) {
		StringBuffer result = new StringBuffer(srcString);

		if (novaString != null) {
			tamanho = (tamanho - result.length());
			result.insert(result.length(), repetir(novaString, tamanho));
		}
		return result.toString();
	}	

	/**
	 * Retorna o nome do metodo com o prefixo definido.
	 * 
	 * @param prefix
	 *            prefixo
	 * @param atributo
	 *            nome do atributo
	 * @return nome do m�todo.
	 */
	public  String getNomeMetodo(String prefix, String atributo) {
		StringBuffer nome = new StringBuffer();
		if (!"".equals(prefix) && !"".equals(atributo) && atributo.length() > 1) {
			nome.append(prefix);
			nome.append(atributo.substring(0, 1).toUpperCase());
			nome.append(atributo.substring(1));
		}
		return nome.toString();
	}

	/**
	 * Retorna o nome do atributo com base no nome do m�todo.
	 * 
	 * @param nomeDoMetodo
	 *            Nome do metodo. Ex: getMatricula
	 * @return nome do atributo.
	 */
	public  String getNomeDoAtributo(String nomeDoMetodo) {
		StringBuffer atributo = new StringBuffer();

		if (!"".equals(nomeDoMetodo)) {
			String get = "get";
			String set = "set";
			String is = "is";

			int inicio = 0;
			if (nomeDoMetodo.startsWith(get) || nomeDoMetodo.startsWith(set)) {
				inicio = 3;
			}
			if (nomeDoMetodo.startsWith(is)) {
				inicio = 2;
			}
			atributo.append(nomeDoMetodo);
			atributo.delete(0, inicio);
			atributo.replace(0, 1, String.valueOf(atributo.charAt(0)) );
		}
		return atributo.toString();
	}

	/**
	 * Retorna a quantidade de caracteres solicitados.
	 * 
	 * @param string
	 *            String que sera percorrida e verificada a quantidade de
	 *            caracteres.
	 * @param caracter
	 *            Caracter que ser� contado.
	 * @return quantidade de caracteres.
	 */
	public  int getQuantidadeDeCaracteres(String string, char caracter) {
		int quantidade = 0;

		if (!"".equals(string)) {
			for (int indice = 0; indice < string.length(); indice++) {
				if (string.charAt(indice) == caracter) {
					quantidade++;
				}
			}
		}
		return quantidade;
	}

}
