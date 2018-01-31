package br.com.correios.ppjsiscap.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.correios.ppjsiscap.seguranca.Usuario;


/**
 * Servlet implementation class SairServlet
 */
@SuppressWarnings("serial")
public class SairServlet extends HttpServlet {	
    
	@Inject
    private Usuario usuario;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		
		if(usuario != null && usuario.getSession() != null){
			usuario.getSession().invalidate();
		}else{
			HttpSession session = request.getSession(false);
			if(session != null){
				session.invalidate();
			}
		}
		response.sendRedirect(getInitParameter("urlLogoutCas"));					
	}

}
