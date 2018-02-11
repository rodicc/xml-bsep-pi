package repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import model.NalogZaPlacanje;

public interface NalogZaPlacanjeRepository extends CrudRepository<NalogZaPlacanje, Integer>{

	@Query("SELECT n FROM NalogZaPlacanje n where n.racunDuznika=:racun OR n.racunPrimaoca=:racun")
	public List<NalogZaPlacanje> nadjiPoRacunuDuznikaIliPrimaoca(@Param("racun") String racun);
}
