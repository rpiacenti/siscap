package br.com.correios.ppjsiscap.enums;

public enum NATUREZAORGAO {
	
	ADMINISTRATIVO("'02','01','10','11','23','03','14','33','34','35','36'","Administrativo"),
	OPERACIONAL("'04','05','06','07','08','13','17','19','22','29','30','31','32'","Operacional"),
	REDE("'09','20','25','26'","Rede Atendimento"),
	TERCEIRO_ACF("'16'","Terceiro ACF"),
	TERCEIRO_AGF("'12'","Terceiro AGF"),
	TERCEIRO_ACC("'27','28'","Terceiro ACC"),
	ALL("'02','01','10','23','03','14','04','05','06','07','08','11','13','17','19','20','22','29','30','09','25','26','16','12','27','28','31','32','33','34','35','36'","Todos");
	
	private String codigo;
	private String descricao;
	
	private NATUREZAORGAO(String codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
}
