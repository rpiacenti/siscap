package br.com.correios.ppjsiscap.enums;

public enum Modulo {
	
	CLIENTE("Cliente"), 
	DEPENDENTE("Dependente");
	
	private String descModulo;
	
	Modulo(String desc){
		descModulo = desc;
	}
	
	public String getDescModulo() {
		return descModulo;
	}

	public void setDescModulo(String descModulo) {
		this.descModulo = descModulo;
	}	
	
}
