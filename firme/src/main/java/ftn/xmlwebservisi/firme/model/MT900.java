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
public class MT900 {

	@Id @GeneratedValue
	private int id;
	private String idPoruke;
    private String swiftKodBankeDuznika;
    private String obracunskiRacunBankeDuznika;
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
