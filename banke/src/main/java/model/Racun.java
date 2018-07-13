package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Racun {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String brojRacuna;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
	private Date datumOtvaranja;
	private BigDecimal stanjeRacuna;
	private BigDecimal rezervisanNovac;
	private boolean vazeci;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "firma_id")
	@JsonBackReference
	private Firma firma;
	
	public Banka getBanka_owner() {
		return banka_owner;
	}

	public void setBanka_owner(Banka banka_owner) {
		this.banka_owner = banka_owner;
	}

	@OneToOne(mappedBy = "obracunskiRacun")
	private Banka banka;
	
	@ManyToOne
	@JoinColumn(name = "banka_id")
	@JsonBackReference
	private Banka banka_owner;
	
	
	@OneToOne
	@JsonManagedReference
	private UkidanjeRacuna ukidanjeRacuna;
	
	@OneToMany(mappedBy = "racun", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<DnevnoStanjeRacuna> dnevnoStanjeRacuna;

	
	
	public Racun() {
		super();
		this.rezervisanNovac = new BigDecimal(0.0);
		this.stanjeRacuna = new BigDecimal(0.0);
		this.vazeci = true;
		this.ukidanjeRacuna = null;
		this.dnevnoStanjeRacuna = new ArrayList<>();
		this.datumOtvaranja = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBrojRacuna() {
		return brojRacuna;
	}

	public void setBrojRacuna(String brojRacuna) {
		this.brojRacuna = brojRacuna;
	}

	public Date getDatumOtvaranja() {
		return datumOtvaranja;
	}

	public void setDatumOtvaranja(Date datumOtvaranja) {
		this.datumOtvaranja = datumOtvaranja;
	}

	public BigDecimal getRezervisanNovac() {
		return rezervisanNovac;
	}

	public void setRezervisanNovac(BigDecimal rezervisanNovac) {
		this.rezervisanNovac = rezervisanNovac;
	}

	public boolean isVazeci() {
		return vazeci;
	}

	public void setVazeci(boolean vazeci) {
		this.vazeci = vazeci;
	}

	
	public Firma getFirma() {
		return firma;
	}

	public void setFirma(Firma firma) {
		this.firma = firma;
	}
	
	@JsonIgnore
	public UkidanjeRacuna getUkidanjeRacuna() {
		return ukidanjeRacuna;
	}

	public void setUkidanjeRacuna(UkidanjeRacuna ukidanjeRacuna) {
		this.ukidanjeRacuna = ukidanjeRacuna;
	}
	
	
	public List<DnevnoStanjeRacuna> getDnevnoStanjeRacuna() {
		return dnevnoStanjeRacuna;
	}

	public void setDnevnoStanjeRacuna( List<DnevnoStanjeRacuna> dnevnoStanjeRacuna) {
		this.dnevnoStanjeRacuna = dnevnoStanjeRacuna;
	}

	public BigDecimal getStanjeRacuna() {
		return stanjeRacuna;
	}

	public void setStanjeRacuna(BigDecimal stanjeRacuna) {
		this.stanjeRacuna = stanjeRacuna;
	}
	
	@JsonIgnore
	public Banka getBanka() {
		return banka;
	}

	public void setBanka(Banka banka) {
		this.banka = banka;
	}
	
	
}
