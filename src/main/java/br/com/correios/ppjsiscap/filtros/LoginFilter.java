package br.com.correios.ppjsiscap.filtros;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.validation.Assertion;

import br.com.correios.ppjsiscap.excecao.CorreiosException;
import br.com.correios.ppjsiscap.excecao.CorreiosRuntimeException;
import br.com.correios.ppjsiscap.excecao.UtilExcecao;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.seguranca.UsuarioLogadoFactory;
import br.com.correios.ppjsiscap.seguranca.UsuariosLogados;
import br.com.correios.ppjsiscap.util.UtilMensagem;
import br.com.correios.siscap.constantes.Constantes;
import br.com.correios.siscap.constantes.MensagemID;
import br.com.correios.siscap.excecao.UtilCorreiosException;

/**
 * Filtro responsável por verificar se o usuário está corretamente logado na
 * aplicação
 * 
 * @author ArivaldoJunior
 */
public class LoginFilter implements Filter {

	private FilterConfig filterConfig = null;

	@Inject
	private UsuariosLogados usuariosLogados;

	@Inject
	private Usuario usuario;

	@Inject
	private UtilCorreiosException utilCorreiosException;

	@Inject
	private UsuarioLogadoFactory usuarioLogadoFactory;
	
	@Inject UtilMensagem utilMensagem;

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		String strLogout = filterConfig.getInitParameter("logoutPage");
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);
		if (usuario.getLogin() == null) {

			try {
				String loginFromCAS = getLoginFromCAS((Assertion) session.getAttribute(Constantes.CHAVE_SESSAO_USUARIO_CAS));
				
				usuarioLogadoFactory.constroiUsuarioLogado(request,	usuario,loginFromCAS);
				
			//	usuarioLogadoFactory.setInformacaoUsuarioSiscap(usuario);
				
				adicionaUsuarioAoContextoDaAplicacao(usuario, request);
				session.setAttribute("usuario", usuario);// passo necessário
															// para o listener
															// implementado na
															// classe Usuario
															// funcionar
				usuario.setSession(session);
			} catch (CorreiosException e) {				
				redirecionaPaginaErro(request, response, e);				
			} catch (SQLException e) {
				redirecionaPaginaErro(request, response, e);
			} 

		}

		chain.doFilter(request, response);

	}

	/**
	 * @param a
	 * @return
	 */
	private String getLoginFromCAS(Assertion a) {
		return a.getPrincipal().getName().trim();
	}

	/*
	 * Método usado para sistemas com o uso do SEGUI
	 */
	private void adicionaUsuarioAoContextoDaAplicacao(Usuario usuario,
			HttpServletRequest request) throws CorreiosException {
		if (usuariosLogados.isExiste(usuario)) {
			// verifica se o usuário já está
			// logado na aplicação.
			// permite entrada somente se estiver no mesmo ip. Serve para não
			// permitir mais de 1 usuário com mesma credencial
			if (!usuario.getIp().equals(request.getRemoteAddr())) {
				throw utilCorreiosException.exibirMensagem(
						MensagemID.SESSAO_ATIVA_APLICACAO, usuario.getNome());
			}
		}
		usuariosLogados.adicionar(usuario);
	}

	/**
	 * @param response
	 * @param e
	 * @throws IOException
	 * @throws ServletException
	 */
	private void redirecionaPaginaErro(HttpServletRequest request,
			HttpServletResponse response, Exception e) throws IOException,
			ServletException {
		String msgErro = extrairMensagensErro(e, request.getLocale());
		request.setAttribute(Constantes.ATTRIBUTE_ERROR_EXCEPTION, e);
		request.setAttribute(Constantes.ATTRIBUTE_ERROR_EXCEPTION_TYPE,
				e.getClass());
		request.setAttribute(Constantes.ATTRIBUTE_ERROR_MESSAGE, msgErro);
		request.setAttribute(Constantes.MENSAGEM_EXCECAO_SESSAO, msgErro);
		request.setAttribute(Constantes.ATTRIBUTE_ERROR_REQUEST_URI,
				request.getRequestURI());
		request.setAttribute(Constantes.ATTRIBUTE_ERROR_STATUS_CODE,
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		RequestDispatcher requestDispatcher = request
				.getRequestDispatcher("/erro.jsf");
		requestDispatcher.forward(request, response);
	}

	/**
	 * Extrai as mensagens de erro desde a exceção original.
	 * 
	 * @param e
	 * @param locale
	 * @return Msg de erro
	 */
	private String extrairMensagensErro(Exception e, Locale locale) {
		String msg = null;
		if (e.getClass().isAssignableFrom(CorreiosException.class)
				&& ((CorreiosException) e).getKey() != null
				&& !((CorreiosException) e).getKey().equals("")) {
			CorreiosException exception = (CorreiosException) e;
			msg = utilMensagem.getMensagem(locale, exception.getKey(),
					exception.getArgs());
		} else if (e.getClass()
				.isAssignableFrom(CorreiosRuntimeException.class)
				&& ((CorreiosRuntimeException) e).getKey() != null
				&& !((CorreiosRuntimeException) e).getKey().equals("")) {
			CorreiosRuntimeException exception = (CorreiosRuntimeException) e;
			msg = utilMensagem.getMensagem(locale, exception.getKey(),
					exception.getArgs());
		} else {
			msg = new UtilExcecao().getMensagem(e);
		}
		return msg;
	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		this.filterConfig = null;
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setUtilMensagem(UtilMensagem utilMensagem) {
		this.utilMensagem = utilMensagem;
	}

	public void setUtilCorreiosException(
			UtilCorreiosException utilCorreiosException) {
		this.utilCorreiosException = utilCorreiosException;
	}
}
