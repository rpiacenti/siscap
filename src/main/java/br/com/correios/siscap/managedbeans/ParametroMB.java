package br.com.correios.siscap.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.SelectEvent;

import br.com.correios.ppjsiscap.enums.CD;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.dao.DRDAO;
import br.com.correios.siscap.dao.GrupoAtendimentoDAO;
import br.com.correios.siscap.dao.ParametroDAO;
import br.com.correios.siscap.model.Dr;
import br.com.correios.siscap.model.GrupoAtendimento;
import br.com.correios.siscap.model.Parametro;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ParametroMB extends MB {
	
	@Inject
	private Usuario usuario;

	@Inject
	@OracleDb
	private DataSource dataSource;

	private String id;

	private static String cd;
	private static Date dt_vigencia_inicial;
	private static Date dt_vigencia_final;
	private static String parametro;
	private static String natureza_parametro;
	private static String dr;
	private static String numerogrupo;
	private static String mcu;
	private static String tipoorgao;
	private static Long valorparametro;

	private static Boolean disabledgrp;
	private static Boolean disableddr;
	private static Boolean disabledtipo;
	private static Boolean disabledmcu;

	private List<SelectItem> listacds;
	private List<SelectItem> listagrps;

	private GrupoAtendimento GrupoAtendimentoModel = new GrupoAtendimento();
	private List<GrupoAtendimento> listagrupo = new ArrayList<GrupoAtendimento>();
	private List<Dr> listadrs = new ArrayList<Dr>();

	private List<String> listapar = new ArrayList<String>();

	private Parametro selectedpar;

	private String mcucia;

	@PostConstruct
	public void init() {
		// User is available here for the case you'd like to work with it
		// directly after bean's construction.
		if (this.getListaTPs().size() == 0) {
			this.getListaTPs();

		}

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
	 * @return the cd
	 */
	public String getCd() {
		return cd;
	}

	/**
	 * @param cd
	 *            the cd to set
	 */
	public void setCd(String pcd) {
		System.out.println("PAssei no data cd" + pcd);
		cd = pcd;
		mcucia = pcd;

	}

	/**
	 * @return the dt_vigencia_inicial
	 */
	public Date getDt_vigencia_inicial() {
		return dt_vigencia_inicial;
	}

	/**
	 * @param dt_vigencia_inicial
	 *            the dt_vigencia_inicial to set
	 */
	public void setDt_vigencia_inicial(Date dt_vig_inicial) {
		System.out.println("PAssei no data inicial");
		dt_vigencia_inicial = dt_vig_inicial;
	}

	/**
	 * @return the dt_vigencia_final
	 */
	public Date getDt_vigencia_final() {
		return dt_vigencia_final;
	}

	/**
	 * @param dt_vigencia_final
	 *            the dt_vigencia_final to set
	 */
	public void setDt_vigencia_final(Date dt_vig_final) {
		System.out.println("PAssei no data Final");
		dt_vigencia_final = dt_vig_final;
	}

	/**
	 * @return the parametro
	 */
	public String getParametro() {
		return parametro;
	}

	/**
	 * @param parametro
	 *            the parametro to set
	 */
	public void setParametro(String par) {
		System.out.println("PAssei no  parametro" + par);
		parametro = par;
	}

	/**
	 * @return the dr
	 */
	public String getDr() {
		return dr;
	}

	/**
	 * @param dr
	 *            the dr to set
	 */
	public void setDr(String pdr) {
		System.out.println("ESCOLHEU dr" + pdr);
		dr = pdr;

	}

	/**
	 * @return the gat
	 */
	public String getNumerogrupo() {
		return numerogrupo;
	}

	/**
	 * @param gat
	 *            the gat to set
	 */
	public void setNumerogrupo(String numgrupo) {
		System.out.println("Ecolheu grupo ...");
		numerogrupo = numgrupo;

	}

	/**
	 * @return the mcu
	 */
	public String getMcu() {
		return mcu;
	}

	/**
	 * @param mcu
	 *            the mcu to set
	 */
	public void setMcu(String pmcu) {
		System.out.println("Ecolheu mcu ...");
		mcu = pmcu;

	}

	/**
	 * @return the tipoorgao
	 */
	public String getTipoorgao() {
		return tipoorgao;
	}

	/**
	 * @param tipoorgao
	 *            the tipoorgao to set
	 */
	public void setTipoorgao(String tiporgao) {
		System.out.println("Ecolheu tipo ...");
		tipoorgao = tiporgao;

	}

	/**
	 * @return the valorparametro
	 */
	public Long getValorparametro() {
		return valorparametro;
	}

	/**
	 * @param valorparametro
	 *            the valorparametro to set
	 */
	public void setValorparametro(Long valorpar) {
		System.out.println("PAssei no valor");
		valorparametro = valorpar;
	}

	/**
	 * @return the natureza_parametro
	 */
	public String getNatureza_parametro() {
		return natureza_parametro;
	}

	/**
	 * @param natureza_parametro
	 *            the natureza_parametro to set
	 */
	public void setNatureza_parametro(String natureza) {
		System.out.println("Ecolheu natureza ...");
		natureza_parametro = natureza;
	}

	/**
	 * @return the listacds
	 */
	public List<SelectItem> getListacds() {
		System.out.println("Passei aqui");
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
	 * @return the listagrupo
	 */
	public List<GrupoAtendimento> getListagrupo() {
		return listagrupo;
	}

	/**
	 * @param listagrupo
	 *            the listagrupo to set
	 */
	public void setListagrupo(List<GrupoAtendimento> listagrupo) {
		this.listagrupo = listagrupo;
	}

	/**
	 * @return the listagrps
	 * @throws SQLException
	 */
	public List<SelectItem> getListagrps() throws SQLException {
		this.getListaGrupo(this.getCd());
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
	 * @return the listapar
	 */
	public List<String> getListapar() {
		return listapar;
	}

	/**
	 * @param listapar
	 *            the listapar to set
	 */
	public void setListapar(ArrayList<Parametro> listapar) {
		System.out.println("Passei Aqui !! Lista Parametro");

		this.setListaPar();

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoPAR");
	}

	/**
	 * @return the selecteditem
	 */
	public Parametro getSelectedpar() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Item não encontrado!",
				"O item:  não foi encontrado, tente digitar novo código.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return selectedpar;
	}

	/**
	 * @param selecteditem
	 *            the selecteditem to set
	 */
	public void setSelectedpar(Parametro selectedpar) {
		System.out.println("Passe on selectyedPAr");
		// this.setDr(selectedpar.getCcco_dr());
		// this.setNumerogrupo(selectedpar.getGat_nu());
		// this.setDt_vigencia_inicial(selectedpar.getPar_dt_vigencia_inicial());
		// this.setDt_vigencia_final(selectedpar.getPar_dt_vigencia_final());
		// this.setNatureza_parametro(selectedpar.getPar_in_natureza());
		this.setParametro(selectedpar.getPar_no());
		// this.setTipoorgao(selectedpar.getDrky_tipo_orgao());

		this.selectedpar = selectedpar;
	}

	// Metodos Acessórios //

	public void getListaGrupo(String mcucd) {
		List<GrupoAtendimento> grupoList = null;
		Connection connection = null;
		try {
			if (mcucd != null) {
				UtilLog.logger.debug("Atualizando lista para o CD :" + mcucd
						+ " Tamanho da lista:" + this.listagrupo.size());
				int listSize = this.listagrupo.size();

				if ((listSize == 0)) {

					connection = dataSource.getConnection();

					GrupoAtendimentoDAO dao = new GrupoAtendimentoDAO(
							connection, usuario);
					grupoList = (ArrayList<GrupoAtendimento>) dao
							.findAll(mcucd);
					mcucia = mcucd;
				} else {
					if (this.listagrupo.get(0).getNumcd().indexOf(mcucd) == -1) {
						connection = dataSource.getConnection();
						GrupoAtendimentoDAO dao = new GrupoAtendimentoDAO(
								connection, usuario);
						grupoList = (ArrayList<GrupoAtendimento>) dao
								.findAll(mcucd);
						this.getListagrupo().clear();
						mcucia = mcucd;
					} else {
						grupoList = this.getListagrupo();
						this.getListagrupo().clear();
					}

				}
				if (this.listagrupo.size() > 0) {
					this.listagrps.clear();
				}

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
					e.printStackTrace();
				}
			}
		}
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
			GrupoAtendimento item = it.next();
			lista.add(new SelectItem(item.getNumGrupo().toString(), item
					.getDescricao().toUpperCase()));
		}
		return lista;
	}

	/**
	 * Retorna uma lista de DRs para exibição na combobox.
	 * 
	 * @return
	 */
	public List<SelectItem> getListaDRs() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		Iterator<Dr> it = this.listadrs.iterator();
		while (it.hasNext()) {
			Dr item = it.next();
			lista.add(new SelectItem(item.getCCCO(), item.getCCNAME()
					.toUpperCase()));
		}
		return lista;
	}

	/**
	 * Retorna uma lista de Tipos de Orgaos para exibição na combobox.
	 * 
	 * @return
	 */
	public List<SelectItem> getListaTPs() {
		List<SelectItem> lista = new ArrayList<SelectItem>();

		lista.add(new SelectItem("ADM", "ORGÃO ADMINISTRATIVO"));
		lista.add(new SelectItem("OPE", "ORGÃO OPERACIONAL"));
		lista.add(new SelectItem("RED", "REDE ATENDIMENTO"));
		lista.add(new SelectItem("TER", "TEREIROS"));
		lista.add(new SelectItem("ALL", "TODOS"));

		return lista;
	}

	/**
	 * Retorna uma lista de Parametros para exibição na combobox.
	 * 
	 * @return
	 * @throws SQLException
	 */

	public void actionCarregaGRP_DR_PAR(AjaxBehaviorEvent event)
			throws AbortProcessingException, SQLException {

		System.out.println("Listener OK :" + mcucia + " - " + cd);

		if ((!cd.equals("00049748")) && (!cd.equals("00004010"))) {
			this.setDisabledgrp(true);
			this.setDisableddr(true);
		} else {
			this.setDisabledgrp(false);
			this.setDisableddr(false);
		}

		this.setListaPar();

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoPAR");

		this.getListaGrupo(cd);
		// this.listagrps = this.getListaGRPs();
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoGRP");

		this.getListaDRs(cd);
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoDR");

	}

	public void getListaDRs(String mcucd) {
		List<Dr> drList = null;
		Connection connection = null;
		if (mcucd != null) {
			try {
				System.out.println("Atualizando lista para o CD :" + mcucd
						+ " Tamanho da lista:" + this.listagrupo.size());
				int listSize = this.listadrs.size();

				if ((listSize == 0)) {

					connection = dataSource.getConnection();

					DRDAO dao = new DRDAO(null);
					drList = dao.findByKeyNumber(mcucd);
					mcucia = mcucd;
				} else {
					if (this.listadrs.get(0).getMCUCD().indexOf(mcucd) == -1) {
						connection = dataSource.getConnection();
						DRDAO dao = new DRDAO(connection);
						drList = dao.findByKeyNumber(mcucd);
						this.listadrs.clear();
						mcucia = mcucd;
					} else {
						drList = this.listadrs;
						this.listadrs.clear();
					}

				}
				listadrs.clear();
				for (Dr tdr : drList) {

					this.listadrs.add(tdr);
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
	}

	public void setListaPar() {
		listapar.clear();
		if (cd.equals("00049748")) {
			listapar.add("EXTRA PRODUTO CD LESTE");
			listapar.add("EXTRA PRODUTO CD LESTE");
			listapar.add("PROCESSAMENTO EXTRA CD LESTE");
		} else {
			listapar.add("EXTRA MATERIAL CD OESTE");
			listapar.add("EXTRA MATERIAL CD OESTE");
			listapar.add("PROCESSAMENTO EXTRA CD OESTE");
		}
		listapar.add("PERCENTUAL ITEM");

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
	 * @return the disabledgrp
	 */
	public Boolean getDisabledgrp() {
		return disabledgrp;
	}

	/**
	 * @param disabledgrp
	 *            the disabledgrp to set
	 */
	public void setDisabledgrp(Boolean pdisabledgrp) {
		ParametroMB.disabledgrp = pdisabledgrp;
	}

	/**
	 * @return the disableddr
	 */
	public Boolean getDisableddr() {
		return disableddr;
	}

	/**
	 * @param disableddr
	 *            the disableddr to set
	 */
	public void setDisableddr(Boolean pdisableddr) {
		ParametroMB.disableddr = pdisableddr;
	}

	/**
	 * @return the disabledtipo
	 */
	public Boolean getDisabledtipo() {
		return disabledtipo;
	}

	/**
	 * @param disabledtipo
	 *            the disabledtipo to set
	 */
	public void setDisabledtipo(Boolean pdisabledtipo) {
		ParametroMB.disabledtipo = pdisabledtipo;
	}

	/**
	 * @return the disabledmcu
	 */
	public Boolean getDisabledmcu() {
		return disabledmcu;
	}

	/**
	 * @param disabledmcu
	 *            the disabledmcu to set
	 */
	public void setDisabledmcu(Boolean pdisabledmcu) {
		ParametroMB.disabledmcu = pdisabledmcu;
	}

	public void onRowSelect(SelectEvent event) {
		System.out.println("Marquei/Desmarquei ...");
		Parametro par = (Parametro) event.getObject();
		System.out.println(par.getPar_no() + " - " + par.getPar_in_natureza()
				+ " - " + par.getGat_nu() + " - " + par.getCcco_dr() + " - "
				+ par.getDrky_tipo_orgao() + " - "
				+ par.getPar_dt_vigencia_inicial() + " - "
				+ par.getPar_dt_vigencia_final() + " - " + par.getPar_vr());
		valorparametro = par.getPar_vr();
		parametro = par.getPar_no();
		numerogrupo = par.getGat_nu();
		dr = par.getCcco_dr();
		tipoorgao = par.getDrky_tipo_orgao();
		dt_vigencia_inicial = par.getPar_dt_vigencia_inicial();
		dt_vigencia_final = par.getPar_dt_vigencia_final();
		natureza_parametro = par.getPar_in_natureza();
		/*
		 * FacesContext.getCurrentInstance().getPartialViewContext()
		 * .getRenderIds().add("formParametro:campoPAR");
		 * FacesContext.getCurrentInstance().getPartialViewContext()
		 * .getRenderIds().add("formParametro:campoNAT");
		 * FacesContext.getCurrentInstance().getPartialViewContext()
		 * .getRenderIds().add("formParametro:campoDtVigInicial");
		 * FacesContext.getCurrentInstance().getPartialViewContext()
		 * .getRenderIds().add("formParametro:campoDtVigFinal");
		 * FacesContext.getCurrentInstance().getPartialViewContext()
		 * .getRenderIds().add("formParametro:campoDR");
		 * FacesContext.getCurrentInstance().getPartialViewContext()
		 * .getRenderIds().add("formParametro:campoGRP");
		 * FacesContext.getCurrentInstance().getPartialViewContext()
		 * .getRenderIds().add("formParametro:campoTIPO");
		 * FacesContext.getCurrentInstance().getPartialViewContext()
		 * .getRenderIds().add("formParametro:campoValor");
		 */
		resetForm();
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro");

	}

	public void onNovo() {
		System.out.println("Passei em NOVO!!!!");

		this.setParametro(null);
		this.setNatureza_parametro(null);
		this.setDr(null);
		this.setDt_vigencia_inicial(null);
		this.setDt_vigencia_final(null);
		this.setNumerogrupo(null);
		this.setTipoorgao(null);
		this.setValorparametro(null);

		// resetForm();

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro");

	}

	public void onEdit() {
		System.out.println("Na edição !!!!!!!!!!!");

		Parametro model = new Parametro();
		System.out.println("Na edição 1:" + this.getCd());
		model.setMcmcu_centro_distribuicao(this.getCd());
		model.setPar_no(this.getParametro());
		System.out.println("Na edição 2:" + this.getParametro());
		model.setPar_dt_vigencia_inicial(this.getDt_vigencia_inicial());
		model.setPar_dt_vigencia_final(this.getDt_vigencia_final());
		System.out.println("Na edição 3:" + this.getDt_vigencia_inicial());
		model.setPar_in_natureza(this.getNatureza_parametro());
		model.setCcco_dr(this.getDr());
		model.setGat_nu(this.getNumerogrupo());
		model.setDrky_tipo_orgao(this.getTipoorgao());
		System.out.println("Na edição 4:" + this.getNatureza_parametro());
		model.setPar_vr(this.getValorparametro());
		System.out.println("Na edição 5:" + this.getValorparametro());
		Connection connection = null;
		try {
			connection = dataSource.getConnection();

			ParametroDAO dao = new ParametroDAO(connection);

			if (model.getPar_nu() == null) {
				model.setPar_nu(dao.getNextVal());
			}
			if (this.getCd() != null) {

				System.out.println("Na edição 6:" + model.getPar_nu());

				dao.onEdit(model);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Alteração de Parametro", "O parametro foi alterado.");
				FacesContext.getCurrentInstance().addMessage(null, msg);

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
		model = null;

	}

	public void onExcluir() throws ClassNotFoundException {
		System.out.println("Na exclusão !!!!!!!!!!!");
		Parametro model = new Parametro();
		Connection connection = null;
		try {
			model.setMcmcu_centro_distribuicao(this.getCd());
			model.setPar_no(this.getParametro());
			connection = dataSource.getConnection();
			ParametroDAO dao = new ParametroDAO(connection);

			System.out.println("Aqui apaga!!!!!!!!!!!!");
			dao.delete(model);

			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exclusão de parametro", "O parametro " + model.getPar_no()
							+ ", foi excluído.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			this.setParametro(null);
			this.setNatureza_parametro(null);
			this.setDr(null);
			this.setDt_vigencia_inicial(null);
			this.setDt_vigencia_final(null);
			this.setNumerogrupo(null);
			this.setTipoorgao(null);
			this.setValorparametro(null);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("ERRO:" + e);
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

		// this.listapar = dao.findByPar();

		

	}

	public void resetForm() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		PartialViewContext partialViewContext = facesContext
				.getPartialViewContext();
		Collection<String> renderIds = partialViewContext.getRenderIds();

		for (String renderId : renderIds) {
			UIComponent component = facesContext.getViewRoot().findComponent(
					renderId);
			EditableValueHolder input = (EditableValueHolder) component;
			input.resetValue();
		}
	}

	public void disableField(AjaxBehaviorEvent event) {
		System.out.println("Desabilitando campos ..." + tipoorgao);
		this.setDisabledtipo(false);
		this.setDisableddr(false);
		this.setDisabledgrp(false);
		this.setDisabledmcu(false);

		/*
		 * 
		 * 
		 * if (!(dr == null)) { this.setDisableddr(false);
		 * this.setDisabledgrp(true); this.setDisabledtipo(true);
		 * this.setDisabledmcu(true); }else if (!(mcu == null)) {
		 * this.setDisableddr(true); this.setDisabledgrp(true);
		 * this.setDisabledtipo(true); this.setDisabledmcu(false); }else
		 * if(!(tipoorgao == null)) { this.setDisableddr(true);
		 * this.setDisabledgrp(true); this.setDisabledtipo(false);
		 * this.setDisabledmcu(true); }else if(!(numerogrupo == null)) {
		 * this.setDisableddr(true); this.setDisabledgrp(false);
		 * this.setDisabledtipo(true); this.setDisabledmcu(true);
		 * 
		 * }else{ this.setDisableddr(false); this.setDisabledgrp(false);
		 * this.setDisabledtipo(false); this.setDisabledmcu(false); }
		 */
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoDR");

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoGRP");

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoTipo");

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoMCU");

	}
}
