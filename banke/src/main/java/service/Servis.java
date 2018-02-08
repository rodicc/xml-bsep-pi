package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;

import org.example.soap.NalogZaPlacanje;
import org.example.soap.Presek;
import org.example.soap.StavkaPreseka;
import org.example.soap.Zaglavlje;
import org.example.soap.ZahtevZaIzvod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import helpers.Mapper;
import repository.NalogZaPlacanjeRepository;
import repository.ZahtevZaIzvodRepository;

@Service
public class Servis {

	@Autowired
	private Mapper mapper;
	@Autowired
	private NalogZaPlacanjeRepository nalogZaPlacanjeRepository;
	@Autowired
	private ZahtevZaIzvodRepository zahtevZaIzvodRepository;
	
	public void regulisiNalogZaPlacanje(NalogZaPlacanje nalog) {
		nalogZaPlacanjeRepository.save(mapper.NalogZaPlacanjeSoapToEntity(nalog));
	}
	
	public Presek regulisiZahtevZaIzvod(ZahtevZaIzvod zahtev) {
		try {
		zahtevZaIzvodRepository.save(mapper.zahtevZaIzvodSoapToEntity(zahtev));
		List<model.NalogZaPlacanje> naloziZaDatum = nalogZaPlacanjeRepository.nadjiPoDatumuIBrojuRacuna(zahtev.getDatum().toGregorianCalendar().getTime(), zahtev.getBrojRacuna());
		List<model.NalogZaPlacanje> trazeniNalozi = new ArrayList<model.NalogZaPlacanje>();
		for(int i = (zahtev.getRedniBrojPreseka()-1)*3; i < zahtev.getRedniBrojPreseka()*3; i++) {
			if(i >= naloziZaDatum.size()) break;
			trazeniNalozi.add(naloziZaDatum.get(i));
		}
		if(trazeniNalozi.size() == 0) return null;
		int brojPromenaUKorist = 0;
		double ukupnoUKorist = 0;
		int brojPromenaNaTeret = 0;
		double ukupnoNaTeret = 0;
		List<StavkaPreseka> stavkePreseka = new ArrayList<StavkaPreseka>();
		for(model.NalogZaPlacanje nalog : trazeniNalozi) {
			StavkaPreseka stavka = new StavkaPreseka();
			stavka.setDuznik(nalog.getDuznikNalogodavac());
			stavka.setSvrhaPlacanja(nalog.getSvrhaPlacanja());
			stavka.setPrimalac(nalog.getPrimalacPoverilac());
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(nalog.getDatumNaloga());
			stavka.setDatumNaloga(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
			calendar.setTime(nalog.getDatumValute());
			stavka.setDatumValute(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
			stavka.setRacunDuznika(nalog.getRacunDuznika());
			stavka.setModelZaduzenja(nalog.getModelZaduzenja());
			stavka.setPozivNaBrojZaduzenja(nalog.getPozivNaBrojZaduzenja());
			stavka.setRacunPrimaoca(nalog.getRacunPrimaoca());
			stavka.setModelOdobrenja(nalog.getModelOdobrenja());
			stavka.setPozivNaBrojOdobrenja(nalog.getPozivNaBrojOdobrenja());
			stavka.setIznos(nalog.getIznos());
			if(nalog.getRacunDuznika() == zahtev.getBrojRacuna()) {
				stavka.setSmer("o");
				brojPromenaNaTeret++;
				ukupnoNaTeret += nalog.getIznos().doubleValue();
			}else {
				stavka.setSmer("i");
				brojPromenaUKorist++;
				ukupnoUKorist += nalog.getIznos().doubleValue();
			}
			stavkePreseka.add(stavka);
		}
		Zaglavlje zaglavlje = new Zaglavlje();
		zaglavlje.setBrojRacuna(zahtev.getBrojRacuna());
		zaglavlje.setDatumNaloga(zahtev.getDatum());
		zaglavlje.setBrojPreseka(zahtev.getRedniBrojPreseka());
		zaglavlje.setBrojPromenaUKorist(brojPromenaUKorist);
		zaglavlje.setUkupnoUKorist(new BigDecimal(ukupnoUKorist));
		zaglavlje.setBrojPromenaNaTeret(brojPromenaNaTeret);
		zaglavlje.setUkupnoNaTeret(new BigDecimal(ukupnoNaTeret));
		//treba novo stanje i prethodno stanje
		Presek presek = new Presek();
		presek.setZaglavlje(zaglavlje);
		presek.setStavkaPreseka(stavkePreseka);		
		return presek;
		}catch (Exception e) {
			return null;
		}
	}
}
