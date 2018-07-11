package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class NalogZaPlacanje {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String idPoruke;
	private String duznikNalogodavac;
	private String svrhaPlacanja;
	private String primalacPoverilac;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
	private Date datumNaloga;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
	private Date datumValute;
	
	private String racunDuznika;
	private Integer modelZaduzenja;
	private String pozivNaBrojZaduzenja;
	private String racunPrimaoca;
	private Integer modelOdobrenja;
	private String pozivNaBrojOdobrenja;
	
	private Integer sifraPlacanja;
	private BigDecimal iznos;
	private String oznakaValute;
	private Boolean hitno;
	private Boolean nijeRegulisan;

	@ManyToMany(cascade =  CascadeType.ALL)
	@JoinColumn(name = "dnevnoStanje_id")
	//@JsonBackReference
	private List<DnevnoStanjeRacuna> dnevnoStanje;
	
	
	public NalogZaPlacanje() {
		this.dnevnoStanje = new ArrayList<>();
	}

	public Integer getId() {
		return id;
	}

	
	
	public Integer getSifraPlacanja() {
		return sifraPlacanja;
	}

	public void setSifraPlacanja(Integer sifraPlacanja) {
		this.sifraPlacanja = sifraPlacanja;
	}

	public void setModelZaduzenja(Integer modelZaduzenja) {
		this.modelZaduzenja = modelZaduzenja;
	}

	public void setModelOdobrenja(Integer modelOdobrenja) {
		this.modelOdobrenja = modelOdobrenja;
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

	public Date getDatumValute() {
		return datumValute;
	}

	public void setDatumValute(Date datumValute) {
		this.datumValute = datumValute;
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

	public String getRacunPrimaoca() {
		return racunPrimaoca;
	}

	public void setRacunPrimaoca(String racunPoverioca) {
		this.racunPrimaoca = racunPoverioca;
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

	public String getOznakaValute() {
		return oznakaValute;
	}

	public void setOznakaValute(String oznakaValute) {
		this.oznakaValute = oznakaValute;
	}

	public Boolean getHitno() {
		return hitno;
	}

	public void setHitno(Boolean hitno) {
		this.hitno = hitno;
	}

	public Boolean getNijeRegulisan() {
		return nijeRegulisan;
	}

	public void setNijeRegulisan(Boolean nijeRegulisan) {
		this.nijeRegulisan = nijeRegulisan;
	}
	
	@JsonIgnore
	public List<DnevnoStanjeRacuna> getDnevnoStanje() {
		return dnevnoStanje;
	}

	public void setDnevnoStanje(List<DnevnoStanjeRacuna> dnevnoStanje) {
		this.dnevnoStanje = dnevnoStanje;
	}
	
	
}
