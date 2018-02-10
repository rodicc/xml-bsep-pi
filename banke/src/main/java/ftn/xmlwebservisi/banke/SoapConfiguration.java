package ftn.xmlwebservisi.banke;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class SoapConfiguration {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this package must match the package in the <generatePackage> specified in
		// pom.xml
		marshaller.setContextPath("soap");
		return marshaller;
	}

	@Bean
	public SoapClient soapClient(Jaxb2Marshaller marshaller) {
		SoapClient client = new SoapClient();
		client.setDefaultUri("http://localhost:8083/ws");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}