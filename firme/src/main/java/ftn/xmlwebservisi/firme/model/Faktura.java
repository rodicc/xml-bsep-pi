package ftn.xmlwebservisi.firme.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Faktura {

	@Id
	@GeneratedValue
	private Integer id;
	
	private String idPoruke;
	
	private String nazivDobavljaca;
	private String adresaDobavljaca;
	private String pibDobavljaca;
	private String nazivKupca;
	private String adresaKupca;
	private String pibKupca;
	private BigDecimal brojRacuna;
	private String uplataNaRacun;
	private Date datumRacuna;
	private Date datumValute;
	private String oznakaValute;
	private BigDecimal vrednostRobe;
	private BigDecimal ukupanRabat;
	private BigDecimal iznosZaUplatu;
	
	

	@OneToMany(mappedBy = "faktura", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<StavkaFakture> stavkeFakture;

	private Faktura() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIdPoruke() {
		return idPoruke;
	}

	public void setIdPoruke(String idPoruke) {
		this.idPoruke = idPoruke;
	}

	public String getNazivDobavljaca() {
		return nazivDobavljaca;
	}

	public void setNazivDobavljaca(String nazivDobavljaca) {
		this.nazivDobavljaca = nazivDobavljaca;
	}

	public String getAdresaDobavljaca() {
		return adresaDobavljaca;
	}

	public void setAdresaDobavljaca(String adresaDobavljaca) {
		this.adresaDobavljaca = adresaDobavljaca;
	}

	public String getPibDobavljaca() {
		return pibDobavljaca;
	}

	public void setPibDobavljaca(String pibDobavljaca) {
		this.pibDobavljaca = pibDobavljaca;
	}

	public String getNazivKupca() {
		return nazivKupca;
	}

	public void setNazivKupca(String nazivKupca) {
		this.nazivKupca = nazivKupca;
	}

	public String getAdresaKupca() {
		return adresaKupca;
	}

	public void setAdresaKupca(String adresaKupca) {
		this.adresaKupca = adresaKupca;
	}

	public String getPibKupca() {
		return pibKupca;
	}

	public BigDecimal getBrojRacuna() {
		return brojRacuna;
	}

	public void setBrojRacuna(BigDecimal brojRacuna) {
		this.brojRacuna = brojRacuna;
	}

	public Date getDatumRacuna() {
		return datumRacuna;
	}

	public void setDatumRacuna(Date datumRacuna) {
		this.datumRacuna = datumRacuna;
	}

	public BigDecimal getVrednostRobe() {
		return vrednostRobe;
	}

	public void setVrednostRobe(BigDecimal vrednostRobe) {
		this.vrednostRobe = vrednostRobe;
	}


	public BigDecimal getUkupanRabat() {
		return ukupanRabat;
	}

	public void setUkupanRabat(BigDecimal ukupanRabat) {
		this.ukupanRabat = ukupanRabat;
	}


	public String getOznakaValute() {
		return oznakaValute;
	}

	public void setOznakaValute(String oznakaValute) {
		this.oznakaValute = oznakaValute;
	}

	public BigDecimal getIznosZaUplatu() {
		return iznosZaUplatu;
	}

	public void setIznosZaUplatu(BigDecimal iznosZaUplatu) {
		this.iznosZaUplatu = iznosZaUplatu;
	}

	public String getUplataNaRacun() {
		return uplataNaRacun;
	}

	public void setUplataNaRacun(String uplataNaRacun) {
		this.uplataNaRacun = uplataNaRacun;
	}

	public Date getDatumValute() {
		return datumValute;
	}

	public void setDatumValute(Date datumValute) {
		this.datumValute = datumValute;
	}

	public List<StavkaFakture> getStavkeFakture() {
		return stavkeFakture;
	}

	public void setStavkeFakture(List<StavkaFakture> stavkeFakture) {
		this.stavkeFakture = stavkeFakture;
	}

	public void setPibKupca(String pibKupca) {
		this.pibKupca = pibKupca;
	}

}
