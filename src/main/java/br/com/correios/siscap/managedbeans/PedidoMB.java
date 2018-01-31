package br.com.correios.siscap.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.correios.ppjsiscap.enums.Categoria;
import br.com.correios.ppjsiscap.enums.TIPOPEDIDO;
import br.com.correios.ppjsiscap.excecao.CorreiosException;
import br.com.correios.ppjsiscap.paginador.Paginador;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.util.DataERP;
import br.com.correios.ppjsiscap.util.UtilData;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.constantes.MensagemID;
import br.com.correios.siscap.dao.ItemDisponivelDAO;
import br.com.correios.siscap.dao.ItemPedidoDAO;
import br.com.correios.siscap.dao.JustificativaDAO;
import br.com.correios.siscap.excecao.SiscapException;
import br.com.correios.siscap.excecao.UtilCorreiosException;
import br.com.correios.siscap.model.ItemDisponivel;
import br.com.correios.siscap.model.ItemPedido;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class PedidoMB extends MB {

	@Inject
	private Usuario usuario;

	@Inject
	@OracleDb
	private DataSource dataSource;

	@Inject
	private UtilCorreiosException utilCorreiosException;

	private ItemDisponivel itemDisponivel = new ItemDisponivel();

	private ItemDisponivel itemDisponivelPesquisa = new ItemDisponivel();

	private Boolean habilitado = false;

	private Boolean habilitaConfirma = true;

	private Boolean lupaItemDisponivel = false;

	private String tipoPedido;

	private String tipoItem;
	
	private String custoUnitario;

	private String numeroPedido;

	private String mediaSemestre;

	private String custoTotal;

	private List<ItemDisponivel> itemDisponivelParaPedir;
	
	private LazyDataModel<ItemDisponivel> itemDisponivelDataModel;

	private List<ItemPedido> itensDoPedido = new ArrayList<ItemPedido>();

	private List<ItemDisponivel> itensDisponiveisNoPedido = new ArrayList<ItemDisponivel>();

	private String tipoPedidoAnterior;
	
	private String justificativa;

	@PostConstruct
	public void init() {
		
	}

	private boolean checaDuplicidade() {
		boolean retorno = false;
		if (itensDoPedido != null) {
			for (ItemPedido iPed : itensDoPedido) {
				if (itemDisponivel.getItp_co_item() != null) {
					if (iPed.getEALITM() != null) {
						if (iPed.getEALITM().trim().equals(itemDisponivel.getItp_co_item().trim())) {
							retorno = true;
							break;
						}
					}
				}
			}
		}
		return retorno;
	}

	private ItemPedido converteParaItemPedido() {
		ItemPedido item = new ItemPedido();
		item.setEAAA25(getNumeroPedido());
		item.setEAMCU(usuario.getMcucia());
		item.setEAMCU1(usuario.getMculotacao());
		item.setEAUORG(getItemDisponivel().getItp_tx_quantidade());
		item.setEAT001(getItemDisponivel().getItp_tx_tipo_item());
		if (item.getEAT001().equals("")) {
			item.setEAT001(this.getTipoItem());
		}
		item.setEALITM(getItemDisponivel().getItp_co_item());
		item.setIMDSC1(getItemDisponivel().getItp_tx_descricao_item());
		item.setEAUOM(getItemDisponivel().getItp_tx_unidade_item());
		item.setCOUNCS(getItemDisponivel().getCouncs());
		item.setUMCONV(getItemDisponivel().getUmconv());
		item.setUMCNV1(getItemDisponivel().getUmcnv1());
		item.setUMRUM(getItemDisponivel().getUmrum());
		item.setUMUM(getItemDisponivel().getUmum());
		item.setValorUnitario(getItemDisponivel().getItp_tx_valor_unitario());
		item.setUnidMedFormatada2(getItemDisponivel().getUnidMedFormatada());
		
		item.setNovoItem(true);
		return item;
	}

	public void recuperaPedido() throws SiscapException {

		Connection connection = null;

		try {
			connection = dataSource.getConnection();
			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);

			String valorMCU = usuario.getMculotacao();

			String pedido = dao.findByNumeroPedido(valorMCU, tipoPedido);
			
			if(!pedido.equals("")){
				setNumeroPedido(pedido);
				List<ItemPedido> itensDoPedido = dao.findByPedidoSolicitado(pedido, "S",
						usuario.getMcucia());

				setItensDoPedido(itensDoPedido);

				calculaCustoTotal(itensDoPedido);
//				
			}
		} catch (SQLException e) {
			throw new SiscapException("Erro instrução em banco de dados: " + " - " + e.getMessage());
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

	public void verificaTipoPedido() throws CorreiosException, SiscapException {

		if ("2,6".indexOf(usuario.getStatusERP()) == -1) {

			setHabilitado(false);
			setLupaItemDisponivel(false);

			adicionaMensagemAlerta(null, MensagemID.PENDENCIA_NO_CADASTRO);

		} else if (usuario.getStatusERP().equals("0")) {

			setHabilitado(false);
			setLupaItemDisponivel(false);

			adicionaMensagemAlerta(null,
					MensagemID.SEM_VINCULACAO_GRUPO_ATENDIMENTO);

		} else {

			if (getAcessoPedido()) {
				getPedido(tipoPedido);
				getListaItemDisponivel();
				setHabilitado(true);
				setLupaItemDisponivel(true);
			} else {
				adicionaMensagemInfo(null,
						MensagemID.FORA_DO_PERIODO_DE_ABERTURA_DO_PEDIDO);
				setHabilitado(false);
				setLupaItemDisponivel(false);
			}
		}
	}

	private boolean getAcessoPedido() throws SiscapException {
		boolean grupoAtendimentoPode = true;
		boolean grupoOrgaoPode = true;

		Connection connection = null;
		String valorMCU = usuario.getMculotacao();
		String valorCIA = usuario.getMcucia();

		if (tipoPedido.equals(TIPOPEDIDO.EXTRA.getCodigo())) {

			UtilLog.logger.debug("Acesso a pedidos Extras: " + valorMCU + " - "
					+ valorCIA);
			// / *********************
			// / Este metodo precisa ser aperfeiçoado para checar o numero de
			// pedidos EXTRAS / NORMAIS solicitados no mês
			// / via associação da VW_F5542E01 e VINCULA_PEDIDO_ERP INCREMETAR
			// INTEMPEDIDODAO findByExtraMes
			// / *
			try {
				connection = dataSource.getConnection();
				ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);
				grupoAtendimentoPode = dao.findByPedidoRealizadoMesMCU(
						valorMCU, tipoPedido);
				// OrgaoDAO dao2 = new OrgaoDAO(connection);
				// grupoOrgaoPode = dao2.getAcessoOrgaoGrupo(valorMCU,
				// valorCIA);

				UtilLog.logger.debug("Acesso ao pedidos como: " + valorMCU
						+ " - " + valorCIA);

			} catch (SQLException e) {
				throw new SiscapException("Erro instrução em banco de dados: " + " - " + e.getMessage());
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

		if (tipoPedido.equals(TIPOPEDIDO.NORMAL.getCodigo())) {

			try {
				connection = dataSource.getConnection();
				ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);
			//	grupoAtendimentoPode = dao.findByPedidoRealizadoMesMCU(
			//			valorMCU, tipoPedido);
				if(dao.findByOpenPedidoMCU(valorMCU, "N", "'S'") == 0){ //verifica se existe pedido em status Solicitado
					if (dao.findByOpenPedidoMCU(valorMCU, "N", "'V',' ','R'") > 0) { //verifica se existe pedido em status V, R ou branco
						grupoAtendimentoPode = false;
						RequestContext context = RequestContext
								.getCurrentInstance();
						// context.execute("PF('dlgRecPedido').show();");
						FacesMessage msg = new FacesMessage(
								FacesMessage.SEVERITY_INFO,
								"Solicitação de Pedido",
								"Você possui pedido(s) NORMAL pendente de validação/processamento, contacte seu validador!");
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
				}
				
				// OrgaoDAO dao2 = new OrgaoDAO(connection);
				// grupoOrgaoPode = dao2.getAcessoOrgaoGrupo(valorMCU,
				// valorCIA);

				UtilLog.logger.debug("Acesso ao pedidos como: " + valorMCU
						+ " - " + valorCIA);

			} catch (SQLException e) {
				throw new SiscapException("Erro instrução em banco de dados: "
						+ " - " + e.getMessage());
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
		if(grupoAtendimentoPode){ // & grupoOrgaoPode){
			return true;
		}else{
			return false;
		}
		
	}
	
	public List<ItemDisponivel> getItemDisponivelParaPedir() {
		return itemDisponivelParaPedir;
	}

	public void setItemDisponivelParaPedir(
			List<ItemDisponivel> itemDisponivelParaPedir) {
		this.itemDisponivelParaPedir = itemDisponivelParaPedir;
	}

	public List<ItemDisponivel> getItensDisponiveisNoPedido() {
		return itensDisponiveisNoPedido;
	}

	public void setItensDisponiveisNoPedido(List<ItemDisponivel> itempedidolista) {
		this.itensDisponiveisNoPedido = itempedidolista;
	}

	public Categoria[] categorias() {
		return Categoria.values();
	}

	/**
	 * @return the additem
	 */
	public void addItem() {

		ItemDisponivel itemAdd = new ItemDisponivel();

		itemAdd.setItp_tx_unidade_conversao_item("-");

		if (itensDisponiveisNoPedido.isEmpty()) {

			itensDisponiveisNoPedido.add(itemAdd);

		}
		itensDisponiveisNoPedido.add(itemAdd);

	}

	// Metodos que tratam a edição de linhas das tabelas de pedidos

	public void onEdit(RowEditEvent event) {
		ItemPedido itemEdit = (ItemPedido) event.getObject();

		calculaCustoTotal(itensDoPedido);

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formPedido:campoCusto");

		FacesMessage msg = new FacesMessage("Item atualizado",
				itemEdit.getEALITM());
		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	public void onCancel(RowEditEvent event) throws SQLException,
			ClassNotFoundException, SiscapException {
		ItemPedidoMB itped = (ItemPedidoMB) event.getObject();
		// this.setNumeropedido(itped.getCodigopedido());
		// pedidolist.remove(itped);
		// pedidolist.trimToSize();
		if (0 == 0) { /* pedidolist.size() */

			String txtNumPed = "";/* this.getNumeropedido() */

			Connection connection = dataSource.getConnection();
			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);

			try {
				System.out.println("Apagando ..." + txtNumPed);
				dao.delete(txtNumPed);

			} catch (SQLException e) {
				throw new SiscapException("Erro instrução em banco de dados: " + " - " + e.getMessage());
			}finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Cancelado o Pedido", "Seu pedido foi excluído!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formPedido:pedList");

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formPedido:campoCusto");

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("dlgExcLinha.show();");

	}

	public long getCountItem() throws SQLException {
		String mcucd = usuario.getMcucia();
		long recnumber = 0;

		Connection connection = dataSource.getConnection();
		ItemDisponivelDAO dao = new ItemDisponivelDAO(connection, usuario);
		recnumber = Integer.parseInt(dao.findByCount(mcucd));

		return recnumber;

	}

	private void getPedido(String tipoPed) throws SiscapException {
		
		Connection connection = null;
		try {
		connection = dataSource.getConnection();
		ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);
		String valorMCU = usuario.getMculotacao();
		
			if (tipoPed.equals("E")) {
				if (dao.findByOpenPedidoMCU(valorMCU, tipoPed, "'S'") > 0) {
					RequestContext context = RequestContext
							.getCurrentInstance();
				recuperaPedido();
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Solicitação de Pedido", "Seu pedido de número: "
				+ itensDoPedido.get(0).getEAAA25()
				+ " do tipo EXTRA foi recuperado para edição!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				}
				
			} else {				
				if (dao.findByOpenPedidoMCU(valorMCU, tipoPed, "'S'") > 0) {
					RequestContext context = RequestContext
							.getCurrentInstance();
					//context.execute("PF('dlgRecPedido').show();");
					recuperaPedido();
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Solicitação de Pedido", "Seu pedido de número do tipo NORMAL foi recuperado para edição!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}

		} catch (SQLException e) {
			throw new SiscapException("Erro instrução em banco de dados: " + " - " + e.getMessage());
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

	private void getPedidoPesquisa(String pCodPedido) throws SiscapException  {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		
		ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);

		List<ItemPedido> aPedido = dao.findByPedidoSolicitado(pCodPedido,
				"'S','V','R','C',' '", usuario.getMcucia()); // aqui

		// pedidolist.clear();

		for (ItemPedido iPed : aPedido) {

			ItemPedidoMB itPed = new ItemPedidoMB();
			System.out.println(iPed.getEALITM());
			itPed.setCodigopedido(iPed.getEAAA25());
			itPed.setCodigoItem(iPed.getEALITM());
			itPed.setDatapedido(iPed.getEATRDJ());
			itPed.setMcuorgao(iPed.getEAMCU1());
			itPed.setMcucia(iPed.getEAMCU());
			itPed.setQuantidade(iPed.getEAUORG());
			itPed.setSigladr(iPed.getEAKCO());
			itPed.setTipoitem(iPed.getEAT001());
			itPed.setTipopedido(iPed.getEAT002());
			itPed.setUnidadeMedida(iPed.getEAUOM());
			String[] aDetalheItem = null;// getDetalheItem(iPed.getEALITM());
			if (aDetalheItem == null) {
				itPed.setValorUnitario("0");
				itPed.setDescricaoItem("");
			} else {
				itPed.setValorUnitario(aDetalheItem[1]);
				itPed.setDescricaoItem(aDetalheItem[0]);
			}

			// setNumeropedido(iPed.getEAAA25());
			// pedidolist.add(itPed);

			itPed = null;
		}
		// this.setCustototal(this.getCustototal());
		} catch (SQLException e) {
			throw new SiscapException("Erro instrução em banco de dados: " + " - " + e.getMessage());
		}finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formPesquisa:itemList");

	}

	public void bloqueio() {
		// response.sendRedirect(urlInicio);
		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		// context.redirect(urlInicio);
	}

	/**
	 * @return the itemDisponivel
	 */
	public ItemDisponivel getItemDisponivel() {
		return itemDisponivel;
	}

	/**
	 * @param itemDisponivel
	 *            the itemDisponivel to set
	 */
	public void setItemDisponivel(ItemDisponivel itemDisponivel) {
		this.itemDisponivel = itemDisponivel;
		itemDisponivel.setItp_tx_tipo_item(getTipoItem());
	}

	public String getTipoItem() {
		return tipoItem;
	}

	public void setTipoItem(String tipoItem) {
		this.tipoItem = tipoItem;
	}
	
	/**
	 * @return the custoUnitario
	 */
	public String getCustoUnitario() {
		return custoUnitario;
	}

	/**
	 * @param custoUnitario the custoUnitario to set
	 */
	public void setCustoUnitario(String custoUnitario) {
		this.custoUnitario = custoUnitario;
	}

	/**
	 * @return the tipoPedido
	 */
	public String getTipoPedido() {
		return tipoPedido;
	}

	/**
	 * @param tipoPedido
	 *            the tipoPedido to set
	 */
	public void setTipoPedido(String tipoPedido) {
		if (tipoPedido == null) {
			tipoPedidoAnterior = null;
		}
		this.tipoPedido = tipoPedido;
	}

	public String getTipoPedidoAnterior() {
		return tipoPedidoAnterior;
	}

	public void setTipoPedidoAnterior(String tipoPedidoAnterior) {
		this.tipoPedidoAnterior = tipoPedidoAnterior;
	}

	/**
	 * @return the numeroPedido
	 */
	public String getNumeroPedido() {
		return numeroPedido;
	}

	/**
	 * @param numeroPedido
	 *            the numeroPedido to set
	 */
	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	/**
	 * @return the habilitado
	 */
	public Boolean getHabilitado() {
		return habilitado;
	}

	/**
	 * @param habilitado
	 *            the habilitado to set
	 */
	public void setHabilitado(Boolean habilitado) {
		this.habilitado = habilitado;
	}

	/**
	 * @param itemDisponivelDataModel
	 *            the itemDisponivelDataModel to set
	 */
	public void setItemDisponivelDataModel(
			LazyDataModel<ItemDisponivel> itemDisponivelDataModel) {
		this.itemDisponivelDataModel = itemDisponivelDataModel;
	}

	/**
	 * @return the itemDisponivelDataModel
	 */
	public LazyDataModel<ItemDisponivel> getItemDisponivelDataModel() {
		if (itemDisponivelDataModel == null) {
			itemDisponivelDataModel = novaInstanciaDeItemDisponivelDataModel();
		}
		return itemDisponivelDataModel;
	}

	/**
	 * @return the lupaItemDisponivel
	 */
	public Boolean getLupaItemDisponivel() {
		return lupaItemDisponivel;
	}

	/**
	 * @param lupaItemDisponivel
	 *            the lupaItemDisponivel to set
	 */
	public void setLupaItemDisponivel(Boolean lupaItemDisponivel) {
		this.lupaItemDisponivel = lupaItemDisponivel;
	}

	/**
	 * @return the itensDoPedido
	 */
	public List<ItemPedido> getItensDoPedido() {
		return itensDoPedido;
	}

	/**
	 * @param itensDoPedido
	 *            the itensDoPedido to set
	 */
	public void setItensDoPedido(List<ItemPedido> itensDoPedido) {
		this.itensDoPedido = itensDoPedido;
	}

	/**
	 * @return the custoTotal
	 */
	public String getCustoTotal() {
		return custoTotal;
	}

	/**
	 * @param custoTotal
	 *            the custoTotal to set
	 */
	public void setCustoTotal(String custoTotal) {
		this.custoTotal = custoTotal;
	}

	/**
	 * @return the itemDisponivelPesquisa
	 */
	public ItemDisponivel getItemDisponivelPesquisa() {
		return itemDisponivelPesquisa;
	}

	/**
	 * @param itemDisponivelPesquisa
	 *            the itemDisponivelPesquisa to set
	 */
	public void setItemDisponivelPesquisa(ItemDisponivel itemDisponivelPesquisa) {
		this.itemDisponivelPesquisa = itemDisponivelPesquisa;
	}

	/**
	 * @return the justificativa
	 */
	public String getJustificativa() {
		return justificativa;
	}

	/**
	 * @param justificativa the justificativa to set
	 */
	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	/**
	 * @return the mediaSemestre
	 */
	public String getMediaSemestre() {
		return mediaSemestre;
	}
	
	/**
	 * @return the habilitaConfirma
	 */
	public Boolean getHabilitaConfirma() {
		return habilitaConfirma;
	}

	/**
	 * @param habilitaConfirma the habilitaConfirma to set
	 */
	public void setHabilitaConfirma(Boolean habilitaConfirma) {
		this.habilitaConfirma = habilitaConfirma;
	}

	/**
	 * @param mediaSemestre
	 *            the mediaSemestre to set
	 */
	public void setMediaSemestre(String mediaSemestre) {
		this.mediaSemestre = mediaSemestre;
	}

	private LazyDataModel<ItemDisponivel> novaInstanciaDeItemDisponivelDataModel() {
		return new LazyDataModel<ItemDisponivel>() {
			private Long totalDeRegistros;

			@Override
			public List<ItemDisponivel> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {

				// filter
			/*	if (filters != null) {
					itemDisponivelPesquisa.setItp_tx_tipo_item(null);
					itemDisponivelPesquisa.setItp_co_item(null);
					itemDisponivelPesquisa.setItp_tx_descricao_item(null);
					for (Iterator<String> it = filters.keySet().iterator(); it
							.hasNext();) {
						try {
							String filterProperty = it.next();
							String filterValue = (String) filters
									.get(filterProperty);
							if (filterProperty.indexOf("itp_tx_tipo_item") > -1) {
								itemDisponivelPesquisa
										.setItp_tx_tipo_item(filterValue);
							}

							if (filterProperty.indexOf("itp_co_item") > -1) {
								itemDisponivelPesquisa
										.setItp_co_item(filterValue);
							}

							if (filterProperty.indexOf("itp_tx_descricao_item") > -1) {
								itemDisponivelPesquisa
										.setItp_tx_descricao_item(filterValue);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
*/
				// filter

				Paginador<ItemDisponivel> p = new Paginador<ItemDisponivel>();
				p.setEntityBean(getItemDisponivelPesquisa());
				p.getEntityBean().setItp_co_cia(usuario.getMcucia());
				p.getEntityBean().setItp_tx_tipo_pedido(tipoPedido);
				p.setPrimeiroRegistro(first);
				p.setQuantidadeDeRegistrosPorPagina(pageSize);
			/*	if (tipoPedidoAnterior != null) {// || tipoPedidoAnterior ==
													// tipoPedido)
					p = getListaItemDisponivelPaginada(p);
					p.setTotalDeRegistros(totalDeRegistros);
				} else {
			*/
					try {
						p = getListaItemDisponivelPaginada(p);
					} catch (SiscapException e) {
						try {
							throw new SiscapException("Erro instrução em banco de dados: " + " - " + e.getMessage());
						} catch (SiscapException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					totalDeRegistros = p.getTotalDeRegistros();
					tipoPedidoAnterior = tipoPedido;
			//	}

				this.setRowCount(p.getTotalDeRegistros().intValue());
				return p.getColecaoDeRegistros();
			}
		};
	}

	private Paginador<ItemDisponivel> getListaItemDisponivelPaginada(
			Paginador<ItemDisponivel> p) throws SiscapException {

		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			
			ItemDisponivelDAO dao = new ItemDisponivelDAO(connection, usuario);
	//		if(itemDisponivelParaPedir == null){
				itemDisponivelParaPedir = dao.findItemTipoOrgaoCD(p,usuario.getTipoOrgao());
				p.setTotalDeRegistros((long) itemDisponivelParaPedir.size());
				p = dao.paginacaoItemTipoOrgaoCD(itemDisponivelParaPedir, p);
	//		}else{
	//			p.setTotalDeRegistros((long) itemDisponivelParaPedir.size());
	//			p = dao.paginacaoItemTipoOrgaoCD(itemDisponivelParaPedir, p);
	//		}
		} catch (SQLException e) {
			throw new SiscapException("Erro instrução em banco de dados: " + " - " + e.getMessage());
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
	
	private void getListaItemDisponivel() throws SiscapException{

		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			
			ItemDisponivelDAO dao = new ItemDisponivelDAO(connection, usuario);

			itemDisponivelParaPedir = dao.findItemTipoOrgaoCD(usuario.getMcucia(), tipoPedido, usuario.getTipoOrgao());

		} catch (SQLException e) {
			throw new SiscapException("Erro instrução em banco de dados: " + " - " + e.getMessage());
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

	/**
	 * @return the custototal
	 */
	private void calculaCustoTotal(List<ItemPedido> itens) {
		if (usuario.getTipoOrgao().indexOf("12,16,27,28") > -1) {
			custoTotal = "";
		} else {
			double vTotal = 0d;

			for (ItemPedido item : itens) {
				double vqtd = 0d;
				double vcusto = 0d;

				if (item.getEAUORG() != null && !item.getEAUORG().equals("") ) {
					vqtd = Double.parseDouble(item.getEAUORG());
					vcusto = Double.parseDouble(item.getValorUnitario().replace(",","."));
					vTotal = vTotal + (vqtd * vcusto);
				}

			}

			NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
			setCustoTotal(defaultFormat.format(vTotal));

		}
	}

	public void editaQuantidade(CellEditEvent event) throws CorreiosException {

		String oldValue = (String) event.getOldValue();
		String newValue = (String) event.getNewValue();
		int index = event.getRowIndex();
		if (newValue == null || newValue.equals(oldValue) || newValue == "") {
			itensDoPedido.get(index).setEAUORG(oldValue);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Alteração na Quantidade", "A quantidade não é válida!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			calculaCustoTotal(itensDoPedido);
			FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formPedido:campoCusto");
		}
	}

	private void cancelar() {

	}

	public void onRowSelect(SelectEvent event) {

		ItemDisponivel selecionado = (ItemDisponivel) event.getObject();
		
		setTipoItem(selecionado.getItp_tx_tipo_item());
	
		setCustoUnitario(selecionado.getItp_tx_valor_unitario());
		
		setItemDisponivel(selecionado);
		itemDisponivel.setItp_tx_tipo_item(getTipoItem());

	}

	public void excluiItemDoPedido(ItemPedido item) throws SQLException {
		// if(item.isNovoItem()){
		itensDoPedido.remove(item);

		calculaCustoTotal(itensDoPedido);
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formPedido:campoCusto");
	}

	public String getUniqueChar() {
		//UniqueID uniqueid = new UniqueID();
		String alpha = "abcdefghijklmnopqrstuvxywz";
		
		int pos = ThreadLocalRandom.current().nextInt(0, 25);
		
		String chr = String.valueOf(alpha.charAt(pos));
			
		return chr ;
	}

	public void adicionaItemAoPedidoConfirmado() {
		// this.setHabilitaConfirma(true);
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        
		String justificativa = request.getParameter("formPedido:campojustificativa");
		
		this.setJustificativa(justificativa);
		
		ItemPedido item = converteParaItemPedido();

		item.setEAT001(getTipoItem());

		if (justificativa.length() < 15 || justificativa == "" || justificativa == null) {
			this.setJustificativa("");

			FacesMessage msg2 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção - Justificativa !!!!",
					"A justificativa para o item do pedido é obrigatória, e deve ter ao menos 15 caracteres ! \n Favor preencher o campo Justificativa.");
			FacesContext.getCurrentInstance().addMessage(null, msg2);
		} else {

			if (this.getJustificativa() != null) {
				item.setJustificativa(justificativa);
				item.setMediaSemetre(this.getMediaSemestre());
			//	item.setJustificativa("");
			//	item.setMediaSemetre("");
			} else {
				item.setJustificativa("");
				item.setMediaSemetre("");
			}

			itensDoPedido.add(item);

			calculaCustoTotal(itensDoPedido);

			setItemDisponivel(new ItemDisponivel());
			
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("formPedido:listaDoPedido");

			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("formPedido:campoCusto");

		}

		

	}
	
	public boolean checaQuantidadeMaxima(int qt){
		
		boolean resu = true;
		if(itemDisponivel.getPit_pe_item_padrao() != null){
			Integer qtMax = Integer.parseInt(itemDisponivel.getPit_pe_item_padrao());
			if(qt >  qtMax){
				resu = false;
			}
		}
		return resu;
		
	}
	
	public void JustificativaChangeMethod(ValueChangeEvent e) {
//		if (e.getNewValue().toString().length() < 15 || e.getNewValue().toString() == "") { //e.getOldValue().toString()) {
//			//this.setHabilitaConfirma(true);
//			this.setJustificativa("");
//			
//			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção - Justificativa !!!!",
//					"A justificativa para o item do pedido é obrigatória, e deve ter ao menos 15 caracteres ! \n Favor preencher o campo Justificativa.");
//			FacesContext.getCurrentInstance().addMessage(null, msg);
//			
//			//RequestContext.getCurrentInstance().execute("PF('dlgConfirma').show();");
//			
//		} else {
//			this.setJustificativa(e.getNewValue().toString());
//			this.adicionaItemAoPedidoConfirmado();
//		}
		
	}

	public void adicionaItemAoPedido() {

		if (checaDuplicidade()) {
			adicionaMensagemInfo(null, MensagemID.PEDIDO_POSSUI_ESSE_ITEM);
		} else {
			// adicionaItemAoPedidoConfirmado(); // comentar se voltar media
			// mensal

			// Calculo Media Descomentar se voltar
			//
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				ItemDisponivelDAO dao = new ItemDisponivelDAO(connection, usuario);

				if (this.checaQuantidadeMaxima(Integer.parseInt(this.itemDisponivel.getItp_tx_quantidade()))) {   //Checa se Quantidade Max foi execedida

					Double mediaItem = dao.findByMediaConsumo(usuario.getMculotacao(),
							getItemDisponivel().getItp_co_item());

					double mediaSemestre = ((double) Math.round((mediaItem * 1.2)));
					// getItemDisponivel().getItp_tx_quantidade()
					if ((Double.parseDouble(this.itemDisponivel.getItp_tx_quantidade()) > mediaSemestre)) {
						RequestContext.getCurrentInstance().execute("PF('dlgConfirma').show();");
						setMediaSemestre(String.valueOf(mediaSemestre));
					} else {
						ItemPedido item = converteParaItemPedido();
						item.setEAT001(getTipoItem());
						// item.setUnidMedFormatada(null);
						item.setUnidMedFormatada(getItemDisponivel().getUnidMedFormatada());
						itensDoPedido.add(item);
						calculaCustoTotal(itensDoPedido);
						setItemDisponivel(new ItemDisponivel());
					}
					FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds()
							.add("formPedido:listaDoPedido");

					FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds()
							.add("formPedido:campoCusto");
				}else{
					FacesMessage msg2 = new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção - Quantidade Máxima Excedida !!!!",
							"A quantidade máxima deste item para solicitação pelo seu órgão é: " + itemDisponivel.getPit_pe_item_padrao() + " \n\n Informe nova quantidade.");
					FacesContext.getCurrentInstance().addMessage(null, msg2);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (connection != null) {
					try {
						connection.close();
						connection = null;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} //
		}
	}

	public void salvarPedido() {

		if (itensDoPedido.size() > 0) {
			UtilData dterp = new UtilData();
			DataERP dtERP = new DataERP();

			DateTime dt = new DateTime(); // current time
			String hours = Integer.toString(dt.getHourOfDay()); // gets hour of
																// day
			String minutes = Integer.toString(dt.getMinuteOfHour());
			String seconds = Integer.toString(dt.getSecondOfMinute());
		

			// Verifica se o pedido é novo ou uma alteração

			boolean alterar = false;
			if (getNumeroPedido() == null) {
				String partCod = dtERP.getDataERP(dterp.getDataAtual()).substring(1, dtERP.getDataERP(dterp.getDataAtual()).length());
				setNumeroPedido(usuario.getMculotacao().trim()+partCod+getTipoPedido().toLowerCase()+getUniqueChar());
			} else {
				alterar = true;
			}

			trataItemDuplicado();
			
			boolean erroAusencia = false;
			int i = 0;
			while (i < itensDoPedido.size()) {
				itensDoPedido.get(i).setEAMCU1(usuario.getMculotacao());
				itensDoPedido.get(i).setEAMCU(usuario.getMcucia());
				itensDoPedido.get(i).setEAAA25(getNumeroPedido());
				itensDoPedido.get(i).setEAKCO(usuario.getSiglaCD());
				itensDoPedido.get(i).setEAT002(this.getTipoPedido());
				itensDoPedido.get(i).setEATDAY(hours + minutes + seconds);

				// Status do pedido no banco de dados(S)olicitado
				itensDoPedido.get(i).setEAEDSP("S");
				itensDoPedido.get(i).setEAUPMJ(
						dtERP.getDataERP(dterp.getDataAtual()));
				itensDoPedido.get(i).setEATRDJ(
						dtERP.getDataERP(dterp.getDataAtual()));
				if (usuario.getUsuarioInterno().getMatricula() != "") {
					itensDoPedido.get(i).setEAJOBN(
							usuario.getUsuarioInterno().getLogin());
				} else {
					itensDoPedido.get(i).setEAJOBN(
							usuario.getUsuarioExterno().getUsuarioExternoPF()
									.getCpf().replace(".", ""));
					itensDoPedido.get(i).setEAJOBN(
							itensDoPedido.get(i).getEAJOBN().replace("-", ""));
				}

				if (itensDoPedido.get(i).getEAT001() == null
						|| itensDoPedido.get(i).getEAT001().equals("")) {
					erroAusencia = true;
				}

				i = i + 1;
			}
			trataItemDuplicado();
			if (erroAusencia == false) {
				
				FacesContext.getCurrentInstance().getPartialViewContext()
						.getRenderIds().add("formPedido:resList");

				Connection connection = null;
				try {
					connection = dataSource.getConnection();
					ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);
				
					dao.insertBatch(itensDoPedido, alterar);
					
					
					JustificativaDAO dao2= new JustificativaDAO(connection, usuario);
					dao2.insertJustificativa(itensDoPedido);
				
				FacesContext.getCurrentInstance().getPartialViewContext()
						.getRenderIds().add("formPedido:listaDoPedido");

				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Inclusão do Pedido", "Seu pedido de número: "
								+ itensDoPedido.get(0).getEAAA25()
								+ " foi gravado!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				} catch (SiscapException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.getMessage();
					e.printStackTrace();
				} finally {
					if (connection != null) {
						try {
							connection.commit();
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				FacesContext.getCurrentInstance().getPartialViewContext()
						.getRenderIds().add("formPedido:listaDoPedido");
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Inclusão do Pedido",
						"Seu pedido não pode ser gravado porque há um item no pedido sem especificação TIPO. Exclua o item e tente incluí-lo novamente.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

		} else {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Inclusão do Pedido",
					"Nenhum item na lista para ser salvo!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
	
	

	public void excluir() throws SQLException, ClassNotFoundException, SiscapException {
		System.out.println("Excluindo pedido ...");
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);
			String txtNumPed = "";
			if (itensDoPedido.size() > 0) {

				dao.delete(this.numeroPedido);

				itensDoPedido.clear();
				setItemDisponivel(null);
				setNumeroPedido(null);

				FacesContext.getCurrentInstance().getPartialViewContext()
						.getRenderIds().add("formPedido:listaDoPedido");

				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Exclusão de Pedido", "Seu pedido de número: "
								+ txtNumPed + " foi excluído!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Exclusão do Pedido", "Nenhum pedido para exclusão!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (SQLException e) {
			throw new SiscapException("Erro ao excluir pedido: " + " - " + e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.commit();
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void fechaPedido(){
		setHabilitado(false);
		setLupaItemDisponivel(false);		
	}
	
	private void trataItemDuplicado() {
		int flag = 0;
		List<ItemPedido> itens = new ArrayList<ItemPedido>();
		if(itensDoPedido.size() > 0){
			for (ItemPedido iPedA : itensDoPedido) {
				if (itens.size() > 0) {
					for (ItemPedido iPedB : itens) {
						String itA = iPedA.getEALITM().trim();
						String itB = iPedB.getEALITM().trim();
						if (itA.equals(itB)) {
							flag = 1;
							break;
						}
					}
				} else {
					itens.add(iPedA);
					flag = 1;
				}
				if (flag == 0) {
					itens.add(iPedA);
				}
				flag = 0;
			}
		
		itensDoPedido.clear();
		itensDoPedido = itens;
		}
	}

}