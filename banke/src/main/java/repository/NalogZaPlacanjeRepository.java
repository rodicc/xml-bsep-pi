package repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import model.NalogZaPlacanje;

public interface NalogZaPlacanjeRepository extends CrudRepository<NalogZaPlacanje, Integer>{


	@Query("select n " +
		   "from NalogZaPlacanje n " +
		   "where n.datumNaloga = :datumNaloga AND (n.racunDuznika = :brojRacuna OR n.racunPrimaoca = :brojRacuna)")
	public List<NalogZaPlacanje> nadjiPoDatumuIBrojuRacuna(Date datumNaloga, String brojRacuna);
	
	@Query("SELECT nzp FROM NalogZaPlacanje nzp " +
			"WHERE nzp.nijeRegulisan = :regulisan " +
			"AND nzp.racunDuznika LIKE :bankaDuznika " + 
			"AND nzp.racunPrimaoca LIKE :bankaPrimaoca")	
	List<NalogZaPlacanje> nadjiSveNeregulisane(@Param("regulisan")boolean regulisan, 
			@Param("bankaDuznika")String bankaDuznika, @Param("bankaPrimaoca")String bankaPrimaoca);

	@Query("SELECT n FROM NalogZaPlacanje n where n.racunDuznika=:racun OR n.racunPrimaoca=:racun")
	public List<NalogZaPlacanje> nadjiPoRacunuDuznikaIliPrimaoca(@Param("racun") String racun);

}
