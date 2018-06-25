package service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import helpers.Mapper;
import model.Banka;
import model.PojedinacnoPlacanjeMT102;
import repository.BankaRepository;
import repository.MT102Repository;
import repository.MT103Repository;
import repository.PojedincanoPlacanjeMT102Repository;
import repository.ZaglavljeMT102Repository;
import xml.ftn.centralnabanka.MT102;
import xml.ftn.centralnabanka.MT102Response;
import xml.ftn.centralnabanka.MT103;
import xml.ftn.centralnabanka.MT103Response;
import xml.ftn.centralnabanka.MT900;
import xml.ftn.centralnabanka.MT910;

@Service
public class Servis {

	@Autowired
	private Mapper mapper;
	@Autowired
	private MT103Repository mt103Repository;
	@Autowired
	private MT102Repository mt102Repository;
	@Autowired
	private ZaglavljeMT102Repository zaglavljeMT102Repository;
	@Autowired
	private PojedincanoPlacanjeMT102Repository pojedinacnoPlacanjeMT102Repository;
	@Autowired
	private BankaRepository bankaRepository;
	
	public MT102Response regulisiMT102(MT102 mt102Soap) {
		model.MT102 mt102 = mapper.MT102SoapToEntity(mt102Soap);
		zaglavljeMT102Repository.save(mt102.getZaglavljeMT102());
		List<PojedinacnoPlacanjeMT102> pojedinacnaPlacanja = mt102.getPojedinacnaPlacanja();
		for (PojedinacnoPlacanjeMT102 placanje : pojedinacnaPlacanja) {
			pojedinacnoPlacanjeMT102Repository.save(placanje);
		}
		mt102Repository.save(mt102);
		
		// prebacivanje novca izmedju banaka
		String swiftBankeDuznika = mt102.getZaglavljeMT102().getSwiftKodBankeDuznika();
		model.Banka bankaDuznika = bankaRepository.findBySwiftKodBanke(swiftBankeDuznika);
		String swiftBankePoverioca = mt102.getZaglavljeMT102().getSwiftKodBankePoverioca();
		model.Banka bankaPoverioca = bankaRepository.findBySwiftKodBanke(swiftBankePoverioca);
		
		double novoStanjeBankeDuznika = bankaDuznika.getStanjeRacuna().doubleValue() - mt102.getZaglavljeMT102().getUkupanIznos().doubleValue();
		bankaDuznika.setStanjeRacuna(new BigDecimal(novoStanjeBankeDuznika));
		double novoStanjeBankePoverioca = bankaPoverioca.getStanjeRacuna().doubleValue() + mt102.getZaglavljeMT102().getUkupanIznos().doubleValue();
		bankaPoverioca.setStanjeRacuna(new BigDecimal(novoStanjeBankePoverioca));
		bankaRepository.save(bankaDuznika);
		bankaRepository.save(bankaPoverioca);
		
		MT102Response response = new MT102Response();
		response.setMT102(mt102Soap);
		MT900 mt900 = new MT900();
		Random random = new Random();
		mt900.setIdPoruke(Integer.toString(random.nextInt(1000000)));
		mt900.setSwiftKodBankeDuznika(mt102.getZaglavljeMT102().getSwiftKodBankeDuznika());
		mt900.setObracunskiRacunBankeDuznika(mt102.getZaglavljeMT102().getObracunskiRacunBankeDuznika());
		mt900.setIdPorukeNaloga(mt102.getZaglavljeMT102().getIdPoruke());
		mt900.setDatumValute(mt102Soap.getZaglavljeMT102().getDatumValute());
		mt900.setIznos(mt102.getZaglavljeMT102().getUkupanIznos());
		mt900.setSifraValute(mt102.getZaglavljeMT102().getSifraValute());
		response.setMT900(mt900);
		MT910 mt910 = new MT910();
		mt910.setIdPoruke(Integer.toString(random.nextInt(1000000)));
		mt910.setSwiftKodBankePoverioca(mt102.getZaglavljeMT102().getSwiftKodBankePoverioca());
		mt910.setObracunskiRacunBankePoverioca(mt102.getZaglavljeMT102().getObracunskiRacunBankePoverioca());
		mt910.setIdPorukeNaloga(mt102.getZaglavljeMT102().getIdPoruke());
		mt910.setDatumValute(mt102Soap.getZaglavljeMT102().getDatumValute());
		mt910.setIznos(mt102.getZaglavljeMT102().getUkupanIznos());
		mt910.setSifraValute(mt102.getZaglavljeMT102().getSifraValute());
		response.setMT910(mt910);
		return response;
	}
	
	public MT103Response regulisiMT103(MT103 mt103Soap) {
		
		model.MT103 mt103 = mapper.MT103SoapToEntity(mt103Soap);
		mt103Repository.save(mt103);
		
		Banka bankaDuznika = bankaRepository.findBySwiftKodBanke(mt103Soap.getSwiftKodBankeDuznika());
		bankaDuznika.setStanjeRacuna(new BigDecimal(bankaDuznika.getStanjeRacuna().doubleValue() - mt103Soap.getIznos().doubleValue()));
		bankaRepository.save(bankaDuznika);
		Banka bankaPoverioca = bankaRepository.findBySwiftKodBanke(mt103Soap.getSwiftKodBankePoverioca());
		bankaPoverioca.setStanjeRacuna(new BigDecimal(bankaPoverioca.getStanjeRacuna().doubleValue() + mt103Soap.getIznos().doubleValue()));
		bankaRepository.save(bankaPoverioca);
		
		MT103Response response = new MT103Response();
		response.setMT103(mt103Soap);
		
		MT900 mt900 = new MT900();
		Random random = new Random();
		mt900.setIdPoruke(Integer.toString(random.nextInt(1000000)));
		mt900.setSwiftKodBankeDuznika(mt103.getSwiftKodBankeDuznika());
		mt900.setObracunskiRacunBankeDuznika(mt103.getObracunskiRacunBankeDuznika());
		mt900.setIdPorukeNaloga(mt103.getIdPoruke());
		mt900.setDatumValute(mt103Soap.getDatumValute());
		mt900.setIznos(mt103.getIznos());
		mt900.setSifraValute(mt103.getSifraValute());
		response.setMT900(mt900);
		
		MT910 mt910 = new MT910();
		mt910.setIdPoruke(Integer.toString(random.nextInt(1000000)));
		mt910.setSwiftKodBankePoverioca(mt103.getSwiftKodBankePoverioca());
		mt910.setObracunskiRacunBankePoverioca(mt103.getObracunskiRacunBankePoverioca());
		mt910.setIdPorukeNaloga(mt103.getIdPoruke());
		mt910.setDatumValute(mt103Soap.getDatumValute());
		mt910.setIznos(mt103.getIznos());
		mt910.setSifraValute(mt103.getSifraValute());
		response.setMT910(mt910);
		
		return response;
	}
}
