package br.com.correios.ppjsiscap.seguranca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.primefaces.context.RequestContext;

import br.com.correios.componente.idautorizador.usuario.Funcionalidade;
import br.com.correios.componente.idautorizador.usuario.Grupo;
import br.com.correios.componente.idautorizador.usuario.ItemMenu;
import br.com.correios.componente.idautorizador.usuario.UsuarioExterno;
import br.com.correios.componente.idautorizador.usuario.UsuarioExternoPJ;
import br.com.correios.componente.idautorizador.usuario.UsuarioInterno;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.dao.OrgaoDAO;
import br.com.correios.siscap.model.Dr;
import br.com.correios.siscap.model.GrupoAtendimento;

@SuppressWarnings("serial")
@Named("usuario")
@SessionScoped
public class Usuario implements javax.servlet.http.HttpSessionBindingListener, Serializable {

	private static final int QUATRO = 4;

	private static final int TRES = 3;

	private static final int DOIS = 2;

	private static final int UM = 1;

	private List<Grupo> grupos = new ArrayList<Grupo>();

	private List<Funcionalidade> funcionalidades = new ArrayList<Funcionalidade>();

	private List<ItemMenu> itensDeMenu = new ArrayList<ItemMenu>();

	private SortedMap<Integer, ItemMenuDecorator> itensDeMenuHierarquico;

	private List<ItemMenuDecorator> menu;

	private List<UsuarioExternoPJ> usuariosExternosPJ = new ArrayList<UsuarioExternoPJ>();
	
	private List<GrupoAtendimento> grupoAtendimento = new ArrayList<GrupoAtendimento>();

	private String nome;
	private String login;
	private String email;

	private String browser;
	private String ip;
	private String siglaLotacao;
	private String cpfCnpj;

	private String tipoUsuario;
	private String siglaUF;
	private String codigoDR;
	private String descricaoDR;
	private String siglaCD;
	private String descricaoCD;
	private String abrevCD;
	private String mcucia;
	private String mculotacao;
	private String matricula;
	private String tipoOrgao;
	private String naturezaOrgao;
	private String nomeOrgao;
	private String an8Orgao;
	private String emailOrgao;
	private String statusERP;
	private String statusContratoERP;
	
	private String campoCD;
	
	private List<Dr> drs = new ArrayList<Dr>();

	private boolean primeiroAcesso;

	private UsuarioExterno usuarioExterno;

	private UsuarioInterno usuarioInterno;

	private transient HttpSession session;

	@Inject
	private UsuariosLogados usuariosLogados;

	public List<GrupoAtendimento> getGrupoAtendimento(){
		return grupoAtendimento;
	}
	
	public void setGrupoAtendimento(List<GrupoAtendimento> list){
		this.grupoAtendimento = list;
	}

	public boolean isValid() {
		return getLogin() != null;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(HttpSession session) {
		this.session = session;
	}

	/**
	 * @return the session
	 */
	public HttpSession getSession() {
		return session;
	}

	/**
	 * @return the grupos
	 */
	public List<Grupo> getGrupos() {
		return grupos;
	}

	/**
	 * @param grupos
	 *            the grupos to set
	 */
	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	/**
	 * @return the itensDeMenuHierarquico
	 */
	public SortedMap<Integer, ItemMenuDecorator> getItensDeMenuHierarquico() {
		return itensDeMenuHierarquico;
	}

	/**
	 * @param itensDeMenuHierarquico
	 *            the itensDeMenuHierarquico to set
	 */
	public void setItensDeMenuHierarquico(
			SortedMap<Integer, ItemMenuDecorator> itensDeMenuHierarquico) {
		this.itensDeMenuHierarquico = itensDeMenuHierarquico;
	}

	/**
	 * @return the usuariosExternosPJ
	 */
	public List<UsuarioExternoPJ> getUsuariosExternosPJ() {
		return usuariosExternosPJ;
	}

	/**
	 * @param usuariosExternosPJ
	 *            the usuariosExternosPJ to set
	 */
	public void setUsuariosExternosPJ(List<UsuarioExternoPJ> usuariosExternosPJ) {
		this.usuariosExternosPJ = usuariosExternosPJ;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the browser
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * @param browser
	 *            the browser to set
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
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
		this.ip = ip;
	}

	/**
	 * @return the siglaLotacao
	 */
	public String getSiglaLotacao() {
		return siglaLotacao;
	}

	/**
	 * @param siglaLotacao
	 *            the siglaLotacao to set
	 */
	public void setSiglaLotacao(String siglaLotacao) {
		this.siglaLotacao = siglaLotacao;
	}

	/**
	 * @return the cpfCnpj
	 */
	public String getCpfCnpj() {
		return cpfCnpj;
	}

	/**
	 * @param cpfCnpj
	 *            the cpfCnpj to set
	 */
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	/**
	 * @return the itensDeMenu
	 */
	public List<ItemMenu> getItensDeMenu() {
		return itensDeMenu;
	}

	/**
	 * @return the usuarioExterno
	 */
	public UsuarioExterno getUsuarioExterno() {
		return usuarioExterno;
	}
	
	

	/**
	 * @param usuarioExterno
	 *            the usuarioExterno to set
	 */
	public void setUsuarioExterno(UsuarioExterno usuarioExterno) {
		this.usuarioExterno = usuarioExterno;
		if (usuarioExterno != null) {
			nome = usuarioExterno.getNome();
			email = usuarioExterno.getEmail();
			login = usuarioExterno.getIdCorreios();
		}
	}

	public void setItensDeMenu(List<ItemMenu> itensDeMenu) {
		this.itensDeMenu = itensDeMenu;
		SortedMap<Integer, ItemMenuDecorator> mapa = new TreeMap<Integer, ItemMenuDecorator>();

		for (int i = 0; i < itensDeMenu.size(); i++) {

			ItemMenu it = itensDeMenu.get(i);
			if (it.getNivel() == UM) {
				mapa.put(it.getId().getIdItemMenu(), new ItemMenuDecorator(it));
				continue;
			}
			if (it.getNivel() == DOIS) {
				mapa.get(it.getIdItemMenuSup())
						.getFilhos()
						.put(it.getId().getIdItemMenu(),
								new ItemMenuDecorator(it));
				continue;
			}
			if (it.getNivel() == TRES) {
				for (ItemMenuDecorator m : mapa.values()) {// iterando 1º nível
					ItemMenuDecorator itemMenuDecorator = m.getFilhos().get(
							it.getIdItemMenuSup()); // 2º nivel - recuperando o
													// pai
					if (itemMenuDecorator != null) {// o filho do segundo nível.
						itemMenuDecorator.getFilhos().put(
								it.getId().getIdItemMenu(),
								new ItemMenuDecorator(it));
						break;
					}
				}
				continue;
			}
			if (it.getNivel() == QUATRO) {
				for (ItemMenuDecorator m : mapa.values()) {// iterando 1º nível
					for (ItemMenuDecorator m2 : m.getFilhos().values()) { // 2º
																			// nivel
						for (ItemMenuDecorator m3 : m2.getFilhos().values()) {// 3º
																				// nível
							ItemMenuDecorator itemMenuDecorator = m3
									.getFilhos().get(it.getIdItemMenuSup());// recuperando
																			// o
																			// pai
							if (itemMenuDecorator != null) {// o 3º nível.
								itemMenuDecorator.getFilhos().put(
										it.getId().getIdItemMenu(),
										new ItemMenuDecorator(it));
								break;
							}
						}
					}
				}
			}

		}
		setItensDeMenuHierarquico(mapa);

	}

	/**
	 * Menu para iteração no menu.xhtml
	 * 
	 * @return
	 */
	public List<ItemMenuDecorator> getMenu() {
		menu = new ArrayList<ItemMenuDecorator>();
		if (getItensDeMenuHierarquico() != null) {
			menu.addAll(getItensDeMenuHierarquico().values());
		}
		return menu;
	}

	/**
	 * @param menu
	 *            the menu to set
	 */
	public void setMenu(List<ItemMenuDecorator> menu) {
		this.menu = menu;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Usuario other = (Usuario) obj;
		if (login == null) {
			if (other.login != null) {
				return false;
			}
		} else if (!login.equals(other.login)) {
			return false;
		}
		return true;
	}

	/**
	 * @see HttpSessionBindingListener#valueBound(HttpSessionBindingEvent)
	 */
	public void valueBound(HttpSessionBindingEvent evt) {
		UtilLog.logger.info(
				"Usuário " + getNome() + " foi inserido na sessão");
	}

	/**
	 * @see HttpSessionBindingListener#valueUnbound(HttpSessionBindingEvent)
	 */
	public void valueUnbound(HttpSessionBindingEvent evt) {
		usuariosLogados.remover(this);
		UtilLog.logger.info(
				"Usuário " + getNome() + " foi removido da sessão");
	}

	public void adicionaUsuarioExternoPJ(UsuarioExternoPJ usuarioExternoPJ) {
		this.usuariosExternosPJ.add(usuarioExternoPJ);
	}

	/**
	 * @return the primeiroAcesso
	 */
	public boolean isPrimeiroAcesso() {
		return primeiroAcesso;
	}

	/**
	 * @param primeiroAcesso
	 *            the primeiroAcesso to set
	 */
	public void setPrimeiroAcesso(boolean primeiroAcesso) {
		this.primeiroAcesso = primeiroAcesso;
	}

	/**
	 * @return the usuarioInterno
	 */
	public UsuarioInterno getUsuarioInterno() {
		return usuarioInterno;
	}

	/**
	 * @param usuarioInterno
	 *            the usuarioInterno to set
	 */
	public void setUsuarioInterno(UsuarioInterno usuarioInterno) {
		this.usuarioInterno = usuarioInterno;
		if (usuarioInterno != null) {
			if(usuarioInterno.getUsuarioFuncionario() != null){
				nome = usuarioInterno.getUsuarioFuncionario().getNome();
				login = usuarioInterno.getLogin();			
				siglaLotacao = usuarioInterno.getUsuarioFuncionario()
						.getSiglaLotacao();
				email = usuarioInterno.getEmail();
				mculotacao = usuarioInterno.getUsuarioFuncionario().getMcuLotacao();
			}else{
				nome = usuarioInterno.getNome();
				login = usuarioInterno.getLogin();			
				siglaLotacao = usuarioInterno.getLotacao();
				email = usuarioInterno.getEmail();
				mculotacao = usuarioInterno.getLotacao();
			}
			

		}
	}

	/**
	 * @return the funcionalidades
	 */
	public List<Funcionalidade> getFuncionalidades() {
		return funcionalidades;
	}

	/**
	 * @param funcionalidades
	 *            the funcionalidades to set
	 */
	public void setFuncionalidades(List<Funcionalidade> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	/**
	 * @return the tipoUsuario
	 */
	public String getTipoUsuario() {
		return tipoUsuario;
	}

	/**
	 * @param tipoUsuario
	 *            the tipoUsuario to set
	 */
	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	/**
	 * @return the siglaUF
	 */
	public String getSiglaUF() {
		return siglaUF;
	}

	/**
	 * @param siglaUF
	 *            the siglaUF to set
	 */
	public void setSiglaUF(String siglaUF) {
		this.siglaUF = siglaUF;
	}

	/**
	 * @return the siglaCD
	 */
	public String getSiglaCD() {
		return siglaCD;
	}

	/**
	 * @param siglaCD
	 *            the siglaCD to set
	 */
	public void setSiglaCD(String siglaCD) {
		this.siglaCD = siglaCD;
	}

	/**
	 * @return the mculotacao
	 */
	public String getMculotacao() {
		return mculotacao;
	}

	/**
	 * @param mculotacao
	 *            the mculotacao to set
	 */
	public void setMculotacao(String mculotacao) {
		this.mculotacao = mculotacao;
	}
	
	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
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
		this.mcucia = mcucia;
	}

	/**
	 * @return the tipoOrgao
	 */
	public String getTipoOrgao() {
		return tipoOrgao;
	}

	/**
	 * @param tipoOrgao
	 *            the tipoOrgao to set
	 */
	public void setTipoOrgao(String tipoOrgao) {
		this.tipoOrgao = tipoOrgao;
	}
	
	
	public String getNaturezaOrgao() {
		return naturezaOrgao;
	}

	public void setNaturezaOrgao(String naturezaOrgao) {
		this.naturezaOrgao = naturezaOrgao;
	}

	/**
	 * @return the nomeOrgao
	 */
	public String getNomeOrgao() {
		return nomeOrgao;
	}

	/**
	 * @param nomeOrgao
	 *            the nomeOrgao to set
	 */
	public void setNomeOrgao(String nomeOrgao) {
		this.nomeOrgao = nomeOrgao;
	}

	/**
	 * @return the an8Orgao
	 */
	public String getAn8Orgao() {
		return an8Orgao;
	}

	/**
	 * @param an8Orgao
	 *            the an8Orgao to set
	 */
	public void setAn8Orgao(String an8Orgao) {
		this.an8Orgao = an8Orgao;
	}

	/**
	 * @return the emialOrgao
	 */
	public String getEmailOrgao() {
		return emailOrgao;
	}

	/**
	 * @param emialOrgao
	 *            the emialOrgao to set
	 */
	public void setEmailOrgao(String emialOrgao) {
		this.emailOrgao = emialOrgao;
	}

	/**
	 * @return the codigoDR
	 */
	public String getCodigoDR() {
		return codigoDR;
	}

	/**
	 * @param codigoDR
	 *            the codigoDR to set
	 */
	public void setCodigoDR(String codigoDR) {
		this.codigoDR = codigoDR;
	}

	/**
	 * @return the statusERP
	 */
	public String getStatusERP() {
		return statusERP;
	}

	/**
	 * @param statusERP
	 *            the statusERP to set
	 */
	public void setStatusERP(String statusERP) {
		this.statusERP = statusERP;
	}

	public String getStatusContratoERP() {
		return statusContratoERP;
	}

	public void setStatusContratoERP(String statusContratoERP) {
		this.statusContratoERP = statusContratoERP;
	}

	
	/**
	 * @return the drs
	 */
	public List<Dr> getDrs() {
		return drs;
	}

	/**
	 * @param drs
	 *            the drs to set
	 */
	public void setDrs(List<Dr> drs) {
		this.drs = drs;
	}

	/**
	 * @return the descricaoDR
	 */
	public String getDescricaoDR() {

		return this.descricaoDR;
	}
	
	

	/**
	 * @param descricaoDR
	 *            the descricaoDR to set
	 */
	public void setDescricaoDR(String codigoDR) {
		for (Dr aDR : this.getDrs()) {
			// System.out.println("DRS: " + aDR.getCCCO() + " - " +
			// aDR.getCCNAME());
			if (aDR.getCCCO().trim().equals(codigoDR)) {
				this.descricaoDR = aDR.getCCNAME();
			}
		}
		if (codigoDR.trim().equals("00001")) {
			this.descricaoDR = "AC";
		}

		// this.descricaoDR = descricaoDR;
	}

	/**
	 * @return the descricaoCD
	 */
	public String getDescricaoCD() {
		return descricaoCD;
	}

	/**
	 * @param descricaoCD
	 *            the descricaoCD to set
	 */
	public void setDescricaoCD(String mcuCD) {
		if (mcuCD.equals("    00004010")) {
			this.descricaoCD = "CENTRO DE DISTRIBUIÇÃO OESTE - CD OESTE";
		} else {
			this.descricaoCD = "CENTRO DE DISTRIBUIÇÃO LESTE - CD LESTE";
		}

	}

	/**
	 * @return the abrevCD
	 */
	public String getAbrevCD() {
		return abrevCD;
	}

	/**
	 * @param abrevCD
	 *            the abrevCD to set
	 */
	public void setAbrevCD(String mcuCD) {
		if (mcuCD.equals("    00004010")) {
			this.abrevCD = "CD OESTE";
		} else {
			this.abrevCD = "CD LESTE";
		}
	}

	public void setDialogGestor(){
		if ((this.grupos.get(0).getNome().indexOf("TECNICO")> -1) || (this.grupos.get(0).getNome().indexOf("SISTEMA")> -1) || (this.grupos.get(0).getNome().indexOf("Gestor")> -1)) {
			RequestContext context = RequestContext
					.getCurrentInstance();
			context.execute("PF('dlgGestor').show();");
		}
	}
	
	
	
	public String getCampoCD() {
		return campoCD;
	}

	public void setCampoCD(String campoCD) {
		
		this.setAbrevCD(campoCD);
		this.setDescricaoCD(campoCD);
		this.mcucia = campoCD;
		this.campoCD = campoCD;
		session.setAttribute("usuario", this);
		setCDGestor();
	}

	public void setCDGestor(){
		
			RequestContext context = RequestContext
					.getCurrentInstance();
			context.execute("PF('dlgGestor').hide();");
		
	}

}
