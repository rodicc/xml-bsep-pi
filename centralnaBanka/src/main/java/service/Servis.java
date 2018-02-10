package service;

import java.util.GregorianCalendar;
import java.util.Random;

import xml.ftn.centralnabanka.MT102;
import xml.ftn.centralnabanka.MT102Response;
import xml.ftn.centralnabanka.MT103;
import xml.ftn.centralnabanka.MT103Response;
import xml.ftn.centralnabanka.MT900;
import xml.ftn.centralnabanka.MT910;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import helpers.Mapper;
import model.PojedinacnoPlacanjeMT102;
import repository.MT102Repository;
import repository.MT103Repository;
import repository.PojedincanoPlacanjeMT102Repository;
import repository.ZaglavljeMT102Repository;

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
	
	public MT102Response regulisiMT102(MT102 mt102Soap) {
		model.MT102 mt102 = mapper.MT102SoapToEntity(mt102Soap);
		zaglavljeMT102Repository.save(mt102.getZaglavljeMT102());
		pojedinacnoPlacanjeMT102Repository.save(mt102.getPojedinacnaPlacanja());
		mt102Repository.save(mt102);
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
