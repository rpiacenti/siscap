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
import br.com.correios.ppjsiscap.util.UtilData;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.excecao.SiscapException;
import br.com.correios.siscap.model.Auditoria;
import br.com.correios.siscap.model.Cronograma;
import br.com.correios.siscap.model.GrupoAtendimento;

public class CronogramaDAO {
	
	private Usuario usuario;

	private Connection conexao;

	public CronogramaDAO(Connection c, Usuario pUsuario) {
		conexao = c;
		this.usuario = pUsuario;
	}

	private ArrayList<Cronograma> aCrono = new ArrayList<Cronograma>();

	

	public void onEdit(Object c) throws ClassNotFoundException, SQLException {
		Cronograma croModel = (Cronograma) c;
		int qryModel;

		qryModel = this.findByCount(croModel.getGat_co_cia(),
				croModel.getGat_nu(), croModel.getCro_an(),
				Integer.toString(croModel.getNumMes(croModel.getCro_me())));

		// System.out.println("BUSCA DE REGISTROS: "+qryModel);
		if (qryModel == 0) {
			this.insert(c);
		} else {
			this.update(c);
		}

	}

	public void insert(Object c) throws ClassNotFoundException, SQLException {
		Cronograma croModel = (Cronograma) c;

		StringBuilder vQuery = new StringBuilder(
				"INSERT INTO CRONOGRAMA_ATENDIMENTO (MCMCU_CENTRO_DISTRIBUICAO,GAT_NU, CRO_AN,CRO_ME,CRO_DT_INI_SOLICITACAO_PEDIDO,CRO_DT_FIM_SOLICITACAO_PEDIDO,CRO_DT_PROCESSAMENTO) ");
		vQuery.append(" VALUES (?, ?, ?, ?, ?, ?, ?)");

		UtilLog.logger.debug(vQuery);
		if (croModel.getGat_co_cia() != null && croModel.getGat_nu() > 0
				&& croModel.getNumMes(croModel.getCro_me()) > 0
				&& Integer.parseInt(croModel.getCro_an()) > 2013) {
			PreparedStatement stm = null;
			try {
				stm = conexao.prepareStatement(vQuery.toString());
				stm.setString(
						1,
						"            ".substring(0, (12 - croModel
								.getGat_co_cia().length()))
								+ croModel.getGat_co_cia());
				stm.setInt(2, croModel.getGat_nu());
				stm.setString(3, croModel.getCro_an());
				stm.setInt(4, croModel.getNumMes(croModel.getCro_me()));
				stm.setDate(
						5,
						new java.sql.Date(new UtilData().parse(
								croModel.getCro_dt_ini_solicitacao(),
								"dd/MM/yyyy").getTime()));
				stm.setDate(
						6,
						new java.sql.Date(new UtilData().parse(
								croModel.getCro_dt_fim_solicitacao(),
								"dd/MM/yyyy").getTime()));
				stm.setDate(
						7,
						new java.sql.Date(new UtilData().parse(
								croModel.getCro_dt_processamento(),
								"dd/MM/yyyy").getTime()));

				stm.executeQuery();
				String detEvento = croModel.getGat_co_cia() + ";" + croModel.getGat_nu() + ";" + croModel.getCro_an() + ";" + croModel.getNumMes(croModel.getCro_me()) + ";" + croModel.getCro_dt_ini_solicitacao() + ";" + croModel.getCro_dt_fim_solicitacao() + ";" + croModel.getCro_dt_processamento();
				this.setAuditoria(this.usuario.getMatricula(), vQuery.toString() + " ==>> " + detEvento, "CRONOGRAMA_ATENDIMENTO", "INCLUSAO CRONOGRAMA ATENDIMENTO",
						"Cron - " + croModel
						.getGat_co_cia().trim() + " - "+ croModel.getGat_nu());

			} catch (SiscapException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (stm != null) {
					stm.close();

				}
			}

		}

	}

	public void update(Object c) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Cronograma croModel = (Cronograma) c;

		StringBuilder vQuery = new StringBuilder(
				"UPDATE CRONOGRAMA_ATENDIMENTO SET ");
		vQuery.append("CRO_DT_INI_SOLICITACAO_PEDIDO = ?, CRO_DT_FIM_SOLICITACAO_PEDIDO = ?, CRO_DT_PROCESSAMENTO = ? ");
		vQuery.append("WHERE MCMCU_CENTRO_DISTRIBUICAO = ? ");
		vQuery.append("AND  GAT_NU = ? ");
		vQuery.append("AND CRO_AN = ? ");
		vQuery.append("AND CRO_ME = ? ");

		UtilLog.logger.debug(vQuery);

		PreparedStatement stm = null;
		try {
			stm = conexao.prepareStatement(vQuery.toString());
			stm.setDate(
					1,
					new java.sql.Date(new UtilData().parse(
							croModel.getCro_dt_ini_solicitacao(), "dd/MM/yyyy")
							.getTime()));
			stm.setDate(
					2,
					new java.sql.Date(new UtilData().parse(
							croModel.getCro_dt_fim_solicitacao(), "dd/MM/yyyy")
							.getTime()));
			stm.setDate(
					3,
					new java.sql.Date(new UtilData().parse(
							croModel.getCro_dt_processamento(), "dd/MM/yyyy")
							.getTime()));
			stm.setString(
					4,
					"            ".substring(0, (12 - croModel.getGat_co_cia()
							.length()))
							+ croModel.getGat_co_cia());
			stm.setInt(5, croModel.getGat_nu());
			stm.setString(6, croModel.getCro_an());
			stm.setInt(7, croModel.getNumMes(croModel.getCro_me()));

			stm.executeQuery();
			String detEvento = croModel.getGat_co_cia() + ";" + croModel.getGat_nu() + ";" + croModel.getCro_an() + ";" + croModel.getNumMes(croModel.getCro_me()) + ";" + croModel.getCro_dt_ini_solicitacao() + ";" + croModel.getCro_dt_fim_solicitacao() + ";" + croModel.getCro_dt_processamento();
			this.setAuditoria(this.usuario.getMatricula(), vQuery.toString() + " ==>> " + detEvento, "CRONOGRAMA_ATENDIMENTO", "ALTERAÇAO CRONOGRAMA ATENDIMENTO",
					"Cron - " + croModel
					.getGat_co_cia().trim() + " - "+ croModel.getGat_nu());

		} catch (SiscapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stm != null) {
				stm.close();

			}
		}


	}

	public void delete(Object c) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		Cronograma croModel = (Cronograma) c;

		StringBuilder vQuery = new StringBuilder(
				"DELETE CRONOGRAMA_ATENDIMENTO ");
		vQuery.append("WHERE MCMCU_CENTRO_DISTRIBUICAO = ? ");
		vQuery.append("AND  GAT_NU = ? ");
		vQuery.append("AND CRO_AN = ? ");
		vQuery.append("AND CRO_ME = ? ");

		UtilLog.logger.debug(vQuery);

		PreparedStatement stm = null;
		try {
			stm = conexao.prepareStatement(vQuery.toString());
			stm.setString(
					1,
					"            ".substring(0, (12 - croModel.getGat_co_cia()
							.length()))
							+ croModel.getGat_co_cia());
			stm.setInt(2, croModel.getGat_nu());
			stm.setString(3, croModel.getCro_an());
			stm.setInt(4, croModel.getNumMes(croModel.getCro_me()));

			stm.executeQuery();
			String detEvento = croModel.getGat_co_cia() + ";" + croModel.getGat_nu() + ";" + croModel.getCro_an() + ";" + croModel.getNumMes(croModel.getCro_me()) + ";" + croModel.getCro_dt_ini_solicitacao() + ";" + croModel.getCro_dt_fim_solicitacao() + ";" + croModel.getCro_dt_processamento();
			this.setAuditoria(this.usuario.getMatricula(), vQuery.toString() + " ==>> " + detEvento, "CRONOGRAMA_ATENDIMENTO", "EXCLUSAO CRONOGRAMA ATENDIMENTO",
					"Cron - " + croModel
					.getGat_co_cia().trim() + " - "+ croModel.getGat_nu());

		} catch (SiscapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stm != null) {
				stm.close();

			}
		}

	}

	public Object findByID(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object findByKeyNumber(String keyNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object findByKeyNumber(String MCUCD, String Grupo, String Ano)
			throws SQLException {
		// TODO Auto-generated method stub
		if ((MCUCD != null) && (Grupo != "0") && (Grupo != null)
				&& (Ano != null)) {

			StringBuilder vQuery = new StringBuilder(
					"SELECT * FROM CRONOGRAMA_ATENDIMENTO ");
			vQuery.append("WHERE MCMCU_CENTRO_DISTRIBUICAO = ? ");
			vQuery.append("AND  GAT_NU = ? ");
			vQuery.append("AND CRO_AN = ? ");

			UtilLog.logger.debug(vQuery);

			PreparedStatement stm = null;
			try {
				stm = conexao.prepareStatement(vQuery.toString());
				stm.setString(1,
						"            ".substring(0, (12 - MCUCD.length()))
								+ MCUCD);
				stm.setString(2, Grupo);
				stm.setString(3, Ano);
				stm.executeQuery();
				ResultSet rs = stm.getResultSet();

				boolean retorno = rs.next();

			} finally {
				if (stm != null) {
					stm.close();

				}
			}
		}

		return false;
	}

	public ArrayList<Cronograma> findByKeys(String MCUCD, int Grupo,
			String Ano, String Mes) throws SQLException {
		// TODO Auto-generated method stub

		
		if ((MCUCD != null) && (Grupo > 0) && (Ano != null)){ 
			
				StringBuilder vQuery = new StringBuilder("SELECT * FROM CRONOGRAMA_ATENDIMENTO ");
				vQuery.append("WHERE MCMCU_CENTRO_DISTRIBUICAO = lpad(trim(?),12,' ') ");
				vQuery.append("AND  GAT_NU = ? ");
				vQuery.append("AND CRO_AN = ? ");
				vQuery.append("AND CRO_ME = ? ");
				
				UtilLog.logger.debug(vQuery);
				
				PreparedStatement stm = null;
				try{
					stm  = conexao.prepareStatement(vQuery.toString());
					stm.setString(1, MCUCD);
					stm.setInt(2, Grupo);
					stm.setInt(3, Integer.parseInt(Ano));
					stm.setInt(4, Integer.parseInt(Mes));
					stm.executeQuery();
					ResultSet rs = stm.getResultSet();
				
				aCrono.clear();

				while (rs.next()) {

					Cronograma cro = new Cronograma();
					cro.setGat_nu(rs.getInt("GAT_NU"));
					cro.setGat_co_cia(rs.getString("MCMCU_CENTRO_DISTRIBUICAO"));
					cro.setCro_an(rs.getString("CRO_AN"));
					cro.setCro_me(rs.getString("CRO_ME"));
					cro.setCro_dt_ini_solicitacao(rs
							.getString("CRO_DT_INI_SOLICITACAO_PEDIDO"));
					cro.setCro_dt_fim_solicitacao(rs
							.getString("CRO_DT_FIM_SOLICITACAO_PEDIDO"));
					cro.setCro_dt_processamento(rs
							.getString("CRO_DT_PROCESSAMENTO"));
				
					aCrono.add(cro);

					cro = null;
				}
				}finally{
					if(stm != null){
						stm.close();
						
					}
				}
				return aCrono;
			}
		return null;
	}

	public int findByCount(String MCUCD, int Grupo, String Ano,
			String Mes) throws SQLException {
		// TODO Auto-generated method stub
		int numreg;

		
		if ((MCUCD != null) && (Grupo > 0) && (Ano != null)){ 
			
				StringBuilder vQuery = new StringBuilder("SELECT COUNT(*) AS NUMREG FROM CRONOGRAMA_ATENDIMENTO ");
				vQuery.append("WHERE MCMCU_CENTRO_DISTRIBUICAO = ? ");
				vQuery.append("AND  GAT_NU = ? ");
				vQuery.append("AND CRO_AN = ? ");
				vQuery.append("AND CRO_ME = ? ");
				
				UtilLog.logger.debug(vQuery);
				
				PreparedStatement stm = null;
				try{
					stm  = conexao.prepareStatement(vQuery.toString());
					stm.setString(1, "            ".substring(0, (12 - MCUCD.length()))+ MCUCD);
					stm.setInt(2, Grupo);
					stm.setString(3, Ano);
					stm.setString(4, Mes);
					stm.executeQuery();
					ResultSet rs = stm.getResultSet();
		
					aCrono.clear();

					rs.next();
					numreg = rs.getInt("NUMREG");
					
				}finally{
					if(stm != null){
						stm.close();
						
					}
				}
				return numreg;
				
			}
		return 0;
	}
	
	public List<GrupoAtendimento> setCronogramaAnoMes(List<GrupoAtendimento> grupos) throws SQLException{
		
		UtilData utData = new UtilData();
		
		int mes = utData.getMes(new Date());
		int ano = utData.getAno(new Date());
		
		List<GrupoAtendimento> newGrupo = new ArrayList<GrupoAtendimento>();
		
		for (GrupoAtendimento grp : grupos) {
			for(Cronograma crono : this.findByKeys(grp.getNumcd(), grp.getNumGrupo(), Integer.toString(ano), Integer.toString(mes))){
				grp.getCronograma().add(crono);
			}
			for(Cronograma crono : this.findByKeys(grp.getNumcd(), grp.getNumGrupo(), Integer.toString(ano), Integer.toString((mes+1)))){
				grp.getCronograma().add(crono);
			}
			
			newGrupo.add(grp);
	}
		
		return newGrupo;
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
