package br.com.correios.siscap.model;

import java.io.Serializable;
import java.text.ParseException;

@SuppressWarnings("serial")
public class Cronograma implements Serializable {

	private String cro_dt_ini_solicitacao;

	private String cro_dt_fim_solicitacao;

	private String cro_dt_processamento;

	private String gat_co_cia;

	private Integer gat_nu;

	private String cro_an;

	private String cro_me;

	private final static String[] meses;
	static {
		meses = new String[12];
		meses[0] = "Janeiro";
		meses[1] = "Fevereiro";
		meses[2] = "Março";
		meses[3] = "Abril";
		meses[4] = "Maio";
		meses[5] = "Junho";
		meses[6] = "Julho";
		meses[7] = "Agosto";
		meses[8] = "Setembro";
		meses[9] = "Outubro";
		meses[10] = "Novembro";
		meses[11] = "Dezembro";
	}

	public Cronograma() {

	}

	public Cronograma(String cro_dt_inciao_solicitacao, String gat_co_cia,
			int gat_nu_grupo_atend) throws ParseException {
		super();
		this.cro_dt_ini_solicitacao = cro_dt_inciao_solicitacao;
		this.gat_co_cia = gat_co_cia;
		this.gat_nu = gat_nu_grupo_atend;
	}

	/**
	 * @return the cro_ano
	 */
	public String getCro_an() {
		return cro_an;
	}

	/**
	 * @param cro_an
	 *            the cro_an to set
	 */
	public void setCro_an(String cro_an) {
		this.cro_an = cro_an;
	}

	/**
	 * @return the cro_me
	 */
	public String getCro_me() {
		return cro_me;
	}

	/**
	 * @param cro_me
	 *            the cro_me to set
	 */
	public void setCro_me(String cro_me) {
		this.cro_me = cro_me;
	}

	/**
	 * @return the cro_dt_ini_solicitacao
	 * @throws ParseException
	 */
	public String getCro_dt_ini_solicitacao() {

		return getFormatData(cro_dt_ini_solicitacao);

	}

	/**
	 * @param cro_dt_ini_solicitacao
	 *            the cro_dt_ini_solicitacao to set
	 */
	public void setCro_dt_ini_solicitacao(String cro_dt_ini_solicitacao) {

		this.cro_dt_ini_solicitacao = cro_dt_ini_solicitacao;
	}

	/**
	 * @return the cro_dt_fim_solicitacao
	 * @throws ParseException
	 */
	public String getCro_dt_fim_solicitacao() {
		return getFormatData(cro_dt_fim_solicitacao);

	}

	/**
	 * @param cro_dt_fim_solicitacao
	 *            the cro_dt_fim_solicitacao to set
	 */
	public void setCro_dt_fim_solicitacao(String cro_dt_fim_solicitacao) {

		this.cro_dt_fim_solicitacao = cro_dt_fim_solicitacao;
	}

	/**
	 * @return the cro_dt_processamento
	 * @throws ParseException
	 */
	public String getCro_dt_processamento() {

		return getFormatData(cro_dt_processamento);
	}

	/**
	 * @param cro_dt_processamento
	 *            the cro_dt_processamento to set
	 */
	public void setCro_dt_processamento(String cro_dt_processamento) {

		this.cro_dt_processamento = cro_dt_processamento;
	}

	/**
	 * @return the gat_co_cia
	 */
	public String getGat_co_cia() {
		return gat_co_cia;
	}

	/**
	 * @param gat_co_cia
	 *            the gat_co_cia to set
	 */
	public void setGat_co_cia(String gat_co_cia) {
		this.gat_co_cia = gat_co_cia;
	}

	/**
	 * @return the gat_nu
	 */
	public int getGat_nu() {
		return gat_nu;
	}

	/**
	 * @param integer
	 *            the gat_nu to set
	 */
	public void setGat_nu(Integer integer) {
		this.gat_nu = integer;
	}

	public String getFormatData(String pData) {
		if (pData != null) {
			if (pData.indexOf('-') > -1) {
				String vData = pData.substring(0, 10);
				String[] aData = vData.split("-");
				return aData[2] + "/" + aData[1] + "/" + aData[0];
			}
		} else {
			return null;
		}
		return pData;

	}

	public String CompletaData(String data) {
		if (data != null) {
			if (data.indexOf('/') < 0) {
				String dia = data.substring(0, 2);
				String mes = data.substring(2, 4);
				// String ano = data.substring(5, 9);
				String ndata = dia + "/" + mes + "/" + this.cro_an;
				return ndata;
			} else {
				if (data.length() < 10) {
					String[] adata = data.split("/");
					String dia = adata[0];
					String mes = adata[1];
					// String ano = data.substring(5, 9);
					String ndata = dia + "/" + mes + "/" + this.cro_an;
					return ndata;
				} else {
					return data;
				}

			}
		} else {
			return null;
		}

	}

	public String setOracleData(String pData) {

		if (pData != null && pData.indexOf('/') > -1) {
			String vData = pData.substring(0, 10);
			String[] aData = vData.split("/");
			return aData[2] + "-" + aData[1] + "-" + aData[0];

		}

		if (pData != null && pData.indexOf('-') > -1) {
			return pData;
		}
		return null;
	}

	public int getNumMes(String pMes) {

		for (int i = 0; i < 12; i++) {
			if (meses[i].equals(pMes)) {
				return i + 1;
			}
		}
		return 0;
	}

}
