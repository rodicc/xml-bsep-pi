package ftn.xmlwebservisi.firme.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StavkaFakture {

	@Id
	private int id;
	private String idFakture;
	private int redniBroj;
	private String nazivRobeIliUsluge;
	private double kolicina;
	private String jedinicaMere;
	private double jedinicnaCena;
	private double vrednost;
	private double procenatRabata;
	private double iznosRabata;
	private double umanjenoZaRabat;
	private double ukupanPorez;
	
	public StavkaFakture(){
		
	}

	
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getIdFakture(){
		return idFakture;
	}
	
	public void setIdFakture(String idFakture){
		this.idFakture = idFakture;
	}
	
	public int getRedniBroj() {
		return redniBroj;
	}

	public void setRedniBroj(int redniBroj) {
		this.redniBroj = redniBroj;
	}

	public String getNazivRobeIliUsluge() {
		return nazivRobeIliUsluge;
	}

	public void setNazivRobeIliUsluge(String nazivRobeIliUsluge) {
		this.nazivRobeIliUsluge = nazivRobeIliUsluge;
	}

	public double getKolicina() {
		return kolicina;
	}

	public void setKolicina(double kolicina) {
		this.kolicina = kolicina;
	}

	public String getJedinicaMere() {
		return jedinicaMere;
	}

	public void setJedinicaMere(String jedinicaMere) {
		this.jedinicaMere = jedinicaMere;
	}

	public double getJedinicnaCena() {
		return jedinicnaCena;
	}

	public void setJedinicnaCena(double jedinicnaCena) {
		this.jedinicnaCena = jedinicnaCena;
	}

	public double getVrednost() {
		return vrednost;
	}

	public void setVrednost(double vrednost) {
		this.vrednost = vrednost;
	}

	public double getProcenatRabata() {
		return procenatRabata;
	}

	public void setProcenatRabata(double procenatRabata) {
		this.procenatRabata = procenatRabata;
	}

	public double getIznosRabata() {
		return iznosRabata;
	}

	public void setIznosRabata(double iznosRabata) {
		this.iznosRabata = iznosRabata;
	}

	public double getUmanjenoZaRabat() {
		return umanjenoZaRabat;
	}

	public void setUmanjenoZaRabat(double umanjenoZaRabat) {
		this.umanjenoZaRabat = umanjenoZaRabat;
	}

	public double getUkupanPorez() {
		return ukupanPorez;
	}

	public void setUkupanPorez(double ukupanPorez) {
		this.ukupanPorez = ukupanPorez;
}
}
