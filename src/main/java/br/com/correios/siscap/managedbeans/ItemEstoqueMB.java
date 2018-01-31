package br.com.correios.siscap.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.correios.ppjsiscap.paginador.Paginador;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.constantes.MensagemID;
import br.com.correios.siscap.dao.ItemDisponivelDAO;
import br.com.correios.siscap.excecao.UtilCorreiosException;
import br.com.correios.siscap.model.ItemDisponivel;
import br.com.correios.siscap.model.ItemPedido;


@SuppressWarnings("serial")
@Named
@ViewScoped
public class ItemEstoqueMB extends MB {

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

	private Boolean lupaItemDisponivel = false;

	private String tipoPedido;

	private String numeroPedido;

	private String mediaSemestre;

	private String custoTotal;
	
	private List<ItemDisponivel> itemDisponivelEstoque;

	private LazyDataModel<ItemDisponivel> itemDisponivelDataModel;

	private List<ItemPedido> itensDoPedido = new ArrayList<ItemPedido>();

	private List<ItemDisponivel> itensDisponiveisNoPedido = new ArrayList<ItemDisponivel>();
	
	private Connection sqliteCon;
	
	@PostConstruct
	public void init() {
		getListaItemDisponivel();
//		FacesContext.getCurrentInstance().getPartialViewContext()
//		.getRenderIds().add("form2:listaDeItemDisponivel");
	}
	
	
	private Paginador<ItemDisponivel> getListaItemDisponivelPaginada(
			Paginador<ItemDisponivel> p) {

		Connection connection = null;
		try {
			connection = dataSource.getConnection();

			ItemDisponivelDAO dao = new ItemDisponivelDAO(connection, usuario);
			
			if(itemDisponivelEstoque == null){
				itemDisponivelEstoque = dao.listaTodosItemComEstoquePaginado(p);
				p.setTotalDeRegistros((long) itemDisponivelEstoque.size());
				p = dao.paginacaoTodosItemComEstoquePaginado(itemDisponivelEstoque, p);
			}else{
				p.setTotalDeRegistros((long) itemDisponivelEstoque.size());
				p = dao.paginacaoTodosItemComEstoquePaginado(itemDisponivelEstoque, p);
			}
		} catch (SQLException e) {
			adicionaMensagemErro(null, MensagemID.ERRO,
					"Problema ao consultar a lista de itens disponíveis no estoque.");
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
	
	private void getListaItemDisponivel(){

		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			
			ItemDisponivelDAO dao = new ItemDisponivelDAO(connection, usuario);

			itemDisponivelEstoque = dao.listaTodosItemComEstoque(usuario.getMcucia(), "'T','E','N'", usuario.getTipoOrgao());

		} catch (SQLException e) {
			e.printStackTrace();
			// adicionaMensagemErro(null, MensagemID.ERRO,
			// "Problema ao consultar a lista de itens disponíveis para o pedido.");
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

	public List<ItemDisponivel> getItemDisponivelEstoque() {
		return itemDisponivelEstoque;
	}



	public void setItemDisponivelEstoque(List<ItemDisponivel> itemDisponivelEstoque) {
		this.itemDisponivelEstoque = itemDisponivelEstoque;
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
	
	private LazyDataModel<ItemDisponivel> novaInstanciaDeItemDisponivelDataModel() {
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

				//filter
				
				Paginador<ItemDisponivel> p = new Paginador<ItemDisponivel>();
				p.setEntityBean(getItemDisponivelPesquisa());
				p.getEntityBean().setItp_co_cia(usuario.getMcucia());
				p.setPrimeiroRegistro(first);
				p.setQuantidadeDeRegistrosPorPagina(pageSize);
				p = getListaItemDisponivelPaginada(p);
				this.setRowCount(p.getTotalDeRegistros().intValue());
				return p.getColecaoDeRegistros();
				
				
			}
		};
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
	
	
	
	
	private static String cd;
	private static String mcucia;
	private static String drcia;
	private static String ip;
	
	private String datahora;

	private static long totalreg;

	private static List<ItemDisponivel> listaitemdisponivel = new ArrayList<ItemDisponivel>();

	

	/**
	 * @return the listaitemdisponivel
	 */
	public ArrayList<ItemDisponivel> getListaitemdisponivel() {
		return (ArrayList<ItemDisponivel>) listaitemdisponivel;
	}

	public void InicializaListaItemDisponivel() throws SQLException {

		// String selectCat = getSelCat();

		// System.out.println("Tamanho da lista: " +
		// this.getListaitemdisponivel().size());
		if (this.getListaitemdisponivel().isEmpty()) {

			listaitemdisponivel.clear();
			totalreg = this.getCountItem();
			listaitemdisponivel = this.getListaItem(mcucia);

			FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formestoque:estList");
		}

	}

	public List<ItemDisponivel> getListaItem(String mcucd) {
		List<ItemDisponivel> tLista = (List<ItemDisponivel>) new ItemDisponivelMB();
		// System.out.println("CD do Usuário:" + usuario.getMcucia());
		// System.out.println("Grupo do Usuário:" +
		// usuario.getGrupos().get(0).getNome());
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		
		ItemDisponivelDAO dao = new ItemDisponivelDAO(connection, usuario);
		tLista = dao.findItemEstoqueoCD(mcucd, 1, totalreg, usuario.getTipoOrgao());

		dao = null;
		} catch (SQLException e) {
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
		return tLista;
	}

	public long getCountItem() throws SQLException {
		System.out.println("Carrega contador de itens.....");
		String mcucd = usuario.getMcucia();
		long recnumber = 0;
		if (totalreg > 0) {
			recnumber = totalreg;
		} else {
			Connection connection = dataSource.getConnection();
			ItemDisponivelDAO dao = new ItemDisponivelDAO(connection, usuario);
			recnumber = Integer.parseInt(dao.findByCount(mcucd));
		}

		return recnumber;

	}

	/**
	 * @return the cd
	 */
	public String getCd() {
		return cd;
	}

	/**
	 * @param cd
	 *            the cd to set
	 */
	public void setCd(String cd) {
		ItemEstoqueMB.cd = cd;
	}

	/**
	 * @return the mcucia
	 */
	public String getMcucia() {
		return mcucia;
	}

	/**
	 * @param mcucia
	 *            the mcucia to set
	 */
	public void setMcucia(String mcucia) {
		ItemEstoqueMB.mcucia = mcucia;
	}

	/**
	 * @return the drcia
	 */
	public String getDrcia() {
		return drcia;
	}

	/**
	 * @param drcia
	 *            the drcia to set
	 */
	public void setDrcia(String drcia) {
		ItemEstoqueMB.drcia = drcia;
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
	public void setIp(String ip) {
		ItemEstoqueMB.ip = ip;
	}

	/**
	 * @param listaitemdisponivel
	 *            the listaitemdisponivel to set
	 */
	public void setListaitemdisponivel(
			ArrayList<ItemDisponivel> listaitemdisponivel) {
		ItemEstoqueMB.listaitemdisponivel = listaitemdisponivel;
	}

	/**
	 * @return the datahora
	 */
	public String getDatahora() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEEEE, dd/MMMMMM/yyyy  -  HH:mm:ss");	
		//EEE, d MMM yyyy HH:mm:ss Z
		
		System.out.println("Data brasileira: "+ sdf.format(c.getTime()));
		
		return sdf.format(c.getTime()).replaceAll("/", " de ");
	}

	/**
	 * @param datahora the datahora to set
	 */
	public void setDatahora(String datahora) {
		this.datahora = datahora;
	}

	
}
