package br.com.correios.ppjsiscap.enums;

/**
 * Enum usado para as acoes do componente picklist
 * @author 81095228
 *
 */
public enum TipoAcao {
	A("Adicionar"),
	AT("Adicionar Todos"),
	R("Remover"),
	RT("Remover Todos");
	
	private String acao;

	public String getAcao() {
		return acao;
	}

	private TipoAcao(String acao) {
		this.acao = acao;
	}
		
}
