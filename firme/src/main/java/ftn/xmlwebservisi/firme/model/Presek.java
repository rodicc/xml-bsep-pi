package ftn.xmlwebservisi.firme.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Presek {
	@Id
	private int idPreseka;
	private String brojRacuna;
	private Date datumNaloga;
	private int brojPreseka;
	private double prethodnoStanje;
	private int brojPromenaUKorist;
	private double ukupnoUKorist;
	private int brojPromenaNaTeret;
	private double ukupnoNaTeret;
	private double novoStanje;
	private List<StavkaPreseka> stavkePreseka;
	
	public Presek(){
		stavkePreseka = new ArrayList<StavkaPreseka>();
	}

	public int getIdPreseka() {
		return idPreseka;
	}

	public void setIdPreseka(int idPreseka) {
		this.idPreseka = idPreseka;
	}

	public String getBrojRacuna() {
		return brojRacuna;
	}

	public void setBrojRacuna(String brojRacuna) {
		this.brojRacuna = brojRacuna;
	}

	public Date getDatumNaloga() {
		return datumNaloga;
	}

	public void setDatumNaloga(Date datumNaloga) {
		this.datumNaloga = datumNaloga;
	}

	public int getBrojPreseka() {
		return brojPreseka;
	}

	public void setBrojPreseka(int brojPreseka) {
		this.brojPreseka = brojPreseka;
	}

	public double getPrethodnoStanje() {
		return prethodnoStanje;
	}

	public void setPrethodnoStanje(double prethodnoStanje) {
		this.prethodnoStanje = prethodnoStanje;
	}

	public int getBrojPromenaUKorist() {
		return brojPromenaUKorist;
	}

	public void setBrojPromenaUKorist(int brojPromenaUKorist) {
		this.brojPromenaUKorist = brojPromenaUKorist;
	}

	public double getUkupnoUKorist() {
		return ukupnoUKorist;
	}

	public void setUkupnoUKorist(double ukupnoUKorist) {
		this.ukupnoUKorist = ukupnoUKorist;
	}

	public int getBrojPromenaNaTeret() {
		return brojPromenaNaTeret;
	}

	public void setBrojPromenaNaTeret(int brojPromenaNaTeret) {
		this.brojPromenaNaTeret = brojPromenaNaTeret;
	}

	public double getUkupnoNaTeret() {
		return ukupnoNaTeret;
	}

	public void setUkupnoNaTeret(double ukupnoNaTeret) {
		this.ukupnoNaTeret = ukupnoNaTeret;
	}

	public double getNovoStanje() {
		return novoStanje;
	}

	public void setNovoStanje(double novoStanje) {
		this.novoStanje = novoStanje;
	}

	public List<StavkaPreseka> getStavkePreseka() {
		return stavkePreseka;
	}

	public void setStavkePreseka(List<StavkaPreseka> stavkePreseka) {
		this.stavkePreseka = stavkePreseka;
	}

	
}
