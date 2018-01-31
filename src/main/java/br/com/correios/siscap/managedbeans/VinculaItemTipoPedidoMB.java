package br.com.correios.siscap.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import br.com.correios.ppjsiscap.enums.CD;
import br.com.correios.ppjsiscap.enums.NATUREZAORGAO;
import br.com.correios.ppjsiscap.enums.TIPOPEDIDO;
import br.com.correios.ppjsiscap.paginador.Paginador;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.dao.ItemDisponivelDAO;
import br.com.correios.siscap.dao.ItemParametroDAO;
import br.com.correios.siscap.excecao.UtilCorreiosException;
import br.com.correios.siscap.model.ItemDisponivel;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class VinculaItemTipoPedidoMB extends MB {
	
	@Inject
	private Usuario usuario;

	@Inject
	@OracleDb
	private DataSource dataSource;

	@Inject
	private UtilCorreiosException utilCorreiosException;

	private ItemDisponivel itemDisponivel = new ItemDisponivel();
	
	private ItemDisponivel itemDisponivelPesquisa = new ItemDisponivel();
	
	private List<ItemDisponivel> itemDisponivelCDs = new ArrayList<ItemDisponivel>();
	
	private ArrayList<String> lItens;

	private LazyDataModel<ItemDisponivel> itemDataModel;

	private ItemDisponivel selectedItem = new ItemDisponivel();

	private List<ItemDisponivel> itemLista;

	private List<ItemDisponivel> filteredItem;

	private List<SelectItem> listanaturezaorgao;

	private List<SelectItem> listacds;

	private List<SelectItem> listatipopedido;

	private Boolean disabledTIP;
	
	private Boolean disabledORG;
	
	private String qtMaxima;

	private String id;

	@PostConstruct
	public void init() {
		// User is available here for the case you'd like to work with it
		// directly after bean's construction.
		// this.setMcucia(usuario.getMcucia());
		if (disabledTIP == null) {
			this.setDisabledTIP(true);
			this.setDisabledORG(true);
		}

		this.setListanaturezaorgao(getListaNatureza());

	}
	
	private void getListaItemDisponivelCD() {

		Connection connection = null;
		try {
			connection = dataSource.getConnection();

			ItemDisponivelDAO dao = new ItemDisponivelDAO(connection, usuario);

			if (itemDisponivelCDs.size() == 0) {
				itemDisponivelCDs = dao.listaTodosItem(itemDisponivel.getItp_co_cia());
			}

			lItens = dao.getItensOrgaoTipo(this.itemDisponivel.getItp_co_cia(), itemDisponivel.getItp_tx_tipo_pedido(),
					this.itemDisponivel.getItp_tx_natureza_orgao());

			this.atualizaCheckbox();

		} catch (SQLException e) {
			e.printStackTrace();
			// adicionaMensagemErro(null, MensagemID.ERRO,
			// "Problema ao consultar a lista de itens disponíveis para o
			// pedido.");
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public void atualizaCheckbox(){
		if (itemDisponivelCDs.size() > 0) {
			List<ItemDisponivel> flgItem = new ArrayList<ItemDisponivel>();

			for (ItemDisponivel item : itemDisponivelCDs) {

				if (lItens.size() > 0) {
					for (int t = 0; t < lItens.size(); t++) {
						String[] aItens = lItens.get(t).split("-");
						String vComp = aItens[0].trim();
						if (vComp.equals(item.getItp_co_item().trim())) {
							item.setMarcado(true);
							if(aItens[1].indexOf("null") == -1){
								item.setPit_pe_item_padrao(aItens[1]);
							}
							break;
						} else {
							item.setMarcado(false);
						}
					}
				} else {
					item.setMarcado(false);
				}
				flgItem.add(item);
			}

			this.setItemDisponivelCDs(flgItem);
		}
		
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametroItem:itemList");
	}
	
	/**
	 * @return the itemDisponivelCDs
	 */
	public List<ItemDisponivel> getItemDisponivelCDs() {
		return itemDisponivelCDs;
	}

	/**
	 * @param itemDisponivelCDs the itemDisponivelCDs to set
	 */
	public void setItemDisponivelCDs(List<ItemDisponivel> itemDisponivelCDs) {
		this.itemDisponivelCDs = itemDisponivelCDs;
	}



	public LazyDataModel<ItemDisponivel> novaInstanciaItem() {
		return new LazyDataModel<ItemDisponivel>() {
			@Override
			public List<ItemDisponivel> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				//filter
				if (filters != null) {
					itemDisponivelPesquisa.setItp_tx_tipo_item(null);
					itemDisponivelPesquisa.setItp_co_item(null);
					itemDisponivelPesquisa.setItp_tx_descricao_item(null);
	                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
	                    try {
	                        String filterProperty = it.next();
	                        String filterValue = (String) filters.get(filterProperty);
	                        if(filterProperty.indexOf("itp_tx_tipo_item") > -1){
	                        	itemDisponivelPesquisa.setItp_tx_tipo_item(filterValue);
	                        }
	                        
	                        if(filterProperty.indexOf("itp_co_item") > -1){
	                        	itemDisponivelPesquisa.setItp_co_item(filterValue);
	                        }
	                        
	                        if(filterProperty.indexOf("itp_tx_descricao_item") > -1){
	                        	itemDisponivelPesquisa.setItp_tx_descricao_item(filterValue);
	                        }
	                    } catch(Exception e) {
	                    	e.printStackTrace();
	                    }
	                }
	            }

				Paginador<ItemDisponivel> p = new Paginador<ItemDisponivel>();
				if (itemDisponivel.getItp_co_cia() != null) {
					p.setEntityBean(new ItemDisponivel());
					p.setEntityBean(getItemDisponivelPesquisa());
					p.getEntityBean().setItp_co_cia(
							itemDisponivel.getItp_co_cia());
					p.getEntityBean().setItp_tx_tipo_pedido(
							itemDisponivel.getItp_tx_tipo_pedido());
					p.getEntityBean().setItp_tx_natureza_orgao(
							itemDisponivel.getItp_tx_natureza_orgao());
					p.setPrimeiroRegistro(first);
					p.setQuantidadeDePaginas(pageSize);
					p = getListaDeItemPaginada(p);
					this.setRowCount(p.getTotalDeRegistros().intValue());

				}
				return p.getColecaoDeRegistros();
			}

			@Override
			public List<ItemDisponivel> load(int first, int pageSize,
					List<SortMeta> multiSortMeta, Map<String, Object> filters) {
				// TODO Auto-generated method stub//filter
				if (filters != null) {
					itemDisponivelPesquisa.setItp_tx_tipo_item(null);
					itemDisponivelPesquisa.setItp_co_item(null);
					itemDisponivelPesquisa.setItp_tx_descricao_item(null);
	                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
	                    try {
	                        String filterProperty = it.next();
	                        String filterValue = (String) filters.get(filterProperty);
	                        if(filterProperty.indexOf("itp_tx_tipo_item") > -1){
	                        	itemDisponivelPesquisa.setItp_tx_tipo_item(filterValue);
	                        }
	                        
	                        if(filterProperty.indexOf("itp_co_item") > -1){
	                        	itemDisponivelPesquisa.setItp_co_item(filterValue);
	                        }
	                        
	                        if(filterProperty.indexOf("itp_tx_descricao_item") > -1){
	                        	itemDisponivelPesquisa.setItp_tx_descricao_item(filterValue);
	                        }
	                    } catch(Exception e) {
	                    	e.printStackTrace();
	                    }
	                }
	            }

				
				Paginador<ItemDisponivel> p = new Paginador<ItemDisponivel>();
				if (itemDisponivel.getItp_co_cia() != null) {

					p.setEntityBean(new ItemDisponivel());
					p.setEntityBean(getItemDisponivelPesquisa());
					p.getEntityBean().setItp_co_cia(
							itemDisponivel.getItp_co_cia());
					p.getEntityBean().setItp_tx_tipo_pedido(
							itemDisponivel.getItp_tx_tipo_pedido());
					p.getEntityBean().setItp_tx_natureza_orgao(
							itemDisponivel.getItp_tx_natureza_orgao());
					p.setPrimeiroRegistro(first);
					p = getListaDeItemPaginada(p);
					this.setRowCount(p.getTotalDeRegistros().intValue());
				}
				return p.getColecaoDeRegistros();
			}

			@Override
			public String getRowKey(ItemDisponivel item) {
				return "ok";
				// throw new
				// UnsupportedOperationException("getRowKey(T object) must be implemented when basic rowKey algorithm is not used.");

			}
		};
	}

	public Paginador<ItemDisponivel> getListaDeItemPaginada(
			Paginador<ItemDisponivel> p) {
		Connection connection = null;

		try {
			connection = dataSource.getConnection();
			ItemDisponivelDAO dao = new ItemDisponivelDAO(connection, usuario);
			p = dao.listaTodosItemPaginado(p);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return p;

	}
	
	public ItemDisponivel getItemDisponivelPesquisa() {
		return itemDisponivelPesquisa;
	}

	public void setItemDisponivelPesquisa(ItemDisponivel itemDisponivelPesquisa) {
		this.itemDisponivelPesquisa = itemDisponivelPesquisa;
	}

	public ItemDisponivel getItemDisponivel() {
		return itemDisponivel;
	}

	public void setItemDisponivel(ItemDisponivel itemDisponivel) {
		this.itemDisponivel = itemDisponivel;
	}

	public List<ItemDisponivel> getItemLista() {
		return itemLista;
	}

	public void setItemLista(List<ItemDisponivel> itemLista) {
		this.itemLista = itemLista;
	}

	public LazyDataModel<ItemDisponivel> getItemDataModel() {
		if (itemDataModel == null) {
			itemDataModel = novaInstanciaItem();

		}

		return itemDataModel;
	}

	public void setItemDataModel(LazyDataModel<ItemDisponivel> itemDataModel) {
		this.itemDataModel = itemDataModel;
	}

	public ItemDisponivel getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ItemDisponivel selectedItem) {
		this.selectedItem = selectedItem;
	}

	public List<ItemDisponivel> getFilteredItem() {
		return filteredItem;
	}

	public void setFilteredItem(List<ItemDisponivel> filteredItem) {
		this.filteredItem = filteredItem;
	}

	/**
	 * @return the disabledTIP
	 */
	public Boolean getDisabledTIP() {
		return disabledTIP;
	}

	/**
	 * @param disabledTIP
	 *            the disabledTIP to set
	 */
	public void setDisabledTIP(Boolean disableTIP) {
		disabledTIP = disableTIP;
	}

	/**
	 * @return the disabledORG
	 */
	public Boolean getDisabledORG() {
		return disabledORG;
	}

	/**
	 * @param disabledORG
	 *            the disabledORG to set
	 */
	public void setDisabledORG(Boolean disableORG) {
		disabledORG = disableORG;
	}

	/**
	 * @return the listatiponaturezaorgao
	 */
	public List<SelectItem> getListanaturezaorgao() {
		return getListaNatureza();
	}

	/**
	 * @param listatiponaturezaorgao
	 *            the listatiponaturezaorgao to set
	 */
	public void setListanaturezaorgao(List<SelectItem> listatiponaturezaorgao) {
		this.listanaturezaorgao = listatiponaturezaorgao;
	}

	/**
	 * @return the qtMaxima
	 */
	public String getQtMaxima() {
		return qtMaxima;
	}

	/**
	 * @param qtMaxima the qtMaxima to set
	 */
	public void setQtMaxima(String qtMaxima) {
		this.qtMaxima = qtMaxima;
	}

	public List<SelectItem> getListaNatureza() {
		List<SelectItem> lista = new ArrayList<SelectItem>();

//		lista.add(new SelectItem(NATUREZAORGAO.ALL.getCodigo(),
//				NATUREZAORGAO.ALL.getDescricao()));
		lista.add(new SelectItem(NATUREZAORGAO.ADMINISTRATIVO.getCodigo(),
				NATUREZAORGAO.ADMINISTRATIVO.getDescricao()));
		lista.add(new SelectItem(NATUREZAORGAO.OPERACIONAL.getCodigo(),
				NATUREZAORGAO.OPERACIONAL.getDescricao()));
		lista.add(new SelectItem(NATUREZAORGAO.REDE.getCodigo(),
				NATUREZAORGAO.REDE.getDescricao()));
		lista.add(new SelectItem(NATUREZAORGAO.TERCEIRO_ACF.getCodigo(),
				NATUREZAORGAO.TERCEIRO_ACF.getDescricao()));
		lista.add(new SelectItem(NATUREZAORGAO.TERCEIRO_AGF.getCodigo(),
				NATUREZAORGAO.TERCEIRO_AGF.getDescricao()));
		lista.add(new SelectItem(NATUREZAORGAO.TERCEIRO_ACC.getCodigo(),
				NATUREZAORGAO.TERCEIRO_ACC.getDescricao()));

		return lista;
	}

	/**
	 * @return the listacds
	 */
	public List<SelectItem> getListacds() {
		return getListaCD();
	}

	/**
	 * @param listacds
	 *            the listacds to set
	 */
	public void setListacds(List<SelectItem> listacds) {
		this.listacds = listacds;
	}

	/**
	 * Retorna uma coleÃ§Ã£o de CDs para exibiÃ§Ã£o na combobox.
	 * 
	 * @return
	 */

	public List<SelectItem> getListaCD() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(CD.CDLESTE.getCodigo(), CD.CDLESTE
				.getDescricao()));
		lista.add(new SelectItem(CD.CDOESTE.getCodigo(), CD.CDOESTE
				.getDescricao()));
		return lista;
	}

	/**
	 * Retorna uma coleÃ§Ã£o de TipoPedidos para exibiÃ§Ã£o na combobox.
	 * 
	 * @return
	 */

	public List<SelectItem> getListaTipoPedidos() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(TIPOPEDIDO.EXTRA.getCodigo(), TIPOPEDIDO.EXTRA
				.getDescricao()));
		lista.add(new SelectItem(TIPOPEDIDO.NORMAL.getCodigo(),
				TIPOPEDIDO.NORMAL.getDescricao()));
		return lista;
	}

	/**
	 * @return the listatipopedido
	 */
	public List<SelectItem> getListatipopedido() {
		return getListaTipoPedidos();
	}

	/**
	 * @param listatipopedido
	 *            the listatipopedido to set
	 */
	public void setListatipopedido(List<SelectItem> listatipopedido) {
		this.listatipopedido = listatipopedido;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void onChangeCD() { // ValueChangeEvent event

		// itemDisponivel.setCdtransito((String) event.getNewValue());
		// itemDisponivel.setItp_co_cia((String) event.getNewValue());
		if (itemDisponivel.getItp_co_cia() == null) {
			setDisabledTIP(true);
		} else {
			setDisabledTIP(false);
		}

		//itemDataModel = null; // Faz com que o objeto seja recarregado
		// no banco de dados

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametroItem:campoTipo");
		
		this.itemDisponivelCDs.clear();
	
		getListaItemDisponivelCD();
		if(this.itemDisponivelCDs != null){
			FacesContext.getCurrentInstance().getPartialViewContext()
			.getRenderIds().add("formParametroItem:itemList");
		}
		

	}

	public void onChangeTip() {

		if (itemDisponivel.getItp_tx_tipo_pedido() == null) {
			this.setDisabledORG(true);
		} else {
			this.setDisabledORG(false);
			itemDisponivel
					.setItp_tx_natureza_orgao("'02','01','10','23','03','14','04','05','06','07','08','11','13','17','19','20','22','29','30','09','25','26','16','12','27','28','31','32','33','34','35','36'");
		}

		//this.itemDataModel = null;// Faz com que o objeto seja recarregado no
									// banco de dados

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametroItem:campoTipoOrgao");

		getListaItemDisponivelCD();
		if(this.itemDisponivelCDs != null){
			FacesContext.getCurrentInstance().getPartialViewContext()
			.getRenderIds().add("formParametroItem:itemList");
		}
	}

	public void onChangeOrg() {

		//this.itemDataModel = null;// Faz com que o objeto seja recarregado no
									// banco de dados
		getListaItemDisponivelCD();

			FacesContext.getCurrentInstance().getPartialViewContext()
			.getRenderIds().add("formParametroItem:itemList");

	}

	public void onRowSelect(SelectEvent event) throws SQLException {
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        
		String qtMaxima = request.getParameter("formParametroItem:qtMaxima");
		
		this.setQtMaxima(qtMaxima);
		
		if (itemDisponivel.getItp_tx_natureza_orgao() == null
				|| itemDisponivel.getItp_tx_tipo_pedido() == null) {
			FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formParametroItem");

			FacesMessage msg = new FacesMessage("Seleção de Item",
					"Informe o tipo de pedido e tipo do orgão para a vinculação do item.");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} else {
			String txtMsg = "";

			ItemDisponivel itemSel = (ItemDisponivel) event.getObject();

			itemSel.setCdtransito(itemDisponivel.getItp_co_cia());
			itemSel.setItp_tx_natureza_orgao(itemDisponivel
					.getItp_tx_natureza_orgao());
			itemSel.setItp_tx_tipo_pedido(itemDisponivel
					.getItp_tx_tipo_pedido());
			itemSel.setPit_pe_item_padrao(itemDisponivel.getPit_pe_item_padrao());
						
			Connection connection = null;
			try {

				connection = dataSource.getConnection();

				ItemParametroDAO dao = new ItemParametroDAO(connection, usuario);

				if (itemSel.isMarcado()) {
					itemSel.setMarcado(false);
					dao.delete(itemSel);
					txtMsg = "O item " + itemSel.getItp_co_item()
							+ " foi desmarcado ";
					connection.commit();
					
				} else {
					if(this.getQtMaxima() != null){
						itemSel.setPit_pe_item_padrao(this.getQtMaxima());
					}
					itemSel.setMarcado(true);
					dao.insertBatch(itemSel);
					txtMsg = "O item " + itemSel.getItp_co_item()
							+ " foi marcado ";

				}

				//itemDataModel = null;
				
				getListaItemDisponivelCD();
				if(itemDisponivelCDs != null){
					FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formParametroItem:itemList");
				}
				
				FacesContext.getCurrentInstance().getPartialViewContext()
						.getRenderIds().add("formParametroItem");
				
				FacesMessage msg = new FacesMessage("Seleção de Item", txtMsg);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				
				//itemList

			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} finally {
				if (connection != null) {
					connection.close();
				}

			}
		}
	}

}
