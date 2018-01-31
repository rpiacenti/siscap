package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.excecao.SiscapException;
import br.com.correios.siscap.model.Auditoria;
import br.com.correios.siscap.model.Orgao;

public class OrgaoGrupoDAO{
	
	private Usuario usuario;

	private Connection conexao;
	
	public OrgaoGrupoDAO(Connection c, Usuario pUsuario){
		conexao = c;
		this.usuario = pUsuario;
	}

	
	public void onEdit(Orgao o) throws SQLException {
		long count;

		count = findByCount(o.getCdtransito(),
				o.getGrptransito(), o.getMcu());

		UtilLog.logger.debug("BUSCA DE REGISTROS: " + count);
		if (count == 0) {
			// INSERT
			this.insert(o);
		} else {
			// UPDATE
			this.update(o);
		}
	}

	
	public void insert(Orgao o) throws SQLException {
		
		StringBuilder vQuery = new StringBuilder("INSERT INTO UNIDADE_SOLIC_GRUPO_ATEND (MCMCU_UNIDADE_SOLICITANTE, MCMCU_CENTRO_DISTRIBUICAO,GAT_NU ) ");
		vQuery.append(" VALUES (?, ?, ?)");
		UtilLog.logger.debug(vQuery);
		if (o.getMcu() != null && o.getMcu() != null
				&& o.getGrptransito() > 0
				&& o.getCdtransito() != null) {
			
			PreparedStatement stm = null;
			try{
				stm  = conexao.prepareStatement(vQuery.toString());
				stm.setString(1, "            ".substring(0, (12 - o.getMcu().length()))+ o.getMcu());
				stm.setString(2, "            ".substring(0, (12 - o.getCdtransito().length()))	+ o.getCdtransito());
				stm.setInt(3, o.getGrptransito());
				stm.executeQuery();
				
				this.setAuditoria(this.usuario.getMatricula(), vQuery.toString() + " ==> " + o.getMcu().trim() + " ; "+ o.getCdtransito().trim() +" ; " + o.getGrptransito(),
						"UNIDADE_SOLIC_GRUPO_ATEND","ORGÂO MARCADO PARA O GRUPO",
						o.getMcu().trim() + " ; "+ o.getCdtransito().trim() +" ; " + o.getGrptransito());
				

			} catch (SiscapException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}finally{
				if(stm != null){
					stm.close();
					
				}
			}
	
		}

	}

	public void insertBatch(ArrayList<Orgao> orgaos)
			throws ClassNotFoundException, SQLException {
		String vQuery = "INSERT INTO UNIDADE_SOLIC_GRUPO_ATEND (MCMCU_UNIDADE_SOLICITANTE, MCMCU_CENTRO_DISTRIBUICAO,GAT_NU ) VALUES(?, ?, ?) ";
		
		PreparedStatement ps = null;
		try{
			ps = conexao.prepareStatement(vQuery);
			
			for (Orgao org : orgaos) {
				
				ps.setString(1, org.getMcu());
				ps.setString(2, org.getCdtransito());
				ps.setInt(3, org.getGrptransito());
				ps.addBatch();
			}
			ps.executeBatch(); // insert remaining records
			ps.close();
		}finally{
			if(ps != null){
				ps.close();
			}
			
		}
	}

	
	public void update(Orgao o) throws SQLException {
		
		

//		String vQuery = "DELETE UNIDADE_SOLIC_GRUPO_ATEND "
//				+ " where MCMCU_CENTRO_DISTRIBUICAO = '"
//				+ "            ".substring(0, (12 - org.getCdtransito()
//						.length())) + org.getCdtransito() + "' and gat_nu = "
//				+ org.getGrptransito() + " and MCMCU_UNIDADE_SOLICITANTE = "
//				+ "            ".substring(0, (12 - org.getMcu().length()))
//				+ org.getMcu();
//
//		System.out.println(vQuery);
//		stm = con.createStatement();
//		stm.executeQuery(vQuery);
//
//		vQuery = "INSERT INTO UNIDADE_SOLIC_GRUPO_ATEND (MCMCU_UNIDADE_SOLICITANTE, MCMCU_CENTRO_DISTRIBUICAO,GAT_NU ) VALUES ('"
//				+ "            ".substring(0, (12 - org.getMcu().length()))
//				+ org.getMcu()
//				+ "','"
//				+ "            ".substring(0, (12 - org.getCdtransito()
//						.length()))
//				+ org.getCdtransito()
//				+ "',"
//				+ org.getGrptransito() + ")";
//
//		stm.executeQuery(vQuery);


	}

	public void updateBatch(ArrayList<Orgao> orgaos)
			throws ClassNotFoundException, SQLException {


//		String vQuery = "DELETE UNIDADE_SOLIC_GRUPO_ATEND "
//				+ " where MCMCU_CENTRO_DISTRIBUICAO = '"
//				+ "            ".substring(0, (12 - orgaos.get(1)
//						.getCdtransito().length()))
//				+ orgaos.get(0).getCdtransito() + "' and gat_nu = "
//				+ orgaos.get(0).getGrptransito();
//		System.out.println(vQuery);
//		stm = con.createStatement();
//		stm.executeQuery(vQuery);
//
//		vQuery = "INSERT INTO UNIDADE_SOLIC_GRUPO_ATEND (MCMCU_UNIDADE_SOLICITANTE, MCMCU_CENTRO_DISTRIBUICAO,GAT_NU ) VALUES(?, ?, ?) ";
//
//		PreparedStatement ps = con.prepareStatement(vQuery);
//
//		final int batchSize = 1000;
//		int count = 0;
//
//		for (Orgao org : orgaos) {
//			System.out.println("Insert: " + org.getMcu());
//			ps.setString(1, org.getMcu());
//			ps.setString(2, org.getCdtransito());
//			ps.setInt(3, org.getGrptransito());
//			ps.addBatch();
//
//			if (++count % batchSize == 0) {
//				ps.executeBatch();
//			}
//		}
//		ps.executeBatch(); // insert remaining records
//		con.commit();
//		ps.close();
//		stm.close();
//		
//		con.close();
	}


	public void delete(Orgao orgao) throws SQLException {
		PreparedStatement ps = null;
		StringBuilder vQuery = new StringBuilder("DELETE UNIDADE_SOLIC_GRUPO_ATEND ");
		vQuery.append(" where MCMCU_CENTRO_DISTRIBUICAO = ? ");
		vQuery.append( " and gat_nu = ? and MCMCU_UNIDADE_SOLICITANTE = ?");
		
		UtilLog.logger.debug(vQuery);
		try{
			ps = conexao.prepareStatement(vQuery.toString());
			ps.setString(1, "            ".substring(0, (12 - orgao.getCdtransito().length())) + orgao.getCdtransito());
			ps.setInt(2, orgao.getGrptransito());
			ps.setString(3, "            ".substring(0, (12 - orgao.getMcu().length()))	+ orgao.getMcu());
			ps.executeQuery();
			
			this.setAuditoria(this.usuario.getMatricula(), vQuery.toString() + " ==> " + orgao.getMcu().trim() + " ; "+ orgao.getCdtransito().trim() +" ; " + orgao.getGrptransito(),
					"UNIDADE_SOLIC_GRUPO_ATEND","ORGÂO DESMARCADO PARA O GRUPO",
					orgao.getMcu().trim() + " ; "+ orgao.getCdtransito().trim() +" ; " + orgao.getGrptransito());
			
		} catch (SiscapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(ps != null){
				ps.close();				
			}
		}


	}

	public void deleteBatch(ArrayList<Orgao> orgaos) throws SQLException {
		String vQuery = "DELETE FROM UNIDADE_SOLIC_GRUPO_ATEND where MCMCU_UNIDADE_SOLICITANTE = ? and MCMCU_CENTRO_DISTRIBUICAO = ? and GAT_NU = ? ";
		PreparedStatement ps = null;
		try{
			ps = conexao.prepareStatement(vQuery);
			
			for (Orgao org : orgaos) {
				
				ps.setString(1, org.getMcu());
				ps.setString(2, org.getCdtransito());
				ps.setInt(3, org.getGrptransito());
				ps.addBatch();
			}
			ps.executeBatch();
			ps.close();
		}finally{
			
		}

	}

	
	public String findMCUsGrupo(String mcuCentroAtendimento, int grupoAtendimento) throws SQLException {
		
		PreparedStatement stm = null;
		try {
			StringBuilder mcu = new StringBuilder();
			if ((mcuCentroAtendimento != null) && (grupoAtendimento > 0)) {
				
				StringBuilder vQuery = new StringBuilder("select mcmcu_unidade_solicitante AS MCU_ORG from UNIDADE_SOLIC_GRUPO_ATEND ");
				vQuery.append(" where MCMCU_CENTRO_DISTRIBUICAO = ?  and gat_nu = ?");
				

				stm = conexao.prepareStatement(vQuery.toString());
				stm.setString(1, "            ".substring(0, (12 - mcuCentroAtendimento.length())) + mcuCentroAtendimento);
				stm.setInt(2, grupoAtendimento);
				ResultSet rs = stm.executeQuery();

				while (rs.next()) {
					mcu.append(rs.getString("MCU_ORG")+",");
				}
				
				rs.close();
				
				return mcu.toString();
			}
		} finally{
			if(stm != null){
				stm.close();				
			}
		}
		return "";
	}

	public long findByCount(String keyCD, int KeyGrp, String keyMcu) throws SQLException {
		
		PreparedStatement stm = null;
		long count = 0;
		
		try {
			if ((keyCD != null) && (KeyGrp > 0) && (keyMcu != null)	&& (keyMcu != null)) {
				StringBuilder vQuery = new StringBuilder("select count(*) AS NUMREG from UNIDADE_SOLIC_GRUPO_ATEND ");
				vQuery.append("where MCMCU_CENTRO_DISTRIBUICAO = ? and gat_nu = ? and mcmcu_unidade_solicitante = ?");
						
				

				stm = conexao.prepareStatement(vQuery.toString());
				stm.setString(1, "            ".substring(0, (12 - keyCD.length()))	+ keyCD);
				stm.setInt(2, KeyGrp);
				stm.setString(3, "            ".substring(0, (12 - keyMcu.length())) + keyMcu );
				
				ResultSet rs = stm.executeQuery();				
				rs.next();
				count = rs.getLong("NUMREG");
				rs.close();
				

			}
			return count;

		} finally{
			if(stm != null){
				stm.close();
			}
		}
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
