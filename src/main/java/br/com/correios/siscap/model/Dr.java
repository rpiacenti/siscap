package br.com.correios.siscap.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Dr implements Serializable {
	
	private String CCCO; 
	
	private String CCNAME; 
	
	private String CCAN8;
	
	private String MCUCD;
	
	private String MCRP01;
	
	
	/**
	 * @return the cCCO
	 */
	public String getCCCO() {
		return CCCO;
	}
	/**
	 * @param cCCO the cCCO to set
	 */
	public void setCCCO(String cCCO) {
		CCCO = cCCO;
	}
	/**
	 * @return the cCNAME
	 */
	public String getCCNAME() {
		return CCNAME;
	}
	/**
	 * @param cCNAME the cCNAME to set
	 */
	public void setCCNAME(String cCNAME) {
		CCNAME = cCNAME;
	}
	/**
	 * @return the cCAN8
	 */
	public String getCCAN8() {
		return CCAN8;
	}
	/**
	 * @param cCAN8 the cCAN8 to set
	 */
	public void setCCAN8(String cCAN8) {
		CCAN8 = cCAN8;
	}
	/**
	 * @return the mCUCD
	 */
	public String getMCUCD() {
		return MCUCD;
	}
	/**
	 * @param mCUCD the mCUCD to set
	 */
	public void setMCUCD(String mCUCD) {
		MCUCD = mCUCD;
	}
	/**
	 * @return the mCRP01
	 */
	public String getMCRP01() {
		return MCRP01;
	}
	/**
	 * @param mCRP01 the mCRP01 to set
	 */
	public void setMCRP01(String mCRP01) {
		MCRP01 = mCRP01;
	}
	
	

}
