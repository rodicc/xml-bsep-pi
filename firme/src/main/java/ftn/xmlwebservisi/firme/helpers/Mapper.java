package ftn.xmlwebservisi.firme.helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.firme.model.Presek;
import ftn.xmlwebservisi.firme.model.StavkaFakture;
import soap.NalogZaPlacanje;
import soap.StavkaPreseka;
import soap.Zaglavlje;
import soap.ZahtevZaIzvod;

@Service
public class Mapper {

	public NalogZaPlacanje nalogZaPlacanjeEntityToSoap(ftn.xmlwebservisi.firme.model.NalogZaPlacanje nalogEntity) {
		NalogZaPlacanje nalogSoap = new NalogZaPlacanje();
		nalogSoap.setIdPoruke(nalogEntity.getIdPoruke());
		nalogSoap.setDuznikNalogodavac(nalogEntity.getDuznikNalogodavac());
		nalogSoap.setSvrhaPlacanja(nalogEntity.getSvrhaPlacanja());
		try {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(nalogEntity.getDatumNaloga());
			nalogSoap.setDatumNaloga(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
			calendar.setTime(nalogEntity.getDatumValute());
			nalogSoap.setDatumValute(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		nalogSoap.setRacunDuznika(nalogEntity.getRacunDuznika());
		nalogSoap.setModelZaduzenja(nalogEntity.getModelZaduzenja());
		nalogSoap.setPozivNaBrojZaduzenja(nalogEntity.getPozivNaBrojZaduzenja());
		
		nalogSoap.setPrimalacPoverilac(nalogEntity.getPrimalacPoverilac());
		nalogSoap.setRacunPrimaoca(nalogEntity.getRacunPoverioca());
		nalogSoap.setModelOdobrenja(nalogEntity.getModelOdobrenja());
		nalogSoap.setPozivNaBrojOdobrenja(nalogEntity.getPozivNaBrojOdobrenja());
		
		nalogSoap.setIznos(nalogEntity.getIznos());
		nalogSoap.setOznakaValute(nalogEntity.getOznakaValute());
		nalogSoap.setHitno(nalogEntity.getHitno());
		
		return nalogSoap;
	}

	public ZahtevZaIzvod zahtevZaIzvodEntityToSoap(ftn.xmlwebservisi.firme.model.ZahtevZaIzvod zahtev) {
		ZahtevZaIzvod result = new ZahtevZaIzvod();
		result.setBrojRacuna(zahtev.getBrojRacuna());
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(zahtev.getDatum());
		try {
			result.setDatum(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		result.setRedniBrojPreseka(zahtev.getRedniBrojPreseka());
		return result;
	}

	public Presek presekSoapToEntity(soap.Presek presek) {
		
		if(presek == null) return null;
		
		Presek result = new Presek();
		ArrayList<ftn.xmlwebservisi.firme.model.StavkaPreseka> stavke = new ArrayList<ftn.xmlwebservisi.firme.model.StavkaPreseka>();
		for(StavkaPreseka stavka : presek.getStavkaPreseka()) {
			stavke.add(stavkaPresekaSoapToEntity(stavka));
		}
		result.setStavkePreseka(stavke);
		result.setBrojRacuna(presek.getZaglavlje().getBrojRacuna());
		result.setDatumNaloga(presek.getZaglavlje().getDatumNaloga().toGregorianCalendar().getTime());
		result.setBrojPreseka(presek.getZaglavlje().getBrojPreseka());
		result.setPrethodnoStanje(presek.getZaglavlje().getPrethodnoStanje());
		result.setBrojPromenaUKorist(presek.getZaglavlje().getBrojPromenaUKorist());
		result.setUkupnoUKorist(presek.getZaglavlje().getUkupnoUKorist());
		result.setBrojPromenaNaTeret(presek.getZaglavlje().getBrojPromenaNaTeret());
		result.setUkupnoNaTeret(presek.getZaglavlje().getUkupnoNaTeret());
		result.setNovoStanje(presek.getZaglavlje().getNovoStanje());
		return result;
	}
	
	private ftn.xmlwebservisi.firme.model.StavkaPreseka stavkaPresekaSoapToEntity(StavkaPreseka stavka) {
		ftn.xmlwebservisi.firme.model.StavkaPreseka result = new ftn.xmlwebservisi.firme.model.StavkaPreseka();
		result.setDuznikNalogodavac(stavka.getDuznik());
		result.setSvrhaPlacanja(stavka.getSvrhaPlacanja());
		result.setPrimalacPoverilac(stavka.getPrimalac());
		result.setDatumNaloga(stavka.getDatumNaloga().toGregorianCalendar().getTime());
		result.setDatumValute(stavka.getDatumValute().toGregorianCalendar().getTime());
		result.setRacunDuznika(stavka.getRacunDuznika());
		result.setModelZaduzenja(stavka.getModelZaduzenja());
		result.setPozivNaBrojZaduzenja(stavka.getPozivNaBrojZaduzenja());
		result.setRacunPoverioca(stavka.getRacunPrimaoca());
		result.setModelOdobrenja(stavka.getModelOdobrenja());
		result.setPozivNaBrojOdobrenja(stavka.getPozivNaBrojOdobrenja());
		result.setIznos(stavka.getIznos());
		result.setSmer(stavka.getSmer().charAt(0));
		return result;
	}
}
