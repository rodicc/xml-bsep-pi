package ftn.xmlwebservisi.firme.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class MT102 {

	@Id @GeneratedValue
	private int id;
	@OneToOne
	private ZaglavljeMT102 zaglavljeMT102;
	@OneToMany
	private List<PojedinacnoPlacanjeMT102> pojedinacnaPlacanja;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ZaglavljeMT102 getZaglavljeMT102() {
		return zaglavljeMT102;
	}
	public void setZaglavljeMT102(ZaglavljeMT102 zaglavljeMT102) {
		this.zaglavljeMT102 = zaglavljeMT102;
	}
	public List<PojedinacnoPlacanjeMT102> getPojedinacnaPlacanja() {
		return pojedinacnaPlacanja;
	}
	public void setPojedinacnaPlacanja(List<PojedinacnoPlacanjeMT102> pojedinacnaPlacanja) {
		this.pojedinacnaPlacanja = pojedinacnaPlacanja;
	}
	
	
}
