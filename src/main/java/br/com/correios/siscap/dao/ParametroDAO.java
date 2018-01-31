package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.model.Parametro;

public class ParametroDAO {

	
	private ArrayList<Parametro> parametros = new ArrayList<Parametro>();
	
	private Connection conexao;
	
	public ParametroDAO(Connection con) {
		conexao = con;
	}
	

	
	public void onEdit(Parametro p) throws SQLException {
		
		
		Long qryModel = findByCount(p.getMcmcu_centro_distribuicao(),
				p.getPar_no());
		UtilLog.logger.debug(""+qryModel);
		if (qryModel == null) {
			// INSERT
			insert(p);
		} else {
			// UPDATE
			update(p);
		}
	}

	
	public void insert(Parametro par) throws SQLException {

				
		StringBuilder sb = new StringBuilder("INSERT INTO PARAMETRO_SISTEMA (PAR_NU,CCCO_DR,GAT_NU,MCMCU_ORGAO_SOLICITANTE,PAR_NO,PAR_IN_NATUREZA,PAR_VR,PAR_DT_VIGENCIA_INCIAL,PAR_DT_VIGENCIA_FINAL,MCMCU_CENTRO_DISTRIBUICAO) VALUES ");
		sb.append("(?,?,?,?,?,?,?,?,?,?)");

		PreparedStatement stm = null;
		try{
			String query = sb.toString();
			stm = conexao.prepareStatement(query);
			stm.setLong(1, getNextVal());
			stm.setString(2, par.getCcco_dr());
			stm.setString(3, par.getGat_nu());
			stm.setString(4, par.getDrky_tipo_orgao());
			stm.setString(5, par.getPar_no());
			stm.setString(6, par.getPar_in_natureza());
			stm.setLong(7, par.getPar_vr());
			stm.setDate(8, new java.sql.Date(par.getPar_dt_vigencia_inicial().getTime()));
			stm.setDate(9, new java.sql.Date(par.getPar_dt_vigencia_final().getTime()));
			stm.setString(10, "            ".substring(0, (12 - par.getMcmcu_centro_distribuicao().length())) + par.getMcmcu_centro_distribuicao());
			UtilLog.logger.debug(query);
			stm.executeQuery();
			
		}finally{
			if(stm != null){
				stm.close();			
			}
		}

	}

	public void update(Parametro par) throws SQLException {
		
		
		PreparedStatement stmUpdate = null;
		
		StringBuilder sb = new StringBuilder("UPDATE PARAMETRO_SISTEMA SET ");
		sb.append("PAR_NU = ?, CCCO_DR = ?, GAT_NU = ?, MCMCU_ORGAO_SOLICITANTE = ?, PAR_NO = ?, PAR_IN_NATUREZA = ?, PAR_VR = ?, PAR_DT_VIGENCIA_INCIAL = ?, PAR_DT_VIGENCIA_FINAL = ?, MCMCU_CENTRO_DISTRIBUICAO = ? ");
		
		
		String queryUpdate = sb.toString();
		
		UtilLog.logger.debug(queryUpdate);
		
		try{
			stmUpdate = conexao.prepareStatement(queryUpdate);
			stmUpdate.setLong(1, par.getPar_nu());
			stmUpdate.setString(2, par.getCcco_dr());
			stmUpdate.setString(3, par.getGat_nu());
			stmUpdate.setString(4, par.getDrky_tipo_orgao());
			stmUpdate.setString(5, par.getPar_no());
			stmUpdate.setString(6, par.getPar_in_natureza());
			stmUpdate.setLong(7, par.getPar_vr());
			stmUpdate.setDate(8, new java.sql.Date(par.getPar_dt_vigencia_inicial().getTime()));
			stmUpdate.setDate(9, new java.sql.Date(par.getPar_dt_vigencia_final().getTime()));
			stmUpdate.setString(10, "            ".substring(0, (12 - par.getMcmcu_centro_distribuicao().length())) + par.getMcmcu_centro_distribuicao());
			stmUpdate.executeUpdate();
		}finally{
			if(stmUpdate != null){
				stmUpdate.close();				
			}
		}

	}

	public void delete(Parametro par) throws SQLException {
		
		Long qryModel;

		qryModel = findByCount(par.getMcmcu_centro_distribuicao(), par.getPar_no());

		par.setPar_nu(qryModel);

		String vQuery = "DELETE PARAMETRO_SISTEMA  where par_nu = " + par.getPar_nu() ;
		
		UtilLog.logger.debug(vQuery);

		PreparedStatement stm = null;
		try{
			stm  = conexao.prepareStatement(vQuery);
			stm.executeQuery(vQuery);
			
		}finally{
			if(stm != null){
				stm.close();
			}
		}
	}

	public ArrayList<Parametro> findByPar() throws SQLException {
		
		PreparedStatement pst = null;
		
		try {
			String vQuery = "select PAR_NU, CCCO_DR, GAT_NU, PAR_IN_ORGAO_SOLICITANTE, PAR_NO, PAR_IN_NATUREZA, PAR_VR, PAR_DT_VIGENCIA_INCIAL, "
					+ "PAR_DT_VIGENCIA_FINAL, MCMCU_CENTRO_DISTRIBUICAO from PARAMETRO_SISTEMA order by PAR_NO";


			pst = conexao.prepareStatement(vQuery);

			ResultSet rs = pst.executeQuery();

			parametros.clear();

			while (rs.next()) {
				Parametro pa = new Parametro();
				pa.setPar_nu(rs.getLong("PAR_NU"));
				pa.setCcco_dr(rs.getString("CCCO_DR"));
				pa.setGat_nu(Long.toString(rs.getLong("GAT_NU")));
				pa.setDrky_tipo_orgao(rs.getString("PAR_IN_ORGAO_SOLICITANTE"));
				pa.setPar_no(rs.getString("PAR_NO"));
				pa.setPar_in_natureza(rs.getString("PAR_IN_NATUREZA"));
				pa.setPar_vr(rs.getLong("PAR_VR"));
				pa.setPar_dt_vigencia_inicial(rs
						.getDate("PAR_DT_VIGENCIA_INCIAL"));
				pa.setPar_dt_vigencia_final(rs.getDate("PAR_DT_VIGENCIA_FINAL"));
				pa.setMcmcu_centro_distribuicao(rs
						.getString("MCMCU_CENTRO_DISTRIBUICAO"));

				parametros.add(pa);
			}
			rs.close();
			

			return parametros;

		} finally{
			if(pst != null){
				pst.close();				
			}
		}

	}

	public ArrayList<Parametro> findByParametro(String mcmcu) throws SQLException {
		PreparedStatement stm = null;
		try {
			if ((mcmcu != null)) {
				
//				PAR_NU
//				CCCO_DR
//				GAT_NU
//				PAR_NO
//				PAR_IN_NATUREZA
//				PAR_VR
//				PAR_DT_VIGENCIA_INCIAL
//				PAR_DT_VIGENCIA_FINAL
//				MCMCU_CENTRO_DISTRIBUICAO
//				MCMCU_ORGAO_SOLICITANTE
//				DRKY_TIPO_ORGAO

				StringBuilder query = new StringBuilder("select PAR_NU, CCCO_DR, GAT_NU, MCMCU_ORGAO_SOLICITANTE, PAR_NO, PAR_IN_NATUREZA, PAR_VR, PAR_DT_VIGENCIA_INCIAL, "); 
				query.append(" PAR_DT_VIGENCIA_FINAL, MCMCU_CENTRO_DISTRIBUICAO from PARAMETRO_SISTEMA ");
				query.append("where MCMCU_CENTRO_DISTRIBUICAO = ? ");
				
				stm = conexao.prepareStatement(query.toString());
				stm.setString(1, "            ".substring(0, (12 - mcmcu.length()))	+ mcmcu);
				ResultSet rs = stm.executeQuery();
				parametros.clear();

				while (rs.next()) {
					Parametro pa = new Parametro();
					pa.setPar_nu(rs.getLong("PAR_NU"));
					pa.setCcco_dr(rs.getString("CCCO_DR"));
					pa.setGat_nu(Long.toString(rs.getLong("GAT_NU")));
					pa.setDrky_tipo_orgao(rs
							.getString("MCMCU_ORGAO_SOLICITANTE"));
					pa.setPar_no(rs.getString("PAR_NO"));
					pa.setPar_in_natureza(rs.getString("PAR_IN_NATUREZA"));
					pa.setPar_vr(rs.getLong("PAR_VR"));
					pa.setPar_dt_vigencia_inicial(rs
							.getDate("PAR_DT_VIGENCIA_INCIAL"));
					pa.setPar_dt_vigencia_final(rs
							.getDate("PAR_DT_VIGENCIA_FINAL"));
					pa.setMcmcu_centro_distribuicao(rs
							.getString("MCMCU_CENTRO_DISTRIBUICAO"));

					parametros.add(pa);
				}

			}
			return parametros;

		} finally{
			if(stm != null){
				stm.close();				
			}
		}
	}

	public Long findByCount(String keyCD, String KeyParNo) throws SQLException {
		PreparedStatement stm  = null;
		Long resultado = null;
		try {
			if ((keyCD != null) && (KeyParNo != null)) {
				String vQuery = "select PAR_NU from PARAMETRO_SISTEMA where MCMCU_CENTRO_DISTRIBUICAO = '"
						+ "            ".substring(0, (12 - keyCD.length()))
						+ keyCD + "' " + " and par_no = '" + KeyParNo + "' ";
				
				stm = conexao.prepareStatement(vQuery);
				ResultSet rs = stm.executeQuery(vQuery);
				if (rs.next()) {
					resultado = rs.getLong("PAR_NU");
				}

				rs.close();

			}
			return resultado;

		} finally{
			if(stm != null){
				stm.close();
			}
			
		}
	}

	public Long getNextVal() throws SQLException {
		
		PreparedStatement stm = null;
		Long numreg = 0L;
		
		try {

			String vQuery = "select MAX(PAR_NU)+1 AS MAX from PARAMETRO_SISTEMA";
			
			stm  = conexao.prepareStatement(vQuery);
			ResultSet rs = stm.executeQuery(vQuery);
			if (rs.next()) {
				numreg = rs.getLong("MAX");
			}

			rs.close();
			

		}finally{
			if(stm != null){
				stm.close();
			}
		}
		return numreg;
	}

	public String getNumPar(String par_no) throws SQLException {
		String retorno = null;

		PreparedStatement stm = null;
		try {
			
			String vQuery = "select PAR_NU from PARAMETRO_SISTEMA where par_no = ?";

			stm  = conexao.prepareStatement(vQuery);
			stm.setString(1, par_no);
			ResultSet rs = stm.executeQuery(vQuery);
			if(rs.next()){
				retorno = rs.getString("PAR_NU");				
			}

			rs.close();
						
		}finally{
			if(stm != null){
				stm.close();
			}
		}

		return retorno;
	}
}
