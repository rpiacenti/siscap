package br.com.correios.ppjsiscap.enums;

public enum Categoria {
	
	P {
		public String toString() {
			return "Produto";
		}
	}, 
	M{
		public String toString() {
			return "Material";
		}
	};
	
	public String getCodigo(){
		String codigo = null;
		if(this == P){
			codigo = "P";
		}
		if(this == M){
			codigo = "M";
		}
		return codigo;
	}

}
