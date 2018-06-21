package ftn.xmlwebservisi.firme.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
import ftn.xmlwebservisi.firme.model.User;

/*
 * This filter will intercept a request and attempt to perform authentication from that request 
 * if the request matches the "/login" pattern and HTTP POST method
 * Authentication is performed by the attemptAuthentication method
 */
public class LoginProcessingFilter extends UsernamePasswordAuthenticationFilter {
	
	private final String AUTH_HEADER = "Authorization";
	private final String AUTH_SCHEME = "Bearer";
	
	// AuthenticationManager is required to process the authentication request tokens
	private AuthenticationManager authenticationManager;
	
	private TokenHandler tokenHandler;
	
	public LoginProcessingFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		this.tokenHandler = new TokenHandler();
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
			
			super.setDetails(request, authRequest);
			
			return authenticationManager.authenticate(authRequest);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		User user = (User)authResult.getPrincipal();
		String token = tokenHandler.generateToken(user.getUsername());
		response.addHeader(AUTH_HEADER, AUTH_SCHEME + " " + token);
		response.addHeader("User", user.getUsername());
	}

	
}
