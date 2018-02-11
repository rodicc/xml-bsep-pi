package helpers;

import org.springframework.stereotype.Service;

import model.MT900;
import model.MT910;
import model.NalogZaPlacanje;
import model.ZahtevZaIzvod;

@Service
public class Mapper {

	public NalogZaPlacanje NalogZaPlacanjeSoapToEntity(xml.ftn.banke.NalogZaPlacanje nalog) {
		NalogZaPlacanje result = new NalogZaPlacanje();
		result.setIdPoruke(nalog.getIdPoruke());
		result.setDuznikNalogodavac(nalog.getDuznikNalogodavac());
		result.setSvrhaPlacanja(nalog.getSvrhaPlacanja());
		result.setPrimalacPoverilac(nalog.getPrimalacPoverilac());
		result.setDatumNaloga(nalog.getDatumNaloga().toGregorianCalendar().getTime());
		result.setDatumValute(nalog.getDatumValute().toGregorianCalendar().getTime());
		result.setRacunDuznika(nalog.getRacunDuznika());
		result.setModelZaduzenja(nalog.getModelZaduzenja());
		result.setPozivNaBrojZaduzenja(nalog.getPozivNaBrojZaduzenja());
		result.setRacunPrimaoca(nalog.getRacunPrimaoca());
		result.setModelOdobrenja(nalog.getModelOdobrenja());
		result.setPozivNaBrojOdobrenja(nalog.getPozivNaBrojOdobrenja());
		result.setIznos(nalog.getIznos());
		result.setOznakaValute(nalog.getOznakaValute());
		result.setHitno(nalog.isHitno());
		return result;
	}
	
	public ZahtevZaIzvod zahtevZaIzvodSoapToEntity(xml.ftn.banke.ZahtevZaIzvod zahtev) {
		ZahtevZaIzvod result = new ZahtevZaIzvod();
		result.setBrojRacuna(zahtev.getBrojRacuna());
		result.setDatum(zahtev.getDatum().toGregorianCalendar().getTime());
		result.setRedniBrojPreseka(zahtev.getRedniBrojPreseka());
		return result;
	}

	public MT900 mt900SoapToEntity(soap.MT900 mt900) {
		MT900 result = new MT900();
		result.setIdPoruke(mt900.getIdPoruke());
		result.setSwiftKodBankeDuznika(mt900.getSwiftKodBankeDuznika());
		result.setObracunskiRacunBankeDuznika(mt900.getObracunskiRacunBankeDuznika());
		result.setIdPorukeNaloga(mt900.getIdPorukeNaloga());
		result.setDatumValute(mt900.getDatumValute().toGregorianCalendar().getTime());
		result.setIznos(mt900.getIznos());
		result.setSifraValute(mt900.getSifraValute());
		return result;
	}
	
	public MT910 mt910SoapToEntity(soap.MT910 mt910) {
		MT910 result = new MT910();
		result.setIdPoruke(mt910.getIdPoruke());
		result.setSwiftKodBankePoverioca(mt910.getSwiftKodBankePoverioca());
		result.setObracunskiRacunBankePoverioca(mt910.getObracunskiRacunBankePoverioca());
		result.setIdPorukeNaloga(mt910.getIdPorukeNaloga());
		result.setDatumValute(mt910.getDatumValute().toGregorianCalendar().getTime());
		result.setIznos(mt910.getIznos());
		result.setSifraValute(mt910.getSifraValute());
		return result;
	}
}
