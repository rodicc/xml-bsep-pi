package ftn.xmlwebservisi.firme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ftn.xmlwebservisi.firme.dto.UserDTO;
import ftn.xmlwebservisi.firme.model.Role;
import ftn.xmlwebservisi.firme.model.User;
import ftn.xmlwebservisi.firme.model.UserPrincipal;
import ftn.xmlwebservisi.firme.repository.RoleRepository;
import ftn.xmlwebservisi.firme.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

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
		newUser.addRole(role);

		return userRepository.save(newUser);
	}

	// Retrieving user-related data from a database
	// Throws UsernameNotFoundException if user could not be found
	// Returns populated UserPrincipal object
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User with username " + username + " could not be found.");
		}

		return new UserPrincipal(user);
	}
	
	public boolean checkOldPassword(UserDetails user, String oldPassword) {
		return bCryptPasswordEncoder.matches(oldPassword, user.getPassword());
	}
	
	public User updatePassword(String username, String newPassword) {
		User u = userRepository.findByUsername(username);
		if (u == null) {
			return null;
		}
		u.setPassword(bCryptPasswordEncoder.encode(newPassword));
		return userRepository.save(u);
	}
}
