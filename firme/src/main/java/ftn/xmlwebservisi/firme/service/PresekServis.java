package ftn.xmlwebservisi.firme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.firme.SoapClient;
import ftn.xmlwebservisi.firme.helpers.Mapper;
import ftn.xmlwebservisi.firme.model.Presek;
import ftn.xmlwebservisi.firme.model.ZahtevZaIzvod;
import soap.PosaljiZahtevZaIzvodResponse;

@Service
public class PresekServis {
	
	@Autowired
	private SoapClient client;
	@Autowired
	private Mapper mapper;
	
	public Presek posaljiZahtevZaPresek(ZahtevZaIzvod zahtevZaIzvod) {
		PosaljiZahtevZaIzvodResponse odgovor = client.posaljiZahtevZaIzvod(zahtevZaIzvod);		
		return mapper.presekSoapToEntity(odgovor.getPresek());
	}
	
}
