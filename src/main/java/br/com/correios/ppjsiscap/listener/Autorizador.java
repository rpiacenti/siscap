package br.com.correios.ppjsiscap.listener;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

@SuppressWarnings("serial")
public class Autorizador implements PhaseListener {

	@Override
	public void afterPhase(PhaseEvent event) {
		
		FacesContext context = event.getFacesContext();
		String pagina = context.getViewRoot().getViewId(); // -> /inicio/inicio.xhtml
		
		context = FacesContext.getCurrentInstance();
		
		//paginasSemAcesso[100,101,102]
		
		
		
		//pesquisar-cliente = 100;
		//arrayPaginasPublicas[pesquisar-cliente, cadastrar-cliente]
		
		//arrayUsuario[100,101,200]
		
		//Se pagina NÃO tiver dentro do meu arrayPaginasPublicas
			//Se pagina não tiver dentro do meu array no usuarioSiscap
				//Não deixa acessar
		
		/*if (usuarioSiscap == null) {
			NavigationHandler handler = context.getApplication().getNavigationHandler();
			handler.handleNavigation(context, null, "inicio");
			context.renderResponse();
		}*/

	}

	@Override
	public void beforePhase(PhaseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
