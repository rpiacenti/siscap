package br.com.correios.ppjsiscap.util.validadores;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilValidadorDeEmail {

	public boolean validaEmail(String email) {
	    Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$"); 
	    Matcher m = p.matcher(email); 
	    return m.find();  
	 }

}
