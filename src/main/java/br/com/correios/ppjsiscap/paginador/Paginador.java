package br.com.correios.ppjsiscap.paginador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.correios.siscap.constantes.Constantes;

/**
 * 
 * @author Arivaldo
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class Paginador<T> implements Serializable{
	
	private int		      quantidadeDeRegistrosPorPagina = Constantes.QTD_POR_PAGINA_PADRAO;

	private long		  paginaAtual = 1;
	
	private int 		  primeiroRegistro;

	private long		  totalDeRegistros;

	private long		  quantidadeDePaginas;

	private List<T> colecaoDeRegistros = new ArrayList<T>();
	
	private List<T> colecaoDeRegistrosPorPagina = new ArrayList<T>();

	private T			  entityBean;
	
	private int tamanhoDaColecao;
	
	

		
	

	/**
	 * @return the tamanhoDaColecao
	 */
	public int getTamanhoDaColecao() {
		tamanhoDaColecao = getColecaoDeRegistros().size();
		return tamanhoDaColecao;
	}



	/**
	 * @param tamanhoDaColecao the tamanhoDaColecao to set
	 */
	public void setTamanhoDaColecao(int tamanhoDaColecao) {
		this.tamanhoDaColecao = tamanhoDaColecao;
	}



	/**
	 * @return the quantidadeDeRegistrosPorPagina
	 */
	public int getQuantidadeDeRegistrosPorPagina() {
		return quantidadeDeRegistrosPorPagina;
	}

	/**
	 * @param quantidadeDeRegistrosPorPagina the quantidadeDeRegistrosPorPagina to set
	 */
	public void setQuantidadeDeRegistrosPorPagina(int quantidadeDeRegistrosPorPagina) {
		this.quantidadeDeRegistrosPorPagina = quantidadeDeRegistrosPorPagina;
	}

	/**
	 * @return the paginaAtual
	 */
	public long getPaginaAtual() {
		return paginaAtual;
	}

	/**
	 * @param paginaAtual the paginaAtual to set
	 */
	public void setPaginaAtual(long paginaAtual) {
		this.paginaAtual = paginaAtual;
	}

	/**
	 * @return the totalDeRegistros
	 */
	public Long getTotalDeRegistros() {
		return totalDeRegistros;
	}

	/**
	 * @param totalDeRegistros the totalDeRegistros to set
	 */
	public void setTotalDeRegistros(Long totalDeRegistros) {
		this.totalDeRegistros = totalDeRegistros;
	}

	/**
	 * @return the quantidadeDePaginas
	 */
	public long getQuantidadeDePaginas() {
		if(getTotalDeRegistros() > getQuantidadeDeRegistrosPorPagina()){
			quantidadeDePaginas = (getTotalDeRegistros() % getQuantidadeDeRegistrosPorPagina()) != 0 ? 
					(getTotalDeRegistros() - (getTotalDeRegistros() % getQuantidadeDeRegistrosPorPagina())) / getQuantidadeDeRegistrosPorPagina() +1 :
						getTotalDeRegistros() / getQuantidadeDeRegistrosPorPagina() ;
		}else{
			quantidadeDePaginas = 1L;
		}
		return quantidadeDePaginas;
	}

	/**
	 * @param quantidadeDePaginas the quantidadeDePaginas to set
	 */
	public void setQuantidadeDePaginas(int quantidadeDePaginas) {
		this.quantidadeDePaginas = quantidadeDePaginas;
	}

	/**
	 * @return the colecaoDeRegistros
	 */
	public List<T> getColecaoDeRegistros() {
		return colecaoDeRegistros;
	}

	/**
	 * @param colecaoDeRegistros the colecaoDeRegistros to set
	 */
	public void setColecaoDeRegistros(List<T> colecaoDeRegistros) {
		this.colecaoDeRegistros = colecaoDeRegistros;
	}

	/**
	 * @return the entityBean
	 */
	public T getEntityBean() {
		return entityBean;
	}

	/**
	 * @param entityBean the entityBean to set
	 */
	public void setEntityBean(T entityBean) {
		this.entityBean = entityBean;
	}

	public List<T> getColecaoDeRegistrosPorPagina() {
		return colecaoDeRegistrosPorPagina;
	}

	public void setColecaoDeRegistrosPorPagina(List<T> colecaoDeRegistrosPorPagina) {
		this.colecaoDeRegistrosPorPagina = colecaoDeRegistrosPorPagina;
	}



	/**
	 * @return the primeiroRegistro
	 */
	public int getPrimeiroRegistro() {
		return primeiroRegistro;
	}



	/**
	 * @param primeiroRegistro the primeiroRegistro to set
	 */
	public void setPrimeiroRegistro(int primeiroRegistro) {
		this.primeiroRegistro = primeiroRegistro;
	}
	
	

}
