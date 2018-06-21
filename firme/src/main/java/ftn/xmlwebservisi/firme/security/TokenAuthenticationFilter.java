package ftn.xmlwebservisi.firme.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import ftn.xmlwebservisi.firme.service.UserService;

/*
 * Processes any HTTP request that has a header of Authorization with an authentication scheme of Bearer
 * If authentication is successful, the resulting Authentication object will be placed into the SecurityContextHolder.
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private final String AUTH_HEADER = "Authorization";
	private final String AUTH_SCHEME = "Bearer";

	@Autowired
	private TokenHandler tokenHandler;
	@Autowired
	private UserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = request.getHeader(AUTH_HEADER);
		if (token == null || !token.startsWith(AUTH_SCHEME)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		token = token.substring(7);
		String username = tokenHandler.getUsername(token);
		if (username != null) {
			UserDetails userDetails = userService.loadUserByUsername(username);
			CustomAuthenticationToken authToken = new CustomAuthenticationToken(userDetails, token);
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		filterChain.doFilter(request, response);
	}
}
