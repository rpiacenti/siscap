package br.com.correios.ppjsiscap.seguranca;

import java.util.ArrayList;

//import br.com.correios.idautorizadorws.usuario.interno.client.ItemMenu;

public class ItemMenuDecoratorSiscap {
	
	private ItemMenuSiscap itemMenu;
	
	private ArrayList<ItemMenuDecoratorSiscap> filhos = new ArrayList<ItemMenuDecoratorSiscap>();
	
	/**
	 * Construtor
	 * @param itemMenuSiscap
	 */
	public ItemMenuDecoratorSiscap(ItemMenuSiscap itemMenuSiscap){
		itemMenu = itemMenuSiscap;
	}

	/**
	 * @return the itemMenu
	 */
	public ItemMenuSiscap getItemMenu() {
		return itemMenu;
	}

	/**
	 * @param itemMenu the itemMenu to set
	 */
	public void setItemMenu(ItemMenuSiscap itemMenu) {
		this.itemMenu = itemMenu;
	}

	/**
	 * @return the filhos
	 */
	public ArrayList<ItemMenuDecoratorSiscap> getFilhos() {
		return filhos;
	}

	/**
	 * @param filhos the filhos to set
	 */
	public void setFilhos(ArrayList<ItemMenuDecoratorSiscap> filhos) {
		this.filhos = filhos;
	}
	
	

}
