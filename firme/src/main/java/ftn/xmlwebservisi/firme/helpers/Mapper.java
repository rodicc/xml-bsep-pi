package ftn.xmlwebservisi.firme.helpers;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.stereotype.Service;

import soap.NalogZaPlacanje;
import soap.ZahtevZaIzvod;

public class Mapper {

	public NalogZaPlacanje nalogZaPlacanjeEntityToSoap(ftn.xmlwebservisi.firme.model.NalogZaPlacanje nalogEntity) {
		NalogZaPlacanje nalogSoap = new NalogZaPlacanje();
		nalogSoap.setIdPoruke(nalogEntity.getIdPoruke());
		nalogSoap.setDuznikNalogodavac(nalogEntity.getDuznikNalogodavac());
		nalogSoap.setSvrhaPlacanja(nalogEntity.getSvrhaPlacanja());
		nalogSoap.setPrimalacPoverilac(nalogEntity.getPrimalacPoverilac());
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
}
