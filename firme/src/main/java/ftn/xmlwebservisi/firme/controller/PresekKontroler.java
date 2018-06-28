package ftn.xmlwebservisi.firme.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ftn.xmlwebservisi.firme.model.Presek;
import ftn.xmlwebservisi.firme.model.ZahtevZaIzvod;
import ftn.xmlwebservisi.firme.service.PresekServis;

@RestController
@RequestMapping("/izvodi")
public class PresekKontroler {
	
	@Autowired
	private PresekServis presekServis;
	private final Logger logger = LoggerFactory.getLogger(PresekKontroler.class);
	
	@PostMapping
	public ResponseEntity<Presek> posaljiZahtevZaPresek(@RequestBody ZahtevZaIzvod nalog) {
		Presek response = presekServis.posaljiZahtevZaPresek(nalog);
		if(response != null) {
			return new ResponseEntity<Presek>(response, HttpStatus.OK);
		} else {
			logger.error("Invalid ZahtevZaIzvod request, Obj={}", nalog);
			return new ResponseEntity<Presek>(HttpStatus.BAD_REQUEST);
		}
	
		
	}
}
