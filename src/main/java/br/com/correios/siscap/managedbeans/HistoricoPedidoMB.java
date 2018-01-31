package br.com.correios.siscap.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.SelectEvent;

import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.anotacoes.SqlDb;
import br.com.correios.siscap.dao.ItemPedidoDAO;
import br.com.correios.siscap.model.HistoricoPedido;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class HistoricoPedidoMB extends MB {

	@Inject
	private Usuario usuario;
	
	@Inject @OracleDb
	private DataSource dataSource;
	
	@Inject @SqlDb
	private DataSource dataSource2;

	private static String codmcu;
	private static String mculotacao;
	private static String mcucia;
	private static String siglacd;
	private static String ip;
	private static String drcia;
	
	private String numeroPedido;

	private List<String> registrados = new ArrayList<String>();
	
	private static ArrayList<HistoricoPedido> historicolist = new ArrayList<HistoricoPedido>();

	private static ArrayList<HistoricoPedido> pedidohistoricolist = new ArrayList<HistoricoPedido>();

	public HistoricoPedidoMB() {
		// TODO Auto-generated constructor stub
		FacesContext.getCurrentInstance().getPartialViewContext()
		.getRenderIds().add("formConsultaPedido:histlist");
	}

	@PostConstruct
	public void init() {
		setCodmcu(usuario.getMculotacao());
		setMcucia(usuario.getMcucia());
		setIp(usuario.getIp());
		setDrcia(usuario.getSiglaCD());
		if(pedidohistoricolist != null){
			pedidohistoricolist.clear();
		}
		if(historicolist != null){
			historicolist.clear();
		}
		getHistorico();
	}
	
	public String getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(String numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	/**
	 * @return the drcia
	 */
	public static String getDrcia() {
		return drcia;
	}

	/**
	 * @param drcia
	 *            the drcia to set
	 */
	public static void setDrcia(String drcia) {
		HistoricoPedidoMB.drcia = drcia;
	}

	/**
	 * @return the historicolist
	 */
	public ArrayList<HistoricoPedido> getHistoricolist() {
		return historicolist;
	}

	/**
	 * @param historicolist
	 *            the historicolist to set
	 */
	public void setHistoricolist(ArrayList<HistoricoPedido> phistoricolist) {
		historicolist = phistoricolist;
	}

	/**
	 * @return the pedidohistoricolist
	 */
	public ArrayList<HistoricoPedido> getPedidohistoricolist() {
		return pedidohistoricolist;
	}

	/**
	 * @param pedidohistoricolist
	 *            the pedidohistoricolist to set
	 */
	public void setPedidohistoricolist(
			ArrayList<HistoricoPedido> ppedidohistoricolist) {
		pedidohistoricolist = ppedidohistoricolist;
	}

	/**
	 * @return the codmcu
	 */
	public static String getCodmcu() {
		return codmcu;
	}

	/**
	 * @param codmcu
	 *            the codmcu to set
	 */
	public static void setCodmcu(String codmcu) {
		HistoricoPedidoMB.codmcu = codmcu;
	}

	/**
	 * @return the mculotacao
	 */
	public static String getMculotacao() {
		return mculotacao;
	}

	/**
	 * @param mculotacao
	 *            the mculotacao to set
	 */
	public static void setMculotacao(String mculotacao) {
		HistoricoPedidoMB.mculotacao = mculotacao;
	}

	/**
	 * @return the mcucia
	 */
	public static String getMcucia() {
		return mcucia;
	}

	/**
	 * @param mcucia
	 *            the mcucia to set
	 */
	public static void setMcucia(String mcucia) {
		HistoricoPedidoMB.mcucia = mcucia;
	}

	/**
	 * @return the siglacd
	 */
	public static String getSiglacd() {
		return siglacd;
	}

	/**
	 * @param siglacd
	 *            the siglacd to set
	 */
	public static void setSiglacd(String siglacd) {
		HistoricoPedidoMB.siglacd = siglacd;
	}

	/**
	 * @return the ip
	 */
	public static String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public static void setIp(String ip) {
		HistoricoPedidoMB.ip = ip;
	}
	
	/**
	 * @return the registrados
	 */
	public List<String> getRegistrados() {
		return registrados;
	}

	/**
	 * @param registrados the registrados to set
	 */
	public void setRegistrados(List<String> registrados) {
		this.registrados = registrados;
	}

	public void getHistorico()  {
		Connection connection1 = null;
		Connection connection2 = null;
		try{
			ItemPedidoDAO dao;
			if(mcucia.trim().equals("00004010")){
				connection1 = dataSource.getConnection();
				dao = new ItemPedidoDAO(connection1, usuario);
			}else{
				connection1 = dataSource.getConnection();
				connection2 = dataSource2.getConnection();
				dao = new ItemPedidoDAO(connection1,connection2, usuario);
			}

			historicolist.clear();
			historicolist = dao.getHistorico("A", codmcu, null, null, mcucia);
			
			removeNonDataFatura();
			
			FacesContext.getCurrentInstance().getPartialViewContext()
			.getRenderIds().add("formConsultaPedido:hisList");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(connection1 != null){
				try {
					connection1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(connection2 != null){
				try {
					connection2.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

	

	public void onRowSelectPedido(SelectEvent event)  {

		HistoricoPedido aItemSel = (HistoricoPedido) event
				.getObject();

		Connection connection1 = null;
		Connection connection2 = null;
		try{
			ItemPedidoDAO dao;
			if(mcucia.trim().equals("00004010")){
				connection1 = dataSource.getConnection();
				dao = new ItemPedidoDAO(connection1, usuario);
			}else{
				connection1 = dataSource.getConnection();
				connection2 = dataSource2.getConnection();
				dao = new ItemPedidoDAO(connection1,connection2, usuario);
			}
			
			pedidohistoricolist.clear();
			
			if((aItemSel.getVIN_CO() != null) && (aItemSel.getSDDRQJ() != null)){
				pedidohistoricolist = dao.getHistorico("B", codmcu, aItemSel.getVIN_CO(), aItemSel.getSDDRQJ(),mcucia);
				
			//	consolidaHistorico();
				
				if(mcucia.endsWith("    00004010")){
					registrados = (ArrayList<String>) dao.getRegistrados(aItemSel.getVIN_CO(), codmcu);
				}else{
					for(HistoricoPedido hPed : pedidohistoricolist){
						if(!hPed.getREAA15().equals("N/D")){
							registrados.add(hPed.getREAA15());
						}
					}
				}
			
				
				setNumeroPedido(aItemSel.getVIN_CO());
				
				FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formConsultaPedido:dlgHis");
				FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formConsultaPedido:dlgReg");
				
			}else{
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Histórico do Pedido", "Não foi possível recuperar o histórico deste pedido!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(connection1 != null){
				try {
					connection1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(connection2 != null){
				try {
					connection2.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		FacesContext.getCurrentInstance().getPartialViewContext()
		.getRenderIds().add("formConsultaPedido:histitem");

	}
	
	private void consolidaHistorico(){
		List<HistoricoPedido> tHist = new ArrayList<HistoricoPedido>();
				
		for(HistoricoPedido lHist : pedidohistoricolist){
			int found = 0;
			int indexHist = 0;
			for(HistoricoPedido cHist : tHist){
				if(lHist.getSDLITM().equals(tHist.get(indexHist).getSDLITM())){
					long qt = Long.parseLong(tHist.get(indexHist).getSDUORG()) + 1;
					long qta = Long.parseLong(cHist.getSDQTYT()) + 1;
					tHist.get(indexHist).setSDUORG(Long.toString(qt));
					tHist.get(indexHist).setSDQTYT(Long.toString(qta));
					found = 1;
					break;
				}
				indexHist ++;
			}
			if(found == 0){
				tHist.add(lHist);
			}
		}
		pedidohistoricolist.clear();
		for(HistoricoPedido cHist : tHist){
			pedidohistoricolist.add(cHist);
		}
	}
	
	private void removeNonDataFatura() {
		// TODO Auto-generated method stub
		
		List<HistoricoPedido> tHist = new ArrayList<HistoricoPedido>();
		
		for(HistoricoPedido lHist : historicolist){
			if(!lHist.getSDDRQJ().equals("-")){
				tHist.add(lHist);
			}
		}
		historicolist.clear();
		for(HistoricoPedido cHist : tHist){
			historicolist.add(cHist);
		}
	}
}
