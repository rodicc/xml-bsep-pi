package ftn.xmlwebservisi.firme.helpers;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.springframework.stereotype.Service;

import soap.NalogZaPlacanje;

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
		
		nalogSoap.setRacunPrimaoca(nalogEntity.getRacunPoverioca());
		nalogSoap.setModelOdobrenja(nalogEntity.getModelOdobrenja());
		nalogSoap.setPozivNaBrojOdobrenja(nalogEntity.getPozivNaBrojOdobrenja());
		
		nalogSoap.setIznos(nalogEntity.getIznos());
		nalogSoap.setOznakaValute(nalogEntity.getOznakaValute());
		nalogSoap.setHitno(nalogEntity.getHitno());
		
		return nalogSoap;
	}

}
