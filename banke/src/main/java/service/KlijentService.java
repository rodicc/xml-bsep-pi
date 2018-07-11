package service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dto.KlijentDto;
import model.Banka;
import model.Firma;
import model.Racun;
import repository.BankaRepository;
import repository.FirmaRepository;

@Service
public class KlijentService {

	@Autowired
	FirmaRepository firmaRepository;

	@Autowired
	BankaRepository bankaRepository;
	
	public Racun noviKlijent(KlijentDto dto) {
		if(dto.getBrojRacuna() != null && 
				dto.getAdresa() != null &&
				dto.getNazivFirme() != null && 
				dto.getPib() != null) {
			
			Firma firma = new Firma(dto.getNazivFirme(), dto.getAdresa(), dto.getPib());
			Racun racun = new Racun();
			Banka banka = bankaRepository.findByOznakaBanke(dto.getBrojRacuna().substring(0, 3));
			if( banka == null) {
				return null;
			}
			racun.setBrojRacuna(dto.getBrojRacuna());
			racun.setFirma(firma);
			racun.setBanka(banka);
			ArrayList<Racun> racuni = new ArrayList<>();
			racuni.add(racun);
			firma.setRacuni(racuni);
			firmaRepository.save(firma);
			return racun;
		}
		return null;
	}
	
	public List<Firma> sviKlijenti() {
		List<Firma> result = new ArrayList<>();
		firmaRepository.findAll().forEach(result :: add);
		return result;
	}
}
