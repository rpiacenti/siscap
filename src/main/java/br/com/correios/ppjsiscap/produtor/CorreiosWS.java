package br.com.correios.ppjsiscap.produtor;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import br.com.correios.componente.idautorizador.usuario.IdAutorizadorUsuarioService;
import br.com.correios.ppjsiscap.enums.AmbientePublicacao;
import br.com.correios.ppjsiscap.util.UtilLog;

@ApplicationScoped
public class CorreiosWS {
	
	private Configuration properties;
	
	private AmbientePublicacao ambiente;
	
	private Map<AmbientePublicacao, String> adressLocationDosAmbientesIdCorreios;
	
	@PostConstruct
	void init() {
		
		instanciaPropertiesConfiguration();
		
		ambiente = AmbientePublicacao.valueOf(System.getProperty("ambiente.publicacao"));
		
		carregaConfiguracaoDoAdressLocationIdCorreios();
		
	}
	
	
	@Produces
	@ApplicationScoped
	public IdAutorizadorUsuarioService getIdAutorizadorUsuarioService() {
		return createServicePort(IdAutorizadorUsuarioService.class, 
				new QName("http://services.usuario.idautorizadorws.correios.com.br/", "idAutorizadorUsuario"),
				adressLocationDosAmbientesIdCorreios.get(ambiente));
	}

	
	
	private <T> T createServicePort(Class<T> clazz, QName qName, String endpointAddress) {
		T port = Service.create(qName).getPort(clazz);
		bindAddressAndAuthentication(port, endpointAddress);
		
		return port;
	}
	
	
	private void bindAddressAndAuthentication(Object port, String endpointAddress) {
		((BindingProvider) port).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointAddress);	
		((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, properties.getString("usuario"));

		String senha;
		if (AmbientePublicacao.PRODUCAO.equals(this.ambiente)) {
			senha = properties.getString("pwd.prod");
		} else {
			senha = properties.getString("pwd.hom");			
		}
		((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, senha);
		
	}
	
	private void carregaConfiguracaoDoAdressLocationIdCorreios(){				
		adressLocationDosAmbientesIdCorreios = new HashMap<AmbientePublicacao, String>();
		adressLocationDosAmbientesIdCorreios.put(AmbientePublicacao.LOCAL, properties.getString("idAutorizadorLocalAdressLocation"));
		adressLocationDosAmbientesIdCorreios.put(AmbientePublicacao.DESENVOLVIMENTO, properties.getString("idAutorizadorDesenvolvimentoAdressLocation"));
		adressLocationDosAmbientesIdCorreios.put(AmbientePublicacao.HOMOLOGACAO, properties.getString("idAutorizadorHomologacaoAdressLocation"));
		adressLocationDosAmbientesIdCorreios.put(AmbientePublicacao.PRODUCAO, properties.getString("idAutorizadorProducaoAdressLocation"));
	}

	
	
	private void instanciaPropertiesConfiguration() {
		try{
			properties = new PropertiesConfiguration("configuracaoWS.properties");
		} catch (ConfigurationException e) {
			UtilLog.logger.error("Ocorreu um erro:", e);
		}				
		
	}

}
