package br.com.correios.siscap.dao;

import java.sql.SQLException;


public interface SiscapDAO<T> {


	void insert(T c) throws SQLException;

	void update(T c) throws SQLException;

	void delete(T c) throws SQLException;

	T findByID(Integer id);

	T findByKeyNumber(String keyNumber);

	T findByKeyNumber(String keyCD, String keyGrp);
	
	T findByKeyNumber(String keyCD, String keyGrp, String keyAno);

	void onEdit(T c) throws ClassNotFoundException, SQLException;
	
}
