package org.example.soap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import service.Servis;

@Endpoint
public class SoapEndpoint {

	private static final String NAMESPACE_URI = "http://www.example.org/soap";
	@Autowired
	private Servis servis;
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "posaljiNalogZaPlacanjeRequest")
	@ResponsePayload
	public void posaljiNalogZaPlacanje(@RequestPayload PosaljiNalogZaPlacanjeRequest request) {
		servis.regulisiNalogZaPlacanje(request.getNalogZaPlacanje());
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "posaljiZahtevZaIzvodRequest")
	public PosaljiZahtevZaIzvodResponse posaljiZahtevZaIzvod(@RequestPayload PosaljiZahtevZaIzvodRequest request) {
		PosaljiZahtevZaIzvodResponse response = new PosaljiZahtevZaIzvodResponse();
		response.setPresek(servis.regulisiZahtevZaIzvod(request.getZahtevZaIzvod()));
		return response;
	}
}
