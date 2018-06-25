package helpers;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;

import org.springframework.stereotype.Service;

import model.MT102;
import model.MT103;
import model.ZaglavljeMT102;
import xml.ftn.centralnabanka.PojedinacnoPlacanjeMT102;

@Service
public class Mapper {

	public xml.ftn.centralnabanka.MT103 MT103EntityToSoap(MT103 mt103) {
		try {
			xml.ftn.centralnabanka.MT103 result = new xml.ftn.centralnabanka.MT103();
			result.setIdPoruke(mt103.getIdPoruke());
			result.setSwiftKodBankeDuznika(mt103.getSwiftKodBankeDuznika());
			result.setObracunskiRacunBankeDuznika(mt103.getObracunskiRacunBankeDuznika());
			result.setSwiftKodBankePoverioca(mt103.getSwiftKodBankePoverioca());
			result.setObracunskiRacunBankePoverioca(mt103.getObracunskiRacunBankePoverioca());
			result.setDuznikNalogodavac(mt103.getDuznikNalogodavac());
			result.setSvrhaPlacanja(mt103.getSvrhaPlacanja());
			result.setPrimalacPoverilac(mt103.getPrimalacPoverilac());
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(mt103.getDatumNaloga());
			result.setDatumNaloga(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
			calendar.setTime(mt103.getDatumValute());
			result.setDatumValute(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));		
			result.setRacunDuznika(mt103.getRacunDuznika());
			result.setModelZaduzenja(mt103.getModelZaduzenja());
			result.setPozivNaBrojZaduzenja(mt103.getPozivNaBrojZaduzenja());
			result.setRacunPoverioca(mt103.getRacunPoverioca());
			result.setModelOdobrenja(mt103.getModelOdobrenja());
			result.setPozivNaBrojOdobrenja(mt103.getPozivNaBrojOdobrenja());
			result.setIznos(mt103.getIznos());
			result.setSifraValute(mt103.getSifraValute());
			return result;
		} catch(Exception e) {
			return null;
		}	
	}
	
	public MT103 MT103SoapToEntity(xml.ftn.centralnabanka.MT103 mt103) {
		MT103 result = new MT103();
		result.setIdPoruke(mt103.getIdPoruke());
		result.setSwiftKodBankeDuznika(mt103.getSwiftKodBankeDuznika());
		result.setObracunskiRacunBankeDuznika(mt103.getObracunskiRacunBankeDuznika());
		result.setSwiftKodBankePoverioca(mt103.getSwiftKodBankePoverioca());
		result.setObracunskiRacunBankePoverioca(mt103.getObracunskiRacunBankePoverioca());
		result.setDuznikNalogodavac(mt103.getDuznikNalogodavac());
		result.setSvrhaPlacanja(mt103.getSvrhaPlacanja());
		result.setPrimalacPoverilac(mt103.getPrimalacPoverilac());
		result.setDatumNaloga(mt103.getDatumNaloga().toGregorianCalendar().getTime());
		result.setDatumValute(mt103.getDatumValute().toGregorianCalendar().getTime());		
		result.setRacunDuznika(mt103.getRacunDuznika());
		result.setModelZaduzenja(mt103.getModelZaduzenja());
		result.setPozivNaBrojZaduzenja(mt103.getPozivNaBrojZaduzenja());
		result.setRacunPoverioca(mt103.getRacunPoverioca());
		result.setModelOdobrenja(mt103.getModelOdobrenja());
		result.setPozivNaBrojOdobrenja(mt103.getPozivNaBrojOdobrenja());
		result.setIznos(mt103.getIznos());
		result.setSifraValute(mt103.getSifraValute());
		return result;
	}

	public MT102 MT102SoapToEntity(xml.ftn.centralnabanka.MT102 mt102) {
		MT102 result = new MT102();
		ZaglavljeMT102 zaglavlje = new ZaglavljeMT102();
		zaglavlje.setIdPoruke(mt102.getZaglavljeMT102().getIdPoruke());
		zaglavlje.setSwiftKodBankeDuznika(mt102.getZaglavljeMT102().getSwiftKodBankeDuznika());
		zaglavlje.setObracunskiRacunBankeDuznika(mt102.getZaglavljeMT102().getObracunskiRacunBankeDuznika());
		zaglavlje.setSwiftKodBankePoverioca(mt102.getZaglavljeMT102().getSwiftKodBankePoverioca());
		zaglavlje.setObracunskiRacunBankePoverioca(mt102.getZaglavljeMT102().getObracunskiRacunBankePoverioca());
		zaglavlje.setUkupanIznos(mt102.getZaglavljeMT102().getUkupanIznos());
		zaglavlje.setSifraValute(mt102.getZaglavljeMT102().getSifraValute());
		zaglavlje.setDatumValute(mt102.getZaglavljeMT102().getDatumValute().toGregorianCalendar().getTime());
		zaglavlje.setDatum(mt102.getZaglavljeMT102().getDatum().toGregorianCalendar().getTime());
		result.setZaglavljeMT102(zaglavlje);
		List<model.PojedinacnoPlacanjeMT102> placanja = new ArrayList<model.PojedinacnoPlacanjeMT102>();
		for(PojedinacnoPlacanjeMT102 pojedinacnoSoap : mt102.getPojedinacnoPlacanjeMT102()) {
			model.PojedinacnoPlacanjeMT102 pojedinacno = new model.PojedinacnoPlacanjeMT102();
			pojedinacno.setIdNalogaZaPlacanje(pojedinacnoSoap.getIdNalogaZaPlacanje());
			pojedinacno.setDuznikNalogodavac(pojedinacnoSoap.getDuznikNalogodavac());
			pojedinacno.setSvrhaPlacanja(pojedinacnoSoap.getSvrhaPlacanja());
			pojedinacno.setPrimalacPoverilac(pojedinacnoSoap.getPrimalacPoverilac());
			pojedinacno.setDatumNaloga(pojedinacnoSoap.getDatumNaloga());
			pojedinacno.setRacunDuznika(pojedinacnoSoap.getRacunDuznika());
			pojedinacno.setModelZaduzenja(pojedinacnoSoap.getModelZaduzenja());
			pojedinacno.setPozivNaBrojZaduzenja(pojedinacnoSoap.getPozivNaBrojZaduzenja());
			pojedinacno.setRacunPoverioca(pojedinacnoSoap.getRacunPoverioca());
			pojedinacno.setModelOdobrenja(pojedinacnoSoap.getModelOdobrenja());
			pojedinacno.setPozivNaBrojOdobrenja(pojedinacnoSoap.getPozivNaBrojOdobrenja());
			pojedinacno.setIznos(pojedinacnoSoap.getIznos());
			pojedinacno.setSifraValute(pojedinacnoSoap.getSifraValute());
			placanja.add(pojedinacno);
		}
		result.setPojedinacnaPlacanja(placanja);
		return result;
	}
}
