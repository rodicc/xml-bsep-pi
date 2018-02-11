package ftn.xmlwebservisi.firme;

import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import ftn.xmlwebservisi.firme.helpers.Mapper;
import soap.NalogZaPlacanje;
import soap.PosaljiNalogZaPlacanjeRequest;

public class SoapClient extends WebServiceGatewaySupport {
	
	public void posaljiNalogZaPlacanje(ftn.xmlwebservisi.firme.model.NalogZaPlacanje nalog) {
		System.out.println("DOSAO");
		Mapper maper = new Mapper();
		NalogZaPlacanje nalogSoap = maper.nalogZaPlacanjeEntityToSoap(nalog);
		PosaljiNalogZaPlacanjeRequest posaljiNalog = new PosaljiNalogZaPlacanjeRequest();
		posaljiNalog.setNalogZaPlacanje(nalogSoap);
		System.out.println("ovde");
		SoapActionCallback callback = new SoapActionCallback("http://www.ftn.xml/banke/posaljiNalogZaPlacanjeRequest");
		System.out.println("callback napravljen");
		WebServiceTemplate template = getWebServiceTemplate();
		System.out.println(template);
		try {
			System.out.println(template.getMarshaller());
			template.marshalSendAndReceive("http://localhost:8082/ws", posaljiNalog, callback);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("nope");
	}
	
}	
