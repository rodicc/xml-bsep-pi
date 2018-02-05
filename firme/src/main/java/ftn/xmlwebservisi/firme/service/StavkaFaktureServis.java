package ftn.xmlwebservisi.firme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.firme.repository.StavkaFaktureRepozitorijum;

@Service
public class StavkaFaktureServis {
	
	@Autowired
	private StavkaFaktureRepozitorijum stavkaFaktureRepozitorijum;
}
