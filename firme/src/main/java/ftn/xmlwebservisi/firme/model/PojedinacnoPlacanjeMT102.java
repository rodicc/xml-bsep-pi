package ftn.xmlwebservisi.firme.model;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class PojedinacnoPlacanjeMT102 {

	@Id @GeneratedValue
	private int id;
    private String idNalogaZaPlacanje;
    private String duznikNalogodavac;
    private String svrhaPlacanja;
    private String primalacPoverilac;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date datumNaloga;
    private String racunDuznika;
    private int modelZaduzenja;
    private String pozivNaBrojZaduzenja;
    private String racunPoverioca;
    private int modelOdobrenja;
    private String pozivNaBrojOdobrenja;
    private BigDecimal iznos;
    private String sifraValute;
    
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdNalogaZaPlacanje() {
		return idNalogaZaPlacanje;
	}
	public void setIdNalogaZaPlacanje(String idNalogaZaPlacanje) {
		this.idNalogaZaPlacanje = idNalogaZaPlacanje;
	}
	public String getDuznikNalogodavac() {
		return duznikNalogodavac;
	}
	public void setDuznikNalogodavac(String duznikNalogodavac) {
		this.duznikNalogodavac = duznikNalogodavac;
	}
	public String getSvrhaPlacanja() {
		return svrhaPlacanja;
	}
	public void setSvrhaPlacanja(String svrhaPlacanja) {
		this.svrhaPlacanja = svrhaPlacanja;
	}
	public String getPrimalacPoverilac() {
		return primalacPoverilac;
	}
	public void setPrimalacPoverilac(String primalacPoverilac) {
		this.primalacPoverilac = primalacPoverilac;
	}
	public Date getDatumNaloga() {
		return datumNaloga;
	}
	public void setDatumNaloga(Date datumNaloga) {
		this.datumNaloga = datumNaloga;
	}
	public String getRacunDuznika() {
		return racunDuznika;
	}
	public void setRacunDuznika(String racunDuznika) {
		this.racunDuznika = racunDuznika;
	}
	public int getModelZaduzenja() {
		return modelZaduzenja;
	}
	public void setModelZaduzenja(int modelZaduzenja) {
		this.modelZaduzenja = modelZaduzenja;
	}
	public String getPozivNaBrojZaduzenja() {
		return pozivNaBrojZaduzenja;
	}
	public void setPozivNaBrojZaduzenja(String pozivNaBrojZaduzenja) {
		this.pozivNaBrojZaduzenja = pozivNaBrojZaduzenja;
	}
	public String getRacunPoverioca() {
		return racunPoverioca;
	}
	public void setRacunPoverioca(String racunPoverioca) {
		this.racunPoverioca = racunPoverioca;
	}
	public int getModelOdobrenja() {
		return modelOdobrenja;
	}
	public void setModelOdobrenja(int modelOdobrenja) {
		this.modelOdobrenja = modelOdobrenja;
	}
	public String getPozivNaBrojOdobrenja() {
		return pozivNaBrojOdobrenja;
	}
	public void setPozivNaBrojOdobrenja(String pozivNaBrojOdobrenja) {
		this.pozivNaBrojOdobrenja = pozivNaBrojOdobrenja;
	}
	public BigDecimal getIznos() {
		return iznos;
	}
	public void setIznos(BigDecimal iznos) {
		this.iznos = iznos;
	}
	public String getSifraValute() {
		return sifraValute;
	}
	public void setSifraValute(String sifraValute) {
		this.sifraValute = sifraValute;
	}
    
    
}
