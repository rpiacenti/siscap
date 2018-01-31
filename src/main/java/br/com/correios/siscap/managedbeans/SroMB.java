package br.com.correios.siscap.managedbeans;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;



public class SroMB extends MB {

	static String Sql;

	static Connection con = null;

	static Connection DBcon = null;

	static Statement Tbl = null;

	static ResultSet rs = null;

	static Set<Object> Layout = new HashSet<Object>();

	static Boolean Sair;

	static Boolean ProdutoSazonal;

	// static bancoWebPedidos DB = new bancoWebPedidos();

	static StringBuffer bfMCU = new StringBuffer();

	static StringBuffer bfMCUDR = new StringBuffer();

	static String[] xml;

	static URL url;

	static List linhas;

	String sronumero;
	String srodata;
	String srohora;
	String srodescricao;

	public SroMB() {
		// TODO Auto-generated constructor stub
	}

	public void getXML(String sro) {
		// StringBuffer registros = new StringBuffer();
		int pendentes = 0;

		try {
			// Gravando no arquivo Ocorrencias.
			Date arqData = getDataAtual();
			String sData = arqData.toLocaleString();
			String[] aData = sData.split(" ");
			sData = aData[0].replace("/", "_");
			String sHora = aData[1].replace(":", "_");

			// url = new URL(
			// "http://10.8.38.91:8084/sroxml/xml?Tipo=L&Resultado=U&usuario=SIBRAX&senha=CW15GG&Evento=&Objetos=SU041555265BR");
			//
			url = new URL(
					"http://webservicesro/sroxml/xml?Tipo=L&Resultado=U&usuario=SIBRAX2013&senha=?4G@YJD6GN&Objetos="
							+ sro);

			InputStream in = url.openStream();
			// Aqui você informa o nome do arquivo XML.

			// Criamos uma classe SAXBuilder que vai processar o XML4
			SAXBuilder sb = new SAXBuilder();

			// Este documento agora possui toda a estrutura do arquivo.
			Document d = sb.build(in);

			// Recuperamos o elemento root
			Element mural = d.getRootElement();
			// Recuperamos os elementos filhos (children)
			List elements1 = mural.getChildren();
			Iterator ite = elements1.iterator();

			while (ite.hasNext()) {
				Element nivel1 = (Element) ite.next();

				if (nivel1.getName().equals("objeto")) {

					List elements2 = nivel1.getChildren();
					Iterator ite2 = elements2.iterator();
					while (ite2.hasNext()) {
						Element element = (Element) ite2.next();

						this.setSronumero(nivel1.getChildText("numero"));
						this.setSrodata(element.getChildText("data"));
						this.setSrohora(element.getChildText("hora"));
						this.setSrodescricao(element.getChildText("descricao"));
					}
				}

			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("URL mal formatado." + url.getPath());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Erro de arquivo IO !.");
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	static void listaDR() {
		try {
			String[] aMCUDR = bfMCU.toString().split(";");
			StringBuffer lMCU = new StringBuffer();
			for (int i = 0; i < aMCUDR.length; i++) {
				lMCU.append("'" + aMCUDR[i] + "',");
			}

			bfMCUDR.delete(0, bfMCUDR.length());

			String Sql = "select Distinct MCMCU,MCRP01 from vw_f0006 "
					+ "where MCMCU in (" + lMCU + ") " + "order by MCRP01";

			Statement stm = con.createStatement();

			ResultSet rs = stm.executeQuery(Sql);

			while (rs.next()) {
				bfMCUDR.append(rs.getString("MCMCU") + ";"
						+ rs.getString("MCRP01"));
			}
			rs.close();
			stm.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static int getDataJuliana(String pData) {
		String[] aData = pData.split("/");
		GregorianCalendar gc = new GregorianCalendar(
				Integer.parseInt(aData[2]), Integer.parseInt(aData[1]),
				Integer.parseInt(aData[0]));
		int dataJuliana = gc.get(Calendar.DAY_OF_YEAR);
		return dataJuliana;
	}

	public static Date getDataAtual() {
		Calendar calendar = new GregorianCalendar();
		Date date = new Date();

		calendar.setTime(date);
		return calendar.getTime();
	}

	public static String getJulianERP(String pData) {
		Calendar hoje = Calendar.getInstance();

		String[] aDataReg = pData.split("-");

		hoje.set(Integer.parseInt(aDataReg[0]), Integer.parseInt(aDataReg[1]),
				Integer.parseInt(aDataReg[2]));

		NumberFormat formatter = new DecimalFormat("000");

		String sNumDias = formatter.format(hoje.get(Calendar.DAY_OF_YEAR));

		String sDataJuliana = "1"
				+ Integer.toString(hoje.get(Calendar.YEAR)).substring(2, 4)
				+ sNumDias;

		return sDataJuliana;
	}

	/**
	 * @return the sronumero
	 */
	public String getSronumero() {
		return sronumero;
	}

	/**
	 * @param sronumero
	 *            the sronumero to set
	 */
	public void setSronumero(String sronumero) {
		this.sronumero = sronumero;
	}

	/**
	 * @return the srodata
	 */
	public String getSrodata() {
		return srodata;
	}

	/**
	 * @param srodata
	 *            the srodata to set
	 */
	public void setSrodata(String srodata) {
		this.srodata = srodata;
	}

	/**
	 * @return the srohora
	 */
	public String getSrohora() {
		return srohora;
	}

	/**
	 * @param srohora
	 *            the srohora to set
	 */
	public void setSrohora(String srohora) {
		this.srohora = srohora;
	}

	/**
	 * @return the srodescrição
	 */
	public String getSrodescricao() {
		return srodescricao;
	}

	/**
	 * @param srodescrição
	 *            the srodescrição to set
	 */
	public void setSrodescricao(String srodescricao) {
		this.srodescricao = srodescricao;
	}
	
	public void sroSSP(){
		/*SELECT A.PSU_NU_PAUTA_SUPRIMENTO                                              AS PAUTA_SUPRIMENTO,

	       A.PSU_IN_FORNECIMENTO                                                  AS TIPO_FORNECIMENTO,

	       A.MCMCU                                                                AS MCMCU,

	       C.ETI_NU_REGISTRO                                                      AS OBJETO,

	       CONVERT( CHAR(10), C.ETI_DT_EXPEDICAO, 103 )                           AS DATA_EXPEDICAO,

	       LEFT( C.ETI_HR_EXPEDICAO, 2 ) + ':' + RIGHT( C.ETI_HR_EXPEDICAO, 2 )   AS HORA_EXPEDICAO,

	       C.ETI_IN_TIPO_EVENTO_SRO                                               AS TIPO_EVENTO_SRO,

	       C.ETI_IN_SITUACAO_EVENTO_SRO                                           AS SITUACAO_EVENTO_SRO,

	       CONVERT( CHAR(10), C.ETI_DT_EVENTO_SRO, 103 )                          AS DATA_EVENTO_SRO,

	       LEFT( C.ETI_HR_EVENTO_SRO, 2 ) + ':' + RIGHT( C.ETI_HR_EVENTO_SRO, 2 ) AS HORA_EVENTO_SRO,

	       C.ETI_DS_EVENTO_SRO                                                    AS DESCRICAO_EVENTO_SRO

	FROM CAM_PSU A,

	     CAM_PEX B,

	     CAM_ETI C

	WHERE B.PSU_NU_PAUTA_SUPRIMENTO = A.PSU_NU_PAUTA_SUPRIMENTO AND

	      B.PSU_IN_FORNECIMENTO     = A.PSU_IN_FORNECIMENTO     AND

	      B.MCMCU                   = A.MCMCU                   AND

	      C.ETI_NU_REGISTRO         = B.ETI_NU_REGISTRO         AND

	      C.ETI_DT_EXPEDICAO        = B.ETI_DT_EXPEDICAO        AND

	      C.ETI_DT_CANCELAMENTO     IS NULL
	      */
	}

}
