package br.com.correios.siscap.managedbeans;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

@Named("ItemDisponivelMB")
@ViewScoped
public class ItemDisponivelMB extends MB {

	private String itp_co_cia;
	private String itp_co_item;
	private String itp_co_curto_item;
	private String itp_tx_descricao_item;
	private String itp_tx_descricao_detalhe_item;
	private String itp_tx_unidade_item;
	private String itp_tx_unidade_conversao_item;
	private String itp_tx_valor_unitario;
	private String itp_tx_tipo_item;
	private String itp_tx_desc_tipo_item;
	private String itp_tx_tipo_pedido;
	private String itp_tx_natureza_orgao;
	private String itp_tx_quantidade;

	private String cdtransito;
	private String natureza;
	private Boolean marcado = false;
	static Locale loc = new Locale("pt", "BR");

	private List<ItemDisponivelMB> itemdisponivellist = new ArrayList<ItemDisponivelMB>();
	private List<ItemDisponivelMB> listaitemdisponivel = new ArrayList<ItemDisponivelMB>();

	private ItemDisponivelMB selectedItemListaPesquisa;

	public ItemDisponivelMB() {
		super();
		// TODO Auto-generated constructor stub

	}

	public ItemDisponivelMB(String itp_co_cia, String itp_co_item,
			String itp_tx_descricao_item, String itp_tx_descricao_detalhe_item,
			String itp_tx_unidade_item, String itp_tx_unidade_conversao_item,
			String itp_tx_tipo_pedido, String itp_tx_quantidade) {
		super();
		this.itp_co_cia = itp_co_cia;
		this.itp_co_item = itp_co_item;
		this.itp_tx_descricao_item = itp_tx_descricao_item;
		this.itp_tx_unidade_item = itp_tx_unidade_item;
		this.itp_tx_unidade_conversao_item = itp_tx_unidade_conversao_item;
		this.itp_tx_tipo_pedido = itp_tx_tipo_pedido;
		this.itp_tx_quantidade = itp_tx_quantidade;
	}

	public List<ItemDisponivelMB> getListaitemdisponivel() {

		return itemdisponivellist;
	}

	public ItemDisponivelMB getSelectedItemListaPesquisa() {
		return selectedItemListaPesquisa;
	}

	public void setSelectedItemListaPesquisa(ItemDisponivelMB Pesquisa) {
		this.selectedItemListaPesquisa = Pesquisa;
	}

	/**
	 * @return the itemdisponivellist
	 */
	public List<ItemDisponivelMB> getItemdisponivellist() {
		return itemdisponivellist;
	}

	/**
	 * @param itemdisponivellist
	 *            the itemdisponivellist to set
	 */
	public void setItemdisponivellist() {
		this.itemdisponivellist = this.getListaitemdisponivel();
	}

	/**
	 * @return the itp_co_cia
	 */
	public String getItp_co_cia() {
		return itp_co_cia;
	}

	/**
	 * @param itp_co_cia
	 *            the itp_co_cia to set
	 */
	public void setItp_co_cia(String itp_co_cia) {
		this.itp_co_cia = itp_co_cia;
	}

	/**
	 * @return the itp_co_item
	 */
	public String getItp_co_item() {
		return itp_co_item;
	}

	/**
	 * @param itp_co_item
	 *            the itp_co_item to set
	 */
	public void setItp_co_item(String itp_co_item) {
		this.itp_co_item = itp_co_item;
	}

	/**
	 * @return the itp_tx_descricao_item
	 */
	public String getItp_tx_descricao_item() {
		return itp_tx_descricao_item;
	}

	/**
	 * @param itp_tx_descricao_item
	 *            the itp_tx_descricao_item to set
	 */
	public void setItp_tx_descricao_item(String itp_tx_descricao_item) {
		this.itp_tx_descricao_item = itp_tx_descricao_item;
	}

	/**
	 * @return the itp_tx_unidade_item
	 */
	public String getItp_tx_unidade_item() {
		return itp_tx_unidade_item;
	}

	/**
	 * @param itp_tx_unidade_item
	 *            the itp_tx_unidade_item to set
	 */
	public void setItp_tx_unidade_item(String itp_tx_unidade_item) {
		this.itp_tx_unidade_item = itp_tx_unidade_item;
	}

	/**
	 * @return the itp_tx_tipo_pedido
	 */
	public String getItp_tx_tipo_item() {
		return itp_tx_tipo_item;
	}

	/**
	 * @param itp_tx_tipo_pedido
	 *            the itp_tx_tipo_pedido to set
	 */
	public void setItp_tx_tipo_item(String itp_tx_tipo_item) {
		this.itp_tx_tipo_item = itp_tx_tipo_item;
	}
	
	/**
	 * @return the itp_tx_Desc_tipo_item
	 */
	public String getItp_tx_desc_tipo_item() {
		if(this.itp_tx_tipo_item == "M"){
			return "Material";
		}else{
			return "Produto";
		}
		//return itp_tx_Desc_tipo_item;
	}

	/**
	 * @param itp_tx_Desc_tipo_item the itp_tx_Desc_tipo_item to set
	 */
	public void setItp_tx_desc_tipo_item(String itp_tx_desc_tipo_item) {
		this.itp_tx_desc_tipo_item = itp_tx_desc_tipo_item;
	}

	/**
	 * @return the itp_tx_descricao__detalhe_item
	 */
	public String getItp_tx_descricao_detalhe_item() {
		return itp_tx_descricao_detalhe_item;
	}

	/**
	 * @param itp_tx_descricao__detalhe_item
	 *            the itp_tx_descricao__detalhe_item to set
	 */
	public void setItp_tx_descricao_detalhe_item(
			String itp_tx_descricao_detalhe_item) {
		this.itp_tx_descricao_detalhe_item = itp_tx_descricao_detalhe_item;
	}

	/**
	 * @return the itp_tx_unidade_conversao_item
	 */
	public String getItp_tx_unidade_conversao_item() {
		return itp_tx_unidade_conversao_item;
	}

	/**
	 * @param itp_tx_unidade_conversao_item
	 *            the itp_tx_unidade_conversao_item to set
	 */
	public void setItp_tx_unidade_conversao_item(
			String itp_tx_unidade_conversao_item) {
		this.itp_tx_unidade_conversao_item = itp_tx_unidade_conversao_item;
	}

	/**
	 * @return the itp_tx_custo
	 */
	public String getItp_tx_valor_unitario() {
		return itp_tx_valor_unitario;
	}

	/**
	 * @param itp_tx_custo
	 *            the itp_tx_custo to set
	 */
	public void setItp_tx_valor_unitario(String valor) {
		/*NumberFormat df = DecimalFormat.getInstance();

		String dx = df.format(Double.parseDouble(valor)).toString();
		if (dx.indexOf(",") > -1 && dx.length() >= 3) {
			String[] adx = dx.split(",");

			if (adx[1].length() == 1) {
				adx[1] = adx[1] + "0";
			}

			dx = adx[0] + "," + adx[1];
		}
		if(dx.equalsIgnoreCase("0")){
			dx= "0,00";
		}
		*/
		String vl = this.displayCurrency(loc, valor);
		this.itp_tx_valor_unitario = vl.substring(3,vl.length());
	}

	/**
	 * @return the itp_tx_quantidade
	 */
	public String getItp_tx_quantidade() {
		return itp_tx_quantidade;
	}

	/**
	 * @param itp_tx_quantidade
	 *            the itp_tx_quantidade to set
	 */
	public void setItp_tx_quantidade(String itp_tx_quantidade) {
		this.itp_tx_quantidade = itp_tx_quantidade;
	}

	// METODO DE BUSCA E RECUPERA INFORMAÇÃO DE ITENS

	public Boolean getItemDetalhe(String pCodItem) {
		Boolean retorno = false;
		Iterator<ItemDisponivelMB> itr = this.itemdisponivellist.iterator();
		while (itr.hasNext()) {
			ItemDisponivelMB item = (ItemDisponivelMB) itr.next();
			if (pCodItem.equals(item.getItp_co_item())) {
				this.itp_co_item = pCodItem;
				this.itp_tx_descricao_item = item.getItp_tx_descricao_item();
				this.itp_tx_descricao_detalhe_item = item
						.getItp_tx_descricao_detalhe_item();
				this.itp_tx_unidade_item = item.getItp_tx_unidade_item();
				this.itp_tx_valor_unitario = item.getItp_tx_valor_unitario();
				this.itp_tx_unidade_conversao_item = item
						.getItp_tx_unidade_conversao_item();
				this.itp_tx_tipo_item = item.getItp_tx_tipo_item();
				retorno = true;
				break;
			}

		}
		return retorno;
	}

	/**
	 * @return the marcado
	 */
	public Boolean getMarcado() {
		return marcado;
	}

	/**
	 * @param marcado
	 *            the marcado to set
	 */
	public void setMarcado(Boolean marcado) {
		this.marcado = marcado;
	}

	/**
	 * @return the cdtransito
	 */
	public String getCdtransito() {
		return cdtransito;
	}

	/**
	 * @param cdtransito
	 *            the cdtransito to set
	 */
	public void setCdtransito(String cdtransito) {
		this.cdtransito = cdtransito;
	}

	/**
	 * @return the natureza
	 */
	public String getNatureza() {
		return natureza;
	}

	/**
	 * @param natureza
	 *            the natureza to set
	 */
	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}

	/**
	 * @return the itp_co_curto_item
	 */
	public String getItp_co_curto_item() {
		return itp_co_curto_item;
	}

	/**
	 * @param itp_co_curto_item
	 *            the itp_co_curto_item to set
	 */
	public void setItp_co_curto_item(String itp_co_curto_item) {
		this.itp_co_curto_item = itp_co_curto_item;
	}

	/**
	 * @return the itp_tx_tipo_pedido
	 */
	public String getItp_tx_tipo_pedido() {
		return itp_tx_tipo_pedido;
	}

	/**
	 * @param itp_tx_tipo_pedido the itp_tx_tipo_pedido to set
	 */
	public void setItp_tx_tipo_pedido(String itp_tx_tipo_pedido) {
		this.itp_tx_tipo_pedido = itp_tx_tipo_pedido;
	}
	
	/**
	 * @return the itp_tx_natureza_orgao
	 */
	public String getItp_tx_natureza_orgao() {
		return itp_tx_natureza_orgao;
	}

	/**
	 * @param itp_tx_natureza_orgao the itp_tx_natureza_orgao to set
	 */
	public void setItp_tx_natureza_orgao(String itp_tx_natureza_orgao) {
		this.itp_tx_natureza_orgao = itp_tx_natureza_orgao;
	}

	public String displayCurrency( Locale currentLocale, String valor) {
	 
		double dValor = Double.parseDouble(valor);
	    NumberFormat currencyFormatter = 
	        NumberFormat.getCurrencyInstance(currentLocale);
        return currencyFormatter.format(dValor);
	}

}
