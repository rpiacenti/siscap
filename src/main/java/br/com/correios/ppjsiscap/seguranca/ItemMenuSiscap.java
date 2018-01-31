package br.com.correios.ppjsiscap.seguranca;

import java.util.Comparator;

public class ItemMenuSiscap {
	private int ordem;
	private int nivel;
	private String url;
	private String descricao;
	private String superior;
	private String status;
	private String co_item;

	public ItemMenuSiscap() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return
	 */
	public int getOrdem() {
		return ordem;

	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int pnivel) {
		this.nivel = pnivel;
	}

	public void setOrdem(int string) {
		this.ordem = string;
	}

	
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the nomeItem
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param nomeItem the nomeItem to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public ItemMenuSiscap get(Integer id) {
		return this;
	}

	/**
	 * @return the superior
	 */
	public String getSuperior() {
		return superior;
	}

	/**
	 * @param superior the superior to set
	 */
	public void setSuperior(String superior) {
		this.superior = superior;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param string the status to set
	 */
	public void setStatus(String string) {
		this.status = string;
	}

	/**
	 * @return the co_item
	 */
	public String getCo_item() {
		return co_item;
	}

	/**
	 * @param string the co_item to set
	 */
	public void setCo_item(String string) {
		this.co_item = string;
	}

	public int compareTo(ItemMenuSiscap compareMenu) {
		 
		int compareOrdem = ((ItemMenuSiscap) compareMenu).getOrdem(); 
 
		//ascending order
		return this.ordem - compareOrdem;
 
		//descending order
		//return compareQuantity - this.quantity;
 
	}
 
	public static Comparator<ItemMenuSiscap> MenuNameComparator 
                          = new Comparator<ItemMenuSiscap>() {
 
	    public int compare(ItemMenuSiscap menu1, ItemMenuSiscap menu2) {
 
	      String menuName1 = menu1.getDescricao().toUpperCase();
	      String menuName2 = menu2.getDescricao().toUpperCase();
 
	      //ascending order
	      return menuName1.compareTo(menuName2);
 
	      //descending order
	      //return fruitName2.compareTo(fruitName1);
	    }
	};	    
	
}
