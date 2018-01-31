package br.com.correios.ppjsiscap.util;





/**
 * Classe utilitária responsável por manipular objetos.
 * 
 * @author arivaldojunior
 */
public final class UtilObjeto {

	

	/**
	 * Retorna true se o objeto for do tipo informado.
	 * 
	 * @param objeto Objeto validado.
	 * @param tipo Tipo desejado
	 * @return true se o objeto for do tipo informado.
	 */	
	public  boolean isObjetoDoTipo(Object objeto, Class<?> tipo) {
		boolean res = false;
		
		if (objeto != null && tipo != null) {
			Class<?> classe = getClasse(objeto);
			res = tipo.isAssignableFrom(classe);
		}
		return res;
	}
	
	/**
	 * Retorna a classe do objeto.
	 * 
	 * @param objeto
	 *            Objeto
	 * @return Classe
	 */
	public  Class<?> getClasse(Object objeto) {
		Class<?> classe = null;

		if (objeto != null) {
			if (isClasse(objeto)) {
				classe = (Class<?>) objeto;
			} else {
				classe = objeto.getClass();
			}
		}
		return classe;
	}
	
	/**
	 * Retorna true se o objeto for uma classe.
	 * 
	 * @param objeto
	 *            Objeto validado.
	 * @return true se o objeto for uma classe.
	 */
	public  boolean isClasse(Object objeto) {
		return (objeto != null && (objeto instanceof Class));
	}


}
