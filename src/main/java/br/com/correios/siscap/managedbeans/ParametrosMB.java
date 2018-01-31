package br.com.correios.siscap.managedbeans;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import br.com.correios.ppjsiscap.excecao.CorreiosException;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.dao.DRDAO;
import br.com.correios.siscap.dao.GrupoAtendimentoDAO;
import br.com.correios.siscap.dao.ParametroDAO;
import br.com.correios.siscap.excecao.UtilCorreiosException;
import br.com.correios.siscap.model.Dr;
import br.com.correios.siscap.model.GrupoAtendimento;
import br.com.correios.siscap.model.Parametro;


@SuppressWarnings("serial")
@Named
@ViewScoped
public class ParametrosMB extends MB {
	@Inject
	private Usuario usuario;

	@Inject
	@OracleDb
	private DataSource dataSource;

	@Inject
	private UtilCorreiosException utilCorreiosException;
	
	private  String cd;
	private  Date dt_vigencia_inicial;
	private  Date dt_vigencia_final;
	private String parametro;
	private String natureza_parametro;
	private String dr;
	private String numerogrupo;
	private String mcu;
	private String tipoorgao;
	private Long valorparametro;

	private Boolean disabledgrp;
	private Boolean disableddr;
	private Boolean disabledtipo;
	private Boolean disabledmcu;
	private Boolean disabledlupa;
	private Boolean disabledSalvar = true;
	private Boolean disabledExcluir = true;

	private List<SelectItem> listacds;
	private List<SelectItem> listagrps;
	private List<SelectItem> listadr;
	private List<SelectItem> listapar;

	private Parametro selectedpar;
	private ArrayList<Parametro> parametroList = new ArrayList<Parametro>();

	private String mcucia;

	private List<GrupoAtendimento> listagrupo = new ArrayList<GrupoAtendimento>();
	private List<Dr> listadrs = new ArrayList<Dr>();
		

	@PostConstruct
	public void init() {
		// User is available here for the case you'd like to work with it
		// directly after bean's construction.
		if (this.getListaTPs().size() == 0) {
			this.getListaTPs();
		}

	}

	public String getCd() {
		return cd;
	}

	public void setCd(String pcd) {
		cd = pcd;

		mcucia = pcd;

	}

	public Date getDt_vigencia_inicial() {
		return dt_vigencia_inicial;
	}

	public void setDt_vigencia_inicial(Date pdt_vigencia_inicial) {
		UtilLog.logger.debug("Data inicio OK!" + pdt_vigencia_inicial);
		
		
		dt_vigencia_inicial = pdt_vigencia_inicial;
	}

	public Date getDt_vigencia_final() {
		return dt_vigencia_final;
	}

	public void setDt_vigencia_final(Date pdt_vigencia_final) {
		UtilLog.logger.debug("Data fim OK!" + pdt_vigencia_final);
	
		dt_vigencia_final = pdt_vigencia_final;
		
		if(dt_vigencia_final != null && dt_vigencia_inicial != null ){
			this.setDisabledSalvar(false);
			this.setDisabledExcluir(false);
		}else{
			this.setDisabledSalvar(true);
			this.setDisabledExcluir(true);
		}

		
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String pparametro) {
		parametro = pparametro;
	}

	public String getNatureza_parametro() {
		return natureza_parametro;
	}

	public void setNatureza_parametro(String pnatureza_parametro) {
		UtilLog.logger.debug("Passei em natureza!!!!!");
		natureza_parametro = pnatureza_parametro;
	}

	public String getDr() {
		return dr;
	}

	public void setDr(String pdr) {
		if (!(pdr.equals("")) && !(pdr == null)) {
			UtilLog.logger.debug("Desabilitando campos ...");
			this.setDisabledtipo(true);
			this.setDisableddr(false);
			this.setDisabledgrp(true);
			this.setDisabledmcu(true);
			this.setDisabledSalvar(true);
			this.setDisabledExcluir(true);
			refreshCampos();
		} else {
			this.setDisabledtipo(false);
			this.setDisableddr(false);
			this.setDisabledgrp(false);
			this.setDisabledmcu(false);
			refreshCampos();
		}
		dr = pdr;
	}

	public String getNumerogrupo() {
		return numerogrupo;
	}

	public void setNumerogrupo(String pnumerogrupo) {
		if (!(pnumerogrupo.equals("")) && !(pnumerogrupo == null)) {
			UtilLog.logger.debug("Desabilitando campos ...");
			this.setDisabledtipo(true);
			this.setDisableddr(true);
			this.setDisabledgrp(false);
			this.setDisabledmcu(true);
			refreshCampos();
		} else {
			this.setDisabledtipo(false);
			this.setDisableddr(false);
			this.setDisabledgrp(false);
			this.setDisabledmcu(false);
			refreshCampos();
		}
		numerogrupo = pnumerogrupo;
	}

	public String getMcu() {
		return mcu;
	}

	public void setMcu(String pmcu) {
		if (!(pmcu.equals("")) && !(pmcu == null)) {
			UtilLog.logger.debug("Desabilitando campos ...");
			this.setDisabledtipo(true);
			this.setDisableddr(true);
			this.setDisabledgrp(true);
			this.setDisabledmcu(false);
			refreshCampos();
		} else {
			this.setDisabledtipo(false);
			this.setDisableddr(false);
			this.setDisabledgrp(false);
			this.setDisabledmcu(false);
			refreshCampos();
		}
		mcu = pmcu;
	}

	public String getTipoorgao() {
		return tipoorgao;
	}

	public void setTipoorgao(String ptipoorgao) {
		if (!(ptipoorgao.equals("")) && !(ptipoorgao == null)) {
			UtilLog.logger.debug("Desabilitando campos ...");
			this.setDisabledtipo(false);
			this.setDisableddr(true);
			this.setDisabledgrp(true);
			this.setDisabledmcu(true);
			refreshCampos();
		} else {
			this.setDisabledtipo(false);
			this.setDisableddr(false);
			this.setDisabledgrp(false);
			this.setDisabledmcu(false);
			refreshCampos();
		}
		tipoorgao = ptipoorgao;
	}

	public Long getValorparametro() {
		return valorparametro;
	}

	public void setValorparametro(Long pvalorparametro) {
		valorparametro = pvalorparametro;
	}
	
	public Boolean getDisabledlupa() {
		return disabledlupa;
	}

	public void setDisabledlupa(Boolean disabledlupa) {
		this.disabledlupa = disabledlupa;
	}

	public Boolean getDisabledgrp() {
		return disabledgrp;
	}

	public void setDisabledgrp(Boolean pdisabledgrp) {
		disabledgrp = pdisabledgrp;
	}

	public Boolean getDisableddr() {
		return disableddr;
	}

	public void setDisableddr(Boolean pdisableddr) {
		disableddr = pdisableddr;
	}

	public Boolean getDisabledtipo() {
		return disabledtipo;
	}

	public void setDisabledtipo(Boolean pdisabledtipo) {
		disabledtipo = pdisabledtipo;
	}

	public Boolean getDisabledmcu() {
		return disabledmcu;
	}

	public void setDisabledmcu(Boolean pdisabledmcu) {
		disabledmcu = pdisabledmcu;
	}

	public Boolean getDisabledSalvar() {
		return disabledSalvar;
	}

	public void setDisabledSalvar(Boolean disabledSavar) {
		this.disabledSalvar = disabledSavar;
	}

	public Boolean getDisabledExcluir() {
		return disabledExcluir;
	}

	public void setDisabledExcluir(Boolean disabledEcluir) {
		this.disabledExcluir = disabledEcluir;
	}

	public List<SelectItem> getListacds() {
		return listacds;
	}

	public void setListacds(List<SelectItem> plistacds) {
		listacds = plistacds;
	}

	public List<Dr> getListadrs() {
		return listadrs;
	}

	public void setListadrs(List<Dr> plistadr) {
		listadrs = plistadr;
	}

	public List<SelectItem> getListagrps() {
		return listagrps;
	}

	public void setListagrps(List<SelectItem> plistagrps) {
		listagrps = plistagrps;
	}

	public List<SelectItem> getListapar() {
		return listapar;
	}

	public void setListapar(List<SelectItem> plistapar) {
		listapar = plistapar;
	}

	public Parametro getSelectedpar() {
		return selectedpar;
	}

	public void setSelectedpar(Parametro selectedpar) {
		this.selectedpar = selectedpar;
	}

	public String getMcucia() {
		return mcucia;
	}

	public void setMcucia(String mcucia) {
		this.mcucia = mcucia;
	}

	public List<GrupoAtendimento> getListagrupo() {
		return listagrupo;
	}

	public void setListagrupo(List<GrupoAtendimento> listagrupo) {
		this.listagrupo = listagrupo;
	}

	public ArrayList<Parametro> getParametroList() {
		//getListaParametro(cd);
		return parametroList;
	}

	public void setParametroList(ArrayList<Parametro> pparametroList) {

		parametroList = pparametroList;

	}

	public List<SelectItem> getListaCDs() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(CD.CDLESTE.getCodigo(), CD.CDLESTE
				.getDescricao()));
		lista.add(new SelectItem(CD.CDOESTE.getCodigo(), CD.CDOESTE
				.getDescricao()));
		// UtilLog.logger.debug(lista.get(0));
		return lista;
	}

	public List<SelectItem> getListaDRs() throws CorreiosException  {
		// TODO Auto-generated method stub
		List<Dr> drList;
		List<SelectItem> lista = new ArrayList<SelectItem>();
		if (cd != null) {
			// UtilLog.logger.debug("Atualizando lista para o CD :" + mcucd
			// + " Tamanho da lista:" + this.listadrs.size());'	
			int listSize = this.listadrs.size();
			mcucia = cd;
			Connection connection = null;
			try{
				connection = dataSource.getConnection();
				if ((listSize == 0)) {
					DRDAO dao = new DRDAO(connection);
					drList = dao.findByKeyNumber(cd);
				} else {
					if (listadrs.get(0).getMCUCD().indexOf(cd) == -1) {
						// UtilLog.logger.debug("Passo 2");
						DRDAO dao = new DRDAO(connection);
						drList = dao.findByKeyNumber(cd);
						this.listadrs.clear();
						
					} else {
						// UtilLog.logger.debug("Passo 3");
						drList = listadrs;
						listadrs.clear();
					}
					
				}
				if (listadr != null) {
					listadr.clear();
				}
				
				for (Dr tdr : drList) {
					// UtilLog.logger.debug("Passo 4" + drList.size());
					listadrs.add(tdr);
					SelectItem item = new SelectItem();
					item.setLabel(tdr.getCCNAME());
					item.setValue(tdr.getCCCO());
					lista.add(item);
					item = null;
				}
				
			} catch (SQLException e) {
				throw utilCorreiosException.erro(e);
			}finally {
				if(connection != null){
					try {
						connection.close();
					} catch (SQLException e) {
						// sem tratamentos por aqui.
					}
				}
				
			}
		}
		return lista;
	}

	public List<SelectItem> setListaPar() {

		List<SelectItem> lista = new ArrayList<SelectItem>();

		String[] listaOptLeste = { "EXTRA PROD CD LESTE", "EXTRA MAT CD LESTE",
				"PROC EXTRA CD LESTE", "CIA NORMAL CD LESTE", "CIA EXTRA CD LESTE" };
		String[] listaOptOeste = { "EXTRA PROD CD OESTE", "EXTRA MAT CD OESTE",
				"PROC EXTRA CD OESTE" ,	"CIA NORMAL CD OESTE", "CIA EXTRA CD OESTE"  };
		if (cd.equals("00049748")) {
			for (String opt : listaOptLeste) {
				SelectItem item = new SelectItem();
				item.setLabel(opt);
				item.setValue(opt);
				lista.add(item);
				item = null;
			}
		} else {
			for (String opt : listaOptOeste) {
				SelectItem item = new SelectItem();
				item.setLabel(opt);
				item.setValue(opt);
				lista.add(item);
				item = null;
			}

		}
		SelectItem item = new SelectItem();
		item.setLabel("PERCENTUAL ITEM");
		item.setValue("PERCENTUAL ITEM");
		lista.add(item);
		return lista;

	}

	public List<SelectItem> getListaTPs() {
		List<SelectItem> lista = new ArrayList<SelectItem>();

		lista.add(new SelectItem("ADM", "ORGÃO ADMINISTRATIVO"));
		lista.add(new SelectItem("OPE", "ORGÃO OPERACIONAL"));
		lista.add(new SelectItem("RED", "REDE ATENDIMENTO"));
		lista.add(new SelectItem("TER", "TEREIROS"));
		lista.add(new SelectItem("ALL", "TODOS"));

		return lista;
	}

	public void getListaGrupo() {
		if (cd != null) {
			Connection connection = null;
			try {
				connection = dataSource.getConnection();
				GrupoAtendimentoDAO dao = new GrupoAtendimentoDAO(connection, usuario);
				listagrupo = (ArrayList<GrupoAtendimento>) dao.findAll(cd);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if(connection != null){
					try {
						connection.close();
					} catch (SQLException e) {
						// sem tratamentos por aqui.
					}
				}
				
			}

		}

	}

	public void actionCarregaGRP_DR_PAR(AjaxBehaviorEvent event)
			throws AbortProcessingException, CorreiosException {

		UtilLog.logger.debug("Listener OK :" + mcucia + " - " + cd);
		
		setDisabledSalvar(true);
		setDisabledExcluir(true);

		resetValores();

		if ((!cd.equals("00049748")) && (!cd.equals("00004010"))) {
			this.setDisabledgrp(true);
			this.setDisableddr(true);
		} else {
			this.setDisabledgrp(false);
			this.setDisableddr(false);
		}

		getListaGrupo();
		
		listapar = setListaPar();
		
		listadr = getListaDRs();
		
		getListaParametro(cd);

		refreshCampos();

	}

	

	public void onEdit() throws CorreiosException {
		UtilLog.logger.debug("Na edição !!!!!!!!!!!");

		Parametro parametro = new Parametro();
		UtilLog.logger.debug("Na edição 1:" + getCd());
		parametro.setMcmcu_centro_distribuicao(getCd());
		parametro.setPar_no(this.getParametro());
		UtilLog.logger.debug("Na edição 2:" + getParametro());
		parametro.setPar_dt_vigencia_inicial(getDt_vigencia_inicial());
		parametro.setPar_dt_vigencia_final(getDt_vigencia_final());
		UtilLog.logger.debug("Na edição 3:" + getDt_vigencia_inicial());
		parametro.setPar_in_natureza(getNatureza_parametro());
		parametro.setCcco_dr(getDr());
		parametro.setGat_nu("99");
		UtilLog.logger.debug("Na edição 3,5:" + getNumerogrupo());
		parametro.setDrky_tipo_orgao(getTipoorgao());
		UtilLog.logger.debug("Na edição 4:" + getNatureza_parametro());
		parametro.setPar_vr(this.getValorparametro());
		UtilLog.logger.debug("Na edição 5:" + getValorparametro());

		boolean stData = true;
		// Checa intervalo de datas
		if(parametro.getPar_dt_vigencia_inicial() != null && parametro.getPar_dt_vigencia_inicial() != null){
			if(parametro.getPar_dt_vigencia_inicial().after(parametro.getPar_dt_vigencia_final())) {  
				stData = false;
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Datas do Parametro", "A data incial não pode ser posterior a final ! Corrija as datas e tente novamente.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} 
		}else{
			stData = false;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Datas do Parametro", "As data incial e/ou final devem ser preenchidas ! Corrija as datas e tente novamente.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
		
		
		Connection c = null;
		try{
			c = dataSource.getConnection();
			ParametroDAO dao = new ParametroDAO(c);
			
			if (parametro.getPar_nu() == null) {
				parametro.setPar_nu(dao.getNextVal());
			}
			
			if (this.getCd() != null && stData) {
				
				UtilLog.logger.debug("Na edição 6:" + parametro.getPar_nu());
				try {
					dao.onEdit(parametro);
					
					parametroList.clear();
					
					getListaParametro(cd);
					
					refreshCampos();
					
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Editar Parametro", "O parametro foi incluído/alterado.");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}  catch (SQLException e) {
					UtilLog.logger.debug(e);
					FacesMessage msgerr = new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Erro de Instrução no Banco de Dados", "Erro de SQL !"+e.getMessage());
					FacesContext.getCurrentInstance().addMessage(null, msgerr);
				}
			}
		} catch (SQLException e1) {
			throw utilCorreiosException.erro(e1);
		}finally {
			if(c != null){
				try {
					c.close();
				} catch (SQLException e) {
					//nao percisa fazer nada por aqui.
				}
			}
		}

		parametro = null;

	}

	public void onExcluir() {
		UtilLog.logger.debug("Na exclusão !!!!!!!!!!!");
		Parametro model = new Parametro();

		model.setMcmcu_centro_distribuicao(getCd());
		model.setPar_no(getParametro());
		Connection c = null;
		
		try {
			c = dataSource.getConnection();
			ParametroDAO dao = new ParametroDAO(c);
			UtilLog.logger.debug("Aqui apaga!!!!!!!!!!!!");
			dao.delete(model);
			
			parametroList.clear();
			
			String tempCd = cd;
						
			resetValores();	
			
			setCd(tempCd);
			
			getListaParametro(cd);
			
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Exclusão de parametro", "O parametro " + model.getPar_no()
							+ ", foi excluído.");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			UtilLog.logger.debug("ERRO:" + e);
			FacesMessage msgerr = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro de Instrução no Banco de Dados", "Erro de SQL !"+e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msgerr);
		} catch (CorreiosException ec) {
			// TODO Auto-generated catch block
			ec.printStackTrace();
		}finally{
			if(c != null){
				try {
					c.close();
				} catch (SQLException e) {
				}
			}
		}
		
	}

	public void getListaParametro(String mcucd) throws CorreiosException {
		List<Parametro> parametros;
		if (mcucd != null) {
			UtilLog.logger.debug("Atualizando lista para o CD :" + mcucd
					+ " Tamanho da lista:" + this.listagrupo.size());
			int listSize = parametroList.size();

			Connection connection = null;
			try{
				connection = dataSource.getConnection();
				if ((listSize == 0)) {
					ParametroDAO dao = new ParametroDAO(connection);
					parametros = dao.findByParametro(mcucd);
					mcucia = mcucd;
				} else {
					if (parametroList.get(0).getMcmcu_centro_distribuicao()
							.indexOf(mcucd) == -1) {
						ParametroDAO dao = new ParametroDAO(connection);
						parametros = dao.findByParametro(mcucd);
						parametroList.clear();
						mcucia = mcucd;
					} else {
						parametros = parametroList;
						parametroList.clear();
					}
					
				}
				parametroList.clear();
				for (Parametro tpar : parametros) {
					
					parametroList.add(tpar);
				}
				setDisabledlupa(true);
				FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro");
			} catch (SQLException e) {
				throw utilCorreiosException.erro(e);
			}finally {
				if(connection != null){
					try {
						connection.close();
					} catch (SQLException e) {
					}
				}
			}
		}
	}

	public void onRowSelect(SelectEvent event) {
		UtilLog.logger.debug("Marquei/Desmarquei ...");
		Parametro par = (Parametro) event.getObject();
		
		UtilLog.logger.debug(par.getPar_no() + " - " + par.getPar_in_natureza()
				+ " - " + par.getGat_nu() + " - " + par.getCcco_dr() + " - "
				+ par.getDrky_tipo_orgao() + " - "
				+ par.getPar_dt_vigencia_inicial() + " - "
				+ par.getPar_dt_vigencia_final() + " - " + par.getPar_vr());
		parametro = par.getPar_no();
		valorparametro = par.getPar_vr();
		natureza_parametro = par.getPar_in_natureza();
		
		dr = null;
		if(listadrs != null && par.getCcco_dr() != null){
			for(Dr drs: listadrs){
				if(drs.getCCCO().indexOf(par.getCcco_dr()) > -1){
					dr = par.getCcco_dr();
					setDisabledSalvar(false);
					setDisabledExcluir(false);
					updateValores();
					break;
				}
			}
		}
		
		numerogrupo = null;
		if(listagrupo != null && par.getGat_nu() != null){
			for(GrupoAtendimento grp: listagrupo){
				if(grp.getNumGrupo() == Integer.parseInt(par.getGat_nu())){
					numerogrupo = par.getGat_nu();
					setDisabledSalvar(false);
					setDisabledExcluir(false);
					updateValores();
					break;
				}
			}
		}

		tipoorgao = null;
		if(getListaTPs() != null && par.getDrky_tipo_orgao() != null){
			for(SelectItem grp: getListaTPs()){
				if(grp.getValue() == par.getDrky_tipo_orgao()){
					tipoorgao = par.getDrky_tipo_orgao();
					break;
				}
			}
		}

		dt_vigencia_inicial = par.getPar_dt_vigencia_inicial();
		dt_vigencia_final = par.getPar_dt_vigencia_final();

		updateValores();
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

	public void resetValores() {
		valorparametro = null;
		tipoorgao = null;
		parametro = null;
		numerogrupo = null;
		natureza_parametro = null;
		dt_vigencia_inicial = null;
		dt_vigencia_final = null;
		dr = null;
		disabledlupa= true;
		

		updateValores();
	}
	
	public void updateValores() {
				
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoPAR");

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoGRP");

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoDR");

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoTIPO");

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoNAT");

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoValor");

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoDtVigInicial");

		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formParametro:campoDtVigFinal");
		
		FacesContext.getCurrentInstance().getPartialViewContext()
		.getRenderIds().add("formParametro:btnSalvar");
		
		FacesContext.getCurrentInstance().getPartialViewContext()
		.getRenderIds().add("formParametro:btnExcluir");
	}
	
	public void refreshCampos() {
		updateValores();
		
		FacesContext.getCurrentInstance().getPartialViewContext()
		.getRenderIds().add("formParametro:parList");
	}
	
}
