package ftn.xmlwebservisi.firme.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ftn.xmlwebservisi.firme.model.Faktura;
import ftn.xmlwebservisi.firme.model.StavkaFakture;
import ftn.xmlwebservisi.firme.service.FakturaServis;
import ftn.xmlwebservisi.firme.service.StavkaFaktureServis;

@RestController
@RequestMapping("/fakture")
public class FakturaKontroler {
	
	@Autowired
	FakturaServis fakturaServis;
	
	@Autowired
	StavkaFaktureServis stavkaFaktureServis;
	
	@GetMapping
	public List<Faktura> nadjiSveFakture() {
		return fakturaServis.nadjiSveFakture();
	}
	
	@PreAuthorize("hasAnyRole('INSIDER')")
	@GetMapping("/{id}")
	public ResponseEntity<Faktura> nadjiFakturu(@PathVariable(value = "id") Integer fakturaId) {
		Faktura nadjenaFaktura = fakturaServis.nadjiFakturu(fakturaId);
		if (nadjenaFaktura == null) {
			return ResponseEntity.badRequest().build();
		}
		return new ResponseEntity<>(nadjenaFaktura, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping
	public Faktura dodajFakturu(@RequestBody Faktura faktura) {
		return fakturaServis.novaFaktura(faktura); 
	}
	
	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping("/{id}")
	public ResponseEntity<StavkaFakture> dodajStavkuFakture(@PathVariable(value = "id") Integer fakturaId,
											@RequestBody StavkaFakture stavkaFakture) {
		Faktura faktura = fakturaServis.nadjiFakturu(fakturaId);
		if (faktura == null) {
			return ResponseEntity.badRequest().build();
		}
		stavkaFakture.setFaktura(faktura);
		StavkaFakture kreiranaStavka = stavkaFaktureServis.dodajStavkuFakture(stavkaFakture);
		return new ResponseEntity<>(kreiranaStavka, HttpStatus.CREATED);
	}
	
	
}
