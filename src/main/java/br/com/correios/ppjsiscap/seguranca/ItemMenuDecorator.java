package br.com.correios.ppjsiscap.seguranca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.com.correios.componente.idautorizador.usuario.ItemMenu;



@SuppressWarnings("serial")
public class ItemMenuDecorator implements Serializable, Comparable<ItemMenuDecorator>{
	
	private ItemMenu itemMenu;
	
	private Map<Integer, ItemMenuDecorator> filhos = new TreeMap<Integer,ItemMenuDecorator>();
	
	/**
	 * Construtor
	 * @param item
	 */
	public ItemMenuDecorator(ItemMenu item){
		itemMenu = item;
	}

	/**
	 * @return the itemMenu
	 */
	public ItemMenu getItemMenu() {
		return itemMenu;
	}

	/**
	 * @param itemMenu the itemMenu to set
	 */
	public void setItemMenu(ItemMenu itemMenu) {
		this.itemMenu = itemMenu;
	}

	/**
	 * @return the filhos
	 */
	public Map<Integer, ItemMenuDecorator> getFilhos() {
		return filhos;
	}

	/**
	 * @param filhos the filhos to set
	 */
	public void setFilhos(TreeMap<Integer, ItemMenuDecorator> filhos) {
		this.filhos = filhos;
	}
	
	/**
	 * Retorna uma lista da arvore para correta iteração do ui:repeat
	 * @return
	 */
	public List<ItemMenuDecorator> getListaDeFilhos(){
		List<ItemMenuDecorator> resultado = null;
		if(filhos.size() != 0){
			resultado = new ArrayList<ItemMenuDecorator>();
			resultado.addAll(filhos.values());
			Collections.sort(resultado);
		}
		return resultado; 
	}

	@Override
	public int compareTo(ItemMenuDecorator outroItemMenuDecorator) {
		return this.getItemMenu().getOrdem().compareTo(outroItemMenuDecorator.getItemMenu().getOrdem());
	}
	
}
