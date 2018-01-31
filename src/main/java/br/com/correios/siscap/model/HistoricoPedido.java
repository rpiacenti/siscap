package br.com.correios.siscap.model;

import java.io.Serializable;

import br.com.correios.ppjsiscap.util.DataERP;

@SuppressWarnings("serial")
public class HistoricoPedido implements Serializable {

	private String SDAN8;
	private String SDDOCO;
	private String REAA15;
	private String REDATE01;
	private String SDDRQJ;
	private String SDIVD;
	private String SDLITM;
	private String SDDSC1;
	private String SDUOM;
	private String SDUORG;
	private String SDQTYT;
	private String VIN_CO;
	private String REGISTRADO;
	private String NUMNOTA;
	private String SDLTTR;
	private String SDDOC;
	private String SDVR01;
	private String SDVR02;
	
	
	
	
	public HistoricoPedido() {
		
	}


	public HistoricoPedido(String sDAN8, String sDDOCO, String rEAA15,
			String rEDATE01, String sDDRQJ, String sDIVD, String sDLITM,
			String sDDSC1, String sDUOM, String sDUORG, String sDQTYT, String sVIN_CO,
			String rEGISTRADO, String nUMNOTA, String nSDLTTR, String nSDDOC, String nSDVR01, String nSDVR02) {
		super();
		SDAN8 = sDAN8;
		SDDOCO = sDDOCO;
		REAA15 = rEAA15;
		REDATE01 = rEDATE01;
		SDDRQJ = sDDRQJ;
		SDIVD = sDIVD;
		SDLITM = sDLITM;
		SDDSC1 = sDDSC1;
		SDUOM = sDUOM;
		SDUORG = sDUORG;
		SDQTYT = sDQTYT;
		VIN_CO = sVIN_CO;
		REGISTRADO = rEGISTRADO;
		NUMNOTA = nUMNOTA;
		SDLTTR = nSDLTTR;
		SDDOC = nSDDOC;
		SDVR01 = nSDVR01;
		SDVR02 = nSDVR02;
	}





	/**
	 * @return the sDAN8
	 */
	public String getSDAN8() {
		return SDAN8;
	}



	/**
	 * @param sDAN8 the sDAN8 to set
	 */
	public void setSDAN8(String sDAN8) {
		SDAN8 = sDAN8;
	}



	/**
	 * @return the sDDOCO
	 */
	public String getSDDOCO() {
		return SDDOCO;
	}



	/**
	 * @param sDDOCO the sDDOCO to set
	 */
	public void setSDDOCO(String sDDOCO) {
		SDDOCO = sDDOCO;
	}



	/**
	 * @return the rEAA15
	 */
	public String getREAA15() {
		return REAA15;
	}



	/**
	 * @param rEAA15 the rEAA15 to set
	 */
	public void setREAA15(String rEAA15) {
		REAA15 = rEAA15;
	}



	/**
	 * @return the rEDATE01
	 */
	public String getREDATE01() {
		if (this.REDATE01 != null) {
			if (!this.REDATE01.equals("N/D")) {
				DataERP dtERP = new DataERP(this.REDATE01);
				return dtERP.getDataGregoriana();
			} else {
				return this.REDATE01;
			}
		} else {
			return "N/D";
		}

	}



	/**
	 * @param rEDATE01 the rEDATE01 to set
	 */
	public void setREDATE01(String rEDATE01) {
		REDATE01 = rEDATE01;
	}



	/**
	 * @return the sDDRQJ
	 */
	public String getSDDRQJ() {
		if(!this.SDDRQJ.equals("0")){
		DataERP dtERP = new DataERP(this.SDDRQJ);
		return dtERP.getDataGregoriana();
		}else{
			return "-";
		}
	}



	/**
	 * @param sDDRQJ the sDDRQJ to set
	 */
	public void setSDDRQJ(String sDDRQJ) {
		SDDRQJ = sDDRQJ;
	}



	/**
	 * @return the sDIVD
	 */
	public String getSDIVD() {
		if(!this.SDIVD.equals("0")){
			DataERP dtERP = new DataERP(this.SDIVD);
			return dtERP.getDataGregoriana();
		}else{
			return "-";
		}
		
	}



	/**
	 * @param sDIVD the sDIVD to set
	 */
	public void setSDIVD(String sDIVD) {
		SDIVD = sDIVD;
	}



	/**
	 * @return the sDLITM
	 */
	public String getSDLITM() {
		return SDLITM;
	}



	/**
	 * @param sDLITM the sDLITM to set
	 */
	public void setSDLITM(String sDLITM) {
		SDLITM = sDLITM;
	}



	/**
	 * @return the sDDSC1
	 */
	public String getSDDSC1() {
		return SDDSC1;
	}



	/**
	 * @param sDDSC1 the sDDSC1 to set
	 */
	public void setSDDSC1(String sDDSC1) {
		SDDSC1 = sDDSC1;
	}



	/**
	 * @return the sDUOM
	 */
	public String getSDUOM() {
		return SDUOM;
	}



	/**
	 * @param sDUOM the sDUOM to set
	 */
	public void setSDUOM(String sDUOM) {
		SDUOM = sDUOM;
	}



	/**
	 * @return the sDUORG
	 */
	public String getSDUORG() {
		return SDUORG;
	}



	/**
	 * @param sDUORG the sDUORG to set
	 */
	public void setSDUORG(String sDUORG) {
		SDUORG = sDUORG;
	}



	/**
	 * @return the sDQTYT
	 */
	public String getSDQTYT() {
		return SDQTYT;
	}



	/**
	 * @param sDQTYT the sDQTYT to set
	 */
	public void setSDQTYT(String sDQTYT) {
		SDQTYT = sDQTYT;
	}



	/**
	 * @return the rEGISTRADO
	 */
	public String getREGISTRADO() {
		return REGISTRADO;
	}



	/**
	 * @param rEGISTRADO the rEGISTRADO to set
	 */
	public void setREGISTRADO(String rEGISTRADO) {
		REGISTRADO = rEGISTRADO;
	}


	/**
	 * @return the vIN_CO
	 */
	public String getVIN_CO() {
		return VIN_CO;
	}


	/**
	 * @param vIN_CO the vIN_CO to set
	 */
	public void setVIN_CO(String vIN_CO) {
		VIN_CO = vIN_CO;
	}


	/**
	 * @return the nUMNOTA
	 */
	public String getNUMNOTA() {
		return NUMNOTA;
	}


	/**
	 * @param nUMNOTA the nUMNOTA to set
	 */
	public void setNUMNOTA(String nUMNOTA) {
		NUMNOTA = nUMNOTA;
	}


	/**
	 * @return the sDLTTR
	 */
	public String getSDLTTR() {
		return SDLTTR;
	}


	/**
	 * @param sDLTTR the sDLTTR to set
	 */
	public void setSDLTTR(String sDLTTR) {
		SDLTTR = sDLTTR;
	}


	/**
	 * @return the sDDOC
	 */
	public String getSDDOC() {
		return SDDOC;
	}


	/**
	 * @param sDDOC the sDDOC to set
	 */
	public void setSDDOC(String sDDOC) {
		SDDOC = sDDOC;
	}


	/**
	 * @return the sDVR01
	 */
	public String getSDVR01() {
		return SDVR01;
	}


	/**
	 * @param sDVR01 the sDVR01 to set
	 */
	public void setSDVR01(String sDVR01) {
		SDVR01 = sDVR01;
	}


	/**
	 * @return the sDVR02
	 */
	public String getSDVR02() {
		return SDVR02;
	}


	/**
	 * @param sDVR02 the sDVR02 to set
	 */
	public void setSDVR02(String sDVR02) {
		SDVR02 = sDVR02;
	}
	
	
	
}
