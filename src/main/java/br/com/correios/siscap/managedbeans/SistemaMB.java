package br.com.correios.siscap.managedbeans;

import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@SuppressWarnings("serial")
public class SistemaMB extends MB {

	private TreeNode itemArvoreSelecionado;
	private TreeNode arvore = new DefaultTreeNode("Raiz", null);
	
	private List<String> listaDeLinhas = new ArrayList<String>();
	private List<String> listaDeColunas = new ArrayList<String>();
	

	
	public void limpaItensDaArvore() {
		getArvore().getChildren().clear();
	}
	
	public TreeNode getItemArvoreSelecionado() {
		return itemArvoreSelecionado;
	}

	public void setItemArvoreSelecionado(TreeNode iSelecionado) {
		this.itemArvoreSelecionado = iSelecionado;
	}

	public TreeNode getArvore() {
		return arvore;
	}

	public void setArvore(TreeNode arvore) {
		this.arvore = arvore;
	}

	//Métodos para implementação da impressão posicional
	public List<String> getListaDeLinhas() {
		listaDeLinhas.clear();
		listaDeLinhas.add("1");
		listaDeLinhas.add("2");
		listaDeLinhas.add("3");
		listaDeLinhas.add("4");
		listaDeLinhas.add("5");
		listaDeLinhas.add("6");
		listaDeLinhas.add("7");
		listaDeLinhas.add("8");
		return listaDeLinhas;
	}
	public void setListaDeLinhas(List<String> listaDeLinhas) {
		this.listaDeLinhas = listaDeLinhas;
	}
	public List<String> getListaDeColunas() {
		listaDeColunas.clear();
		listaDeColunas.add("1");
		listaDeColunas.add("2");
		return listaDeColunas;
	}
	public void setListaDeColunas(List<String> listaDeColunas) {
		this.listaDeColunas = listaDeColunas;
	}

}
