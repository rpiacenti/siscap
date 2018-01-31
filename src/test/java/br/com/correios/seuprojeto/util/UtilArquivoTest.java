package br.com.correios.seuprojeto.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import br.com.correios.ppjsiscap.util.UtilArquivo;

public class UtilArquivoTest {

	UtilArquivo utilArquivo;

	@Before
	public void setUp() {
		this.utilArquivo = new UtilArquivo();
	}

	@Test
	public void deveRecuperarOTextoDoArquivo() {
		BufferedReader buf = null;
		String textoDoArquivo = null;

		InputStream inputStream = UtilArquivoTest.class
				.getResourceAsStream("FacesContextMocked.class");
		buf = new BufferedReader(new InputStreamReader(inputStream));
		textoDoArquivo = this.utilArquivo.getTextoDoArquivo(buf);

		MatcherAssert.assertThat(textoDoArquivo, Matchers.notNullValue());
	}



}
