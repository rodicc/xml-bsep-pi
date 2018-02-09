package ftn.xmlwebservisi.firme.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.firme.model.Faktura;
import ftn.xmlwebservisi.firme.repository.FakturaRepozitorijum;

@Service
public class FakturaServis {
	
	@Autowired
	private FakturaRepozitorijum fakturaRepozitorijum;
	
	public List<Faktura> nadjiSveFakture() {
		List<Faktura> fakture = new ArrayList<>();
		fakturaRepozitorijum.findAll().forEach(fakture::add);
		return fakture;
	}
	
	public Faktura nadjiFakturu(Integer id) {
		return fakturaRepozitorijum.findOne(id);
	}
	
	public Faktura novaFaktura(Faktura faktura) {
		return fakturaRepozitorijum.save(faktura);
	}
	
}
