package model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Firma {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String nazivFirme;
	private String adresa;
	private String pib;
	private String brojRacuna;
	private BigDecimal stanjeRacuna;
	private BigDecimal rezervisanNovac;

	public Firma() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNazivFirme() {
		return nazivFirme;
	}

	public void setNazivFirme(String nazivFirme) {
		this.nazivFirme = nazivFirme;
	}

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public String getPib() {
		return pib;
	}

	public void setPib(String pib) {
		this.pib = pib;
	}

	public String getBrojRacuna() {
		return brojRacuna;
	}

	public void setBrojRacuna(String brojRacuna) {
		this.brojRacuna = brojRacuna;
	}

	public BigDecimal getStanjeRacuna() {
		return stanjeRacuna;
	}

	public void setStanjeRacuna(BigDecimal stanjeRacuna) {
		this.stanjeRacuna = stanjeRacuna;
	}

	public BigDecimal getRezervisanNovac() {
		return rezervisanNovac;
	}

	public void setRezervisanNovac(BigDecimal rezervisanNovac) {
		this.rezervisanNovac = rezervisanNovac;
	}
	
	
}
