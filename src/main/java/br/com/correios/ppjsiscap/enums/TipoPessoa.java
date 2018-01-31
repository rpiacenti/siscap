package br.com.correios.ppjsiscap.enums;

public enum TipoPessoa {

	FISICA("1"),
	JURIDICA("2");
	
	
	private final String sigla;
	
	public String getSigla() {
		return sigla;
	}
	
	private TipoPessoa(String sigla){
		this.sigla = sigla;
	}	
	
	
}
