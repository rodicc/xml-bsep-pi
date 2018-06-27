package ftn.xmlwebservisi.firme.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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

	@Autowired
	private TokenHandler tokenHandler;
	@Autowired
	private UserService userService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		Cookie[] cookies = request.getCookies();
		String jwt = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("jjwt")) {
					jwt = cookie.getValue();
				}
			}
		}
		
		if (cookies == null || jwt == null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String username = tokenHandler.getUsername(jwt);
		if (username != null) {
			UserDetails userDetails = userService.loadUserByUsername(username);
			CustomAuthenticationToken authToken = new CustomAuthenticationToken(userDetails, jwt);
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		filterChain.doFilter(request, response);
	}
}
