package ftn.xmlwebservisi.firme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ftn.xmlwebservisi.firme.model.User;
import ftn.xmlwebservisi.firme.model.UserPrincipal;
import ftn.xmlwebservisi.firme.repository.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
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

}
