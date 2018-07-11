package ftn.xmlwebservisi.firme.model;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class DnevnoStanjeRacuna {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private BigDecimal novoStanje;
	private BigDecimal prethodnoStanje;
	private BigDecimal prometNaTeret;
	private BigDecimal prometUKorist;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "racun_id")
	@JsonBackReference(value = "stanje-racun")
	private Racun racun;
	
	@OneToMany(mappedBy = "dnevnoStanje", cascade = CascadeType.ALL)
	@JsonManagedReference(value = "stanje-nalog")
	private List<NalogZaPlacanje> analitikaIzvoda;
	
	

	public DnevnoStanjeRacuna() {
		super();
		this.novoStanje = new BigDecimal(0.0);
		this.prethodnoStanje = new BigDecimal(0.0);
		this.prometNaTeret = new BigDecimal(0.0);
		this.prometUKorist = new BigDecimal(0.0);
		this.analitikaIzvoda = new ArrayList<>();
	}
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getNovoStanje() {
		return novoStanje;
	}

	public void setNovoStanje(BigDecimal novoStanje) {
		this.novoStanje = novoStanje;
	}

	public BigDecimal getPrethodnoStanje() {
		return prethodnoStanje;
	}

	public void setPrethodnoStanje(BigDecimal prethodnoStanje) {
		this.prethodnoStanje = prethodnoStanje;
	}

	public BigDecimal getPrometNaTeret() {
		return prometNaTeret;
	}

	public void setPrometNaTeret(BigDecimal prometNaTeret) {
		this.prometNaTeret = prometNaTeret;
	}

	public BigDecimal getPrometUKorist() {
		return prometUKorist;
	}

	public void setPrometUKorist(BigDecimal prometUKorist) {
		this.prometUKorist = prometUKorist;
	}

	public Racun getRacun() {
		return racun;
	}

	public void setRacun(Racun racun) {
		this.racun = racun;
	}

	public List<NalogZaPlacanje> getAnalitikaIzvoda() {
		return analitikaIzvoda;
	}

	public void setAnalitikaIzvoda(List<NalogZaPlacanje> analitikaIzvoda) {
		this.analitikaIzvoda = analitikaIzvoda;
	}
	
	
}
