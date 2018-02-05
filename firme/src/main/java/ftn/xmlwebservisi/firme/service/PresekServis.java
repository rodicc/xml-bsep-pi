package ftn.xmlwebservisi.firme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.firme.repository.PresekRepozitorijum;

@Service
public class PresekServis {
	@Autowired
	private PresekRepozitorijum presekRepozitorijum;
}
