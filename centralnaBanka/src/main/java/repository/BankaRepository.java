package repository;

import org.springframework.data.repository.CrudRepository;

import model.Banka;

public interface BankaRepository extends CrudRepository<Banka, Integer> {

	public Banka findBySwiftKodBanke(String swiftKodBanke);
	
	public Banka findByOznakaBanke(String oznakaBanke);
}
