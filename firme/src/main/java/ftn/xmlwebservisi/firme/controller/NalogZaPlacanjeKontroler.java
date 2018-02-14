package ftn.xmlwebservisi.firme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ftn.xmlwebservisi.firme.SoapClient;
import ftn.xmlwebservisi.firme.helpers.MyJsonValidator;
import ftn.xmlwebservisi.firme.model.NalogZaPlacanje;
import ftn.xmlwebservisi.firme.service.NalogZaPlacanjeServis;

@RestController
@RequestMapping("/nalozi")
public class NalogZaPlacanjeKontroler {
	
	@Autowired
	NalogZaPlacanjeServis nalogZaPlacanjeServis;
	@Autowired
	SoapClient client;
	
	@GetMapping
	public List<NalogZaPlacanje> nadjiSveNaloge() {
		return nalogZaPlacanjeServis.nadjiSveNalogeZaPlacanje();
	}
	
	@PostMapping
	public ResponseEntity<NalogZaPlacanje> dodajNalog(@RequestBody NalogZaPlacanje nalog) {
		boolean validan = MyJsonValidator.validirajNalogZaPlacanje(nalog);
		
		if (!validan) {
			return ResponseEntity.badRequest().build();
		}
		client.posaljiNalogZaPlacanje(nalog);
		return ResponseEntity.ok().build();
	}
	
}
