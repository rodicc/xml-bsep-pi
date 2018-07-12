package model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
	
	@OneToMany(mappedBy = "banka_owner", cascade = CascadeType.ALL)
	@JsonManagedReference(value = "racuniKlijenata")
	private List<Racun> racuniKlijenata;
	
	public Banka() {
		super();
		this.racuniKlijenata = new ArrayList<>();
	}
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
