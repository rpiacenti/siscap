package br.com.correios.siscap.dbcp;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

import br.com.correios.siscap.anotacoes.OracleDb;
import br.com.correios.siscap.anotacoes.SqlDb;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class DbDatasources {
	
	
	@Produces @ApplicationScoped @SqlDb
	public DataSource getDataSourceSqlServer(){
		HikariConfig config = new HikariConfig("/hikari-sqlserver.properties");
		return new HikariDataSource(config);
	}
	
	@Produces @ApplicationScoped @OracleDb
	public DataSource getDataSourceOracle(){
		HikariConfig config = new HikariConfig("/hikari-oracle.properties");
		return new HikariDataSource(config);
	}

	
}

