package security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import service.UserService;


/*
 * Processes any HTTP request that has a header of Authorization with an authentication scheme of Bearer
 * If authentication is successful, the resulting Authentication object will be placed into the SecurityContextHolder.
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
	
	private Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
	@Autowired
	private TokenHandler tokenHandler;
	@Autowired
	private UserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		logger.debug("Looking for jwt header...");
		String jwtHeader = request.getHeader("Authorization");
		
		if (jwtHeader == null) {
			logger.debug("header doesn't exist");
			filterChain.doFilter(request, response);
			return;
		}
		
		logger.debug("Parsing jwt token...");
		String username = tokenHandler.getUsername(jwtHeader);
		if (username != null) {
			logger.debug("Successfully parsed user from the token");
			UserDetails userDetails = userService.loadUserByUsername(username);
			CustomAuthenticationToken authToken = new CustomAuthenticationToken(userDetails, jwtHeader);
			SecurityContextHolder.getContext().setAuthentication(authToken);
			logger.debug("User " + username + " successfully added to the spring security context");
		}
		
		filterChain.doFilter(request, response);
	}
}
