package br.com.correios.ppjsiscap.excecao;


/**
 * Classe utilitaria responsavel em manipular excecoes.
 * 
 * @author Arivaldo Junior
 */
public final class UtilExcecao {

	

	/**
	 * Retorna o texto do stacktrace da excecao.
	 * 
	 * @param e excecao
	 * @return texto do stacktrace.
	 */
	public  String getStackStrace(Throwable e) {
		StringBuffer buffer = new StringBuffer();

		if (e != null) {
			StackTraceElement traceElement = null;

			StackTraceElement[] stacktrace = e.getStackTrace();
			for (int line = 0; line < stacktrace.length; line++) {
				traceElement = stacktrace[line];
				buffer.append(traceElement.toString()).append("\r\n");
			}
		}
		return buffer.toString();
	}

	/**
	 * Retorna a mensagem de erro da excecao.
	 * 
	 * @param e excecao
	 * @return mensagem de erro da excecao.
	 */
	public  String getMensagem(Throwable e) {
		String mensagem = null;

		if (e != null) {
			mensagem = e.getMessage();
			if ("".equals(mensagem)) {
				mensagem = e.getCause() != null ? e.getCause().toString():null;
			}
		}
		return mensagem;
	}
}
