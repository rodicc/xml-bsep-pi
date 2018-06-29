package ftn.xmlwebservisi.firme.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
	private final Logger logger = LoggerFactory.getLogger(NalogZaPlacanjeKontroler.class);
	
	@PreAuthorize("hasAnyRole('INSIDER')")
	@GetMapping
	public ResponseEntity<List<NalogZaPlacanje>> nadjiSveNaloge() {
		List<NalogZaPlacanje> response = nalogZaPlacanjeServis.nadjiSveNalogeZaPlacanje();
		if(response != null) {
			return new ResponseEntity<List<NalogZaPlacanje>>(response, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<NalogZaPlacanje>>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PreAuthorize("hasAnyRole('USER', 'INSIDER')")
	@PostMapping
	public ResponseEntity<NalogZaPlacanje> dodajNalog(@RequestBody NalogZaPlacanje nalog) {
		boolean validan = MyJsonValidator.validirajNalogZaPlacanje(nalog);
		
		if (!validan) {
			logger.error("Invalid NalogZaPlacanje request, Obj={}", nalog);
			return new ResponseEntity<NalogZaPlacanje>(HttpStatus.BAD_REQUEST);
		} else {
			client.posaljiNalogZaPlacanje(nalog);
			return new ResponseEntity<NalogZaPlacanje>(HttpStatus.OK);
		}
	}
	
}
