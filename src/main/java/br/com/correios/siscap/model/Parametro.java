package br.com.correios.siscap.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Parametro implements Serializable{
	
	private Long par_nu;
		
	private String mcmcu_centro_distribuicao;
	
	
	private String ccco_dr;
	
	
	private String gat_nu;
	
	
	private String drky_tipo_orgao;
	
	
	private String par_no;
	
	
	private String par_in_natureza;
	
	
	private Long par_vr;
	
	
	private Date par_dt_vigencia_inicial;
	
	
	private Date par_dt_vigencia_final;
	
	
	private String par_mcu;
	
	
	/**
	 * @return the par_nu
	 */
	public Long getPar_nu() {
		return par_nu;
	}
	/**
	 * @param long1 the par_nu to set
	 */
	public void setPar_nu(Long long1) {
		this.par_nu = long1;
	}
	/**
	 * @return the mcmcu_centro_distribuicao
	 */
	public String getMcmcu_centro_distribuicao() {
		return mcmcu_centro_distribuicao;
	}
	/**
	 * @param mcmcu_centro_distribuicao the mcmcu_centro_distribuicao to set
	 */
	public void setMcmcu_centro_distribuicao(String mcmcu_centro_distribuicao) {
		this.mcmcu_centro_distribuicao = mcmcu_centro_distribuicao;
	}
	/**
	 * @return the ccco_dr
	 */
	public String getCcco_dr() {
		return ccco_dr;
	}
	/**
	 * @param ccco_dr the ccco_dr to set
	 */
	public void setCcco_dr(String ccco_dr) {
		this.ccco_dr = ccco_dr;
	}
	/**
	 * @return the gat_nu
	 */
	public String getGat_nu() {
		return gat_nu;
	}
	/**
	 * @param gat_nu the gat_nu to set
	 */
	public void setGat_nu(String gat_nu) {
		this.gat_nu = gat_nu;
	}
	/**
	 * @return the drky_tipo_orgao
	 */
	public String getDrky_tipo_orgao() {
		return drky_tipo_orgao;
	}
	/**
	 * @param drky_tipo_orgao the drky_tipo_orgao to set
	 */
	public void setDrky_tipo_orgao(String drky_tipo_orgao) {
		this.drky_tipo_orgao = drky_tipo_orgao;
	}
	/**
	 * @return the par_no
	 */
	public String getPar_no() {
		return par_no;
	}
	/**
	 * @param par_no the par_no to set
	 */
	public void setPar_no(String par_no) {
		this.par_no = par_no;
	}
	/**
	 * @return the par_in_natureza
	 */
	public String getPar_in_natureza() {
		return par_in_natureza;
	}
	/**
	 * @param par_in_natureza the par_in_natureza to set
	 */
	public void setPar_in_natureza(String par_in_natureza) {
		this.par_in_natureza = par_in_natureza;
	}
	/**
	 * @return the par_vr
	 */
	public Long getPar_vr() {
		return par_vr;
	}
	/**
	 * @param par_vr the par_vr to set
	 */
	public void setPar_vr(Long par_vr) {
		this.par_vr = par_vr;
	}
	/**
	 * @return the par_dt_vigencia_inicial
	 */
	public Date getPar_dt_vigencia_inicial() {
		return par_dt_vigencia_inicial;
	}
	/**
	 * @param par_dt_vigencia_inicial the par_dt_vigencia_inicial to set
	 */
	public void setPar_dt_vigencia_inicial(Date par_dt_vigencia_inicial) {
		this.par_dt_vigencia_inicial = par_dt_vigencia_inicial;
	}
	/**
	 * @return the par_dt_vigencia_final
	 */
	public Date getPar_dt_vigencia_final() {
		return par_dt_vigencia_final;
	}
	/**
	 * @param par_dt_vigencia_final the par_dt_vigencia_final to set
	 */
	public void setPar_dt_vigencia_final(Date par_dt_vigencia_final) {
		this.par_dt_vigencia_final = par_dt_vigencia_final;
	}
	public String getPar_mcu() {
		return par_mcu;
	}
	public void setPar_mcu(String par_mcu) {
		this.par_mcu = par_mcu;
	}

	
}
