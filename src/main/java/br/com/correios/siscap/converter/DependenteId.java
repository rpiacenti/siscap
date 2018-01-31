package br.com.correios.siscap.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="dependenteId" )
public class DependenteId implements Converter{

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {		
		return arg2 != null && !"".equals(arg2) ? Integer.valueOf(arg2) : null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {		
		return arg2 != null && ((Integer)arg2) != 0 ? arg2.toString(): "";
	}

}
