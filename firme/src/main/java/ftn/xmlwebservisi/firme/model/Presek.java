package ftn.xmlwebservisi.firme.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Presek {
	@Id
	private Integer idPreseka;
	private String brojRacuna;
	private Date datumNaloga;
	private int brojPreseka;
	private BigDecimal prethodnoStanje;
	private int brojPromenaUKorist;
	private BigDecimal ukupnoUKorist;
	private int brojPromenaNaTeret;
	private BigDecimal ukupnoNaTeret;
	private BigDecimal novoStanje;
	
	
	@OneToMany(mappedBy = "presek", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<StavkaPreseka> stavkePreseka;
	
	public Presek(){
		stavkePreseka = new ArrayList<StavkaPreseka>();
	}

	public int getIdPreseka() {
		return idPreseka;
	}

	public void setIdPreseka(Integer idPreseka) {
		this.idPreseka = idPreseka;
	}

	public void setBrojPreseka(Integer brojPreseka) {
		this.brojPreseka = brojPreseka;
	}

	public void setBrojPromenaUKorist(Integer brojPromenaUKorist) {
		this.brojPromenaUKorist = brojPromenaUKorist;
	}

	public void setBrojPromenaNaTeret(Integer brojPromenaNaTeret) {
		this.brojPromenaNaTeret = brojPromenaNaTeret;
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

	public BigDecimal getPrethodnoStanje() {
		return prethodnoStanje;
	}

	public void setPrethodnoStanje(BigDecimal prethodnoStanje) {
		this.prethodnoStanje = prethodnoStanje;
	}

	public int getBrojPromenaUKorist() {
		return brojPromenaUKorist;
	}

	public void setBrojPromenaUKorist(int brojPromenaUKorist) {
		this.brojPromenaUKorist = brojPromenaUKorist;
	}

	public BigDecimal getUkupnoUKorist() {
		return ukupnoUKorist;
	}

	public void setUkupnoUKorist(BigDecimal ukupnoUKorist) {
		this.ukupnoUKorist = ukupnoUKorist;
	}

	public int getBrojPromenaNaTeret() {
		return brojPromenaNaTeret;
	}

	public void setBrojPromenaNaTeret(int brojPromenaNaTeret) {
		this.brojPromenaNaTeret = brojPromenaNaTeret;
	}

	public BigDecimal getUkupnoNaTeret() {
		return ukupnoNaTeret;
	}

	public void setUkupnoNaTeret(BigDecimal ukupnoNaTeret) {
		this.ukupnoNaTeret = ukupnoNaTeret;
	}

	public BigDecimal getNovoStanje() {
		return novoStanje;
	}

	public void setNovoStanje(BigDecimal novoStanje) {
		this.novoStanje = novoStanje;
	}

	public List<StavkaPreseka> getStavkePreseka() {
		return stavkePreseka;
	}

	public void setStavkePreseka(List<StavkaPreseka> stavkePreseka) {
		this.stavkePreseka = stavkePreseka;
	}

	
}
