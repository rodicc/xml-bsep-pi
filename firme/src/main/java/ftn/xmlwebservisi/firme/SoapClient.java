package ftn.xmlwebservisi.firme;

import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import ftn.xmlwebservisi.firme.helpers.Mapper;
import soap.NalogZaPlacanje;
import soap.PosaljiNalogZaPlacanjeRequest;

@Service
public class SoapClient extends WebServiceGatewaySupport {
	
	public void posaljiNalogZaPlacanje(ftn.xmlwebservisi.firme.model.NalogZaPlacanje nalog) {
		System.out.println("DOSAO");
		Mapper maper = new Mapper();
		NalogZaPlacanje nalogSoap = maper.nalogZaPlacanjeEntityToSoap(nalog);
		PosaljiNalogZaPlacanjeRequest posaljiNalog = new PosaljiNalogZaPlacanjeRequest();
		posaljiNalog.setNalogZaPlacanje(nalogSoap);
		
		this.getWebServiceTemplate().marshalSendAndReceive("http://localhost:8082/ws", posaljiNalog,
				new SoapActionCallback("http://www.ftn.xml/banke/posaljiNalogZaPlacanje"));
	}
	
}	
