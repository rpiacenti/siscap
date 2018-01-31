package br.com.correios.siscap.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import br.com.correios.ppjsiscap.enums.CD;
import br.com.correios.ppjsiscap.paginador.Paginador;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.dao.GrupoAtendimentoDAO;
import br.com.correios.siscap.dao.OrgaoDAO;
import br.com.correios.siscap.dao.OrgaoGrupoDAO;
import br.com.correios.siscap.excecao.UtilCorreiosException;
import br.com.correios.siscap.model.GrupoAtendimento;
import br.com.correios.siscap.model.Orgao;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class OrgaoGrupoAtendimentoMB extends MB {

	@Inject
	private Usuario usuario;

	@Inject
	@OracleDb
	private DataSource dataSource;

	@Inject
	private UtilCorreiosException utilCorreiosException;

	private GrupoAtendimento grupoAtendimento = new GrupoAtendimento();

	private List<GrupoAtendimento> gruposDeAtendimento = new ArrayList<GrupoAtendimento>();

	private LazyDataModel<GrupoAtendimento> grupoAtendimentoDataModel;

	private Orgao orgao = new Orgao();
	
	private Orgao orgaoPesquisa = new Orgao();

	private LazyDataModel<Orgao> orgaoDataModel;

	private Orgao selectedOrgao = new Orgao();

	private List<Orgao> filteredOrgao;

	@PostConstruct
	public void init() {
		mcucia = usuario.getMcucia();

		getListaGrupo(mcucia);
	}

	public LazyDataModel<GrupoAtendimento> novaInstanciaGrp() {
		return new LazyDataModel<GrupoAtendimento>() {
			@Override
			public List<GrupoAtendimento> load(int first, int pageSize,
					String sortField, SortOrder sortOrder,
					Map<String, Object> filters) {
				
				
				
				Paginador<GrupoAtendimento> p = new Paginador<GrupoAtendimento>();
				p.setEntityBean(new GrupoAtendimento());
				
				p.getEntityBean().setMcuCd(usuario.getMcucia());
				p.setPrimeiroRegistro(first);
				p = getListaDeGruposDeAtendimentoPaginada(p);
				this.setRowCount(p.getTotalDeRegistros().intValue());
				return p.getColecaoDeRegistros();
			}
		};
	}

	public LazyDataModel<Orgao> novaInstanciaOrg() {
		return new LazyDataModel<Orgao>() {
			@Override
			public List<Orgao> load(int first, int pageSize, String sortField,
					SortOrder sortOrder, Map<String, Object> filters) {
				//filter
				if (filters != null) {
					orgaoPesquisa.setMcu(null);
					orgaoPesquisa.setNome(null);
					orgaoPesquisa.setDr(null);
	                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
	                    try {
	                        String filterProperty = it.next();
	                        String filterValue = (String) filters.get(filterProperty);
	                        if(filterProperty.indexOf("mcu") > -1){
	                        	orgaoPesquisa.setMcu(filterValue);
	                        }
	                        
	                        if(filterProperty.indexOf("nome") > -1){
	                        	orgaoPesquisa.setNome(filterValue);
	                        }
	                        
	                        if(filterProperty.indexOf("dr") > -1){
	                        	orgaoPesquisa.setDr(filterValue);
	                        }
	                    } catch(Exception e) {
	                    	e.printStackTrace();
	                    }
	                }
	            }
				//filter
				
				Paginador<Orgao> p = new Paginador<Orgao>();
				p.setEntityBean(new Orgao());
				p.setEntityBean(getOrgaoPesquisa());
				p.getEntityBean().setMcucia(usuario.getMcucia());
				if (grupoAtendimento.getNumGrupo() != null) {
					p.getEntityBean().setGrptransito(
							grupoAtendimento.getNumGrupo());
				}
				p.setPrimeiroRegistro(first);
				p.setQuantidadeDePaginas(pageSize);
				p = getListaDeOrgaoPaginada(p);
				this.setRowCount(p.getTotalDeRegistros().intValue());
				return p.getColecaoDeRegistros();
			}

			@Override
			public List<Orgao> load(int first, int pageSize,
					List<SortMeta> multiSortMeta, Map<String, Object> filters) {
				// TODO Auto-generated method stub

				Paginador<Orgao> p = new Paginador<Orgao>();
				p.setEntityBean(new Orgao());
			//	p.getEntityBean().setMcu(usuario.getMcucia());
				if (grupoAtendimento.getNumGrupo() != null) {
					p.getEntityBean().setGrptransito(
							grupoAtendimento.getNumGrupo());
				}
				p.setPrimeiroRegistro(first);
				p = getListaDeOrgaoPaginada(p);
				this.setRowCount(p.getTotalDeRegistros().intValue());
				return p.getColecaoDeRegistros();
			}

			@Override
			public String getRowKey(Orgao org) {
				return "ok";
				// throw new
				// UnsupportedOperationException("getRowKey(T object) must be implemented when basic rowKey algorithm is not used.");

			}
		};
	}

	public Paginador<GrupoAtendimento> getListaDeGruposDeAtendimentoPaginada(
			Paginador<GrupoAtendimento> p) {
		Connection connection = null;

		if (p.getEntityBean().getMcuCd() != null) {
			try {
				connection = dataSource.getConnection();
				GrupoAtendimentoDAO dao = new GrupoAtendimentoDAO(connection, usuario);
				p = dao.listaTodosGruposDeAtendimentoPaginado(p);
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
		}
		return p;

	}

	public Paginador<Orgao> getListaDeOrgaoPaginada(Paginador<Orgao> p) {
		Connection connection = null;

		if (p.getEntityBean().getMcucia() != null) {
			try {
				connection = dataSource.getConnection();
				OrgaoDAO dao = new OrgaoDAO(connection);
				p = dao.listaTodosOrgaoPaginado(p);
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
		}
		return p;

	}

	/**
	 * @return the grupoAtendimentoDataModel
	 */
	public LazyDataModel<GrupoAtendimento> getGrupoAtendimentoDataModel() {
		if (grupoAtendimentoDataModel == null) {
			grupoAtendimentoDataModel = novaInstanciaGrp();
		}
		return grupoAtendimentoDataModel;
	}

	/**
	 * @param grupoAtendimentoDataModel
	 *            the grupoAtendimentoDataModel to set
	 */
	public void setGrupoAtendimentoDataModel(
			LazyDataModel<GrupoAtendimento> grupoAtendimentoDataModel) {
		this.grupoAtendimentoDataModel = grupoAtendimentoDataModel;
	}

	public LazyDataModel<Orgao> getOrgaoDataModel() {
		if (orgaoDataModel == null) {
			orgaoDataModel = novaInstanciaOrg();

		}
		return orgaoDataModel;
	}

	public void setOrgaoDataModel(LazyDataModel<Orgao> orgaoDataModel) {
		this.orgaoDataModel = orgaoDataModel;
	}

	public Orgao getSelectedOrgao() {
		return selectedOrgao;
	}

	public void setSelectedOrgao(Orgao selectedOrgao) {
		this.selectedOrgao = selectedOrgao;
	}

	public GrupoAtendimento getGrupoAtendimento() {
		return grupoAtendimento;
	}

	public void setGrupoAtendimento(GrupoAtendimento grupoAtendimento) {
		this.grupoAtendimento = grupoAtendimento;
		getOrgaoDataModel();
	}

	public Orgao getOrgao() {
		return orgao;
	}

	public void setOrgao(Orgao orgaoModel) {
		this.orgao = orgaoModel;
	}

	public Orgao getOrgaoPesquisa() {
		return orgaoPesquisa;
	}

	public void setOrgaoPesquisa(Orgao orgaoPesquisa) {
		this.orgaoPesquisa = orgaoPesquisa;
	}

	public ArrayList<GrupoAtendimento> getGruposDeAtendimento() {
		return (ArrayList<GrupoAtendimento>) gruposDeAtendimento;
	}

	public void setGruposDeAtendimento(
			List<GrupoAtendimento> gruposDeAtendimento) {
		this.gruposDeAtendimento = gruposDeAtendimento;
	}

	public List<Orgao> getFilteredOrgao() {
		return filteredOrgao;
	}

	public void setFilteredOrgao(List<Orgao> filteredOrgao) {
		this.filteredOrgao = filteredOrgao;
	}

	private static String cdgrupoatendimento;
	private static String numerogrupo;
	private static String cdGrupoAtendimentoModel;
	private static String mcucia;
	private static String cdabrev;

	private static int pagAtual;
	private static int linPagina;
	private static long rowAtual;

	private static Boolean disabledGrp;

	private static SelectItem[] opcaomarcada;

	private List<SelectItem> listacds;
	private List<SelectItem> listagrps;
	private static ArrayList<Orgao> selectorgaos = new ArrayList<Orgao>();
	private ArrayList<GrupoAtendimento> listagrupo = new ArrayList<GrupoAtendimento>();

	public static List<GrupoAtendimento> numerogrupos;

	private SelectOneMenu selCD;
	private SelectOneMenu selGRP;

	private Orgao[] selectedorgaorows;

	/**
	 * @return the numerogrupos
	 */
	public List<GrupoAtendimento> getNumerogrupos() {
		return numerogrupos;
	}

	/**
	 * @param numerogrupos
	 *            the numerogrupos to set
	 */
	public void setNumerogrupos(List<GrupoAtendimento> pnumerogrupos) {
		System.out.println("Selecionei grupo");
		numerogrupos = pnumerogrupos;
	}

	/**
	 * @return the cdabrev
	 */
	public String getCdabrev() {
		return cdabrev;
	}

	/**
	 * @param cdabrev
	 *            the cdabrev to set
	 */
	public void setCdabrev(String pcdabrev) {
		cdabrev = pcdabrev;
	}

	/**
	 * @return the selectorgaos
	 */
	public ArrayList<Orgao> getSelectorgaos() {

		return selectorgaos;
	}

	/**
	 * @param selectorgaos
	 *            the selectorgaos to set
	 */
	public void setSelectorgaos(ArrayList<Orgao> selectorg) {
		selectorgaos = selectorg;
	}

	/**
	 * @return the selectedOrgaoRows
	 */
	public Orgao[] getSelectedOrgaoRows() {
		return selectedorgaorows;
	}

	/**
	 * @param selectedOrgaoRows
	 *            the selectedOrgaoRows to set
	 */
	public void setSelectedOrgaoRows(Orgao[] selectedorgaorows) {

		for (int i = 0; i < selectedorgaorows.length; i++) {

			if (selectedorgaorows[i].getMarcado()) {
				selectedorgaorows[i].setMarcado(false);
			} else {
				selectedorgaorows[i].setMarcado(true);
			}

		}
		this.selectedorgaorows = selectedorgaorows;
	}

	public void onCheckBoxSelect(SelectEvent event) {
		System.out.println("check box checked");

		DataTable objDataTable = (DataTable) event.getSource();
		System.out.println("Selected checkbox row index: "
				+ objDataTable.getRowIndex());
		// objDataTable.getRowData();

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
	 * Retorna uma coleção de CDs para exibição na combobox.
	 * 
	 * @return
	 */

	public List<SelectItem> getListaCD() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(CD.CDLESTE.getCodigo(), CD.CDLESTE
				.getDescricao()));
		lista.add(new SelectItem(CD.CDOESTE.getCodigo(), CD.CDOESTE
				.getDescricao()));
		System.out.println(lista.get(0));
		return lista;
	}

	/**
	 * Retorna uma coleção de CDs para exibição na combobox.
	 * 
	 * @return
	 */

	public List<SelectItem> getListaCDs() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(CD.CDLESTE.getCodigo(), CD.CDLESTE
				.getDescricao()));
		lista.add(new SelectItem(CD.CDOESTE.getCodigo(), CD.CDOESTE
				.getDescricao()));
		// System.out.println(lista.get(0));
		return lista;
	}

	/**
	 * @return the numerogrupo
	 */
	public String getNumerogrupo() {
		return numerogrupo;
	}

	/**
	 * @param numerogrupo
	 *            the numerogrupo to set
	 */
	public void setNumerogrupo(String numgrupo) {
		numerogrupo = numgrupo;
	}

	/**
	 * @return the cdgrupoatendimento
	 */
	public String getCdgrupoatendimento() {
		return cdgrupoatendimento;
	}

	/**
	 * @param cdgrupoatendimento
	 *            the cdgrupoatendimento to set
	 * @throws SQLException
	 */
	public void setCdgrupoatendimento(String cdgrupo) throws SQLException {
		// System.out.println("Atualiza CD" + cdgrupo);
		// mcucia = cdgrupo;
		if (cdgrupoatendimento != cdgrupo) {
			cdgrupoatendimento = cdgrupo;
			this.setDisabledGrp(false);
			this.getListaCDs();
			this.getListaGrupo(mcucia);
			this.listagrps = this.getListaGRPs();
			FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formOrgaoGrupo:campoGRP");
			selectorgaos.clear();
			selectorgaos = (ArrayList<Orgao>) getOrgaosLista();
			atribuiOrgaoGrupo();
			FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formOrgaoGrupo:orgList");
		}

	}

	/**
	 * @return the cdGrupoAtendimentoModel
	 */
	public static String getCdGrupoAtendimentoModel() {
		return cdGrupoAtendimentoModel;
	}

	/**
	 * @param cdGrupoAtendimentoModel
	 *            the cdGrupoAtendimentoModel to set
	 */
	public static void setCdGrupoAtendimentoModel(String cdGrupoAtendimentoModel) {
		OrgaoGrupoAtendimentoMB.cdGrupoAtendimentoModel = cdGrupoAtendimentoModel;
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
		OrgaoGrupoAtendimentoMB.mcucia = mcucia;
	}

	/**
	 * @return the disabledGrp
	 */
	public Boolean getDisabledGrp() {
		return disabledGrp;
	}

	/**
	 * @param disabledGrp
	 *            the disabledGrp to set
	 */
	public void setDisabledGrp(Boolean p_disabledGrp) {
		disabledGrp = p_disabledGrp;
	}

	/**
	 * @return the selCD
	 */

	public String getSelCD() {
		/*
		 * UIViewRoot viewRoot =
		 * FacesContext.getCurrentInstance().getViewRoot(); selCD =
		 * (SelectOneMenu) viewRoot.findComponent(":formOrgaoGrupo:campoCD");
		 * 
		 * String valor = selCD.getValue().toString();
		 */
		return this.mcucia;
	}

	/**
	 * @param selCD
	 *            the selCD to set
	 */
	public void setSelCD(SelectOneMenu selCD) {
		this.selCD = selCD;
	}

	/**
	 * @return the selGRP
	 */
	public String getSelGRP() {
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		selGRP = (SelectOneMenu) viewRoot
				.findComponent(":formOrgaoGrupo:campoGRP");

		String valor = selGRP.getValue().toString();

		return valor;
	}

	/**
	 * @param selGRP
	 *            the selGRP to set
	 */
	public void setSelGRP(SelectOneMenu selGRP) {
		this.selGRP = selGRP;
	}

	/**
	 * @return the selectedorgaorows
	 */
	public Orgao[] getSelectedorgaorows() {
		return selectedorgaorows;
	}

	/**
	 * @param selectedorgaorows
	 *            the selectedorgaorows to set
	 */
	public void setSelectedorgaorows(Orgao[] selectedorgaorows) {
		this.selectedorgaorows = selectedorgaorows;
	}

	// Atributos secundários

	public void onRowSelect(SelectEvent event) {
		System.out.println("Marquei/Desmarquei ...");

		String txtMsg = "";

		// Orgao orgaoSel = (Orgao) event.getObject();

		// orgaoSel.setCdtransito(this.getSelCD());
		// orgaoSel.setGrptransito(Integer.parseInt(this.getSelGRP()));

		// System.out.println("Selecionado: " + orgaoSel.getMcu() + " - "
		// + orgaoSel.getMarcado());
		/*
		 * ArrayList<GrupoAtendimento> listaTemp;
		 * 
		 * this.getListaGrupo(this.mcucia);
		 * 
		 * listaTemp = this.getListagrupo();
		 * 
		 * System.out.println("Tamanho atual da lista: " + listaTemp.size());
		 * for (int i = 0; i < listaTemp.size(); i++) {
		 * System.out.println(listaTemp.get(i).getNumGrupo() + " //// " +
		 * this.numerogrupo); if
		 * (listaTemp.get(i).getNumGrupo().equals(this.numerogrupo)) { txtMsg =
		 * listaTemp.get(i).getDescricao(); } }
		 */

		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			OrgaoGrupoDAO dao = new OrgaoGrupoDAO(connection, usuario);

			if (selectedOrgao.getMarcado()) {
				selectedOrgao.setMarcado(false);
				selectedOrgao.setGrptransito(grupoAtendimento.getNumGrupo());
				selectedOrgao.setCdtransito(usuario.getMcucia());

				dao.delete(selectedOrgao);

				txtMsg = "O orgão " + selectedOrgao.getMcu() + " - "
						+ selectedOrgao.getNome()
						+ " foi desvinculado do grupo: " + txtMsg;

			} else {
				selectedOrgao.setMarcado(true);
				selectedOrgao.setGrptransito(grupoAtendimento.getNumGrupo());
				selectedOrgao.setCdtransito(usuario.getMcucia());
				dao.insert(selectedOrgao);
				txtMsg = "O orgão " + selectedOrgao.getMcu() + " - "
						+ selectedOrgao.getNome() + " foi vinculado ao grupo: "
						+ txtMsg;

			}

			this.atribuiOrgaoGrupo();

			FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formOrgaoGrupo:orgList");
			System.out.println(txtMsg);
			FacesMessage msg = new FacesMessage("Seleção de Orgão", txtMsg);
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (SQLException e) {
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

	public void onRowUnselect(UnselectEvent event) {
		System.out.println("Desmarquei ...");

		FacesMessage msg = new FacesMessage("Orgao Unselected",
				((Orgao) event.getObject()).getMcu());

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * @return the orgaolist
	 * @throws SQLException
	 */
	public void getOrgaolist() throws SQLException {
		selectorgaos.clear();
		selectorgaos = (ArrayList<Orgao>) this.getOrgaosLista();

	}

	/**
	 * @throws SQLException
	 */
	public void setOrgaolist() throws SQLException {
		if (selectorgaos.isEmpty()) {
			selectorgaos = (ArrayList<Orgao>) this.getOrgaosLista();
		}
	}

	/**
	 * @return the rowAtual
	 */
	public static int getPagAtual() {
		return pagAtual;
	}

	/**
	 * @param rowAtual
	 *            the rowAtual to set
	 */
	public static void setPagAtual(int pagAtual) {
		OrgaoGrupoAtendimentoMB.pagAtual = pagAtual;
	}

	/**
	 * @return the linPagina
	 */
	public static int getLinPagina() {
		return linPagina;
	}

	/**
	 * @param linPagina
	 *            the linPagina to set
	 */
	public static void setLinPagina(int linPagina) {
		OrgaoGrupoAtendimentoMB.linPagina = linPagina;
	}

	/**
	 * @return the rowAtual
	 */
	public static long getRowAtual() {
		return rowAtual;
	}

	/**
	 * @param rowAtual
	 *            the rowAtual to set
	 */
	public static void setRowAtual(long rowAtual) {
		OrgaoGrupoAtendimentoMB.rowAtual = rowAtual;
	}

	// Metodos Acessórios

	public void actionCarregaGRPs(AjaxBehaviorEvent event)
			throws AbortProcessingException, SQLException {

		System.out.println("Listener OK :" + mcucia + " - "
				+ cdgrupoatendimento);
		if ((!cdgrupoatendimento.equals("00049748"))
				&& (!cdgrupoatendimento.equals("00004010"))) {
			this.setDisabledGrp(true);
		} else {
			this.setDisabledGrp(false);
		}
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formOrgaoGrupo:campoGRP");
		this.getAllGrupo();
		this.listagrps = this.getListaGRPs();

	}

	public void actionCarregaORGs(AjaxBehaviorEvent event)
			throws AbortProcessingException, SQLException {

		System.out.println("Listener ORG OK :" + mcucia + " - "
				+ cdgrupoatendimento);
		this.getOrgaolist();

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formOrgaoGrupo:orgList");

	}

	public void actionAtribuiGRPs(AjaxBehaviorEvent event)
			throws AbortProcessingException, SQLException {

		System.out.println("Listener Atribui GRUPO OK :" + mcucia + " - "
				+ this.grupoAtendimento.getNumGrupo());

		// DataTable dt1 = (DataTable) FacesContext.getCurrentInstance()
		// .getViewRoot().findComponent("formOrgaoGrupo:orgList");
		// dt1.reset();

		// atribuiOrgaoGrupo();

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formOrgaoGrupo:orgList");

	}

	public void onClickSalvar() throws SQLException {
		System.out.println("Salvei!");
		ArrayList<Orgao> iLista = new ArrayList<Orgao>();

		Connection connection = dataSource.getConnection();
		OrgaoGrupoDAO dao = new OrgaoGrupoDAO(connection, usuario);

		for (Orgao org : selectorgaos) {
			System.out.println("Atualizando... :" + this.getNumerogrupo()
					+ " para " + org.getMcu() + " e para o CD "
					+ this.getCdgrupoatendimento() + " - " + org.getMarcado());

			if (org.getMarcado()) {
				iLista.add(org);
			}
		}
		System.out.println("Tamanho da lista para atualização: "
				+ iLista.size());
		try {
			dao.updateBatch(iLista);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			FacesMessage msgerr = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro de Banco de Dacos", "Conexão com o Banco Falhou!");
			FacesContext.getCurrentInstance().addMessage(null, msgerr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			FacesMessage msgerr = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro de Instrução no Banco de Dados", "Erro de SQL !");
			FacesContext.getCurrentInstance().addMessage(null, msgerr);
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

	public void actionPaginarORGs(PageEvent event)
			throws AbortProcessingException {
		System.out
				.println("Paginação em:" + event.getPage() + " - " + pagAtual);

	}

	public void getAllGrupo() {
		ArrayList<GrupoAtendimento> grupoList;
		int listSize = this.listagrupo.size();
		Connection connection = null;

		try {
			if ((listSize == 0) && (cdgrupoatendimento != null)) {

				connection = dataSource.getConnection();

				GrupoAtendimentoDAO dao = new GrupoAtendimentoDAO(connection, usuario);
				grupoList = (ArrayList<GrupoAtendimento>) dao.findAll(this
						.getCdgrupoatendimento());

			} else {
				grupoList = this.getListagrupo();
				this.getListagrupo().clear();
			}
			if (grupoList.size() > 0) {
				for (GrupoAtendimento grupo : grupoList) {

					this.getListagrupo().add(grupo);
				}
			}
		} catch (SQLException e) {
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

	/**
	 * @return the listaDeGrupoAtendimentos
	 */
	public ArrayList<GrupoAtendimento> getListagrupo() {

		return listagrupo;

	}

	/**
	 * Retorna uma lista de GRUPOS para exibição na combobox.
	 * 
	 * @return
	 */

	public List<SelectItem> getListaGRPs() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		Iterator<GrupoAtendimento> it = this.gruposDeAtendimento.iterator();
		while (it.hasNext()) {
			GrupoAtendimento item = (GrupoAtendimento) it.next();
			lista.add(new SelectItem(item.getNumGrupo().toString(), item
					.getDescricao().toUpperCase()));
			// System.out.println("Preenchendo select de grupos: "
			// + item.getDescricao());
		}
		return lista;
	}

	public void getListaGrupo(String mcucd) {
		if (mcucd != null) {
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				GrupoAtendimentoDAO dao = new GrupoAtendimentoDAO(connection, usuario);
				gruposDeAtendimento = dao.findAll(mcucia);
			} catch (SQLException e) {
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

	/**
	 * @return the listagrps
	 * @throws SQLException
	 */
	public List<SelectItem> getListagrps() throws SQLException {
		this.getAllGrupo();
		return getListaGRPs();
	}

	/**
	 * @param listagrps
	 *            the listagrps to set
	 */
	public void setListagrps(List<SelectItem> listagrps) {
		this.listagrps = listagrps;
	}

	public List<Orgao> getOrgaosLista() {

		List<Orgao> tLista = new ArrayList<Orgao>();
		System.out.println("Lista de Orgãos !!!!!!!!!!!" + rowAtual);

		Connection connection = null;
		try {
			connection = dataSource.getConnection();

			OrgaoDAO daoOrgao = new OrgaoDAO(connection);
			tLista = daoOrgao.findAll(cdgrupoatendimento);

			daoOrgao = null;
		} catch (SQLException e) {
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
		return tLista;

	}

	public void atribuiOrgaoGrupo() {
		ArrayList<Orgao> oLista = new ArrayList<Orgao>();

		Connection connection = null;
		try {
			connection = dataSource.getConnection();

			OrgaoGrupoDAO dao = new OrgaoGrupoDAO(connection, usuario);

			String MCUs = dao.findMCUsGrupo(mcucia, this.getGrupoAtendimento()
					.getNumGrupo());

			System.out.println("Atribuindo... :" + MCUs);

			for (Orgao org : selectorgaos) {
				org.setMarcado(false);
				if (MCUs.indexOf(org.getMcu()) > -1) {
					org.setGrptransito(Integer.parseInt(this.getNumerogrupo()));
					org.setCdtransito(cdgrupoatendimento);
					org.setMarcado(true);
				}
				oLista.add(org);
			}
			selectorgaos.clear();
			selectorgaos = oLista;
		} catch (SQLException e) {
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

	public SelectItem[] getOpcaomarcada() {
		return opcaomarcada;
	}

	private SelectItem[] createFilterOptions() {
		SelectItem[] options = new SelectItem[3];

		options[0] = new SelectItem("", "Todas");
		options[1] = new SelectItem("true", "Sim");
		options[2] = new SelectItem("false", "Não");

		return options;
	}

}
