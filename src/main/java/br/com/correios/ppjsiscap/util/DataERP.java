package br.com.correios.ppjsiscap.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataERP {

	String erpData;
	String centureErpData;
	String anoErpData;
	String diaErpData;
	String anoData = "";
	String mesData = "";
	String diaData = "";

	public DataERP() {
		super();
	}

	public DataERP(String erpData) {
		super();
		this.erpData = erpData;
	}

	public Date getDataDay(int dias) {

		Calendar calendarData = Calendar.getInstance();

		calendarData.add(Calendar.DATE, dias);

		Date novaData = new Date(calendarData.getTimeInMillis());

		return novaData;

	}

	public String getDataGregoriana() {

		String dataGreg = null;

	//	DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		centureErpData = erpData.substring(0, 1);
		anoErpData = erpData.substring(1, 3);
		diaErpData = erpData.substring(erpData.length() - 3, erpData.length());

		if (this.centureErpData.equals("1")) {
			this.anoData = anoData.concat("20" + this.anoErpData);
		} else {
			this.anoData = anoData.concat("19" + this.anoErpData);
		}

		Calendar calendarData = Calendar.getInstance();

		calendarData.set(Integer.parseInt(this.anoData), 0, 1);

		// System.out.println(df.format(calendarData.getTime()));
		calendarData.add(Calendar.DAY_OF_YEAR,
				Integer.parseInt(this.diaErpData) - 1);

		dataGreg = df.format(calendarData.getTime());

		return dataGreg;
	}
	
	public String getDataOracle() {

		String dataGreg = null;

	//	DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		centureErpData = erpData.substring(0, 1);
		anoErpData = erpData.substring(1, 3);
		diaErpData = erpData.substring(erpData.length() - 3, erpData.length());

		if (this.centureErpData.equals("1")) {
			this.anoData = anoData.concat("20" + this.anoErpData);
		} else {
			this.anoData = anoData.concat("19" + this.anoErpData);
		}

		Calendar calendarData = Calendar.getInstance();

	//	calendarData.set(Integer.parseInt(this.anoData), 0, 1);

		// System.out.println(df.format(calendarData.getTime()));
		calendarData.add(Calendar.DAY_OF_YEAR,
				Integer.parseInt(this.diaErpData) - 1);

		dataGreg = df.format(calendarData.getTime());

		return dataGreg;
	}

	public String getDataERP(String pData) {

		String dataERP = null;
		String[] aData = pData.split("/");
		Calendar calendarData = Calendar.getInstance();

		calendarData.set(Integer.parseInt(aData[2]),
				Integer.parseInt(aData[1]) - 1, Integer.parseInt(aData[0]));


		centureErpData = "1";
		int anoErp = calendarData.get(Calendar.YEAR);
		int diaErp = calendarData.get(Calendar.DAY_OF_YEAR);
		if (diaErp < 100) {
			diaErpData = "0" + Integer.toString(diaErp);
			if(diaErp < 10){
				diaErpData = "00" + Integer.toString(diaErp);
			}
		} else {
			diaErpData = Integer.toString(diaErp);
		}

		dataERP = centureErpData + Integer.toString(anoErp).substring(2, 4)	+ diaErpData;

		return dataERP;
	}

	public String getDataERPToday() {

		String dataERP = null;

		Calendar calendarData = Calendar.getInstance();

		calendarData.setTime(new Date());

		// DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);

		centureErpData = "1";
		int anoErp = calendarData.get(Calendar.YEAR);
		int diaErp = calendarData.get(Calendar.DAY_OF_YEAR);
		if (diaErp < 100) {
			diaErpData = "0" + Integer.toString(diaErp);
			if(diaErp < 10){
				diaErpData = "00" + Integer.toString(diaErp);
			}
		} else {
			diaErpData = Integer.toString(diaErp);
		}

		dataERP = centureErpData + Integer.toString(anoErp).substring(2, 4)
				+ diaErpData;
		//this.erpData = dataERP;
		// System.out.println(dataERP);
		// dataERP = dataERP.substring(0, 3)+ "." + dataERP.substring(3, 6);

		return dataERP;
	}
	

    
    public String getDataERPYearMinus() {

		String dataERP = null;

		Calendar calendarData = Calendar.getInstance();
		
		calendarData.setTime(new Date());
		
		calendarData.add(Calendar.YEAR, -1);

		// DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);

		centureErpData = "1";
		int anoErp = calendarData.get(Calendar.YEAR);
		int diaErp = calendarData.get(Calendar.DAY_OF_YEAR);
		if (diaErp < 100) {
			diaErpData = "0" + Integer.toString(diaErp);
			if(diaErp < 10){
				diaErpData = "00" + Integer.toString(diaErp);
			}
		} else {
			diaErpData = Integer.toString(diaErp);
		}

		dataERP = centureErpData + Integer.toString(anoErp).substring(2, 4)
				+ diaErpData;
		//this.erpData = dataERP;
		// System.out.println(dataERP);
		// dataERP = dataERP.substring(0, 3)+ "." + dataERP.substring(3, 6);

		return dataERP;
	}
	
	public void setERPData(String pERPDt){
		this.erpData = pERPDt;
	}
	
	public int getDataERPAno() {
		anoErpData = erpData.substring(1, 2);
		if(Integer.parseInt(anoErpData) < 100){
			return Integer.parseInt("20"+anoErpData);
		}else{
			return Integer.parseInt(anoErpData);
		}
	}
	
	public int getDataERPMes() {
		String sDtErp = this.getDataGregoriana();
		String sMes = sDtErp.substring(3,5);
		return Integer.parseInt(sMes);
	}

}
