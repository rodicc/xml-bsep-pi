package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Banka {

	@Id
	@GeneratedValue
	private int id;
	private String nazivBanke;
	@OneToOne
	private Racun obracunskiRacun;
	
	private String swiftKodBanke;
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
	
	public String getSwiftKodBanke() {
		return swiftKodBanke;
	}
	public void setSwiftKodBanke(String swiftKodBanke) {
		this.swiftKodBanke = swiftKodBanke;
	}
	public Racun getObracunskiRacun() {
		return obracunskiRacun;
	}
	public void setObracunskiRacun(Racun obracunskiRacun) {
		this.obracunskiRacun = obracunskiRacun;
	}
	public String getOznakaBanke() {
		return oznakaBanke;
	}
	public void setOznakaBanke(String oznakaBanke) {
		this.oznakaBanke = oznakaBanke;
	}
	
	
}
