package ftn.xmlwebservisi.firme;


import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import ftn.xmlwebservisi.firme.helpers.Mapper;
import ftn.xmlwebservisi.firme.model.ZahtevZaIzvod;
import soap.NalogZaPlacanje;
import soap.PosaljiNalogZaPlacanjeRequest;
import soap.PosaljiZahtevZaIzvodResponse;

public class SoapClient extends WebServiceGatewaySupport {
	
	public void posaljiNalogZaPlacanje(ftn.xmlwebservisi.firme.model.NalogZaPlacanje nalog) {
		Mapper maper = new Mapper();
		NalogZaPlacanje nalogSoap = maper.nalogZaPlacanjeEntityToSoap(nalog);
		PosaljiNalogZaPlacanjeRequest posaljiNalog = new PosaljiNalogZaPlacanjeRequest();
		posaljiNalog.setNalogZaPlacanje(nalogSoap);
		SoapActionCallback callback = new SoapActionCallback("http://www.ftn.xml/banke/posaljiNalogZaPlacanjeRequest");
		WebServiceTemplate template = getWebServiceTemplate();
		try {
			template.marshalSendAndReceive("http://localhost:8082/ws", posaljiNalog, callback);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PosaljiZahtevZaIzvodResponse posaljiZahtevZaIzvod(ZahtevZaIzvod zahtev) {
		Mapper mapper = new Mapper();
		soap.ZahtevZaIzvod zahtevZaIzvod = mapper.zahtevZaIzvodEntityToSoap(zahtev);
		return  (PosaljiZahtevZaIzvodResponse) getWebServiceTemplate().marshalSendAndReceive("http://localhost:8082/ws", zahtevZaIzvod, 
				new SoapActionCallback("http://www.ftn.xml/banke/posaljiZahtevZaIzvodRequest"));
	}
}	
