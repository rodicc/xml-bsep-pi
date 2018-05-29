package ftn.xmlwebservisi.firme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.firme.dto.UserDTO;
import ftn.xmlwebservisi.firme.model.Role;
import ftn.xmlwebservisi.firme.model.User;
import ftn.xmlwebservisi.firme.repository.RoleRepository;
import ftn.xmlwebservisi.firme.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public User createUser(UserDTO userDto) {
		User newUser = new User();
		newUser.setUsername(userDto.getUsername());
		newUser.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		
		Role role = roleRepository.findByName("USER");
		newUser.setRole(role);
		
		return userRepository.save(newUser);
	}
}
