package br.com.correios.siscap.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.SelectEvent;

import br.com.correios.ppjsiscap.excecao.CorreiosException;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.util.NotaInvalida;
import br.com.correios.ppjsiscap.util.UtilEmail;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.dao.ItemPedidoDAO;
import br.com.correios.siscap.excecao.UtilCorreiosException;
import br.com.correios.siscap.model.ItemPedido;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ConsultaPedidoMB extends MB {

	@Inject
	private Usuario usuario;

	@Inject
	@OracleDb
	private DataSource dataSource;

	@Inject
	private UtilCorreiosException utilCorreiosException;

	private String tipoPedido;

	private String numeroPedido;

	private List<ItemPedido> itensDoPedido = new ArrayList<ItemPedido>();

	private static ArrayList<ItemPedidoMB> pedidolist = new ArrayList<ItemPedidoMB>();

	private List<ItemPedido> pesquisalist = new ArrayList<ItemPedido>();

	private String StatusPedido;

	@PostConstruct
	public void init() {
		this.setPesquisalist(null);
	}

	public List<ItemPedido> getItensDoPedido() {
		return itensDoPedido;
	}

	public void setItensDoPedido(List<ItemPedido> itensDoPedido) {
		this.itensDoPedido = itensDoPedido;
	}

	public List<ItemPedido> getPesquisalist() {

		return pesquisalist;
	}

	public void setPesquisalist(List pesquisalist) {
		this.pesquisalist = this.recuperaPedido();
	}

	private void setNumeropedido(String eaaa25) {

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

	private void getPedidoPesquisa(String pCodPedido) {
		// TODO Auto-generated method stub

		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);

			List<ItemPedido> aPedido = dao.findByPedido(pCodPedido,
					"'S','V','R','C','O','D',' '", usuario.getMculotacao()); // aqui 

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
				itPed.setStatuspedido(iPed.getEAEDSP());
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
	 * @return the pesquisalist
	 */
	public List<ItemPedido> recuperaPedido() {

		List<ItemPedido> pedidos = null;
		Connection connection = null;

		try {
			connection = dataSource.getConnection();
			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);

			String valorMCU = usuario.getMculotacao();

			pedidos = dao.findByPedidoMCU(valorMCU, "'S','R','V','C','O',' '");

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

		try {
			connection = dataSource.getConnection();
			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);

			String valorMCU = usuario.getMcucia();

			
			List<ItemPedido> itens = dao.findByPedido(
					this.numeroPedido, StatusPedido, valorMCU);
			itensDoPedido.clear();
			itensDoPedido = itens;
			trataItemDuplicado();
			setItensDoPedido(itensDoPedido);

			// calculaCustoTotal(itensDoPedido);
//			UtilEmail utilEmail = new UtilEmail();
//			utilEmail.enviarEmail("ronaldpiacenti@correios.com.br", "SISCAP - Sistema de Captação - Aviso de Prazo\n\n", "Você pesquisou o pedido: "+itensDoPedido.get(0).getEAAA25() );
//			utilEmail = null;

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

		ItemPedido aItemSel = (ItemPedido) event.getObject();

		this.setNumeroPedido(aItemSel.getEAAA25());
		this.setTipoPedido(aItemSel.getEAT002());
		this.setStatusPedido(aItemSel.getEAEDSP());

		try {
				recuperaDetalhePedido();
		} catch (CorreiosException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formConsultaPedido:dlg");

		
	}

	public void closeList() {
		itensDoPedido.clear();
	}
	
	private void trataItemDuplicado() {
		int flag = 0;
		List<ItemPedido> itens = new ArrayList<ItemPedido>();
		if(itensDoPedido.size() > 0){
			for (ItemPedido iPedA : itensDoPedido) {
				if (itens.size() > 0) {
					for (ItemPedido iPedB : itens) {
						if (iPedA.getEALITM().trim()
								.equals(iPedB.getEALITM().trim())) {
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
