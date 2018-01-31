package br.com.correios.siscap.managedbeans;

import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;

public interface Arvore {
	
	void expandeItensDaArvore(NodeExpandEvent event);
	void limpaItensDaArvore();
	void selecionaItemDaArvore(NodeSelectEvent event);
	
}
