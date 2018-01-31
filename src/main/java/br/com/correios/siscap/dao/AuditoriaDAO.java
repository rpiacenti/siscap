package br.com.correios.siscap.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.correios.ppjsiscap.util.UtilLog;
import br.com.correios.siscap.model.Auditoria;

public class AuditoriaDAO {

	private Connection conexao;

	public AuditoriaDAO(Connection c) {

		conexao = c;

	}

	public void insert(Auditoria c) throws ClassNotFoundException, SQLException {
		Auditoria audModel = c;

		String oraDt = "{ts '" + audModel.getAud_dh_execucao() + "'}";

		String vQuery = "INSERT INTO AUDITORIA (AUD_NU, AUD_TX_USUARIO, AUD_TX_FUNCIONALIDADE, AUD_NO_ENTIDADE, AUD_DH_EXECUCAO, AUD_TX_IDENTIDADE_REGISTRO, AUD_TX_REGISTRO) "
				+ " VALUES ((select count(*)+1 as ID_NU from auditoria),'" + audModel.getAud_tx_usuario() + "','"
				+ audModel.getAud_tx_funcionalidade() + "','" + audModel.getAud_no_entidade() + "'," + oraDt + ",'"
				+ audModel.getAud_tx_identidade_registro().trim() + "','"
				+ audModel.getAud_tx_registro().replace("'", "\"") + "')";

		UtilLog.logger.debug(vQuery);
		PreparedStatement stm = null;
		try {
			stm = conexao.prepareStatement(vQuery);
			stm.executeQuery();

		} finally {
			if (stm != null) {

				stm.close();

			}
		}

	}

}
