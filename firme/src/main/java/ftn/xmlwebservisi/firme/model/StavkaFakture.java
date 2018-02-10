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
	private Integer redniBroj;
	private String naziv;
	private BigDecimal kolicina;
	private String jedinicaMere;
	private BigDecimal jedinicnaCena;
	private BigDecimal vrednost;
	private BigDecimal Rabat;
	private BigDecimal iznosRabata;
	private BigDecimal umanjenoZaRabat;

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

	public Integer getRedniBroj() {
		return redniBroj;
	}

	public void setRedniBroj(Integer redniBroj) {
		this.redniBroj = redniBroj;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String nazivRobeIliUsluge) {
		this.naziv = nazivRobeIliUsluge;
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

	public BigDecimal getRabat() {
		return Rabat;
	}

	public void setRabat(BigDecimal procenatRabata) {
		this.Rabat = procenatRabata;
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


	public Faktura getFaktura() {
		return faktura;
	}

	public void setFaktura(Faktura faktura) {
		this.faktura = faktura;
	}

}
