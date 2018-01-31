package br.com.correios.siscap.model;


import java.util.List;

import org.primefaces.model.LazyDataModel;


@SuppressWarnings("serial")
public class ItemLazy extends LazyDataModel<ItemDisponivel>{

	
	private List<ItemDisponivel> datasource;

	
	
	
	public ItemLazy() {
	}
	

	@Override
	public void setRowIndex(int rowIndex) {
	    /*
	     * The following is in ancestor (LazyDataModel):
	     * this.rowIndex = rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
	     */
	    if (rowIndex == -1 || getPageSize() == 0) {
	        super.setRowIndex(-1);
	    } else {
	        super.setRowIndex(rowIndex % getPageSize());
	    }
	}

	@Override
	public ItemDisponivel getRowData(String rowKey) {
	    for (ItemDisponivel auxItm : datasource) {
	        if (auxItm.getItp_co_item().equals(rowKey)) {
	            return auxItm;
	        }
	    }

	    return null;
	}

	@Override
	public Object getRowKey(ItemDisponivel item) {
	    return item.getItp_co_item();
	}


}


