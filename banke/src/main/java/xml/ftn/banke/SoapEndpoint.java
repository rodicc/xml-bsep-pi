package xml.ftn.banke;

import xml.ftn.banke.PosaljiNalogZaPlacanjeRequest;
import xml.ftn.banke.PosaljiZahtevZaIzvodRequest;
import xml.ftn.banke.PosaljiZahtevZaIzvodResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import service.Servis;

@Endpoint
public class SoapEndpoint {

	private static final String NAMESPACE_URI = "http://www.ftn.xml/banke";
	@Autowired
	private Servis servis;
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "posaljiNalogZaPlacanjeRequest")
	public void posaljiNalogZaPlacanje(@RequestPayload PosaljiNalogZaPlacanjeRequest request) {
		System.out.println("ovde");
		servis.regulisiNalogZaPlacanje(request.getNalogZaPlacanje());
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "posaljiZahtevZaIzvodRequest")
	public PosaljiZahtevZaIzvodResponse posaljiZahtevZaIzvod(@RequestPayload PosaljiZahtevZaIzvodRequest request) {
		PosaljiZahtevZaIzvodResponse response = new PosaljiZahtevZaIzvodResponse();
		response.setPresek(servis.regulisiZahtevZaIzvod(request.getZahtevZaIzvod()));
		return response;
	}
}
