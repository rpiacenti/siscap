package br.com.correios.siscap.managedbeans;

import javax.faces.bean.ManagedBean;
import javax.inject.Named;

@Named("ItemPedidoMB")
@ManagedBean
public class ItemPedidoMB extends MB {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2714871468621140503L;

	private String codigopedido;
	private String siglacia;
	private String sigladr;
	private String mcucia;
	private String mcuorgao;
	private String tipoitem;
	private String tipopedido;
	private String codigoItem;
	private String descricaoItem;
	private String unidadeMedida;
	private String unidadeConversao;
	private String valorUnitario;
	private String mediaSemestre;
	private String quantidade;
	private String quantDisponivel;
	private String datapedido;
	private String statuspedido;

	public ItemPedidoMB() {
		// TODO Auto-generated constructor stub
	}

	public ItemPedidoMB(String codigopedido, String tipoitem,
			String tipopedido, String codigoItem, String descricaoItem,
			String unidadeMedida, String unidadeConversao,
			String valorUnitario, String quantidade, String quantDisponivel) {
		super();
		// this.categoriapedido = codigopedido;
		this.tipoitem = tipoitem;
		this.tipopedido = tipopedido;
		this.codigoItem = codigoItem;
		this.descricaoItem = descricaoItem;
		this.unidadeMedida = unidadeMedida;
		this.unidadeConversao = unidadeConversao;
		this.valorUnitario = valorUnitario;
		this.quantidade = quantidade;
		this.quantDisponivel = quantDisponivel;
		// System.out.println("adiconado :"+this.descricaoItem);
	}

	
	/**
	 * @return the quantiDisponivel
	 */
	public String getQuantDisponivel() {
		return quantDisponivel;
	}

	/**
	 * @param quantiDisponivel the quantiDisponivel to set
	 */
	public void setQuantDisponivel(String quantiDisponivel) {
		this.quantDisponivel = quantiDisponivel;
	}

	/**
	 * @return the codigopedido
	 */
	public String getCodigopedido() {
		return codigopedido;
	}

	/**
	 * @param codigopedido
	 *            the codigopedido to set
	 */
	public void setCodigopedido(String codigopedido) {
		this.codigopedido = codigopedido;
	}

	/**
	 * @return the sigladr
	 */
	public String getSiglacia() {
		return siglacia;
	}

	/**
	 * @param sigladr
	 *            the sigladr to set
	 */
	public void setSiglacia(String siglacia) {
		this.siglacia = siglacia;
	}

	/**
	 * @return the mcucia
	 */
	public String getMcucia() {
		return mcucia;
	}

	/**
	 * @param mcucia
	 *            the mcucia to set
	 */
	public void setMcucia(String mcucia) {
		this.mcucia = mcucia;
	}

	/**
	 * @return the mcuorgao
	 */
	public String getMcuorgao() {
		return mcuorgao;
	}

	/**
	 * @param mcuorgao
	 *            the mcuorgao to set
	 */
	public void setMcuorgao(String mcuorgao) {
		this.mcuorgao = mcuorgao;
	}

	/**
	 * @return the categoriapedido
	 */
	public String getTipoitem() {
		return tipoitem;
	}

	/**
	 * @param categoriapedido
	 *            the categoriapedido to set
	 */
	public void setTipoitem(String tipoitem) {
		this.tipoitem = tipoitem;
	}

	/**
	 * @return the tipopedido
	 */
	public String getTipopedido() {
		return tipopedido;
	}

	/**
	 * @param tipopedido
	 *            the tipopedido to set
	 */
	public void setTipopedido(String tipopedido) {
		this.tipopedido = tipopedido;
	}

	/**
	 * @return the codigoItem
	 */
	public String getCodigoItem() {
		return codigoItem;
	}

	/**
	 * @param codigoItem
	 *            the codigoItem to set
	 */
	public void setCodigoItem(String codigoItem) {
		this.codigoItem = codigoItem;
	}

	/**
	 * @return the descricaoItem
	 */
	public String getDescricaoItem() {
		return descricaoItem;
	}

	/**
	 * @param descricaoItem
	 *            the descricaoItem to set
	 */
	public void setDescricaoItem(String descricaoItem) {
		this.descricaoItem = descricaoItem;
	}

	/**
	 * @return the unidadeMedida
	 */
	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	/**
	 * @param unidadeMedida
	 *            the unidadeMedida to set
	 */
	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	/**
	 * @return the unidadeConversao
	 */
	public String getUnidadeConversao() {
		return unidadeConversao;
	}

	/**
	 * @param unidadeConversao
	 *            the unidadeConversao to set
	 */
	public void setUnidadeConversao(String unidadeConversao) {
		this.unidadeConversao = unidadeConversao;
	}

	/**
	 * @return the valorUnitario
	 */
	public String getValorUnitario() {
		return valorUnitario;
	}

	/**
	 * @param valorUnitario
	 *            the valorUnitario to set
	 */
	public void setValorUnitario(String valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	/**
	 * @return the mediaSemestre
	 */
	public String getMediaSemestre() {
		return mediaSemestre;
	}

	/**
	 * @param mediaSemestre
	 *            the mediaSemestre to set
	 */
	public void setMediaSemestre(String mediaSemestre) {
		this.mediaSemestre = mediaSemestre;
	}

	/**
	 * @return the quantidade
	 */
	public String getQuantidade() {
		return quantidade;
	}

	/**
	 * @param quantidade
	 *            the quantidade to set
	 */
	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	/**
	 * @return the datapedido
	 */
	public String getDatapedido() {
		return datapedido;
	}

	/**
	 * @param datapedido
	 *            the datapedido to set
	 */
	public void setDatapedido(String datapedido) {
		this.datapedido = datapedido;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the sigladr
	 */
	public String getSigladr() {
		return sigladr;
	}

	/**
	 * @param sigladr
	 *            the sigladr to set
	 */
	public void setSigladr(String sigladr) {
		this.sigladr = sigladr;
	}

	/**
	 * @return the statuspedido
	 */
	public String getStatuspedido() {
		return statuspedido;
	}

	/**
	 * @param statuspedido
	 *            the statuspedido to set
	 */
	public void setStatuspedido(String statuspedido) {
		this.statuspedido = statuspedido;
	}

}
