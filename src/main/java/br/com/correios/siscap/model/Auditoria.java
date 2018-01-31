package br.com.correios.siscap.model;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Auditoria implements Serializable {

	private String 	aud_nu; 					//AUD_NU							NUMBER	6
	private String 	aud_tx_usuario;  			//AUD_TX_USUARIO					VARCHAR2	30
	private String 	aud_tx_funcionalidade; 		//AUD_TX_FUNCIONALIDADE				VARCHAR2	100
	private String 	aud_no_entidade; 			//AUD_NO_ENTIDADE					VARCHAR2	50
	private String	aud_dh_execucao; 			//AUD_DH_EXECUCAO					DATE	0
	private String 	aud_tx_identidade_registro; //AUD_TX_IDENTIDADE_REGISTRO		VARCHAR2	20
	private String	aud_tx_registro; 			//AUD_TX_REGISTRO					VARCHAR2	4000

	public Auditoria(String p_aud_nu, String p_aud_tx_usuario, String p_aud_tx_funcionalidade, String p_aud_no_entidade, String p_aud_dh_execucao, String p_aud_tx_identidade_registro, String p_aud_tx_registro) {
		super();

		this.aud_nu = p_aud_nu;
		this.aud_tx_usuario = p_aud_tx_usuario; 
		this.aud_tx_funcionalidade = p_aud_tx_funcionalidade; 
		this.aud_no_entidade = p_aud_no_entidade;
		this.aud_dh_execucao = p_aud_dh_execucao; 
		this.aud_tx_identidade_registro = p_aud_tx_identidade_registro; 
		this.aud_tx_registro = p_aud_tx_registro;		
	}
	
	public Auditoria() {
		// TODO Auto-generated constructor stub
	}

	public String getAud_nu() {
		return aud_nu;
	}

	public void setAud_nu(String aud_nu) {
		this.aud_nu = aud_nu;
	}

	public String getAud_tx_usuario() {
		return aud_tx_usuario;
	}

	public void setAud_tx_usuario(String aud_tx_usuario) {
		this.aud_tx_usuario = aud_tx_usuario;
	}

	public String getAud_tx_funcionalidade() {
		return aud_tx_funcionalidade;
	}

	public void setAud_tx_funcionalidade(String aud_tx_funcionalidade) {
		this.aud_tx_funcionalidade = aud_tx_funcionalidade;
	}

	public String getAud_no_entidade() {
		return aud_no_entidade;
	}

	public void setAud_no_entidade(String aud_no_entidade) {
		this.aud_no_entidade = aud_no_entidade;
	}

	public String getAud_dh_execucao() {
		return aud_dh_execucao;
	}

	public void setAud_dh_execucao(String aud_dh_execucao) {
		this.aud_dh_execucao = aud_dh_execucao;
	}

	public String getAud_tx_identidade_registro() {
		return aud_tx_identidade_registro;
	}

	public void setAud_tx_identidade_registro(String aud_tx_identidade_registro) {
		this.aud_tx_identidade_registro = aud_tx_identidade_registro;
	}

	public String getAud_tx_registro() {
		return aud_tx_registro;
	}

	public void setAud_tx_registro(String aud_tx_registro) {
		this.aud_tx_registro = aud_tx_registro;
	}
	
	

}
