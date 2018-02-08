package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;

@Entity
public class ZahtevZaIzvod {

	@Id @GeneratedValue
	private int id;
	private String brojRacuna;
    private Date datum;
    private int redniBrojPreseka;
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
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public int getRedniBrojPreseka() {
		return redniBrojPreseka;
	}
	public void setRedniBrojPreseka(int redniBrojPreseka) {
		this.redniBrojPreseka = redniBrojPreseka;
	}
    
    
}
