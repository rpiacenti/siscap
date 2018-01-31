package br.com.correios.ppjsiscap.seguranca;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.xml.ws.WebServiceException;

import br.com.correios.componente.idautorizador.usuario.ComponenteException;
import br.com.correios.componente.idautorizador.usuario.Funcionalidade;
import br.com.correios.componente.idautorizador.usuario.Grupo;
import br.com.correios.componente.idautorizador.usuario.IdAutorizadorUsuarioService;
import br.com.correios.componente.idautorizador.usuario.ItemMenu;
import br.com.correios.componente.idautorizador.usuario.UsuarioExterno;
import br.com.correios.componente.idautorizador.usuario.UsuarioInterno;
import br.com.correios.ppjsiscap.excecao.CorreiosException;
import br.com.correios.ppjsiscap.util.DataERP;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.constantes.MensagemID;
import br.com.correios.siscap.dao.CronogramaDAO;
import br.com.correios.siscap.dao.DRDAO;
import br.com.correios.siscap.dao.GrupoAtendimentoDAO;
import br.com.correios.siscap.dao.OrgaoDAO;
import br.com.correios.siscap.dao.RastreamentoDAO;
import br.com.correios.siscap.excecao.UtilCorreiosException;
import br.com.correios.siscap.model.GrupoAtendimento;
import br.com.correios.siscap.model.Orgao;

@ApplicationScoped
public class UsuarioLogadoFactory {

	private static final String IDIOMA = "P";

	@Inject
	private IdAutorizadorUsuarioService autorizadorServico;

	@Inject
	private UtilCorreiosException utilCorreiosException;
	
	@Inject @OracleDb DataSource ds;

	public void constroiUsuarioLogado(HttpServletRequest request,
			Usuario usuario, String loginCas) throws CorreiosException, SQLException {

		adicionaUsuarioExternoOuInternoAoUsuario(usuario, request);
		
		adicionaMenuAoUsuario(usuario, request);

		adicionaGruposAoUsuario(usuario);

		adicionaFuncionalidadesAoUsuario(usuario);

		setInformacaoUsuarioSiscap(usuario);
		usuario.setBrowser(request.getHeader("User-Agent"));
		usuario.setIp(request.getRemoteAddr());

		UtilLog.logger.info("Usuario montado:" + usuario.getNome()); //usuario.getUsuarioInterno().getMatricula()
	}

	private void adicionaFuncionalidadesAoUsuario(Usuario usuario)
			throws CorreiosException, SQLException {
		 for(Grupo g :usuario.getGrupos()){
			 try {
			 List<Funcionalidade> funcionalidades =	 autorizadorServico.listarFuncionalidadesAutorizadasDoGrupo(g.getId().getIdGrupo());
			 usuario.getFuncionalidades().addAll(funcionalidades);
			 } catch (ComponenteException e) {
				 throw utilCorreiosException.exibirMensagem(MensagemID.ERRO, e.getMessage());
			 }
		 }

	}
	
	private void setInformacaoUsuarioSiscap(Usuario usuario) throws SQLException {

		Connection connection = null;
		try {

			connection = ds.getConnection();
			
			OrgaoDAO dao = new OrgaoDAO(connection);

			Orgao orgao = new Orgao();
	      // usuario.setMculotacao("    00024515");   //comentar para deploy "    00429267"

			orgao = dao.findByOrgao(usuario.getMculotacao());
			
			if(orgao != null){
				usuario.setAn8Orgao(orgao.getAn8());
				usuario.setNomeOrgao(orgao.getNome());
				usuario.setTipoOrgao(orgao.getTipo());
				usuario.setEmailOrgao(orgao.getEmail());
				usuario.setSiglaUF(orgao.getUf());
				usuario.setCodigoDR(orgao.getDr().trim());
				usuario.setStatusERP(orgao.getStatus());
				usuario.setMcucia(orgao.getMcucia());
				usuario.setSiglaCD(orgao.getDr());
				usuario.setSiglaLotacao(orgao.getSigla());
				usuario.setSiglaLotacao(orgao.getSigla());
				// Checa contrato ....
				if (usuario.getTipoOrgao().indexOf("12,16,27,28") > -1) {
					
					String[] aContrato = dao.findByContraoOrgao(usuario.getMculotacao());
					
					usuario.setNaturezaOrgao("ACF,AGF,ACC");
					
					DataERP dtERP = new DataERP();
					Long ldtERP = Long.parseLong(dtERP.getDataERPToday());
					Long iniContrat = Long.parseLong(aContrato[1]);
					Long fimContrat = Long.parseLong(aContrato[2]);
					
					if (aContrato[0].indexOf("A,D") == -1) {
						usuario.setStatusContratoERP("0"); // Contrato cancelado,
						// extinto etc
					} else if (ldtERP < fimContrat && ldtERP > iniContrat) {
						usuario.setStatusContratoERP("1"); // Contrato OK !
					} else {
						usuario.setStatusContratoERP("0"); // Contrato fora da
						// vigência.
					}
					
				} else {
					usuario.setNaturezaOrgao("AC");
					usuario.setStatusContratoERP("1");
				}
			}


			// fim checa contrato

			// checa Grupo Atendimento
			
			orgao = null;
			orgao = dao.findByOrgao(usuario.getMcucia());
			usuario.setSiglaCD(orgao.getDr());
			orgao = null;

			GrupoAtendimentoDAO dao2 = new GrupoAtendimentoDAO(connection, usuario);

			usuario.setGrupoAtendimento(dao2.findByUserGrupo(usuario.getMculotacao(),
					usuario.getMcucia()));

			if (usuario.getGrupoAtendimento().size() == 0) {
				ArrayList<GrupoAtendimento> aGrupo = new ArrayList<GrupoAtendimento>();
				GrupoAtendimento grpModel = new GrupoAtendimento();
				grpModel.setNumGrupo(0);
				grpModel.setDescricao("Grupo de Atendimento não disponível para este usuário.");
				aGrupo.add(grpModel);
				usuario.setGrupoAtendimento(aGrupo);
				grpModel = null;
				usuario.setStatusContratoERP("0");// Sem vinculo com grupo de atendimento
			}else{
				CronogramaDAO dao3 = new CronogramaDAO(connection, usuario);
				usuario.setGrupoAtendimento(dao3.setCronogramaAnoMes(usuario.getGrupoAtendimento()));
			}

			// Fim checa Grupo Atendimento
			
			
			DRDAO dao3 = new DRDAO(connection);

			usuario.setDrs(dao3.findByKeyNumber(usuario.getMcucia()));
			usuario.setDescricaoDR(usuario.getCodigoDR());
			usuario.setDescricaoCD(usuario.getMcucia());
			usuario.setAbrevCD(usuario.getMcucia());

			Calendar calendarData = Calendar.getInstance();

			calendarData.setTime(new Date());
		} finally {
			if(connection != null){
				connection.close();
			}
		}
	}

	private void adicionaUsuarioExternoOuInternoAoUsuario(Usuario usuario,
			HttpServletRequest request) throws CorreiosException {

		UsuarioExterno usuarioExterno = null;
		UsuarioInterno usuarioInterno = null;
		try {
			usuarioInterno = autorizadorServico.getUsuarioInternoAtivoPeloLogin(request.getUserPrincipal().getName());
			if (usuarioInterno == null) {
				usuarioExterno = autorizadorServico.getUsuarioExterno(request.getUserPrincipal().getName());
				if (usuarioExterno == null) {
					throw utilCorreiosException.exibirMensagem(MensagemID.PERFIL_INEXISTENTE);
				} else {
					usuario.setUsuarioExterno(usuarioExterno);
					if(usuarioExterno.getUsuarioExternoPF().getCpf() != null){
						usuario.setMatricula(usuarioExterno.getUsuarioExternoPF().getCpf());
					}else{
						usuario.setMatricula(usuarioExterno.getUsuarioExternoPJ().getCnpj());
					}
				}
			} else {
				UtilLog.logger.debug("O Usuário interno:"+usuarioInterno.getLogin() );
				usuario.setMatricula(usuarioInterno.getLogin());
				usuario.setUsuarioInterno(usuarioInterno);
				if(usuario.getMculotacao() == null){
		//			usuario.setMculotacao(usuarioInterno.getUsuarioFuncionario().getMcuLotacao());
				}
			}
			
		} catch (ComponenteException e) {
			throw utilCorreiosException.exibirMensagem(MensagemID.ERRO,	e.getMessage());
		} catch (WebServiceException e) {
			throw utilCorreiosException.exibirMensagem(MensagemID.ERRO,	"O SISCAP não está conseguindo se comunicar com IDCorreios: " + e.getMessage());
		} 

	}

	private void adicionaGruposAoUsuario(Usuario usuario)
			throws CorreiosException {
		try {
			if (usuario.getUsuarioExterno() != null
					&& usuario.getUsuarioExterno().getUsuarioInterno() == null) {
				List<Grupo> gruposAutorizadosDoUsuarioPF = autorizadorServico
						.listarGruposAutorizadosDoUsuarioPF(usuario
								.getUsuarioExterno().getIdCorreios(), usuario
								.getUsuarioExterno().getUsuarioExternoPJ()
								.getIdCorreios());
				usuario.getGrupos().addAll(gruposAutorizadosDoUsuarioPF);
			} else {
				List<Grupo> gruposAutorizadosDoUsuarioInternoNoSistema = autorizadorServico
						.listarGruposAutorizadosDoUsuarioInternoNoSistema(usuario
								.getUsuarioInterno().getId());
				usuario.getGrupos().addAll(
						gruposAutorizadosDoUsuarioInternoNoSistema);
			}
		} catch (ComponenteException e) {
			throw utilCorreiosException.exibirMensagem(e, MensagemID.ERRO,
					e.getMessage());
		}
	}

	private void adicionaMenuAoUsuario(Usuario usuario,
			HttpServletRequest request) throws CorreiosException {
		try {
			if (usuario.getUsuarioExterno() != null
					&& usuario.getUsuarioExterno().getUsuarioExternoPJ() == null) {
				throw utilCorreiosException.exibirMensagem(
						MensagemID.USUARIO_EXTERNO_SEM_VINCULACAO_COM_PJ,
						usuario.getNome());
			} else if (usuario.getUsuarioExterno() != null
					&& usuario.getUsuarioExterno().getUsuarioExternoPJ() != null
					&& usuario.getUsuarioExterno().getUsuarioInterno() == null) {
				List<ItemMenu> itensMenuIdiomaAutorizadosDoUsuarioPF = autorizadorServico
						.listarItensMenuIdiomaAutorizadosDoUsuarioPF(usuario
								.getUsuarioExterno().getUsuarioExternoPJ()
								.getIdCorreios(), usuario.getLogin(), IDIOMA);

				usuario.setItensDeMenu(itensMenuIdiomaAutorizadosDoUsuarioPF);
			} else {
				List<ItemMenu> menuIdiomaAutorizadosDoUsuarioInterno = autorizadorServico
						.listarItensMenuIdiomaAutorizadosDoUsuarioInterno(
								usuario.getUsuarioInterno().getId(), IDIOMA);
				usuario.setItensDeMenu(menuIdiomaAutorizadosDoUsuarioInterno);
			}
		} catch (ComponenteException e) {
			throw utilCorreiosException.exibirMensagem(e, MensagemID.ERRO,
					e.getMessage());
		}

	}

}
