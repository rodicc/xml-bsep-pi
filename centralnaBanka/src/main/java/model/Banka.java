package model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Banka {

	@Id
	@GeneratedValue
	private int id;
	private String nazivBanke;
	private BigDecimal stanjeRacuna;
	private String swiftKodBanke;
	private String obracunskiRacun;
	private String oznakaBanke;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNazivBanke() {
		return nazivBanke;
	}
	public void setNazivBanke(String nazivBanke) {
		this.nazivBanke = nazivBanke;
	}
	public BigDecimal getStanjeRacuna() {
		return stanjeRacuna;
	}
	public void setStanjeRacuna(BigDecimal stanjeRacuna) {
		this.stanjeRacuna = stanjeRacuna;
	}
	public String getSwiftKodBanke() {
		return swiftKodBanke;
	}
	public void setSwiftKodBanke(String swiftKodBanke) {
		this.swiftKodBanke = swiftKodBanke;
	}
	public String getObracunskiRacun() {
		return obracunskiRacun;
	}
	public void setObracunskiRacun(String obracunskiRacun) {
		this.obracunskiRacun = obracunskiRacun;
	}
	public String getOznakaBanke() {
		return oznakaBanke;
	}
	public void setOznakaBanke(String oznakaBanke) {
		this.oznakaBanke = oznakaBanke;
	}
	
	
}
