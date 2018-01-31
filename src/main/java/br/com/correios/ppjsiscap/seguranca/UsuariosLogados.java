package br.com.correios.ppjsiscap.seguranca;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * Classe que referencia todos os usuários da aplicação
 * @author 80131085
 *
 */
@Named
@ApplicationScoped
public class UsuariosLogados {

	private List<Usuario> usuarios = new ArrayList<Usuario>();

	/**
	 * Adiciona um usuário na lista
	 * @param u
	 */
	public void adicionar(Usuario u){
		usuarios.add(u);
	}
	
	/**
	 * Remove um usuário da lista
	 * @param u
	 */
	public void remover(Usuario u){
		usuarios.remove(u);
	}

	/**
	 * 
	 * @param usuario
	 * @return
	 */
	public boolean isExiste(Usuario usuario) {		
		return usuarios.indexOf(usuario) > -1;
	}

}
