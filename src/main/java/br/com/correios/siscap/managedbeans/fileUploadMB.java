package br.com.correios.siscap.managedbeans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.correios.ppjsiscap.enums.TIPOPEDIDO;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.util.DataERP;
import br.com.correios.ppjsiscap.util.UniqueKey;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.dao.ItemPedidoDAO;
import br.com.correios.siscap.excecao.UtilCorreiosException;
import br.com.correios.siscap.model.ItemPedido;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class fileUploadMB {
	@Inject
	private Usuario usuario;

	@Inject
	@OracleDb
	private DataSource dataSource;

	@Inject
	private UtilCorreiosException utilCorreiosException;
	private static final long serialVersionUID = 1L;
	private UploadedFile file;
	private static String tipo = null;
	private static String categoria = null;
	private static String mcucia;
	private static String drcia;
	private static String ip;

	private Boolean status = false;

	private ArrayList<ItemPedido> itemPedido = new ArrayList<ItemPedido>();

	private List<SelectItem> listacategoria;

	private List<SelectItem> listatipopedido;

	private List<SelectItem> itemcategoria;

	private List<SelectItem> itemtipopedido;

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public void upload(FileUploadEvent event) {
		file = event.getFile();

		Connection connection = null;

		String codPedido = null;

		try {
			connection = dataSource.getConnection();

			ItemPedidoDAO dao = new ItemPedidoDAO(connection, usuario);

			new UniqueKey();
			codPedido = UniqueKey.createOrderNumber();

			if (file != null) {

				BufferedReader lerArq = new BufferedReader(
						new InputStreamReader(file.getInputstream()));

				DataERP dataERP = new DataERP();
				String dataPedido = dataERP.getDataERPToday();
				String linha = null;
				String[] aLinha = null;
			//	String ItemAtual = "111111111";
			//	String Unidade = null;
				lerArq.readLine();

				while ((linha = lerArq.readLine()) != null) {

					if (linha.length() > 10) {
						aLinha = linha.split(";");

						ItemPedido itemPed = new ItemPedido();

					//	if (ItemAtual.equals(aLinha[1])) {
							// Aqui consulta a unidade de medida do item o item
							// atual IMPLEMNETAR
						//	Unidade = aLinha[3];
					//		ItemAtual = aLinha[1]; // exemplo

					//	}
						/*
						 * O Layout do Arquivo a ser imoprtado deve serguir o
						 * formato: MCU Orgão, Código do Item, Quantidade e UNIDADE
						 */
						// private String EAKCO; // C�digo da Cia
						itemPed.setEAKCO(getDrcia());
						// private String EAMCU; // MCU da Cia
						itemPed.setEAMCU(getMcucia());
						// private String EAJOBN; // Identifica��o Esta��o de
						// trabalho
						// itemPed.setEAJOBN(usuario.getIp());
						itemPed.setEAMCU1(aLinha[0]); // MCU Solicitante
						itemPed.setEATRDJ(dataPedido); // EATRDJ - Data Juliana
														// do
														// Pedido
						itemPed.setEALITM(aLinha[1]); // EALITM; - C�digo Longo
														// do
														// ItemPedidoMB
						itemPed.setEAUORG(aLinha[2]); // EAUORG; - Quantidade do
														// ItemPedidoMB
						itemPed.setEAT001(this.getCategoria()); // EAT001 -
																// Natureza
						// do
						// Pedido
						// (Produto/Material)
						itemPed.setEAT002(this.getTipo()); // EAT002 - Tipo do
															// Pedido
															// (Extra / Normal)
						itemPed.setEAUOM(aLinha[3]); // EAUOM - Unidade de Medida
													// do
													// ItemPedidoMB
						itemPed.setEAAA25(aLinha[0].trim() + codPedido); // EAAA25
																			// -
																			// N�mero
																			// do
						// Pedido
						// SISCAP
						itemPed.setEAEDSP("I"); // LIBERADOPOR DEFAULT EAEDSP -
												// Status de Processamento do
												// ItemPedidoMB
						itemPed.setEAUPMJ(dataPedido); // EAUPMJ - Data da
														// Atualiza��o no Banco
														// de
														// Dados
						itemPed.setEACKEDSP(" "); // EACKEDSP - Campo de
													// Controle
													// (Em Branco)
						itemPed.setEAUSER("WEBPEDIDOS"); // EAUSER =
															// "WEBPEDIDOS" -
															// Fixo (WEBPEDIDOS)
						itemPed.setEAPID("SISCAP_ERP"); // private String EAPID
														// =
														// "SISCAP_ERP" - Fixo
														// (SISCAP_ERP)
						itemPed.setEATDAY(dataPedido); // da insert no banco de
														// dados

						itemPed.setEAJOBN(this.getIp()); // ip estação cliente

					//	itemPedido.add(itemPed);
						dao.insert(itemPed);

						

					}
				}
				// System.out.println("Passei aqui 5");
				lerArq.close();
				/*
				 * System.out.println("Tanhamo do Model:"+ itemPedido.size());
				 * System.out.println("Dado:Pedido:"+
				 * itemPedido.get(0).getEAAA25()); System.out.println("Dado::"+
				 * itemPedido.get(0).getEACKEDSP());
				 * System.out.println("Dado:Statsu Proc:"+
				 * itemPedido.get(0).getEAEDSP());
				 * System.out.println("Dado:Programa:"+
				 * itemPedido.get(0).getEAJOBN());
				 * System.out.println("Dado:DR Compainha:"+
				 * itemPedido.get(0).getEAKCO());
				 * System.out.println("Dado:item:"+
				 * itemPedido.get(0).getEALITM());
				 * System.out.println("Dado:MCU CD:"+
				 * itemPedido.get(0).getEAMCU());
				 * System.out.println("Dado:MCU ORGAO:"+
				 * itemPedido.get(0).getEAMCU1());
				 * System.out.println("Dado:ID:"+ itemPedido.get(0).getEAPID());
				 * System.out.println("Dado:TIPO:"+
				 * itemPedido.get(0).getEAT001());
				 * System.out.println("Dado:CATEGORIA:"+
				 * itemPedido.get(0).getEAT002());
				 * System.out.println("Dado:DAT DAY:"+
				 * itemPedido.get(0).getEATDAY());
				 * System.out.println("Dado:DAT DJ:"+
				 * itemPedido.get(0).getEATRDJ());
				 * System.out.println("Dado:UOM:"+
				 * itemPedido.get(0).getEAUOM());
				 * System.out.println("Dado:QUANTIDADE:"+
				 * itemPedido.get(0).getEAUORG());
				 * System.out.println("Dado:DAT PMJ:"+
				 * itemPedido.get(0).getEAUPMJ());
				 * System.out.println("Dado:USER:"+
				 * itemPedido.get(0).getEAUSER());
				 * System.out.println("Dado:USER:"+
				 * itemPedido.get(0).getEAJOBN());
				 */

				
				status = true;
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"O arquivo", file.getFileName()
								+ " foi processado com sucesso.");
				FacesContext.getCurrentInstance().addMessage(null, msg);

			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
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
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String pip) {
		ip = pip;
	}

	/**
	 * @return the drcia
	 */
	public String getDrcia() {
		setDrcia(usuario.getSiglaCD());
		return drcia;
	}

	/**
	 * @param drcia
	 *            the drcia to set
	 */
	public void setDrcia(String pdrcia) {
		// System.out.println("Não é que chegou ???"+pdrcia);
		drcia = pdrcia;
	}

	/**
	 * @return the mcucia
	 */
	public String getMcucia() {
		setMcucia(usuario.getMcucia());
		return mcucia;
	}

	/**
	 * @param mcucia
	 *            the mcucia to set
	 */
	public void setMcucia(String pmcucia) {
		// System.out.println("Não é que chegou ???"+pmcucia);
		mcucia = pmcucia;
	}

	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(String ptipo) {
		tipo = ptipo;
	}

	/**
	 * @return the categoria
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * @param categoria
	 *            the categoria to set
	 */
	public void setCategoria(String pcategoria) {
		categoria = pcategoria;
	}

	/**
	 * @return the itemcategoria
	 */
	public List<SelectItem> getItemcategoria() {
		return itemcategoria;
	}

	/**
	 * @param listacategoria
	 *            the itemacategoria to set
	 */
	public void setItemcategoria(List<SelectItem> itemcategoria) {
		this.itemcategoria = itemcategoria;
	}

	/**
	 * @return the listatipopedido
	 */
	public List<SelectItem> getItemtipopedido() {
		return itemtipopedido;
	}

	/**
	 * @param listatipopedido
	 *            the listatipopedido to set
	 */
	public void setItemtipopedido(List<SelectItem> itemtipopedido) {
		this.itemtipopedido = itemtipopedido;
	}

	/**
	 * Retorna uma cole��o de TipoPedidos para exibi��o na combobox.
	 * 
	 * @return
	 */

	/**
	 * @param listatipopedido
	 *            the listatipopedido to set
	 */
	public void setListatipopedido(List<SelectItem> listatipopedido) {
		// System.out.println("PAsssei Aqui 1!");
		this.listatipopedido = listatipopedido;
	}

	public List<SelectItem> getListatipopedido() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(TIPOPEDIDO.EXTRA.getCodigo(), TIPOPEDIDO.EXTRA
				.getDescricao()));
		lista.add(new SelectItem(TIPOPEDIDO.NORMAL.getCodigo(),
				TIPOPEDIDO.NORMAL.getDescricao()));
		// System.out.println(lista.get(0));
		return lista;
	}

	/**
	 * Retorna uma cole��o de CategoriaPedidos para exibi��o na combobox.
	 * 
	 * @return
	 */

	/**
	 * @param listacategoria
	 *            the listacategoria to set
	 */
	public void setListacategoria(List<SelectItem> listacategoria) {

		this.listacategoria = listacategoria;
	}

	/**
	 * @param handleClose
	 *            Popula campos de itens
	 */
	public void handleClose() {

	}
}