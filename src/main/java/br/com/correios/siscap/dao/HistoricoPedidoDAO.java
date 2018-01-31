package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.correios.ppjsiscap.util.UtilLog;

public class HistoricoPedidoDAO {
	private Connection conexao;

	public HistoricoPedidoDAO(Connection c) {
		conexao = c;
	}
	
	public String findPedidoVinculado(String mcmcu) throws SQLException{
		String lista = "";
		if (mcmcu != null) {
			

			StringBuilder query = new StringBuilder(
					"select VIN_CO,	SDDOCO,	MCMCU_ORGAO_SOLICITANTE, VIN_DT_PEDIDO from vincula_pedido_erp where MCMCU_ORGAO_SOLICITANTE like ?");

			UtilLog.logger.debug(query);

			PreparedStatement stm = null;
			try {
				stm = conexao.prepareStatement(query.toString());
				stm.setString(1,"            ".substring(0, (12 - mcmcu.length()))	+ mcmcu);

				stm.executeQuery();
				ResultSet rs = stm.getResultSet();


				while (rs.next()) {

					lista = "'"+rs.getString("VIN_CO")+",'";

				}

			} finally {
				if (stm != null) {
					stm.close();

				}
			}
			
		}
		return lista;
	}
}
