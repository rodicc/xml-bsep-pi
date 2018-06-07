package ftn.xmlwebservisi.firme.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;

import ftn.xmlwebservisi.firme.dto.UserDTO;

/*
 * This filter will intercept a request and attempt to perform authentication from that request 
 * if the request matches the "/login" pattern and http POST method
 * Authentication is performed by the attemptAuthentication method
 */
public class LoginProcessingFilter extends UsernamePasswordAuthenticationFilter {
	
	// AuthenticationManager is required to process the authentication request tokens
	private AuthenticationManager authenticationManager;
	
	public LoginProcessingFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		try {
			// Extracting request parameters
			UserDTO user = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);
			
			String username = user.getUsername();
			if (username == null || username.trim().isEmpty()) {
				throw new PreAuthenticatedCredentialsNotFoundException("Username cannot be null or empty");
			}
			username = username.trim();
			
			String password = user.getPassword();
			if (password == null) {
				throw new PreAuthenticatedCredentialsNotFoundException("Password cannot be null");
			}
			
			UsernamePasswordAuthenticationToken authRequest = 
					new UsernamePasswordAuthenticationToken(username, password);
			
			return authenticationManager.authenticate(authRequest);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	
}
