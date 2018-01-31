package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.util.DataERP;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.excecao.SiscapException;
import br.com.correios.siscap.model.Auditoria;
import br.com.correios.siscap.model.HistoricoPedido;
import br.com.correios.siscap.model.ItemPedido;

public class ItemPedidoDAO {

	private Usuario usuario;

	private Connection conexaoOracle;

	private Connection conexaoSqlServer;

	/**
	 * Construtor para chamadas para o banco oracle apenas
	 * 
	 * @param oracle
	 */
	public ItemPedidoDAO(Connection oracle, Usuario pUsuario) {
		conexaoOracle = oracle;
		this.usuario = pUsuario;
	}

	/**
	 * Construtor para chamadas ao banco oracle e slqserver
	 * 
	 * @param oracle
	 * @param sqlServer
	 */
	public ItemPedidoDAO(Connection oracle, Connection sqlServer, Usuario pUsuario) {
		conexaoOracle = oracle;
		conexaoSqlServer = sqlServer;
		this.usuario = pUsuario;
	}

	public void onEdit(ItemPedido itemPedido) throws SQLException, SiscapException {

		long qry = this.findByCount(itemPedido.getEAAA25());
		if (qry == 0) {
			insert(itemPedido);
		} else {
			update(itemPedido);
		}

	}

	public void insert(ItemPedido itemPedido) throws SQLException {

		String query = "INSERT INTO VW_F5542E01 (EAKCO,EAMCU,EAMCU1,EATRDJ,EALITM,EAUORG,EAT001,EAT002,EAUOM,EAAA25,EAEDSP,EAUPMJ,EAUSER,EAPID,EAJOBN, EATDAY, EACKEDSP) "
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		UtilLog.logger.debug(query);

		PreparedStatement stm = null;
		if (itemPedido.getEAAA25() != null && itemPedido.getEALITM() != null
				&& Float.parseFloat(itemPedido.getEAUORG()) > 0 && itemPedido.getEAMCU1() != null
				&& itemPedido.getEAMCU() != null) {

			try {
				stm = conexaoOracle.prepareStatement(query);
				stm.setString(1, itemPedido.getEAKCO().trim());
				stm.setString(2, "            ".substring(0, (12 - itemPedido.getEAMCU().trim().length()))
						+ itemPedido.getEAMCU().trim());
				stm.setString(3, "            ".substring(0, (12 - itemPedido.getEAMCU1().trim().length()))
						+ itemPedido.getEAMCU1().trim());
				stm.setString(4, itemPedido.getEATRDJ());
				stm.setString(5, itemPedido.getEALITM());
				stm.setString(6, itemPedido.getEAUORG() + "0000");
				stm.setString(7, itemPedido.getEAT001());
				stm.setString(8, itemPedido.getEAT002());
				stm.setString(9, itemPedido.getEAUOM());
				stm.setString(10, itemPedido.getEAAA25());
				stm.setString(11, itemPedido.getEAEDSP());
				stm.setString(12, itemPedido.getEAUPMJ());
				stm.setString(13, itemPedido.getEAUSER());
				stm.setString(14, itemPedido.getEAPID());
				stm.setString(15, "SISCAP");
				stm.setString(16, itemPedido.getEATDAY());
				stm.setString(17, " ");
				stm.executeQuery();
				stm.close();

			} finally {
				if (stm != null) {
					stm.close();
				}
			}
		}

	}

	public void insertBatch(List<ItemPedido> pedidos, boolean alterar) throws SQLException, SiscapException  {

		final int batchSize = 1000;
		int count = 0;
		conexaoOracle.setAutoCommit(false);
		PreparedStatement stmDeleteVw = null;
		PreparedStatement stmDelete = null;
		PreparedStatement pstInsertVinculaPedido = null;
		PreparedStatement pstInsertVw = null;
		try {

			if (alterar) {

				String queryDeleteVw = "DELETE VW_F5542E01 " + " where EAAA25 = '" + pedidos.get(0).getEAAA25().trim()
						+ "' and EAEDSP = 'S' ";
				UtilLog.logger.debug(queryDeleteVw);
				stmDeleteVw = conexaoOracle.prepareStatement(queryDeleteVw);
				// stmDeleteVw.addBatch(queryDeleteVw);

				String queryDelete = "DELETE VINCULA_PEDIDO_ERP " + " where VIN_CO = '"
						+ pedidos.get(0).getEAAA25().trim() + "'";
				UtilLog.logger.debug(queryDelete);
				stmDelete = conexaoOracle.prepareStatement(queryDelete);
				// stmDelete.addBatch(queryDelete);
				
				stmDeleteVw.executeQuery();
				stmDelete.executeQuery();

				 this.setAuditoria(this.usuario.getMatricula(),
				 queryDelete, "VINCULA_PEDIDO_ERP", "ALTERAR PEDIDO",
				 pedidos.get(0).getEAAA25());

			}
			
			

			for (ItemPedido ped : pedidos) {
				if (count == 0) {
					DataERP dtERP = new DataERP(ped.getEATRDJ());
					String oraDt = "{ts '"+ dtERP.getDataOracle() +"'}";
					
//					String queryInsert = "INSERT INTO VINCULA_PEDIDO_ERP (VIN_CO,SDDOCO,MCMCU_ORGAO_SOLICITANTE,VIN_DT_PEDIDO) "
//							+ "VALUES(?,?,?,?)";
					
					String queryInsert = "INSERT INTO VINCULA_PEDIDO_ERP (VIN_CO,SDDOCO,MCMCU_ORGAO_SOLICITANTE,VIN_DT_PEDIDO) "
							+ "VALUES('"+ped.getEAAA25().trim()+"',999999999,'"+"            ".substring(0, (12 - ped.getEAMCU1().trim().length()))
							+ ped.getEAMCU1().trim()+"',"+ oraDt +")";

					UtilLog.logger.debug(queryInsert);

					pstInsertVinculaPedido = conexaoOracle.prepareStatement(queryInsert);

					pstInsertVinculaPedido.executeQuery();

				}

				String queryInsertVw = "INSERT INTO VW_F5542E01 (EAKCO,EAMCU,EAMCU1,EATRDJ,EALITM,EAUORG,EAT001,EAT002,EAUOM,EAAA25,EAEDSP,EAUPMJ,EAUSER,EAPID,EAJOBN, EACKEDSP )"
						+ " VALUES('" + ped.getEAKCO().trim() + "','"
						+ "             ".substring(0, 12 - ped.getEAMCU().trim().length()) + ped.getEAMCU().trim()
						+ "','" + "             ".substring(0, 12 - ped.getEAMCU1().trim().length())
						+ ped.getEAMCU1().trim() + "'," + Long.parseLong(ped.getEATRDJ()) + ",'" + ped.getEALITM()
						+ "'," + Long.parseLong(ped.getEAUORG() + "0000") + ",'" + ped.getEAT001() + "','"
						+ ped.getEAT002() + "','" + ped.getEAUOM() + "','" + ped.getEAAA25() + "','" + ped.getEAEDSP()
						+ "'," + Long.parseLong(ped.getEAUPMJ()) + ",'" + ped.getEAUSER() + "','" + ped.getEAPID()
						+ "','" + ped.getEAJOBN() + "','" + " " + "')";


				pstInsertVw = conexaoOracle.prepareStatement(queryInsertVw);

				pstInsertVw.executeQuery();
				
				this.setAuditoria(this.usuario.getMatricula(), queryInsertVw,
				 "VW_F5542E01", "ALTERAR/INSERT PEDIDO",
				 ped.getEAAA25());

				++count;
			}
		} finally {

			if (stmDeleteVw != null) {
				stmDeleteVw.close();
			}
			if (stmDelete != null) {
				stmDelete.close();
			}

			if (pstInsertVw != null) {
				pstInsertVw.close();
			}
			if (pstInsertVinculaPedido != null) {
				pstInsertVinculaPedido.close();
			}
		}
	}

	public void insertBatchValidacao(List<ItemPedido> pedidos, boolean alterar) throws SQLException, SiscapException {

		final int batchSize = 1000;
		int count = 0;
		conexaoOracle.setAutoCommit(false);
		PreparedStatement stmDeleteVw = null;
		PreparedStatement pstInsertVw = null;
		try {

			if (alterar) {

				String queryDeleteVw = "DELETE VW_F5542E01 " + " where EAAA25 = '" + pedidos.get(0).getEAAA25().trim()
						+ "' and EAEDSP = 'R' ";
				UtilLog.logger.debug(queryDeleteVw);
				stmDeleteVw = conexaoOracle.prepareStatement(queryDeleteVw);
				// stmDeleteVw.addBatch(queryDeleteVw);

				stmDeleteVw.executeQuery();

				this.setAuditoria(
						this.usuario.getMatricula(),
						queryDeleteVw, "VW_F5542E01", "VALIDACAO PEDIDO", pedidos.get(0).getEAAA25());

			}

			for (ItemPedido ped : pedidos) {

				String queryInsertVw = "INSERT INTO VW_F5542E01 (EAKCO,EAMCU,EAMCU1,EATRDJ,EALITM,EAUORG,EAT001,EAT002,EAUOM,EAAA25,EAEDSP,EAUPMJ,EAUSER,EAPID,EAJOBN, EACKEDSP )"
						+ " VALUES('" + ped.getEAKCO().trim() + "','"
						+ "             ".substring(0, 12 - ped.getEAMCU().trim().length()) + ped.getEAMCU().trim()
						+ "','" + "             ".substring(0, 12 - ped.getEAMCU1().trim().length())
						+ ped.getEAMCU1().trim() + "'," + Long.parseLong(ped.getEATRDJ()) + ",'" + ped.getEALITM()
						+ "'," + Long.parseLong(ped.getEAUORG() + "0000") + ",'" + ped.getEAT001() + "','"
						+ ped.getEAT002() + "','" + ped.getEAUOM() + "','" + ped.getEAAA25() + "','R',"
						+ Long.parseLong(ped.getEAUPMJ()) + ",'" + ped.getEAUSER() + "','" + ped.getEAPID() + "','"
						+ ped.getEAJOBN() + "','" + " " + "')";

				UtilLog.logger.debug(ped.getEAKCO() + " - " + ped.getEAMCU() + " - " + ped.getEAMCU1() + " - "
						+ ped.getEATRDJ() + " - " + ped.getEALITM() + " - " + ped.getEAUORG() + " - " + ped.getEAT001()
						+ " - " + ped.getEAT002() + " - " + ped.getEAUOM() + " - " + ped.getEAAA25() + " - R - "
						+ ped.getEAUPMJ() + " - " + ped.getEAUSER() + " - " + ped.getEAPID() + " - " + ped.getEAJOBN());
				UtilLog.logger.debug("Query Batch: " + queryInsertVw);

				pstInsertVw = conexaoOracle.prepareStatement(queryInsertVw);
				pstInsertVw.executeQuery();
				this.setAuditoria(
						this.usuario.getMatricula(),
						queryInsertVw, "VW_F5542E01", "VALIDACAO PEDIDO", pedidos.get(0).getEAAA25());

				++count;
			}
		} finally {

			if (stmDeleteVw != null) {
				stmDeleteVw.close();
			}

			if (pstInsertVw != null) {
				pstInsertVw.close();
			}

		}
	}

	public void update(ItemPedido itemPedido) throws SQLException, SiscapException {

		String query = "UPDATE VW_F5542E01 " + "set " + "EAKCO ='" + itemPedido.getEAKCO().trim() + "', " + "EAMCU = '"
				+ "            ".substring(0, (12 - itemPedido.getEAMCU().trim().length()))
				+ itemPedido.getEAMCU().trim() + "'," + "EAMCU1 = '"
				+ "            ".substring(0, (12 - itemPedido.getEAMCU1().trim().length()))
				+ itemPedido.getEAMCU1().trim() + "'," + "EATRDJ =" + itemPedido.getEATRDJ() + "," + "EALITM = '"
				+ itemPedido.getEALITM() + "'," + "EAUORG = " + itemPedido.getEAUORG() + "0000" + "," + "EAT001 = '"
				+ itemPedido.getEAT001() + "'," + "EAT002 = '" + itemPedido.getEAT002() + "'," + "EAUOM = '"
				+ itemPedido.getEAUOM() + "'," + "EAAA25 = '" + itemPedido.getEAAA25().trim() + "'," + "EAEDSP = '"
				+ itemPedido.getEAEDSP() + "'," + "EAUPMJ = " + itemPedido.getEAUPMJ() + "," + "EAUSER = '"
				+ itemPedido.getEAUSER() + "'," + "EAPID = '" + itemPedido.getEAPID() + "'," + "EAJOBN = '"
				+ itemPedido.getEAJOBN() + "'," + "EATDAY = '" + itemPedido.getEATDAY() + "'" + "where EAAA25 = '"
				+ itemPedido.getEAAA25() + "' " + "and  EALITM = " + itemPedido.getEALITM();
		UtilLog.logger.debug(query);

		PreparedStatement stm = null;

		try {
			stm = conexaoOracle.prepareStatement(query);
			stm.executeQuery(query);
			this.setAuditoria(this.usuario.getMatricula(),
					query, "VW_F5542E01", "ATUALIZACAO DE ITEM DO PEDIDO", itemPedido.getEAAA25().trim());
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
	}

	public void cancelItemPedido(String pedido, String item) throws SQLException, SiscapException {

		DataERP dtERP = new DataERP();

		String query = "UPDATE VW_F5542E01 set EAEDSP = 'D', EAUPMJ = " + dtERP.getDataERPToday()
				+ " where trim(EAAA25) = trim('" + pedido + "') and  EALITM = '" + item + "' ";

		UtilLog.logger.debug(query);
		PreparedStatement stm = null;
		try {
			stm = conexaoOracle.prepareStatement(query);
			stm.executeQuery(query);
			this.setAuditoria(
					this.usuario.getMatricula(),
					query, "VW_F5542E01", "CANCELAMENTO DE ITEM DO PEDIDO", pedido);
		} finally {
			if (stm != null) {
				stm.close();
			}

		}

	}

	public void cancelPedido(String pedido) throws ClassNotFoundException, SQLException, SiscapException {

		DataERP dtERP = new DataERP();

		String query = "UPDATE VW_F5542E01 set EAEDSP = 'C', EAUPMJ = ?  where EAAA25 = ? and EAEDSP <> 'Y'";
		UtilLog.logger.debug(query);

		PreparedStatement stm = null;
		try {
			stm = conexaoOracle.prepareStatement(query);
			stm.setString(1, dtERP.getDataERPToday());
			stm.setString(2, pedido);
			stm.executeQuery();
			this.setAuditoria(this.usuario.getMatricula(),
					query, "VW_F5542E01", "CANCELAMENTO DO PEDIDO", pedido);
		} finally {
			if (stm != null) {
				stm.close();
			}
		}

	}

	public void changeStatusPedido(String pedido, String statusAtual, String statusNovo)
			throws SQLException, SiscapException {

		DataERP dtERP = new DataERP();

		String currStatus = null;
		if (statusAtual.equals("C")) { // Muda de C para R
			currStatus = statusNovo;
		}
		if (statusAtual.equals("R")) { // Muda de R para V
			currStatus = statusNovo;
		}
		if (statusAtual.equals("V")) { // Muda de V para R
			currStatus = statusNovo;
		}
		if (statusAtual.equals(" ")) { // Muda para branco quando for R
			currStatus = statusNovo;
		}

		String query = "UPDATE VW_F5542E01 set EAEDSP = '" + currStatus + "' , EAUPMJ = '" + dtERP.getDataERPToday()
				+ "' where EAAA25 = '" + pedido + "' and EAEDSP not in ('S','D','Y',' ')";

		UtilLog.logger.debug(query);

		PreparedStatement stm = null;
		try {
			stm = conexaoOracle.prepareStatement(query);

			stm.executeQuery();
			this.setAuditoria(
					this.usuario.getMatricula(),
					query, "VW_F5542E01", "MUDANCA DE STATUS DO PEDIDO: " + statusAtual + " - " + currStatus, pedido);
		} finally {
			if (stm != null) {
				stm.close();
			}
		}

	}

	public void delete(String pedido) throws SQLException, SiscapException {

		String query = "DELETE VW_F5542E01  where EAAA25 = ? and EAEDSP = 'S' ";
		UtilLog.logger.debug(query);

		PreparedStatement stm = null;

		stm = conexaoOracle.prepareStatement(query);
		stm.setString(1, pedido);
		stm.executeQuery();
		
		this.setAuditoria(this.usuario.getMatricula(),
				query, "VW_F5542E01", "APAGOU PEDIDO TABELA CAPTACAO", pedido);

	}

	public long findByCount(String pedido) throws SQLException {

		PreparedStatement stm = null;
		long qtd = 0L;

		try {
			if ((pedido != null) && (pedido != "")) {

				String query = "select count(*) as NUMREC from VW_F5542E01 where EAEDSP = 'S' " + "and EAAA25 = '"
						+ pedido + "' ";
				UtilLog.logger.debug("Query count: " + query);

				stm = conexaoOracle.prepareStatement(query);
				ResultSet rs = stm.executeQuery();
				rs.next();
				qtd = rs.getLong("NUMREC");
				rs.close();

			}

		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		return qtd;
	}

	public String findByNumeroPedido(String mcu, String tipoPedido) throws SQLException {

		PreparedStatement stm = null;

		String eaaa25 = null;
		try {
			if ((tipoPedido != null) && (tipoPedido != "")) {
				if ((mcu != null) && (mcu != "0")) {
					String query = "select EAAA25 from VW_F5542E01 where EAEDSP = 'S' "
							+ "and EAMCU1 = ? and EAT002 = ? and EAPID = 'SISCAP_ERP'";
					UtilLog.logger.debug("Query count: " + query);

					stm = conexaoOracle.prepareStatement(query);
					stm.setString(1, mcu);
					stm.setString(2, tipoPedido);
					ResultSet rs = stm.executeQuery();

					if (rs.next()) {
						eaaa25 = rs.getString("EAAA25");
					} else {
						eaaa25 = "";
					}

					rs.close();

				}
			}

		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		return eaaa25;
	}

	public long findByOpenPedidoMCU(String mcu, String tipoPedido, String status) throws SQLException {

		PreparedStatement stm = null;
		long qtd = 0L;
		try {
			if (mcu != null && !mcu.equals("0")) {
				String query = "select count(*) AS NUMREG from VW_F5542E01 where EAEDSP in (" + status + ") "
						+ "and EAMCU1 = lpad(trim(?),12,' ') and EAT002 = ? and EAPID = 'SISCAP_ERP'";
				UtilLog.logger.debug("Query count: " + query);
				System.out.println(query);
				stm = conexaoOracle.prepareStatement(query);
				stm.setString(1, mcu);
				stm.setString(2, tipoPedido);
				ResultSet rs = stm.executeQuery();
				rs.next();
				qtd = rs.getLong("NUMREG");
				rs.close();

			}

		} finally {
			if (stm != null) {
				stm.close();
			}

		}
		return qtd;
	}

	public boolean findByPedidoStatus(String keyPED, String keyStatus) throws SQLException {

		String query = "SELECT EAKCO,EAMCU,EAMCU1,EATRDJ,EALITM,EAUORG/10000 AS EAUORG,EAT001,EAT002,EAUOM,EAAA25,EAEDSP,EAUPMJ,EAUSER,EAPID,EAJOBN "
				+ "FROM VW_F5542E01 " + "WHERE EAAA25 = '" + keyPED + "'  and EAEDSP in (" + keyStatus + ") "
				+ "and EAPID = 'SISCAP_ERP'";

		UtilLog.logger.debug("Recupera Status Pedido: findByPedidoStatus");
		UtilLog.logger.debug(query);

		PreparedStatement stm = null;
		try {

			stm = conexaoOracle.prepareStatement(query);
			ResultSet rs = stm.executeQuery();
			if (rs.next()) {
				rs.close();
				return true;
			}

		} finally {
			if (stm != null) {
				stm.close();
			}
		}

		return false;

	}

	public List<ItemPedido> findByPedido(String pedido, String status, String valorMCU) {
		ArrayList<ItemPedido> pedidos = new ArrayList<ItemPedido>();
		
		StringBuilder query = new StringBuilder("SELECT A.EAUKID, A.EAKCO, A.EAMCU, A.EAMCU1, A.EATRDJ,A.EALITM, ");
		query.append("A.EAUORG / 10000 AS EAUORG, A.EAT001, A.EAT002, A.EAUOM, A.EAAA25, A.EAEDSP, A.EAUPMJ, ");
		query.append("A.EAUSER, A.EAPID, A.EAJOBN, B.IMITM, B.IMDSC1, B.IMUOM1, B.IMUOM6, D.UMCONV, D.UMRUM, D.UMUM, ");
		query.append("CASE WHEN B.IMUOM6 = B.IMUOM1 THEN 1 ");
		query.append("WHEN D.UMCNV1 IS NULL    THEN 1 ");
		query.append("ELSE D.UMCNV1/10000000 ");
		query.append("END AS UMCNV1, "); 
		query.append("E.PIJ_TX_ITEM_JUSTIFICATIVA AS JUSTIFICATIVA, "); 
		query.append("E.PIJ_QT_ITEM_MEDIA AS MEDIASEMESTRE, ");
		query.append("F.EBD200 AS CANCELAMENTO, ");
		query.append("G.SDNXTR, G.SDLTTR ");
		query.append("FROM VW_F5542E01 A ");
		query.append("LEFT OUTER JOIN VW_F5542E02 F ON (A.EAUKID = F.EBUKID) ");
		query.append("LEFT OUTER JOIN VW_F4101 B ON A.EALITM = B.IMLITM ");
		query.append("LEFT OUTER JOIN VW_F41002 D ON (B.IMITM= D.UMITM AND A.EAUOM = D.UMUM AND B.IMUOM1 = D.UMRUM) ");
		query.append("LEFT OUTER JOIN PEDIDO_ITEM_JUSTIFICATIVA E ON (A.EAAA25 = E.PIJ_CO_EAAA25_PEDIDO AND A.EALITM = E.PIJ_CO_LITM_ITEM) "); 
		query.append("LEFT OUTER JOIN VW_F42119 G ON (G.SDVR01 = A.EAAA25 and G.SDNXTR = 999 and G.SDLTTR <> 620 and G.SDMCU = A.EAMCU and G.SDTRDJ >= A.EATRDJ) ");
		query.append("WHERE A.EAAA25 = RPAD(?,25,' ')  ");
//		query.append("and A.EAEDSP in (?)  ");
		query.append("ORDER BY A.EAT001, B.IMDSC1 ");

//		StringBuilder query = new StringBuilder(
//				"SELECT A.EAUKID, A.EAKCO, A.EAMCU, A.EAMCU1, A.EATRDJ, A.EALITM, EAUORG/10000 AS EAUORG, A.EAT001, A.EAT002, A.EAUOM, ");
//		query.append(
//				"A.EAAA25, A.EAEDSP, A.EAUPMJ, A.EAUSER, A.EAPID, A.EAJOBN, B.IMDSC1, B.IMUOM1, B.IMUOM6, C.COUNCS/10000 as COUNCS, ");
//		query.append("CASE WHEN B.IMUOM6 = B.IMUOM1 THEN 1 ");
//		query.append("WHEN D.UMCNV1 IS NULL    THEN 1 ");
//		query.append("ELSE D.UMCNV1/10000000 ");
//		query.append("END AS UMCNV1, ");
//		query.append("D.UMCONV, D.UMRUM, D.UMUM, ");
//		query.append("E.PIJ_TX_ITEM_JUSTIFICATIVA AS JUSTIFICATIVA, E.PIJ_QT_ITEM_MEDIA AS MEDIASEMESTRE ");
//		query.append("FROM VW_F5542E01 A ");
//		query.append("LEFT OUTER JOIN PEDIDO_ITEM_JUSTIFICATIVA E ON (A.EAAA25 = E.PIJ_CO_EAAA25_PEDIDO AND A.EALITM = E.PIJ_CO_LITM_ITEM) ");
//		query.append("LEFT OUTER JOIN VW_F4101 B ON A.EALITM = B.IMLITM ");
//		query.append("LEFT OUTER JOIN VW_F4105 C ON B.IMITM = C.COITM ");
//		query.append("LEFT OUTER JOIN VW_F41002 D ON (B.IMITM= D.UMITM AND A.EAUOM= D.UMUM AND B.IMUOM1 = D.UMRUM) ");
//		query.append("WHERE A.EAAA25 like ? ");
//		query.append("AND C.COMCU = lpad(trim(?),12,' ') ");
//	//	query.append("and A.EAEDSP in (?)  ");
//		query.append("AND C.COLEDG = '02' ");
//		query.append("ORDER BY A.EAT001, B.IMDSC1 ");

		UtilLog.logger.debug(query.toString());
		PreparedStatement stm = null;
		try {

			stm = conexaoOracle.prepareStatement(query.toString());
			stm.setString(1, pedido + "%");
		//	stm.setString(2, valorMCU.trim());
		//	stm.setString(2, status);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ItemPedido item = new ItemPedido();
				item.setEAKCO(rs.getString("EAKCO"));
				item.setEAMCU(rs.getString("EAMCU"));
				item.setEAMCU1(rs.getString("EAMCU1"));
				item.setEATRDJ(rs.getString("EATRDJ"));
				item.setEALITM(rs.getString("EALITM"));
				item.setEAUORG(rs.getString("EAUORG"));
				item.setEAT001(rs.getString("EAT001"));
				item.setEAT002(rs.getString("EAT002"));
				item.setEAUOM(rs.getString("EAUOM"));
				item.setEAAA25(rs.getString("EAAA25"));
				item.setEAEDSP(rs.getString("EAEDSP"));
				item.setEAUPMJ(rs.getString("EAUPMJ"));
				item.setEAUPMJ(rs.getString("EAUPMJ"));
				item.setEAUSER(rs.getString("EAUSER"));
				item.setEAPID(rs.getString("EAPID"));
				item.setEAJOBN(rs.getString("EAJOBN"));
				//item.setCOUNCS(rs.getString("COUNCS"));
				item.setIMDSC1(rs.getString("IMDSC1"));
				item.setUMCONV(rs.getString("UMCONV"));
				item.setUMRUM(rs.getString("IMUOM1"));
				item.setUMCNV1(rs.getString("UMCNV1"));
				item.setUMUM(rs.getString("IMUOM6"));
				item.setJustificativa(rs.getString("JUSTIFICATIVA"));
				item.setMediaSemetre(rs.getString("MEDIASEMESTRE"));
				item.setCancelamento(rs.getString("CANCELAMENTO") + " / " + rs.getString("SDNXTR") + " / " + rs.getString("SDLTTR"));
				if(item.getEAEDSP().indexOf("C") > -1){
					item.setCancelamento("Item Cancelado na validação!");
				}else{
					if(item.getCancelamento().indexOf("null / null / null") > -1){
						item.setCancelamento(null);
					}
				}
				item.setUnidMedFormatada(null);

				double vCustUnit = 0.00; //this.findByCustoUnitario(rs.getString("IMITM"));
				// double vFatConv = Double.parseDouble(item.getUmconv());
				double vFatConv = Double.parseDouble(item.getUMCNV1());
				double vUnitario = 0;

				vUnitario = vCustUnit * vFatConv;

				item.setValorUnitario(Double.toString(vUnitario));

				pedidos.add(item);
			}
			if (stm != null) {
				stm.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pedidos;

	}
	
	public Double findByCustoUnitario(String itm, String mcucia){
		
		PreparedStatement stmCust = null;
		
		double vCustUnit = 0.00;
		
		
		String sQuery = "Select COUNCS/10000 as COUNCS from VW_F4105 where COMCU = lpad(trim('" + mcucia + "'),12,' ') and COITM = " + itm + " and COLEDG = '02' ";
		
				
		UtilLog.logger.debug(sQuery);
		//PreparedStatement stm = null;
		try {
			
			stmCust = conexaoOracle.prepareStatement(sQuery);
			ResultSet rsCust = stmCust.executeQuery();
			
			while (rsCust.next()) {
				vCustUnit = Double.parseDouble(rsCust.getString("COUNCS"));
				break;
			}
			if (stmCust != null) {
				stmCust.close();
			}
		} catch (SQLException e) {
			e.getMessage();
		}
		return vCustUnit;

	}
	
	public List<ItemPedido> findByPedidoSolicitado(String pedido, String status, String valorMCU) {
		ArrayList<ItemPedido> pedidos = new ArrayList<ItemPedido>();
		
		StringBuilder query = new StringBuilder("SELECT A.EAUKID, A.EAKCO, A.EAMCU, A.EAMCU1, A.EATRDJ,A.EALITM, ");
		query.append("A.EAUORG / 10000 AS EAUORG, A.EAT001, A.EAT002, A.EAUOM, A.EAAA25, A.EAEDSP, A.EAUPMJ, ");
		query.append("A.EAUSER, A.EAPID, A.EAJOBN, B.IMITM, B.IMDSC1, B.IMUOM1, B.IMUOM6, D.UMCONV, D.UMRUM, D.UMUM, ");
		query.append("CASE WHEN B.IMUOM6 = B.IMUOM1 THEN 1 ");
		query.append("WHEN D.UMCNV1 IS NULL    THEN 1 ");
		query.append("ELSE D.UMCNV1/10000000 ");
		query.append("END AS UMCNV1, "); 
		query.append("E.PIJ_TX_ITEM_JUSTIFICATIVA AS JUSTIFICATIVA, "); 
		query.append("E.PIJ_QT_ITEM_MEDIA AS MEDIASEMESTRE, ");
		query.append("F.EBD200 AS CANCELAMENTO, ");
		query.append("G.SDNXTR, G.SDLTTR ");
		query.append("FROM VW_F5542E01 A ");
		query.append("LEFT OUTER JOIN VW_F5542E02 F ON (A.EAUKID = F.EBUKID) ");
		query.append("LEFT OUTER JOIN VW_F4101 B ON A.EALITM = B.IMLITM ");
		query.append("LEFT OUTER JOIN VW_F41002 D ON (B.IMITM= D.UMITM AND A.EAUOM = D.UMUM AND B.IMUOM1 = D.UMRUM) ");
		query.append("LEFT OUTER JOIN PEDIDO_ITEM_JUSTIFICATIVA E ON (A.EAAA25 = E.PIJ_CO_EAAA25_PEDIDO AND A.EALITM = E.PIJ_CO_LITM_ITEM) "); 
		query.append("LEFT OUTER JOIN VW_F42119 G ON (G.SDVR01 = A.EAAA25 and G.SDNXTR = 999 and G.SDLTTR <> 620 and G.SDMCU = A.EAMCU and G.SDTRDJ >= A.EATRDJ) ");
		query.append("WHERE A.EAAA25 = RPAD(?,25,' ')  ");
//		query.append("and A.EAEDSP in (?)  ");
		query.append("ORDER BY A.EAT001, B.IMDSC1 ");

//		StringBuilder query = new StringBuilder(
//				"SELECT A.EAUKID, A.EAKCO, A.EAMCU, A.EAMCU1, A.EATRDJ, A.EALITM, EAUORG/10000 AS EAUORG, A.EAT001, A.EAT002, A.EAUOM, ");
//		query.append(
//				"A.EAAA25, A.EAEDSP, A.EAUPMJ, A.EAUSER, A.EAPID, A.EAJOBN, B.IMDSC1, B.IMUOM1, B.IMUOM6, C.COUNCS/10000 as COUNCS, ");
//		query.append("CASE WHEN B.IMUOM6 = B.IMUOM1 THEN 1 ");
//		query.append("WHEN D.UMCNV1 IS NULL    THEN 1 ");
//		query.append("ELSE D.UMCNV1/10000000 ");
//		query.append("END AS UMCNV1, ");
//		query.append("D.UMCONV, D.UMRUM, D.UMUM, ");
//		query.append("E.PIJ_TX_ITEM_JUSTIFICATIVA AS JUSTIFICATIVA, E.PIJ_QT_ITEM_MEDIA AS MEDIASEMESTRE ");
//		query.append("FROM VW_F5542E01 A ");
//		query.append("LEFT OUTER JOIN PEDIDO_ITEM_JUSTIFICATIVA E ON (A.EAAA25 = E.PIJ_CO_EAAA25_PEDIDO AND A.EALITM = E.PIJ_CO_LITM_ITEM) ");
//		query.append("LEFT OUTER JOIN VW_F4101 B ON A.EALITM = B.IMLITM ");
//		query.append("LEFT OUTER JOIN VW_F4105 C ON B.IMITM = C.COITM ");
//		query.append("LEFT OUTER JOIN VW_F41002 D ON (B.IMITM= D.UMITM AND A.EAUOM= D.UMUM AND B.IMUOM1 = D.UMRUM) ");
//		query.append("WHERE A.EAAA25 like ? ");
//		query.append("AND C.COMCU = lpad(trim(?),12,' ') ");
//		query.append("and A.EAEDSP in (?)  ");
//		query.append("AND C.COLEDG = '02' ");
//		query.append("ORDER BY A.EAT001, B.IMDSC1 ");

		UtilLog.logger.debug(query.toString());
		PreparedStatement stm = null;
		try {

			stm = conexaoOracle.prepareStatement(query.toString());
			stm.setString(1, pedido);
			//stm.setString(2, valorMCU);
		//	stm.setString(3, status);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ItemPedido item = new ItemPedido();
				item.setEAKCO(rs.getString("EAKCO"));
				item.setEAMCU(rs.getString("EAMCU"));
				item.setEAMCU1(rs.getString("EAMCU1"));
				item.setEATRDJ(rs.getString("EATRDJ"));
				item.setEALITM(rs.getString("EALITM"));
				item.setEAUORG(rs.getString("EAUORG"));
				item.setEAT001(rs.getString("EAT001"));
				item.setEAT002(rs.getString("EAT002"));
				item.setEAUOM(rs.getString("EAUOM"));
				item.setEAAA25(rs.getString("EAAA25"));
				item.setEAEDSP(rs.getString("EAEDSP"));
				item.setEAUPMJ(rs.getString("EAUPMJ"));
				item.setEAUPMJ(rs.getString("EAUPMJ"));
				item.setEAUSER(rs.getString("EAUSER"));
				item.setEAPID(rs.getString("EAPID"));
				item.setEAJOBN(rs.getString("EAJOBN"));
	//			item.setCOUNCS(rs.getString("COUNCS"));
				item.setIMDSC1(rs.getString("IMDSC1"));
				item.setUMCONV(rs.getString("UMCONV"));
				item.setUMRUM(rs.getString("IMUOM1"));
				item.setUMCNV1(rs.getString("UMCNV1"));
				item.setUMUM(rs.getString("IMUOM6"));
				item.setJustificativa(rs.getString("JUSTIFICATIVA"));
				item.setMediaSemetre(rs.getString("MEDIASEMESTRE"));
				item.setCancelamento(rs.getString("CANCELAMENTO") + " / " + rs.getString("SDNXTR") + " / " + rs.getString("SDLTTR"));
				if(item.getEAEDSP().indexOf("C") > -1){
					item.setCancelamento("Item Cancelado na validação!");
				}else{
					if(item.getCancelamento().indexOf("null / null / null") > -1){
						item.setCancelamento(null);
					}
				}
				item.setUnidMedFormatada(null);

				double vCustUnit = this.findByCustoUnitario(rs.getString("IMITM"), valorMCU); //0.00;//Double.parseDouble(item.getCOUNCS());
				// double vFatConv = Double.parseDouble(item.getUmconv());
				double vFatConv = Double.parseDouble(item.getUMCNV1());
				double vUnitario = 0;

				vUnitario = vCustUnit * vFatConv;

				item.setValorUnitario(Double.toString(vUnitario));

				pedidos.add(item);
			}
			if (stm != null) {
				stm.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pedidos;

	}

	public List<ItemPedido> findByStatusPedido(String status) throws SQLException {
		ArrayList<ItemPedido> pedidos = new ArrayList<ItemPedido>();

		String query = "SELECT distinct A.EAMCU,A.EAMCU1,A.EATRDJ,A.EAAA25, A.EAT002, A.EAEDSP, A.EAPID, B.MCDL01, B.MCRP01 "
				+ "FROM VW_F5542E01 A, vw_F0006 B " + "WHERE A.EAMCU1 = B.MCMCU "
				+ "and A.EAAA25 in (select distinct EAAA25 from VW_F5542E01 where EAEDSP in (" + status + ")) "
				+ " and A.EAEDSP in (" + status + ")  and EAPID = 'SISCAP_ERP'";

		PreparedStatement stm = null;
		try {

			stm = conexaoOracle.prepareStatement(query);
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {

				ItemPedido validador = new ItemPedido();

				DataERP dERP = new DataERP(rs.getString("EATRDJ"));

				validador.setEAMCU(rs.getString("EAMCU"));
				validador.setEAMCU1(rs.getString("EAMCU1"));
				validador.setMCDSC1(rs.getString("MCDL01"));
				validador.setMCRP01(rs.getString("MCRP01"));
				validador.setEAAA25(rs.getString("EAAA25"));
				validador.setEATRDJ(dERP.getDataGregoriana());
				// tempValModel.setCatepedido(rs.getString("EAT001"));
				validador.setEAT002(rs.getString("EAT002"));
				// tempValModel.setStatuspedido(rs.getString("EAEDSP"));

				validador.setDescricaoEAEDSP(getStatusDescricao(rs.getString("EAEDSP"), rs.getString("EAPID")));

				pedidos.add(validador);

			}
			rs.close();

		} finally {
			if (stm != null) {
				stm.close();
			}
		}

		return pedidos;
	}

	public List<ItemPedido> findByPedidoMCU(String mcu, String status) throws SQLException {
		List<ItemPedido> pedidos = new ArrayList<ItemPedido>();

		String query = "SELECT distinct A.EAMCU,A.EAMCU1,A.EATRDJ,A.EAAA25, A.EAT002, A.EAEDSP, A.EAPID, B.MCDL01, B.MCRP01 "
				+ "FROM VW_F5542E01 A, vw_F0006 B " 
				+ "WHERE A.EAMCU1 = lpad(trim(?),12,' ') "
				+ "and A.EAMCU1 = B.MCMCU "
				+ "and A.EAAA25 in (select distinct EAAA25 from VW_F5542E01 where EAEDSP in(" + status + ")) "
				+ " and A.EAEDSP in (" + status + ") and EAPID = 'SISCAP_ERP'";
		UtilLog.logger.debug("Recupera Pedido: findByPedidoMCU" + query);

		PreparedStatement stm = null;
		try {

			stm = conexaoOracle.prepareStatement(query);
			stm.setString(1, mcu);
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				ItemPedido item = new ItemPedido();

				DataERP dERP = new DataERP(rs.getString("EATRDJ"));

				item.setEAMCU(rs.getString("EAMCU"));
				item.setEAMCU1(rs.getString("EAMCU1"));
				item.setIMDSC1(rs.getString("MCDL01"));
				item.setEAAA25(rs.getString("EAAA25"));
				item.setEATRDJ(dERP.getDataGregoriana());
				item.setEAT002(rs.getString("EAT002"));
				item.setEAEDSP(rs.getString("EAEDSP"));
				item.setMCDSC1(rs.getNString("MCDL01"));
				item.setMCRP01(rs.getNString("MCRP01"));

				item.setDescricaoEAEDSP(getStatusDescricao(rs.getString("EAEDSP"), rs.getString("EAPID")));

				pedidos.add(item);

			}
			rs.close();

		} finally {
			if (stm != null) {
				stm.close();
			}
		}

		return pedidos;
	}

	public List<ItemPedido> findByPedidoValidacao(String status) throws SQLException {
		List<ItemPedido> pedidos = new ArrayList<ItemPedido>();

		String query = "SELECT distinct A.EAMCU, A.EAMCU1, A.EATRDJ, A.EAAA25, A.EAT002, A.EAEDSP, A.EAPID, B.MCDL01, B.MCRP01 "
				+ "FROM VW_F5542E01 A, vw_F0006 B " + "WHERE A.EAMCU1 = B.MCMCU and A.EAAA25 in "
				+ "(select distinct EAAA25 from VW_F5542E01 where EAEDSP in(" + status + "))" + " and A.EAEDSP in ("
				+ status + ")  and EAPID = 'SISCAP_ERP' ORDER BY B.MCDL01";

		UtilLog.logger.debug("Recupera Pedido: findByPedidoMCU" + query);

		PreparedStatement stm = null;
		try {

			stm = conexaoOracle.prepareStatement(query);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				ItemPedido item = new ItemPedido();

				DataERP dERP = new DataERP(rs.getString("EATRDJ"));

				item.setEAMCU(rs.getString("EAMCU"));
				item.setEAMCU1(rs.getString("EAMCU1"));
				item.setIMDSC1(rs.getString("MCDL01"));
				item.setEAAA25(rs.getString("EAAA25"));
				item.setEATRDJ(dERP.getDataGregoriana());
				item.setEAT002(rs.getString("EAT002"));
				item.setEAEDSP(rs.getString("EAEDSP"));
				item.setMCDSC1(rs.getNString("MCDL01"));
				item.setMCRP01(rs.getNString("MCRP01"));

				item.setDescricaoEAEDSP(getStatusDescricao(rs.getString("EAEDSP"), rs.getString("EAPID")));
				pedidos.add(item);
			}
			rs.close();

		} finally {
			if (stm != null) {
				stm.close();
			}
		}

		return pedidos;
	}

	public void calcelaPedido(String pedido) throws SQLException {
		PreparedStatement stm = null;

		try {
			if (pedido != null && !pedido.equals("0")) {

				String query = "update VW_F5542E01 set EAEDSP = 'O' where EAAA25  = '" + pedido + "'";

				UtilLog.logger.debug("Query count: " + query);

				stm = conexaoOracle.prepareStatement(query);

				stm.executeQuery();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
	}

	public boolean checaOrgao(String mcu) throws SQLException {

		PreparedStatement stm = null;

		boolean retorno = false;

		try {
			if (mcu != null && !mcu.equals("0")) {

				String query = "select count(*) AS NUMREG from VW_F0006 where MCMCU = lpad(trim('" + mcu
						+ "'),12,' ') and MCPECC in ('2','6') ";

				UtilLog.logger.debug("Query count: " + query);

				stm = conexaoOracle.prepareStatement(query);

				ResultSet rs = stm.executeQuery();

				rs.next();
				long numreg = rs.getLong("NUMREG");
				if (numreg > 0) {
					retorno = true;
				}
				rs.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stm != null) {
				stm.close();
			}
		}

		return retorno;
	}

	public List<ItemPedido> checaitens(List<ItemPedido> pedido) throws SQLException {

		String listItem = "";
		String mcuCia = null;
		List<ItemPedido> tempPed = new ArrayList<ItemPedido>();
		List<String> itensValidos = new ArrayList<String>();

		for (ItemPedido ped : pedido) {
			mcuCia = ped.getEAMCU();
			listItem = listItem + "'" + ped.getEALITM() + "',";
		}

		if (listItem.length() > 0) {
			listItem = listItem.substring(0, listItem.length() - 1);
		}

		PreparedStatement stm = null;

		try {
			if (pedido.size() > 0) {

				String query = "select IBLITM from VW_F4102 where IBLITM in (" + listItem + ") and IBMCU = '" + mcuCia
						+ "'";

				UtilLog.logger.debug("Query count: " + query);

				stm = conexaoOracle.prepareStatement(query);

				ResultSet rs = stm.executeQuery();

				while (rs.next()) {
					itensValidos.add(rs.getString("IBLITM"));
				}
				rs.close();

				for (ItemPedido iPed : pedido) {
					boolean stItem = false;
					for (String item : itensValidos) {
						if (item.trim().equals(iPed.getEALITM().trim())) {
							stItem = true;
							break;
						}
					}
					if (stItem) {
						tempPed.add(iPed);
					} else {
						// Item cancelado por obsolescência status "O"
						calcelaItem(iPed.getEAAA25(), iPed.getEALITM());
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stm != null) {
				stm.close();
			}
		}

		return tempPed;
	}

	public void calcelaItem(String pedido, String item) throws SQLException {
		PreparedStatement stm = null;

		try {
			if (pedido != null && !pedido.equals("0")) {

				String query = "update VW_F5542E01 set EAEDSP = 'O' where EAAA25  = '" + pedido
						+ "' and TRIM(EALITM) = '" + item.trim() + "'";

				UtilLog.logger.debug("Query count: " + query);

				stm = conexaoOracle.prepareStatement(query);

				stm.executeQuery();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
	}

	public long findByExtraMes(String mcu, String tipoPedido) throws SQLException {

		PreparedStatement stm = null;

		long numreg = 0L;

		try {
			if (mcu != null && !mcu.equals("0")) {

				String query = "select count(*) AS NUMREG from VW_F5542E01 where EAMCU1 = ? and EAT002 = ? and EAPID = 'SISCAP_ERP'";
				UtilLog.logger.debug("Query count: " + query);

				stm = conexaoOracle.prepareStatement(query);
				stm.setString(1, "            ".substring(0, (12 - mcu.length())) + mcu);
				stm.setString(2, tipoPedido);
				ResultSet rs = stm.executeQuery();

				rs.next();
				numreg = rs.getLong("NUMREG");
				rs.close();
			}
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		return numreg;
	}

	public boolean findByPedidoRealizadoMesMCU(String mcu, String tipoPedido) throws SQLException {

		boolean retorno = true;

		DataERP erpDt = new DataERP();

		String dterp_hoje = erpDt.getDataERPToday();
		long dterp = Long.parseLong(dterp_hoje);

		int periodo;
		if (tipoPedido.equals("N")) {
			periodo = 30;
		} else {
			periodo = 15;
		}

		PreparedStatement stm = null;
		Statement stm2 = null;

		try {

			if (mcu != "0" && mcu != null) {
				String query = "SELECT TO_CHAR(B.CRO_DT_PROCESSAMENTO, 'DD/MM/YYYY') AS DT_PROCE, TO_CHAR(CRO_DT_INI_SOLICITACAO_PEDIDO, 'DD/MM/YYYY') AS DT_INI, TO_CHAR(CRO_DT_FIM_SOLICITACAO_PEDIDO, 'DD/MM/YYYY') AS DT_FIM "
						+ "FROM UNIDADE_SOLIC_GRUPO_ATEND A, CRONOGRAMA_ATENDIMENTO B " + "WHERE A.GAT_NU = B.GAT_NU "
						+ "AND A.MCMCU_CENTRO_DISTRIBUICAO = B.MCMCU_CENTRO_DISTRIBUICAO "
						+ "AND A.MCMCU_UNIDADE_SOLICITANTE =  lpad(trim('" + mcu + "'),12,' ') "
						// +
						// "AND B.CRO_DT_PROCESSAMENTO between (SELECT
						// trunc(sysdate - "+periodo+") as NOW1 FROM DUAL) and
						// (SELECT sysdate as NOW2 FROM DUAL) "
						+ "ORDER BY DT_PROCE";

				stm2 = conexaoOracle.createStatement();
				
				long erpProcDt = 0L;
				long erpIniDt = 0L;
				long erpFimDt = 0L;
				
				ResultSet rs = stm2.executeQuery(query);

				while (rs.next()) {
					if (rs.getString("DT_PROCE") != null) {
						erpProcDt = Long.parseLong(erpDt.getDataERP(rs.getString("DT_PROCE")));
						erpIniDt = Long.parseLong(erpDt.getDataERP(rs.getString("DT_INI")));
						erpFimDt = Long.parseLong(erpDt.getDataERP(rs.getString("DT_FIM")));
					}
				}
				rs.close();

				if ((dterp <= erpProcDt) && (dterp >= erpIniDt)) {
					retorno = false;
				}

			}

		} finally {
			if (stm != null) {
				stm.close();
			}
			if (stm2 != null) {
				stm2.close();
			}
		}
		return retorno;
	}

	public ArrayList<HistoricoPedido> getHistorico(String pMode, String keyMCU, String keyPedido, String keyData,
			String keyMCUCia) throws SQLException {
		ArrayList<HistoricoPedido> aHist = new ArrayList<HistoricoPedido>();
		DataERP erpDt = new DataERP();
		
		String erpDtYearMinus = erpDt.getDataERPYearMinus();
		
		
		// keyMCUCia = "00049748";

		// keyMCU = " 00427312";

		String vQuery = null;

		// Status "A" para resumo do pedido e "B" para detalhes do pedido

		if (pMode == "A") {
			if (keyMCUCia.trim().equals("00004010")) {

				vQuery = "select distinct A.SDAN8, " + "D.VIN_CO, " + "A.SDDRQJ, " + "A.SDIVD, A.SDVR01, A.SDVR02  " + "from vw_f42119 A "
						+ "inner join vw_f0006 C ON A.SDAN8 = C.MCAN8 "
						+ "inner join vincula_pedido_erp D ON A.SDDOCO = D.SDDOCO "
						+ "left join vw_f5546209 B ON (A.SDDOCO = B.REDOCO and A.SDAN8 = B.REAN8) "
						+ "where D.MCMCU_ORGAO_SOLICITANTE = lpad('" + keyMCU.trim() + "',12,' ') "
						+ "and D.MCMCU_ORGAO_SOLICITANTE = C.mcmcu and A.SDDRQJ > "+ erpDtYearMinus + " order by A.SDDRQJ ";

			} else {
				vQuery = "select " + "distinct A.SDAN8, D.VIN_CO, A.SDDOCO, A.SDDRQJ, A.SDIVD, A.SDDOC, A.SDVR01, A.SDVR02 "
						+ "from vw_f42119 A, vw_f0006 C, vincula_pedido_erp D " + "where A.SDMCU = lpad('"
						+ keyMCUCia.trim() + "',12,' ') " + "AND D.MCMCU_ORGAO_SOLICITANTE = lpad('" + keyMCU.trim()
						+ "',12,' ') " + "and D.MCMCU_ORGAO_SOLICITANTE = C.mcmcu " + "and A.SDAN8 = C.MCAN8 "
						+ "and A.SDDOCO = D.SDDOCO and A.SDDRQJ > "+ erpDtYearMinus + " order by A.SDDRQJ ";
			}

		} else {

			if (keyMCUCia.trim().equals("00004010")) {
				vQuery = " select distinct " + "A.SDDOCO, D.VIN_CO, " + "A.SDLITM, " + "A.SDDSC1, " + "A.SDUOM, "
						+ "A.SDUORG/10000 AS SDUORG, " + "A.SDQTYT/10000 AS SDQTYT, "
						+ "CASE WHEN B.REAA15 is null or A.SDLTTR = 980 THEN 'N/D' " + "else TO_CHAR(B.REAA15) "
						+ "END as REAA15, " 
						+ "CASE WHEN B.REDATE01 is null or A.SDLTTR = 980 THEN 'N/D' "
						+ "else TO_CHAR(B.REDATE01) " 
						+ "END REDATE01, " + "A.SDDRQJ, "
						+ "CASE WHEN A.SDIVD = 0 THEN 'N/D' " 
						+ "else TO_CHAR(A.SDIVD) " + "END as SDIVD, "
						+ "CASE WHEN A.SDDOC = 0 THEN 'N/D' " 
						+ "else TO_CHAR(A.SDDOC) " + "END SDDOC, " + "A.SDLTTR, A.SDVR01, A.SDVR02  "
						+ "from vw_f42119 A " + "inner join vw_f0006 C ON A.SDAN8 = C.MCAN8 "
						+ "inner join vincula_pedido_erp D ON A.SDDOCO = D.SDDOCO "
						+ "left join vw_f5546209 B ON (A.SDDOCO = B.REDOCO and A.SDAN8 = B.REAN8) "
						+ "where D.VIN_CO = '" + keyPedido + "' " + "and A.SDMCU = lpad('" + keyMCUCia.trim()
						+ "',12,' ') "
						// + "and A.SDDRQJ = " + erpDt.getDataERP(keyData)
						+ " and D.MCMCU_ORGAO_SOLICITANTE = C.mcmcu and A.SDDRQJ > "+ erpDtYearMinus + " order by A.SDLITM, A.SDDRQJ";
			} else {
				vQuery = " select distinct " + "A.SDDOCO, D.VIN_CO, " + "A.SDLITM, " + "A.SDDSC1, " + "A.SDUOM, "
						+ "A.SDUORG/10000 AS SDUORG, " + "A.SDQTYT/10000 AS SDQTYT, " + "A.SDDRQJ, "
						+ "CASE WHEN A.SDIVD = 0 THEN 'N/D' " + "else TO_CHAR(A.SDIVD) " + "END as SDIVD, "
						+ "CASE WHEN A.SDDOC = 0 THEN 'N/D' " + "else TO_CHAR(A.SDDOC) " + "END SDDOC, " + "A.SDLTTR, A.SDVR01, A.SDVR02  "
						+ "from vw_f42119 A " + "inner join vw_f0006 C ON A.SDAN8 = C.MCAN8 "
						+ "inner join vincula_pedido_erp D ON A.SDDOCO = D.SDDOCO " + "where D.VIN_CO = '" + keyPedido
						+ "' " + "and A.SDMCU = lpad('" + keyMCUCia.trim() + "',12,' ') "
						// + "and A.SDDRQJ = " + erpDt.getDataERP(keyData)
						+ " and D.MCMCU_ORGAO_SOLICITANTE = C.mcmcu and A.SDDRQJ > "+ erpDtYearMinus + " order by A.SDLITM, A.SDDRQJ";
			}

		}

		PreparedStatement stm = null;
		try {

			stm = conexaoOracle.prepareStatement(vQuery);
			ResultSet rs = stm.executeQuery(vQuery);

			String numnota = "";

			while (rs.next()) {
				HistoricoPedido tHist = new HistoricoPedido();

				if (pMode == "B") {
					System.out.println("recuperou:" + rs.getString("SDLITM") + "- " + rs.getString("SDUORG"));
					tHist.setSDLITM(rs.getString("SDLITM"));
					tHist.setSDDSC1(rs.getString("SDDSC1"));
					tHist.setSDUOM(rs.getString("SDUOM"));
					tHist.setSDUORG(rs.getString("SDUORG"));
					tHist.setSDQTYT(rs.getString("SDQTYT"));
					tHist.setSDDOCO(rs.getString("SDDOCO"));
					tHist.setVIN_CO(rs.getString("VIN_CO"));
					tHist.setSDDRQJ(rs.getString("SDDRQJ"));
					tHist.setSDIVD(rs.getString("SDIVD"));
					tHist.setNUMNOTA(rs.getString("SDDOC"));
					tHist.setSDLTTR(rs.getNString("SDLTTR"));
					tHist.setSDDOC(rs.getString("SDDOC"));
					tHist.setSDVR01(rs.getString("SDVR01"));
					tHist.setSDVR02(rs.getString("SDVR02"));
					

					// tHist.setREDATE01("0");

					if (keyMCUCia.trim().equals("00004010")) {
						tHist.setREAA15(rs.getString("REAA15"));
						tHist.setREDATE01(rs.getString("REDATE01"));
					} else {
						String SSPRegi = this.getRegistradoCDLeste(tHist.getSDDOC(), keyMCU, tHist.getSDDRQJ());

						// System.out.println("Retorno SSP: "+ SSPRegi);
						if (!SSPRegi.equals("N/D")) {
							for (String str : SSPRegi.split(";")) {
								String[] aSRO = str.split("#");
								// System.out.println("Retorno SSP: "+ aSRO[0] +
								// " ------ " + aSRO[1] );
								tHist.setREAA15(aSRO[0]);
								tHist.setREDATE01(aSRO[1]);
							}
						} else {
							tHist.setREAA15(SSPRegi);
						}
						numnota = tHist.getNUMNOTA();

					}
					boolean logic = true;
					for (HistoricoPedido hist : aHist) {
						if (tHist.getSDLITM().equals(hist.getSDLITM())) {
							logic = false;
							break;
						}
					}
					if (logic == true) {
						aHist.add(tHist);
					}

					tHist = null;
				}

				if (pMode == "A") {
					tHist.setSDAN8(rs.getString("SDAN8"));
					// tHist.setSDDOCO(rs.getString("SDDOCO"));
					// tHist.setSDDOC(rs.getString("SDDOC"));
					tHist.setVIN_CO(rs.getString("VIN_CO"));
					tHist.setSDDRQJ(rs.getString("SDDRQJ"));
					tHist.setSDIVD(rs.getString("SDIVD"));
					tHist.setSDVR01(rs.getString("SDVR01"));
					tHist.setSDVR02(rs.getString("SDVR02"));
					numnota = tHist.getNUMNOTA();
					aHist.add(tHist);
				}
				// if(!tHist.getSDIVD().equals("-")){

				// }
				tHist = null;
			}
			// }
			rs.close();

		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		return aHist;

	}

	public String getRegistradoCDLeste(String pNota, String keyMCU, String dataPed)  {

		String sRegistrado = "";
		String nota = "";

		if (dataPed != null) {

			int iAno = Integer.parseInt(dataPed.substring(6, 10));
			int iMes = Integer.parseInt(dataPed.substring(3, 5));

			if (pNota != null && (!pNota.equals("0"))) {
				nota = pNota.substring(0, pNota.length() - 2) + iAno;

				PreparedStatement stm2 = null;

				try {
					String vQuery = "SELECT distinct A.PSU_NU_PAUTA_SUPRIMENTO AS PAUTA_SUPRIMENTO,"
							+ "A.PSU_IN_FORNECIMENTO AS TIPO_FORNECIMENTO," + "A.MCMCU AS MCMCU,"
							+ "C.ETI_NU_REGISTRO AS OBJETO,"
							+ "CONVERT( CHAR(10), C.ETI_DT_EXPEDICAO, 103 ) AS DATA_EXPEDICAO,"
							+ "LEFT( C.ETI_HR_EXPEDICAO, 2 ) + ':' + RIGHT( C.ETI_HR_EXPEDICAO, 2 ) AS HORA_EXPEDICAO "
							+ "FROM [DBSSP].[dbo].[CAM_PSU] A,[DBSSP].[dbo].[CAM_PEX] B,[DBSSP].[dbo].[CAM_ETI] C  "
							+ "WHERE B.PSU_NU_PAUTA_SUPRIMENTO = A.PSU_NU_PAUTA_SUPRIMENTO AND " + "B.MCMCU = '"
							+ "            ".substring(0, (12 - keyMCU.length())) + keyMCU + "' "
							+ "AND A.PSU_NU_PAUTA_SUPRIMENTO = '" + nota + "' " + "AND YEAR(C.ETI_DT_EXPEDICAO) = "
							+ iAno + " AND B.PSU_IN_FORNECIMENTO = A.PSU_IN_FORNECIMENTO     AND "
							+ "B.MCMCU                   = A.MCMCU                   AND "
							+ "C.ETI_NU_REGISTRO         = B.ETI_NU_REGISTRO         AND "
							+ "C.ETI_DT_EXPEDICAO        = B.ETI_DT_EXPEDICAO        AND "
							+ "C.ETI_DT_CANCELAMENTO     IS NULL";

					UtilLog.logger.debug("Query SQLSERVER" + vQuery);

					stm2 = conexaoSqlServer.prepareStatement(vQuery);
					ResultSet rs2 = stm2.executeQuery();
					while (rs2.next()) {
						DataERP dtERP = new DataERP();
						sRegistrado = sRegistrado + rs2.getString("OBJETO") + "#"
								+ dtERP.getDataERP(rs2.getString("DATA_EXPEDICAO")) + ";";
					}
					rs2.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (stm2 != null) {

						try {
							stm2.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}
		}
		if (sRegistrado == "" || sRegistrado == null) {
			sRegistrado = "N/D";
		}
		return sRegistrado;

	}

	public void updateQtdPedido(String numPedido, String item, String quantidade) throws SQLException, SiscapException {
		String query = "UPDATE VW_F5542E01 set EAUORG = " + Double.parseDouble(quantidade) * 10000 + " where EAAA25 = '"
				+ numPedido + "' and EALITM = '" + item + "'";

		PreparedStatement stm = null;
		try {
			stm = conexaoOracle.prepareStatement(query);

			stm.executeQuery(query);
			this.setAuditoria(this.usuario.getMatricula(),
					query, "VW_F5542E01", "ALTEROU QUANT. ITEM DE PEDIDO TABELA CAPTACAO", numPedido);
		} finally {
			if (stm != null) {
				stm.close();
			}

		}
	}

	public String getUnidadeMedida(String pItem) throws SQLException {
		PreparedStatement stm = null;

		String resu = "";

		String vQuery = "SELECT A.IMUOM6 " + "FROM vw_F4101 A " + "where A.IMLITM = '" + pItem + "'";

		try {
			stm = conexaoOracle.prepareStatement(vQuery);

			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				resu = rs.getString("IMUOM6");

			}
		} finally {
			if (stm != null) {
				stm.close();
			}

		}

		return resu;

	}

	public String getStatusDescricao(String pStatus, String pJob) {
		String descricao = "";

		if (pStatus.equals("S")) {
			descricao = "Solicitado";
		}
		if (pStatus.equals("R")) {
			descricao = "Em Aprovação";
		}
		if (pStatus.equals("V")) {
			descricao = "Em Validação";
		}
		if ((pStatus.equals(" ")) && (pJob.trim().equals("SISCAP_ERP"))) {
			descricao = "Aprovado";
		} else {
			if (pStatus.equals(" ")) {
				descricao = "Pendente";
			}
		}
		if (pStatus.equals("Y")) {
			descricao = "Processado";
		}
		if (pStatus.equals("C")) {
			descricao = "Cancelado";
		}
		if (pStatus.equals("O")) {
			descricao = "Obsoleto";
		}
		if (pStatus.equals("D")) {
			descricao = "Excluido";
		}
		return descricao;
	}

	public List<String> getRegistrados(String pedido, String mcu) throws SQLException {

		PreparedStatement stm = null;

		List<String> resu = new ArrayList<String>();

		String vQuery = "SELECT DISTINCT B.REAA15 " + "FROM VW_F42119 A " + "LEFT OUTER JOIN VW_F5546209 B ON "
				+ "(B.REDOCO = A.SDDOCO AND B.REMCU  = A.SDMCU  AND B.REAN8  = A.SDAN8) "
				+ "WHERE A.SDDOCO in (select DISTINCT SDDOCO from vincula_pedido_erp where VIN_CO = '" + pedido
				+ "' AND SDDOCO < 999999999) "
				+ "AND A.SDAN8 = (select DISTINCT MCAN8 from VW_F0006 where MCMCU = LPAD('" + mcu.trim() + "',12,' ')) "
				+ "AND A.SDMCU = LPAD('00004010',12,' ') " + "AND SUBSTR(A.SDTRDJ,0,3) = '116' " + "AND A.SDLTTR = 620";
		try {
			stm = conexaoOracle.prepareStatement(vQuery);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				resu.add(rs.getString("REAA15"));
			}
		} finally {
			if (stm != null) {
				stm.close();
			}

		}

		return resu;
	}
	


	private void setAuditoria(String usuario, String registro, String entidade, String funcionalidade,
			String pedidoCodigo) throws SiscapException, SQLException {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now = new Date();
		
		Auditoria audit = new Auditoria();
		audit.setAud_nu("{select count(*)+1 as ID_NU from auditoria}");
		audit.setAud_no_entidade(entidade);
		audit.setAud_tx_funcionalidade(funcionalidade);
		audit.setAud_tx_identidade_registro(pedidoCodigo);
		audit.setAud_tx_registro(registro);
		audit.setAud_tx_usuario(usuario);
		audit.setAud_dh_execucao(dateFormat.format(now));
		

		Connection connection;
		try {
			connection = this.conexaoOracle;
			AuditoriaDAO dao = new AuditoriaDAO(connection);

			dao.insert(audit);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
