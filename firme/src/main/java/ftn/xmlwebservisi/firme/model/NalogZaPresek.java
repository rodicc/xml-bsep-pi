package ftn.xmlwebservisi.firme.model;

import java.util.Date;

public class NalogZaPresek {

	private String brojRacuna;
	private Date datum;
	private Integer redniBrojPreseka;
	
	
	
	public NalogZaPresek() {
		super();
	}
	
	public String getBrojRacuna() {
		return brojRacuna;
	}
	public void setBrojRacuna(String brojRacuna) {
		this.brojRacuna = brojRacuna;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public Integer getRedniBrojPreseka() {
		return redniBrojPreseka;
	}
	public void setRedniBrojPreseka(Integer redniBrojPreseka) {
		this.redniBrojPreseka = redniBrojPreseka;
	}
	
	
	
}
