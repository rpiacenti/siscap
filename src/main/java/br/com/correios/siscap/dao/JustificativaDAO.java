package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.siscap.model.ItemPedido;

public class JustificativaDAO {
	private Usuario usuario;

	private Connection conexaoOracle;


	public JustificativaDAO(Connection oracle, Usuario pUsuario) {
		// TODO Auto-generated constructor stub
		conexaoOracle = oracle;
		this.usuario = pUsuario;
	}
	
	public void insertJustificativa(List<ItemPedido> pedidos) throws SQLException {

		// UtilLog.logger.debug(query);

		PreparedStatement stm = null;
		for (ItemPedido itemPedido : pedidos) {

			if (itemPedido.getJustificativa() != null) {
				
				if(itemPedido.getJustificativa().indexOf("-") > -1){
					String[] aJust = itemPedido.getJustificativa().split("-");
					itemPedido.setJustificativa(aJust[0]);
				}

				String query = "select count(*) as totJus from PEDIDO_ITEM_JUSTIFICATIVA "
						+ "where PIJ_CO_EAAA25_PEDIDO = '" + itemPedido.getEAAA25() + "' " + "and PIJ_CO_LITM_ITEM = '"
						+ itemPedido.getEALITM() + "' ";

				try {
					stm = conexaoOracle.prepareStatement(query);

					ResultSet rs = stm.executeQuery();

					rs.next();
					
					if (rs.getInt("totJus") == 0 && itemPedido.getJustificativa() != null) {
						query = "INSERT INTO PEDIDO_ITEM_JUSTIFICATIVA (PIJ_CO_EAAA25_PEDIDO, PIJ_CO_LITM_ITEM, PIJ_TX_ITEM_JUSTIFICATIVA, PIJ_QT_ITEM_PEDIDO, PIJ_QT_ITEM_MEDIA) "
								+ "VALUES ('" + itemPedido.getEAAA25() + "','" + itemPedido.getEALITM() + "','"
								+ itemPedido.getJustificativa().concat(" - Justificado por: ")
										.concat(usuario.getLogin())
								+ "','" + itemPedido.getEAUORG() + "'," + itemPedido.getMediaSemetre() + ")";

						stm.executeQuery(query);

					} else {
						if( itemPedido.getJustificativa() != null){
							query = "UPDATE PEDIDO_ITEM_JUSTIFICATIVA SET " + "PIJ_TX_ITEM_JUSTIFICATIVA = '"
									+ itemPedido.getJustificativa().concat(" - Justificado por: ")
											.concat(usuario.getLogin())
									+ "', " + "PIJ_QT_ITEM_PEDIDO = '" + itemPedido.getEAUORG() + "', "
									+ " PIJ_QT_ITEM_MEDIA = " + itemPedido.getMediaSemetre()
									+ " where PIJ_CO_EAAA25_PEDIDO = '" + itemPedido.getEAAA25()
									+ "' and PIJ_CO_LITM_ITEM = '" + itemPedido.getEALITM() + "'";
							stm.executeQuery(query);
						}
						
					}

				} finally {
					if (stm != null) {
						stm.close();
					}
				}

			}
		}
	}
	
	public void deleteJustificativa(String pedido) throws SQLException {
		String query = "DELETE PEDIDO_ITEM_JUSTIFICATIVA WHERE EAAA25 = ?) ";
		// UtilLog.logger.debug(query);

		PreparedStatement stm = null;

		try {
			stm = conexaoOracle.prepareStatement(query);
			stm.setString(1, pedido);

			stm.executeQuery();
			stm.close();

		} finally {
			if (stm != null) {
				stm.close();
			}
		}

	}

}
