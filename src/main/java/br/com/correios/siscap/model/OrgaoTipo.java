package br.com.correios.siscap.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OrgaoTipo implements Serializable{
	
	
	private String DRKY; 
	
	private String DRDL01;
	
	private String DRDL02;
	
	private String DRSPHD;
	
	
	/**
	 * @return the dRKY
	 */
	public String getDRKY() {
		return DRKY;
	}
	/**
	 * @param dRKY the dRKY to set
	 */
	public void setDRKY(String dRKY) {
		DRKY = dRKY;
	}
	/**
	 * @return the dRDL01
	 */
	public String getDRDL01() {
		return DRDL01;
	}
	/**
	 * @param dRDL01 the dRDL01 to set
	 */
	public void setDRDL01(String dRDL01) {
		DRDL01 = dRDL01;
	}
	/**
	 * @return the dRDL02
	 */
	public String getDRDL02() {
		return DRDL02;
	}
	/**
	 * @param dRDL02 the dRDL02 to set
	 */
	public void setDRDL02(String dRDL02) {
		DRDL02 = dRDL02;
	}
	/**
	 * @return the dRSPHD
	 */
	public String getDRSPHD() {
		return DRSPHD;
	}
	/**
	 * @param dRSPHD the dRSPHD to set
	 */
	public void setDRSPHD(String dRSPHD) {
		DRSPHD = dRSPHD;
	}

	
	
	
}
