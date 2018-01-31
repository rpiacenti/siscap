package br.com.correios.ppjsiscap.enums;

public enum TIPOCONTRATO {
	
	ADM_TCO("01","ADM","ADM-TCO"),
	DIR_TCO("02","ADM","DIR-TCO"),
	DR_TCO("03","ADM","DR-TCO"),
	CDD_TCO("04","OPE","CDD-TCO"),
	CTE_TCO("05","OPE","CTE-TCO"),
	CTCE_TCO("06","OPE","CTCE-TCO"),
	CST_TCO("07","OPE","CST-TCO"),
	CTC_TCO("08","OPE","CTC-TCO"),
	AC_TCO("09","OPE","AC-TCO"),
	AMB_TCO("10","ADM","AMB-TCO"),
	AGF_TCO("12","OPE","AGF-TCO"),
	TECA_TCO("13","OPE","TECA-TCO"),
	REOP_TCO("14","ADM","REOP-TCO"),
	ACS_TCO("15","OPE","ACS-TCO"),
	ACF_TCO("16","OPE","ACF-TCO"),
	CAT_TCO("17","OPE","CAT-TCO"),
	PC_TCO("18","OPE","PC-TCO"),
	CLI_TCO("19","OPE","CLI-TCO"),
	AF_TCO("20","OPE","AF-TCO"),
	AGC_TCO("21","OPE","AGC-TCO"),
	CTO_TCO("22","OPE","CTO-TCO"),
	DRD_TCO("23","ADM","DRD-TCO"),
	PVP_TCO("24","OPE","PVP-TCO"),
	ACCI_TCO("25,27","OPE","ACCI-TCO"),
	ACCII_TCO("26,28","OPE","ACCII-TCO"),
	CEE_TCO("29","OPE","CEE-TCO"),
	CTCI_TCO("30","OPE","CTCI-TCO");
			
	private String codigo;
	private String natureza;
	private String descricao;
	
	private TIPOCONTRATO(String codigo, String natureza, String descricao) {
		this.codigo = codigo;
		this.natureza = natureza;
		this.descricao = descricao;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	
	/**
	 * @return the natureza
	 */
	public String getNatureza() {
		return natureza;
	}

	/**
	 * @param natureza the natureza to set
	 */
	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
}
