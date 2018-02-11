package repository;

import org.springframework.data.repository.CrudRepository;

import model.Firma;

public interface FirmaRepository extends CrudRepository<Firma, Integer> {

	public Firma findByBrojRacuna(String brojRacuna);
}
