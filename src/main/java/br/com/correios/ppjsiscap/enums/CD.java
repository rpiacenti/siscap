/**
 * 
 */
package br.com.correios.ppjsiscap.enums;

/**
 * @author Ronald Piacenti Junior
 *
 */
public enum CD {
	CDLESTE("00049748","CD Leste"),
	CDOESTE("00004010","CD Oeste");
	
	
	private String codigo;
	private String descricao;
	
	private CD(String codigo, String descricao) {
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



