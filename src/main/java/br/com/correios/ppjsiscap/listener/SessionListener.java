package br.com.correios.ppjsiscap.listener;

import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.jboss.weld.context.http.HttpConversationContext;


/**
 * Classe responsável por definir o timeout da conversação
 * @author 80131085
 *
 */
@WebListener
public class SessionListener implements HttpSessionListener{
	
	private static final int MIL = 1000;
	private static final int SESSENTA = 60;
	
	@Inject private HttpConversationContext conversationContext;

	@Override
	public void sessionCreated(HttpSessionEvent evt) {
		String timeoutDaConversacao = evt.getSession().getServletContext().getInitParameter("timeout-conversacao");
		if (conversationContext != null && timeoutDaConversacao != null) {
            long DEFAULT_TIMEOUT = Long.valueOf(timeoutDaConversacao) * SESSENTA * SESSENTA * MIL;
            if (conversationContext.getDefaultTimeout() < DEFAULT_TIMEOUT){
                conversationContext.setDefaultTimeout(DEFAULT_TIMEOUT);
            }
        }
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		
		
	}

}
