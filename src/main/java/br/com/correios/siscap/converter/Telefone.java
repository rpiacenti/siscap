package br.com.correios.siscap.converter;

import java.text.ParseException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.swing.text.MaskFormatter;


@FacesConverter(value="telefoneConverter" )
public class Telefone implements Converter{

	static final String MASCARA_TELEFONE = "####-####";

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return arg2 != null && !"".equals(arg2) ? Integer.valueOf(arg2.replaceAll("[()-]", "")) : null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		String resultado = null;
		try {
			MaskFormatter maskFormatter = new MaskFormatter(MASCARA_TELEFONE);
			maskFormatter.setValueContainsLiteralCharacters(false);
			resultado =  maskFormatter.valueToString(arg2);
		} catch (ParseException e) {			
			throw new RuntimeException(e);
		}
		return resultado;
	}

}
