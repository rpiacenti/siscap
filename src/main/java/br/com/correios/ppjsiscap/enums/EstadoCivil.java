package br.com.correios.ppjsiscap.enums;

public enum EstadoCivil {
	
	SOLTEIRO(1,"Solteiro(a)"),
	CASADO(2,"Casado(a)"),
	VIUVO(3,"Viúvo(a)"),
	DIVORCIADO(4,"Divorciado(a)");
	
	private Integer codigo;
	private String descricao;
	
	private EstadoCivil(Integer codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		return codigo;
	}

	
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
		
}
