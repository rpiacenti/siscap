package br.com.correios.siscap.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.correios.ppjsiscap.enums.CD;
import br.com.correios.ppjsiscap.excecao.CorreiosException;
import br.com.correios.ppjsiscap.paginador.Paginador;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.constantes.MensagemID;
import br.com.correios.siscap.dao.CronogramaDAO;
import br.com.correios.siscap.dao.GrupoAtendimentoDAO;
import br.com.correios.siscap.excecao.UtilCorreiosException;
import br.com.correios.siscap.model.Cronograma;
import br.com.correios.siscap.model.CronogramaAtendimento;
import br.com.correios.siscap.model.GrupoAtendimento;

/**
 * @author Ronald Piacenti Junior
 * 
 */

@SuppressWarnings("serial")
@Named
@ViewScoped
public class CronogramaMB extends MB {

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
	private static List<Cronograma> cronogramalist = new ArrayList<Cronograma>();
	private List<SelectItem> listaAnos = new ArrayList<SelectItem>();

	
	private static String cdgrupoatendimento;
	private static String datainiciosolicitacao;
	private static String datafimsolicitacao;
	private static String dataprocessamento;
	private static String numerogrupo;
	private static String cdGrupoAtendimentoModel;
	private static String ano;
	private String id;
	private static String mcucia;
	private static String cdabrev;

	private static Boolean disabledGrp;
	private static Boolean disabledAno;

	private List<SelectItem> listacds;
	private List<SelectItem> listagrps;

	private GrupoAtendimento GrupoAtendimentoModel = new GrupoAtendimento();
	private Paginador<GrupoAtendimento> paginador = new Paginador<GrupoAtendimento>();
	private List<GrupoAtendimento> listagrupo = new ArrayList<GrupoAtendimento>();

	private SelectOneMenu selCD;
	private SelectOneMenu selGRP;
	private SelectOneMenu selAno;
	
	private final static String[] meses;
	static {
		meses = new String[12];
		meses[0] = "Janeiro";
		meses[1] = "Fevereiro";
		meses[2] = "Março";
		meses[3] = "Abril";
		meses[4] = "Maio";
		meses[5] = "Junho";
		meses[6] = "Julho";
		meses[7] = "Agosto";
		meses[8] = "Setembro";
		meses[9] = "Outubro";
		meses[10] = "Novembro";
		meses[11] = "Dezembro";
	}
	
	@PostConstruct
	public void init() throws SQLException {
		// User is available here for the case you'd like to work with it
		// directly after bean's construction.
		mcucia = usuario.getMcucia();

		getListaGrupo(mcucia);
		if (disabledGrp == null) {
			// this.setDisabledGrp(true);
			// this.setDisabledAno(true);
			this.setNumerogrupo("0");
		}

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

	/**
	 * @param grupoAtendimentoDataModel
	 *            the grupoAtendimentoDataModel to set
	 */
	public void setGrupoAtendimentoDataModel(
			LazyDataModel<GrupoAtendimento> grupoAtendimentoDataModel) {
		this.grupoAtendimentoDataModel = grupoAtendimentoDataModel;
	}

	public GrupoAtendimento getGrupoAtendimento() {
		return grupoAtendimento;
	}

	public void setGrupoAtendimento(GrupoAtendimento grupoAtendimento) {
		this.grupoAtendimento = grupoAtendimento;
	}

	public List<GrupoAtendimento> getGruposDeAtendimento() {
		return gruposDeAtendimento;
	}

	public void setGruposDeAtendimento(
			List<GrupoAtendimento> gruposDeAtendimento) {
		this.gruposDeAtendimento = gruposDeAtendimento;
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

	public void editaCrono(CellEditEvent event) throws CorreiosException {

		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		int alteredRow = event.getRowIndex();
		String column_name;
		column_name = event.getColumn().getHeaderText();
		boolean editar = false;

		if (newValue != null && !newValue.equals(oldValue)) {
			Cronograma crono = this.cronogramalist.get(event.getRowIndex());
			if (column_name.indexOf("Data de Início da Validação") > -1) {
				crono.setCro_dt_ini_solicitacao((String) newValue);
				crono.setCro_dt_fim_solicitacao((String) newValue);
			}
			if (column_name.indexOf("Data do Fim da Validação") > -1) {

				crono.setCro_dt_processamento((String) newValue);
			}

			if (checaDias(crono) && editar) {
				Connection connection = null;
				try {
					connection = dataSource.getConnection();
					CronogramaDAO dao = new CronogramaDAO(connection, usuario);
					dao.update(crono);
					connection.commit();

					adicionaMensagemSucesso(null,
							MensagemID.CRONOGRAMA_ALTERADO, "");
				} catch (SQLException e) {
					throw utilCorreiosException.erro(e);

				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (connection != null) {
						try {
							connection.close();
						} catch (SQLException e) {
							// sem tratamento por aqui.
						}
					}
				}
			}
		}

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
	 * @return the cronogramalist
	 */
	public List<Cronograma> getCronogramalist() {
		return cronogramalist;
	}

	/**
	 * @param cronogramalist
	 *            the cronogramalist to set
	 */
	public void setCronogramalist(List<Cronograma> cronolist) {
		cronogramalist = cronolist;
	}

	public void SetListaCronograma() {
		// Date today = new Date();
		// SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
		// String data = DATE_FORMAT.format(today);
		Connection connection = null;
		cronogramalist.clear();
		try {
			connection = dataSource.getConnection();

			CronogramaDAO dao = new CronogramaDAO(connection, usuario);
			for (int i = 0; i < 12; i++) {
				Cronograma crono = new Cronograma();
				crono.setGat_co_cia(mcucia);
				crono.setGat_nu(grupoAtendimento.getNumGrupo());
				crono.setCro_an(getAno());
				crono.setCro_me(meses[i]);

				List<Cronograma> cronoDAO;

				cronoDAO = dao.findByKeys(crono.getGat_co_cia(),
						crono.getGat_nu(), crono.getCro_an(),
						Integer.toString((i + 1)));

				if (cronoDAO.size() > 0) {
					crono = cronoDAO.get(0);
					crono.setCro_me(meses[i]);

				} else {

					crono.setCro_me(meses[i]);
				}
				cronogramalist.add(crono);

				crono = null;
				cronoDAO = null;
			}

			dao = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// sem tratamento por aqui.
				}
			}
		}

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formCronograma:croList");
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

		if (cdgrupoatendimento != cdgrupo) {

			cdgrupoatendimento = cdgrupo;
			this.setDisabledGrp(false);
			this.setNumerogrupo("0");
			this.setDisabledAno(true);

			this.getListaCDs();
			FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formCronograma:campoGRP");

			FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formCronograma:campoAno");
			this.getListaGrupo(mcucia);
			this.listagrps = this.getListaGRPs();
		}

	}

	/**
	 * @return the listaDeGrupoAtendimentos
	 */
	public List<GrupoAtendimento> getListagrupo() {

		return listagrupo;

	}

	/**
	 * @return the datainiciosolicitacao
	 */
	public String getDatainiciosolicitacao() {
		return datainiciosolicitacao;
	}

	/**
	 * @param datainiciosolicitacao
	 *            the datainiciosolicitacao to set
	 */
	public void setDatainiciosolicitacao(String dataini) {

		datainiciosolicitacao = dataini;
	}

	/**
	 * @return the datafimsolicitacao
	 */
	public String getDatafimsolicitacao() {
		return datafimsolicitacao;
	}

	/**
	 * @param datafimsolicitacao
	 *            the datafimsolicitacao to set
	 */
	public void setDatafimsolicitacao(String datafim) {

		datafimsolicitacao = datafim;
	}

	/**
	 * @return the dataprocessamento
	 */
	public String getDataprocessamento() {
		return dataprocessamento;
	}

	/**
	 * @param dataprocessamento
	 *            the dataprocessamento to set
	 */
	public void setDataprocessamento(String dataproc) {
		dataprocessamento = dataproc;
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
		ano = "0000";
	}

	/**
	 * @return the cdGrupoAtendimentoModel
	 */
	public String getCdGrupoAtendimentoModel() {
		return cdGrupoAtendimentoModel;
	}

	/**
	 * @param cdGrupoAtendimentoModel
	 *            the cdGrupoAtendimentoModel to set
	 */
	public void setCdGrupoAtendimentoModel(String cdGrupoModel) {
		cdGrupoAtendimentoModel = cdGrupoModel;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the mes
	 */
	public String[] getMeses() {
		return meses;

	}

	/**
	 * @param mes
	 *            the mes to set
	 */
	public void setMeses(String[] pmeses) {
	}

	/**
	 * @return the GrupoAtendimento
	 */
	public GrupoAtendimento getGrupoAtendimentoModel() {
		return GrupoAtendimentoModel;
	}

	/**
	 * @param GrupoAtendimento
	 *            the GrupoAtendimento to set
	 */
	public void setGrupoAtendimentoModel(GrupoAtendimento GrupoAtendimentoModel) {
		this.GrupoAtendimentoModel = GrupoAtendimentoModel;
	}

	/**
	 * @return the paginador
	 */
	public Paginador<GrupoAtendimento> getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            the paginador to set
	 */
	public void setPaginador(Paginador<GrupoAtendimento> paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return the listacds
	 */
	public List<SelectItem> getListacds() {
		return getListaCDs();
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

	public List<SelectItem> getListaCDs() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(CD.CDLESTE.getCodigo(), CD.CDLESTE
				.getDescricao()));
		lista.add(new SelectItem(CD.CDOESTE.getCodigo(), CD.CDOESTE
				.getDescricao()));
		return lista;
	}

	/**
	 * @return the listagrps
	 * @throws SQLException
	 */
	public List<SelectItem> getListagrps() throws SQLException {
		this.getListaGrupo(this.getCdgrupoatendimento());
		// this.getAllGrupo();
		return getListaGRPs();
	}

	/**
	 * @param listagrps
	 *            the listagrps to set
	 */
	public void setListagrps(List<SelectItem> listagrps) {
		this.listagrps = listagrps;
	}

	/**
	 * Retorna uma lista de GRUPOS para exibição na combobox.
	 * 
	 * @return
	 */

	public List<SelectItem> getListaGRPs() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		Iterator<GrupoAtendimento> it = this.listagrupo.iterator();
		while (it.hasNext()) {
			GrupoAtendimento item = (GrupoAtendimento) it.next();
			lista.add(new SelectItem(item.getNumGrupo().toString(), item
					.getDescricao().toUpperCase()));
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
						// sem tratamento por aqui.
					}
				}
			}

		}

	}

	public void getAllGrupo() {
		List<GrupoAtendimento> grupoList;
		int listSize = this.listagrupo.size();
		Connection connection = null;
		try {
			if ((listSize == 0)) {

				connection = dataSource.getConnection();

				GrupoAtendimentoDAO dao = new GrupoAtendimentoDAO(connection, usuario);
				grupoList = (ArrayList<GrupoAtendimento>) dao.queryAll();

			} else {
				grupoList = this.getListagrupo();
				this.getListagrupo().clear();
			}
			for (GrupoAtendimento grupo : grupoList) {

				this.getListagrupo().add(grupo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// sem tratamento por aqui.
				}
			}
		}

	}

	public String getSelCD() {
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		// selCD = (SelectOneMenu) viewRoot
		// .findComponent(":formCronograma:campoCD");
		// String valor = selCD.getValue().toString();

		String valor = this.mcucia;

		return valor;
	}

	public String getSelGRP() {
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		selGRP = (SelectOneMenu) viewRoot
				.findComponent(":formCronograma:campoGRP");
		String valor = selGRP.getValue().toString();

		return valor;
	}

	public String getSelAno() {
		String valor = selAno.getValue().toString();
		return valor;
	}

	/**
	 * @return the ano
	 */
	public String getAno() {

		return ano;
	}

	/**
	 * @param ano
	 *            the ano to set
	 * @throws SQLException
	 */
	public void setAno(String pano) throws SQLException {
		ano = pano;
		if (ano != null || ano != "") {
			this.SetListaCronograma();
		}

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formCronograma:grpList");

	}

	public List<SelectItem> getlistaanos() {
		List<SelectItem> lAno = new ArrayList<SelectItem>();
		for (int i = 2014; i < 2051; i++) {
			lAno.add(new SelectItem(Integer.toString(i), Integer.toString(i)));
		}
		return lAno;
	}

	public void onEdit(RowEditEvent event) throws SQLException {

		Cronograma model = (Cronograma) event.getObject();
		model.setCro_dt_ini_solicitacao(model.getCro_dt_fim_solicitacao()); // adicionado
																			// porque
	model.setCro_dt_fim_solicitacao(model.getCro_dt_fim_solicitacao());
		model.setCro_dt_processamento(model.getCro_dt_processamento());

		Connection connection = null;
		if (checaDias(model)) {
			connection = dataSource.getConnection();
			CronogramaDAO dao = new CronogramaDAO(connection, usuario);

			try {
				dao.onEdit(model);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Alteração de Cronograma", "O cronograma foi alterado.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				this.SetListaCronograma();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				FacesMessage msgerr = new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Erro de Banco de Dacos",
						"Conexão com o Banco Falhou!");
				FacesContext.getCurrentInstance().addMessage(null, msgerr);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				FacesMessage msgerr = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Erro de Instrução no Banco de Dados", "Erro de SQL !");
				FacesContext.getCurrentInstance().addMessage(null, msgerr);
			}finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (SQLException e) {
						// sem tratamento por aqui.
					}
				}
			}
		} else {
			model = null;
			this.SetListaCronograma();
			FacesMessage msg = new FacesMessage(
					FacesMessage.SEVERITY_INFO,
					"Erro nas datas",
					"A data ou o formato da data (DD/MM/YYYY) invalido. As datas podem estar fora de ordem cronológica. Corrija por favor !!!!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		}
	}

	public void onCancel(RowEditEvent event) throws SQLException {
		Connection connection = null;

		Cronograma model = (Cronograma) event.getObject();
		model.setGat_co_cia(usuario.getMcucia());
		GrupoAtendimento model2 = new GrupoAtendimento();

		try {
			connection = dataSource.getConnection();
			CronogramaDAO dao = new CronogramaDAO(connection, usuario);

			dao.delete(model);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exclusão de dado de cronograma", "O mes "
							+ model.getCro_me() + " do ano "
							+ model.getCro_an() + " do grupo "
							+ model.getGat_nu() + ", foi excluído.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			this.getListaGrupo(model2.getNumcd());

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
		}finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// sem tratamento por aqui.
				}
			}
		}

		this.SetListaCronograma();
	}
	
	// MÉTODOS DE ACESSÓRIOS

	public boolean checaDias(Cronograma pModel) {


		String d1 = pModel.getCro_dt_ini_solicitacao();
		String d2 = pModel.getCro_dt_fim_solicitacao();
		String d3 = pModel.getCro_dt_processamento();
		// int mes = pModel.getNumMes(pModel.getCro_me());

		if (d1 != null && d2 != null && d3 != null) {

			if (d1.indexOf("/") < 0) {
				return false;
			}
			if (d2.indexOf("/") < 0) {
				return false;
			}
			if (d3.indexOf("/") < 0) {
				return false;
			}

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);

			try {
				sdf.parse(d1);
				sdf.parse(d2);
				sdf.parse(d3);

				Calendar tCalendar1 = new GregorianCalendar(Integer.parseInt(d1
						.substring(7, 10)),
						Integer.parseInt(d1.substring(3, 5)),
						Integer.parseInt(d1.substring(0, 2)));
				Calendar tCalendar2 = new GregorianCalendar(Integer.parseInt(d2
						.substring(7, 10)),
						Integer.parseInt(d2.substring(3, 5)),
						Integer.parseInt(d2.substring(0, 2)));
				Calendar tCalendar3 = new GregorianCalendar(Integer.parseInt(d3
						.substring(7, 10)),
						Integer.parseInt(d3.substring(3, 5)),
						Integer.parseInt(d3.substring(0, 2)));

				if (tCalendar1.after(tCalendar2)
						|| tCalendar1.after(tCalendar3)) {

					return false;
				}

				if (tCalendar2.after(tCalendar3)) {

					return false;
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		}
		return true;

	}

	public void actionCarregaGRPs(AjaxBehaviorEvent event)
			throws AbortProcessingException, SQLException {

		if ((!cdgrupoatendimento.equals("00049748"))
				&& (!cdgrupoatendimento.equals("00004010"))) {
			this.setDisabledGrp(true);
		} else {
			this.setDisabledGrp(false);
		}
		cronogramalist.clear();
		this.setNumerogrupo("0");
		this.setDisabledAno(true);
		// this.SetListaCronograma();
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formCronograma:croList");

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formCronograma:campoGRP");
		this.getListaGrupo(this.getCdgrupoatendimento());
		this.listagrps = this.getListaGRPs();

	}

	public void actionCarregaAno(AjaxBehaviorEvent event)
			throws AbortProcessingException, SQLException {

		this.SetListaCronograma();

		if ((grupoAtendimento.getNumGrupo() == 0)) {

			this.setDisabledAno(true);

		} else {
			// System.out.println("Habilitou");
			this.listaAnos.clear();
			this.setListaAnos(getlistaanos());
			this.setDisabledAno(false);

		}
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formCronograma:campoAno");
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
	 * @return the disabledAno
	 */
	public Boolean getDisabledAno() {
		return disabledAno;
	}

	/**
	 * @param disabledAno
	 *            the disabledAno to set
	 */
	public void setDisabledAno(Boolean p_disabledAno) {
		disabledAno = p_disabledAno;
	}

	public List<SelectItem> getListaAnos() {
		return listaAnos;
	}

	public void setListaAnos(List<SelectItem> listaAnos) {
		this.listaAnos = listaAnos;
	}

}
