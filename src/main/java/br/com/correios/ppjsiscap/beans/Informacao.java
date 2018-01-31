package br.com.correios.ppjsiscap.beans;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Informacao {

	private String cabecalho;
	private String[] paragrafos;
	private String[] lista;

	/**
	 * @return the cabecalho
	 */
	public String getCabecalho() {
		return cabecalho;
	}

	/**
	 * @param cabecalho
	 *            the cabecalho to set
	 */
	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	/**
	 * @return the paragrafos
	 */
	public String[] getParagrafos() {
		return paragrafos;
	}

	/**
	 * @param paragrafos
	 *            the paragrafos to set
	 */
	public void setParagrafos(String[] paragrafos) {
		if (paragrafos == null) {
			this.paragrafos = new String[0];
		} else {
			this.paragrafos = Arrays.copyOf(paragrafos, paragrafos.length);
		}
	}

	/**
	 * @return the lista
	 */
	public String[] getLista() {
		return lista;
	}

	/**
	 * @param lista
	 *            the lista to set
	 */
	public void setLista(String[] lista) {
		if (lista == null) {
			this.lista = new String[0];
		} else {
			this.lista = Arrays.copyOf(lista, lista.length);
		}
	}

}
