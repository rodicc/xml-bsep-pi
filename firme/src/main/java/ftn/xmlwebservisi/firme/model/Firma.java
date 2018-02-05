package ftn.xmlwebservisi.firme.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Firma {

	@Id
	private int id;
	private String nazivFirme;
	private String brojRacuna;
	
	public Firma(){
		
	}

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getNazivFirme() {
		return nazivFirme;
	}

	public void setNazivFirme(String nazivFirme) {
		this.nazivFirme = nazivFirme;
	}

	public String getBrojRacuna() {
		return brojRacuna;
	}

	public void setBrojRacuna(String brojRacuna) {
		this.brojRacuna = brojRacuna;
}
}
