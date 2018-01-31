package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.correios.ppjsiscap.enums.NATUREZAORGAO;
import br.com.correios.ppjsiscap.paginador.Paginador;
import br.com.correios.ppjsiscap.util.DataERP;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.model.Orgao;

public class OrgaoDAO {

	private Connection conexao;

	public OrgaoDAO(Connection c) {
		conexao = c;
	}

	public Object findByMCU(String mcu) throws SQLException {
		Statement stm = null;
		try {
			if ((mcu != null)) {
				String vQuery = "select * from VW_F0006  "
						+ "where MCMCU = lpad('"+mcu.trim()+"',12,' ')' ";

				stm = conexao.createStatement();
				ResultSet rs = stm.executeQuery(vQuery);
				boolean retorno = rs.next();
				rs.close();
				stm.close();

				return retorno;
			}
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		return false;
	}

	public String[] findByContraoOrgao(String mcu) throws SQLException {

		String vQuery = "SELECT  D.DNDMSC, D.DNEFTJ, D.DNEXDJ FROM VW_F0006 A, VW_F38010 D "
				+ " WHERE A.MCMCU = ? and A.MCAN8 = D.DNAN8 ";
		UtilLog.logger.debug("Recuperando Contrato: " + vQuery);

		PreparedStatement stm = null;

		String[] resultado = null;

		try {

			stm = conexao.prepareStatement(vQuery);
			stm.setString(1, "            ".substring(0, (12 - mcu.length()))
					+ mcu);

			ResultSet rs = stm.executeQuery();

			if (rs.next()) {
				resultado = new String[3];

				resultado[0] = rs.getString("DNDMSC");
				resultado[1] = rs.getString("DNEFTJ");
				resultado[2] = rs.getString("DNEXDJ");

				rs.close();
			}
			return resultado;

		} finally {
			if (stm != null) {
				stm.close();
			}
		}

	}

	public Orgao findByOrgao(String mcu) throws SQLException {

		PreparedStatement stm = null;
		

		Orgao orgao = null;
		try {
			if (mcu != null) {

				StringBuilder query = new StringBuilder(
						"SELECT distinct A.MCMCU, A.MCSTYL, A.MCAN8, A.MCDL01, A.MCDL02, ");
				query.append(" concat('000',A.MCRP01) as MCRP01, A.MCRP02, A.MCPECC, A.MCRP12,  B.EAEMAL, ");
				query.append(" C.MCMCU_CENTRO_DISTRIBUICAO AS MCMCUCIA FROM VW_F0006 A, VW_F01151 B, CENTRO_DISTRIBUICAO_DR C ");
				query.append(" WHERE A.MCMCU = ? and A.MCAN8 =  B.EAAN8 and concat('000',A.MCRP01) = C.CCCO_DR");

				UtilLog.logger.debug(query);

				stm = conexao.prepareStatement(query.toString());
				stm.setString(1,
						"            ".substring(0, (12 - mcu.length())) + mcu);
				ResultSet rs = stm.executeQuery();

				if (rs.next()) {
					String uf = rs.getString("MCRP12").trim();
					String cia = "";
					orgao = new Orgao();
					orgao.setMcu(rs.getString("MCMCU"));
					orgao.setAn8(rs.getString("MCAN8"));
					orgao.setTipo(rs.getString("MCSTYL"));
					orgao.setNome(rs.getString("MCDL01"));
					orgao.setSigla(rs.getString("MCDL02"));
					orgao.setDr(rs.getString("MCRP01"));
					orgao.setUf(rs.getString("MCRP12"));
					orgao.setNatureza(rs.getString("MCRP02"));
					orgao.setStatus(rs.getString("MCPECC"));
					orgao.setEmail(rs.getString("EAEMAL").toLowerCase());
					if("ES,MS,PR,RJ,RS,SC,SP".indexOf(uf) > -1){
						cia = "    00049748";
					}else{
						cia = "    00004010";
					}
					orgao.setMcucia(cia);

				}
				rs.close();
			}
			return orgao;

		} finally {
			if (stm != null) {
				stm.close();
			}
		}

	}

	public List<Orgao> findAll(String mcuCd) throws SQLException {
		PreparedStatement ps = null;
		List<Orgao> orgaos = new ArrayList<Orgao>();
		try {
			if ((mcuCd != null)) {
				
//				StringBuilder query = new StringBuilder(
//						"SELECT MCMCU,MCSTYL,MCAN8,MCDL01,MCDL02,MCRP01,MCRP02,MCPECC,MCRP12 FROM VW_F0006 ");
//				query.append(" WHERE MCSTYL in (SELECT DISTINCT LTRIM(DRKY) from VW_F0005) AND MCRP02 IN ('OPE','ADM')");
//				query.append(" AND MCSTYL NOT IN ('15','18','21','24')  AND MCPECC = '2' AND CONCAT('000',MCRP01) IN ");
//				query.append(" (SELECT CCCO_DR FROM CENTRO_DISTRIBUICAO_DR WHERE MCMCU_CENTRO_DISTRIBUICAO = ?)  ORDER BY MCDL01");
				
				StringBuilder query = new StringBuilder(
						"SELECT MCMCU,MCSTYL,MCAN8,MCDL01,MCDL02,MCRP01,MCRP02,MCPECC,MCRP12 FROM VW_F0006 ");
				query.append(" WHERE MCSTYL in (SELECT DISTINCT LTRIM(DRKY) from VW_F0005) AND MCRP02 IN ('OPE','ADM')");
				query.append(" AND MCSTYL NOT IN ('15','18','21','24')  AND MCPECC = '2' ");
				
				if(mcuCd.indexOf("00049748") > -1){
					query.append(" AND MCRP12 IN ('ES','MS','PR','RJ','RS','SC','SP') ");
				}else{
					query.append(" AND MCRP12 NOT IN ('ES','MS','PR','RJ','RS','SC','SP') ");
				}
				
				query.append(" ORDER BY MCDL01");

				UtilLog.logger.debug(query.toString());

				ps = conexao.prepareStatement(query.toString());
				
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {

					Orgao orgao = new Orgao();
					orgao.setMcu(rs.getString("MCMCU"));
					orgao.setAn8(rs.getString("MCAN8"));
					orgao.setTipo(rs.getString("MCSTYL"));
					orgao.setNome(rs.getString("MCDL01"));
					orgao.setDr(rs.getString("MCRP01"));
					orgao.setUf(rs.getString("MCRP12"));
					orgao.setNatureza(rs.getString("MCRP02"));
					orgao.setStatus(rs.getString("MCPECC"));

					orgaos.add(orgao);

				}
				rs.close();

			}
			return orgaos;

		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public List<Orgao> pageALL(String mcuCd, long pRow, int pLim)
			throws SQLException {
		PreparedStatement stm = null;
		List<Orgao> orgaos = new ArrayList<Orgao>();
		try {
			if ((mcuCd != null)) {
				StringBuilder query = new StringBuilder(
						"SELECT MCMCU,MCSTYL,MCAN8,MCDL01,MCDL02,MCRP01,MCRP02,MCPECC,MCRP12 FROM VW_F0006 ");
				query.append(" WHERE MCSTYL in (SELECT DISTINCT LTRIM(DRKY) from VW_F0005) AND MCRP02 IN ('OPE','ADM')");
				query.append(" AND MCSTYL NOT IN ('15','18','21','24')  AND MCPECC = '2' ");
				
				if(mcuCd.indexOf("00049748") > -1){
					query.append(" AND MCRP12 IN ('ES','MS','PR','RJ','RS','SC','SP') ");
				}else{
					query.append(" AND MCRP12 NOT IN ('ES','MS','PR','RJ','RS','SC','SP') ");
				}
				
				query.append(" ORDER BY MCDL01");
				long regIni = (pRow);
				long regFim = (pRow + pLim);

				String queryPaginador = " select * "
						+ "from (select a.*, rownum rnum" + " from (" + query
						+ ", rowid) a " + " where rownum <= " + regFim + ") "
						+ " where rnum >= " + regIni;
				UtilLog.logger.debug("vQueryPag: " + queryPaginador);

				stm = conexao.prepareStatement(query.toString());
				
				ResultSet rs = stm.executeQuery(queryPaginador);

				while (rs.next()) {

					Orgao orgao = new Orgao();
					orgao.setMcu(rs.getString("MCMCU"));
					orgao.setAn8(rs.getString("MCAN8"));
					orgao.setTipo(rs.getString("MCSTYL"));
					orgao.setNome(rs.getString("MCDL01"));
					orgao.setDr(rs.getString("MCRP01"));
					orgao.setUf(rs.getString("MCRP12"));
					orgao.setNatureza(rs.getString("MCRP02"));
					orgao.setStatus(rs.getString("MCPECC"));
					orgao.setPag_atual(rs.getLong("rnum"));

					orgaos.add(orgao);

				}
				rs.close();

			}
			return orgaos;
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
	}

	public boolean getAcessoOrgaoGrupo(String mcuSolicitante, String mcuCd)
			throws SQLException {

		Date dtNow = new Date();
		Calendar calendarData = Calendar.getInstance();
		calendarData.setTime(dtNow);
		int mes = calendarData.get(Calendar.MONTH) + 1;
		int ano = calendarData.get(Calendar.YEAR);
		
		boolean retorno = false;

		String query = "select A.GAT_NU, A.MCMCU_UNIDADE_SOLICITANTE, B.CRO_DT_INI_SOLICITACAO_PEDIDO AS DT_INI, B. CRO_DT_PROCESSAMENTO AS DT_FIM "
				+ "from UNIDADE_SOLIC_GRUPO_ATEND A, CRONOGRAMA_ATENDIMENTO B, GRUPO_ATENDIMENTO C "
				+ "where A.MCMCU_UNIDADE_SOLICITANTE = ? "
				+ "AND C.GAT_NU = A.GAT_NU "
				+ "and C.gat_nu = B.GAT_NU "
				+ "AND C.MCMCU_CENTRO_DISTRIBUICAO = A.MCMCU_CENTRO_DISTRIBUICAO "
				+ "AND B.CRO_AN = ? AND B.CRO_ME = ? and B.MCMCU_CENTRO_DISTRIBUICAO = ? ";

		UtilLog.logger.debug(query);

		PreparedStatement stm = null;
		try {
			
			String dtIni;
			String dtFim;
			DataERP dtERP = new DataERP();

			Long hojeDtErp = Long.parseLong(dtERP.getDataERPToday());

			stm = conexao.prepareStatement(query);
			stm.setString(1,
					"            ".substring(0, (12 - mcuSolicitante.trim().length()))
							+ mcuSolicitante.trim());
			stm.setInt(2, ano);
			stm.setInt(3, mes);
			stm.setString(4, "            ".substring(0, (12 - mcuCd.trim().length()))
					+ mcuCd.trim());
			ResultSet rs = stm.executeQuery();

			

			while (rs.next()) {
				dtIni = rs.getString("DT_INI").substring(0, 10);

				String[] aDtIni = dtIni.split("-");

				String tDtIni = aDtIni[2] + "/" + aDtIni[1] + "/" + aDtIni[0];

				long ldtIni = Long.parseLong(dtERP.getDataERP(tDtIni));

				dtFim = rs.getString("DT_FIM").substring(0, 10);
				String[] aDtFim = dtFim.split("-");

				String tDtFim = aDtFim[2] + "/" + aDtFim[1] + "/" + aDtFim[0];

				long ldtFim = Long.parseLong(dtERP.getDataERP(tDtFim));

				if (!((hojeDtErp >= ldtIni) && (hojeDtErp <= ldtFim)))  {
					retorno = true;
					break;
				}
			}
			rs.close();

		} finally {
			if (stm != null) {
				stm.close();
			}
		}

		return retorno;

	}

	public Paginador<Orgao> listaTodosOrgaoPaginado(Paginador<Orgao> p)
			throws SQLException {
		
		String mcstyl = NATUREZAORGAO.ALL.getCodigo();
		
		List<Orgao> lista = new ArrayList<Orgao>();
		
		StringBuilder filtro = new StringBuilder();
		
		String mcuCd = p.getEntityBean().getMcucia().trim();

		adicionaFiltro(p, filtro);

		StringBuilder query = new StringBuilder(
				"select * from (SELECT  distinct MCMCU,MCSTYL,MCAN8,MCDL01,MCDL02,MCRP01,MCRP02,MCPECC,MCRP12, ROWNUM rnum from (SELECT * FROM VW_F0006 f ");
		query.append(" WHERE 1 = 1 ");
		query.append(filtro);
		query.append(" AND MCSTYL in ("+mcstyl+") ");
		query.append(" AND MCRP02 IN ('OPE','ADM')");
		query.append(" AND MCSTYL NOT IN ('15','18','21','24')  AND MCPECC = '2' ");
		
		if(mcuCd.indexOf("00049748") > -1){
			query.append(" AND MCRP12 IN ('ES','MS','PR','RJ','RS','SC','SP') ");
		}else{
			query.append(" AND MCRP12 NOT IN ('ES','MS','PR','RJ','RS','SC','SP') ");
		}
		
		query.append(" order by MCMCU) colunas where ROWNUM <= ?) where rnum > ?  ");

		UtilLog.logger.debug(query);

		StringBuilder queryCount = new StringBuilder(
				"SELECT count(*) as total FROM VW_F0006 f");
		queryCount.append(" WHERE 1 = 1");
		queryCount.append(filtro);
		queryCount.append(" and f.MCSTYL in ("+mcstyl+") ");
		queryCount.append(" AND f.MCRP02 IN ('OPE','ADM')");
		queryCount.append(" AND MCSTYL NOT IN ('15','18','21','24')  AND MCPECC = '2' ");
		
		if(mcuCd.indexOf("00049748") > -1){
			queryCount.append(" AND MCRP12 IN ('ES','MS','PR','RJ','RS','SC','SP') ");
		}else{
			queryCount.append(" AND MCRP12 NOT IN ('ES','MS','PR','RJ','RS','SC','SP') ");
		}
		
		queryCount.append(" order by ROWID");

		PreparedStatement stm = null;

		PreparedStatement stmCount = null;

		try {

			stmCount = conexao.prepareStatement(queryCount.toString());
			
			ResultSet rsCount = stmCount.executeQuery();
			if (rsCount.next()) {
				p.setTotalDeRegistros(rsCount.getLong("total"));
			}
			rsCount.close();

			stm = conexao.prepareStatement(query.toString());
			stm.setInt(
					1,
					p.getPrimeiroRegistro()
							+ p.getQuantidadeDeRegistrosPorPagina()); // linha
																		// final
			stm.setInt(2, p.getPrimeiroRegistro()); // linha inicial
			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				Orgao org = new Orgao();
				org.setMcu(rs.getString("MCMCU"));
				org.setAn8(rs.getString("MCAN8"));
				org.setTipo(rs.getString("MCSTYL"));
				org.setNome(rs.getString("MCDL01"));
				org.setDr(rs.getString("MCRP01"));
				org.setUf(rs.getString("MCRP12"));
				org.setNatureza(rs.getString("MCRP02"));
				org.setStatus(rs.getString("MCPECC"));
				
				org.setPag_atual(rs.getLong("rnum"));
				if(checaGrupoAtendimento(rs.getString("MCMCU"),p.getEntityBean().getMcucia(), p.getEntityBean().getGrptransito())){
					org.setMarcado(true);
				}else{
					org.setMarcado(false);
				}
	
				lista.add(org);

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
	
	private boolean checaGrupoAtendimento(String mcu, String mcucia, int grp)
			throws SQLException {
		PreparedStatement stmGrp = null;
		boolean retorno = false;
		try{
			
			StringBuilder query = new StringBuilder("Select distinct * from UNIDADE_SOLIC_GRUPO_ATEND ");
			query.append("where GAT_NU = " + grp);
			query.append(" and trim(MCMCU_UNIDADE_SOLICITANTE) = " + mcu.trim());
			query.append(" and trim(MCMCU_CENTRO_DISTRIBUICAO) = " + mcucia.trim());
		
			stmGrp = conexao.prepareStatement(query.toString());
		
			ResultSet rs = stmGrp.executeQuery();

			if (rs.next()) {
				retorno = true;
			}
		
		} finally {
			if (stmGrp != null) {
				stmGrp.close();
			}
		}
		return retorno;
	}

	private void adicionaFiltro(Paginador<Orgao> p,
			StringBuilder filtro) {
		if (p.getEntityBean().getMcu() != null
				&& !p.getEntityBean().getMcu().equals("")) {
			filtro.append(" and trim(f.mcmcu) like '"
					+ p.getEntityBean().getMcu().toUpperCase() + "%' ");
		}
		if (p.getEntityBean().getNome() != null
				&& !p.getEntityBean().getNome().equals("")) {
			filtro.append(" and f.MCDL01 like '%"
					+ p.getEntityBean().getNome()
							.toUpperCase() + "%' ");
		}
		if (p.getEntityBean().getDr() != null
				&& !p.getEntityBean().getDr().equals("")) {
		
			filtro.append(" and f.MCRP01 = '" + p.getEntityBean().getDr() + "' ");
		}
	}
	

}
