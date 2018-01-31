package br.com.correios.ppjseuprojeto.util.validadores;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.correios.ppjsiscap.util.validadores.UtilValidadorDeCPF;

public class UtilValidadorDeCPFTest {
	
	private UtilValidadorDeCPF validadorDeCPF;
	
	@Before
	public void setUp() throws Exception {
		this.validadorDeCPF = new UtilValidadorDeCPF();
	}

	@Test
	public void deveInvalidarUmNumeroComPadraoDeZero() {
		Assert.assertFalse(this.validadorDeCPF.validar("000.000.000-00"));
	}
	
	@Test
	public void deveInvalidarUmNumeroComPadraoDeNove() {
		Assert.assertFalse(this.validadorDeCPF.validar("999.999.999-99"));
	}
	
	@Test
	public void deveInvalidarQuandoParametroForNulo() {
		Assert.assertFalse(this.validadorDeCPF.validar(null));
	}
	
	@Test
	public void deveInvalidarQuandoParametroForVazio() {
		Assert.assertFalse(this.validadorDeCPF.validar(""));
	}
	
	@Test
	public void deveInvalidarQuandoParametroMenorDoQue11Caracteres() {
		Assert.assertFalse(this.validadorDeCPF.validar("187.837.765"));
	}
	
	
	public void deveValidarUmNumeroDeCPFValido(){
		Assert.assertTrue(this.validadorDeCPF.validar("187.837.765-59"));		
	}

	
	
}
