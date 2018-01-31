package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.model.Dr;

public class DRDAO {

	private Connection conexao;

	public DRDAO(Connection c) {
		conexao = c;
	}

	public List<Dr> findByKeyNumber(String mcuCd) throws SQLException {
		List<Dr> lista = new ArrayList<Dr>();
		if (mcuCd != null) {

			StringBuilder query = new StringBuilder("SELECT CCCO, CCNAME, CCAN8 from VW_F0010 WHERE CCCO in (SELECT CCCO_DR FROM CENTRO_DISTRIBUICAO_DR ");
			query.append("WHERE MCMCU_CENTRO_DISTRIBUICAO = ? )");
			query.append(" order by CCCO");

			UtilLog.logger.debug(query);

			PreparedStatement stm = null;
			
			String[] drList = {"00001;CS - SEDE;1;00004010",
					"00003;SE - ACRE;3;00004010",
					"00004;SE - ALAGOAS;4;00004010",
					"00005;SE - AMAPÁ;5;00004010",
					"00006;SE - AMAZONAS;6;00004010",
					"00008;SE - BAHIA;8;00004010",
					"00010;SE - BRASÍLIA;10;00004010",
					"00012;SE - CEARÁ;12;00004010",
					"00014;SE - ESPIRITO SANTO;14;00049748",
					"00016;SE - GOIÁS;16;00004010",
					"00018;SE - MARANHÃO;18;00004010",
					"00020;SE - MINAS GERAIS;20;00004010",
					"00022;SE - MATO GROSSO DO SUL;22;00049748",
					"00024;SE - MATO GROSSO;24;00004010",
					"00026;SE - RONDONIA;26;00004010",
					"00028;SE - PARÁ;28;00004010",
					"00030;SE - PARAÍBA;30;00004010",
					"00032;SE - PERNAMBUCO;32;00004010",
					"00034;SE - PIAUÍ;34;00004010",
					"00036;SE - PARANÁ;36;00049748",
					"00050;SE - RIO DE JANEIRO;50;00049748",
					"00060;SE - RIO GRANDE DO NORTE;60;00004010",
					"00064;SE - RIO GRANDE DO SUL;64;00049748",
					"00065;SE - RORAIMA;65;00004010",
					"00068;SE - SANTA CATARINA;68;00049748",
					"00070;SE - SERGIPE;70;00004010",
					"00072;SE - SÃO PAULO METROPOLITANA;72;00049748",
					"00074;SE - SÃO PAULO INTERIOR;74;00049748",
					"00075;SE - TOCANTINS;75;00004010"};
			
			
			for (String item : drList) {
				String[] aItem = item.split(";");
				if (aItem[3].trim().indexOf(mcuCd.trim()) > -1) {
					Dr dr = new Dr();
					dr.setCCCO(aItem[0]);
					dr.setCCNAME(aItem[1]);
					dr.setCCAN8(aItem[2]);
					dr.setMCUCD(mcuCd);

					lista.add(dr);
				}
			}
//			try {
//				stm = conexao.prepareStatement(query.toString());
//				stm.setString(1,"            ".substring(0, (12 - mcuCd.length()))	+ mcuCd);
//
//				stm.executeQuery();
//				ResultSet rs = stm.getResultSet();
//
//
//				while (rs.next()) {
//
//					Dr dr = new Dr();
//					dr.setCCCO(rs.getString("CCCO"));
//					dr.setCCNAME(rs.getString("CCNAME"));
//					dr.setCCAN8(rs.getString("CCAN8"));
//					dr.setMCUCD(mcuCd);
//
//					lista.add(dr);
//
//				}
//
//			} finally {
//				if (stm != null) {
//					stm.close();
//
//				}
//			}
			return lista;
		} else {
			return null;
		}
	}

	public List<Dr> getDR() throws SQLException{
		PreparedStatement stm = null;

		PreparedStatement stmCount = null;

		ArrayList<Dr> lDR = new ArrayList<Dr>();
		
		try {

			String query = "select distinct MCRP01, MCDL01 from vw_F0006 " 
					+"where substr(MCDL01,1,4) = 'SE -'";

			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				Dr tDR = new Dr();
				
				tDR.setCCNAME(rs.getString("MCDL01"));
				tDR.setMCRP01(rs.getString("MCRP01"));
			
				lDR.add(tDR);
				tDR = null;
			}
			rs.close();

		} finally {
			if (stm != null) {
				stm.close();
			}
		}

		return lDR;
	}
    
}
