package helpers;

import org.springframework.stereotype.Service;

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
}
