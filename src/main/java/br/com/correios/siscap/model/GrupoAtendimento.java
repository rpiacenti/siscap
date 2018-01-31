package br.com.correios.siscap.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import br.com.correios.siscap.model.Cronograma;

@SuppressWarnings("serial")
public class GrupoAtendimento implements Serializable {
			
	private Integer numGrupo;
	private String catgrupo;
	private String descricao;
	private String numcd;
	private Integer exterceiro = 1;
	private Integer expropria = 1;
	private String mcuCd;
	
	private List<Cronograma> cronograma = new ArrayList<Cronograma>(); 
		

	public GrupoAtendimento(Integer numgrupo, String descri,
			String numcd, Integer extterceiro, Integer extpropria, String categoriagrp) {
		super();
		this.numGrupo = numgrupo;
		descricao = descri;
		this.numcd = numcd;
		exterceiro = extterceiro;
		expropria = extpropria;
		catgrupo = categoriagrp;
	}



	public GrupoAtendimento() {
	}



	/**
	 * @return the numGrupo
	 */
	public Integer getNumGrupo() {
		return numGrupo;
	}

	/**
	 * @param numGrupo the numGrupo to set
	 */
	public void setNumGrupo(Integer numgrupo) {
		this.numGrupo = numgrupo;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descri) {
		descricao = descri;
	}

	/**
	 * @return the numcd
	 */
	public String getNumcd() {
		return numcd;
	}

	/**
	 * @param numcd the numcd to set
	 */
	public void setNumcd(String numcd) {
		this.numcd = numcd;
	}

	/**
	 * @return the extraterceiro
	 */
	public Integer getExterceiro() {
		return exterceiro;
	}

	/**
	 * @param extraterceiro the extraterceiro to set
	 */
	public void setExterceiro(Integer extterceiro) {
		exterceiro = extterceiro;
	}

	/**
	 * @return the extrapropria
	 */
	public Integer getExpropria() {
		return expropria;
	}

	/**
	 * @param extrapropria the extrapropria to set
	 */
	public void setExpropria(Integer extpropria) {
		expropria = extpropria;
	}


	/**
	 * @return the tipogrupo
	 */
	public String getCatgrupo() {
		return catgrupo;
	}


	/**
	 * @param tipogrupo the tipogrupo to set
	 */
	public void setCatgrupo(String categoriagrupo) {
		this.catgrupo = categoriagrupo;
	}



	/**
	 * @return the mcuCd
	 */
	public String getMcuCd() {
		return mcuCd;
	}



	/**
	 * @param mcuCd the mcuCd to set
	 */
	public void setMcuCd(String mcuCd) {
		this.mcuCd = mcuCd;
	}

	/**
	 * @return the cronograma
	 */
	public List<Cronograma> getCronograma() {
		return cronograma;
	}

	/**
	 * @param cronograma the cronograma to set
	 */
	public void setCronograma(List<Cronograma> cronograma) {
		this.cronograma = cronograma;
	}
	
	public String getStringCronograma(){
		String lCrono = "O início da validação do grupo " + this.descricao + " neste mês é em: #,  e no próximo mês em: *.";
		int pos = 1;
		for(Cronograma crono:this.getCronograma()){
			if(pos == 1){
				lCrono = lCrono.replace("#", crono.getCro_dt_ini_solicitacao());
				pos = pos + 1;
			}else{
				lCrono = lCrono.replace("*", crono.getCro_dt_ini_solicitacao());
			}
		}
		if(lCrono.equals(" ")){
			lCrono = "data não definida.";
			
		}
		return lCrono;
	}

}
