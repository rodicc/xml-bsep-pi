package ftn.xmlwebservisi.firme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.firme.repository.StavkaPresekaRepozitorijum;

@Service
public class StavkaPresekaServis {
	
	@Autowired
	private StavkaPresekaRepozitorijum stavkaPresekaRepozitorijum;
	
}
