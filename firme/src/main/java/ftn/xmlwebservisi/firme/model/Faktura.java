package ftn.xmlwebservisi.firme.model;

import java.time.LocalDate;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Faktura {
	

	@Id
	private int id;
	private String idPoruke;
	private String nazivDobavljaca;
	private String adresaDobavljaca;
	private String pibDobavljaca;
	private String nazivKupca;
	private String adresaKupca;
	private String pibKupca;
	private int brojRacuna;
	private LocalDate datumRacuna;
	private double vrednostRobe;
	private double vrednostUsluga;
	private double ukupnoRobaIUsluge;
	private double ukupanRabat;
	private double ukupanPorez;
	private String oznakaValute;
	private double iznosZaUplatu;
	private String uplataNaRacun;
	private LocalDate datumValute;
	private HashMap<Integer, StavkaFakture> stavkeFakture;
	
	private Faktura(){
		stavkeFakture = new HashMap<Integer, StavkaFakture>();
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

	public String getNazivDobavljaca() {
		return nazivDobavljaca;
	}

	public void setNazivDobavljaca(String nazivDobavljaca) {
		this.nazivDobavljaca = nazivDobavljaca;
	}

	public String getAdresaDobavljaca() {
		return adresaDobavljaca;
	}

	public void setAdresaDobavljaca(String adresaDobavljaca) {
		this.adresaDobavljaca = adresaDobavljaca;
	}

	public String getPibDobavljaca() {
		return pibDobavljaca;
	}

	public void setPibDobavljaca(String pibDobavljaca) {
		this.pibDobavljaca = pibDobavljaca;
	}

	public String getNazivKupca() {
		return nazivKupca;
	}

	public void setNazivKupca(String nazivKupca) {
		this.nazivKupca = nazivKupca;
	}

	public String getAdresaKupca() {
		return adresaKupca;
	}

	public void setAdresaKupca(String adresaKupca) {
		this.adresaKupca = adresaKupca;
	}

	public String getPibKupca() {
		return pibKupca;
	}

	public void setPibKupca(String pibKupca) {
		this.pibKupca = pibKupca;
	}

	public int getBrojRacuna() {
		return brojRacuna;
	}

	public void setBrojRacuna(int brojRacuna) {
		this.brojRacuna = brojRacuna;
	}

	public LocalDate getDatumRacuna() {
		return datumRacuna;
	}

	public void setDatumRacuna(LocalDate datumRacuna) {
		this.datumRacuna = datumRacuna;
	}

	public double getVrednostRobe() {
		return vrednostRobe;
	}

	public void setVrednostRobe(double vrednostRobe) {
		this.vrednostRobe = vrednostRobe;
	}

	public double getVrednostUsluga() {
		return vrednostUsluga;
	}

	public void setVrednostUsluga(double vrednostUsluga) {
		this.vrednostUsluga = vrednostUsluga;
	}

	public double getUkupnoRobaIUsluge() {
		return ukupnoRobaIUsluge;
	}

	public void setUkupnoRobaIUsluge(double ukupnoRobaIUsluge) {
		this.ukupnoRobaIUsluge = ukupnoRobaIUsluge;
	}

	public double getUkupanRabat() {
		return ukupanRabat;
	}

	public void setUkupanRabat(double ukupanRabat) {
		this.ukupanRabat = ukupanRabat;
	}

	public double getUkupanPorez() {
		return ukupanPorez;
	}

	public void setUkupanPorez(double ukupanPorez) {
		this.ukupanPorez = ukupanPorez;
	}

	public String getOznakaValute() {
		return oznakaValute;
	}

	public void setOznakaValute(String oznakaValute) {
		this.oznakaValute = oznakaValute;
	}

	public double getIznosZaUplatu() {
		return iznosZaUplatu;
	}

	public void setIznosZaUplatu(double iznosZaUplatu) {
		this.iznosZaUplatu = iznosZaUplatu;
	}

	public String getUplataNaRacun() {
		return uplataNaRacun;
	}

	public void setUplataNaRacun(String uplataNaRacun) {
		this.uplataNaRacun = uplataNaRacun;
	}

	public LocalDate getDatumValute() {
		return datumValute;
	}

	public void setDatumValute(LocalDate datumValute) {
		this.datumValute = datumValute;
	}

	public HashMap<Integer, StavkaFakture> getStavkeFakture() {
		return stavkeFakture;
	}

	public void setStavkeFakture(HashMap<Integer, StavkaFakture> stavkeFakture) {
		this.stavkeFakture = stavkeFakture;
	}

	
}
