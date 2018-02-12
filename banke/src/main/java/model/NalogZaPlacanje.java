package model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class NalogZaPlacanje {

	@Id @GeneratedValue
	private int id;
    private String idPoruke;
    private String duznikNalogodavac;
    private String svrhaPlacanja;
    private String primalacPoverilac;
    private Date datumNaloga;
    private Date datumValute;
    private String racunDuznika;
    private int modelZaduzenja;
    private String pozivNaBrojZaduzenja;
    private String racunPrimaoca;
    private int modelOdobrenja;
    private String pozivNaBrojOdobrenja;
    private BigDecimal iznos;
    private String oznakaValute;
    private boolean hitno;
    private boolean nijeRegulisan;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	public void setRacunPrimaoca(String racunPrimaoca) {
		this.racunPrimaoca = racunPrimaoca;
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
	public boolean isHitno() {
		return hitno;
	}
	public void setHitno(boolean hitno) {
		this.hitno = hitno;
	}
	public boolean isNijeRegulisan() {
		return nijeRegulisan;
	}
	public void setNijeRegulisan(boolean nijeRegulisan) {
		this.nijeRegulisan = nijeRegulisan;
	}
    
    
}
