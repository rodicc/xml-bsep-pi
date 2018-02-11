package service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

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
		
		// centralna banka prebacuje sredstva izmedju banaka
		MT103Response odgovor = client.sendMT103(mt103);
		if(odgovor.getMT103() != null && odgovor.getMT900() != null && odgovor.getMT910() != null) {
			mt900Repository.save(mapper.mt900SoapToEntity(odgovor.getMT900()));
			mt910Repository.save(mapper.mt910SoapToEntity(odgovor.getMT910()));
			
			double iznosNaloga = nalog.getIznos().doubleValue();
			// skidanje rezervisanog novca
			firmaKojaPlaca.setRezervisanNovac(new BigDecimal(firmaKojaPlaca.getRezervisanNovac().doubleValue() - iznosNaloga));
			firmaRepository.save(firmaKojaPlaca);
			
			// dodavanje iznosa firmi kojoj se placa
			Firma firmaKojojSePlaca = firmaRepository.findByBrojRacuna(nalog.getRacunPrimaoca());
			firmaKojojSePlaca.setStanjeRacuna(new BigDecimal(firmaKojojSePlaca.getStanjeRacuna().doubleValue() + iznosNaloga));
			firmaRepository.save(firmaKojojSePlaca);
			
			// banka prebacuje sredstva od klijenta sebi
			Banka bankaDuznika = bankaRepository.findBySwiftKodBanke(odgovor.getMT900().getSwiftKodBankeDuznika());
			bankaDuznika.setStanjeRacuna(new BigDecimal(bankaDuznika.getStanjeRacuna().doubleValue() + iznosNaloga));
			bankaRepository.save(bankaDuznika);
			
			// banka prebacuje na racun klijenta
			Banka bankaPoverioca = bankaRepository.findBySwiftKodBanke(odgovor.getMT910().getSwiftKodBankePoverioca());
			bankaPoverioca.setStanjeRacuna(new BigDecimal(bankaPoverioca.getStanjeRacuna().doubleValue() - iznosNaloga));
			bankaRepository.save(bankaPoverioca);
		}
	}
	
	private void regulisiClearing(NalogZaPlacanje nalog) {
		MT102 mt102 = kreirajMT102(nalog);
		MT102Response odgovor = client.sendMT102(mt102);
	}
	
	private MT103 kreirajMT103(NalogZaPlacanje nalog) {
		MT103 mt103 = new MT103();
		Random random = new Random();
		mt103.setIdPoruke(Integer.toString(random.nextInt(1000000)));
		
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
	
	private MT102 kreirajMT102(NalogZaPlacanje nalog) {
		MT102 mt102 = new MT102();
		return mt102;
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
