package br.com.correios.siscap.managedbeans;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.SelectEvent;

import br.com.correios.ppjsiscap.enums.CD;
import br.com.correios.ppjsiscap.enums.NATUREZAORGAO;
import br.com.correios.ppjsiscap.enums.TIPOPEDIDO;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.util.DataERP;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.dao.DRDAO;
import br.com.correios.siscap.excecao.UtilCorreiosException;
import br.com.correios.siscap.model.Dr;

@Named
@ViewScoped
public class RelatorioMB extends MB {

	@Inject
	private Usuario usuario;

	@Inject
	@OracleDb
	private DataSource dataSource;

	private static final long serialVersionUID = 1L;

	private static List<SelectItem> listacds;
	private static List<SelectItem> listadrs;
	private static List<SelectItem> listameses;
	private static List<SelectItem> listaanos;
	private static List<SelectItem> listacategoria;
	private static List<SelectItem> listatipopedido;
	private static List<SelectItem> listanaturezaorgao;

	private SelectOneMenu selCD;
	private SelectOneMenu selDR;

	private String cd;
	private String cd2;
	private String dr;
	private Date dt_inicial;
	private Date dt_final;
	private Date dt_inicial2;
	private Date dt_final2;
	private String mes;
	private String ano;
	private String tipoorgao;
	private String tipopedido;
	private String categoria;
	private String mcu = " ";
	private String matricula = "0";
	private String pedido = "0";
	private String item;
	private String grupo;
	private String descricao;

	private String nomeRelatorio;
	private int numeroRelatorio;

	private boolean linkDisabled;

	private String idrelat;

	public RelatorioMB() {
		
	}

	@PostConstruct
	public void init() {
		if (listacds == null) {
			setListacds(this.getListaCDs());
			setListameses(null);
			setListaanos(null);
			setLinkDisabled(false);
		}
		listadrs = getListaDRs(usuario.getMcucia());
		resetDates();
	}

	public void onDateSelectIni(SelectEvent event) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dDate = null;
		try {
			dDate = format.parse(event.getObject().toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDt_inicial(dDate);
	}

	public void onDateSelectFim(SelectEvent event) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dDate = null;
		try {
			dDate = format.parse(event.getObject().toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDt_final(dDate);
	}

	public void actionRelat() {

	}

	public String getTipoorgao() {
		return tipoorgao;
	}

	public void setTipoorgao(String ptipoorgao) {
		tipoorgao = ptipoorgao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String pdescricao) {
		descricao = pdescricao;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String pgrupo) {
		grupo = pgrupo;
	}

	public int getNumeroRelatorio() {
		return numeroRelatorio;
	}

	public void setNumeroRelatorio(int pnumeroRelatorio) {
		numeroRelatorio = pnumeroRelatorio;
	}

	public String getNomeRelatorio() {
		return nomeRelatorio;
	}

	public void setNomeRelatorio(String nome_relatorio) {
		nomeRelatorio = nome_relatorio;
	}

	public boolean isLinkDisabled() {
		return linkDisabled;
	}

	public void setLinkDisabled(boolean plinkDisabled) {
		linkDisabled = plinkDisabled;
	}

	public Date getDt_inicial() {
		return dt_inicial;
	}

	public void setDt_inicial(Date pdt_inicial) {
		dt_inicial = pdt_inicial;
	}

	public Date getDt_final() {
		return dt_final;
	}

	public void setDt_final(Date pdt_final) {
		dt_final = pdt_final;
		DataERP dt_erp = new DataERP();

		// Formatting Date in Java using SimpleDateFormat
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

		String dt_ini = dt_erp.getDataERP(DATE_FORMAT.format(dt_inicial));
		String dt_fim = dt_erp.getDataERP(DATE_FORMAT.format(dt_final));

		Long dt_l_ini = Long.parseLong(dt_ini);
		Long dt_l_fim = Long.parseLong(dt_fim);
		Long dt_dif = dt_l_fim - dt_l_ini;

		if (dt_dif > 31 || dt_dif < 0) {
			FacesMessage msgerr = new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"ATENÇÃO !",
					"O periodo informado excede 31 dias, ou a data inicial é posterior a data final.");
			FacesContext.getCurrentInstance().addMessage(null, msgerr);
			resetDates();
			FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formRelatorio:campoDtInicial");
			FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formRelatorio:campoDtFinal");
		}
	}

	/**
	 * @return the dt_inicial2
	 */
	public Date getDt_inicial2() {
		return dt_inicial2;
	}

	/**
	 * @param dt_inicial2
	 *            the dt_inicial2 to set
	 */
	public void setDt_inicial2(Date dt_inicial2) {
		this.dt_inicial2 = dt_inicial2;
	}

	/**
	 * @return the dt_final2
	 */
	public Date getDt_final2() {
		return dt_final2;
	}

	/**
	 * @param dt_final2
	 *            the dt_final2 to set
	 */
	public void setDt_final2(Date pdt_final2) {
		dt_final2 = pdt_final2;
		DataERP dt_erp = new DataERP();

		// Formatting Date in Java using SimpleDateFormat
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

		String dt_ini = dt_erp.getDataERP(DATE_FORMAT.format(dt_inicial2));
		String dt_fim = dt_erp.getDataERP(DATE_FORMAT.format(dt_final2));

		Long dt_l_ini = Long.parseLong(dt_ini);
		Long dt_l_fim = Long.parseLong(dt_fim);
		Long dt_dif = dt_l_fim - dt_l_ini;

		if (dt_dif > 31 || dt_dif < 0) {
			FacesMessage msgerr = new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"ATENÇÃO !",
					"O periodo informado excede 31 dias, ou a data inicial é posterior a data final.");
			FacesContext.getCurrentInstance().addMessage(null, msgerr);
			resetDates();
			FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formRelatorio:campoDtInicial");
			FacesContext.getCurrentInstance().getPartialViewContext()
					.getRenderIds().add("formRelatorio:campoDtFinal");
		}
	}

	/**
	 * @return the mcu
	 */
	public String getMcu() {
		// this.mcu = usuario.getMculotacao();
		return mcu;
	}

	/**
	 * @param mcu
	 *            the mcu to set
	 */
	public void setMcu(String pmcu) {
		mcu = pmcu;
	}

	/**
	 * @return the matricula
	 */
	public String getMatricula() {
		return matricula;
	}

	/**
	 * @param matricula the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	
	/**
	 * @return the pedido
	 */
	public String getPedido() {
		return pedido;
	}

	/**
	 * @param pedido the pedido to set
	 */
	public void setPedido(String pedido) {
		this.pedido = pedido;
	}

	/**
	 * @return the item
	 */
	public String getItem() {
		return item;
	}

	/**
	 * @param item
	 *            the item to set
	 */
	public void setItem(String pitem) {
		item = pitem;
	}

	/**
	 * @return the mes
	 */
	public String getMes() {
		return mes;
	}

	/**
	 * @param mes
	 *            the mes to set
	 */
	public void setMes(String pmes) {
		mes = pmes;
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
	 */
	public void setAno(String pano) {
		ano = pano;
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
	 * @return the tipopedido
	 */
	public String getTipopedido() {
		return tipopedido;
	}

	/**
	 * @param tipopedido
	 *            the tipopedido to set
	 */
	public void setTipopedido(String ptipopedido) {
		tipopedido = ptipopedido;
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
		dr = pdr;
	}

	/**
	 * @return the cdgrupoatendimento
	 */
	public String getCd() {
		return cd;
	}

	/**
	 * @param cdgrupoatendimento
	 *            the cdgrupoatendimento to set
	 */
	public void setCd(String pcd) {
		if(pcd != null && pcd != ""){
			cd2 = pcd;
		}
		cd = pcd;
	}

	/**
	 * @return the idrelat
	 */
	public String getIdrelat() {
		return idrelat;
	}

	/**
	 * @param idrelat
	 *            the idrelat to set
	 */
	public void setIdrelat(String idrelat) {
		this.idrelat = idrelat;
	}

	/**
	 * @return the listacds
	 */
	public List<SelectItem> getListacds() {
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formRelatorio:campoCD");
		this.setLinkDisabled(false);
		return listacds;
	}

	/**
	 * @param listacds
	 *            the listacds to set
	 */
	public void setListacds(List<SelectItem> plistacds) {
		listacds = plistacds;
	}

	/**
	 * @return the listadrs
	 */
	public List<SelectItem> getListadrs() {
		FacesContext.getCurrentInstance().getPartialViewContext()
				.getRenderIds().add("formRelatorio:campoDR5");
		return listadrs;
	}

	/**
	 * @param listadrs
	 *            the listadrs to set
	 */
	public void setListadrs(List<SelectItem> listadrs) {
		RelatorioMB.listadrs = listadrs;
	}

	/**
	 * @param listacategoria
	 *            the listacategoria to set
	 */
	public void setListacategoria(List<SelectItem> plistacategoria) {
		listacategoria = plistacategoria;
	}

	/**
	 * @param listatipopedido
	 *            the listatipopedido to set
	 */
	public void setListatipopedido(List<SelectItem> plistatipopedido) {
		listatipopedido = plistatipopedido;
	}

	public List<SelectItem> getListanaturezaorgao() {
		return getListaNatureza();
	}

	public void setListanaturezaorgao(List<SelectItem> plistanaturezaorgao) {
		listanaturezaorgao = plistanaturezaorgao;
	}

	/**
	 * @return the listaanos
	 */
	public List<SelectItem> getListaanos() {
		return listaanos;
	}

	/**
	 * @param listaanos
	 *            the listaanos to set
	 */
	public void setListaanos(List<SelectItem> plistaanos) {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		for (int i = 2010; i < 2031; i++) {
			SelectItem item = new SelectItem();
			item.setLabel(Integer.toString(i));
			item.setValue(i);
			lista.add(item);
			item = null;
		}
		listaanos = lista;

	}

	/**
	 * @return the listameses
	 */
	public List<SelectItem> getListameses() {

		return listameses;
	}

	/**
	 * @param listameses
	 *            the listameses to set
	 */
	public void setListameses(List<SelectItem> plistameses) {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		String[] aMeses = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio",
				"Junho", "Julho", "Agosto", "setembro", "Outubro", "Novembro",
				"Dezembro" };
		for (int i = 0; i < aMeses.length; i++) {
			SelectItem item = new SelectItem();
			item.setLabel(aMeses[i]);
			item.setValue(i + 1);
			lista.add(item);
			item = null;
		}
		listameses = lista;
	}

	public String getSelCD() {
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		selCD = (SelectOneMenu) viewRoot
				.findComponent(":formRelatorio:campoCD");
		String valor = selCD.getValue().toString();

		return valor;
	}

	public String getSelDR() {
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		selCD = (SelectOneMenu) viewRoot
				.findComponent(":formRelatorio:campoDR5");
		String valor = selDR.getValue().toString();

		return valor;
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

	public List<SelectItem> getListacategoria() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		return lista;
	}

	public List<SelectItem> getListatipopedido() {
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem("T", "TODOS"));
		lista.add(new SelectItem(TIPOPEDIDO.EXTRA.getCodigo(), TIPOPEDIDO.EXTRA
				.getDescricao()));
		lista.add(new SelectItem(TIPOPEDIDO.NORMAL.getCodigo(),
				TIPOPEDIDO.NORMAL.getDescricao()));
		return lista;
	}

	/**
	 * Retorna uma coleção de CDs para exibição na combobox.
	 * 
	 * @return
	 */

	public List<SelectItem> getListaDRs(String mcucd) {
		List<SelectItem> lista = new ArrayList<SelectItem>();

		Connection connection = null;
		try {
			connection = dataSource.getConnection();

			DRDAO dao = new DRDAO(connection);
			ArrayList<Dr> aDR = new ArrayList<Dr>();

			aDR = (ArrayList<Dr>) dao.findByKeyNumber(mcucd);

			for (Dr tdr : aDR) {
				SelectItem selitem = new SelectItem();
				selitem.setLabel(tdr.getCCNAME());
				selitem.setValue(tdr.getCCCO());
				lista.add(selitem);
				selitem = null;
			}
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
		return lista;
	}

	/**
	 * Retorna uma coleÃ§Ã£o de Natureza do OrgÃ£o (ADM,OPE,TODOS, etc) para
	 * exibiÃ§Ã£o na combobox.
	 * 
	 * @return
	 */

	public static List<SelectItem> getListaNatureza() {
		List<SelectItem> lista = new ArrayList<SelectItem>();

		lista.add(new SelectItem(NATUREZAORGAO.ALL.getCodigo(),
				NATUREZAORGAO.ALL.getDescricao()));
		lista.add(new SelectItem(NATUREZAORGAO.ADMINISTRATIVO.getCodigo(),
				NATUREZAORGAO.ADMINISTRATIVO.getDescricao()));
		lista.add(new SelectItem(NATUREZAORGAO.OPERACIONAL.getCodigo(),
				NATUREZAORGAO.OPERACIONAL.getDescricao()));
		lista.add(new SelectItem(NATUREZAORGAO.REDE.getCodigo(),
				NATUREZAORGAO.REDE.getDescricao()));
		lista.add(new SelectItem(NATUREZAORGAO.TERCEIRO_ACF.getCodigo(),
				NATUREZAORGAO.TERCEIRO_ACF.getDescricao()));
		lista.add(new SelectItem(NATUREZAORGAO.TERCEIRO_AGF.getCodigo(),
				NATUREZAORGAO.TERCEIRO_AGF.getDescricao()));
		lista.add(new SelectItem(NATUREZAORGAO.TERCEIRO_ACC.getCodigo(),
				NATUREZAORGAO.TERCEIRO_ACC.getDescricao()));

		return lista;
	}

	public void resetDates() {
		Calendar c = Calendar.getInstance(); // this takes current date
		this.dt_final = c.getTime();
		this.dt_final2 = c.getTime();
		c.set(Calendar.DAY_OF_MONTH, 1);
		this.dt_inicial = c.getTime();
		this.dt_inicial2 = c.getTime();
	}

	public void onGerarRaletorio() {
		this.setLinkDisabled(true);
		DataERP dt_erp = new DataERP();

		// Formatting Date in Java using SimpleDateFormat
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

		String dt_ini = dt_erp.getDataERP(DATE_FORMAT.format(dt_inicial));
		String dt_fim = dt_erp.getDataERP(DATE_FORMAT.format(dt_final));

		try {

			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) fc
					.getExternalContext().getRequest();

			// String vHost = "jhom2";
			String vHost = "";
			vHost = request.getServletContext()
					.getInitParameter("serverSISCAP");

			String rPrefix = "https://";

			if (request.getRemoteHost().equals("127.0.0.1")) {
				vHost = "localhost:8080";
				rPrefix = "http://";
			}

			String vRelat = "";
			
			if(mcu.equals(null)){
				mcu = "";
			}
			
			mcu = mcu.replace(" ", "");
			
//			if(mcu.trim().length() == 0){
//				vRelat = rPrefix
//						+ vHost
//						+ "/siscap_birt/frameset?__report=report/HistoricoPedido_No_Mcu.rptdesign&__parameterpage=false&pDataPedIni=" + dt_ini + "&pDataPedFim="
//						+ dt_fim;
//				FacesContext.getCurrentInstance().getExternalContext()
//				.redirect(vRelat);
//			}else{
				if((mcu.trim().length() < 8) && (mcu.trim().length() > 0)){
					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Parametros do Relatório",
							"O código MCU deve conter os zeros a frente para compor o padrão de 8 posições !");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}else{
				vRelat = rPrefix
						+ vHost
						+ "/siscap_birt/frameset?__report=report/HistoricoPedido.rptdesign&__parameterpage=false&pMCU=%25"
						+ mcu.trim() + "&pDataPedIni=" + dt_ini + "&pDataPedFim="
						+ dt_fim;
				FacesContext.getCurrentInstance().getExternalContext()
				.redirect(vRelat);
				}
		//	}
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public boolean chekaCampos(int pCamp) {
		boolean retorno = true;
		String texto = "";
		String cd = getCd().trim();
		String ano = getAno().trim();
		if (pCamp == 1) {
			if (cd == null || cd.equals("")) {
				texto = "CD";
				retorno = false;
			}
		}
		if (pCamp == 2) {
			if (cd == null || cd.equals("")) {
				texto = "CD ou ano";
				retorno = false;
			}
			if (ano == null || ano.equals("")) {
				texto = "CD ou ano";
				retorno = false;
			}
		}
		if (retorno == false) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Parametros do Relatório",
					"Falta preencher campo o obrigatório " + texto + " !");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		return retorno;
	}


	public void onGerarRaletorioGestor(int pRel) {
		setLinkDisabled(true);
		DataERP dt_erp = new DataERP();

		// Formatting Date in Java using SimpleDateFormat
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
		
		if(cd2 != null){
			cd = cd2;
		}

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) fc
					.getExternalContext().getRequest();

			// String vHost = "jhom2";
			String vHost = request.getServletContext().getInitParameter(
					"serverSISCAP");

			String rPrefix = "https://";

			if (request.getRemoteHost().equals("127.0.0.1")) {
				vHost = "localhost:8080";
				rPrefix = "http://";
			}

			String vRelat = "";
			String dt_ini = "";
			String dt_fim = "";
			String dt_ini2 = "";
			String dt_fim2 = "";
			String dr = "";


			if ((dt_inicial != null) && (dt_final != null)
					|| (dt_inicial2 != null) && (dt_final2 != null)) {
				dt_ini = dt_erp.getDataERP(DATE_FORMAT.format(dt_inicial));
				dt_fim = dt_erp.getDataERP(DATE_FORMAT.format(dt_final));
				dt_ini2 = dt_erp.getDataERP(DATE_FORMAT.format(dt_inicial2));
				dt_fim2 = dt_erp.getDataERP(DATE_FORMAT.format(dt_final2));
				if (this.dr != "" && this.dr != null) {
					dr = this.dr.replaceAll("000", "");
				} else {
					dr = "%";
				}

				if (Long.parseLong(dt_ini) < Long.parseLong(dt_fim)) {
					switch (pRel) {
					case 1:
						if (chekaCampos(1)) {
							vRelat = rPrefix
									+ vHost
									+ "/siscap_birt/frameset?__report=report/GrupoAtendimento.rptdesign&__parameterpage=false&pCD="
									+ getCd();
						}
						break;
					case 2:
						if (chekaCampos(1)) {
							vRelat = rPrefix
									+ vHost
									+ "/siscap_birt/frameset?__report=report/OrgaoGrupo.rptdesign&__parameterpage=false&pMCU_CD="
									+ getCd().trim() + "&pMCU_Org=%25"
									+ getMcu().trim() + "%25";
						}
						break;
					case 3:
						if (chekaCampos(2)) {
							vRelat = rPrefix
									+ vHost
									+ "/siscap_birt/frameset?__report=report/Cronograma.rptdesign&__parameterpage=false&pCD="
									+ getCd().trim() + "&pGRP=%25"
									+ getGrupo().trim().toUpperCase() + "%25"
									+ "&pAno=" + getAno().trim();
						}
						break;
					case 4:
						vRelat = rPrefix
								+ vHost
								+ "/siscap_birt/frameset?__report=report/ItensParametrizados.rptdesign&__parameterpage=false&pCD="
								+ getCd().trim() + "&pDescricao="
								+ getDescricao().toUpperCase() + "%25"
								+ "&pTPPedido=" + getTipopedido().trim()
								+ "&pTPOrgao=" + getTipoorgao();
						break;

					case 5:
						vRelat = rPrefix
								+ vHost
								+ "/siscap_birt/frameset?__report=report/PedidoNaCaptacao.rptdesign&__parameterpage=false&pDataPedIni="
								+ dt_ini + "&pDataPedFim=" + dt_fim + "&pDR="
								+ dr;
						break;
					case 6:
						vRelat = rPrefix
								+ vHost
								+ "/siscap_birt/frameset?__report=report/PedidoStatusGeral.rptdesign&__parameterpage=false&pDataPedIni="
								+ dt_ini2 + "&pDataPedFim=" + dt_fim2;
						break;
					case 7:
						SimpleDateFormat dt_format = new SimpleDateFormat("MM/dd/yyyy");
						dt_ini2 = dt_format.format(dt_inicial2).toString();
						dt_fim2 = dt_format.format(dt_final2).toString();
						if( this.getPedido().length() > 1){
							dt_ini2= "01/01/2001";
							dt_fim2 = "01/01/2100";
							this.setMatricula("%");
						}else{
							this.setPedido("%");
						}
						if( this.getMatricula().length() > 1){
							this.setPedido("%");
						}else{
							this.setMatricula("%");
						}
						vRelat = rPrefix
							+ vHost
							+ "/siscap_birt/frameset?__report=report/auditoria.rptdesign&__parameterpage=false&pDataIni="
							+ dt_ini2 + "&pDataFim=" + dt_fim2 + "&pMatricula=" + this.getMatricula() + "&pPedido=" + this.getPedido();
						break;
					}
					resetDates();
					if(!vRelat.equals("")){
						FacesContext.getCurrentInstance().getExternalContext()
						.redirect(vRelat);
					}
					
				} else {
					FacesMessage msgerr = new FacesMessage(
							FacesMessage.SEVERITY_WARN, "ATENÇÃO !",
							"A data inicial é posterior a data final.");
					FacesContext.getCurrentInstance().addMessage(null, msgerr);
				}

			} else {
				switch (pRel) {

				case 1:
				//	if (chekaCampos(1)) {
						vRelat = rPrefix
								+ vHost
								+ "/siscap_birt/frameset?__report=report/GrupoAtendimento.rptdesign&__parameterpage=false&pCD="
								+ getCd();
				//	}
					break;
				case 2:
					if (chekaCampos(1)) {
						vRelat = rPrefix
								+ vHost
								+ "/siscap_birt/frameset?__report=report/OrgaoGrupo.rptdesign&__parameterpage=false&pMCU_CD="
								+ getCd().trim() + "&pMCU_Org=%25"
								+ getMcu().trim() + "%25";
					}
					break;
				case 3:
					if (chekaCampos(2)) {
						vRelat = rPrefix
								+ vHost
								+ "/siscap_birt/frameset?__report=report/Cronograma.rptdesign&__parameterpage=false&pCD="
								+ getCd().trim() + "&pGRP=%25"
								+ getGrupo().trim().toUpperCase() + "%25"
								+ "&pAno=" + getAno().trim();
					}
					break;
				case 4:
					vRelat = rPrefix
							+ vHost
							+ "/siscap_birt/frameset?__report=report/ItensParametrizados.rptdesign&__parameterpage=false&pCD="
							+ getCd().trim() + "&pDescricao="
							+ getDescricao().toUpperCase() + "%25"
							+ "&pTPPedido=" + getTipopedido().trim()
							+ "&pTPOrgao=" + getTipoorgao();
					break;
				}
				if(!vRelat.equals("")){
					FacesContext.getCurrentInstance().getExternalContext()
					.redirect(vRelat);
				}
			}
			cd2 = null;
			cd = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}
