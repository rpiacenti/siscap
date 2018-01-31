package br.com.correios.siscap.model;

import java.io.Serializable;


@SuppressWarnings("serial")
public class Orgao implements Serializable {

	private String mcu;
	private String an8;
	private String nome;
	private String sigla;
	private String tipo;
	private String dr;
	private String uf;
    private String natureza;
    private String email;
    private String status;
    private String mcucia;
    private Boolean marcado = false;
    private int grptransito;
    private String cdtransito;
    private long pag_atual;
   
    
    
    public Orgao(){
    	
    }
	
	
	public Orgao(String mcu, String tipo, String nome, String dr, String natureza, Boolean marcado) {
		super();
		this.mcu = mcu;
		this.tipo = tipo;
		this.nome = nome;
		this.dr = dr;
		this.natureza = natureza;
		this.marcado = marcado;
	}

	/**
	 * @return the mcu
	 */
	public String getMcu() {
		return mcu;
	}

	/**
	 * @param mcu
	 *            the mcu to set
	 */
	public void setMcu(String mcu) {
		this.mcu = mcu;
	}

	/**
	 * @return the an8
	 */
	public String getAn8() {
		return an8;
	}

	/**
	 * @param an8 the an8 to set
	 */
	public void setAn8(String an8) {
		this.an8 = an8;
	}

	/**
	 * @return the uf
	 */
	public String getUf() {
		return uf;
	}

	/**
	 * @param uf the uf to set
	 */
	public void setUf(String uf) {
		this.uf = uf;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the dr
	 */
	public String getDr() {
		return dr;
	}

	/**
	 * @param dr
	 *            the dr to set
	 */
	public void setDr(String dr) {
		this.dr = dr;
	}
	

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the natureza
	 */
	public String getNatureza() {
		return natureza;
	}

	/**
	 * @param natureza the natureza to set
	 */
	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}
	
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the marcado
	 */
	public Boolean getMarcado() {
		return marcado;
	}

	/**
	 * @param marcado the marcado to set
	 */
	public void setMarcado(Boolean marcado) {
		this.marcado = marcado;
	}

	/**
	 * @return the grptransito
	 */
	public int getGrptransito() {
		return grptransito;
	}

	/**
	 * @param grptransito the grptransito to set
	 */
	public void setGrptransito(int grptransito) {
		this.grptransito = grptransito;
	}

	/**
	 * @return the cdtransito
	 */
	public String getCdtransito() {
		return cdtransito;
	}

	/**
	 * @param cdtransito the cdtransito to set
	 */
	public void setCdtransito(String cdtransito) {
		this.cdtransito = cdtransito;
	}

	/**
	 * @return the pag_atual
	 */
	public long getPag_atual() {
		return pag_atual;
	}

	/**
	 * @param pag_atual the pag_atual to set
	 */
	public void setPag_atual(long pag_atual) {
		this.pag_atual = pag_atual;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the mcucia
	 */
	public String getMcucia() {
		return mcucia;
	}

	/**
	 * @param mcucia the mcucia to set
	 */
	public void setMcucia(String mcucia) {
		this.mcucia = mcucia;
	}


	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}


	/**
	 * @param sigla the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	

}
