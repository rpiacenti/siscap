package br.com.correios.siscap.managedbeans;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.sql.DataSource;

import br.com.correios.ppjsiscap.util.DataERP;
import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.dao.DesbloqueiaPedidoDAO;
import br.com.correios.siscap.dao.RastreamentoDAO;

@Stateless
public class AgendadorMB extends MB implements Serializable {

//	@Resource
//	TimerService service;

	@Inject
	@OracleDb
	private DataSource dataSource;

	@Inject
	DataERP dataERP;

	private static final long serialVersionUID = 1L;

	//@Schedule(hour = "7", minute = "46")
	void agendado() {
		Connection connection = null;

		Date data = new Date();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		String hoje = formatador.format(data);

		System.out.println("Agendador Executado: " + hoje);

		try {
			connection = dataSource.getConnection();
			DesbloqueiaPedidoDAO dao = new DesbloqueiaPedidoDAO(connection);
			dao.liberaPedidosBloqueados();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}

	//@Schedule(hour = "8", minute = "10")
	void rastreamento() {
		Connection connection = null;

		Date data = new Date();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
		String hoje = formatador.format(data);

		System.out.println("Agendador Executado: " + hoje);

		try {
			connection = dataSource.getConnection();
			RastreamentoDAO dao = new RastreamentoDAO(connection);
			dao.RatrearII();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	
	}
	

}
