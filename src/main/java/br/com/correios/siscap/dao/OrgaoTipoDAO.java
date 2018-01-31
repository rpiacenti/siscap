package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.model.OrgaoTipo;

public class OrgaoTipoDAO  {

	private Connection conexao;
	
	public OrgaoTipoDAO(Connection connection){
		conexao = connection;
	}

	public List<OrgaoTipo> findByKeyNumber() throws SQLException {
		
		PreparedStatement stm = null;
		List<OrgaoTipo> lista = new ArrayList<OrgaoTipo>();
		try {

			String vQuery = "select DRKY, DRDL01,DRDL02,DRSPHD from VW_F0005 order by DRKY";
			UtilLog.logger.debug(vQuery);
			
			stm = conexao.prepareStatement(vQuery);

			ResultSet rs = stm.executeQuery(vQuery);


			while (rs.next()) {

				OrgaoTipo tp = new OrgaoTipo();
				tp.setDRKY(rs.getString("DRKY"));
				tp.setDRDL01(rs.getString("DRDL01"));
				tp.setDRDL02(rs.getString("DRDL02"));
				tp.setDRSPHD(rs.getString("DRSPHD"));

				lista.add(tp);
			}
			rs.close();

			return lista;

		}finally{
			if(stm != null){
				stm.close();				
			}
			
		}
	}

}
