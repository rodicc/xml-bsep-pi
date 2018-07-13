package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class UkidanjeRacuna {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
	private Date datumUkidanja;
	private String racunNaKojiSePrebacujuSredstva;
	
	@OneToOne(mappedBy = "ukidanjeRacuna")
	@JsonBackReference
	private Racun ukinutRacun;
	
	

	public UkidanjeRacuna() {
		super();
		this.datumUkidanja = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDatumUkidanja() {
		return datumUkidanja;
	}

	public void setDatumUkidanja(Date datumUkidanja) {
		this.datumUkidanja = datumUkidanja;
	}

	public String getRacunNaKojiSePrebacujuSredstva() {
		return racunNaKojiSePrebacujuSredstva;
	}

	public void setRacunNaKojiSePrebacujuSredstva(String racunNaKojiSePrebacujuSredstva) {
		this.racunNaKojiSePrebacujuSredstva = racunNaKojiSePrebacujuSredstva;
	}

	public Racun getUkinutRacun() {
		return ukinutRacun;
	}

	public void setUkinutRacun(Racun ukinutRacun) {
		this.ukinutRacun = ukinutRacun;
	}
	
	
}
