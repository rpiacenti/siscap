package br.com.correios.ppjsiscap.enums;

public enum Sexo {
	Masculino("M"),Feminino("F");
	
	private String codigo;
	
	private Sexo(String sexo) {
		codigo = sexo;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}
	
	
}
