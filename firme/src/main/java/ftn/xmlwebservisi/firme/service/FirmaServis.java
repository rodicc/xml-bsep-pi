package ftn.xmlwebservisi.firme.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.firme.model.Firma;
import ftn.xmlwebservisi.firme.repository.FirmaRepozitorijum;

@Service
public class FirmaServis {

	@Autowired
	FirmaRepozitorijum firmaRepozitorijum;
	
	public void novaFirma(Firma firma) {
		firmaRepozitorijum.save(firma);
	}
	
	public List<Firma> nadjiSveFirme(){
		List<Firma> firme = new ArrayList<>();
		firmaRepozitorijum.findAll().forEach(firme::add);
		return firme;
	}
	
}
