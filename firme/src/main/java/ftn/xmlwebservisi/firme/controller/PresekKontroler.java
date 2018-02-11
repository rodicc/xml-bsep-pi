package ftn.xmlwebservisi.firme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ftn.xmlwebservisi.firme.model.NalogZaPresek;
import ftn.xmlwebservisi.firme.model.Presek;
import ftn.xmlwebservisi.firme.model.ZahtevZaIzvod;
import ftn.xmlwebservisi.firme.service.PresekServis;

@RestController
@RequestMapping("/izvodi")
public class PresekKontroler {
	
	@Autowired
	PresekServis presekServis;
	
	@PostMapping
	public Presek posaljiZahtevZaPresek(@RequestBody ZahtevZaIzvod nalog) {
		return presekServis.posaljiZahtevZaPresek(nalog);
		
	}
}
