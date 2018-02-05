package ftn.xmlwebservisi.firme.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ZahtevZaIzvod {
	@Id
	private int id;
	private String brojRacuna;
	private LocalDate datum;
	private int redniBrojPreseka;
	
	public ZahtevZaIzvod(){
		
	}

	
	
	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getBrojRacuna() {
		return brojRacuna;
	}

	public void setBrojRacuna(String brojRacuna) {
		this.brojRacuna = brojRacuna;
	}

	public LocalDate getDatum() {
		return datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

	public int getRedniBrojPreseka() {
		return redniBrojPreseka;
	}

	public void setRedniBrojPreseka(int redniBrojPreseka) {
		this.redniBrojPreseka = redniBrojPreseka;
}
}
