package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.excecao.SiscapException;
import br.com.correios.siscap.model.Auditoria;
import br.com.correios.siscap.model.ItemDisponivel;

public class ItemParametroDAO {
	

	private Usuario usuario;

	private Connection conexao;

	public ItemParametroDAO(Connection c, Usuario pUsuario) {
		conexao = c;
		this.usuario = pUsuario;
	}

	public void insertBatch(ItemDisponivel itemDisponivel) throws SQLException {

		String sDRKY = itemDisponivel.getItp_tx_natureza_orgao().replace("'",
				"");
		String[] aDRKY = sDRKY.split(",");
		PreparedStatement stm = null;
		PreparedStatement stmDel = null;
		try {
			String queryDel = "DELETE PARAMETRO_ITEM_CENTRO_DISTRIB "
					+ " where IBMCU_CENTRO_DISTRIBUICAO = lpad(trim('" + itemDisponivel.getItp_co_cia() + "'),12,' ') AND IBITM_ITEM_PEDIDO = '"+itemDisponivel.getItp_co_item()+"' AND trim(DRKY_TIPO_ORGAO) in  (" + itemDisponivel.getItp_tx_natureza_orgao() + ")";

			String query = "INSERT INTO PARAMETRO_ITEM_CENTRO_DISTRIB (DRKY_TIPO_ORGAO,IBMCU_CENTRO_DISTRIBUICAO,IBITM_ITEM_PEDIDO,PIT_IN_PEDIDO,PIT_PE_ITEM_PADRAO) "
					+ "VALUES (?, ?, ?, ?,?)";

			UtilLog.logger.debug(queryDel);
			UtilLog.logger.debug(query);

			conexao.setAutoCommit(false);

			stmDel = conexao.prepareStatement(queryDel);

			stmDel.executeQuery();

			stm = conexao.prepareStatement(query);
			for (int i = 0; i < aDRKY.length; i++) {
				stm.setString(1, aDRKY[i]);
				stm.setString(
						2,
						"            ".substring(0, (12 - itemDisponivel
								.getItp_co_cia().length()))
								+ itemDisponivel.getItp_co_cia());
				stm.setString(3, itemDisponivel.getItp_co_item());
				stm.setString(4, itemDisponivel.getItp_tx_tipo_pedido().trim());
				stm.setString(5, itemDisponivel.getPit_pe_item_padrao().trim());
				
				
				stm.addBatch();
			}
			stm.executeBatch();

			this.setAuditoria(this.usuario.getMatricula(), query + " ==> " + itemDisponivel.getItp_co_cia() + " ; "+ itemDisponivel.getItp_co_item().trim()+" ; "+itemDisponivel.getItp_tx_tipo_pedido().trim(),
					"PARAMETRO_ITEM_CENTRO_DISTRIB","ITEM MARCADO NA PARAMETRIZAÇÂO", itemDisponivel
					.getItp_co_cia() + " - " + itemDisponivel.getItp_co_item().trim());
			
			
			conexao.commit();
			

		} catch (SiscapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stm != null) {
				stm.close();
			}
		}

	}

	public void delete(ItemDisponivel itemDisponivel) throws SQLException {

		String query = "DELETE PARAMETRO_ITEM_CENTRO_DISTRIB "
				+ " where IBMCU_CENTRO_DISTRIBUICAO = ? "
				+ "AND IBITM_ITEM_PEDIDO = ? "
				+ "AND PIT_IN_PEDIDO = '"+ itemDisponivel.getItp_tx_tipo_pedido().trim() +"' "
				+ "AND trim(DRKY_TIPO_ORGAO) in  ("
				+ itemDisponivel.getItp_tx_natureza_orgao() + ")";

		UtilLog.logger.debug(query);

		PreparedStatement stm = null;
		try {
			stm = conexao.prepareStatement(query);
			stm.setString(
					1,
					"            ".substring(0, (12 - itemDisponivel
							.getCdtransito().length()))
							+ itemDisponivel.getCdtransito());
			stm.setLong(2,
					Long.parseLong(itemDisponivel.getItp_co_item().trim()));
			
			stm.executeQuery();
			this.setAuditoria(this.usuario.getMatricula(), query + " ==> " + itemDisponivel.getItp_co_cia() + " ; "+ itemDisponivel.getItp_co_item().trim()+" ; "+itemDisponivel.getItp_tx_tipo_pedido().trim(),
					"PARAMETRO_ITEM_CENTRO_DISTRIB","ITEM DESMARCADO NA PARAMETRIZAÇÂO", itemDisponivel
					.getItp_co_cia() + " - " + itemDisponivel.getItp_co_item().trim());
			
		} catch (SiscapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stm != null) {
				stm.close();
			}

		}

	}

	public String findItensParametrizados(String cd, String tip,
			String tipoOrgao) throws SQLException {

		String item = "";
		PreparedStatement stm = null;
		try {
			if (cd != null) {

				String query = "SELECT DISTINCT IBITM_ITEM_PEDIDO FROM PARAMETRO_ITEM_CENTRO_DISTRIB "
						+ "WHERE IBMCU_CENTRO_DISTRIBUICAO = ? and PIT_IN_PEDIDO = ? and trim(DRKY_TIPO_ORGAO) = ?";

				UtilLog.logger.debug("Query itens: " + query);

				stm = conexao.prepareStatement(query);
				stm.setString(1,
						"            ".substring(0, (12 - cd.length())) + cd);
				stm.setString(2, tip);
				stm.setString(3, tipoOrgao);
				ResultSet rs = stm.executeQuery();

				while (rs.next()) {

					item = item + "," + rs.getString("IBITM_ITEM_PEDIDO");

				}
				rs.close();
			}
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		return item;
	}

	public List<ItemDisponivel> findItemCD(String cd) throws SQLException {

		ArrayList<ItemDisponivel> itemsDisponiveis = new ArrayList<ItemDisponivel>();

		PreparedStatement stm = null;

		try {

			if (cd != null) {

				String query = "SELECT Distinct A.IMLITM, A.IMDSC1,	A.IMDSC2,	A.IMLNTY,	A.IMSRP2,	A.IMSTKT,	A.IMGLPT,	A.IMUOM1,	A.IMUOM6 "
						+ "FROM vw_F4101 A "
						+ "where A.imlitm in (select iblitm from VW_F4102 WHERE ibmcu = ?) and A.IMPRP4 <> 'UNI' ";
				UtilLog.logger.debug("Query itens 2: " + query);

				stm = conexao.prepareStatement(query);
				stm.setString(1,
						"            ".substring(0, (12 - cd.length())) + cd);

				ResultSet rs = stm.executeQuery();

				while (rs.next()) {
					ItemDisponivel item = new ItemDisponivel();
					item.setItp_co_cia(cd);
					item.setItp_co_item(rs.getString("IMLITM"));
					item.setItp_tx_descricao_item(rs.getString("IMDSC1"));
					item.setItp_tx_descricao_detalhe_item(rs
							.getString("IMDSC2"));
					item.setItp_tx_unidade_item(rs.getString("IMUOM1"));

					itemsDisponiveis.add(item);
				}
				rs.close();
				return itemsDisponiveis;
			}
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		return itemsDisponiveis;
	}
	
	private void setAuditoria(String usuario, String registro, String entidade, String funcionalidade,
			String registroIdent) throws SiscapException, SQLException {
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now = new Date();
		
		Auditoria audit = new Auditoria();
		audit.setAud_nu("{select count(*)+1 as ID_NU from auditoria}");
		audit.setAud_no_entidade(entidade);
		audit.setAud_tx_funcionalidade(funcionalidade);
		audit.setAud_tx_identidade_registro(registroIdent);
		audit.setAud_tx_registro(registro);
		audit.setAud_tx_usuario(usuario);
		audit.setAud_dh_execucao(dateFormat.format(now));
		
		Connection connection;
		try {
			connection = this.conexao;
			AuditoriaDAO dao = new AuditoriaDAO(connection);

			dao.insert(audit);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
