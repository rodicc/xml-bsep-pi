package repository;

import org.springframework.data.repository.CrudRepository;

import model.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
	
	Role findByName(String name);
}
