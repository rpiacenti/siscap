package br.com.correios.siscap.converter;

import java.text.DecimalFormat;
import java.text.ParseException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="valorMonetario" )
public class ValorMonetario implements Converter{
	

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		Object resultado = null;
		if(arg2 != null && !"".equals(arg2)){
			try {
				resultado = getFormater().parse(arg2).doubleValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}			
		}
		return resultado;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		String resultado = null;
		if(String.class.isAssignableFrom(arg2.getClass())){
			arg2 = Double.valueOf((String) arg2);
		}
		resultado = getFormater().format(arg2);
		return resultado;
	}
	
	private DecimalFormat getFormater(){
		DecimalFormat formater = new DecimalFormat();
		formater.setMaximumFractionDigits(4);
		return formater;
	}

}
