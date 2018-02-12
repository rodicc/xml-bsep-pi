package ftn.xmlwebservisi.firme.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;

@Entity
public class MT910 {

	@Id @GeneratedValue
	private int id;
	private String idPoruke;
    private String swiftKodBankePoverioca;
    private String obracunskiRacunBankePoverioca;
    private String idPorukeNaloga;
    private Date datumValute;
    private BigDecimal iznos;
    private String sifraValute;
    
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
	public String getIdPorukeNaloga() {
		return idPorukeNaloga;
	}
	public void setIdPorukeNaloga(String idPorukeNaloga) {
		this.idPorukeNaloga = idPorukeNaloga;
	}
	public Date getDatumValute() {
		return datumValute;
	}
	public void setDatumValute(Date datumValute) {
		this.datumValute = datumValute;
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
