package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dto.KlijentDto;
import dto.RacunDto;
import dto.UkidanjeDto;
import model.Banka;
import model.Firma;
import model.NalogZaPlacanje;
import model.Racun;
import model.UkidanjeRacuna;
import repository.BankaRepository;
import repository.FirmaRepository;
import repository.RacunRepository;
import repository.UkidanjeRacunaRepository;

@Service
public class RacunService {
	
	@Autowired
	RacunRepository racunRepository;
	
	@Autowired
	FirmaRepository firmaRepository;
	
	@Autowired
	UkidanjeRacunaRepository ukidanjeRacunaRepository;
	
	@Autowired
	BankaRepository bankaRepository;
	
	@Autowired
	Servis servis;
	
	public Racun noviRacun(RacunDto dto) {
		if(dto.getBrojRacuna() != null) {
			Racun racun = new Racun();
			Banka banka = bankaRepository.findByOznakaBanke(dto.getBrojRacuna().substring(0, 3));
			if( banka == null) {
				return null;
			}
			racun.setBrojRacuna(dto.getBrojRacuna());
			racun.setFirma(firmaRepository.findById(dto.getFirmaId()));
			racun.setBanka_owner(banka);
			racunRepository.save(racun);
			return racun;
		}
		return null;
	}
	


	public UkidanjeRacuna ukiniRacun(UkidanjeDto dto) {
		Racun racunZaUkidanje = racunRepository.findByBrojRacuna(dto.getBrojRacunaZaUkidanje());
		if( racunZaUkidanje != null && racunZaUkidanje.isVazeci()) {
			
			if(dto.getBrojRacunaZaPrenosSredstava() != null) {
				Racun racunZaPrenos = racunRepository.findByBrojRacuna(dto.getBrojRacunaZaPrenosSredstava());
				NalogZaPlacanje nalog = new NalogZaPlacanje();
				nalog.setDatumNaloga(new Date());
				nalog.setDatumValute(new Date());
				nalog.setDuznikNalogodavac(racunZaUkidanje.getFirma().getNazivFirme());
				nalog.setRacunDuznika(racunZaUkidanje.getBrojRacuna());
				nalog.setPrimalacPoverilac(racunZaPrenos.getBrojRacuna());
				nalog.setRacunPrimaoca(racunZaPrenos.getBrojRacuna());
				nalog.setIznos(racunZaPrenos.getStanjeRacuna());
				nalog.setModelOdobrenja(10);
				nalog.setModelZaduzenja(11);
				nalog.setOznakaValute("RSD");
				nalog.setHitno(false);
				nalog.setPozivNaBrojOdobrenja("");
				nalog.setPozivNaBrojZaduzenja("");
				nalog.setSifraPlacanja(97);
				nalog.setSvrhaPlacanja("Prenos sredstava sa racuna koji se ukida");
				servis.regulisiClearing(nalog);
				
				UkidanjeRacuna ukidanje = new UkidanjeRacuna();
				ukidanje.setRacunNaKojiSePrebacujuSredstva(dto.getBrojRacunaZaPrenosSredstava());
				ukidanje.setUkinutRacun(racunZaUkidanje);
				ukidanjeRacunaRepository.save(ukidanje);
				
				racunZaUkidanje.setVazeci(false);
				racunZaUkidanje.setUkidanjeRacuna(ukidanje);
				racunRepository.save(racunZaUkidanje);
				
				return ukidanje;
			}
		}
		return null;
	}

	public List<Racun> sviRacuni() {
		List<Racun> result = new ArrayList<>();
		racunRepository.findByVazeci(true).forEach(result :: add);
		return result;
	}
	
	public List<Racun> sviUkinutiRacuni() {
		List<Racun> result = new ArrayList<>();
		racunRepository.findByVazeci(false).forEach(result :: add);
		return result;
	}
	
	
	
}
