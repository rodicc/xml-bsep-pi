package ftn.xmlwebservisi.firme.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class NalogZaPlacanje {
	
	@Id
	private int id;
	private String idPoruke;
	private String duznikNalogodavac;
	private String svrhaPlacanja;
	private String primalacPoverilac;
	private LocalDate datumNaloga;
	private LocalDate datumValute;
	private String racunDuznika;
	private int modelZaduzenja;
	private String pozivNaBrojZaduzenja;
	private String racunPoverioca;
	private int modelOdobrenja;
	private String pozivNaBrojOdobrenja;
	private double iznos;
	private String oznakaValute;
	private Boolean hitno;
	
	public NalogZaPlacanje(){
		
	}

	
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getIdPoruke() {
		return idPoruke;
	}

	public void setIdPoruke(String idPoruke) {
		this.idPoruke = idPoruke;
	}

	public String getDuznikNalogodavac() {
		return duznikNalogodavac;
	}

	public void setDuznikNalogodavac(String duznikNalogodavac) {
		this.duznikNalogodavac = duznikNalogodavac;
	}

	public String getSvrhaPlacanja() {
		return svrhaPlacanja;
	}

	public void setSvrhaPlacanja(String svrhaPlacanja) {
		this.svrhaPlacanja = svrhaPlacanja;
	}

	public String getPrimalacPoverilac() {
		return primalacPoverilac;
	}

	public void setPrimalacPoverilac(String primalacPoverilac) {
		this.primalacPoverilac = primalacPoverilac;
	}

	public LocalDate getDatumNaloga() {
		return datumNaloga;
	}

	public void setDatumNaloga(LocalDate datumNaloga) {
		this.datumNaloga = datumNaloga;
	}

	public LocalDate getDatumValute() {
		return datumValute;
	}

	public void setDatumValute(LocalDate datumValute) {
		this.datumValute = datumValute;
	}

	public String getRacunDuznika() {
		return racunDuznika;
	}

	public void setRacunDuznika(String racunDuznika) {
		this.racunDuznika = racunDuznika;
	}

	public int getModelZaduzenja() {
		return modelZaduzenja;
	}

	public void setModelZaduzenja(int modelZaduzenja) {
		this.modelZaduzenja = modelZaduzenja;
	}

	public String getPozivNaBrojZaduzenja() {
		return pozivNaBrojZaduzenja;
	}

	public void setPozivNaBrojZaduzenja(String pozivNaBrojZaduzenja) {
		this.pozivNaBrojZaduzenja = pozivNaBrojZaduzenja;
	}

	public String getRacunPoverioca() {
		return racunPoverioca;
	}

	public void setRacunPoverioca(String racunPoverioca) {
		this.racunPoverioca = racunPoverioca;
	}

	public int getModelOdobrenja() {
		return modelOdobrenja;
	}

	public void setModelOdobrenja(int modelOdobrenja) {
		this.modelOdobrenja = modelOdobrenja;
	}

	public String getPozivNaBrojOdobrenja() {
		return pozivNaBrojOdobrenja;
	}

	public void setPozivNaBrojOdobrenja(String pozivNaBrojOdobrenja) {
		this.pozivNaBrojOdobrenja = pozivNaBrojOdobrenja;
	}

	public double getIznos() {
		return iznos;
	}

	public void setIznos(double iznos) {
		this.iznos = iznos;
	}

	public String getOznakaValute() {
		return oznakaValute;
	}

	public void setOznakaValute(String oznakaValute) {
		this.oznakaValute = oznakaValute;
	}

	public Boolean getHitno() {
		return hitno;
	}

	public void setHitno(Boolean hitno) {
		this.hitno = hitno;
}
}
