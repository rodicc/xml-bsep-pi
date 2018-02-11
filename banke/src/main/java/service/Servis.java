package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import xml.ftn.banke.NalogZaPlacanje;
import xml.ftn.banke.Presek;
import xml.ftn.banke.StavkaPreseka;
import xml.ftn.banke.Zaglavlje;
import xml.ftn.banke.ZahtevZaIzvod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.banke.SoapClient;
import helpers.Mapper;
import model.Banka;
import model.Firma;
import repository.BankaRepository;
import repository.FirmaRepository;
import repository.MT900Repository;
import repository.MT910Repository;
import repository.NalogZaPlacanjeRepository;
import repository.ZahtevZaIzvodRepository;
import soap.MT102;
import soap.MT102Response;
import soap.MT103;
import soap.MT103Response;
import soap.PojedinacnoPlacanjeMT102;
import soap.ZaglavljeMT102;

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
	private FirmaRepository firmaRepository;
	@Autowired
	private BankaRepository bankaRepository;
	@Autowired
	private MT900Repository mt900Repository;
	@Autowired
	private MT910Repository mt910Repository;

	public void regulisiNalogZaPlacanje(NalogZaPlacanje nalog) {
		model.NalogZaPlacanje nalogZaPlacanje = mapper.NalogZaPlacanjeSoapToEntity(nalog);
		if(nalog.isHitno() || nalog.getIznos().doubleValue() >= 250000) {
			regulisiRTGS(nalog);
		}else {
			nalogZaPlacanje.setNijeRegulisan(true);
			regulisiClearing(nalog);
		}
		nalogZaPlacanjeRepository.save(nalogZaPlacanje);		
	}
	
	private void regulisiRTGS(NalogZaPlacanje nalog) {
		MT103 mt103 = kreirajMT103(nalog);
		Firma firmaKojaPlaca = firmaRepository.findByBrojRacuna(nalog.getRacunDuznika());
		rezervisiNovac(firmaKojaPlaca, nalog.getIznos());
		firmaRepository.save(firmaKojaPlaca);
		MT103Response odgovor = client.sendMT103(mt103);
		if(odgovor.getMT103() != null && odgovor.getMT900() != null && odgovor.getMT910() != null) {
			mt900Repository.save(mapper.mt900SoapToEntity(odgovor.getMT900()));
			mt910Repository.save(mapper.mt910SoapToEntity(odgovor.getMT910()));
			double iznosNaloga = nalog.getIznos().doubleValue();
			firmaKojaPlaca.setRezervisanNovac(new BigDecimal(firmaKojaPlaca.getRezervisanNovac().doubleValue() - iznosNaloga));
			firmaRepository.save(firmaKojaPlaca);
			Firma firmaKojojSePlaca = firmaRepository.findByBrojRacuna(nalog.getRacunPrimaoca());
			firmaKojojSePlaca.setStanjeRacuna(new BigDecimal(firmaKojojSePlaca.getStanjeRacuna().doubleValue() + iznosNaloga));
			firmaRepository.save(firmaKojojSePlaca);
			Banka bankaDuznika = bankaRepository.findBySwiftKodBanke(odgovor.getMT900().getSwiftKodBankeDuznika());
			bankaDuznika.setStanjeRacuna(new BigDecimal(bankaDuznika.getStanjeRacuna().doubleValue() + iznosNaloga));
			bankaRepository.save(bankaDuznika);
			Banka bankaPoverioca = bankaRepository.findBySwiftKodBanke(odgovor.getMT910().getSwiftKodBankePoverioca());
			bankaPoverioca.setStanjeRacuna(new BigDecimal(bankaPoverioca.getStanjeRacuna().doubleValue() - iznosNaloga));
			bankaRepository.save(bankaPoverioca);
		}
	}
	
	private void regulisiClearing(NalogZaPlacanje nalog) {
		model.NalogZaPlacanje nalogEntity = mapper.NalogZaPlacanjeSoapToEntity(nalog);
		String bankaDuznikaEntity = nalogEntity.getRacunDuznika().substring(0, 3);
		String bankaPrimaocaEntity = nalogEntity.getRacunPrimaoca().substring(0, 3);
		
		// svi neregulisani nalozi
		List<model.NalogZaPlacanje> neregulisaniNalozi = nalogZaPlacanjeRepository.findByNijeRegulisan(true);
		// neregulisani nalozi sa istim bankama poverioca, i bankama primaoca
		List<model.NalogZaPlacanje> naloziZaRegulisanje = new ArrayList<>();
		naloziZaRegulisanje.add(nalogEntity);
		
		// da li ima vise od 3 neregulisana naloga
		// gde su iste banke poverioca i banke primaoca
		if (neregulisaniNalozi.size() >= 2) {
			for (model.NalogZaPlacanje nalogZaPlacanje : neregulisaniNalozi) {
				String bankaDuznika = nalogZaPlacanje.getRacunDuznika().substring(0, 3);
				String bankaPrimaoca = nalogZaPlacanje.getRacunPrimaoca().substring(0, 3);
				
				if (bankaDuznikaEntity.equals(bankaDuznika) && bankaPrimaocaEntity.equals(bankaPrimaoca)) {
					naloziZaRegulisanje.add(nalogZaPlacanje);
				}
			}
			// ukoliko ima 3 i vise naloga, radi kliring
			if (naloziZaRegulisanje.size() >= 3) {
				MT102 mt102 = kreirajMT102(naloziZaRegulisanje);
				MT102Response odgovor = client.sendMT102(mt102);
			}
		}
		
	}
	
	private MT103 kreirajMT103(NalogZaPlacanje nalog) {
		MT103 mt103 = new MT103();
		Random random = new Random(1000000);
		mt103.setIdPoruke(random.toString());
		Banka bankaDuznika = bankaRepository.findByOznakaBanke(nalog.getRacunDuznika().substring(0, 3));
		mt103.setSwiftKodBankeDuznika(bankaDuznika.getSwiftKodBanke());
		mt103.setObracunskiRacunBankeDuznika(bankaDuznika.getObracunskiRacun());
		Banka bankaPoverioca = bankaRepository.findByOznakaBanke(nalog.getRacunPrimaoca().substring(0, 3));
		mt103.setSwiftKodBankePoverioca(bankaPoverioca.getSwiftKodBanke());
		mt103.setObracunskiRacunBankePoverioca(bankaPoverioca.getObracunskiRacun());
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
		model.Banka bankaPoverioca = bankaRepository.findByOznakaBanke(oznakaBankePoverioca);
		if (bankaDuznika == null || bankaPoverioca == null) {
			System.out.println("nisam pronasao banke");
			return null;
		}

		MT102 mt102 = new MT102();
		//zaglavlje naloga
		ZaglavljeMT102 zaglavlje = new ZaglavljeMT102();
		Random random = new Random(1000000);
		zaglavlje.setIdPoruke(random.toString());
		
		zaglavlje.setSwiftKodBankeDuznika(bankaDuznika.getSwiftKodBanke());
		zaglavlje.setObracunskiRacunBankeDuznika(bankaDuznika.getObracunskiRacun());
		
		zaglavlje.setSwiftKodBankePoverioca(bankaPoverioca.getSwiftKodBanke());
		zaglavlje.setObracunskiRacunBankePoverioca(bankaPoverioca.getObracunskiRacun());
		
		// racunanje iznosa
		double iznos = 0;
		for (model.NalogZaPlacanje nalog : nalozi) {
			iznos += nalog.getIznos().doubleValue();
		}
		BigDecimal iznosBigDecimal = new BigDecimal(iznos);
		zaglavlje.setUkupanIznos(iznosBigDecimal);
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
		return null;
	}
	
	private void rezervisiNovac(Firma firma, BigDecimal iznos) {
		double rezervisanNovac = firma.getRezervisanNovac().doubleValue() + iznos.doubleValue();
		firma.setRezervisanNovac(new BigDecimal(rezervisanNovac));
		double novoStanjeRacuna = firma.getStanjeRacuna().doubleValue() - iznos.doubleValue();
		firma.setStanjeRacuna(new BigDecimal(novoStanjeRacuna));
	}
	
	
	public Presek regulisiZahtevZaIzvod(ZahtevZaIzvod zahtev) {
		try {
			zahtevZaIzvodRepository.save(mapper.zahtevZaIzvodSoapToEntity(zahtev));
			List<model.NalogZaPlacanje> naloziZaDatum = nalogZaPlacanjeRepository.nadjiPoDatumuIBrojuRacuna(
					zahtev.getDatum().toGregorianCalendar().getTime(), zahtev.getBrojRacuna());
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
				if (nalog.getRacunDuznika() == zahtev.getBrojRacuna()) {
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
