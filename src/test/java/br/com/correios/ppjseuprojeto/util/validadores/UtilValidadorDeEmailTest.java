package br.com.correios.ppjseuprojeto.util.validadores;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import br.com.correios.ppjsiscap.util.validadores.UtilValidadorDeEmail;

public class UtilValidadorDeEmailTest {

	
	private UtilValidadorDeEmail validador;
	
	@Before
	public void setUp(){
		this.validador = new UtilValidadorDeEmail();
	}
	
	@Test
	public void deveValidarUmEmailQueEstahDentroDosPadroes() {
		MatcherAssert.assertThat(
				this.validador.validaEmail("fulano@correios.com.br"),
				Matchers.equalTo(true));
	}
	
	@Test
	public void deveInvalidarUmEmailQueEstahForaDosPadroes() {
		MatcherAssert.assertThat(
				this.validador.validaEmail("fulanocorreios.com.br"),
				Matchers.equalTo(false));
	}
	
}
