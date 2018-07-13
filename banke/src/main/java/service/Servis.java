package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.banke.SoapClient;
import helpers.Mapper;
import model.Banka;
import model.DnevnoStanjeRacuna;
import model.Racun;
import repository.BankaRepository;
import repository.DnevnoStanjeRepository;
import repository.MT900Repository;
import repository.MT910Repository;
import repository.NalogZaPlacanjeRepository;
import repository.RacunRepository;
import repository.ZahtevZaIzvodRepository;
import soap.MT102;
import soap.MT102Response;
import soap.MT103;
import soap.MT103Response;
import soap.MT900;
import soap.MT910;
import soap.PojedinacnoPlacanjeMT102;
import soap.ZaglavljeMT102;
import xml.ftn.banke.NalogZaPlacanje;
import xml.ftn.banke.Presek;
import xml.ftn.banke.StavkaPreseka;
import xml.ftn.banke.Zaglavlje;
import xml.ftn.banke.ZahtevZaIzvod;

@Service
public class Servis {

	@Autowired
	private Mapper mapper;
	@Autowired
	private NalogZaPlacanjeRepository nalogZaPlacanjeRepository;
	@Autowired
	private ZahtevZaIzvodRepository zahtevZaIzvodRepository;
	@Autowired
	private SoapClient client;
	@Autowired
	private RacunRepository racunRepository;
	@Autowired
	private BankaRepository bankaRepository;
	@Autowired
	private MT900Repository mt900Repository;
	@Autowired
	private MT910Repository mt910Repository;
	@Autowired 
	private DnevnoStanjeRepository dnevnoStanjeRepository;
	
	private final Logger logger = LoggerFactory.getLogger(Servis.class);

	public void regulisiNalogZaPlacanje(NalogZaPlacanje nalog) {

		if (nalog.isHitno() || nalog.getIznos().doubleValue() >= 250000) {
			regulisiRTGS(nalog);
			model.NalogZaPlacanje nalogZaPlacanje = mapper.NalogZaPlacanjeSoapToEntity(nalog);
			nalogZaPlacanje.setNijeRegulisan(false);
			this.upisiNalogZaPlacanje(nalogZaPlacanje);
		} else {
			regulisiClearing(nalog);
		}

	}

	private void regulisiRTGS(NalogZaPlacanje nalog) {
		if(nalogZaPlacanjeRepository.findByIdPoruke(nalog.getIdPoruke()) == null) {
			MT103 mt103 = kreirajMT103(nalog);
			if(mt103 == null) {
				logger.error("Aborting regulisiRTGS, got null for MT103");
			}
			String racunDuznika = nalog.getRacunDuznika();
			Racun firmaKojaPlaca = racunRepository.findByBrojRacuna(racunDuznika);
			if(firmaKojaPlaca == null) {
				logger.error("FirmaRepository.findByBrojRacuna() returned null for Obj={}", racunDuznika);
			}
			rezervisiNovac(firmaKojaPlaca, nalog.getIznos());
			racunRepository.save(firmaKojaPlaca);
	
			// centralna banka prebacuje sredstva izmedju banaka
			MT103Response odgovor = client.sendMT103(mt103);
			if (odgovor.getMT103() != null && odgovor.getMT900() != null && odgovor.getMT910() != null) {
				mt900Repository.save(mapper.mt900SoapToEntity(odgovor.getMT900()));
				mt910Repository.save(mapper.mt910SoapToEntity(odgovor.getMT910()));
				

				double iznosNaloga = nalog.getIznos().doubleValue();
				// skidanje rezervisanog novca
				firmaKojaPlaca.setRezervisanNovac(
						new BigDecimal(firmaKojaPlaca.getRezervisanNovac().doubleValue() - iznosNaloga));
				racunRepository.save(firmaKojaPlaca);
	
				// dodavanje iznosa firmi kojoj se placa
				String racunPrimaoca = nalog.getRacunPrimaoca();
				Racun firmaKojojSePlaca = racunRepository.findByBrojRacuna(racunPrimaoca);
				if(firmaKojojSePlaca == null) {
					logger.error("FirmaRepository.findByBrojRacuna() returned null for Obj={}", racunPrimaoca);
				}
				firmaKojojSePlaca
						.setStanjeRacuna(new BigDecimal(firmaKojojSePlaca.getStanjeRacuna().doubleValue() + iznosNaloga));
				racunRepository.save(firmaKojojSePlaca);
	
				// banka prebacuje sredstva od klijenta sebi
				String swiftKodBankeDuznika = odgovor.getMT900().getSwiftKodBankeDuznika();
				Racun racunBankeDuznika = bankaRepository.findBySwiftKodBanke(swiftKodBankeDuznika).getObracunskiRacun();
				if(racunBankeDuznika == null) {
					logger.error("BankaRepository.findBySwiftKodBanke returned null for Obj={}", swiftKodBankeDuznika);
				}
				racunBankeDuznika.setStanjeRacuna(new BigDecimal(racunBankeDuznika.getStanjeRacuna().doubleValue() + iznosNaloga));
				racunRepository.save(racunBankeDuznika);
	
				// banka prebacuje na racun klijenta
				Racun racunBankePoverioca = bankaRepository.findBySwiftKodBanke(odgovor.getMT910().getSwiftKodBankePoverioca()).getObracunskiRacun();
				if(racunBankePoverioca == null) {
					logger.error("BankaRepository.findBySwiftKodBanke returned null for Obj={}", odgovor.getMT910().getSwiftKodBankePoverioca());
				}
				racunBankePoverioca
						.setStanjeRacuna(new BigDecimal(racunBankePoverioca.getStanjeRacuna().doubleValue() - iznosNaloga));
				racunRepository.save(racunBankePoverioca);
			} else {
				logger.error("Invalid MT103Response, got null MT103={}, MT900={}, MT910={}",odgovor.getMT103(), odgovor.getMT900(), odgovor.getMT910());
			}
		}
		logger.error("Invalid NalogZaPlacanje, duplicate entry idPoruke Obj={}", nalog.getIdPoruke());
	}

	private void regulisiClearing(NalogZaPlacanje nalog) {
		if(nalogZaPlacanjeRepository.findByIdPoruke(nalog.getIdPoruke()) == null) {
			model.NalogZaPlacanje nalogEntity = mapper.NalogZaPlacanjeSoapToEntity(nalog);
			this.regulisiClearing(nalogEntity);
			this.upisiNalogZaPlacanje(nalogEntity);
			return;
		}
		logger.error("Invalid NalogZaPlacanje, duplicate entry idPoruke Obj={}", nalog.getIdPoruke());
	}
	
	public void regulisiClearing(model.NalogZaPlacanje nalogEntity) {
		nalogEntity.setNijeRegulisan(true);
		//nalogZaPlacanjeRepository.save(nalogEntity);
		
		// rezervacija sredstava na racunu klijenta(firme)
		String racunDuznika = nalogEntity.getRacunDuznika();
		Racun firmaDuznika = racunRepository.findByBrojRacuna(racunDuznika);
		if(firmaDuznika == null) {
			logger.error("FirmaRepository.findByBrojRacuna() returned null for Obj={}", racunDuznika);
		}
		
		double rezervisanNovacNovoStanje = firmaDuznika.getRezervisanNovac().doubleValue() 
				+ nalogEntity.getIznos().doubleValue();
		firmaDuznika.setRezervisanNovac(new BigDecimal(rezervisanNovacNovoStanje));
		double novoStanjeRacuna = firmaDuznika.getStanjeRacuna().doubleValue() 
				- nalogEntity.getIznos().doubleValue();
		firmaDuznika.setStanjeRacuna(new BigDecimal(novoStanjeRacuna));
		racunRepository.save(firmaDuznika);
		
		String bankaDuznikaNovogNaloga = nalogEntity.getRacunDuznika().substring(0, 3);
		String bankaPrimaocaNovogNaloga = nalogEntity.getRacunPrimaoca().substring(0, 3);

		// negerulisani nalozi sa istim bankama duznika i istim bankama primaoca
		List<model.NalogZaPlacanje> neregulisaniNalozi = nalogZaPlacanjeRepository.nadjiSveNeregulisane(true,
				bankaDuznikaNovogNaloga + "%", bankaPrimaocaNovogNaloga + "%");

		if (neregulisaniNalozi.size() < 3) {
			return;
		}
		
		// ukoliko ima 3 i vise naloga, radi kliring
		MT102 mt102 = kreirajMT102(neregulisaniNalozi);
		if(mt102 == null) {
			logger.error("Aborting regulisiClearing, got null for MT102");
			return;
		}
		MT102Response mt102Response = client.sendMT102(mt102);

		MT900 mt900Soap = mt102Response.getMT900();
		model.MT900 mt900Entity = mapper.mt900SoapToEntity(mt900Soap);
		mt900Repository.save(mt900Entity);

		MT910 mt910Saop = mt102Response.getMT910();
		model.MT910 mt910Entity = mapper.mt910SoapToEntity(mt910Saop);
		mt910Repository.save(mt910Entity);

		for (model.NalogZaPlacanje nalogZaRegulisanje : neregulisaniNalozi) {
			nalogZaRegulisanje.setNijeRegulisan(false);
			nalogZaPlacanjeRepository.save(nalogZaRegulisanje);
		}
		
		// dodavanje sredstava na racun banke duznika
		String oznakaBankeDuznika = racunDuznika.substring(0, 3);		
		Racun racunBankaeDuznika = bankaRepository.findByOznakaBanke(oznakaBankeDuznika).getObracunskiRacun();
		if(racunBankaeDuznika == null) {
			logger.error("BankaRepository.findByOznakaBanke returned null for Obj={}", oznakaBankeDuznika);
		}
		
		double novoStanjeBankeDuznika = racunBankaeDuznika.getStanjeRacuna().doubleValue()
				+ mt102.getZaglavljeMT102().getUkupanIznos().doubleValue();
		racunBankaeDuznika.setStanjeRacuna(new BigDecimal(novoStanjeBankeDuznika));
		racunRepository.save(racunBankaeDuznika);
		
		// skidanje sredstava sa racuna banke primaoca
		String racunPrimaoca = nalogEntity.getRacunPrimaoca();
		String oznakaBankePrimaoca = racunPrimaoca.substring(0, 3);
		Racun racunBankePrimaoca = bankaRepository.findByOznakaBanke(oznakaBankePrimaoca).getObracunskiRacun();
		if(racunBankePrimaoca == null) {
			logger.error("BankaRepository.findByOznakaBanke returned null for Obj={}", oznakaBankePrimaoca);
		}
		
		double novoStanjeBankePrimaoca = racunBankePrimaoca.getStanjeRacuna().doubleValue()
				- mt102.getZaglavljeMT102().getUkupanIznos().doubleValue();
		racunBankePrimaoca.setStanjeRacuna(new BigDecimal(novoStanjeBankePrimaoca));
		racunRepository.save(racunBankePrimaoca);
		
		// regulisanje stanja rezervisanog novca u okviru firmi za svako pojedinacno placanje
		List<PojedinacnoPlacanjeMT102> pojedinacnaPlacanja = mt102.getPojedinacnoPlacanjeMT102();
		for (PojedinacnoPlacanjeMT102 placanje : pojedinacnaPlacanja) {
			String rDuznika = placanje.getRacunDuznika();
			Racun fDuznika = racunRepository.findByBrojRacuna(rDuznika);
			if(fDuznika == null) {
				logger.error("FirmaRepository.findByBrojRacuna() returned null for Obj={}", rDuznika);
			}
			double rezervisanoStanje = fDuznika.getRezervisanNovac().doubleValue()
					- placanje.getIznos().doubleValue();
			fDuznika.setRezervisanNovac(new BigDecimal(rezervisanoStanje));
			racunRepository.save(fDuznika);
			
			String rPrimaoca = placanje.getRacunPoverioca();
			Racun fPrimaoca = racunRepository.findByBrojRacuna(rPrimaoca);
			if(fPrimaoca == null) {
				logger.error("FirmaRepository.findByBrojRacuna() returned null for Obj={}", rPrimaoca);
			}
			double stanjeRacuna = fPrimaoca.getStanjeRacuna().doubleValue()
					+ placanje.getIznos().doubleValue();
			fPrimaoca.setStanjeRacuna(new BigDecimal(stanjeRacuna));
			racunRepository.save(fPrimaoca);
		}
	}
	
	

	private MT103 kreirajMT103(NalogZaPlacanje nalog) {
		MT103 mt103 = new MT103();
		Random random = new Random();
		mt103.setIdPoruke(Integer.toString(random.nextInt(1000000)));
		String oznakaBankeDuznika = nalog.getRacunDuznika().substring(0, 3);
		Banka bankaDuznika = bankaRepository.findByOznakaBanke(oznakaBankeDuznika);
		if(bankaDuznika == null) {
			logger.error("BankaRepository.findByOznakaBanke returned null for Obj={}", oznakaBankeDuznika);
		}
		String oznakaBankePoverioca = nalog.getRacunPrimaoca().substring(0, 3);
		Banka bankaPoverioca = bankaRepository.findByOznakaBanke(oznakaBankePoverioca);
		if(bankaPoverioca == null) {
			logger.error("BankaRepository.findByOznakaBanke returned null for Obj={}", oznakaBankePoverioca);
		}
		if (bankaDuznika == null || bankaPoverioca == null) {
			logger.error("Aborting kreirajMT103");
			return null;
		}
		mt103.setSwiftKodBankeDuznika(bankaDuznika.getSwiftKodBanke());
		mt103.setObracunskiRacunBankeDuznika(bankaDuznika.getObracunskiRacun().getBrojRacuna());
		
		mt103.setSwiftKodBankePoverioca(bankaPoverioca.getSwiftKodBanke());
		mt103.setObracunskiRacunBankePoverioca(bankaPoverioca.getObracunskiRacun().getBrojRacuna());

		mt103.setDuznikNalogodavac(nalog.getDuznikNalogodavac());
		mt103.setSvrhaPlacanja(nalog.getSvrhaPlacanja());
		mt103.setPrimalacPoverilac(nalog.getPrimalacPoverilac());
		mt103.setDatumNaloga(nalog.getDatumNaloga());
		mt103.setDatumValute(nalog.getDatumValute());
		mt103.setRacunDuznika(nalog.getRacunDuznika());
		mt103.setModelZaduzenja(nalog.getModelZaduzenja());
		mt103.setPozivNaBrojZaduzenja(nalog.getPozivNaBrojZaduzenja());
		mt103.setRacunPoverioca(nalog.getRacunPrimaoca());
		mt103.setModelOdobrenja(nalog.getModelOdobrenja());
		mt103.setPozivNaBrojOdobrenja(nalog.getPozivNaBrojOdobrenja());
		mt103.setIznos(nalog.getIznos());
		mt103.setSifraValute(nalog.getOznakaValute());

		return mt103;
	}

	private MT102 kreirajMT102(List<model.NalogZaPlacanje> nalozi) {

		String oznakaBankeDuznika = nalozi.get(0).getRacunDuznika().substring(0, 3);
		String oznakaBankePoverioca = nalozi.get(0).getRacunPrimaoca().substring(0, 3);
		model.Banka bankaDuznika = bankaRepository.findByOznakaBanke(oznakaBankeDuznika);
		if(bankaDuznika == null) {
			logger.error("BankaRepository.findByOznakaBanke returned null for Obj={}", oznakaBankeDuznika);
		}
		model.Banka bankaPoverioca = bankaRepository.findByOznakaBanke(oznakaBankePoverioca);
		if(bankaPoverioca == null) {
			logger.error("BankaRepository.findByOznakaBanke returned null for Obj={}", oznakaBankePoverioca);
		}
		if (bankaDuznika == null || bankaPoverioca == null) {
			logger.error("Aborting kreirajMT102");
			return null;
		}

		MT102 mt102 = new MT102();
		// zaglavlje naloga
		ZaglavljeMT102 zaglavlje = new ZaglavljeMT102();
		zaglavlje.setIdPoruke(UUID.randomUUID().toString());

		zaglavlje.setSwiftKodBankeDuznika(bankaDuznika.getSwiftKodBanke());
		zaglavlje.setObracunskiRacunBankeDuznika(bankaDuznika.getObracunskiRacun().getBrojRacuna());

		zaglavlje.setSwiftKodBankePoverioca(bankaPoverioca.getSwiftKodBanke());
		zaglavlje.setObracunskiRacunBankePoverioca(bankaPoverioca.getObracunskiRacun().getBrojRacuna());

		// racunanje iznosa
		double ukupanIznos = 0;
		for (model.NalogZaPlacanje nalog : nalozi) {
			ukupanIznos += nalog.getIznos().doubleValue();
		}
		zaglavlje.setUkupanIznos(new BigDecimal(ukupanIznos));
		zaglavlje.setSifraValute(nalozi.get(0).getOznakaValute());

		try {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(nalozi.get(0).getDatumValute());
			zaglavlje.setDatumValute(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
			calendar.setTime(nalozi.get(0).getDatumNaloga());
			zaglavlje.setDatum(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		mt102.setZaglavljeMT102(zaglavlje);

		// pojedinacna placanja
		List<PojedinacnoPlacanjeMT102> pojedinacnaPlacanja = mt102.getPojedinacnoPlacanjeMT102();
		for (model.NalogZaPlacanje nalog : nalozi) {
			PojedinacnoPlacanjeMT102 placanje = new PojedinacnoPlacanjeMT102();
			placanje.setIdNalogaZaPlacanje(nalog.getIdPoruke());

			placanje.setDuznikNalogodavac(nalog.getDuznikNalogodavac());
			placanje.setSvrhaPlacanja(nalog.getSvrhaPlacanja());
			placanje.setPrimalacPoverilac(nalog.getPrimalacPoverilac());
			placanje.setDatumNaloga(nalog.getDatumNaloga().toString());

			placanje.setRacunDuznika(nalog.getRacunDuznika());
			placanje.setModelZaduzenja(nalog.getModelZaduzenja());
			placanje.setPozivNaBrojZaduzenja(nalog.getPozivNaBrojZaduzenja());

			placanje.setRacunPoverioca(nalog.getRacunPrimaoca());
			placanje.setModelOdobrenja(nalog.getModelOdobrenja());
			placanje.setPozivNaBrojOdobrenja(nalog.getPozivNaBrojOdobrenja());

			placanje.setIznos(nalog.getIznos());
			placanje.setSifraValute(nalog.getOznakaValute());

			pojedinacnaPlacanja.add(placanje);

		}
		return mt102;
	}
	
	private void upisiNalogZaPlacanje(model.NalogZaPlacanje nalog) {
		
		DnevnoStanjeRacuna dnevnoStanjeRacunaDuznika = null;
		try {
			dnevnoStanjeRacunaDuznika = dnevnoStanjeRepository.nadjiPoDatumuIRacunu(nalog.getDatumNaloga(), nalog.getRacunDuznika());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if( dnevnoStanjeRacunaDuznika == null) {
			
			dnevnoStanjeRacunaDuznika = new DnevnoStanjeRacuna(nalog.getDatumNaloga(), racunRepository.findByBrojRacuna(nalog.getRacunDuznika()));
		}
		dnevnoStanjeRacunaDuznika.setPrethodnoStanje(dnevnoStanjeRacunaDuznika.getNovoStanje());
		dnevnoStanjeRacunaDuznika.setPrometNaTeret(dnevnoStanjeRacunaDuznika.getPrometNaTeret().add(nalog.getIznos()));
		dnevnoStanjeRacunaDuznika.setNovoStanje(dnevnoStanjeRacunaDuznika.getNovoStanje().subtract(nalog.getIznos()));
		
		DnevnoStanjeRacuna dnevnoStanjeRacunaPrimaoca = dnevnoStanjeRepository.nadjiPoDatumuIRacunu(nalog.getDatumNaloga(), nalog.getRacunPrimaoca());
		if( dnevnoStanjeRacunaPrimaoca == null) {
			dnevnoStanjeRacunaPrimaoca = new DnevnoStanjeRacuna(nalog.getDatumNaloga(), racunRepository.findByBrojRacuna(nalog.getRacunPrimaoca()));
		}
		dnevnoStanjeRacunaPrimaoca.setPrethodnoStanje(dnevnoStanjeRacunaPrimaoca.getNovoStanje());
		dnevnoStanjeRacunaPrimaoca.setPrometUKorist(dnevnoStanjeRacunaPrimaoca.getPrometUKorist().add(nalog.getIznos()));
		dnevnoStanjeRacunaPrimaoca.setNovoStanje(dnevnoStanjeRacunaPrimaoca.getNovoStanje().add(nalog.getIznos()));
		
		//Veze naloga za placanje sa dnevnim stanjima racuna duznika i primaoca
		List<DnevnoStanjeRacuna> dnevnaStanjaNaloga = nalog.getDnevnoStanje();
		dnevnaStanjaNaloga.add(dnevnoStanjeRacunaPrimaoca);
		dnevnaStanjaNaloga.add(dnevnoStanjeRacunaDuznika);
		nalog.setDnevnoStanje(dnevnaStanjaNaloga);	
		//nalogZaPlacanjeRepository.save(nalog);     //treba kad je nov, ne treba kad vec ima
		
		//Upisivanje naloga u dnevna stanja duznika i primaoca
		List<model.NalogZaPlacanje> analitikaIzvodaPrimaoca = dnevnoStanjeRacunaPrimaoca.getAnalitikaIzvoda();
		analitikaIzvodaPrimaoca.add(nalog);
		dnevnoStanjeRacunaPrimaoca.setAnalitikaIzvoda(analitikaIzvodaPrimaoca);
		dnevnoStanjeRepository.save(dnevnoStanjeRacunaPrimaoca);
		
		List<model.NalogZaPlacanje> analitikaIzvodaDuznika = dnevnoStanjeRacunaDuznika.getAnalitikaIzvoda();
		analitikaIzvodaDuznika.add(nalog);
		dnevnoStanjeRacunaDuznika.setAnalitikaIzvoda(analitikaIzvodaDuznika);
		//dnevnoStanjeRepository.save(dnevnoStanjeRacunaDuznika);
		
		
	}

	private void rezervisiNovac(Racun racunFirme, BigDecimal iznos) {
		double rezervisanNovac = racunFirme.getRezervisanNovac().doubleValue() + iznos.doubleValue();
		racunFirme.setRezervisanNovac(new BigDecimal(rezervisanNovac));
		double novoStanjeRacuna = racunFirme.getStanjeRacuna().doubleValue() - iznos.doubleValue();
		racunFirme.setStanjeRacuna(new BigDecimal(novoStanjeRacuna));
	}

	public Presek regulisiZahtevZaIzvod(ZahtevZaIzvod zahtev) {
		try {
			zahtevZaIzvodRepository.save(mapper.zahtevZaIzvodSoapToEntity(zahtev));
			List<model.NalogZaPlacanje> naloziZaDatogKlijenta = nalogZaPlacanjeRepository.nadjiPoRacunuDuznikaIliPrimaoca(zahtev.getBrojRacuna());
			List<model.NalogZaPlacanje> naloziZaDatum = new ArrayList<>();
			for(model.NalogZaPlacanje nalog : naloziZaDatogKlijenta) {
				if(nalog.getDatumNaloga().compareTo(zahtev.getDatum().toGregorianCalendar().getTime()) == 0) {
					naloziZaDatum.add(nalog);
				}
			}
			List<model.NalogZaPlacanje> trazeniNalozi = new ArrayList<model.NalogZaPlacanje>();
			for (int i = (zahtev.getRedniBrojPreseka() - 1) * 3; i < zahtev.getRedniBrojPreseka() * 3; i++) {
				if (i >= naloziZaDatum.size())
					break;
				trazeniNalozi.add(naloziZaDatum.get(i));
			}
			if (trazeniNalozi.size() == 0)
				return null;
			int brojPromenaUKorist = 0;
			double ukupnoUKorist = 0;
			int brojPromenaNaTeret = 0;
			double ukupnoNaTeret = 0;
			List<StavkaPreseka> stavkePreseka = new ArrayList<StavkaPreseka>();
			for (model.NalogZaPlacanje nalog : trazeniNalozi) {
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
				if (nalog.getRacunDuznika().equals(zahtev.getBrojRacuna())) {
					stavka.setSmer("o");
					brojPromenaNaTeret++;
					ukupnoNaTeret += nalog.getIznos().doubleValue();
				} else {
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
			
			// treba novo stanje i prethodno stanje
			int poslednjiNalog = stavkePreseka.size() - 1;
			List<model.NalogZaPlacanje> naloziPoslePreseka = nalogZaPlacanjeRepository.nadjiSvePosleDatogNaloga(trazeniNalozi.get(poslednjiNalog).getId(), zahtev.getBrojRacuna());
			double sumaPoslePreseka = 0;
			for(model.NalogZaPlacanje n : naloziPoslePreseka) {
				if(n.getDuznikNalogodavac().equals(zahtev.getBrojRacuna())) {
					sumaPoslePreseka -= n.getIznos().doubleValue();
				}else {
					sumaPoslePreseka += n.getIznos().doubleValue();
				}
			}
			String brojRacuna = zahtev.getBrojRacuna();
			Racun firma = racunRepository.findByBrojRacuna(brojRacuna);
			if(firma == null) {
				logger.error("FirmaRepository.findByBrojRacuna() returned null for Obj={}", brojRacuna);
			}
			zaglavlje.setNovoStanje(new BigDecimal(firma.getStanjeRacuna().doubleValue() - sumaPoslePreseka));
			zaglavlje.setPrethodnoStanje(new BigDecimal(zaglavlje.getNovoStanje().doubleValue() - zaglavlje.getUkupnoUKorist().doubleValue() + zaglavlje.getUkupnoNaTeret().doubleValue()));
			
			Presek presek = new Presek();
			presek.setZaglavlje(zaglavlje);
			List<StavkaPreseka> sp = presek.getStavkaPreseka();
			sp.addAll(stavkePreseka);
			return presek;
		} catch (Exception e) {
			return null;
		}
	}
}
