package ftn.xmlwebservisi.firme.repository;

import org.springframework.data.repository.CrudRepository;

import ftn.xmlwebservisi.firme.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
	
	User findByUsername(String username);
}
