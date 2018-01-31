package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.correios.ppjsiscap.util.UtilLog;

public class DesbloqueiaPedidoDAO {
	private Connection conexaoOracle;
	
	/**
	 * Construtor para chamadas para o banco oracle apenas
	 * 
	 * @param oracle
	 */
	public DesbloqueiaPedidoDAO(Connection oracle) {
		conexaoOracle = oracle;
	}
	
	public void liberaPedidosBloqueados() throws SQLException {

		String query = "UPDATE VW_F5542E01 set EAEDSP = 'V' "
				+ "where EAPID = 'SISCAP_ERP' "
				+ "and EAEDSP = 'R'";
				
		UtilLog.logger.debug(query);

		PreparedStatement stm = null;

		try {
			stm = conexaoOracle.prepareStatement(query);
			stm.executeQuery(query);

		} finally {
			if (stm != null) {
				stm.close();
			}
		}
	}
}
