package ftn.xmlwebservisi.firme.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class StavkaFakture {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private BigDecimal redniBroj;
	private String nazivRobeIliUsluge;
	private BigDecimal kolicina;
	private String jedinicaMere;
	private BigDecimal jedinicnaCena;
	private BigDecimal vrednost;
	private BigDecimal procenatRabata;
	private BigDecimal iznosRabata;
	private BigDecimal umanjenoZaRabat;
	private BigDecimal ukupanPorez;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "faktura_id")
	@JsonBackReference
	private Faktura faktura;

	public StavkaFakture() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getRedniBroj() {
		return redniBroj;
	}

	public void setRedniBroj(BigDecimal redniBroj) {
		this.redniBroj = redniBroj;
	}

	public String getNazivRobeIliUsluge() {
		return nazivRobeIliUsluge;
	}

	public void setNazivRobeIliUsluge(String nazivRobeIliUsluge) {
		this.nazivRobeIliUsluge = nazivRobeIliUsluge;
	}

	public BigDecimal getKolicina() {
		return kolicina;
	}

	public void setKolicina(BigDecimal kolicina) {
		this.kolicina = kolicina;
	}

	public String getJedinicaMere() {
		return jedinicaMere;
	}

	public void setJedinicaMere(String jedinicaMere) {
		this.jedinicaMere = jedinicaMere;
	}

	public BigDecimal getJedinicnaCena() {
		return jedinicnaCena;
	}

	public void setJedinicnaCena(BigDecimal jedinicnaCena) {
		this.jedinicnaCena = jedinicnaCena;
	}

	public BigDecimal getVrednost() {
		return vrednost;
	}

	public void setVrednost(BigDecimal vrednost) {
		this.vrednost = vrednost;
	}

	public BigDecimal getProcenatRabata() {
		return procenatRabata;
	}

	public void setProcenatRabata(BigDecimal procenatRabata) {
		this.procenatRabata = procenatRabata;
	}

	public BigDecimal getIznosRabata() {
		return iznosRabata;
	}

	public void setIznosRabata(BigDecimal iznosRabata) {
		this.iznosRabata = iznosRabata;
	}

	public BigDecimal getUmanjenoZaRabat() {
		return umanjenoZaRabat;
	}

	public void setUmanjenoZaRabat(BigDecimal umanjenoZaRabat) {
		this.umanjenoZaRabat = umanjenoZaRabat;
	}

	public BigDecimal getUkupanPorez() {
		return ukupanPorez;
	}

	public void setUkupanPorez(BigDecimal ukupanPorez) {
		this.ukupanPorez = ukupanPorez;
	}

	public Faktura getFaktura() {
		return faktura;
	}

	public void setFaktura(Faktura faktura) {
		this.faktura = faktura;
	}

}
