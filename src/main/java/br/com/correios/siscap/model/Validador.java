package br.com.correios.siscap.model;


public class Validador {

	// /////// CLASSE QUE REPRESENTA A LISTA DE ITENS DO PEDIDO /////

	private String EAKCO; // Código da Cia
	
	
	private String EAMCU; // MCU da Cia
	
	
	private String EAMCU1; // MCU Solicitante
	
	
	private String EATRDJ; // Data Juliana do Pedido
	
	
	private String EALITM; // Código Longo do ItemPedido
	
	
	private String EAUORG; // Quantidade do ItemPedido
	
	
	private String EAT001; // Natureza do Pedido (Produto/Material)
	
	
	private String EAT002; // Tipo do Pedido (Extra / Normal)
	
	
	private String EAUOM; // Unidade de Medida do ItemPedido
	
	
	private String EAAA25; // Número do Pedido SISCAP
	
	
	private String EAEDSP; // Status de Processamento do ItemPedido
	
	
	private String EAUPMJ; // Data da Atualização no Banco de Dados
	
	
	private String EACKEDSP; // Campo de Controle (Em Branco)
	
	
	private String EAUSER = "WEBPEDIDOS"; // Fixo (WEBPEDIDOS)
	
	
	private String EAPID = "SISCAP_ERP"; // SISCAP_ERP
	
	
	private String EAJOBN; // Identificação Estação de trabalho
	
	
	private String EATDAY; // Hora da Atualização no Banco de Dados
	
	private String IMDSC1; // Descrição do item na view vw_f4101
	
	private String COUNCS; // custo unitário da VW_F4105
	
	private String UMCONV; // fator de conversao da VW_F41002
	
	private String UMRUM; // unidade de medida de origem da VW_F41002
	
	private String UMUM; // unidade de medida de destino da VW_F41002
	
	private String descricaoEAEDSP; // Status de Processamento do ItemPedido
	
	private boolean novoItem;
	
    private String MCDSC1;
	
	private String MCRP01;

	public Double valorUnitario(){
		double valorUnitario = 0d;
		double valorCustoUnitario = Double.parseDouble(getCOUNCS());
		double valorFatorConversao = Double.parseDouble(getUMCONV());
		
		if ((getUMRUM().indexOf("UN") > -1) && (getUMUM().indexOf(getEAUOM())) > -1) {
			valorUnitario = (valorCustoUnitario / 10000)	* (valorFatorConversao / 10000000);
		} else {
			valorUnitario = (valorCustoUnitario / 10000);
		}
		return valorUnitario;
	}
		
	

	/**
	 * @return the eAKCO
	 */
	public String getEAKCO() {
		return EAKCO;
	}

	/**
	 * @param eAKCO
	 *            the eAKCO to set
	 */
	public void setEAKCO(String eAKCO) {
		EAKCO = eAKCO;
	}

	/**
	 * @return the eAMCU
	 */
	public String getEAMCU() {
		return EAMCU;
	}

	/**
	 * @param eAMCU
	 *            the eAMCU to set
	 */
	public void setEAMCU(String eAMCU) {
		EAMCU = eAMCU;
	}

	/**
	 * @return the eAMCU1
	 */
	public String getEAMCU1() {
		return EAMCU1;
	}

	/**
	 * @param eAMCU1
	 *            the eAMCU1 to set
	 */
	public void setEAMCU1(String eAMCU1) {
		EAMCU1 = eAMCU1;
	}

	/**
	 * @return the eATRDJ
	 */
	public String getEATRDJ() {
		return EATRDJ;
	}

	/**
	 * @param eATRDJ
	 *            the eATRDJ to set
	 */
	public void setEATRDJ(String eATRDJ) {
		EATRDJ = eATRDJ;
	}

	/**
	 * @return the eALITM
	 */
	public String getEALITM() {
		return EALITM;
	}

	/**
	 * @param eALITM
	 *            the eALITM to set
	 */
	public void setEALITM(String eALITM) {
		EALITM = eALITM;
	}

	/**
	 * @return the eAUORG
	 */
	public String getEAUORG() {
		return EAUORG;
	}

	/**
	 * @param eAUORG
	 *            the eAUORG to set
	 */
	public void setEAUORG(String eAUORG) {
		EAUORG = eAUORG;
	}

	/**
	 * @return the eAT001
	 */
	public String getEAT001() {
		return EAT001;
	}

	/**
	 * @param eAT001
	 *            the eAT001 to set
	 */
	public void setEAT001(String eAT001) {
		EAT001 = eAT001;
	}

	/**
	 * @return the eAT002
	 */
	public String getEAT002() {
		return EAT002;
	}

	/**
	 * @param eAT002
	 *            the eAT002 to set
	 */
	public void setEAT002(String eAT002) {
		EAT002 = eAT002;
	}

	/**
	 * @return the eAUOM
	 */
	public String getEAUOM() {
		return EAUOM;
	}

	/**
	 * @param eAUOM
	 *            the eAUOM to set
	 */
	public void setEAUOM(String eAUOM) {
		EAUOM = eAUOM;
	}

	/**
	 * @return the eAAA25
	 */
	public String getEAAA25() {
		return EAAA25;
	}

	/**
	 * @param eAAA25
	 *            the eAAA25 to set
	 */
	public void setEAAA25(String eAAA25) {
		EAAA25 = eAAA25;
	}

	/**
	 * @return the eAEDSP
	 */
	public String getEAEDSP() {
		return EAEDSP;
	}

	/**
	 * @param eAEDSP
	 *            the eAEDSP to set
	 */
	public void setEAEDSP(String eAEDSP) {
		EAEDSP = eAEDSP;
	}

	/**
	 * @return the eAUPMJ
	 */
	public String getEAUPMJ() {
		return EAUPMJ;
	}

	/**
	 * @param eAUPMJ
	 *            the eAUPMJ to set
	 */
	public void setEAUPMJ(String eAUPMJ) {
		EAUPMJ = eAUPMJ;
	}

	/**
	 * @return the eACKEDSP
	 */
	public String getEACKEDSP() {
		return EACKEDSP;
	}

	/**
	 * @param eACKEDSP
	 *            the eACKEDSP to set
	 */
	public void setEACKEDSP(String eACKEDSP) {
		EACKEDSP = eACKEDSP;
	}

	/**
	 * @return the eAUSER
	 */
	public String getEAUSER() {
		return EAUSER;
	}

	/**
	 * @param eAUSER
	 *            the eAUSER to set
	 */
	public void setEAUSER(String eAUSER) {
		EAUSER = eAUSER;
	}

	/**
	 * @return the eAPID
	 */
	public String getEAPID() {
		return EAPID;
	}

	/**
	 * @param eAPID
	 *            the eAPID to set
	 */
	public void setEAPID(String eAPID) {
		EAPID = eAPID;
	}

	/**
	 * @return the eAJOBN
	 */
	public String getEAJOBN() {
		return EAJOBN;
	}

	/**
	 * @param eAJOBN
	 *            the eAJOBN to set
	 */
	public void setEAJOBN(String eAJOBN) {
		EAJOBN = eAJOBN;
	}

	/**
	 * @return the eATDAY
	 */
	public String getEATDAY() {
		return EATDAY;
	}

	/**
	 * @param eATDAY the eATDAY to set
	 */
	public void setEATDAY(String eATDAY) {
		EATDAY = eATDAY;
	}

	/**
	 * @return the iMDSC1
	 */
	public String getIMDSC1() {
		return IMDSC1;
	}

	/**
	 * @param iMDSC1 the iMDSC1 to set
	 */
	public void setIMDSC1(String iMDSC1) {
		IMDSC1 = iMDSC1;
	}

	/**
	 * @return the cOUNCS
	 */
	public String getCOUNCS() {
		return COUNCS;
	}

	/**
	 * @param cOUNCS the cOUNCS to set
	 */
	public void setCOUNCS(String cOUNCS) {
		COUNCS = cOUNCS;
	}

	/**
	 * @return the uMCONV
	 */
	public String getUMCONV() {
		return UMCONV;
	}

	/**
	 * @param uMCONV the uMCONV to set
	 */
	public void setUMCONV(String uMCONV) {
		UMCONV = uMCONV;
	}

	/**
	 * @return the uMRUM
	 */
	public String getUMRUM() {
		return UMRUM;
	}

	/**
	 * @param uMRUM the uMRUM to set
	 */
	public void setUMRUM(String uMRUM) {
		UMRUM = uMRUM;
	}

	/**
	 * @return the uMUM
	 */
	public String getUMUM() {
		return UMUM;
	}

	/**
	 * @param uMUM the uMUM to set
	 */
	public void setUMUM(String uMUM) {
		UMUM = uMUM;
	}



	public String getMCDSC1() {
		return MCDSC1;
	}



	public void setMCDSC1(String mCDSC1) {
		MCDSC1 = mCDSC1;
	}



	public String getMCRP01() {
		return MCRP01;
	}



	public void setMCRP01(String mCRP01) {
		MCRP01 = mCRP01;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((EALITM == null) ? 0 : EALITM.hashCode());
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ItemPedido other = (ItemPedido) obj;
		if (EALITM == null) {
			if (other.getEALITM() != null) {
				return false;
			}
		} else if (!EALITM.equals(other.getEALITM())) {
			return false;
		}
		return true;
	}

	/**
	 * @return the novoItem
	 */
	public boolean isNovoItem() {
		return novoItem;
	}

	/**
	 * @param novoItem the novoItem to set
	 */
	public void setNovoItem(boolean novoItem) {
		this.novoItem = novoItem;
	}

	public String getDescricaoEAEDSP() {
		if (EAEDSP.indexOf("S") > -1) {
			setDescricaoEAEDSP("Solicitado");

		}
		if (EAEDSP.indexOf("R") > -1) {
			setDescricaoEAEDSP("Em Aprovação");

		}
		if (EAEDSP.indexOf("V") > -1) {
			setDescricaoEAEDSP("Em Validação");

		}
		if (EAEDSP.indexOf(" ") > -1) {
			setDescricaoEAEDSP("Validado");

		}
		if (EAEDSP.indexOf("Y") > -1) {
			setDescricaoEAEDSP("Processado");

		}
		if (EAEDSP.indexOf("C") > -1) {
			setDescricaoEAEDSP("Cancelado");

		}
		return descricaoEAEDSP;
	}


	public void setDescricaoEAEDSP(String descricaoEAEDSP) {
		this.descricaoEAEDSP = descricaoEAEDSP;
	}
	
	
}
