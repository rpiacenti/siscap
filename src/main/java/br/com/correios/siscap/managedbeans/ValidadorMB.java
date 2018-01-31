package br.com.correios.siscap.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import br.com.correios.ppjsiscap.excecao.CorreiosException;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.util.UtilEmail;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.dao.ItemPedidoDAO;
import br.com.correios.siscap.dao.OrgaoDAO;
import br.com.correios.siscap.excecao.SiscapException;
import br.com.correios.siscap.excecao.UtilCorreiosException;
import br.com.correios.siscap.model.ItemPedido;
import br.com.correios.siscap.model.Orgao;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ValidadorMB extends MB {
	@Inject
	private Usuario usuario;

	@Inject
	@OracleDb
	private DataSource dataSource;

	@Inject
	private UtilCorreiosException utilCorreiosException;

	private String tipoPedido;

	private String numeroPedido;
	
	private String mcuOrgaoSolicitante;
	
	private ItemPedido itemPedido;

	private List<ItemPedido> itensDoPedido = new ArrayList<ItemPedido>();

	private ArrayList<ItemPedidoMB> pedidolist = new ArrayList<ItemPedidoMB>();

	private List<ItemPedido> validalist = new ArrayList<ItemPedido>();
	
	private List<ItemPedido> canceladosList = new ArrayList<ItemPedido>();

	private String StatusPedido;
	
	private String StatusPrevio;
	
	private boolean disabledValida = true;

	private boolean disabledCancel = true;

	private boolean disabledLibera = true;

	@PostConstruct
	public void init() {
		this.setvalidalist(null);
	}

	/**
	 * @return the canceladosList
	 */
	public List<ItemPedido> getCanceladosList() {
		return canceladosList;
	}

	/**
	 * @param canceladosList the canceladosList to set
	 */
	public void setCanceladosList(List<ItemPedido> canceladosList) {
		this.canceladosList = canceladosList;
	}



	public List<ItemPedido> getItensDoPedido() {
		return itensDoPedido;
	}

	public void setItensDoPedido(List<ItemPedido> itensDoPedido) {
		this.itensDoPedido = itensDoPedido;
	}

	public List<ItemPedido> getvalidalist() {

		return validalist;
	}

	public void setvalidalist(List<ItemPedido> validalist) {
		this.validalist.clear();
		this.validalist = recuperaPedido();
	}

	private void setNumeropedido(String eaaa25) {

	}

	public ItemPedido getItemPedido() {
		return itemPedido;
	}

	public void setItemPedido(ItemPedido itemPedido) {
		this.itemPedido = itemPedido;
	}
	
	public void marcaItem(ActionEvent actionEvent){
		this.setItemPedido(itemPedido);
	}

	public String getTipoPedido() {
		return tipoPedido;
	}

	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido;
	}

	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

		public String getStatusPedido() {
		return StatusPedido;
	}

	public void setStatusPedido(String statusPedido) {
		StatusPedido = statusPedido;
	}

	
	public String getStatusPrevio() {
		return StatusPrevio;
	}

	public void setStatusPrevio(String statusPrevio) {
		StatusPrevio = statusPrevio;
	}

	public boolean isDisabledValida() {
		return disabledValida;
	}

	public void setDisabledValida(boolean disabledValida) {
		this.disabledValida = disabledValida;
	}

	public boolean isDisabledCancel() {
		return disabledCancel;
	}

	public void setDisabledCancel(boolean disabledCancel) {
		this.disabledCancel = disabledCancel;
	}

	public boolean isDisabledLibera() {
		return disabledLibera;
	}

	public void setDisabledLibera(boolean disabledLibera) {
		this.disabledLibera = disabledLibera;
	}

	private void getPedidoPesquisa(String pCodPedido) {
		// TODO Auto-generated method stub

		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);

			List<ItemPedido> aPedido = dao.findByPedido(pCodPedido,"'V','R',' '", usuario.getMculotacao()); // aqui

			pedidolist.clear();

			for (ItemPedido iPed : aPedido) {

				ItemPedidoMB itPed = new ItemPedidoMB();

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
				/*
				 * String[] aDetalheItem = getDetalheItem(iPed.getEALITM()); if
				 * (aDetalheItem == null) { itPed.setValorUnitario("0");
				 * itPed.setDescricaoItem(""); } else {
				 * itPed.setValorUnitario(aDetalheItem[1]);
				 * itPed.setDescricaoItem(aDetalheItem[0]); }
				 */
				setNumeropedido(iPed.getEAAA25());
				pedidolist.add(itPed);

				itPed = null;
			}
			// this.setCustototal(this.getCustototal());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formPesquisa:itemList");

	}

	/**
	 * @return the validalist
	 */
	public List<ItemPedido> recuperaPedido() {

		List<ItemPedido> pedidos = null;
		List<ItemPedido> tempPed = new ArrayList<ItemPedido>();
		
		boolean exibirCancelados = false;
		
		Connection connection = null;

		try {
			connection = dataSource.getConnection();
			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);

			pedidos = dao.findByPedidoValidacao("'R','V',' '");
			
			for(ItemPedido iPed : pedidos){
				if(!dao.checaOrgao(iPed.getEAMCU1())){
					dao.calcelaPedido(iPed.getEAAA25());
					canceladosList.add(iPed);
					exibirCancelados = true;
				}else{
					tempPed.add(iPed);
				}
			}
			
			pedidos.clear();
			
			pedidos = tempPed;
			
			if(exibirCancelados){
				RequestContext.getCurrentInstance().execute("PF('dlgCanc').show()");
			}
			
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

		return pedidos;
	}

	public void recuperaDetalhePedido() throws CorreiosException {

		Connection connection = null;
		
		itensDoPedido.clear();
		setStatusPedido("R");
		
		try {
			connection = dataSource.getConnection();
			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);

			String valorMCU = usuario.getMcucia();

			List<ItemPedido> itens = dao.findByPedido(
					this.numeroPedido, StatusPedido, valorMCU);
			
			itens = dao.checaitens(itens);
			
			itensDoPedido.clear();
			itensDoPedido = itens;
			trataItemDuplicado();
			setItensDoPedido(itensDoPedido);
			
			if(itensDoPedido.size() > 0){
				this.mcuOrgaoSolicitante = itensDoPedido.get(0).getEAMCU1();
			}	
			// calculaCustoTotal(itensDoPedido);

		} catch (SQLException e) {
			throw utilCorreiosException.erro(e);
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

	public void onRowSelectPedido(SelectEvent event) {

		if (StatusPedido != null) {
			if (getStatusPedido().indexOf("R") > -1) {
				onSairPedido();
			}
		}
		
		
		ItemPedido aItemSel = (ItemPedido) event.getObject();
		setNumeroPedido(aItemSel.getEAAA25());
		setTipoPedido(aItemSel.getEAT002());
		setStatusPedido(aItemSel.getEAEDSP());
		setStatusPrevio(getStatusPedido());
				
		if (getStatusPedido().indexOf("R") > -1) {
			setNumeroPedido("");
			itensDoPedido.clear();
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Seleção de Pedido", "O pedido selecionado está em aprovação por outro valiador.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("formValida:dlg");

		} else {

			Connection connection = null;

			try {
				connection = dataSource.getConnection();
				
				// Muda status do pedido para R (em Revisão)
				
				ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);
				dao.changeStatusPedido(getNumeroPedido(),getStatusPedido(),"R");
				
				recuperaDetalhePedido();
				
				setDisabledCancel(false);
				setDisabledLibera(false);
				setDisabledValida(false);
				
				setvalidalist(null);
				
				
				this.atualizaForm();

			} catch (CorreiosException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (SiscapException e) {
				// TODO Auto-generated catch block
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
		}
	}
	

	public void onExcluiItem(ItemPedido item ) {

			this.itensDoPedido.remove(item);
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);
				
				dao.cancelItemPedido(item.getEAAA25(), item.getEALITM());

			} catch (SQLException e) {
				FacesMessage msgerr = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Erro de Instrução no Banco de Dados", "Erro de SQL !");
				FacesContext.getCurrentInstance().addMessage(null, msgerr);
			} catch (SiscapException e) {
				// TODO Auto-generated catch block
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
			

			RequestContext context = RequestContext.getCurrentInstance();
			context.update("formValida:itemList");
			
			FacesMessage msgerr = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Cancelamento de item de pedido.", "Item de pedido cancelado pelo validador !");
			FacesContext.getCurrentInstance().addMessage(null, msgerr);

	}

	public void onCancelPedido() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);
			
			dao.changeStatusPedido(getNumeroPedido(), getStatusPedido(),"C");

			dao = null;

			this.setvalidalist(null);
			
			this.mcuOrgaoSolicitante = "";

		} catch (SQLException e) {
			FacesMessage msgerr = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro de Instrução no Banco de Dados", "Erro de SQL !");
			FacesContext.getCurrentInstance().addMessage(null, msgerr);
		} catch (SiscapException e) {
			// TODO Auto-generated catch block
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

		setStatusPrevio("C");
		
		 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
		 "Pedido Cancelado", "O pedido de número: " + getNumeroPedido()
		 + " foi cancelado pelo validador.");
		 FacesContext.getCurrentInstance().addMessage(null, msg);
		 
		 RequestContext context = RequestContext.getCurrentInstance();
				context.update("formValida");
		
		

	}

	public void onValidarPedido() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);
			
			dao.changeStatusPedido(getNumeroPedido(), "R" ," ");

			dao = null;
			
		//	sendMail(this.mcuOrgaoSolicitante, getNumeroPedido(), " ");
			this.setvalidalist(null);
			
			this.mcuOrgaoSolicitante = "";

		} catch (SQLException e) {
			FacesMessage msgerr = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro de Instrução no Banco de Dados", "Erro de SQL !");
			FacesContext.getCurrentInstance().addMessage(null, msgerr);
		} catch (SiscapException e) {
			// TODO Auto-generated catch block
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

		setStatusPrevio(" ");
		
		 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
		 "Pedido Validado", "O pedido de número: " + getNumeroPedido()
		 + " foi validado.");
		 FacesContext.getCurrentInstance().addMessage(null, msg);
		 
		 RequestContext context = RequestContext.getCurrentInstance();
				context.update("formValida");
	}
	
	public void onSairPedido(){
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);
			// Muda status do pedido de R (em Revisão) para anteiror
			if(getStatusPedido() != "C"){
				dao.changeStatusPedido(getNumeroPedido(), "R", this.getStatusPrevio());
			}
			
			itensDoPedido.clear();
			
		    this.setNumeroPedido("");
		    
		    this.mcuOrgaoSolicitante = "";
			
		    setDisabledCancel(true);
			setDisabledLibera(true);
			setDisabledValida(true);
		    
			setvalidalist(null);

			setNumeroPedido("");

			atualizaForm();
		

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (SiscapException e) {
			// TODO Auto-generated catch block
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
	}
	
	public void onEditValidador(CellEditEvent event){
		
		String oldValue = (String) event.getOldValue();
		String newValue = (String) event.getNewValue();
		int index = event.getRowIndex();

		Connection connection = null;
		if (newValue == null || newValue.equals(oldValue) || newValue == "") {
			itensDoPedido.get(index).setEAUORG(oldValue);
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Alteração na Quantidade", "A quantidade não é válida!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			try {
				connection = dataSource.getConnection();

				ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);

				dao.updateQtdPedido(getNumeroPedido(), itensDoPedido.get(index).getEALITM(), newValue);

				FacesMessage msgerr = new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Alteração de Dados",
						"Quantidade alterada pelo validador.");
				FacesContext.getCurrentInstance().addMessage(null, msgerr);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				FacesMessage msgerr = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Erro de Instrução no Banco de Dados", "Erro de SQL !");
				FacesContext.getCurrentInstance().addMessage(null, msgerr);
			} catch (SiscapException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		FacesMessage msgerr = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"Editar Celula", "EDITADO !");
		FacesContext.getCurrentInstance().addMessage(null, msgerr);
	}
	
	public void onEditValidadorLinha(RowEditEvent event){
	
		FacesMessage msgerr = new FacesMessage(FacesMessage.SEVERITY_ERROR,
				"Editar Celula", "EDITADO !");
		FacesContext.getCurrentInstance().addMessage(null, msgerr);
	}
	
	private String getStatus(String syedsp) {
		String resu = null;
		switch (syedsp) {
		case "A":
			resu = "Aprovado";
			break;
		case "R":
			resu = "Em Avaliação";
			break;
		case "S":
			resu = "Solicitado";
			break;
		case "V":
			resu = "Em Validação";
			break;
		case "O":
			resu = "Obsoleto";
			break;
		case "Y":
			resu = "Processado";
			break;
		case " ":
			resu = "Liberado Processado ERP";
			break;
		}

		return resu;
	}

	public void atualizaForm(){
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("formValida:itemList");
		context = RequestContext.getCurrentInstance();
		context.update("formValida:valList");
		context = RequestContext.getCurrentInstance();
		context.update("formValida:statusPedido");
		context = RequestContext.getCurrentInstance();
		context.update("formValida:btnSair");
		context = RequestContext.getCurrentInstance();
		context.update("formValida:btnValidar");
		context = RequestContext.getCurrentInstance();
		context.update("formValida:btnCancelar");
	}
	
		
	private void trataItemDuplicado(){
		int flag = 0;
		int totalInitItens = 0;
		int totalFinalItens = 0;
		List<ItemPedido> itens = new ArrayList<ItemPedido>();
		if (itensDoPedido.size() > 0) {
			totalInitItens = itensDoPedido.size();
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

			totalFinalItens = itensDoPedido.size();

			if (totalInitItens > totalFinalItens) {
				Connection connection = null;
				try {
					connection = dataSource.getConnection();
					ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);

					dao.insertBatchValidacao(itensDoPedido, true);
				} catch (SQLException e) {
					try {
						throw new SiscapException(
								"Erro instrução em banco de dados: " + " - "
										+ e.getMessage());
					} catch (SiscapException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (SiscapException e) {
					// TODO Auto-generated catch block
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
			}

		}

	}
	
	public void sendMail(String mcuOrgao, String codPedido, String status){
		UtilEmail mail = new UtilEmail();
	//	UtilSendMail mail = new UtilSendMail();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy às HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String data = dateFormat.format(cal.getTime());

		Connection connection = null;
		try {
			connection = dataSource.getConnection();

			OrgaoDAO dao = new OrgaoDAO(connection);
			
			Orgao org = dao.findByOrgao(mcuOrgao);

			//mail.setPMail(org.getEmail().toLowerCase());
			
			String dest = "ronaldpiacenti@correios.com.br";
			String assu = "SISCAP - Aviso mudança de Status do Pedido: "+codPedido;
			StringBuffer bf = new StringBuffer("Prezado(a) Senhor(a) "
					+ org.getNome()
					+ ",\n\n"
					+ "Informamos mudança de status de seu pedido "
					+ codPedido
					+ " em " + data
					+ "\n\n"
					+ "O status atual e: "+ this.getStatus(status)
					+ "\n\n"
					+ "Atenciosamente,\n\n" + "Suporte SISCAP\n");
			String[] aDest={dest};
	
		//	String postMail = mail.postMail(aDest, assu , bf.toString(), dest);
		//	boolean postMail = mail.enviarEmail(dest, assu , bf.toString());
			
//			FacesMessage msgerr = new FacesMessage(
//					FacesMessage.SEVERITY_INFO, "Envio Email",
//					"Status retornado: "+postMail);
//			FacesContext.getCurrentInstance().addMessage(null, msgerr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesMessage msgerr = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro de Instrução no Banco de Dados", "Erro de SQL !");
			FacesContext.getCurrentInstance().addMessage(null, msgerr);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
