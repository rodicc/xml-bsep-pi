package ftn.xmlwebservisi.firme.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.firme.model.NalogZaPresek;
import ftn.xmlwebservisi.firme.model.Presek;
import ftn.xmlwebservisi.firme.model.StavkaPreseka;

@Service
public class PresekServis {
	
	public Presek posaljiZahtevZaPresek(NalogZaPresek nalogZaPresek) {
		
		//soap zahtev vraca soap.presek koji treba parsirati u model.presek
		
		Presek result = new Presek();
		result.setBrojPreseka(nalogZaPresek.getRedniBrojPreseka());		
		result.setBrojPromenaNaTeret(0);
		result.setBrojPromenaUKorist(3);
		result.setBrojRacuna(nalogZaPresek.getBrojRacuna());
		result.setDatumNaloga(nalogZaPresek.getDatum());
		result.setIdPreseka(3);
		result.setNovoStanje(BigDecimal.valueOf(25000));
		result.setPrethodnoStanje(BigDecimal.valueOf(20000));
		result.setUkupnoNaTeret(BigDecimal.valueOf(0));
		result.setUkupnoUKorist(BigDecimal.valueOf(5000));
		
		StavkaPreseka stavka1 = new StavkaPreseka();
		stavka1.setDatumNaloga(nalogZaPresek.getDatum());
		stavka1.setDatumValute(nalogZaPresek.getDatum());
		stavka1.setDuznikNalogodavac("Bogdan");
		stavka1.setId(123);
		stavka1.setIdStavke(234);
		stavka1.setIznos(BigDecimal.valueOf(1500));
		stavka1.setModelOdobrenja(12);
		stavka1.setModelZaduzenja(97);
		stavka1.setPozivNaBrojOdobrenja("666");
		stavka1.setPozivNaBrojZaduzenja("777");
		stavka1.setPrimalacPoverilac("primalacPoverilac");
		stavka1.setRacunDuznika("12345678");
		stavka1.setRacunPoverioca("987654321");
		stavka1.setSmer('1');
		stavka1.setSvrhaPlacanja("svrhaPlacanja");
		
		result.getStavkePreseka().add(stavka1);
		result.getStavkePreseka().add(stavka1);
		result.getStavkePreseka().add(stavka1);
		
		return result;
	}
	
}
