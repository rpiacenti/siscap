package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import br.com.correios.ppjsiscap.enums.Categoria;
import br.com.correios.ppjsiscap.enums.NATUREZAORGAO;
import br.com.correios.ppjsiscap.enums.TIPOPEDIDO;
import br.com.correios.ppjsiscap.paginador.Paginador;
import br.com.correios.ppjsiscap.seguranca.Usuario;
import br.com.correios.ppjsiscap.util.DataERP;
import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.model.ItemDisponivel;
import br.com.correios.siscap.model.ItemPedido;

public class ItemDisponivelDAO {
	
	private Usuario usuario;

	private Connection conexao;

	public ItemDisponivelDAO(Connection c, Usuario pUsuario) {
		conexao = c;
		this.usuario = pUsuario;
	}

	public List<ItemDisponivel> findItemCD(String cd) throws SQLException {
		List<ItemDisponivel> items = new ArrayList<ItemDisponivel>();

		PreparedStatement stm = null;
		try {

			if (cd != null) {

				String query = "SELECT DISTINCT A.IMLITM, A.IMITM, A.IMDSC1,	A.IMDSC2,	A.IMLNTY,	A.IMSRP2,	A.IMSTKT,	A.IMGLPT,	A.IMUOM1,	A.IMUOM6, A.IMGLPT "
						+ "FROM vw_F4101 A "
						+ "where A.IMLITM in (select iblitm from VW_F4102 WHERE ibmcu = ?) and A.IMPRP4 <> 'UNI' ";
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
					item.setItp_tx_unidade_item(rs.getString("IMUOM6"));
					item.setItp_co_curto_item(rs.getString("IMITM"));
					if (rs.getString("IMGLPT").indexOf("I010") > -1) {
						item.setItp_tx_tipo_item("M");
					} else {
						item.setItp_tx_tipo_item("P");
					}
					items.add(item);
				}
				rs.close();
			}
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		return items;
	}

	
	
	public Paginador<ItemDisponivel> paginacaoItemTipoOrgaoCD(List<ItemDisponivel> Itens, Paginador<ItemDisponivel> p) throws SQLException{
		Long nl = 0L;
		int linhaContadas = 0;
		int linhaSelecionadas = 0;

		for (ItemDisponivel vitem : Itens) {
		
		    linhaSelecionadas++;
			ItemDisponivel item = new ItemDisponivel();
			if (linhaSelecionadas > p.getPrimeiroRegistro()	&& linhaContadas <= p.getQuantidadeDeRegistrosPorPagina()) {
				
				if (aplicaFiltro(p, vitem) ){
					linhaContadas++;
					nl++;
					item.setItp_co_cia(vitem.getItp_co_cia());
					item.setItp_co_item(vitem.getItp_co_item());
					item.setItp_tx_descricao_item(vitem.getItp_tx_descricao_item());
					item.setItp_tx_descricao_detalhe_item(vitem.getItp_tx_descricao_detalhe_item());
					item.setItp_tx_unidade_item(vitem.getItp_tx_unidade_item());
					item.setItp_co_curto_item(vitem.getItp_co_curto_item());
					item.setCouncs(vitem.getCouncs());
					item.setUmconv(vitem.getUmconv());
					item.setUmrum(vitem.getUmrum());
					item.setUmum(vitem.getUmum());
					item.setItp_tx_valor_unitario(vitem.getItp_tx_valor_unitario());
					item.setItp_tx_tipo_item(vitem.getItp_tx_tipo_item());

					p.getColecaoDeRegistros().add(item);
				}

			} else {
				item = null;
				continue;
			}

		}
		p.setTotalDeRegistros(nl);
		return p;
	}
	
	private boolean aplicaFiltro(Paginador<ItemDisponivel> p,
			ItemDisponivel vitem) {
		boolean retorno = true;
		
		if (p.getEntityBean().getItp_co_item() != null
				&& !p.getEntityBean().getItp_co_item().equals("")) {
			if(vitem.getItp_co_item().indexOf(p.getEntityBean().getItp_co_item().toUpperCase()) == -1){
				retorno = false;
			}
		}
		if (p.getEntityBean().getItp_tx_descricao_item() != null
				&& !p.getEntityBean().getItp_tx_descricao_item().equals("")) {
			
			if(vitem.getItp_tx_descricao_item().indexOf(p.getEntityBean().getItp_tx_descricao_item().toUpperCase()) == -1){
				retorno = false;
			}

		}
		if (p.getEntityBean().getItp_tx_tipo_item() != null
				&& !p.getEntityBean().getItp_tx_tipo_item().equals("")) {
			
			if(vitem.getItp_tx_tipo_item().indexOf(p.getEntityBean().getItp_tx_tipo_item().toUpperCase()) == -1){
				retorno = false;
			}

		}
		
		return retorno;
	}
	
	
	private List<String> listaItemOrgao(String tipoOrgao, String mcuCD) throws SQLException {
		List<String> lista = new ArrayList<String>();
		PreparedStatement stm = null;
		String query = "select IBITM_ITEM_PEDIDO as item from PARAMETRO_ITEM_CENTRO_DISTRIB "
				+"where IBMCU_CENTRO_DISTRIBUICAO = lpad('"+mcuCD.trim()+"',12,' ') "
				+"and DRKY_TIPO_ORGAO = '"+tipoOrgao+"' "
				+"and PIT_IN_PEDIDO in ('T','N')";
		try {
			stm = conexao.prepareStatement(query);
			
			ResultSet rs = stm.executeQuery();
			
			while(rs.next()){
				lista.add(rs.getString("item"));
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (stm != null) {
				stm.close();
			}
		}
		
		return lista;
	}

	public List<ItemDisponivel> findItemTipoOrgaoCD(
			Paginador<ItemDisponivel> p, String tipOrgao) throws SQLException {

		PreparedStatement stm = null;
		List<ItemDisponivel> listaRetorno = new ArrayList<ItemDisponivel>();

		try {

			adicionaTnoTipoPedido(p);

			String tipoPedido = p.getEntityBean().getItp_tx_tipo_pedido();

			StringBuilder sqlQuery = this.getQueryItemDisponivel(p
					.getEntityBean().getItp_co_cia(), tipOrgao, tipoPedido, true);

			stm = conexao.prepareStatement(sqlQuery.toString());

			ResultSet rs = stm.executeQuery();

			Long nl = 0L;
			int linhaContadas = 0;
			int linhaSelecionadas = 0;
			List<String> listaItemOrgao = new ArrayList<String>();

			while (rs.next()) {
				linhaSelecionadas++;
				ItemDisponivel item = new ItemDisponivel();
				linhaContadas++;
				if (rs.getString("IMLITM").indexOf('R') == -1) {
					nl++;
					item.setItp_co_cia(p.getEntityBean().getItp_co_cia());
					item.setItp_co_item(rs.getString("IMLITM"));
					item.setItp_tx_descricao_item(rs.getString("IMDSC1"));
					item.setItp_tx_descricao_detalhe_item(rs
							.getString("IMDSC2"));
					item.setItp_tx_unidade_item(rs.getString("IMUOM6"));
					item.setItp_co_curto_item(rs.getString("IMITM"));
					item.setCouncs(rs.getString("COUNCS"));
					item.setUmconv(rs.getString("UMCONV"));
					item.setUmrum(rs.getString("UMRUM"));
					item.setUmum(rs.getString("UMUM"));
					item.setUmcnv1(rs.getString("UMCNV1"));
					item.setUnidMedFormatada(null);

					double vCustUnit = Double.parseDouble(item.getCouncs());
					// double vFatConv = Double.parseDouble(item.getUmconv());
					double vFatConv = Double.parseDouble(item.getUmcnv1());
					double vUnitario = 0;

					vUnitario = vCustUnit * vFatConv;
					
					item.setItp_tx_valor_unitario(Double.toString(vUnitario));

					item.setItp_tx_tipo_item(rs.getString("EGT001"));
					
					item.setPit_pe_item_padrao(rs.getString("QT_IT_MAX_SOLILITACAO"));

					listaRetorno.add(item);

				} else {
					item = null;
					continue;
				}
			}
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		return listaRetorno;
	}

	private void adicionaFiltro(Paginador<ItemDisponivel> p,
			StringBuilder filtro) {
		if (p.getEntityBean().getItp_co_item() != null
				&& !p.getEntityBean().getItp_co_item().equals("")) {
			filtro.append(" and A.IMLITM like '"
					+ p.getEntityBean().getItp_co_item().toUpperCase() + "%' ");
		}
		if (p.getEntityBean().getItp_tx_descricao_item() != null
				&& !p.getEntityBean().getItp_tx_descricao_item().equals("")) {
			filtro.append(" and A.IMDSC1 like '%"
					+ p.getEntityBean().getItp_tx_descricao_item()
							.toUpperCase() + "%' ");
		}
		if (p.getEntityBean().getItp_tx_tipo_item() != null
				&& !p.getEntityBean().getItp_tx_tipo_item().equals("")) {
			String tipo = null;
			if (p.getEntityBean().getItp_tx_tipo_item()
					.equals(Categoria.M.getCodigo())) {
				tipo = "'I010'";
			}
			if (p.getEntityBean().getItp_tx_tipo_item()
					.equals(Categoria.P.getCodigo())) {
				tipo = "'I001'";
			}
			filtro.append(" and A.IMGLPT = " + tipo + " ");
		}
	}

	private void adicionaTnoTipoPedido(Paginador<ItemDisponivel> p) {
		if (p.getEntityBean().getItp_tx_tipo_pedido().indexOf("N") > -1) {
			p.getEntityBean().setItp_tx_tipo_pedido("'T','N'");
		} else if (p.getEntityBean().getItp_tx_tipo_pedido().indexOf("E") > -1) {
			p.getEntityBean().setItp_tx_tipo_pedido("'T','E'");
		}
	}

	public String findByCount(String keyCD) throws SQLException {
		String result = "0";
		PreparedStatement stm = null;
		try {
			if ((keyCD != null) && (keyCD != "")) {
				String query = "select count(A.IMITM) as NUMREC "
						+ "  from vw_f4101 A, VW_F4105 B, VW_F41002 C "
						+ "where 	A.IMITM in (select IBITM from vw_f4102 where IBMCU = '"
						+ "            ".substring(0, (12 - keyCD.length()))
						+ keyCD + "') " + "and A.IMPRP4 <> 'UNI' "
						+ "and B.COITM = A.IMITM " + "and B.COMCU = '"
						+ "            ".substring(0, (12 - keyCD.length()))
						+ keyCD + "' AND B.COLEDG = '02' "
						+ "AND C.UMITM = A.IMITM " + "AND C.UMUM = A.IMUOM6 "
						+ "AND B.COUNCS > 0 ";

				stm = conexao.prepareStatement(query);
				stm.setQueryTimeout(160);

				ResultSet rs = stm.executeQuery();

				if (rs.next()) {

					result = rs.getString("NUMREC");
				}
				rs.close();
			}

		} finally {
			if (stm != null) {
				stm.close();
			}
		}

		return result;
	}

	public Double findByMediaConsumo(String mcu, String item)
			throws SQLException {
		Double result = 0d;

		// Transforma data de seis meses atrás em data ERP
		DataERP de = new DataERP();

		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");

		String dataERP = de.getDataERP(ft.format(de.getDataDay(-180)));

		// fim transforma data
		PreparedStatement stm = null;
		try {
			if ((mcu != null) && (mcu != "") && (item != null) && (item != "")) {
				String query = "SELECT AVG(SDQTYT)/10000  as MED_CONSUMO "
				+ "FROM VW_F42119 A, VW_F4101 B "
				+ "where  A.SDAN8 = (select mcan8 from VW_F0006 where mcmcu = lpad('"+mcu.trim()+"',12,' ') ) "
				+ "and B.imlitm = '"+item.trim()+"' "
				+ "and B.IMITM = A.SDITM "
				+ "and A.SDDRQJ > "+dataERP+" "
				+ "and A.SDNXTR = 999 "
				+ "and A.SDLTTR = 620 ";
//				String query = "SELECT AVG(A.SDUORG)/10000 as MED_CONSUMO "
//						+ "FROM VW_F42119 A, VW_F4101 B "
//						+ "where A.SDAN8 = (select mcan8 from VW_F0006 where mcmcu = lpad(trim(?),12,' ') ) "
//						+ "and A.SDITM = B.imitm "
//						+ "and B.imlitm = ? "
//						+ "and A.SDDRQJ > ? " 
//						+ "and A.SDNXTR = '999' "
//						+ "and A.SDLTTR = '620' ";

				stm = conexao.prepareStatement(query);
//				stm.setString(1,mcu.trim());
//				stm.setString(2,item.trim());
//				stm.setString(3, dataERP);

				ResultSet rs = stm.executeQuery();

				if (rs.next()) {
					if (rs.getString("MED_CONSUMO") != null) {
						result = Double.parseDouble(rs.getString("MED_CONSUMO"));
					} else {
						result = 0d;
					}
				}
			}

		} finally {
			if (stm != null) {
				stm.close();
			}
		}

		return result;
	}

	public String[] getSaldoItens(String keyCD) throws SQLException {
		ArrayList<String> orgSaldo = new ArrayList<String>();
		StringBuffer bSaldo = new StringBuffer();

		PreparedStatement stm = null;
		PreparedStatement stm2 = null;
		try {
			String query = "select distinct A.IMITM AS ITM "
					+ "from vw_f4101 A, VW_F4105 B, VW_F41002 C, VW_F41021 D "
					+ "where trim(A.imlitm) in (select distinct trim(replace(IBITM_ITEM_PEDIDO,'.','')) from parametro_item_centro_distrib	where IBMCU_CENTRO_DISTRIBUICAO = '    00004010' and A.IMPRP4 <> 'UNI' AND PIT_IN_PEDIDO IN ('T','N','E')) "
					+ "and D.LIITM = A.IMITM " 
					+ "and D.limcu = lpad('"+keyCD.trim()+"',12,' ')'"
					+ "and B.COITM = A.IMITM "
					+ "and B.COMCU = lpad('"+keyCD.trim()+"',12,' ')'"
					+ "AND B.COLEDG = '02' "
					+ "AND C.UMITM = A.IMITM " + "AND C.UMUM = A.IMUOM6 "
					+ "order by A.IMITM ";

			stm = conexao.prepareStatement(query);

			stm.setQueryTimeout(160);

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {
				orgSaldo.add(rs.getString("ITM"));
			}
			//
			rs.close();

			for (int i = 0; i < orgSaldo.size(); i++) {

				query = "select distinct LIITM, (LIPQOH-LIPCOM-LIHCOM)/10000 as SALDO from vw_f41021 "
						+ "where limcu = '"
						+ "            ".substring(0, (12 - keyCD.length()))
						+ keyCD + "' " + "and liitm = " + orgSaldo.get(i);

				stm2 = conexao.prepareStatement(query);
				stm2.setQueryTimeout(160);

				ResultSet rs2 = stm2.executeQuery();
				if (rs2.next()) {
					if (rs2.getString("SALDO") != null) {
						bSaldo.append(orgSaldo.get(i) + "-"
								+ rs2.getString("SALDO") + ";");
						// System.out.println("Item: "+ orgSaldo.get(i) +
						// " - Saldo: " + rs.getString("SALDO"));
					}

				}
				rs2.close();

			}

		} finally {
			if (stm != null) {
				stm.close();
			}
			if (stm2 != null) {
				stm2.close();
			}
		}

		String[] aResu = bSaldo.toString().split(";");
		//UtilLog.logger.debug(aResu.toString());
		return aResu;

	}

	public List<ItemDisponivel> findItemEstoqueoCD(String keyCD, int first,
			long totalreg, String tipoOrgao) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<ItemDisponivel> tItem = new ArrayList<ItemDisponivel>();
		PreparedStatement stm = null;
		try {
			String[] itSaldo = this.getSaldoItens(keyCD);

			if ((keyCD != null) && (keyCD != "")) {

				StringBuilder sqlQuery = this.getQueryItemDisponivel(keyCD, tipoOrgao, "'T','N','E'",false);

				stm = conexao.prepareStatement(sqlQuery.toString());

				ResultSet rs = stm.executeQuery();

				while (rs.next()) {
					ItemDisponivel auxItm = new ItemDisponivel();
					// linha = linha + 1;
					/*
					 * try { Thread.sleep(50); } catch(InterruptedException ex)
					 * { Thread.currentThread().interrupt(); }
					 */
					for (int i = 0; i < itSaldo.length; i++) {
						if (itSaldo[i].indexOf(rs.getString("IMITM")) > -1) {

							String[] pQtd = itSaldo[i].split("-");

							auxItm.setItp_co_cia(keyCD);
							auxItm.setItp_co_item(rs.getString("IMLITM"));
							auxItm.setItp_tx_descricao_item(rs
									.getString("IMDSC1"));
							auxItm.setItp_tx_descricao_detalhe_item(rs
									.getString("IMDSC2"));
							auxItm.setItp_tx_unidade_item(rs
									.getString("IMUOM6"));
							auxItm.setItp_co_curto_item(rs.getString("IMITM"));
							auxItm.setItp_tx_quantidade(pQtd[1]);

							if (rs.getString("IMGLPT").indexOf("I010") > -1) {
								auxItm.setItp_tx_tipo_item("M");
							} else {
								auxItm.setItp_tx_tipo_item("P");
							}
							tItem.add(auxItm);
							break;
						}
					}

					auxItm = null;

				}
			}
		} finally {
			if (stm != null) {
				stm.close();
			}
		}

		return tItem;

	}

	public Paginador<ItemDisponivel> listaTodosItemPaginado(
			Paginador<ItemDisponivel> p) throws SQLException {
		PreparedStatement stm = null;
		PreparedStatement stmCount = null;

		String orgNat = "'02','01','10','23','03','14','04','05','06','07','08','13','17','19','20','22','29','30','09','25','26','16','12','27','28','31','32','33','34','35','36'";
		if (p.getEntityBean().getItp_tx_natureza_orgao() == null) {
			p.getEntityBean().setItp_tx_natureza_orgao(orgNat);
		}

		ArrayList<String> lItens = getItensOrgaoTipo(p.getEntityBean());

		try {
			UtilLog.logger.debug("Recuoperando Itens "
					+ p.getEntityBean().getItp_co_cia() + "  -  "
					+ p.getEntityBean().getItp_tx_tipo_pedido());

			StringBuilder filtro = new StringBuilder();

			adicionaFiltro(p, filtro);

			StringBuilder query = new StringBuilder(
					"select * from (select colunas.*, ROWNUM rnum from (select distinct A.IMLITM ,A.IMDSC1,A.IMDSC2,A.IMUOM6,A.IMUOM1,A.IMITM, A.IMGLPT, B.COUNCS ");
			query.append(" from vw_f4101 A ");
			query.append(" join VW_F4102 E on A.IMLITM = E.IBLITM and E.IBMCU = lpad(?,12,' ') ");
			query.append(" join VW_F4105 B on B.COITM = A.IMITM and B.COMCU = lpad(?,12,' ') and B.COLEDG = '02'");
			query.append(" where A.IMPRP4 <> 'UNI' 	");
			query.append(filtro);
			query.append(" order by A.IMLITM)  colunas where ROWNUM <= ?) where rnum > ?  ");

			StringBuilder queryCount = new StringBuilder(
					" select  count(distinct A.IMLITM ||  A.IMDSC1 || A.IMDSC2 || A.IMUOM6 || A.IMUOM1 || A.IMITM || A.IMGLPT || B.COUNCS ) as total ");
			queryCount
					.append(" from vw_f4101 A, VW_F4102 E, VW_F4105 B where A.IMLITM = E.IBLITM ");
			queryCount.append(" and E.IBMCU = lpad(?,12,' ') ");
			queryCount.append("and A.IMPRP4 <> 'UNI' AND B.COITM = A.IMITM  ");
			queryCount.append("and B.COMCU = lpad(?,12,' ') ");
			queryCount.append(" and B.COLEDG = '02' ");
			queryCount.append(filtro);
			queryCount.append(" order by A.IMLITM ");

			stmCount = conexao.prepareStatement(queryCount.toString());
			stmCount.setString(	1, p.getEntityBean().getItp_co_cia().trim());
			stmCount.setString(	2, p.getEntityBean().getItp_co_cia().trim());


			ResultSet rsCount = stmCount.executeQuery();

			if (rsCount.next()) {
				p.setTotalDeRegistros(rsCount.getLong("total"));
			}
			rsCount.close();

			// "select count(*) as total from GRUPO_ATENDIMENTO WHERE MCMCU_CENTRO_DISTRIBUICAO = ? order by ROWID";

			UtilLog.logger.debug("Query itens disponiveis 2: "
					+ query.toString());

			stm = conexao.prepareStatement(query.toString());

			stm.setString(1, p.getEntityBean().getItp_co_cia().trim());
			stm.setString(2, p.getEntityBean().getItp_co_cia().trim());
			stm.setInt(	3, p.getPrimeiroRegistro()
							+ p.getQuantidadeDeRegistrosPorPagina()); // linha
																		// final
			stm.setInt(4, p.getPrimeiroRegistro()); // linha inicial

			ResultSet rs = stm.executeQuery();

			while (rs.next()) {

				ItemDisponivel item = new ItemDisponivel();

				if (rs.getString("IMLITM").indexOf('R') == -1) {

					item.setItp_co_cia(p.getEntityBean().getItp_co_cia());
					item.setItp_co_item(rs.getString("IMLITM"));
					item.setItp_tx_descricao_item(rs.getString("IMDSC1"));
					item.setItp_tx_descricao_detalhe_item(rs
							.getString("IMDSC2"));
					item.setItp_tx_unidade_item(rs.getString("IMUOM6"));
					item.setItp_tx_unidade_conversao_item(rs.getString("IMUOM1"));
					item.setItp_co_curto_item(rs.getString("IMITM"));
					

					// item.setItp_tx_quantidade(rs.getString("QTD"));

					if (lItens.size() > 0) {

						for (int t = 0; t < lItens.size(); t++) {
							String vComp = lItens.get(t).trim();
							if (vComp.equals(rs.getString("IMLITM").trim())) {
								item.setMarcado(true);
								break;
							} else {
								item.setMarcado(false);
							}
						}

					} else {
						item.setMarcado(false);
					}

					if (rs.getString("IMGLPT").indexOf("I010") > -1) {
						item.setItp_tx_tipo_item(Categoria.M.getCodigo());
					} else {
						item.setItp_tx_tipo_item(Categoria.P.getCodigo());
					}

					p.getColecaoDeRegistros().add(item);

				} else {
					continue;
				}

			}

			rs.close();

		} finally {

			if (stm != null) {
				stm.close();
			}

			if (stmCount != null) {
				stmCount.close();
			}
		}

		return p;
	}

	public ArrayList<String> getItensOrgaoTipo(ItemDisponivel item)
			throws SQLException {
		List<String> lItens = new ArrayList<String>();

		Statement stm2 = null;

		String vCDs = "'00004010','00049748'";
		String vTPs = "'T'";
		
		if(item != null){
				vCDs = "'"+item.getItp_co_cia().trim()+"'";
				vTPs = "'"+item.getItp_tx_tipo_pedido()+"'";
		}
		
		try {
			String query = "select distinct IBITM_ITEM_PEDIDO from parametro_item_centro_distrib "
					+ "where trim(IBMCU_CENTRO_DISTRIBUICAO) in ("+ vCDs +") "
					+ "and PIT_IN_PEDIDO in ("+ vTPs + ") "
					+ "and DRKY_TIPO_ORGAO in ("
					+ item.getItp_tx_natureza_orgao()
					+ ") "
					+ "order by IBITM_ITEM_PEDIDO";

			stm2 = conexao.createStatement();
			ResultSet rs = stm2.executeQuery(query);
			Long vLong = (long) 0;
			while (rs.next()) {
				vLong = rs.getLong("IBITM_ITEM_PEDIDO");
				lItens.add(vLong.toString());
			}
		} finally {
			if (stm2 != null) {
				stm2.close();
			}
		}
		return (ArrayList<String>) lItens;
	}
	
	public ArrayList<String> getItensOrgaoTipo(String mcuCia, String tipoPedido, String naturezaOrgao)
			throws SQLException {
		List<String> lItens = new ArrayList<String>();

		Statement stm2 = null;

		String vCDs = "'00004010','00049748'";
		String vTPs = "'T'";
		
		if(mcuCia != null){
				vCDs = "'"+mcuCia.trim()+"'";
				vTPs = "'"+tipoPedido+"'";
		}
		
		try {
			String query = "select distinct IBITM_ITEM_PEDIDO, PIT_PE_ITEM_PADRAO AS QT_IT_MAX_SOLILITACAO from parametro_item_centro_distrib "
					+ "where trim(IBMCU_CENTRO_DISTRIBUICAO) in ("+ vCDs +") "
					+ "and PIT_IN_PEDIDO in ("+ vTPs + ") "
					+ "and DRKY_TIPO_ORGAO in ("
					+ naturezaOrgao
					+ ") "
					+ "order by IBITM_ITEM_PEDIDO";

			stm2 = conexao.createStatement();
			ResultSet rs = stm2.executeQuery(query);
			Long vLong = (long) 0;
			while (rs.next()) {
				vLong = rs.getLong("IBITM_ITEM_PEDIDO");
				lItens.add(vLong.toString()+" - "+rs.getString("QT_IT_MAX_SOLILITACAO"));
			}
		} finally {
			if (stm2 != null) {
				stm2.close();
			}
		}
		return (ArrayList<String>) lItens;
	}

	public List<ItemDisponivel> listaTodosItemComEstoquePaginado(
		Paginador<ItemDisponivel> p) throws SQLException {
		PreparedStatement stm = null;
		//PreparedStatement stmCount = null;
		
		List<ItemDisponivel> listaRetorno = new ArrayList<ItemDisponivel>();

		try {
			StringBuilder filtro = new StringBuilder();

			StringBuilder query = new StringBuilder(
					"select distinct A.IMLITM ,A.IMDSC1,A.IMDSC2,A.IMUOM6,A.IMUOM1,A.IMITM, A.IMGLPT,  D.UMCONV, D.UMRUM, D.UMUM "); 
			query.append(" from vw_f4101 A ");
			query.append(" join VW_F4102 E on A.IMITM = E.IBITM and E.IBMCU = ? ");
			query.append(" join  VW_F41021 C on C.LIITM = A.IMITM and C.limcu = ? ");
			query.append(" join VW_F41002 D ON D.UMUM = A.IMUOM6 and D.UMITM = A.IMITM ");
			query.append(" where A.IMPRP4 <> 'UNI' 	");
			query.append(" and D.UMCONV = (select max(UMCONV) from VW_F41002 where UMITM = A.IMITM and UMUM = A.IMUOM6) ");
			query.append(" and (C.LIPQOH-(C.LIPCOM + C.LIHCOM)) > 0 ");
			query.append(" and C.LIPQOH > 0 ");
			query.append(" order by A.IMLITM");


			String pMcu = "            ".substring(0, (12 - p.getEntityBean()
					.getItp_co_cia().length()))
					+ p.getEntityBean().getItp_co_cia();
			
			stm = conexao.prepareStatement(query.toString());

			stm.setString(1, pMcu);
			stm.setString(2, pMcu);
			

			ResultSet rs = stm.executeQuery();
			
			int linhaContadas = 0;
			int linhaSelecionadas = 0;

			while (rs.next()) {
				linhaSelecionadas ++;
				linhaContadas ++;
				ItemDisponivel item = new ItemDisponivel();

				if (rs.getString("IMLITM").indexOf('R') == -1) {

					item.setItp_co_cia(p.getEntityBean().getItp_co_cia());
					item.setItp_co_item(rs.getString("IMLITM"));
					item.setItp_tx_descricao_item(rs.getString("IMDSC1"));
					item.setItp_tx_descricao_detalhe_item(rs
							.getString("IMDSC2"));
					item.setItp_tx_unidade_item(rs.getString("IMUOM6"));
					item.setItp_tx_unidade_conversao_item(rs.getString("IMUOM1"));
					item.setItp_co_curto_item(rs.getString("IMITM"));
					// item.setCouncs(rs.getString("COUNCS"));
					item.setUmconv(rs.getString("UMCONV"));
					item.setUmum(rs.getString("UMUM"));
					item.setUmrum(rs.getString("UMRUM"));
					item.setUnidMedFormatada(null);

					if (rs.getString("IMGLPT").indexOf("I010") > -1) {
						item.setItp_tx_tipo_item(Categoria.M.getCodigo());
					} else {
						item.setItp_tx_tipo_item(Categoria.P.getCodigo());
					}

					listaRetorno.add(item);

				} else {
					continue;
				}

			}
			
			rs.close();

		} finally {

			if (stm != null) {
				stm.close();
			}
			
		}

		return listaRetorno;
	}
	
	public Paginador<ItemDisponivel> paginacaoTodosItemComEstoquePaginado(List<ItemDisponivel> Itens, Paginador<ItemDisponivel> p){
		Long nl = 0L;
		int linhaContadas = 0;
		int linhaSelecionadas = 0;

		for (ItemDisponivel vitem : Itens) {
			linhaSelecionadas++;
			ItemDisponivel item = new ItemDisponivel();
			if (linhaSelecionadas > p.getPrimeiroRegistro()
					&& linhaContadas <= p.getQuantidadeDeRegistrosPorPagina()) {
				if (aplicaFiltro(p, vitem)) {

					linhaContadas++;

					nl++;
					item.setItp_co_cia(vitem.getItp_co_cia());
					item.setItp_co_item(vitem.getItp_co_item());
					item.setItp_tx_descricao_item(vitem.getItp_tx_descricao_item());
					item.setItp_tx_descricao_detalhe_item(vitem.getItp_tx_descricao_detalhe_item());
					item.setItp_tx_unidade_item(vitem.getItp_tx_unidade_item());
					item.setItp_tx_unidade_conversao_item(vitem.getItp_tx_unidade_conversao_item());
					item.setItp_co_curto_item(vitem.getItp_co_curto_item());
					item.setCouncs(vitem.getCouncs());
					item.setUmconv(vitem.getUmconv());
					item.setUmrum(vitem.getUmrum());
					item.setUmum(vitem.getUmum());
					item.setItp_tx_valor_unitario(vitem.getItp_tx_valor_unitario());
					item.setItp_tx_tipo_item(vitem.getItp_tx_tipo_item());
					item.setPit_pe_item_padrao(vitem.getPit_pe_item_padrao());
					item.setUnidMedFormatada(null);

					p.getColecaoDeRegistros().add(item);
				}

			} else {
				item = null;
				continue;
			}

		}
		p.setTotalDeRegistros(p.getTotalDeRegistros());
		return p;
	}
	
	public List<ItemDisponivel> findItemTipoOrgaoCD(String mcuCia,
			String tipoPedido , String tipOrgao) throws SQLException {
		String codCia = null;
		PreparedStatement stm = null;
		List<ItemDisponivel> listaRetorno = new ArrayList<ItemDisponivel>();

		try {

			if (tipoPedido.indexOf("N") > -1) {
				tipoPedido = "'T','N'";
			} else if (tipoPedido.indexOf("E") > -1) {
				tipoPedido = "'T','E'";
			}
			
			if(mcuCia.trim().equals("00004010")){
				codCia = "00010";
			}else{
				codCia = "00072";
			}
	

			StringBuilder sqlQuery = this.getQueryItemDisponivel(mcuCia, tipOrgao, tipoPedido, true);

			stm = conexao.prepareStatement(sqlQuery.toString());

			ResultSet rs = stm.executeQuery();

			Long nl = 0L;
			int linhaContadas = 0;
			int linhaSelecionadas = 0;
			List<String> listaItemOrgao = new ArrayList<String>();

			while (rs.next()) {
				linhaSelecionadas++;
				ItemDisponivel item = new ItemDisponivel();
				linhaContadas++;
				if (rs.getString("IMLITM").indexOf('R') == -1) {
					nl++;
					item.setItp_co_cia(codCia);
					item.setItp_co_item(rs.getString("IMLITM"));
					item.setItp_tx_descricao_item(rs.getString("IMDSC1"));
					item.setItp_tx_descricao_detalhe_item(rs
							.getString("IMDSC2"));
					item.setItp_tx_unidade_item(rs.getString("IMUOM6"));
					item.setItp_tx_unidade_conversao_item(rs.getString("IMUOM1"));
					item.setItp_co_curto_item(rs.getString("IMITM"));
					item.setCouncs(rs.getString("COUNCS"));
					item.setUmconv(rs.getString("UMCONV"));
					item.setUmrum(rs.getString("UMRUM"));
					item.setUmum(rs.getString("UMUM"));
					item.setUmcnv1(rs.getString("UMCNV1"));
					item.setUnidMedFormatada(null);

					double vCustUnit = Double.parseDouble(item.getCouncs());
					// double vFatConv = Double.parseDouble(item.getUmconv());
					double vFatConv = Double.parseDouble(item.getUmcnv1());
					double vUnitario = 0;

					vUnitario = vCustUnit * vFatConv;
					
					item.setItp_tx_valor_unitario(Double.toString(vUnitario));

					item.setItp_tx_tipo_item(rs.getString("EGT001"));
					
					item.setPit_pe_item_padrao(rs.getString("QT_IT_MAX_SOLILITACAO"));

					listaRetorno.add(item);

				} else {
					item = null;
					continue;
				}
			}
		} finally {
			if (stm != null) {
				stm.close();
			}
		}
		return listaRetorno;
	}


	public List<ItemDisponivel> listaTodosItemComEstoque(String mcuCia,
			String tipoPedido, String tipoOrgao) throws SQLException {
		
		PreparedStatement stm = null;
		List<ItemDisponivel> listaRetorno = new ArrayList<ItemDisponivel>();

		try {

			StringBuilder sqlQuery = this.getQueryItemDisponivel(mcuCia, tipoOrgao, tipoPedido, false);
			stm = conexao.prepareStatement(sqlQuery.toString());

			ResultSet rs = stm.executeQuery();
			
			while (rs.next()) {

				ItemDisponivel item = new ItemDisponivel();

				if (rs.getString("IMLITM").indexOf('R') == -1) {
					item.setItp_co_cia(mcuCia);
					item.setItp_co_item(rs.getString("IMLITM"));
					item.setItp_tx_descricao_item(rs.getString("IMDSC1"));
					item.setItp_tx_descricao_detalhe_item(rs
							.getString("IMDSC2"));
					item.setItp_tx_unidade_item(rs.getString("IMUOM6"));
					item.setItp_tx_unidade_conversao_item(rs.getString("IMUOM1"));
					item.setItp_co_curto_item(rs.getString("IMITM"));
					item.setCouncs(rs.getString("COUNCS"));
					item.setUmconv(rs.getString("UMCONV"));
					item.setUmrum(rs.getString("UMRUM"));
					item.setUmum(rs.getString("UMUM"));
					item.setUmcnv1(rs.getString("UMCNV1"));
					item.setUnidMedFormatada(null);

					double vCustUnit = Double.parseDouble(item.getCouncs());
					// double vFatConv = Double.parseDouble(item.getUmconv());
					double vFatConv = Double.parseDouble(item.getUmcnv1());
					double vUnitario = 0;

					vUnitario = vCustUnit * vFatConv;
					
					item.setItp_tx_valor_unitario(Double.toString(vUnitario));

					item.setItp_tx_tipo_item(rs.getString("EGT001"));
					
					item.setPit_pe_item_padrao(rs.getString("QT_IT_MAX_SOLILITACAO"));

					listaRetorno.add(item);

				} else {
					item = null;
					continue;
				}
			}
			
			rs.close();

		} finally {

			if (stm != null) {
				stm.close();
			}
			
		}

		return listaRetorno;
		
	}

	public List<ItemDisponivel> listaTodosItem(String mcuCia) throws SQLException {
		
		PreparedStatement stm = null;
		List<ItemDisponivel> listaRetorno = new ArrayList<ItemDisponivel>();
		
		try {

			StringBuilder sqlQuery = this.getQueryItemDisponivel(mcuCia);
			stm = conexao.prepareStatement(sqlQuery.toString());

			ResultSet rs = stm.executeQuery();
			
			while (rs.next()) {
			
				ItemDisponivel item = new ItemDisponivel();
				if (rs.getString("IBLITM") != null) {
					if (rs.getString("IBLITM").indexOf('R') == -1) {
						item.setItp_co_cia(mcuCia);
						item.setItp_co_item(rs.getString("IBLITM"));
						item.setItp_tx_descricao_item(rs.getString("IMDSC1"));
						item.setItp_tx_descricao_detalhe_item(rs
								.getString("IMDSC2"));
						item.setItp_tx_unidade_item(rs.getString("IMUOM6"));
						item.setItp_tx_unidade_conversao_item(rs.getString("IMUOM1"));
						item.setItp_co_curto_item(rs.getString("IBITM"));
						item.setCouncs(rs.getString("COUNCS"));
						item.setUmconv(rs.getString("UMCONV"));
						item.setUmrum(rs.getString("UMRUM"));
						item.setUmum(rs.getString("UMUM"));
						item.setUmcnv1(rs.getString("UMCNV1"));
						item.setUnidMedFormatada(null);

						double vCustUnit = Double.parseDouble(item.getCouncs());
						// double vFatConv =
						// Double.parseDouble(item.getUmconv());
						double vFatConv = Double.parseDouble(item.getUmcnv1());
						double vUnitario = 0;

						vUnitario = vCustUnit * vFatConv;

						item.setItp_tx_valor_unitario(Double
								.toString(vUnitario));

						item.setItp_tx_tipo_item(rs.getString("EGT001"));
						
				//		item.setPit_pe_item_padrao(rs.getString("QT_IT_MAX_SOLILITACAO"));

						if (!(rs.getString("EGT001") == null)) {

							listaRetorno.add(item);

						}

					} else {
						item = null;
						continue;
					}
				}
			}
			
			rs.close();

		} finally {

			if (stm != null) {
				stm.close();
			}
			
		}

		return listaRetorno;
		
	}

	public StringBuilder getQueryItemDisponivel(String mcuCia, String tipOrgao, String tipoPedido, boolean scopo){
		DataERP dtERP = new DataERP();

		String codDR = "";

		if(usuario.getCodigoDR().substring(3,usuario.getCodigoDR().length()).indexOf("01") > -1){
			codDR = "10";
		}else{
			codDR = usuario.getCodigoDR().trim();
		}

		StringBuilder query = new StringBuilder(
				"SELECT IMLITM, IMITM, IMSRP2, IMPRP4, IMDSC1, IMDSC2, IMUOM1, ");
				query.append(" IMUOM6, IMGLPT, IMSTKT, COUNCS, UMCONV, UMCNV1, ");
				query.append(" UMUM, UMRUM, EGT001, JSEFTE, DRDL01, ");
				query.append(" QT_IT_DISP, QT_IT_MAX_SOLILITACAO ");
				query.append(" FROM ( ");
				query.append(" SELECT DISTINCT ");
				query.append("      B.IMLITM, ");
				query.append("      B.IMITM, ");
				query.append("      B.IMSRP2, ");
				query.append("      B.IMPRP4, ");
				query.append("      B.IMDSC1, ");
				query.append("      B.IMDSC2, ");
				query.append("      B.IMUOM1, ");
				query.append("      B.IMUOM6, ");
				query.append("      B.IMGLPT, ");
				query.append("      B.IMSTKT, ");
				query.append("      CASE WHEN C.COUNCS IS NULL THEN 0 ");
				query.append("           ELSE C.COUNCS/10000 ");
				query.append("      END AS COUNCS, ");
				query.append(" D.UMCONV, ");
				query.append("      CASE WHEN B.IMUOM6 = B.IMUOM1 THEN 1 ");
				query.append("           WHEN D.UMCNV1 IS NULL    THEN 1 ");
				query.append("      ELSE D.UMCNV1/10000000 END AS UMCNV1, ");
				query.append("      D.UMUM, ");
				query.append("      D.UMRUM, ");
				query.append("      ( ");
				query.append("      SELECT DISTINCT ");
				query.append("         E.EGT001 ");
				query.append("      FROM ");
				query.append("         VW_F5542E07 E ");
				query.append("      WHERE ");
				query.append("         E.EGSRP2 = B.IMSRP2 ");
				query.append("         AND E.EGPRP4 = B.IMPRP4 ");
				query.append("         AND ROWNUM = 1 ");
				query.append("         AND E.EGSTYL = A.DRKY_TIPO_ORGAO) AS EGT001, ");
				query.append("      ( ");
				query.append("      SELECT ");
				query.append("		CASE WHEN SUM (F.LIPQOH - (F.LIHCOM + F.LIPCOM))/10000 < 0 THEN 0 ");
				query.append("              ELSE SUM (F.LIPQOH - (F.LIHCOM + F.LIPCOM))/10000 ");
				query.append("         END AS QT_IT_DISP ");
				query.append("      FROM ");
				query.append("         VW_F41021 F ");
				query.append("      WHERE ");
				query.append("         F.LIITM = B.IMITM ");
				query.append("         AND F.LIMCU = A.IBMCU_CENTRO_DISTRIBUICAO ");
				query.append("         GROUP BY ");
				query.append("         F.LIITM, F.LIMCU) AS QT_IT_DISP, ");
				query.append("   G.JSEFTE, ");
				query.append(" 	 H.DRDL01, ");
				query.append(" 	 A.PIT_PE_ITEM_PADRAO AS QT_IT_MAX_SOLILITACAO");
				query.append("   FROM ");
				query.append("      parametro_item_centro_distrib A ");
				query.append("   INNER JOIN ");
				query.append("      VW_F4102 I ON (TO_CHAR(A.IBITM_ITEM_PEDIDO) = TRIM(I.IBLITM) AND A.IBMCU_CENTRO_DISTRIBUICAO = I.IBMCU) ");
				query.append("   LEFT OUTER JOIN ");
				query.append("      VW_F4101 B ON (TO_CHAR(A.IBITM_ITEM_PEDIDO) = TRIM(B.IMLITM)) ");
				query.append("   LEFT OUTER JOIN ");
				query.append("      VW_F4105 C ON (B.IMITM = C.COITM AND C.COMCU =  A.IBMCU_CENTRO_DISTRIBUICAO AND C.COLEDG = '02') ");
				query.append("   LEFT OUTER JOIN ");
				query.append("      VW_F41002 D ON (B.IMITM  = D.UMITM  AND B.IMUOM6 = D.UMUM  AND B.IMUOM1 = D.UMRUM) ");
				query.append(" 	 LEFT OUTER JOIN VW_F5541014 G ON (I.IBITM = G.JSITM) ");
				query.append("   LEFT OUTER JOIN VW_F0005_PRAZO_ENTREGA H ON (TRIM(H.DRKY) = '" + codDR.substring(codDR.length()-2) + "') "); 
				query.append("   WHERE ");
				query.append("      A.IBMCU_CENTRO_DISTRIBUICAO =  lpad(trim('"+ mcuCia +"'),12, ' ') ");
				query.append("      AND DRKY_TIPO_ORGAO in ("+ tipOrgao +") ");
				query.append("      AND PIT_IN_PEDIDO in ("+ tipoPedido +") ");
				query.append("      AND B.IMPRP4 != 'UNI' ");
				query.append(" 	   AND (G.JSEFTE > (" + dtERP.getDataERPToday() + " + H.DRDL01) OR G.JSEFTE IS  NULL )");
				query.append("   ORDER BY ");
				query.append("      B.IMLITM ");
				query.append("   ) ");
		if(scopo){
			query.append("WHERE  QT_IT_DISP >= 0 ");
		}else{
			query.append("WHERE  QT_IT_DISP > 0 ");
		}
		return query;
	}
	
	public StringBuilder getQueryItemDisponivel(String mcuCia){
		StringBuilder query = new StringBuilder("SELECT DISTINCT A.IBLITM, A.IBITM, A.IBSRP2, A.IBPRP4, B.IMDSC1, B.IMDSC2, B.IMUOM1, B.IMUOM6, ");
		query.append("CASE WHEN C.COUNCS IS NULL THEN 0 ");
		query.append("ELSE C.COUNCS/10000 ");
		query.append("END AS COUNCS, ");
		query.append("D.UMCONV, ");
		query.append("CASE WHEN B.IMUOM6 = B.IMUOM1 THEN 1 ");
		query.append("WHEN D.UMCNV1 IS NULL    THEN 1 ");
		query.append("ELSE D.UMCNV1/10000000 ");
		query.append("END AS UMCNV1, ");
		query.append("D.UMUM, ");
		query.append("D.UMRUM, ");
		query.append("(SELECT DISTINCT E.EGT001 ");
		query.append("FROM VW_F5542E07 E ");
		query.append("WHERE E.EGSRP2 = A.IBSRP2  ");
		query.append("AND E.EGPRP4 = A.IBPRP4  ");
		query.append("AND ROWNUM = 1) AS EGT001,  ");
		query.append("(SELECT CASE WHEN SUM (F.LIPQOH - (F.LIHCOM + F.LIPCOM))/10000 < 0 THEN 0 ");
		query.append("ELSE SUM (F.LIPQOH - (F.LIHCOM + F.LIPCOM))/10000 ");
		query.append("END AS QT_IT_DISP ");
		query.append("FROM VW_F41021 F ");
		query.append("WHERE  F.LIITM = A.IBITM ");
		query.append("AND F.LIMCU = A.IBMCU ");
		query.append("GROUP BY F.LIITM, F.LIMCU) AS QT_IT_DISP ");
		query.append("FROM VW_F4102 A ");
		query.append("INNER JOIN VW_F4101 B ");
		query.append("ON A.IBITM = B.IMITM ");
		query.append("LEFT OUTER JOIN VW_F4105 C ");
		query.append("ON (C.COITM =  A.IBITM ");
		query.append("AND C.COMCU =  A.IBMCU ");
		query.append("AND C.COLEDG = '02') ");
		query.append("LEFT OUTER JOIN VW_F41002 D ");
		query.append("ON (B.IMITM  = D.UMITM  ");
		query.append("AND B.IMUOM6 = D.UMUM  ");
		query.append("AND B.IMUOM1 = D.UMRUM) ");
		query.append("WHERE A.IBMCU =  lpad(trim('"+ mcuCia +"'),12, ' ') ");
		query.append("AND A.IBPRP4 != ('UNI') ");
		query.append("ORDER BY A.IBLITM ");
		return query;
	}
}
