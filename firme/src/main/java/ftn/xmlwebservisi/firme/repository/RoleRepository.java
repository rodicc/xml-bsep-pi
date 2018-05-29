package ftn.xmlwebservisi.firme.repository;

import org.springframework.data.repository.CrudRepository;

import ftn.xmlwebservisi.firme.model.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	
	Role findByName(String name);
}
