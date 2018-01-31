package br.com.correios.ppjsiscap.enums;

public enum TIPOPEDIDO {
	EXTRA("E","Extra"),
	NORMAL("N","Normal");
	
	
	private String codigo;
	private String descricao;
	
	private TIPOPEDIDO(String codigo, String descricao) {
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
