package ftn.xmlwebservisi.firme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ftn.xmlwebservisi.firme.helpers.PorukeWrapper;
import ftn.xmlwebservisi.firme.service.PregledPorukaServis;

@RestController
@RequestMapping("/poruke")
public class PregledPorukaKontroler {

	@Autowired
	PregledPorukaServis pregledPorukaServis;
	
	@GetMapping
	public PorukeWrapper prikaziSvePoruke() {
		System.out.println("servis");
		return pregledPorukaServis.findAllMessages();
	}
	
}
