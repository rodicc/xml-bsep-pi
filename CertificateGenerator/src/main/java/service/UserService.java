package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import model.User;
import model.UserPrincipal;
import repository.RoleRepository;
import repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	private Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	
	// Retrieving user-related data from a database
	// Throws UsernameNotFoundException if user could not be found
	// Returns populated UserPrincipal object
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);
		if (user == null) {
			logger.error("User with username " + username + " could not be found");
			throw new UsernameNotFoundException("User with username " + username + " could not be found.");
		}
		return new UserPrincipal(user);
	}
	
}
