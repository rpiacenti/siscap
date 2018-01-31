package br.com.correios.siscap.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

@SuppressWarnings("serial")
public class ItemDisponivel implements Serializable {
	
	private String itp_co_cia;
	private String itp_co_item;
	private String itp_co_curto_item;
	private String itp_tx_descricao_item;
	private String itp_tx_descricao_detalhe_item;
	private String itp_tx_unidade_item;
	private String itp_tx_unidade_conversao_item;
	private String itp_tx_valor_unitario;
	private String itp_tx_valor_unitario_formatado;
	private String itp_tx_tipo_item;
	private String itp_tx_desc_tipo_item;
	private String itp_tx_tipo_pedido;
	private String itp_tx_natureza_orgao;
	private String itp_tx_quantidade;
	private String pit_pe_item_padrao;
	
	private String umconv;
	
	private String councs;
	
	private String umrum;
	
	private String umum;
	
	private String umcnv1;
	
	private String cdtransito;
	
	private boolean marcado;
	
	private String unidMedFormatada;
	
	
	/**
	 * @return the itp_co_cia
	 */
	public String getItp_co_cia() {
		return itp_co_cia;
	}
	/**
	 * @param itp_co_cia the itp_co_cia to set
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
	 * @param itp_co_item the itp_co_item to set
	 */
	public void setItp_co_item(String itp_co_item) {
		this.itp_co_item = itp_co_item;
	}
	/**
	 * @return the itp_co_curto_item
	 */
	public String getItp_co_curto_item() {
		return itp_co_curto_item;
	}
	/**
	 * @param itp_co_curto_item the itp_co_curto_item to set
	 */
	public void setItp_co_curto_item(String itp_co_curto_item) {
		this.itp_co_curto_item = itp_co_curto_item;
	}
	/**
	 * @return the itp_tx_descricao_item
	 */
	public String getItp_tx_descricao_item() {
		return itp_tx_descricao_item;
	}
	/**
	 * @param itp_tx_descricao_item the itp_tx_descricao_item to set
	 */
	public void setItp_tx_descricao_item(String itp_tx_descricao_item) {
		this.itp_tx_descricao_item = itp_tx_descricao_item;
	}
	/**
	 * @return the itp_tx_descricao_detalhe_item
	 */
	public String getItp_tx_descricao_detalhe_item() {
		return itp_tx_descricao_detalhe_item;
	}
	/**
	 * @param itp_tx_descricao_detalhe_item the itp_tx_descricao_detalhe_item to set
	 */
	public void setItp_tx_descricao_detalhe_item(
			String itp_tx_descricao_detalhe_item) {
		this.itp_tx_descricao_detalhe_item = itp_tx_descricao_detalhe_item;
	}
	/**
	 * @return the itp_tx_unidade_item
	 */
	public String getItp_tx_unidade_item() {
		return itp_tx_unidade_item;
	}
	/**
	 * @param itp_tx_unidade_item the itp_tx_unidade_item to set
	 */
	public void setItp_tx_unidade_item(String itp_tx_unidade_item) {
		this.itp_tx_unidade_item = itp_tx_unidade_item;
	}
	/**
	 * @return the itp_tx_unidade_conversao_item
	 */
	public String getItp_tx_unidade_conversao_item() {
		return itp_tx_unidade_conversao_item;
	}
	/**
	 * @param itp_tx_unidade_conversao_item the itp_tx_unidade_conversao_item to set
	 */
	public void setItp_tx_unidade_conversao_item(
			String itp_tx_unidade_conversao_item) {
		this.itp_tx_unidade_conversao_item = itp_tx_unidade_conversao_item;
	}
	/**
	 * @return the itp_tx_valor_unitario
	 */
	public String getItp_tx_valor_unitario() {
		return itp_tx_valor_unitario;
	}
	/**
	 * @param itp_tx_valor_unitario the itp_tx_valor_unitario to set
	 */
	public void setItp_tx_valor_unitario(String itp_tx_valor_unitario) {
		this.itp_tx_valor_unitario = itp_tx_valor_unitario;
	}
	
	public String getItp_tx_valor_unitario_formatado() {
		return getValorUnitarioFormatado();
	}
	public void setItp_tx_valor_unitario_formatado(String itp_tx_valor_unitario_formatado) {
		this.itp_tx_valor_unitario_formatado = getValorUnitarioFormatado();
	}
	/**
	 * @return the itp_tx_tipo_item
	 */
	public String getItp_tx_tipo_item() {
		return itp_tx_tipo_item;
	}
	/**
	 * @param itp_tx_tipo_item the itp_tx_tipo_item to set
	 */
	public void setItp_tx_tipo_item(String itp_tx_tipo_item) {
		this.itp_tx_tipo_item = itp_tx_tipo_item;
	}
	/**
	 * @return the itp_tx_desc_tipo_item
	 */
	public String getItp_tx_desc_tipo_item() {
		return itp_tx_desc_tipo_item;
	}
	/**
	 * @param itp_tx_desc_tipo_item the itp_tx_desc_tipo_item to set
	 */
	public void setItp_tx_desc_tipo_item(String itp_tx_desc_tipo_item) {
		this.itp_tx_desc_tipo_item = itp_tx_desc_tipo_item;
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
	/**
	 * @return the itp_tx_quantidade
	 */
	public String getItp_tx_quantidade() {
		return itp_tx_quantidade;
	}
	/**
	 * @param itp_tx_quantidade the itp_tx_quantidade to set
	 */
	public void setItp_tx_quantidade(String itp_tx_quantidade) {
		this.itp_tx_quantidade = itp_tx_quantidade;
	}
		
	/**
	 * @return the pit_pe_item_padrao
	 */
	public String getPit_pe_item_padrao() {
		return pit_pe_item_padrao;
	}
	/**
	 * @param pit_pe_item_padrao the pit_pe_item_padrao to set
	 */
	public void setPit_pe_item_padrao(String pit_pe_item_padrao) {
		this.pit_pe_item_padrao = pit_pe_item_padrao;
	}
	/**
	 * @return the cdtransito
	 */
	public String getCdtransito() {
		return cdtransito;
	}
	/**
	 * @param cdtransito the cdtransito to set
	 */
	public void setCdtransito(String cdtransito) {
		this.cdtransito = cdtransito;
	}
	/**
	 * @return the marcado
	 */
	public boolean isMarcado() {
		return marcado;
	}
	/**
	 * @param marcado the marcado to set
	 */
	public void setMarcado(boolean marcado) {
		this.marcado = marcado;
	}
	/**
	 * @return the umconv
	 */
	public String getUmconv() {
		return umconv;
	}
	/**
	 * @param umconv the umconv to set
	 */
	public void setUmconv(String umconv) {
		this.umconv = umconv;
	}
	
	
	/**
	 * @return the councs
	 */
	public String getCouncs() {
		return councs;
	}
	/**
	 * @param councs the councs to set
	 */
	public void setCouncs(String councs) {
		this.councs = councs;
	}
	/**
	 * @return the umrum
	 */
	public String getUmrum() {
		return umrum;
	}
	/**
	 * @param umrum the umrum to set
	 */
	public void setUmrum(String umrum) {
		this.umrum = umrum;
	}
	/**
	 * @return the umum
	 */
	public String getUmum() {
		return umum;
	}
	/**
	 * @param umum the umum to set
	 */
	public void setUmum(String umum) {
		this.umum = umum;
	}
	
	public String getUmcnv1() {
		return umcnv1;
	}
	public void setUmcnv1(String umcnv1) {
		this.umcnv1 = umcnv1;
	}
	
	public String getValorUnitarioFormatado(){
		DecimalFormat df2 = new DecimalFormat( "#,###,###,##0.00" );
		String retorno = "0.00";
		if(getItp_tx_valor_unitario() != null){
			Double dd2dec = Double.parseDouble(getItp_tx_valor_unitario());
			String dd2decstr = df2.format(dd2dec);
			retorno = dd2decstr ;
		//	retorno = getItp_tx_valor_unitario();
		}
		return retorno;
	}
	
	public String getUnidMedFormatada() {
		return this.unidMedFormatada;
	}
	
	public void setUnidMedFormatada(String unidMedFormatada) {
		this.unidMedFormatada = "1 " + itp_tx_unidade_item + " - " + umcnv1 + " " + itp_tx_unidade_conversao_item;
	}
		
}
