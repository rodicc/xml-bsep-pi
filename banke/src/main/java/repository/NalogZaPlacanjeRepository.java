package repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import model.NalogZaPlacanje;

public interface NalogZaPlacanjeRepository extends CrudRepository<NalogZaPlacanje, Integer>{

	@Query("from NalogZaPlacanje n " +
		   "where n.datumNaloga = datum naloga AND (n.racunDuznika = :brojRacuna OR n.racunPrimaoca = :brojRacuna")
	public List<NalogZaPlacanje> nadjiPoDatumuIBrojuRacuna(Date datumNaloga, String brojRacuna);
}
