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

import javax.inject.Inject;
import javax.sql.DataSource;

import br.com.correios.ppjsiscap.paginador.Paginador;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.excecao.SiscapException;
import br.com.correios.siscap.model.Auditoria;
import br.com.correios.siscap.model.GrupoAtendimento;

public class GrupoAtendimentoDAO{

	@Inject
	@OracleDb
	private DataSource dataSource;
	
	private Usuario usuario;
	
	private Connection conexao;

	public GrupoAtendimentoDAO(Connection c, Usuario pUsuario) {
		conexao = c;
		this.usuario = pUsuario;
	}
	

	public void insert(GrupoAtendimento grupoAtendimento ) throws SQLException {
		
		
		StringBuilder vQuery = new StringBuilder(
				"INSERT INTO GRUPO_ATENDIMENTO (GAT_NU,MCMCU_CENTRO_DISTRIBUICAO,GAT_NO,GAT_QT_PEDIDO_EXTRA_TERCEIRO,GAT_QT_PEDIDO_EXTRA_ECT, GAT_IN_ITEM_PEDIDO) ");
		vQuery.append(" VALUES (?, ?, ?, ?, ?, ?)");
		
		UtilLog.logger.debug(vQuery.toString());
		
		PreparedStatement stm = null;
		try {
			stm = conexao.prepareStatement(vQuery.toString());
			stm.setInt(1, grupoAtendimento.getNumGrupo()); 
			stm.setString(2,"            ".substring(0, (12 - grupoAtendimento.getMcuCd().length())) + grupoAtendimento.getMcuCd());
			stm.setString(3, grupoAtendimento.getDescricao());
			stm.setInt(4, grupoAtendimento.getExterceiro());
			stm.setInt(5, grupoAtendimento.getExpropria());
			stm.setString(6, grupoAtendimento.getCatgrupo());
			
			stm.executeQuery();
			String detEvento = grupoAtendimento.getNumGrupo() + ";" + grupoAtendimento.getMcuCd().trim() + ";" + grupoAtendimento.getDescricao() + ";" + grupoAtendimento.getExterceiro() + ";" + grupoAtendimento.getCatgrupo();
			this.setAuditoria(this.usuario.getMatricula(), vQuery.toString() + " ==>> " + detEvento, "GRUPO_ATENDIMENTO", "INCLUSAO GRUPO ATENDIMENTO",
					grupoAtendimento.getNumGrupo().toString());
			
		} catch (SiscapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stm != null) {
				stm.close();

			}
		}
	
		
	}

	
	public void update(GrupoAtendimento ga) throws SQLException {
		
		
		StringBuilder vQuery = new StringBuilder("UPDATE GRUPO_ATENDIMENTO SET ");
		vQuery.append("GAT_NO = ?, GAT_QT_PEDIDO_EXTRA_TERCEIRO = ? , GAT_QT_PEDIDO_EXTRA_ECT = ?, GAT_IN_ITEM_PEDIDO = ? ");
		vQuery.append(" WHERE MCMCU_CENTRO_DISTRIBUICAO = ? ");
		vQuery.append(" AND  GAT_NU = ?");
		

		UtilLog.logger.debug(vQuery);

		PreparedStatement stm = null;
		try {
			stm = conexao.prepareStatement(vQuery.toString());
			stm.setString(1, ga.getDescricao());
			stm.setInt(2, ga.getExterceiro());
			stm.setInt(3, ga.getExpropria());
			stm.setString(4, ga.getCatgrupo());
			stm.setString(5,"            ".substring(0, (12 - ga.getNumcd().length()))+ ga.getNumcd());
			stm.setInt(6, ga.getNumGrupo()); 		
			stm.executeQuery();
			
			if(ga != null){
				String detEvento = ga.getNumGrupo() + ";" + ga.getMcuCd().trim() + ";" + ga.getDescricao() + ";" + ga.getExterceiro() + ";" + ga.getCatgrupo();
				this.setAuditoria(this.usuario.getMatricula(), vQuery.toString() + " ==>> " + detEvento, "GRUPO_ATENDIMENTO", "ALTERACAO GRUPO ATENDIMENTO",
					ga.getNumGrupo().toString());
			}

		} catch (SiscapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stm != null) {
				stm.close();

			}
		}
	}
	
	public int deleteGRP(GrupoAtendimento grupoAtendimento) throws SQLException {
		
		
		StringBuilder vQuery = new StringBuilder(
				"DELETE GRUPO_ATENDIMENTO ");
		vQuery.append("WHERE MCMCU_CENTRO_DISTRIBUICAO = ? ");
		vQuery.append("AND  GAT_NU = ? ");
		vQuery.append("AND 0 = (select count(*) from UNIDADE_SOLIC_GRUPO_ATEND where gat_nu = ? AND MCMCU_CENTRO_DISTRIBUICAO = ?)");
		vQuery.append("AND 0 = (select count(*) from CRONOGRAMA_ATENDIMENTO where gat_nu = ? AND MCMCU_CENTRO_DISTRIBUICAO = ?)");

		UtilLog.logger.debug(vQuery);

		PreparedStatement stm = null;
		int numexc=0;
		try {
			stm = conexao.prepareStatement(vQuery.toString());
			stm.setString(1,"            ".substring(0, (12 - grupoAtendimento.getNumcd().length()))+ grupoAtendimento.getNumcd());
			stm.setInt(2, grupoAtendimento.getNumGrupo());
			stm.setInt(3, grupoAtendimento.getNumGrupo());
			stm.setString(4, "            ".substring(0, (12 - grupoAtendimento.getNumcd().length()))+ grupoAtendimento.getNumcd());
			stm.setInt(5, grupoAtendimento.getNumGrupo());
			stm.setString(6, "            ".substring(0, (12 - grupoAtendimento.getNumcd().length()))+ grupoAtendimento.getNumcd());

			ResultSet rs = stm.executeQuery();
			
			numexc = rs.getFetchSize();
			
			String detEvento = grupoAtendimento.getNumGrupo() + ";" + grupoAtendimento.getMcuCd().trim();
			this.setAuditoria(this.usuario.getMatricula(), vQuery.toString() + " ==>> " + detEvento, "GRUPO_ATENDIMENTO", "EXCLUSAO GRUPO ATENDIMENTO",
					grupoAtendimento.getNumGrupo().toString());
			
		} catch (SiscapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (stm != null) {
				stm.close();

			}
		}
		return numexc;
	}
		

	
	public Object findByID(Integer id) {
		
		return null;
	}

	public int verificaSeExiste(GrupoAtendimento ga) throws SQLException {
		
			StringBuilder query = new StringBuilder(
					"SELECT COUNT(GAT_NU) as NUM_GRP FROM GRUPO_ATENDIMENTO ");
			query.append("WHERE MCMCU_CENTRO_DISTRIBUICAO = ? ");
			query.append("AND  GAT_NU = ? ");

			UtilLog.logger.debug(query.toString());

			PreparedStatement stm = null;
			
			int retorno = 0;
			try {
				stm = conexao.prepareStatement(query.toString());
				stm.setString(1,"            ".substring(0, (12 - ga.getMcuCd().length()))+ ga.getMcuCd());
				stm.setInt(2, ga.getNumGrupo());
				
				ResultSet rs = stm.executeQuery();
				if(rs.next()){
					retorno = rs.getInt("NUM_GRP");
				}

			} finally {
				if (stm != null) {
					stm.close();

				}
			}
			
			return retorno;

	}
		
		

	public String newGrpNumber(String MCUCD) throws SQLException {
		
		StringBuilder vQuery = new StringBuilder("SELECT max(gat_nu) as keyGrp FROM GRUPO_ATENDIMENTO ");
		vQuery.append("WHERE MCMCU_CENTRO_DISTRIBUICAO = ? ");

		UtilLog.logger.debug(vQuery);

		PreparedStatement stm = null;
		
		String retorno = "1";
		try {
			stm = conexao.prepareStatement(vQuery.toString());
			stm.setString(1,"            ".substring(0, (12 - MCUCD.length()))+ MCUCD);
			
			stm.executeQuery();
			ResultSet rs = stm.getResultSet();

			rs.next();
			retorno = Integer.toString(rs.getInt("keyGrp") + 1);

		} finally {
			if (stm != null) {
				stm.close();

			}
		}
		
		return retorno;
     }
			

	public List<GrupoAtendimento> queryAll() throws SQLException {
		StringBuilder query = new StringBuilder("SELECT * FROM GRUPO_ATENDIMENTO ORDER BY GAT_NU");
		List<GrupoAtendimento> lista = new ArrayList<GrupoAtendimento>();

		UtilLog.logger.debug(query);

		PreparedStatement stm = null;
		
		try {
			stm = conexao.prepareStatement(query.toString());
					
			stm.executeQuery();
			ResultSet rs = stm.getResultSet();


			while (rs.next()) {

				GrupoAtendimento grp = new GrupoAtendimento();
				grp.setNumGrupo(rs.getInt("GAT_NU"));
				grp.setDescricao(rs.getString("GAT_NO"));
				grp.setNumcd(rs.getString("MCMCU_CENTRO_DISTRIBUICAO"));
				grp.setExterceiro(rs.getInt("GAT_QT_PEDIDO_EXTRA_TERCEIRO"));
				grp.setExpropria(rs.getInt("GAT_QT_PEDIDO_EXTRA_ECT"));
				grp.setCatgrupo(rs.getString("GAT_IN_ITEM_PEDIDO"));

				lista.add(grp);

			}

		} finally {
			if (stm != null) {
				stm.close();

			}
		}
		
		return lista;
     }
		
	
	public List<GrupoAtendimento> findAll(String MCUCD) throws SQLException {
		List<GrupoAtendimento> lista = new ArrayList<GrupoAtendimento>();
		StringBuilder query = new StringBuilder("SELECT * FROM GRUPO_ATENDIMENTO ");
		query.append("WHERE MCMCU_CENTRO_DISTRIBUICAO = ? ");

		UtilLog.logger.debug(query);

		PreparedStatement stm = null;
		
		try {
			stm = conexao.prepareStatement(query.toString());
			stm.setString(1,"            ".substring(0, (12 - MCUCD.length()))+ MCUCD);
			
			stm.executeQuery();
			ResultSet rs = stm.getResultSet();


			while (rs.next()) {

				GrupoAtendimento grp = new GrupoAtendimento();
				grp.setNumGrupo(rs.getInt("GAT_NU"));
				grp.setDescricao(rs.getString("GAT_NO"));
				grp.setNumcd(rs.getString("MCMCU_CENTRO_DISTRIBUICAO"));
				grp.setExterceiro(rs.getInt("GAT_QT_PEDIDO_EXTRA_TERCEIRO"));
				grp.setExpropria(rs.getInt("GAT_QT_PEDIDO_EXTRA_ECT"));
				grp.setCatgrupo(rs.getString("GAT_IN_ITEM_PEDIDO"));

				lista.add(grp);

				grp = null;
			}

		} finally {
			if (stm != null) {
				stm.close();

			}
		}
		
		return lista;
	}
	
	public Paginador<GrupoAtendimento> listaTodosGruposDeAtendimentoPaginado(Paginador<GrupoAtendimento> p) throws SQLException {
		List<GrupoAtendimento> lista = new ArrayList<GrupoAtendimento>();
		StringBuilder query = new StringBuilder("select * from (select colunas.*, ROWNUM rnum from (SELECT * FROM GRUPO_ATENDIMENTO ");
		query.append("WHERE MCMCU_CENTRO_DISTRIBUICAO = ? order by ROWID) colunas where ROWNUM <= ?) where rnum > ?  ");

		UtilLog.logger.debug(query);
		
		String queryCount = "select count(*) as total from GRUPO_ATENDIMENTO WHERE MCMCU_CENTRO_DISTRIBUICAO = ? order by ROWID"; 

		PreparedStatement stm = null;
		
		PreparedStatement stmCount = null;
		
		try {
			
			stmCount = conexao.prepareStatement(queryCount);
			stmCount.setString(1, p.getEntityBean().getMcuCd());
			ResultSet rsCount = stmCount.executeQuery();
			if(rsCount.next()){
				p.setTotalDeRegistros(rsCount.getLong("total"));
			}
			rsCount.close();
			
			
			stm = conexao.prepareStatement(query.toString());
			stm.setString(1,"            ".substring(0, (12 - p.getEntityBean().getMcuCd().length()))+ p.getEntityBean().getMcuCd());
			stm.setInt(2, p.getPrimeiroRegistro() + p.getQuantidadeDeRegistrosPorPagina()); // linha final
			stm.setInt(3, p.getPrimeiroRegistro()); // linha inicial
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				GrupoAtendimento grp = new GrupoAtendimento();
				grp.setNumGrupo(rs.getInt("GAT_NU"));
				grp.setDescricao(rs.getString("GAT_NO"));
				grp.setNumcd(rs.getString("MCMCU_CENTRO_DISTRIBUICAO"));
				grp.setExterceiro(rs.getInt("GAT_QT_PEDIDO_EXTRA_TERCEIRO"));
				grp.setExpropria(rs.getInt("GAT_QT_PEDIDO_EXTRA_ECT"));
				grp.setCatgrupo(rs.getString("GAT_IN_ITEM_PEDIDO"));

				lista.add(grp);

			}
			rs.close();
			p.setColecaoDeRegistros(lista);

		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		
		return p;
	}
	
	


	public List<GrupoAtendimento> findByUserGrupo(String MCUOrgao,
			String MCUCD) throws SQLException {
		List<GrupoAtendimento> lista = new ArrayList<GrupoAtendimento>();
		
		StringBuilder query = new StringBuilder("SELECT A.GAT_NU, B.GAT_NO,  A.MCMCU_CENTRO_DISTRIBUICAO, B.GAT_IN_ITEM_PEDIDO ");
		query.append("FROM UNIDADE_SOLIC_GRUPO_ATEND A, GRUPO_ATENDIMENTO B ");
		query.append("WHERE A.MCMCU_UNIDADE_SOLICITANTE = ? ");
		query.append("AND A.MCMCU_CENTRO_DISTRIBUICAO = ? ");
		query.append("AND A.MCMCU_CENTRO_DISTRIBUICAO = B.MCMCU_CENTRO_DISTRIBUICAO ");
		query.append("AND A.GAT_NU = B.GAT_NU ");
		
		UtilLog.logger.debug(query);

		PreparedStatement stm = null;
		
		try {
			stm = conexao.prepareStatement(query.toString());
			stm.setString(1,"            ".substring(0, (12 - MCUCD.length()))+ MCUOrgao);
			stm.setString(2,"            ".substring(0, (12 - MCUCD.length()))+ MCUCD);
			
			stm.executeQuery();
			ResultSet rs = stm.getResultSet();

			while (rs.next()) {

				GrupoAtendimento grp = new GrupoAtendimento();
				grp.setNumGrupo(rs.getInt("GAT_NU"));
				grp.setDescricao(rs.getString("GAT_NO"));
				grp.setNumcd(rs.getString("MCMCU_CENTRO_DISTRIBUICAO"));
				grp.setCatgrupo(rs.getString("GAT_IN_ITEM_PEDIDO"));
				
				lista.add(grp);

			}

		} finally {
			if (stm != null) {
				stm.close();

			}
		}
		
		return lista;
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
