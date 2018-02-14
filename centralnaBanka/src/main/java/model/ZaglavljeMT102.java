package model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class ZaglavljeMT102 {

	@Id @GeneratedValue
	private int id;
    private String idPoruke;
    private String swiftKodBankeDuznika;
    private String obracunskiRacunBankeDuznika;
    private String swiftKodBankePoverioca;
    private String obracunskiRacunBankePoverioca;
    private BigDecimal ukupanIznos;
    private String sifraValute;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date datumValute;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date datum;
    
    
    
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
	public String getSwiftKodBankeDuznika() {
		return swiftKodBankeDuznika;
	}
	public void setSwiftKodBankeDuznika(String swiftKodBankeDuznika) {
		this.swiftKodBankeDuznika = swiftKodBankeDuznika;
	}
	public String getObracunskiRacunBankeDuznika() {
		return obracunskiRacunBankeDuznika;
	}
	public void setObracunskiRacunBankeDuznika(String obracunskiRacunBankeDuznika) {
		this.obracunskiRacunBankeDuznika = obracunskiRacunBankeDuznika;
	}
	public String getSwiftKodBankePoverioca() {
		return swiftKodBankePoverioca;
	}
	public void setSwiftKodBankePoverioca(String swiftKodBankePoverioca) {
		this.swiftKodBankePoverioca = swiftKodBankePoverioca;
	}
	public String getObracunskiRacunBankePoverioca() {
		return obracunskiRacunBankePoverioca;
	}
	public void setObracunskiRacunBankePoverioca(String obracunskiRacunBankePoverioca) {
		this.obracunskiRacunBankePoverioca = obracunskiRacunBankePoverioca;
	}
	public BigDecimal getUkupanIznos() {
		return ukupanIznos;
	}
	public void setUkupanIznos(BigDecimal ukupanIznos) {
		this.ukupanIznos = ukupanIznos;
	}
	public String getSifraValute() {
		return sifraValute;
	}
	public void setSifraValute(String sifraValute) {
		this.sifraValute = sifraValute;
	}
	public Date getDatumValute() {
		return datumValute;
	}
	public void setDatumValute(Date datumValute) {
		this.datumValute = datumValute;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}

    
    
}
