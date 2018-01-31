package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RastreamentoDAO {
	private Connection conexaoOracle;

	public RastreamentoDAO(Connection oracle) {
		conexaoOracle = oracle;
	}

	public void Ratrear() throws SQLException {

		PreparedStatement stm = null;

		try {
			List<String> aPedPen = new ArrayList<String>();
			
			String query = "Select VIN_CO, MCMCU_ORGAO_SOLICITANTE from vincula_pedido_erp "
					+ "where SDDOCO = 999999999 and VIN_DT_PEDIDO >= ADD_MONTHS(SYSDATE, -1)";

			stm = conexaoOracle.prepareStatement(query);
			ResultSet rs = stm.executeQuery(query);

			while (rs.next()) {
				aPedPen.add(rs.getString("MCMCU_ORGAO_SOLICITANTE"));
			}
			rs.close();
			stm.close();

			for (String pen : aPedPen) {

				query = "insert into vincula_pedido_erp (VIN_CO, SDDOCO, MCMCU_ORGAO_SOLICITANTE, VIN_DT_PEDIDO) "
						+ "select distinct SZEDBT, SZDOCO, lpad('"+ pen+ "',12,' ') as MCMCU_ORGAO_SOLICITANTE, (SELECT SYSDATE 'NOW' FROM DUAL) as VIN_DT_PEDIDO "
						+ "from vw_F47012 "
						+ "where SZEDBT in (select distinct vin_co from vincula_pedido_erp where MCMCU_ORGAO_SOLICITANTE =  lpad('"+ pen+ "',12,' ')) "
						+ "and SZDOCO not in (select distinct SDDOCO from vincula_pedido_erp where MCMCU_ORGAO_SOLICITANTE = lpad('"+ pen + "',12,' ') and SDDOCO <> 999999999 ) ";

				stm = conexaoOracle.prepareStatement(query);
				stm.executeQuery(query);

			}
		} finally {
			if (stm != null) {
				stm.close();
			}
		}

	}

	public void RatrearII() throws SQLException {

		PreparedStatement stm = null;

		try {
			String query = "Select MCMCU_ORGAO_SOLICITANTE as MCU from vincula_pedido_erp "
					+ "where SDDOCO = 999999999 and VIN_DT_PEDIDO >= ADD_MONTHS(SYSDATE, -1) order by VIN_DT_PEDIDO DESC ";

			stm = conexaoOracle.prepareStatement(query);
			ResultSet rs = stm.executeQuery(query);

			// Long totalReg = 0L;
			// Float fracaoReg = 0F;

			while (rs.next()) {
				String vMcu = rs.getString("MCU").trim();
//				String oraDt = "{ts '" + audModel.getAud_dh_execucao() + "'}";
				query = "insert into vincula_pedido_erp (VIN_CO, SDDOCO, MCMCU_ORGAO_SOLICITANTE, VIN_DT_PEDIDO) "
						+ "select distinct SZEDBT, SZDOCO, "
						+ "lpad('"+vMcu+"',12,' ') as MCMCU_ORGAO_SOLICITANTE, "
						+ "(SELECT SYSDATE FROM DUAL) as VIN_DT_PEDIDO "
						+ "from vw_F47012 where SZEDBT in (select distinct vin_co from vincula_pedido_erp where MCMCU_ORGAO_SOLICITANTE = lpad('"+vMcu+"',12,' ')) "
						+ "and SZDOCO not in (select distinct SDDOCO from vincula_pedido_erp where MCMCU_ORGAO_SOLICITANTE = lpad('"+vMcu+"',12,' ') and SDDOCO <> 999999999 )";
				
//				insert into vincula_pedido_erp (VIN_CO, SDDOCO, MCMCU_ORGAO_SOLICITANTE, VIN_DT_PEDIDO) 
//				select distinct SZEDBT, SZDOCO, lpad('00020806',12,' ') as MCMCU_ORGAO_SOLICITANTE, (SELECT SYSDATE FROM DUAL) as VIN_DT_PEDIDO 
//				from vw_F47012 
//				where SZEDBT in (select distinct vin_co from vincula_pedido_erp where MCMCU_ORGAO_SOLICITANTE =  lpad('00020806',12,' ')) 
//				and SZDOCO not in (select distinct SDDOCO from vincula_pedido_erp where MCMCU_ORGAO_SOLICITANTE = lpad('00020806',12,' ') and SDDOCO <> 999999999 )

				stm.execute(query);
			}

		} finally {
			if (stm != null) {
				stm.close();
			}
		}

	}

	public boolean checaDoco(String pDoco, String pVinCo) throws SQLException {

		boolean retorno = false;
		PreparedStatement stm = null;

		String query = "Select count(*) as retorno from vincula_pedido_erp where SDDOCO = "
				+ pDoco + " AND trim(VIN_CO) = '" + pVinCo + "' ";
		try {

			stm = conexaoOracle.prepareStatement(query);

			ResultSet rs = stm.executeQuery(query);

			rs.next();

			if (rs.getLong("retorno") > 0) {
				retorno = true;
			}
			rs.close();
			stm.close();

		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		return retorno;
	}
}
