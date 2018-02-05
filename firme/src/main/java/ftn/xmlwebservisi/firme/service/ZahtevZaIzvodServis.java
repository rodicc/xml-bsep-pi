package ftn.xmlwebservisi.firme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.firme.repository.ZahtevZaIzvodRepozitorijum;

@Service
public class ZahtevZaIzvodServis {
	
	@Autowired
	private ZahtevZaIzvodRepozitorijum zahtevZaIzvodRepozitorijum;
}
