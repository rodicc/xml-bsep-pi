package repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import model.Racun;

public interface RacunRepository extends CrudRepository<Racun, Integer>{

	public Racun findByBrojRacuna(String brojRacuna);
	public List<Racun> findByVazeci(boolean vazeci);
	
}
