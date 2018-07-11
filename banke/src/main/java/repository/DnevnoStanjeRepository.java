package repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import model.DnevnoStanjeRacuna;

public interface DnevnoStanjeRepository extends CrudRepository<DnevnoStanjeRacuna, Integer>{
	
	@Query("select d " +
			   "from DnevnoStanjeRacuna d " +
			   "where d.datumStanja = :datum AND (d.racun.brojRacuna = :brojRacuna)")
	public DnevnoStanjeRacuna nadjiPoDatumuIRacunu(@Param("datum")Date datum, @Param("brojRacuna") String brojRacuna);
	
	
}
