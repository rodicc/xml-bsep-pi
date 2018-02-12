package ftn.xmlwebservisi.firme.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.firme.helpers.PorukeWrapper;
import ftn.xmlwebservisi.firme.model.MT102;
import ftn.xmlwebservisi.firme.model.MT103;
import ftn.xmlwebservisi.firme.model.MT900;
import ftn.xmlwebservisi.firme.model.MT910;
import ftn.xmlwebservisi.firme.repository.MT102Repository;
import ftn.xmlwebservisi.firme.repository.MT103Repository;
import ftn.xmlwebservisi.firme.repository.MT900Repository;
import ftn.xmlwebservisi.firme.repository.MT910Repository;

@Service
public class PregledPorukaServis {

	@Autowired
	MT102Repository mt102Repo;
	
	@Autowired
	MT103Repository mt103Repo;
	
	@Autowired
	MT900Repository mt900Repo;
	
	@Autowired
	MT910Repository mt910Repo;
	
	public PorukeWrapper findAllMessages() {
		
		List<MT102> mt102Poruke =  new ArrayList<>();
		mt102Repo.findAll().forEach(mt102Poruke::add);
		
		List<MT103> mt103Poruke =  new ArrayList<>();
		mt103Repo.findAll().forEach(mt103Poruke::add);
		
		List<MT900> mt900Poruke =  new ArrayList<>();
		mt900Repo.findAll().forEach(mt900Poruke::add);
		
		List<MT910> mt910Poruke =  new ArrayList<>();
		mt910Repo.findAll().forEach(mt910Poruke::add);
		
		PorukeWrapper result = new PorukeWrapper(mt102Poruke, mt103Poruke, mt900Poruke, mt910Poruke);
		
		return result;
	}
	
}
